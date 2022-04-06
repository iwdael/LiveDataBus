package com.iwdael.livedatabus

/**
 * author : Iwdael
 * e-mail : iwdael@outlook.com
 */
fun runOnBackgroundThread(callback: (() -> Unit)) {
    LiveDataBus.Holder.BUS.handler.post { callback.invoke() }
}