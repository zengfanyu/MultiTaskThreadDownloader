package com.zfy.downloadkit.architecture;

/**
 * 与DownloadManager相关联的类,DownloadManager用Downloader 管理DownloadTask
 * Created by zfy on 2016/8/27.
 */
public interface IDownloader {

    interface OnDownloaderDestroyedListener {
        void onDestroyed(String key, IDownloader IDownloader);
    }

    boolean isRunning();

    void start();

    void pause();

    void cancel();

    void onDestroy();
}
