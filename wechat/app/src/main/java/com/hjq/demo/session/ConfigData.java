package com.hjq.demo.session;

import android.preference.PreferenceManager;



public class ConfigData {

    /**
     * 移除配置参数
     */
    public static void removeKey(String key) {
        PreferenceManager.getDefaultSharedPreferences(AManager.getContext()).edit().remove(key).commit();
    }

    /**
     * 判断配置是否存在
     */
    public static boolean contains(String key) {
        return PreferenceManager.getDefaultSharedPreferences(AManager.getContext()).contains(key);
    }

    /**
     * 清空配置参数
     */
    public static void clear() {
        PreferenceManager.getDefaultSharedPreferences(AManager.getContext()).edit().clear().commit();
    }

    /**
     * 保存配置参数
     */
    public static void save(String key, Object value) {
        if (value == null) {
            return;
        }
        if (value instanceof Integer) {
            PreferenceManager.getDefaultSharedPreferences(AManager.getContext()).edit().putInt(key, (Integer) value).commit();
        } else if (value instanceof Long) {
            PreferenceManager.getDefaultSharedPreferences(AManager.getContext()).edit().putLong(key, (Long) value).commit();
        } else if (value instanceof Boolean) {
            PreferenceManager.getDefaultSharedPreferences(AManager.getContext()).edit().putBoolean(key, (Boolean) value).commit();
        } else if (value instanceof Float) {
            PreferenceManager.getDefaultSharedPreferences(AManager.getContext()).edit().putFloat(key, (Float) value).commit();
        } else {
            PreferenceManager.getDefaultSharedPreferences(AManager.getContext()).edit().putString(key, String.valueOf(value)).commit();
        }
    }

    /**
     * 读取int配置参数
     */
    public static int read(String key, int defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(AManager.getContext()).getInt(key, defaultValue);
    }

    /**
     * 读取long配置参数
     */
    public static long read(String key, long defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(AManager.getContext()).getLong(key, defaultValue);
    }

    /**
     * 读取Boolean配置参数
     */
    public static boolean read(String key, boolean defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(AManager.getContext()).getBoolean(key, defaultValue);
    }

    /**
     * 读取String配置参数
     */
    public static String read(String key, String defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(AManager.getContext()).getString(key, defaultValue);
    }

    /**
     * 读取float配置参数
     */
    public static float read(String key, float defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(AManager.getContext()).getFloat(key, defaultValue);
    }

    /**
     * 读取double配置参数
     */
    public static double read(String key, double defaultValue) {
        return Double.valueOf(read(key, String.valueOf(defaultValue)));
    }

    /**
     * 读取int配置参数
     */
    public static int readInt(String key) {
        return read(key, 0);
    }

    /**
     * 读取long配置参数
     */
    public static long readLong(String key) {
        return read(key, 0l);
    }

    /**
     * 读取Boolean配置参数
     */
    public static boolean readBoolean(String key) {
        return read(key, false);
    }

    /**
     * 读取String配置参数
     */
    public static String readString(String key) {
        return read(key, "");
    }

    /**
     * 读取float配置参数
     */
    public static float readFloat(String key) {
        return read(key, 0f);
    }

    /**
     * 读取double配置参数
     */
    public static double readBouble(String key) {
        return read(key, 0d);
    }

}
