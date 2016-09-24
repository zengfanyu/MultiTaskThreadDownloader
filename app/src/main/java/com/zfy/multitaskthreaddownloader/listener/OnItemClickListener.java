package com.zfy.multitaskthreaddownloader.listener;

import android.view.View;

/**
 * Created by ZFY on 2016/8/30.
 */
public interface OnItemClickListener<T> {
    void onItemClick(View v, int position, T t);
}
