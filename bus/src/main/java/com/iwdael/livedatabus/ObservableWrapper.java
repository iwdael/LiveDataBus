package com.iwdael.livedatabus;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableBusLiveData;
import androidx.lifecycle.Observer;

public class ObservableWrapper<T> implements Observable<T> {
    final MutableBusLiveData<T> liveData;

    public ObservableWrapper(MutableBusLiveData<T> liveData) {
        this.liveData = liveData;
    }

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        this.liveData.observe(owner, observer);
    }

    @Override
    public void observeSticky(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        this.liveData.observeSticky(owner, observer);
    }

    @Override
    public void observeForever(@NonNull Observer<? super T> observer) {
        this.liveData.observeForever(observer);
    }

    @Override
    public void observeForeverSticky(@NonNull Observer<? super T> observer) {
        this.liveData.observeForeverSticky(observer);
    }

    @Override
    public void removeObserver(@NonNull Observer<? super T> observer) {
        this.liveData.removeObserver(observer);
    }
}
