package com.zfy.multitaskthreaddownloader.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zfy.multitaskthreaddownloader.R;
import com.zfy.multitaskthreaddownloader.ui.fragment.MainFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState==null){
            MainFragment fragment = new MainFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_container,fragment).commit();
        }

    }


}
