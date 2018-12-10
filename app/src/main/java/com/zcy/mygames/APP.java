package com.zcy.mygames;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.zcy.mygames.utils.MyLog;
import com.zcy.mygames.utils.MyToast;

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
