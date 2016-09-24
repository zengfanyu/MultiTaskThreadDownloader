package com.zfy.multitaskthreaddownloader;

import android.app.Application;
import android.content.Context;

import com.zfy.downloadkit.DownloadConfiguration;
import com.zfy.downloadkit.DownloadManager;

/**
 * $desc
 * Created by zfy on 2016/8/30.
 */
public class DownloadApplication extends Application {
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        initDownloader();
    }

    private void initDownloader() {
        DownloadConfiguration configuration = new DownloadConfiguration();
        configuration.setMaxThreadNum(10);
        configuration.setThreadNum(3);
        DownloadManager.getInstance().init(sContext, configuration);
    }

    public static Context getContext(){
        return sContext;
    }
}
