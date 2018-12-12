package com.zcy.mygames.views.ltzj;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.DrawableRes;

import com.zcy.mygames.R;
import com.zcy.mygames.utils.BitmapUtils;
import com.zcy.mygames.utils.MyLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Fighter extends RectF{

    public static final int[] ZJ_DRAWABLE_IDS = {
      R.drawable.icon_enemy_zj01a,
      R.drawable.icon_enemy_zj01b,
      R.drawable.icon_enemy_zj02a,
      R.drawable.icon_enemy_zj02b,
      R.drawable.icon_enemy_zj03a,
      R.drawable.icon_enemy_zj03b,
      R.drawable.icon_enemy_zj04a,
      R.drawable.icon_enemy_zj04b,
      R.drawable.icon_enemy_zj05a,
      R.drawable.icon_enemy_zj05b,
      R.drawable.icon_enemy_zj06a,
      R.drawable.icon_enemy_zj06b,
      R.drawable.icon_enemy_zj07a,
      R.drawable.icon_enemy_zj07b,
      R.drawable.icon_enemy_zj08a,
      R.drawable.icon_enemy_zj08b,
      R.drawable.icon_enemy_zj09a,
      R.drawable.icon_enemy_zj09b,
      R.drawable.icon_enemy_zj10a,
      R.drawable.icon_enemy_zj10b,
      R.drawable.icon_enemy_zj11a,
      R.drawable.icon_enemy_zj11b,
      R.drawable.icon_enemy_zj12a,
      R.drawable.icon_enemy_zj12b,
      R.drawable.icon_enemy_zj13a,
      R.drawable.icon_enemy_zj13b,
      R.drawable.icon_enemy_zj14a,
      R.drawable.icon_enemy_zj14b,
      R.drawable.icon_enemy_zj15a,
      R.drawable.icon_enemy_zj15b
    };


    public static final int TYPE_NONE = 0;//未分配
    public static final int TYPE_MY = 1;//我
    public static final int TYPE_ENEMY = 2;//敌人

    public int type;
    private int bulletResId;
    private Bitmap bitmap;
    public long lifeValue;//生命值
    public long attack;//攻击力
    private float vx;
    private float vy;

    private int launchInterval;//发射间隔

    private static List<Fighter> list = new ArrayList<>();
    private static RectF drawRectF = new RectF();
    private static Fighter mainFighter;

    private static Random random = new Random();

    private Fighter() {
    }

    public static int getEnemyNumber(){
        int number = 0;
        for (Fighter f : list){
            if (TYPE_ENEMY == f.type)number++;
        }
        return number;
    }

    private long boomTime;
    public boolean isBoom;

    /**
     * 被击中
     * @param bak 对方攻击力
     */
    public void beHit(long bak){
//        MyLog.i("被击中");
        lifeValue -= bak;
        if (lifeValue <= 0 ){
            //挂了
            isBoom = true;
            boomTime = System.currentTimeMillis();
        }
    }

    public static void createEnemy(){
        if (1 == random.nextInt(30)){
            float h = 0.03f + random.nextFloat()*0.07f;
            float centerX = random.nextFloat();
            createFighter(TYPE_ENEMY,500,1,0f,0.0003f, 100 + random.nextInt(100), ZJ_DRAWABLE_IDS[random.nextInt(ZJ_DRAWABLE_IDS.length)],R.drawable.icon_enemy_bullet,centerX -0.01f,-h,centerX+0.01f,0f);
        }
    }

    public static Fighter createFighter(int type , long lifeValue ,long attack , float vx , float vy ,int launchInterval ,@DrawableRes int resId ,@DrawableRes int bulletResId , float left , float top , float right , float bottom){
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
        fighter.lifeValue = lifeValue;
        fighter.type = type;
        fighter.bulletResId = bulletResId;
        fighter.attack = attack;
        fighter.vx = vx;
        fighter.vy = vy;
        fighter.isBoom = false;
        fighter.boomTime = 0;
        fighter.launchInterval = launchInterval;
        fighter.bitmap = BitmapUtils.findBitmapById(resId);
        fighter.upRectF();
        if (TYPE_MY == fighter.type){
            mainFighter = fighter;
        }
        return fighter;
    }

    private void upRectF(){
        float width = bitmap.getWidth();
        float height = bitmap.getHeight();
        float dx = (width() - height() * width / height) / 2f;
        inset(dx,0f);
    }

    public static void drawAll(Canvas canvas , Paint p ,float width , float height){
        for (Fighter f : list){
            f.draw(canvas,p,width,height);
        }
        Bullet.drawAll(canvas,p,width,height);
    }

    private void draw(Canvas canvas , Paint p ,float width , float height){

        if (TYPE_NONE != type){

            drawRectF.set(centerX()*width - width()/2f * height ,top* height , centerX()*width + width()/2f * height ,bottom*height );
            canvas.drawBitmap(bitmap , null , drawRectF , p);
            if (isBoom){
                Bitmap boomBitmap = BitmapUtils.getBoomBitmap();

                float len = height * height()/2f;
                drawRectF.set(centerX()*width - len , centerY()*height - len,centerX()*width + len , centerY()*height + len);
                canvas.drawBitmap(boomBitmap ,null ,drawRectF ,p);
            }

        }

    }

    public void translation(float x , float y){
        if (TYPE_MY == type && !isBoom){
            if (0 != x){
                x = x > 0f ? Math.min(x , 1f - centerX()):Math.max(x , -centerX());
            }

            if (0 != y){
                y = y > 0f ? Math.min(y , 1f - centerY()) : Math.max(y , - centerY());
            }

            offset(x,y);
        }
    }

    private void calc(){
        if (TYPE_NONE != type){
            Bullet.calcFighter(this);
            if (!isBoom){
                if (TYPE_MY == type){
                    // TODO: 2018/12/12 主机
                }else if (TYPE_ENEMY == type){
                    offset(vx,vy);
                    if (centerX() > 1.2f || centerX() < -0.2f || centerY() < -0.2f || centerY() > 1.2f){
                        release();
                    }
                }
                launch();
            }else {
                if (System.currentTimeMillis() - boomTime > (TYPE_MY == type ? 5000L : 2000L)){
                    release();
                }
            }
        }
    }

    public static void calcAll(){
        for (Fighter f : list){
            f.calc();
        }
        Bullet.calcAll();
    }

    private int number;

    private void launch(){
        if (launchInterval > 0 && mainFighter.type == TYPE_MY && !mainFighter.isBoom){
            number++;
            if (0 == number%launchInterval ){
                number = 0;
                float bulletVx = 0f;
                float bulletVy = 0f;
                if (TYPE_MY == type){
                    bulletVx = 0f;
                    bulletVy = -0.01f;
                }else if (TYPE_ENEMY == type){
                    bulletVx = -(centerX() - mainFighter.centerX())/200f;
                    bulletVy = -(centerY() - mainFighter.centerY())/200f;
                }
                Bullet.createBullet(type,attack,bulletVx,bulletVy,bulletResId,centerX()-0.01f,centerY()-0.02f,centerX()+0.01f,centerY()+0.02f);
            }
        }
    }

    private void release(){
        type = TYPE_NONE;
        bitmap = null;
        lifeValue = 0L;
        attack = 0L;
        vx = 0f;
        vy = 0f;
        isBoom = false;
        boomTime = 0L;
    }

}
