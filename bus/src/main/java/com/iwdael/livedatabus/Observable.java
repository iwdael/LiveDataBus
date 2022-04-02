package com.iwdael.livedatabus;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

public interface Observable<T> {

    void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer);

    void observeSticky(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer);

    void observeForever(@NonNull Observer<? super T> observer);

    void observeForeverSticky(@NonNull Observer<? super T> observer);

    void removeObserver(@NonNull Observer<? super T> observer);
}
