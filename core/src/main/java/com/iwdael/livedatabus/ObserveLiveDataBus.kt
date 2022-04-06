package com.iwdael.livedatabus

/**
 * author : Iwdael
 * e-mail : iwdael@outlook.com
 */
interface ObserveLiveDataBus<T> {
    fun observe(owner: T): ObserveLiveDataBus<T>

    fun removeAllObserver()
}