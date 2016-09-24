package com.zfy.downloadkit.architecture;

/**
 * 建筑者模式中的Director的抽象类
 * Created by zfy on 2016/8/27.
 */
public interface IDownloader {

    /*
    * 下载结束的监听器
    * */
    interface OnDownloaderDestroyedListener {
        void onDestroyed(String key, IDownloader IDownloader);
    }

    boolean isRunning();

    void start();

    void pause();

    void cancel();

    void onDestroy();
}
