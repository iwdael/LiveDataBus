package com.iwdael.livedatabus.example;

import androidx.appcompat.app.AppCompatActivity;

import com.iwdael.livedatabus.annotation.Observe;
import com.iwdael.livedatabus.annotation.ObserveSticky;
import com.iwdael.livedatabus.annotation.UseLiveDataBus;

@UseLiveDataBus
public class MainJavaActivity extends AppCompatActivity {

    @Observe("dasdasdqa")
    public void livedata(String name){

    }

    @ObserveSticky("23131")
    public void livedata2(String name){

    }
}
