package com.zfy.downloadkit;

import com.zfy.downloadkit.core_architecture_Impl.ConnectTaskImpl;

import java.net.HttpURLConnection;

/**
 * $desc
 * Created by zfy on 2016/8/27.
 */
public interface CallBack {

    void onStart();

    /**
     *<p> This will be the first method called by {@link ConnectTaskImpl}
     */
    void onConnecting();


    /**
     * <p>if {@link ConnectTaskImpl} is successfully connected with the http/https server,
     * this method will be invoked.
     *
     * @param total The length of the file {@link HttpURLConnection#getContentLength()}
     * @param isRangeSupport Indicate whether download can be resumed from where it paused.
     *                       See {@link ConnectTaskImpl#run()}.If the value of http header field
     *                       {@code Accept-Ranges} is {@code bytes} the value of  isRangeSupport is
     *                       {@code true} else {@code false}
     */
    void onConnected(long total, boolean isRangeSupport);

    /**
     * <p>Progress callback
     *
     * @param finished the downloaded length of the file
     * @param total he total length of the file same value with method {@link CallBack#onConnected(long, boolean)}  }
     * @param progress the percent of progress (finished/total)*100
     */
    void onProgress(long finished, long total, int progress);

    /**
     * <p>download complete
     */
    void onCompleted();

    /**
     * <p>if you invoke {@link DownloadManager#pause(String)} or {@link DownloadManager#pauseAll()}
     * this method will be invoke if the downloading task is successfully paused.
     */
    void onDownloadPaused();

    /**
     *<p>if you invoke {@link DownloadManager#cancel(String)} or {@link DownloadManager#cancelAll()}
     * this method will be invoke if the downloading task is successfully canceled.
     */
    void onDownloadCanceled();

    /**
     * <p>download fail or exception callback
     *
     * @param e download exception
     */
    void onFailed(DownloadException e);


}
