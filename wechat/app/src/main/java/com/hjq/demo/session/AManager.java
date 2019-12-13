package com.hjq.demo.session;

import android.app.Application;
import android.content.Context;

import com.hjq.demo.common.MyApplication;


public class AManager {

    private static Application application;

    public static void init(Application application) {
        AManager.application = application;
    }

    public static MyApplication getApp() {
        return (MyApplication) application;
    }

    public static Context getContext() {
        return application.getApplicationContext();
    }
}
