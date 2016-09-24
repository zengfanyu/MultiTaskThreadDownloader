package com.zfy.downloadkit.architecture;

import com.zfy.downloadkit.DownloadException;

/**
 * 建筑者模式中的IBuilder建筑抽象类
 * 下载的回应信息
 * Created by zfy on 2016/8/27.
 */
public interface IDownloadResponse {

    /*
    * 开始连接
    * */
    void onStarted();

    /*
    * 正在连接
    * */
    void onConnecting();

    /*
    * 已经链接
    * */
    void onConnected(long time, long total, boolean isAcceptRanges);

    /*
    * 连接失败
    * */
    void onConnectFailed(DownloadException e);

    /*
    *取消连接
    * */
    void onConnectCanceled();

   /*
   * 正在下载
   * */
    void onDownloadProgress(long finished, long total, int percent);

    /*
    * 完成下载
    * */
    void onDownloadCompleted();

    /*
    * 暂停下载
    * */
    void onDownloadPaused();

    /*
    * 取消下载
    * */
    void onDownloadCanceled();

    /*
    * 下载失败
    * */
    void onDownloadFailed(DownloadException e);


}
