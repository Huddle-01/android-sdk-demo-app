package com.rohg007.android.huddle01androiddemoapp.state;

import androidx.annotation.NonNull;
import androidx.core.util.Supplier;
import androidx.lifecycle.MutableLiveData;

public class CustomTypeMutableLiveData<T> extends MutableLiveData<T> {

    public CustomTypeMutableLiveData(@NonNull Supplier<T> supplier) {
        setValue(supplier.get());
    }

    @NonNull
    @Override
    @SuppressWarnings("all")
    public T getValue() {
        return super.getValue();
    }

    public interface Invoker<T> {
        void invokeAction(T value);
    }

    public void postValue(@NonNull CustomTypeMutableLiveData.Invoker<T> invoker) {
        T value = getValue();
        invoker.invokeAction(value);
        postValue(value);
    }
}
