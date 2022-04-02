package com.iwdael.livedatabus.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.iwdael.livedatabus.LiveDataBus
import com.iwdael.livedatabus.annotation.Observe
import com.iwdael.livedatabus.annotation.ObserveSticky
import com.iwdael.livedatabus.annotation.UseLiveDataBus

@UseLiveDataBus
class MainKtActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        LiveDataBus.post("IMG form LiveDataBus")
    }


    @Observe
    fun liveData(name: String) {
        Log.v("dzq", "$name")
    }

    @ObserveSticky
    fun liveData2(name: String) {
        Log.v("dzq", "$name")
    }


}