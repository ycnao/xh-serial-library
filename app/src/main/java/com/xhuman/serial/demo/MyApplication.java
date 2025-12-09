package com.xhuman.serial.demo;

import android.app.Application;
import android.content.Context;


/**
 * MyApplication
 * author: Created by 闹闹 on 2019/7/10
 * version: 1.0.0
 */
public class MyApplication extends Application {

    private static MyApplication instance;
    public static Context applicationContext;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        applicationContext = this;
    }

    public static MyApplication instance() {
        return instance;
    }


}
