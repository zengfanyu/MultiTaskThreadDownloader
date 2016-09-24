package com.zfy.downloadkit.core_architecture_Impl;

import com.zfy.downloadkit.DownloadConfiguration;
import com.zfy.downloadkit.DownloadException;
import com.zfy.downloadkit.DownloadRequest;
import com.zfy.downloadkit.Moudle.DownloadInfo;
import com.zfy.downloadkit.Moudle.ThreadInfo;
import com.zfy.downloadkit.architecture.DownloadStatus;
import com.zfy.downloadkit.architecture.IConnectTask;
import com.zfy.downloadkit.architecture.IDownloadResponse;
import com.zfy.downloadkit.architecture.IDownloadTask;
import com.zfy.downloadkit.architecture.IDownloader;
import com.zfy.downloadkit.db.DatabaseManager;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * 建造者模式中的BuilderImpl
 * Created by zfy on 2016/8/29.
 */
public class DownloaderImpl implements IDownloader, IDownloadTask.onDownloadListener, IConnectTask.onConnectListener {

    private DownloadRequest mRequest;
    private IDownloadResponse mResponse;
    private Executor mExecutor;
    private DatabaseManager mDatabaseManager;
    private String mTag;
    private DownloadConfiguration mConfiguration;
    private OnDownloaderDestroyedListener mListener;
    private int mStatus;
    private DownloadInfo mDownloadInfo;
    private IConnectTask mIConnectTask;
    private List<IDownloadTask> mIDownloadTasks;

    //构造方法
    public DownloaderImpl(DownloadRequest request,
                          IDownloadResponse response,
                          Executor executor,
                          DatabaseManager dbManager,
                          String key,
                          DownloadConfiguration config,
                          OnDownloaderDestroyedListener listener) {
        mRequest = request;
        mResponse = response;
        mExecutor = executor;
        mDatabaseManager = dbManager;
        mTag = key;
        mConfiguration = config;
        mListener = listener;

        init();
    }

    private void init() {
        mDownloadInfo = new DownloadInfo(mRequest.getName().toString(), mRequest.getUrl(), mRequest.getFolder());
        mIDownloadTasks = new LinkedList<>();
    }


    @Override
    public boolean isRunning() {
        return mStatus == DownloadStatus.STATUS_STARTED
                || mStatus == DownloadStatus.STATUS_CONNECTING
                || mStatus == DownloadStatus.STATUS_CONNECTED
                || mStatus == DownloadStatus.STATUS_PROGRESS;
    }

    @Override
    public void start() {
        mStatus = DownloadStatus.STATUS_STARTED;
        mResponse.onStarted();
        mIConnectTask = new ConnectTaskImpl(mRequest.getUrl(), this);
        mExecutor.execute(mIConnectTask);


    }

    @Override
    public void pause() {
        if (mIConnectTask != null) {
            mIConnectTask.pause();
        }
        for (IDownloadTask task : mIDownloadTasks) {
            task.pause();
        }
        if (mStatus != DownloadStatus.STATUS_PROGRESS) {
            onDownloadPaused();
        }
    }

    @Override
    public void cancel() {
        if (mIConnectTask != null) {
            mIConnectTask.cancel();
        }
        for (IDownloadTask task : mIDownloadTasks) {
            task.cancel();
        }
        if (mStatus != DownloadStatus.STATUS_PROGRESS) {
            onDownloadCanceled();
        }


    }

    @Override
    public void onDestroy() {
        //回调OnDestroy接口 通知DownloadManager
        mListener.onDestroyed(mTag, this);
    }

    @Override //onConnectListener的回调
    public void onConnecting() {
        mStatus = DownloadStatus.STATUS_CONNECTING;
        mResponse.onConnecting();

    }

    @Override//onConnectListener的回调
    public void onConnected(long time, long length, boolean isAcceptRanges) {
        if (mIConnectTask.isCanceled()) {
            //// despite connection is finished, the entire downloader is canceled
            onConnectCanceled();
        } else {
            mStatus = DownloadStatus.STATUS_CONNECTED;
            mResponse.onConnected(time, length, isAcceptRanges);

            mDownloadInfo.setAcceptRanges(isAcceptRanges);
            mDownloadInfo.setLength(length);
            download(length, isAcceptRanges);
        }

    }

    @Override//onConnectListener的回调
    public void onConnectPaused() {
        onDownloadPaused();

    }

    @Override//onConnectListener的回调
    public void onConnectCanceled() {
        //删除数据库中的数据
        deleteFromDB();
        //删除本地文件的数据
        deleteFile();
        mStatus = DownloadStatus.STATUS_CANCELED;
        mResponse.onConnectCanceled();
        onDestroy();


    }

    @Override//onConnectListener的回调
    public void onConnectFailed(DownloadException e) {
        if (mIConnectTask.isCanceled()) {
            // despite connection is failed, the entire downloader is canceled
            onConnectCanceled();
        } else if (mIConnectTask.isPaused()) {
            // despite connection is failed, the entire downloader is paused
            onDownloadPaused();
        } else {
            mStatus = DownloadStatus.STATUS_FAILED;
            mResponse.onConnectFailed(e);
            onDestroy();
        }

    }


    @Override//onDownloadListener的回调
    public void onDownloadProgress(long finished, long length) {
        //calculate percent
        final int percent = (int) (finished * 100 / length);
        mResponse.onDownloadProgress(finished, length, percent);


    }

    @Override//onDownloadListener的回调
    public void onDownloadCompleted() {
        if (isAllComplete()) {
            deleteFromDB();
            mStatus = DownloadStatus.STATUS_COMPLETED;
            mResponse.onDownloadCompleted();
            onDestroy();
        }

    }

    @Override//onDownloadListener的回调
    public void onDownloadPaused() {
        if (isAllPaused()) {
            mStatus = DownloadStatus.STATUS_PAUSED;
            mResponse.onDownloadPaused();
            onDestroy();
        }

    }

    @Override//onDownloadListener的回调
    public void onDownloadCanceled() {
        if (isAllCanceled()) {
            //删除数据库中的ThreadInfo
            deleteFromDB();
            //删除本地已经下载的文件
            deleteFile();
            mStatus = DownloadStatus.STATUS_CANCELED;
            mResponse.onDownloadCanceled();
            onDestroy();
        }

    }

    @Override//onDownloadListener的回调
    public void onDownloadFailed(DownloadException e) {
        if (isAllFailed()) {
            mStatus = DownloadStatus.STATUS_FAILED;
            mResponse.onDownloadFailed(e);
            onDestroy();
        }

    }

    private boolean isAllComplete() {
        return false;
    }

    private boolean isAllPaused() {
        return false;
    }

    private boolean isAllCanceled() {
        return false;
    }

    private boolean isAllFailed() {
        return false;
    }

    /*
    * Method to delete local file
    * */
    private void deleteFile() {
        File file = new File(mDownloadInfo.getDir(), mDownloadInfo.getName());
        if (file.exists() && file.isFile()) {
            file.delete();
        }
    }

    /*
    * Method to delete DB
    * */
    private void deleteFromDB() {
        mDatabaseManager.delete(mTag);

    }

    private void download(long length, boolean isAcceptRanges) {
        mStatus = DownloadStatus.STATUS_PROGRESS;
        initDownloadTasks(length, isAcceptRanges);
        //start tasks
        for (IDownloadTask task : mIDownloadTasks) {
            mExecutor.execute(task);
        }
    }

    private void initDownloadTasks(long length, boolean isAcceptRanges) {
        mIDownloadTasks.clear();
        if (isAcceptRanges) {
            List<ThreadInfo> threadInfos = getMultiThreadInfos(length);
            //初始化finished
            int finished = 0;
            for (ThreadInfo info : threadInfos) {
                finished += info.getFinished();
            }
            mDownloadInfo.setFinished(finished);

            for (ThreadInfo info : threadInfos) {
                mIDownloadTasks.add(new MultiDownloadTask(mDownloadInfo, info, mDatabaseManager, this));
            }

        } else {
            ThreadInfo info = getSingleThreadInfo();
            mIDownloadTasks.add(new SingleDownloadTask(mDownloadInfo, info, this));

        }


    }

    private ThreadInfo getSingleThreadInfo() {
        ThreadInfo threadInfo = new ThreadInfo(0, mTag, mRequest.getUrl(), 0);
        return threadInfo;

    }

    private List<ThreadInfo> getMultiThreadInfos(long length) {
        //从数据库中初始化ThreadInfo
        // init threadInfo from db
        final List<ThreadInfo> threadInfos = mDatabaseManager.getThreadInfos(mTag);
        if (threadInfos.isEmpty()) {
            final int threadNum = mConfiguration.getThreadNum();
            for (int i = 0; i < threadNum; i++) {
                // calculate average
                final long average = length / threadNum;
                final long start = average * i;
                final long end;
                if (i == threadNum - 1) {
                    end = length;
                } else {
                    end = start + average - 1;
                }
                ThreadInfo threadInfo = new ThreadInfo(i, mTag, mRequest.getUrl(), start, end, 0);
                threadInfos.add(threadInfo);
            }
        }
        return threadInfos;


    }


}
