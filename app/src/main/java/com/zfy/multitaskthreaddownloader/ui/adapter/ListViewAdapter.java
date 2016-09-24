package com.zfy.multitaskthreaddownloader.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.zfy.multitaskthreaddownloader.listener.OnItemClickListener;

/**
 * $desc
 * Created by zfy on 2016/8/30.
 */
public class ListViewAdapter extends BaseAdapter {

    private OnItemClickListener mListener;

    public void setOnItemClickedListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
