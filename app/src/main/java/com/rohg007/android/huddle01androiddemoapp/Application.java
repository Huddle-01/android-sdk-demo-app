package com.rohg007.android.huddle01androiddemoapp;

import android.content.Context;

import com.rohg007.android.huddle01_android_sdk.HuddleClient;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        HuddleClient.initApp(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}