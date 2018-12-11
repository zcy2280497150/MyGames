package com.zcy.mygames.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.SparseArray;

import com.zcy.mygames.APP;

public class BitmapUtils {

    private static SparseArray<Bitmap> bitmapSparseArray = new SparseArray<>();

    public static Bitmap findBitmapById(int id){
        Bitmap bitmap = bitmapSparseArray.get(id);
        if (null == bitmap){
            bitmap = BitmapFactory.decodeResource(APP.getContext().getResources() , id);
            bitmapSparseArray.put(id,bitmap);
        }
        return bitmap;
    }

}
