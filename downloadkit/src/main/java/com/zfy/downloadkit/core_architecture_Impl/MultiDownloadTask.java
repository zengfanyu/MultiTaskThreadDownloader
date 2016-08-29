package com.zfy.downloadkit.core_architecture_Impl;

import com.zfy.downloadkit.Moudle.DownloadInfo;
import com.zfy.downloadkit.Moudle.ThreadInfo;
import com.zfy.downloadkit.db.DatabaseManager;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * $desc
 * Created by zfy on 2016/8/29.
 */
public class MultiDownloadTask extends DownloadTaskImpl {
    private DatabaseManager mDatabaseManager;

    public MultiDownloadTask(DownloadInfo downloadInfo, ThreadInfo threadInfo,DatabaseManager dbManager, onDownloadListener onDownloadListener) {
        super(downloadInfo, threadInfo, onDownloadListener);
        this.mDatabaseManager=dbManager;
    }

    @Override
    protected void updateDB(ThreadInfo threadInfo) {
        mDatabaseManager.update(threadInfo.getTag(),threadInfo.getId(),threadInfo.getFinished());

    }

    @Override
    protected RandomAccessFile getFile(File dir, String name, long offset) throws IOException {
        File file = new File(dir, name);
        RandomAccessFile raf = new RandomAccessFile(file, "rwd");
        raf.seek(offset);
        return raf;
    }

    @Override
    protected int getResponseCode() {
        return HttpURLConnection.HTTP_PARTIAL;
    }

    @Override
    protected Map<String, String> getHttpHeaders(ThreadInfo threadInfo) {
        Map<String, String> headers = new HashMap<String, String>();
        long start = threadInfo.getStart() + threadInfo.getFinished();
        long end = threadInfo.getEnd();
        headers.put("Range", "bytes=" + start + "-" + end);
        return headers;

    }

    @Override
    protected void insertIntoDB(ThreadInfo threadInfo) {
        if (mDatabaseManager.exists(threadInfo.getTag(),threadInfo.getId())){
            mDatabaseManager.insert(threadInfo);
        }

    }

    @Override
    protected String getTag() {
        return this.getClass().getSimpleName();
    }
}
