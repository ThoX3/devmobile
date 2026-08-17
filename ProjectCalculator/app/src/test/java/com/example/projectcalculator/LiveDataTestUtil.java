package com.example.projectcalculator;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

public class LiveDataTestUtil {
    public static <T> T getOrAwaitValue(final LiveData<T> liveData) {
        final Object[] data = new Object[1];

        Observer<T> observer = new Observer<T>() {
            @Override
            public void onChanged(T t) {
                data[0] = t;
                liveData.removeObserver(this);
            }
        };

        liveData.observeForever(observer);

        return (T) data[0];
    }
}
