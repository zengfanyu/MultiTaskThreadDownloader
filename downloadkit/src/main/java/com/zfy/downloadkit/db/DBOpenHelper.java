package com.zfy.downloadkit.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库的帮助类
 * Created by zfy on 2016/8/29.
 */
public class DBOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME="download.db";
    private static final int DB_VERSION=1;

    public DBOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropTable(db);
        createTable(db);

    }

    private void createTable(SQLiteDatabase db) {
        ThreadInfoDao.createTable(db);


    }

    private void dropTable(SQLiteDatabase db) {
        ThreadInfoDao.dropTable(db);


    }
}
