package com.zfy.downloadkit.architecture;

/**
 * $desc
 * Created by zfy on 2016/8/27.
 */
public interface IDownloadStatusDelivery {

    /*
    * 传递下载状态的方法
    * */
    void post(DownloadStatus status);
}
