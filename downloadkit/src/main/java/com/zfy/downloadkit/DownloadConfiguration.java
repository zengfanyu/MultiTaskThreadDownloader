package com.zfy.downloadkit;

/**
 * Configuration about download's thread
 * Created by zfy on 2016/8/29.
 */
public class DownloadConfiguration {
    /*
    * 线程池中默认最大线程数
    * */
    public static final int DEFAULT_MAX_THREAD_NUMBER=10;
    /*
    * 每一个Download的线程数
    * */
    public static final int DEFAULT_THREAD_NUMBER=1;

    /*
    * thread number in the pool
    * */
    private int maxThreadNum;

    /*
    * thread number for each download
    * */
    private int threadNum;

    /*
    *
    * init with default number*/

    public DownloadConfiguration() {
        maxThreadNum=DEFAULT_MAX_THREAD_NUMBER;
        threadNum=DEFAULT_THREAD_NUMBER;
    }

    public int getMaxThreadNum() {
        return maxThreadNum;
    }

    public void setMaxThreadNum(int maxThreadNum) {
        this.maxThreadNum = maxThreadNum;
    }

    public int getThreadNum() {
        return threadNum;
    }

    public void setThreadNum(int threadNum) {
        this.threadNum = threadNum;
    }
}
