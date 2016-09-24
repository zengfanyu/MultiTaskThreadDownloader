package com.zfy.multitaskthreaddownloader.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zfy.multitaskthreaddownloader.entity.AppInfo;
import com.zfy.multitaskthreaddownloader.listener.OnItemClickListener;
import com.zfy.multitaskthreaddownloader.ui.adapter.ListViewAdapter;

import java.io.File;
import java.util.List;

/**
 * $desc
 * Created by zfy on 2016/8/30.
 */
public class MainFragment extends Fragment implements OnItemClickListener {

    private File mDownloadDir;

    private DownloadReceiver mReceiver;

    private List<AppInfo>mAppInfos;
    private ListViewAdapter mAdapter;

    public MainFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDownloadDir=new File(Environment.getExternalStorageDirectory(),"Download");
        mAdapter=new ListViewAdapter();
        mAdapter.setOnItemClickedListener(this);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onItemClick(View v, int position, Object o) {

    }

    private class DownloadReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }
}
