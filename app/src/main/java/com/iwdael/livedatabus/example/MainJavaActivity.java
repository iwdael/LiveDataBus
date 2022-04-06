package com.iwdael.livedatabus.example;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.iwdael.livedatabus.LiveDataBus;
import com.iwdael.livedatabus.annotation.Dispatcher;
import com.iwdael.livedatabus.annotation.Observe;
import com.iwdael.livedatabus.annotation.ObserveForever;
import com.iwdael.livedatabus.annotation.ObserveForeverSticky;
import com.iwdael.livedatabus.annotation.ObserveSticky;
import com.iwdael.livedatabus.annotation.UseLiveDataBus;

@UseLiveDataBus
public class MainJavaActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LiveDataBus.post("IMG form LiveDataBus");
        LiveDataBus.post("dzq", "IMG form STR");
    }

    @Observe(dispatcher = Dispatcher.Background)
    public void live(String name) {
        Log.v("dzq", "live:" + name);
    }

    @ObserveSticky
    public void liveSticky(String name) {
        Log.v("dzq", "liveSticky:" + name);
    }

    @ObserveForever
    public void liveForever(String name) {
        Log.v("dzq", "liveForever:" + name);
    }

    @ObserveForeverSticky(dispatcher = Dispatcher.Background)
    public void liveForeverSticky(String name) {
        Log.v("dzq", "liveForeverSticky:" + name);
    }


    @Observe("dzq")
    public void liveStr(String name) {
        Log.v("dzq", "liveStr:" + name);
    }

    @ObserveSticky("dzq")
    public void liveStrSticky(String name) {
        Log.v("dzq", "liveStrSticky:" + name);
    }

    @ObserveForever("dzq")
    public void liveStrForever(String name) {
        Log.v("dzq", "liveStrForever:" + name);
    }

    @ObserveForeverSticky("dzq")
    public void liveStrForeverSticky(String name) {
        Log.v("dzq", "liveStrForeverSticky:" + name);
    }
}
