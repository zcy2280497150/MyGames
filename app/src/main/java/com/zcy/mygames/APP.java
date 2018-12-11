package com.zcy.mygames;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.multidex.MultiDexApplication;

import com.zcy.mygames.utils.MyLog;
import com.zcy.mygames.utils.MyToast;
import com.zcy.mygames.views.ltzj.LTZJView;

public class APP extends MultiDexApplication {

    private static Context context;

    public static Context getContext(){
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;
        MyToast.init(this);
        MyLog.open(this);

    }
}
