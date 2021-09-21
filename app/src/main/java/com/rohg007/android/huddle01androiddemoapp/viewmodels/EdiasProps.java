package com.rohg007.android.huddle01androiddemoapp.viewmodels;

import android.app.Application;

import com.rohg007.android.huddle01androiddemoapp.state.StateStore;

import java.lang.reflect.InvocationTargetException;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public abstract class EdiasProps extends AndroidViewModel {

    @NonNull
    private final StateStore mRoomStore;

    EdiasProps(@NonNull Application application, @NonNull StateStore roomStore) {
        super(application);
        mRoomStore = roomStore;
    }

    @NonNull
    StateStore getRoomStore() {
        return mRoomStore;
    }

    public abstract void connect(LifecycleOwner lifecycleOwner);

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull private final Application mApplication;
        @NonNull private final StateStore mStore;

        public Factory(@NonNull Application application, @NonNull StateStore store) {
            mApplication = application;
            mStore = store;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (EdiasProps.class.isAssignableFrom(modelClass)) {
                try {
                    return modelClass
                            .getConstructor(Application.class, StateStore.class)
                            .newInstance(mApplication, mStore);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException("Cannot create an instance of " + modelClass, e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Cannot create an instance of " + modelClass, e);
                } catch (InstantiationException e) {
                    throw new RuntimeException("Cannot create an instance of " + modelClass, e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException("Cannot create an instance of " + modelClass, e);
                }
            }
            return super.create(modelClass);
        }
    }
}