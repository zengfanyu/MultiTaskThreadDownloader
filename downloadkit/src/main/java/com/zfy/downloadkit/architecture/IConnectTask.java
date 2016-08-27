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

        void onConnecting();

        void onConnected(long time, long length, boolean isAcceptRanges);

        void onConnectPaused();

        void onConnectCanceled();

        void onConnectFailed(DownloadException e);
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
