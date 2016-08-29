package com.zfy.downloadkit.Moudle;

/**
 * Thread Moudle
 * Created by zfy on 2016/8/29.
 */
public class ThreadInfo {
    private int id;
    private String tag;
    private String url;
    private long start;
    private long end;
    private long finished;

    public ThreadInfo() {
    }

    public ThreadInfo(int id, String tag, String url, long finished) {
        this.id = id;
        this.tag = tag;
        this.url = url;
        this.finished = finished;
    }

    public ThreadInfo(int id, String tag, String url, long start, long end, long finished) {
        this.id = id;
        this.tag = tag;
        this.url = url;
        this.start = start;
        this.end = end;
        this.finished = finished;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public long getFinished() {
        return finished;
    }

    public void setFinished(long finished) {
        this.finished = finished;
    }
}
