package com.zfy.downloadkit.architecture;

import com.zfy.downloadkit.DownloadException;

/**
 * 下载任务的接口
 * Created by zfy on 2016/8/27.
 */
public interface IDownloadTask extends Runnable {

    interface onDownloadListener {

        void onDownloadConnecting();

        void onDownloadProgress(long finished, long length);

        void onDownloadCompleted();

        void onDownloadPaused();

        void onDownloadCanceled();

        void onDownloadFailed(DownloadException e);


    }

    void cancel();

    void pause();

    boolean isDownloading();

    boolean isCompleted();

    boolean isPaused();

    boolean isCanceled();

    boolean isFailed();

    @Override
    void run();


}
