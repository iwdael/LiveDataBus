package androidx.lifecycle;


import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.ArchTaskExecutor;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class MutableBusLiveData<T> extends MutableLiveData<T> {
    private final Map<Observer<? super T>, ObserverStickyWrapper<T>> mObserverWrappers = new HashMap<>();

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        super.observe(owner, createAndSaveObserverWrapper(observer, false));
    }

    public void observeSticky(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        super.observe(owner, createAndSaveObserverWrapper(observer, true));
    }

    @Override
    public void observeForever(@NonNull Observer<? super T> observer) {
        super.observeForever(createAndSaveObserverWrapper(observer, false));
    }

    public void observeForeverSticky(@NonNull Observer<? super T> observer) {
        super.observeForever(createAndSaveObserverWrapper(observer, true));
    }

    @Override
    public void removeObserver(@NonNull Observer<? super T> observer) {
        super.removeObserver(removeObserverWrapper(observer));
    }


    @SuppressLint("RestrictedApi")
    @Override
    public void postValue(T value) {
        if (ArchTaskExecutor.getInstance().isMainThread())
            setValue(value);
        else
            ArchTaskExecutor.getInstance().postToMainThread(() -> {
                setValue(value);
            });
    }


    private ObserverStickyWrapper<T> createAndSaveObserverWrapper(@NonNull Observer<? super T> observer, boolean isSticky) {
        if (!mObserverWrappers.containsKey(observer)) {
            mObserverWrappers.put(observer, isSticky ? new ObserverStickyWrapper<T>(this, observer) : new ObserverWrapper<T>(this, observer));
        }
        return mObserverWrappers.get(observer);
    }

    private Observer<? super T> removeObserverWrapper(Observer<? super T> observer) {
        if (observer instanceof ObserverStickyWrapper) {
            return mObserverWrappers.remove(((ObserverStickyWrapper<? super T>) observer).observer);
        } else {
            return mObserverWrappers.remove(observer);
        }
    }

    static class ObserverWrapper<T> extends ObserverStickyWrapper<T> {

        public ObserverWrapper(LiveData<T> liveData, @NonNull Observer<? super T> observer) {
            super(liveData, observer);
        }

        @Override
        public void onChanged(T t) {
            LiveData<T> liveData = this.liveDataWeakReference.get();
            if (liveData == null) {
                observer.onChanged(t);
            } else if (this.version < liveData.getVersion()) {
                this.version = liveData.getVersion();
                observer.onChanged(t);
            }
        }
    }

    static class ObserverStickyWrapper<T> implements Observer<T> {
        final Observer<? super T> observer;
        protected int version;
        protected final WeakReference<LiveData<T>> liveDataWeakReference;

        public ObserverStickyWrapper(LiveData<T> liveData, @NonNull Observer<? super T> observer) {
            this.observer = observer;
            this.version = liveData.getVersion();
            this.liveDataWeakReference = new WeakReference<>(liveData);
        }

        @Override
        public void onChanged(T t) {
            observer.onChanged(t);
        }
    }
}
