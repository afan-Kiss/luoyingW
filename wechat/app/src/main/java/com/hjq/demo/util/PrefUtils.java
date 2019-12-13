
package com.hjq.demo.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefUtils {

    private static final String PREF_NAME = "genaral";
    private static final String RELOGIN = "relogin";
    private static final String RELGON2 = "relogin2";

    public static void putReloginflag(Context context, String key, String value) {
        if(context == null)return;
        SharedPreferences preferences = context.getSharedPreferences(RELOGIN, Context.MODE_PRIVATE);
        preferences.edit().putString(key, value).apply();
    }

    public static String getReloginflag(Context context, String key, String defaultValue) {
        SharedPreferences preferences = context.getSharedPreferences(RELOGIN, Context.MODE_PRIVATE);
        return preferences.getString(key, defaultValue);
    }

    public static void removeReloginflag(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(RELOGIN, Context.MODE_PRIVATE);
        preferences.edit().remove(key).apply();
    }


    public static void putReloginflag2(Context context, String key, String value) {
        if(context == null)return;
        SharedPreferences preferences = context.getSharedPreferences(RELGON2, Context.MODE_PRIVATE);
        preferences.edit().putString(key, value).apply();
    }

    public static String getReloginflag2(Context context, String key, String defaultValue) {
        SharedPreferences preferences = context.getSharedPreferences(RELGON2, Context.MODE_PRIVATE);
        return preferences.getString(key, defaultValue);
    }

    public static void removeReloginflag2(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(RELGON2, Context.MODE_PRIVATE);
        preferences.edit().remove(key).apply();
    }



    public static void putString(Context context, String key, String value) {
        if(context == null)return;
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        preferences.edit().putString(key, value).apply();
    }

    public static void putInt(Context context, String key, int value) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        preferences.edit().putInt(key, value).apply();
    }

    public static void putFloat(Context context, String key, float value) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        preferences.edit().putFloat(key, value).apply();
    }

    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        preferences.edit().putBoolean(key, value).apply();
    }

    public static void putLong(Context context, String key, long value) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        preferences.edit().putLong(key, value).apply();
    }

    public static String getString(Context context, String key, String defaultValue) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(key, defaultValue);
    }

    public static Integer getInt(Context context, String key, int defaultValue) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getInt(key, defaultValue);
    }

    public static Float getFloat(Context context, String key, float defaultValue) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getFloat(key, defaultValue);
    }

    public static Boolean getBoolean(Context context, String key, boolean defaultValue) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(key, defaultValue);
    }

    public static Long getLong(Context context, String key, long defaultValue) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getLong(key, defaultValue);
    }

    public static void removeKey(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        preferences.edit().remove(key).apply();
    }

    public static void clear(Context context) {
        String username = PrefUtils.getString(context, PreKeys.USERNAME, "");
        String password = PrefUtils.getString(context, PreKeys.PASSWORD, "");
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        preferences.edit().clear().apply();
        PrefUtils.putString(context, PreKeys.USERNAME, username);
        PrefUtils.putString(context, PreKeys.PASSWORD, password);
    }

}
