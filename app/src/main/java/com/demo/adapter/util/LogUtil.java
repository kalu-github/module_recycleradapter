package com.demo.adapter.util;

import android.util.Log;

/**
 * description: 日志工具类
 * created by kalu on 2016/11/19 11:58
 */
public final class LogUtil {

    //日志打印开关
    private static boolean isLogEnable = true;

    public static void d(String tag, String msg) {
        if (isLogEnable) {
            Log.d(tag, msg);
        }
    }

    public static void d(String tag, String msg, Throwable e) {
        if (isLogEnable) {
            Log.d(tag, msg, e);
        }
    }

    public static void i(String tag, String msg) {
        if (isLogEnable) {
            Log.i(tag, msg);
        }
    }

    public static void i(String tag, String msg, Throwable e) {
        if (isLogEnable) {
            Log.i(tag, msg, e);
        }
    }

    public static void v(String tag, String msg) {
        if (isLogEnable) {
            Log.v(tag, msg);
        }
    }

    public static void v(String tag, String msg, Throwable e) {
        if (isLogEnable) {
            Log.v(tag, msg, e);
        }
    }

    public static void w(String tag, String msg) {
        if (isLogEnable) {
            Log.w(tag, msg);
        }
    }

    public static void w(String tag, String msg, Throwable e) {
        if (isLogEnable) {
            Log.w(tag, msg, e);
        }
    }

    public static void e(String tag, String msg) {
        if (isLogEnable) {
            Log.e(tag, msg);
        }
    }

    public static void e(String tag, String msg, Throwable e) {
        if (isLogEnable) {
            Log.e(tag, msg, e);
        }
    }
}
