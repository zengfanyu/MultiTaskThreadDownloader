package com.zfy.downloadkit.Moudle;

import java.io.File;

/**
 * Download Moudle
 * Created by zfy on 2016/8/29.
 */
public class DownloadInfo {
    private String name;
    private String url;
    private File dir;
    private int progress;
    private long finished;
    private long length;
    private boolean acceptRanges;
    private int status;

    public DownloadInfo(String name, String url, File dir) {
        this.name = name;
        this.url = url;
        this.dir = dir;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public File getDir() {
        return dir;
    }

    public void setDir(File dir) {
        this.dir = dir;
    }
    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public long getFinished() {
        return finished;
    }

    public void setFinished(long finished) {
        this.finished = finished;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public boolean isAcceptRanges() {
        return acceptRanges;
    }

    public void setAcceptRanges(boolean acceptRanges) {
        this.acceptRanges = acceptRanges;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
