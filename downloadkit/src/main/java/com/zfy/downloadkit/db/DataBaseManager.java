package com.zfy.downloadkit.db;

import android.content.Context;

import com.zfy.downloadkit.Moudle.ThreadInfo;

import java.util.List;

/**
 * 数据库的管理类
 * Created by zfy on 2016/8/29.
 */
public class DatabaseManager {
    private static DatabaseManager sDatabaseManager;
    private final ThreadInfoDao mThreadInfoDao;

    public static DatabaseManager getInstance(Context context){
        if (sDatabaseManager==null){
            sDatabaseManager=new DatabaseManager(context);
        }
        return sDatabaseManager;
    }
    //单例类
    private DatabaseManager(Context context){
        mThreadInfoDao=new ThreadInfoDao(context);
    }


    public synchronized void insert(ThreadInfo threadInfo) {
        mThreadInfoDao.insert(threadInfo);
    }

    public synchronized void delete(String tag) {
        mThreadInfoDao.delete(tag);
    }

    public synchronized void update(String tag, int threadId, long finished) {
        mThreadInfoDao.update(tag, threadId, finished);
    }

    public List<ThreadInfo> getThreadInfos(String tag) {
        return mThreadInfoDao.getThreadInfos(tag);
    }

    public List<ThreadInfo> getThreadInfos(){
        return mThreadInfoDao.getThreadInfos();
    }

    public boolean exists(String tag, int threadId) {
        return mThreadInfoDao.exists(tag, threadId);
    }
}
