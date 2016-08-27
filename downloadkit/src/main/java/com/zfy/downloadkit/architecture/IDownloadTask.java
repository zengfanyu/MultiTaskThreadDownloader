package com.zfy.downloadkit.architecture;

/**
 * 下载任务的接口
 * Created by zfy on 2016/8/27.
 */
public interface IDownloadTask extends Runnable {

    interface onDownloadListener {

//        void onDownloadConnecting();

        void onDownloadProgress(long finished, long length);

        void onDownloadCompleted();

        void onDownloadPaused();

        void onDownloadCanceled();

        void onDownloadFailed();


    }

    void cancel();

    void pause();

    boolean isDownloading();

    boolean isComplete();

    boolean isPaused();

    boolean isCanceled();

    boolean isFailed();

    @Override
    void run();


}
