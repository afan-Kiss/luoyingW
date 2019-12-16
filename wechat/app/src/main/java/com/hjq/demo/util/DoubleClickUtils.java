package com.hjq.demo.util;

/**
 *<br/>
 * 判断是否快速点击，快速点击两次不让第二次不生效
 */
 public class DoubleClickUtils {
    
     private static long lastClickTime;
    
      public static boolean isDoubleClick() {
            //当前时间
            long currentTime = System.currentTimeMillis();
            long timeInterval = currentTime - lastClickTime;
            if (0 < timeInterval&& timeInterval< 1500) {
                //如果间隔在0-1.秒内就是快速重复点击
                return true;
            }
            lastClickTime = currentTime;
            return false;
        }
    }