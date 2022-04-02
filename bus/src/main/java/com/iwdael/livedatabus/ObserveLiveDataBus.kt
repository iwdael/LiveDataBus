package com.iwdael.livedatabus

interface ObserveLiveDataBus<T> {
    fun observe(owner: T): ObserveLiveDataBus<T>
}