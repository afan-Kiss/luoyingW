package com.hjq.demo.rong;

import android.content.Context;

import androidx.annotation.StringRes;

public class ResourcesHelper {
    private static Context sAppContext;

    public synchronized static void init(Context appContent) {
        sAppContext = appContent.getApplicationContext();
    }


    public static String getString(@StringRes int resID) {
        return sAppContext.getString(resID);
    }
}
