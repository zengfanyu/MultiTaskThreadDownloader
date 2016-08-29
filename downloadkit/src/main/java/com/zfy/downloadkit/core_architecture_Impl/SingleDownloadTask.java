package com.zfy.downloadkit.core_architecture_Impl;

import com.zfy.downloadkit.Moudle.DownloadInfo;
import com.zfy.downloadkit.Moudle.ThreadInfo;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.util.Map;

/**
 * $desc
 * Created by zfy on 2016/8/29.
 */
public class SingleDownloadTask extends DownloadTaskImpl {
    public SingleDownloadTask(DownloadInfo downloadInfo, ThreadInfo threadInfo, onDownloadListener onDownloadListener) {
        super(downloadInfo, threadInfo, onDownloadListener);
    }

    @Override
    protected void updateDB(ThreadInfo threadInfo) {
        //不需要重写

    }

    @Override
    protected RandomAccessFile getFile(File dir, String name, long offset) throws IOException {
        File file = new File(dir, name);
        RandomAccessFile raf = new RandomAccessFile(file, "rwd");
        raf.seek(0);
        return raf;
    }

    @Override
    protected int getResponseCode() {
        return HttpURLConnection.HTTP_OK;
    }

    @Override
    protected Map<String, String> getHttpHeaders(ThreadInfo threadInfo) {
        return null;
    }

    @Override
    protected void insertIntoDB(ThreadInfo threadInfo) {
        //不支持

    }

    @Override
    protected String getTag() {
        return this.getClass().getSimpleName();
    }
}
