package com.zfy.downloadkit;

import java.net.HttpURLConnection;

/**
 * $desc
 * Created by zfy on 2016/8/27.
 */
public interface CallBack {

    void onStart();

    /**
     *<p> 被ConnectTaskImpl 类回调的第一个方法
     */
    void onConnecting();


    /**
     * <p>如果被ConnectTaskImpl成功的连接上服务器,此方法就会被回调
     * 如果没有,DownloadException就会被回调
     *
     * @param total 文件的总长度 {@link HttpURLConnection#getContentLength()}
     * @param isRangeSupport 标记是否断点续存,即从pause的地方重新开始下载
     */
    void onConnected(long total, boolean isRangeSupport);

    /**
     * <p>正在下载的回调
     *
     * @param finished 已经下载的文件的长度
     * @param total 文件的总长度 和 {@link CallBack#onConnected(long, boolean)}中参数相同
     * @param progress 下载的进度 (finished/total)*100
     */
    void onProgress(long finished, long total, int progress);

    /**
     * <p>完成下载的回调
     */
    void onCompleted();

    /**
     * <p>如果成功调用DownloadManager的pause(string),或者DownloadManager的pauseAll()方法
     * 此方法就会被调用
     */
    void onDownloadPaused();

    /**
     *<p>如果成功调用DownloadManager的cancel(string),或者DownloadManager的cancelAll()方法
     * 此方法就会被调用
     */
    void onDownloadCanceled();

    /**
     * <p>下载失败或者是出现异常,回调此方法
     *
     * @param e 下载异常
     */
    void onFailed(DownloadException e);


}
