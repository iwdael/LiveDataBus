package com.iwdael.livedatabus

import androidx.lifecycle.LifecycleOwner
import com.iwdael.livedatabus.ObservableWrapper
import androidx.lifecycle.MutableBusLiveData
import androidx.lifecycle.Observer
/**
 * author : Iwdael
 * e-mail : iwdael@outlook.com
 */
class ObservableWrapper<T>(val liveData: MutableBusLiveData<T>) : Observable<T> {
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        liveData.observe(owner, observer)
    }

    override fun observeSticky(owner: LifecycleOwner, observer: Observer<in T>) {
        liveData.observeSticky(owner, observer)
    }

    override fun observeForever(observer: Observer<in T>) {
        liveData.observeForever(observer)
    }

    override fun observeForeverSticky(observer: Observer<in T>) {
        liveData.observeForeverSticky(observer)
    }

    override fun removeObserver(observer: Observer<in T>) {
        liveData.removeObserver(observer)
    }
}