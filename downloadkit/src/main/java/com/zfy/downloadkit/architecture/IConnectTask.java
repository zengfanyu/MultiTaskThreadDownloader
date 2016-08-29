package com.zfy.downloadkit.architecture;

import com.zfy.downloadkit.DownloadException;

/**
 * `连接任务的接口
 * Created by zfy on 2016/8/27.
 */
public interface IConnectTask extends Runnable {

    /*
    * 监听器,监听连接的状态
    * */
    interface onConnectListener{

        void onConnecting(); //DownloadStatus.STATUS_CONNECTING run方法一进去,回调

        void onConnected(long time, long length, boolean isAcceptRanges); // DownloadStatus.STATUS_CONNECTED;回调

        void onConnectPaused(); //DownloadStatus.STATUS_PAUSED连接异常时回调

        void onConnectCanceled();//DownloadStatus.STATUS_CANCELED连接异常时回调

        void onConnectFailed(DownloadException e); //DownloadStatus.STATUS_FAILED;连接异常时回调
    }

    void pause();


    void cancel();

    boolean isConnecting();

    boolean isConnected();

    boolean isPaused();

    boolean isCanceled();

    boolean isFailed();

    @Override
    void run();





}
