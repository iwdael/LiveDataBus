package com.iwdael.livedatabus

import com.iwdael.livedatabus.ObservableWrapper
import androidx.lifecycle.MutableBusLiveData
import java.util.HashMap

/**
 * author : Iwdael
 * e-mail : iwdael@outlook.com
 */
class LiveDataBus {
    private val busMap: MutableMap<Any, Observable<*>> = HashMap()

    private object Holder {
        val BUS = LiveDataBus()
    }

    companion object {
        @JvmStatic
        fun <T> with(type: Class<T>): Observable<T> {
            if (!Holder.BUS.busMap.containsKey(type)) {
                Holder.BUS.busMap[type] =
                    ObservableWrapper(MutableBusLiveData<T>())
            }
            return Holder.BUS.busMap[type] as Observable<T>
        }

        @JvmStatic
        fun <T> with(type: String): Observable<T> {
            if (!Holder.BUS.busMap.containsKey(type)) {
                Holder.BUS.busMap[type] = ObservableWrapper(MutableBusLiveData<T>())
            }
            return Holder.BUS.busMap[type] as Observable<T>
        }

        @JvmStatic
        fun <T : Any> post(e: T) {
            val observable = with(e.javaClass) as ObservableWrapper<T>
            observable.liveData.postValue(e)
        }

        @JvmStatic
        fun <T : Any> post(key: String, e: T) {
            val observable = with<T>(key) as ObservableWrapper<T>
            observable.liveData.postValue(e)
        }
    }
}