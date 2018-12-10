package com.zcy.mygames.utils;

import android.util.Log;

import com.zcy.mygames.APP;


/**
 * 自定义的日志输出类，添加开关
 * Created by zcy on 2017/7/5.
 */
public class MyLog {

    private static String TAG = "xxx";
    private static boolean isDebug;

    public static void open(APP app){
        isDebug = true;
    }

    public static void v(String msg){
        v(TAG, msg);
    }
    private static void v(String tag , String msg){
        if (isDebug) Log.v(tag, null == msg ? "" : msg);
    }

    public static void d(String msg){
        d(TAG, msg);
    }
    private static void d(String tag , String msg){
        if (isDebug) Log.d(tag, null == msg ? "" : msg);
    }

    public static void i(String msg){
        i(TAG, msg);
    }
    private static void i(String tag , String msg){
        if (isDebug) Log.i(tag, null == msg ? "" : msg);
    }

    public static void w(String msg){
        w(TAG, msg);
    }
    private static void w(String tag , String msg){
        if (isDebug) Log.w(tag, null == msg ? "" : msg);
    }

    public static void e(String msg){
        e(TAG, msg);
    }
    private static void e(String tag , String msg){
        if (isDebug) Log.e(tag, null == msg ? "" : msg);
    }

}
