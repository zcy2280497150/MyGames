package com.zcy.mygames.views;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.zcy.mygames.R;

/*
* 扫雷游戏单元
* */
public class MSUnit {

    public static boolean isBoom;//是否爆炸（挂了）
    public static boolean isOver;//是否结束（游戏结束，可能挂了，也可能是过关了）
    public static float DENSITY;

    static Bitmap bombBitMap; //炸弹资源
    static Bitmap boomBitMap; //爆炸图资源
    static Bitmap xqBitMap; //旗帜资源

    public static void init(Resources res , int ... resIds){
        DENSITY = res.getDisplayMetrics().density;
        isOver = false;
        isBoom = false;
        bombBitMap = BitmapFactory.decodeResource(res , null == resIds || resIds.length <= 0 ? R.drawable.icon_bomb : resIds[0]);
        boomBitMap = BitmapFactory.decodeResource(res , null == resIds || resIds.length <= 1 ? R.drawable.icon_boom5 : resIds[1]);
        xqBitMap = BitmapFactory.decodeResource(res , null == resIds || resIds.length <= 2 ? R.drawable.icon_xq : resIds[2]);
    }

    public void recoveryState(){
        isOpen = false;
        isXQ = false;
        isBomb = false;
        bombNumber = 0;
    }

    public RectF rectF;
    private int id;
    private boolean isBomb;

    public boolean isOpen;//是否揭开
    private boolean isXQ;//是否插旗

    public MSUnit(int id , boolean isBomb , RectF rectF) {
        this.id = id;
        this.isBomb = isBomb;
        this.rectF = rectF;
    }

    public void draw(Canvas canvas , Paint p){
        p.setColor(Color.WHITE);
        canvas.drawRect(rectF,p);//画出白底
        if (isOver || isBoom){
            //游戏结束绘制所有谜底
            if (isBomb){
                if (isOpen){
                    //已经引爆
                    if (null != boomBitMap) canvas.drawBitmap(boomBitMap , null , rectF ,p);
                }else {
                    if (null != bombBitMap) canvas.drawBitmap(bombBitMap , null , rectF ,p);
                }
            }else {
                if (bombNumber > 0){
                    p.setColor(Color.RED);
                    p.setTextSize(DENSITY*10f);
                    canvas.drawText(String.valueOf(bombNumber),rectF.centerX() , (rectF.bottom + rectF.centerY())/2f,p);
                }
            }
        }else {
            //游戏进行中
            if (isOpen){
                //已经揭开
                if (isBomb){
                    if (null != boomBitMap) canvas.drawBitmap(boomBitMap , null , rectF ,p);//正常走，代码不出错，不会走到这里，揭开了炸弹就应该触发了游戏结束
                }else {
                    if (bombNumber > 0){
                        p.setColor(Color.RED);
                        p.setTextSize(DENSITY*10f);
                        canvas.drawText(String.valueOf(bombNumber),rectF.centerX() , (rectF.bottom + rectF.centerY())/2f,p);
                    }
                }
            }else {
                //未揭开
                p.setColor(Color.GRAY);
                canvas.drawRect(rectF,p);//画上表面灰色遮盖
                if (isXQ){
                    if (null != xqBitMap) canvas.drawBitmap(xqBitMap , null , rectF ,p);
                }
            }
        }
    }


    public static final int CLICK_TYPE_DEFAULT = 0;//默认（默认是揭开）
    public static final int CLICK_TYPE_XQ = 1;//插旗

    public void click(int clickType){
        switch (clickType){
            case CLICK_TYPE_XQ:
                xq();
                break;
            case CLICK_TYPE_DEFAULT:
            default:
                open();
                break;
        }
    }

    //点开
    public void open(){
        if (!isOpen){
            isOpen = true;
            if (isBomb){
                isBoom = true;
                isOver = true;
            } else {
                if (0 == bombNumber){
                    if (null != ltUnit) ltUnit.open();
                    if (null != tUnit) tUnit.open();
                    if (null != rtUnit) rtUnit.open();
                    if (null != lUnit) lUnit.open();
                    if (null != rUnit) rUnit.open();
                    if (null != lbUnit) lbUnit.open();
                    if (null != bUnit) bUnit.open();
                    if (null != rbUnit) rbUnit.open();
                }
            }
        }
    }

    //插旗
    public void xq(){
        isXQ = !isXQ;
    }

    private int bombNumber;//周围炸弹数量

    //计算周围炸弹数量
    public void initBombNumber(){
        bombNumber = isBomb ? 1 : 0;
        if (null != ltUnit && ltUnit.isBomb)bombNumber++;
        if (null != tUnit && tUnit.isBomb)bombNumber++;
        if (null != rtUnit && rtUnit.isBomb)bombNumber++;
        if (null != lUnit && lUnit.isBomb)bombNumber++;
        if (null != rUnit && rUnit.isBomb)bombNumber++;
        if (null != lbUnit && lbUnit.isBomb)bombNumber++;
        if (null != bUnit && bUnit.isBomb)bombNumber++;
        if (null != rbUnit && rbUnit.isBomb)bombNumber++;
    }

    private MSUnit ltUnit;//左上
    private MSUnit tUnit;//上
    private MSUnit rtUnit;//右上
    private MSUnit lUnit;//左
    private MSUnit rUnit;//右
    private MSUnit lbUnit;//左下
    private MSUnit bUnit;//下
    private MSUnit rbUnit;//右下

    public int getId() {
        return id;
    }

    public boolean isBomb() {
        return isBomb;
    }

    public void setBomb(boolean bomb) {
        isBomb = bomb;
    }

    public void setLtUnit(MSUnit ltUnit) {
        this.ltUnit = ltUnit;
    }

    public void settUnit(MSUnit tUnit) {
        this.tUnit = tUnit;
    }

    public void setRtUnit(MSUnit rtUnit) {
        this.rtUnit = rtUnit;
    }

    public void setlUnit(MSUnit lUnit) {
        this.lUnit = lUnit;
    }

    public void setrUnit(MSUnit rUnit) {
        this.rUnit = rUnit;
    }

    public void setLbUnit(MSUnit lbUnit) {
        this.lbUnit = lbUnit;
    }

    public void setbUnit(MSUnit bUnit) {
        this.bUnit = bUnit;
    }

    public void setRbUnit(MSUnit rbUnit) {
        this.rbUnit = rbUnit;
    }
}
