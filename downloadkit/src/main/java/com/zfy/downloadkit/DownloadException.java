package com.zfy.downloadkit;

/**
 * 下载异常类
 * Created by zfy on 2016/8/27.
 */
public class DownloadException extends Exception {

    private int errorCode;
    private String errorMessage;

    public DownloadException() {
    }

    public DownloadException(String detailMessage) {
        super(detailMessage);
        this.errorMessage = detailMessage;
    }

    public DownloadException(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public DownloadException(int errorCode, Throwable throwable) {
        super(throwable);
        this.errorCode = errorCode;
    }

    public DownloadException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
        this.errorMessage = detailMessage;
    }


    public DownloadException(Throwable throwable, int errorCode, String errorMessage) {
        super(throwable);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public DownloadException(Throwable throwable) {
        super(throwable);
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
