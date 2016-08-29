package com.zfy.downloadkit.core_architecture_Impl;

import android.os.Handler;

import com.zfy.downloadkit.CallBack;
import com.zfy.downloadkit.architecture.DownloadStatus;
import com.zfy.downloadkit.architecture.IDownloadStatusDelivery;

import java.util.concurrent.Executor;

/**
 * IDownloadStatusDelivery接口 的实现类
 * Created by zfy on 2016/8/27.
 */
public class DownloadStatusDeliveryImpl implements IDownloadStatusDelivery {
    private Executor mDownloadStatusPoster;

    public DownloadStatusDeliveryImpl(final Handler handler) {
        mDownloadStatusPoster = new Executor() {
            @Override
            public void execute(Runnable command) {
                handler.post(command);

            }
        };
    }

    @Override
    public void post(DownloadStatus status) {
        mDownloadStatusPoster.execute(new DownloadStatusDeliveryRunnable(status));

    }

    private class DownloadStatusDeliveryRunnable implements Runnable {
        private final DownloadStatus mDownloadStatus;
        private final CallBack mCallBack;


        public DownloadStatusDeliveryRunnable(DownloadStatus status) {
            this.mDownloadStatus = status;
            this.mCallBack = mDownloadStatus.getCallBack();
        }

        @Override
        public void run() {
            switch (mDownloadStatus.getStatus()) {
                case DownloadStatus.STATUS_CONNECTING:
                    mCallBack.onConnecting();
                    break;
                case DownloadStatus.STATUS_CONNECTED:
                    mCallBack.onConnected(mDownloadStatus.getTotal(),mDownloadStatus.isAcceptRanges());
                    break;
                case DownloadStatus.STATUS_PROGRESS:
                    mCallBack.onProgress(mDownloadStatus.getFinished(),mDownloadStatus.getTotal(),mDownloadStatus.getPercent());
                    break;
                case DownloadStatus.STATUS_PAUSED:
                    mCallBack.onDownloadPaused();
                    break;
                case DownloadStatus.STATUS_CANCELED:
                    mCallBack.onDownloadCanceled();
                    break;
                case DownloadStatus.STATUS_COMPLETED:
                    mCallBack.onCompleted();
                    break;
                case DownloadStatus.STATUS_FAILED:
                    mCallBack.onFailed(mDownloadStatus.getException());
                    break;
            }

        }
    }
}
