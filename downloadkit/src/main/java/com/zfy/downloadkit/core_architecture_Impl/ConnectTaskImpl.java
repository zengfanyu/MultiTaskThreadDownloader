package com.zfy.downloadkit.core_architecture_Impl;

import android.os.Process;
import android.text.TextUtils;

import com.zfy.downloadkit.Constants;
import com.zfy.downloadkit.DownloadException;
import com.zfy.downloadkit.architecture.DownloadStatus;
import com.zfy.downloadkit.architecture.IConnectTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * 建造者模式中的BuilderImpl
 * IConnectTask接口的实现类
 * Created by zfy on 2016/8/27.
 */
public class ConnectTaskImpl implements IConnectTask {
    //使用final关键字之后,一旦被初始化了,其值就不会被改变
    private final String mUrl;
    private final onConnectListener mOnConnectListener;
    //volatile:线程在每次使用变量的时候，都会读取变量修改后的最的值,保证了内存可见性
    //对单个变量的读写,具有操作原子性!
    private volatile int mStatus;
    private volatile long mStartTime;

    public ConnectTaskImpl(String url, onConnectListener onConnectListener) {
        mUrl = url;
        mOnConnectListener = onConnectListener;
    }


    @Override
    public void pause() {
        mStatus = DownloadStatus.STATUS_PAUSED;
    }

    @Override
    public void cancel() {
        mStatus = DownloadStatus.STATUS_CANCELED;
    }

    @Override
    public boolean isConnecting() {
        return mStatus == DownloadStatus.STATUS_CONNECTING;
    }

    @Override
    public boolean isConnected() {
        return mStatus == DownloadStatus.STATUS_CONNECTED;
    }

    @Override
    public boolean isPaused() {
        return mStatus == DownloadStatus.STATUS_PAUSED;
    }

    @Override
    public boolean isCanceled() {
        return mStatus == DownloadStatus.STATUS_CANCELED;
    }

    @Override
    public boolean isFailed() {
        return mStatus == DownloadStatus.STATUS_FAILED;
    }

    @Override
    public void run() {
        //设置线程的优先级 防止后台下载线程造成CPU使用过度，导致UI线程中的线程受到影响
        android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        //改变下载状态
        mStatus = DownloadStatus.STATUS_CONNECTING;
        //回调方法
        mOnConnectListener.onConnecting();
        try {
            //执行连接
            executeConnection();
        } catch (DownloadException e) {
            handleDownloadException(e);
        }

    }

    private void executeConnection() throws DownloadException {
        mStartTime = System.currentTimeMillis();
        HttpURLConnection connection = null;
        final URL url;
        try {
            url = new URL(mUrl);
        } catch (MalformedURLException e) {
            throw new DownloadException(DownloadStatus.STATUS_FAILED, "Worse url", e);
        }

        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(Constants.HTTP.CONNECT_TIME_OUT);
            connection.setReadTimeout(Constants.HTTP.READ_TIME_OUT);
            connection.setRequestMethod(Constants.HTTP.GET);
            connection.setRequestProperty("Range", "bytes=" + 0 + "-");
            final int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                //请求全部成功,这时候,不接受断点续存
                parseResponse(connection, false);
            }
            if (responseCode == HttpURLConnection.HTTP_PARTIAL) {
                //请求部分成功,这时候,接受断点续存
                parseResponse(connection, true);
            } else {
                throw new DownloadException(DownloadStatus.STATUS_FAILED, "UnSupported response code:" + responseCode);
            }
        } catch (ProtocolException e) {
            throw new DownloadException(DownloadStatus.STATUS_FAILED, "Protocol error", e);
        } catch (IOException e) {
            throw new DownloadException(DownloadStatus.STATUS_FAILED, "IO error", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private void parseResponse(HttpURLConnection connection, boolean isAcceptRange) throws DownloadException {
        final long length;
        String contentLength = connection.getHeaderField("Content-Length");

        if (TextUtils.isEmpty(contentLength) || contentLength.equals(0) || contentLength.equals(-1)) {
            length = connection.getContentLength();
        } else {
            length = Long.parseLong(contentLength);
        }

        if (length < 0) {
            throw new DownloadException(DownloadStatus.STATUS_FAILED, "length<=0");
        }
        //检查是否被取消和暂停
        checkCanceledOrPaused();
        //链接成功
        mStatus = DownloadStatus.STATUS_CONNECTED;
        final long timeDelta = System.currentTimeMillis() - mStartTime;
        //回调
        mOnConnectListener.onConnected(timeDelta, length, isAcceptRange);
    }

    private void checkCanceledOrPaused() throws DownloadException {
        if (isCanceled()) {
            // cancel
            throw new DownloadException(DownloadStatus.STATUS_CANCELED, "Connection Canceled!");
        } else if (isPaused()) {
            // paused
            throw new DownloadException(DownloadStatus.STATUS_PAUSED, "Connection Paused!");
        }
    }

    //做好并发同步处理
    private void handleDownloadException(DownloadException e) {
        switch (e.getErrorCode()) {
            case DownloadStatus.STATUS_PAUSED:
                synchronized (mOnConnectListener) {
                    mStatus = DownloadStatus.STATUS_PAUSED;
                    mOnConnectListener.onConnectPaused();
                }
                break;
            case DownloadStatus.STATUS_CANCELED:
                synchronized (mOnConnectListener) {
                    mStatus = DownloadStatus.STATUS_CANCELED;
                    mOnConnectListener.onConnectCanceled();
                }
                break;
            case DownloadStatus.STATUS_FAILED:
                synchronized (mOnConnectListener) {
                    mStatus = DownloadStatus.STATUS_FAILED;
                    mOnConnectListener.onConnectFailed(e);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown state");
        }

    }
}
