package com.zcy.mygames.views.ltzj;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.DrawableRes;

import com.zcy.mygames.utils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

public class Fighter extends RectF{

    public static final int TYPE_NONE = 0;//未分配
    public static final int TYPE_MY = 1;//我
    public static final int TYPE_ENEMY = 2;//敌人

    private int type;
    private Bitmap bitmap;

    private static List<Fighter> list = new ArrayList<>();
    private static RectF drawRectF = new RectF();

    private Fighter() {
    }

    public static Fighter createFighter(int type , @DrawableRes int resId , float left , float top , float right , float bottom){
        Fighter fighter = null;
        for (Fighter f : list){
            if (TYPE_NONE == f.type){
                fighter = f;
                break;
            }
        }
        if (null == fighter){
            fighter = new Fighter();
            list.add(fighter);
        }
        fighter.set(left,top,right,bottom);
        fighter.type = type;
        fighter.bitmap = BitmapUtils.findBitmapById(resId);
        fighter.upRectF();
        return fighter;
    }

    private void upRectF(){
        float width = bitmap.getWidth();
        float height = bitmap.getHeight();
        float dx = (width() - height() * width / height) / 2f;
        inset(dx,0f);
    }



    public void draw(Canvas canvas , Paint p ,float width , float height){

        drawRectF.set(centerX()*width - width()/2f * height ,top* height , centerX()*width + width()/2f * height ,bottom*height );

        canvas.drawBitmap(bitmap , null , drawRectF , p);

    }

    public void translation(float x , float y){
        if (centerX() > 0f && centerX() < 1f && centerY() > 0f && centerY() < 1f)
        offset(x,y);
    }

}
