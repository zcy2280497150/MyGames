package com.zcy.mygames.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.zcy.mygames.APP;


/**
 * 自定义Toast
 * Created by zcy on 2017/6/5.
 */
public class MyToast {

    //开关
    private static boolean flag = true;
    //Toast对象
    private static Toast mToast;

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    public static void init(APP app){
        context = app;
    }

    //自定义时间
    private static void showToast(String text, int length) {
        if (null == context)return;
        if (flag) {
            if (mToast != null)
                mToast.setText(text);
            else {
                mToast = Toast.makeText(context, text, length);
                mToast.setGravity(Gravity.CENTER, 0, 0);
            }
            mToast.show();
        }
    }

    //自定义时间和XML里面的文字Toast
    public static void showToast(int resId, int length) {
        showToast(context.getResources().getString(resId), length);
    }

    //短时间的Toast
    public static void makeTextShort(String msg) {
        showToast(msg, Toast.LENGTH_SHORT);
    }

    //长时间的Toast
    public static void makeTextLong(String msg) {
        showToast(msg, Toast.LENGTH_SHORT);
    }
}
