package com.starrtc.demo.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.Map;

public class ApiURLUtils {

    public static String GetDate(Map<String, String> map) {
        String date;
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();

        map.put("Time", String.valueOf(Long.valueOf(TimeUtils.getCurTimeMills())/1000));
        map.put("Sign", RxEncryptTool.encryptMD5ToString(Ksort(map)));

//        date = new String(RxEncryptTool.base64UrlSafeEncode(gson.toJson(map)));
        date = RxEncryptTool.base64Encode2(gson.toJson(map));

        Log.i("66666", "GetDate: " + date);

        return date;
    }


    //排序
    public static String Ksort(Map<String, String> map) {
        String sb = "";
        String[] key = new String[map.size()];
        int index = 0;
        for (String k : map.keySet()) {
            key[index] = k;
            index++;
        }
        Arrays.sort(key);
        for (String s : key) {
            sb += '"' + s + '"' + ":" + '"' + map.get(s) + '"' + ",";
        }
        sb = sb.substring(0, sb.length() - 1);
        return "{" + sb + "}";
    }


}
