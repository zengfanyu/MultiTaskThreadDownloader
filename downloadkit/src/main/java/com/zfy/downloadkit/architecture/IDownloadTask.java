package com.zfy.downloadkit.architecture;

import com.zfy.downloadkit.DownloadException;

/**
 * 建筑者模式中的IBuilder建筑抽象类
 * 下载任务的接口
 * Created by zfy on 2016/8/27.
 */
public interface IDownloadTask extends Runnable {

    /*
    * 下载状态的监听器
    * */
    interface onDownloadListener {
        //正在下载的回调接口
        void onDownloadProgress(long finished, long length);
        //下载完成的回调接口
        void onDownloadCompleted();
        //下载暂停的回调接口
        void onDownloadPaused();
        //下载取消的回调接口
        void onDownloadCanceled();
        //下载失败的回调接口
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
