package com.hjq.demo.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by zhangrenwei on 2019-11-17 14:02.
 */
public class Utils {

    private static String VERSION = null;
    /**
     * 获取屏幕宽高
     *
     * @param context
     * @return
     */
    public static DisplayMetrics getScreenSize(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    /*
     * 判断是否安装过其他的apk
     * */
    public static boolean isPackageInstalled(Context ctx, String pkgName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = ctx.getPackageManager().getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }
    /*
     * 是否安装了微信
     * */
    public static boolean isWechatInstalled(Context ctx) {
        return isPackageInstalled(ctx, "com.tencent.mm");
    }

    /*
     * 是否安装了qq
     * */
    public static boolean isQQInstalled(Context ctx) {
        return isPackageInstalled(ctx, "com.tencent.mobileqq");
    }

    /*
     * 是否安装了微博
     * */
    public static boolean isWeiboInstalled(Context ctx) {
        return isPackageInstalled(ctx, "com.sina.weibo");
    }
    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
		/*
		移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		联通：130、131、132、152、155、156、185、186
		电信：133、153、180、189、（1349卫通）/^0?1[3|4|5|7|8][0-9]\d{8}$/
		总结起来就是第一位必定为1，第二位必定为3或5或8或7（电信运营商），其他位置的可以为0-9
		*/
        String telRegex = "[1][3456789]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }

    /*
     * 获取设备宽度（单位：px）
     * */
    public static float getScreenWidth(Context ctx) {
        return ctx.getResources().getDisplayMetrics().widthPixels;
    }

    /*
     * 获取设备高度（单位：px）
     * */
    public static float getScreenHeight(Context ctx) {
        return ctx.getResources().getDisplayMetrics().heightPixels;
    }
    /*
     * 获取App版本号
     * */
    public static String getVersion(Context context) {
        if (VERSION != null) {
            return VERSION;
        }
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            VERSION = pi.versionName;
            return VERSION;
        } catch (PackageManager.NameNotFoundException e) {
            return "0.0";
        }
    }

    /*
     * 获取App VersionCode
     * */
    public static int getVersionCode(Context context) {
        if (null == context) {
            return 1;
        }
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return 1;
        }
    }

    /**
     * 打开软键盘
     */
    public static void openKeybord(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                    InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }

    /**
     * 关闭软键盘
     */
    public static void closeKeybord(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }




}
