package com.zcy.mygames.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.SparseArray;

import com.zcy.mygames.APP;
import com.zcy.mygames.R;

import java.util.Random;

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
    private static Random random = new Random();

    public static Bitmap getBoomBitmap(){
        return findBitmapById(BOOM_DRAWABLE_IDS[random.nextInt(BOOM_DRAWABLE_IDS.length)]);
    }

    public static final int[] BOOM_DRAWABLE_IDS = {
            R.drawable.icon_boom1,
            R.drawable.icon_boom2,
            R.drawable.icon_boom3,
            R.drawable.icon_boom4,
            R.drawable.icon_boom5
    };

}
