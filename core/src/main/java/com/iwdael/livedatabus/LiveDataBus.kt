package com.iwdael.livedatabus

import android.os.Handler
import android.os.HandlerThread
import androidx.lifecycle.MutableBusLiveData
import java.util.HashMap

/**
 * author : Iwdael
 * e-mail : iwdael@outlook.com
 */
class LiveDataBus {
    private val busMap: MutableMap<Any, Observable<*>> = HashMap()
    private val handleThread = HandlerThread("live_data_bus")
    internal val handler: Handler

    private constructor() {
        handleThread.start()
        handler = Handler(handleThread.looper)
    }

    internal object Holder {
        val BUS = LiveDataBus()
    }

    companion object {
        @JvmStatic
        fun <V> with(type: Class<V>): Observable<V> {
            if (!Holder.BUS.busMap.containsKey(type)) {
                Holder.BUS.busMap[type] =
                    ObservableWrapper(MutableBusLiveData<V>())
            }
            return Holder.BUS.busMap[type] as Observable<V>
        }

        @JvmStatic
        fun <V> with(type: String): Observable<V> {
            if (!Holder.BUS.busMap.containsKey(type)) {
                Holder.BUS.busMap[type] = ObservableWrapper(MutableBusLiveData<V>())
            }
            return Holder.BUS.busMap[type] as Observable<V>
        }

        @JvmStatic
        fun <V : Any> post(value: V) {
            val observable = with(value.javaClass) as ObservableWrapper<V>
            observable.liveData.postValue(value)
        }

        @JvmStatic
        fun <V : Any> post(key: String, value: V) {
            val observable = with<V>(key) as ObservableWrapper<V>
            observable.liveData.postValue(value)
        }


    }
}