package com.zfy.downloadkit;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;
import com.zfy.downloadkit.Moudle.DownloadInfo;
import com.zfy.downloadkit.Moudle.ThreadInfo;
import com.zfy.downloadkit.architecture.IDownloadResponse;
import com.zfy.downloadkit.architecture.IDownloader;
import com.zfy.downloadkit.core_architecture_Impl.DownloadResponseImpl;
import com.zfy.downloadkit.core_architecture_Impl.DownloadStatusDeliveryImpl;
import com.zfy.downloadkit.core_architecture_Impl.DownloaderImpl;
import com.zfy.downloadkit.db.DatabaseManager;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * $desc
 * Created by zfy on 2016/8/30.
 */
public class DownloadManager implements IDownloader.OnDownloaderDestroyedListener {

    public static final String TAG = DownloadManager.class.getSimpleName();

    /*
    * singleton of DownloadManager
    * */
    private static DownloadManager sDownloadManager;

    private DatabaseManager mDatabaseManager;

    private Map<String, IDownloader> mDownloaderMap;

    private DownloadConfiguration mConfiguration;

    private ExecutorService mExecutorService;

    private DownloadStatusDeliveryImpl mDelivery;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    public static DownloadManager getInstance() {
        if (sDownloadManager == null) {
            synchronized (DownloadManager.class) {
                if (sDownloadManager == null) {
                    sDownloadManager = new DownloadManager();
                }
            }
        }
        return sDownloadManager;
    }

    private DownloadManager() {
        mDownloaderMap = new LinkedHashMap<String, IDownloader>();

    }

    public void init(Context context, @NonNull DownloadConfiguration configuration) {
        if (configuration.getThreadNum() > configuration.getMaxThreadNum()) {
            throw new IllegalArgumentException("thread number must <max thread number");
        }

        mConfiguration = configuration;
        mDatabaseManager = DatabaseManager.getInstance(context);
        mExecutorService = Executors.newFixedThreadPool(mConfiguration.getMaxThreadNum());
        mDelivery = new DownloadStatusDeliveryImpl(mHandler);
    }

    @Override
    public void onDestroyed(final String key, IDownloader IDownloader) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mDownloaderMap.containsKey(key)) {
                    mDownloaderMap.remove(key);

                }
            }
        });

    }

    public void download(DownloadRequest request, String tag, CallBack callBack) {
        final String key = createKey(tag);

        if (check(key)) {
            IDownloadResponse response = new DownloadResponseImpl(mDelivery, callBack);
            IDownloader downloader = new DownloaderImpl(request, response, mExecutorService, mDatabaseManager, key, mConfiguration, this);
            mDownloaderMap.put(key, downloader);
            downloader.start();

        }

    }

    public void pause(String tag) {
        String key = createKey(tag);
        if (mDownloaderMap.containsKey(key)) {
            IDownloader downloader = mDownloaderMap.get(key);
            if (downloader != null) {
                if (!downloader.isRunning()) {
                    downloader.pause();
                }
            }

            mDownloaderMap.remove(key);

        }
    }

    public void cancel(String tag) {
        String key = createKey(tag);
        if (mDownloaderMap.containsKey(key)) {
            IDownloader downloader = mDownloaderMap.get(key);
            if (downloader != null) {
                downloader.cancel();
            }
            mDownloaderMap.remove(key);
        }
    }

    public void pauseAll() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                for (IDownloader downloader : mDownloaderMap.values()) {
                    if (downloader != null) {
                        if (downloader.isRunning()) {
                            downloader.pause();
                        }
                    }
                }
            }
        });
    }

    public void cancelAll() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                for (IDownloader downloader : mDownloaderMap.values()) {
                    if (downloader != null) {
                        if (downloader.isRunning()) {
                            downloader.cancel();
                        }
                    }
                }
            }
        });
    }

    public DownloadInfo getDownloadInfo(String tag) {
        String key = createKey(tag);
        List<ThreadInfo> threadInfos = mDatabaseManager.getThreadInfos(key);
        DownloadInfo downloadInfo = null;
        if (!threadInfos.isEmpty()) {
            int finished = 0;
            int progress = 0;
            int total = 0;
            for (ThreadInfo info : threadInfos) {
                finished += info.getFinished();
                total += (info.getEnd() - info.getStart());
            }
            progress = (int) ((long) finished * 100 / total);
            downloadInfo = new DownloadInfo();
            downloadInfo.setFinished(finished);
            downloadInfo.setLength(total);
            downloadInfo.setProgress(progress);
        }
        return downloadInfo;
    }

    private boolean check(String key) {
        if (mDownloaderMap.containsKey(key)) {
            IDownloader downloader = mDownloaderMap.get(key);
            if (downloader != null) {
                if (downloader.isRunning()) {
                    Logger.d("Task has been started!");
                    return false;
                } else {
                    throw new IllegalStateException("Downloader instance with same tag has not been destroyed!");
                }
            }
        }
        return true;
    }

    private String createKey(String tag) {
        if (tag == null) {
            throw new NullPointerException("tag can not be null!");
        }
        return String.valueOf(tag.hashCode());


    }
}
