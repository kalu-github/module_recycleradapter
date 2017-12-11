package com.demo.adapter;

import android.app.Application;

public class MyApplication extends Application {

    private static MyApplication appContext;

    public static MyApplication getInstance() {
        return appContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
    }
}
