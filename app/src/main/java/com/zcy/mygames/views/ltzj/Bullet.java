package com.zcy.mygames.views.ltzj;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.DrawableRes;

import com.alibaba.fastjson.JSON;
import com.zcy.mygames.utils.BitmapUtils;
import com.zcy.mygames.utils.MyLog;

import java.util.ArrayList;
import java.util.List;

public class Bullet extends RectF{

    private int type;
    private static List<Bullet> list = new ArrayList<>();
    private static RectF drawRectF = new RectF();
    private Bitmap bitmap;
    private long attack;//攻击力
    private float vx;
    private float vy;

    /**
     * @param type 类型 - 敌人/我
     * @param attack 攻击力
     * @param vx X方向速度
     * @param vy Y方向速度
     * @param resId 资源ID
     * @param left 左
     * @param top 上
     * @param right 右
     * @param bottom 下
     */
    public static void createBullet(int type , long attack , float vx , float vy, @DrawableRes int resId , float left , float top , float right , float bottom ){
//        MyLog.i("发射子弹 ： type = " + type);
        Bullet bullet = null;
        for (Bullet b : list){
            if (Fighter.TYPE_NONE == b.type){
                bullet = b;
                break;
            }
        }

        if (null == bullet){
            bullet = new Bullet();
            list.add(bullet);
        }

        bullet.set(left,top,right,bottom);
        bullet.type = type;
        bullet.attack = attack;
        bullet.vx = vx;
        bullet.vy = vy;
        bullet.bitmap = BitmapUtils.findBitmapById(resId);
        bullet.upRectF();
//        MyLog.i("子弹列表 size = " + list.size());
//        MyLog.i("子弹列表  list =  " + JSON.toJSONString(list));
    }

    public static void calcFighter(Fighter fighter){
        for (Bullet bullet : list){
            if (fighter.isBoom || fighter.type == Fighter.TYPE_NONE)return;
            if (Fighter.TYPE_NONE == bullet.type)continue;
            if (bullet.type != fighter.type && !bullet.isEmpty()){

//                MyLog.i("bullet = " + JSON.toJSONString(bullet));
//                MyLog.i("fighter = " + JSON.toJSONString(fighter));
                if ((Fighter.TYPE_MY == fighter.type && bullet.contains(fighter.centerX(),fighter.centerY())) || ( Fighter.TYPE_ENEMY == fighter.type && RectF.intersects(fighter,bullet))){
                    fighter.beHit(bullet.attack);
                    bullet.release();
                }

            }
        }
    }

    private void upRectF() {
        float width = bitmap.getWidth();
        float height = bitmap.getHeight();
        float dx = (width() - height() * width / height) / 2f;
        inset(dx,0f);
    }

    public static void calcAll(){
        for (Bullet b : list){
            b.calc();
        }
    }
    private void calc(){
        if (Fighter.TYPE_NONE != type){
            offset(vx,vy);
            if (centerX() > 1.2f || centerX() < -0.2f || centerY() < -0.2f || centerY() > 1.2f){
                release();
            }
        }
    }

    private void release() {
        type = Fighter.TYPE_NONE;
        attack = 0L;
        vx = 0f;
        vy = 0f;
    }

    public static void drawAll(Canvas canvas , Paint p , float width , float height){
        for (Bullet b : list){
            b.draw(canvas,p,width,height);
        }
    }

    private void draw(Canvas canvas , Paint p ,float width , float height){
        if (Fighter.TYPE_NONE != type){
            drawRectF.set(centerX()*width - width()/2f * height ,top* height , centerX()*width + width()/2f * height ,bottom*height );

            if (Fighter.TYPE_ENEMY == type){
                canvas.save();
                canvas.rotate(180 , drawRectF.centerX(),drawRectF.centerY());
                canvas.drawBitmap(bitmap,null,drawRectF,p);
                canvas.restore();
            }else {
                canvas.drawBitmap(bitmap , null , drawRectF , p);
            }
        }
    }

}
