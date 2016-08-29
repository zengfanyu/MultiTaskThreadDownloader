package com.zfy.downloadkit.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * $desc
 * Created by zfy on 2016/8/29.
 */
public class AbstractDao<T> {
    private DBOpenHelper mHelper;

    public AbstractDao(Context context){
        mHelper=new DBOpenHelper(context);
    }

    protected SQLiteDatabase getWritableDatabase(){
        return mHelper.getWritableDatabase();
    }

    protected SQLiteDatabase getReadableDatabase(){
        return mHelper.getReadableDatabase();
    }

    public void close(){
        mHelper.close();
    }


}
