package com.iwdael.livedatabus;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.lifecycle.MutableBusLiveData;

import java.util.HashMap;
import java.util.Map;

public class LiveDataBus {
    private final Map<Object, Observable> busMap = new HashMap<>();

    private static class Holder {
        private static final LiveDataBus BUS = new LiveDataBus();
    }

    /**
     * 根据类型返回可观察的对象
     */
    public static <T> Observable<T> with(Class<T> type) {
        if (!Holder.BUS.busMap.containsKey(type)) {
            Holder.BUS.busMap.put(type, new ObservableWrapper<T>(new MutableBusLiveData<T>()));
        }
        return (Observable<T>) Holder.BUS.busMap.get(type);
    }

    public static <T> Observable<T> with(String type ) {
        if (!Holder.BUS.busMap.containsKey(type)) {
            Holder.BUS.busMap.put(type, new ObservableWrapper<T>(new MutableBusLiveData<T>()));
        }
        return (Observable<T>) Holder.BUS.busMap.get(type);
    }

    public static <T> void post(T e) {
        ObservableWrapper<T> observable = (ObservableWrapper<T>) with(e.getClass());
        observable.liveData.postValue(e);
    }

}
