package com.zfy.downloadkit.core_architecture_Impl;

import com.zfy.downloadkit.CallBack;
import com.zfy.downloadkit.DownloadException;
import com.zfy.downloadkit.architecture.DownloadStatus;
import com.zfy.downloadkit.architecture.IDownloadResponse;
import com.zfy.downloadkit.architecture.IDownloadStatusDelivery;

/**
 * IDownloadResponse接口 的实现类
 * Created by zfy on 2016/8/27.
 */
public class DownloadResponseImpl implements IDownloadResponse {

    private IDownloadStatusDelivery mDelivery;

    private DownloadStatus mDownloadStatus;

    public DownloadResponseImpl(IDownloadStatusDelivery delivery, CallBack callBack) {
        mDelivery = delivery;
        mDownloadStatus = new DownloadStatus();
        mDownloadStatus.setCallBack(callBack);
    }

    @Override
    public void onStarted() {
        mDownloadStatus.setStatus(DownloadStatus.STATUS_STARTED);
        mDownloadStatus.getCallBack().onStart();

    }

    @Override
    public void onConnecting() {
        mDownloadStatus.setStatus(DownloadStatus.STATUS_CONNECTING);
        mDelivery.post(mDownloadStatus);

    }

    @Override
    public void onConnected(long time, long total, boolean isAcceptRanges) {
        mDownloadStatus.setStatus(DownloadStatus.STATUS_CONNECTED);
        mDownloadStatus.setTime(time);
        mDownloadStatus.setIsAcceptRanges(isAcceptRanges);
        mDelivery.post(mDownloadStatus);


    }

    @Override
    public void onConnectFailed(DownloadException e) {
        mDownloadStatus.setStatus(DownloadStatus.STATUS_FAILED);
        mDownloadStatus.setException(e);
        mDelivery.post(mDownloadStatus);


    }

    @Override
    public void onConnectCanceled() {
        mDownloadStatus.setStatus(DownloadStatus.STATUS_CANCELED);
        mDelivery.post(mDownloadStatus);

    }

    @Override
    public void onDownloadProgress(long finished, long total, int percent) {
        mDownloadStatus.setStatus(DownloadStatus.STATUS_PROGRESS);
        mDownloadStatus.setFinished(finished);
        mDownloadStatus.setTotal(total);
        mDownloadStatus.setPercent(percent);
        mDelivery.post(mDownloadStatus);
    }

    @Override
    public void onDownloadCompleted() {
        mDownloadStatus.setStatus(DownloadStatus.STATUS_COMPLETED);
        mDelivery.post(mDownloadStatus);

    }

    @Override
    public void onDownloadPaused() {
        mDownloadStatus.setStatus(DownloadStatus.STATUS_PAUSE);
        mDelivery.post(mDownloadStatus);


    }

    @Override
    public void onDownloadCanceled() {
        mDownloadStatus.setStatus(DownloadStatus.STATUS_CANCELED);
        mDelivery.post(mDownloadStatus);

    }

    @Override
    public void onDownloadFailed(DownloadException e) {
        mDownloadStatus.setStatus(DownloadStatus.STATUS_FAILED);
        mDownloadStatus.setException(e);
        mDelivery.post(mDownloadStatus);

    }
}
