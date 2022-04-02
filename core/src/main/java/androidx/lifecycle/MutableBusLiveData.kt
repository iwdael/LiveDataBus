package androidx.lifecycle

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.MutableBusLiveData.ObserverStickyWrapper
import android.annotation.SuppressLint
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.lifecycle.LiveData
import java.lang.ref.WeakReference
import java.util.HashMap
/**
 * author : Iwdael
 * e-mail : iwdael@outlook.com
 */
class MutableBusLiveData<T> : MutableLiveData<T>() {
    private val mObserverWrappers: MutableMap<Observer<in T>, ObserverStickyWrapper<T>> = HashMap()
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, createAndSaveObserverWrapper(observer, false))
    }

    fun observeSticky(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, createAndSaveObserverWrapper(observer, true))
    }

    override fun observeForever(observer: Observer<in T>) {
        super.observeForever(createAndSaveObserverWrapper(observer, false))
    }

    fun observeForeverSticky(observer: Observer<in T>) {
        super.observeForever(createAndSaveObserverWrapper(observer, true))
    }

    override fun removeObserver(observer: Observer<in T>) {
        super.removeObserver(removeObserverWrapper(observer))
    }

    @SuppressLint("RestrictedApi")
    override fun postValue(value: T) {
        if (ArchTaskExecutor.getInstance().isMainThread) setValue(value) else ArchTaskExecutor.getInstance()
            .postToMainThread { setValue(value) }
    }

    private fun createAndSaveObserverWrapper(
        observer: Observer<in T>,
        isSticky: Boolean
    ): ObserverStickyWrapper<T> {
        if (!mObserverWrappers.containsKey(observer)) {
            mObserverWrappers[observer] = if (isSticky) ObserverStickyWrapper(
                this,
                observer
            ) else ObserverWrapper(this, observer)
        }
        return mObserverWrappers[observer]!!
    }

    private fun removeObserverWrapper(observer: Observer<in T>): Observer<in T> {
        return if (observer is ObserverStickyWrapper<*>) {
            mObserverWrappers.remove((observer as ObserverStickyWrapper<in T>).observer)!!
        } else {
            mObserverWrappers.remove(observer)!!
        }
    }

    class ObserverWrapper<T>(liveData: LiveData<T>, observer: Observer<in T>) :
        ObserverStickyWrapper<T>(liveData, observer) {
        override fun onChanged(t: T) {
            val liveData = liveDataWeakReference.get()
            if (liveData == null) {
                observer.onChanged(t)
            } else if (version < liveData.version) {
                version = liveData.version
                observer.onChanged(t)
            }
        }
    }

    open class ObserverStickyWrapper<T>(
        liveData: LiveData<T>,
        val observer: Observer<in T>
    ) : Observer<T> {
        protected var version: Int = liveData.version
        protected val liveDataWeakReference: WeakReference<LiveData<T>> = WeakReference(liveData)
        override fun onChanged(t: T) {
            observer.onChanged(t)
        }

    }
}