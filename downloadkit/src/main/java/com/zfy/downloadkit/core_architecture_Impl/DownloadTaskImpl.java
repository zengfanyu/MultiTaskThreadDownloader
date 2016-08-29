package com.zfy.downloadkit.core_architecture_Impl;

import android.os.Process;
import android.text.TextUtils;

import com.zfy.downloadkit.Constants;
import com.zfy.downloadkit.DownloadException;
import com.zfy.downloadkit.Moudle.DownloadInfo;
import com.zfy.downloadkit.Moudle.ThreadInfo;
import com.zfy.downloadkit.Utils.IOCloseUtils;
import com.zfy.downloadkit.architecture.DownloadStatus;
import com.zfy.downloadkit.architecture.IDownloadTask;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;

/**
 * IDownloadTask接口的 实现类
 * Created by zfy on 2016/8/29.
 */
public abstract class DownloadTaskImpl implements IDownloadTask {
    private String mTag;
    private final DownloadInfo mDownloadInfo;
    private  final ThreadInfo mThreadInfo;
    private final onDownloadListener mOnDownloadListener;

    private volatile int mStatus;

    private volatile int mCommend = 0;

    public DownloadTaskImpl(DownloadInfo downloadInfo, ThreadInfo threadInfo, onDownloadListener onDownloadListener) {
        mDownloadInfo = downloadInfo;
        mThreadInfo = threadInfo;
        mOnDownloadListener = onDownloadListener;

        this.mTag=getTag();

        if (TextUtils.isEmpty(mTag)){
            mTag=this.getClass().getSimpleName();
        }

    }

    @Override
    public void cancel() {
        mStatus= DownloadStatus.STATUS_CANCELED;

    }

    @Override
    public void pause() {
        mStatus= DownloadStatus.STATUS_PAUSED;

    }

    @Override
    public boolean isDownloading() {
        return mStatus==DownloadStatus.STATUS_PROGRESS;
    }

    @Override
    public boolean isCompleted() {
        return mStatus==DownloadStatus.STATUS_COMPLETED;
    }

    @Override
    public boolean isPaused() {
        return mStatus==DownloadStatus.STATUS_PAUSED;
    }

    @Override
    public boolean isCanceled() {
        return mStatus==DownloadStatus.STATUS_CANCELED;
    }

    @Override
    public boolean isFailed() {
        return mStatus==DownloadStatus.STATUS_FAILED;
    }

    @Override
    public void run() {
        android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        try {
            //将线程信息存入数据库
            insertIntoDB(mThreadInfo);
            //执行下载操作
            executeDownload();
            synchronized (mOnDownloadListener){
                mStatus=DownloadStatus.STATUS_COMPLETED;
                //回调下载完成的方法
                mOnDownloadListener.onDownloadCompleted();
            }
        } catch (DownloadException e) {
            handleDownloadException(e);
        }

    }

    private void handleDownloadException(DownloadException e) {
        switch (e.getErrorCode()) {
            case DownloadStatus.STATUS_FAILED:
                synchronized (mOnDownloadListener) {
                    mStatus = DownloadStatus.STATUS_FAILED;
                    mOnDownloadListener.onDownloadFailed(e);
                }
                break;
            case DownloadStatus.STATUS_PAUSED:
                synchronized (mOnDownloadListener) {
                    mStatus = DownloadStatus.STATUS_PAUSED;
                    mOnDownloadListener.onDownloadPaused();
                }
                break;
            case DownloadStatus.STATUS_CANCELED:
                synchronized (mOnDownloadListener) {
                    mStatus = DownloadStatus.STATUS_CANCELED;
                    mOnDownloadListener.onDownloadCanceled();
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown state");
        }

    }

    private void executeDownload()throws DownloadException{
        final URL url;

        try {
            url=new URL(mThreadInfo.getUrl());
        } catch (MalformedURLException e) {
            throw new DownloadException(DownloadStatus.STATUS_FAILED,"worse url",e);
        }

        HttpURLConnection connection=null;

        try {
            connection= (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(Constants.HTTP.CONNECT_TIME_OUT);
            connection.setReadTimeout(Constants.HTTP.READ_TIME_OUT);
            connection.setRequestMethod(Constants.HTTP.GET);
            //getHttpHeaders(mThreadInfo) 抽象方法,由子类实现
            setHttpHeader(getHttpHeaders(mThreadInfo), connection);
            final int responseCode = connection.getResponseCode();
            //getResponseCode()抽象方法,由子类实现
            if (responseCode== getResponseCode()) {
                transferData(connection);
            }else {
                throw new DownloadException(DownloadStatus.STATUS_FAILED,"UnSupported response code:"+responseCode);
            }
        }  catch (ProtocolException e) {
            throw new DownloadException(DownloadStatus.STATUS_FAILED, "Protocol error", e);
        } catch (IOException e) {
            throw new DownloadException(DownloadStatus.STATUS_FAILED, "IO error", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }


    }

    private void transferData(HttpURLConnection connection) throws DownloadException {
        InputStream inputStream=null;
        RandomAccessFile raf=null;

        try {
            try {
                inputStream=connection.getInputStream();

            } catch (IOException e) {
                throw new DownloadException(DownloadStatus.STATUS_FAILED, "HTTP get inputStream error", e);
            }

            final long offset=mThreadInfo.getStart()+mThreadInfo.getFinished();

            try {
                raf=getFile(mDownloadInfo.getDir(),mDownloadInfo.getName(),offset);
            } catch (IOException e) {
                throw new DownloadException(DownloadStatus.STATUS_FAILED,"File error",e);
            }

            transferData(inputStream,raf);
        } finally {
            try {
                IOCloseUtils.close(inputStream);
                IOCloseUtils.close(raf);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    private void transferData(InputStream inputStream, RandomAccessFile raf)throws DownloadException{
        final byte[]buffer=new byte[1024*8];
        while(true){
            checkPausedOrCanceled();
            int len=-1;

            try {
                len=inputStream.read(buffer);
                if (len==-1) {
                    break;
                }
                raf.write(buffer,0,len);
                mThreadInfo.setFinished(mThreadInfo.getFinished()+len);
                synchronized (mOnDownloadListener){
                    mDownloadInfo.setFinished(mDownloadInfo.getFinished()+len);
                    //回调
                    mOnDownloadListener.onDownloadProgress(mDownloadInfo.getFinished(),mDownloadInfo.getLength());
                }
            } catch (IOException e) {
                updateDB(mThreadInfo);
                throw new DownloadException(DownloadStatus.STATUS_FAILED,e);
            }
        }

    }

    private void checkPausedOrCanceled() throws DownloadException {
        if (mCommend == DownloadStatus.STATUS_CANCELED) {
            // cancel
            throw new DownloadException(DownloadStatus.STATUS_CANCELED, "Download canceled!");
        } else if (mCommend == DownloadStatus.STATUS_PAUSED) {
            // pause
            updateDB(mThreadInfo);
            throw new DownloadException(DownloadStatus.STATUS_PAUSED, "Download paused!");
        }
    }

    private void setHttpHeader(Map<String, String> httpHeaders, HttpURLConnection connection) {
        if (httpHeaders!=null){
            for (String key:httpHeaders.keySet()){
                connection.setRequestProperty(key,httpHeaders.get(key));
            }
        }
    }


    protected abstract void updateDB(ThreadInfo threadInfo);

    protected abstract RandomAccessFile getFile(File dir, String name, long offset)throws IOException;

    protected abstract int getResponseCode();

    protected abstract Map<String,String> getHttpHeaders(ThreadInfo threadInfo);

    protected abstract void insertIntoDB(ThreadInfo threadInfo);
    protected abstract String getTag();
}
