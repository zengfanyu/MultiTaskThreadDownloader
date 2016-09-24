package com.zfy.downloadkit.architecture;

import com.zfy.downloadkit.CallBack;
import com.zfy.downloadkit.DownloadException;

/**
 * 下载状态 的类
 * Created by zfy on 2016/8/27.
 */
public class DownloadStatus {

    /*
    * 1.开始连接的状态码
    * */
    public static final int STATUS_STARTED = 1111;
    /*
    * 2.正在连接的状态码
    * */
    public static final int STATUS_CONNECTING = 2222;
    /*
    * 3.已经连接的状态码
    * */
    public static final int STATUS_CONNECTED = 3333;
    /*
    * 4.正在下载连接的状态码
    * */
    public static final int STATUS_PROGRESS = 4444;
    /*
    * 5.完成下载的状态码
    * */
    public static final int STATUS_COMPLETED = 5555;
    /*
    * 6.暂停下载的状态码
    * */
    public static final int STATUS_PAUSED = 6666;
    /*
    * 7.取消下载的状态码
    * */
    public static final int STATUS_CANCELED = 7777;
    /*
    * 8.失败下载的状态码
    * */
    public static final int STATUS_FAILED = 8888;





    /*
    * 下载状态
    * */
    private int status;
    /*
    * 下载异常
    * */
    private DownloadException exception;
    /*
    * 下载回调
    * */
    private CallBack callBack;
    /*
    * 连接时间
    * */
    private long time;
    /*
    * 文件总长度
    * */
    private long total;
    /*
    * 已经下载的文件的长度
    * */
    private long finished;
    /*
    * 下载的进度 (finished/total)*100
    * */
    private int percent;
    /*
    * 标记是否断点续存,即从pause的地方重新开始下载
    * */
    private boolean isAcceptRanges;


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public DownloadException getException() {
        return exception;
    }

    public void setException(DownloadException exception) {
        this.exception = exception;
    }

    public CallBack getCallBack() {
        return callBack;
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getFinished() {
        return finished;
    }

    public void setFinished(long finished) {
        this.finished = finished;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public boolean isAcceptRanges() {
        return isAcceptRanges;
    }

    public void setIsAcceptRanges(boolean isAcceptRanges) {
        this.isAcceptRanges = isAcceptRanges;
    }
}
