package com.zcy.mygames.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.zcy.mygames.utils.TimeUtils;

import java.util.Random;

public class MineSweepingView extends SurfaceView implements SurfaceHolder.Callback , Runnable {

    private int clickType = MSUnit.CLICK_TYPE_DEFAULT;

    public void setXq(boolean isXq){
        clickType = isXq ? MSUnit.CLICK_TYPE_XQ : MSUnit.CLICK_TYPE_DEFAULT;
    }

    private Paint p;//画笔
    private float density;
    private SurfaceHolder mHolder;
    private Canvas mCanvas;//绘图的画布
    private boolean mIsDrawing;//控制绘画线程的标志位

    public MineSweepingView(Context context) {
        super(context);
        init(context ,null);
    }

    public MineSweepingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context ,attrs);
    }

    public MineSweepingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context ,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        density = getResources().getDisplayMetrics().density;
        mHolder = getHolder();
        mHolder.addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setKeepScreenOn(true);

        p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setTextAlign(Paint.Align.CENTER);
        p.setStrokeCap(Paint.Cap.ROUND);
        p.setColor(Color.WHITE);

        gameRectF = new RectF();
    }

    private SparseArray<MSUnit> bombSparseArray;

    private RectF gameRectF;

    private int state;

    public void reStart(){
        if (null != bombSparseArray){
            if (1 == state)return;
            state = 1;
            MSUnit.isBoom = false;
            MSUnit.isOver = false;
            for (int i = 0 ; i < bombSparseArray.size() ; i++ ){
                MSUnit unit = bombSparseArray.get(i);
                unit.recoveryState();
            }

            Random random = new Random();
            int bombNumber = 0;
            while (bombNumber < BOMB_NUMBER){
                int index = random.nextInt(VERTICAL_NUMBER * HORIZONTAL_NUMBER);
                MSUnit bomb = bombSparseArray.get(index);
                if (!bomb.isBomb()){
                    bomb.setBomb(true);
                    bombNumber++;
                }
            }

            for (int i = 0 ; i < VERTICAL_NUMBER * HORIZONTAL_NUMBER ; i++){
                MSUnit bomb = bombSparseArray.get(i);
                bomb.initBombNumber();//计算一遍每一个周围的炸弹数量
            }
            startTime = System.currentTimeMillis();
            state = 2;

        }else {
            initBombs();
        }
    }

    private void initBombs() {
        if (1 == state)return;
        state = 1;
        int width = getWidth();
        int height = getHeight();

        if (0 == width || 0 == height){
            state = 0;
            return;
        }

        bombSparseArray = new SparseArray<>();
        MSUnit.init(getResources());//初始化基础图片资源


        float lenMin = Math.min(width, height);
        float lenItem = lenMin * 0.8f / Math.max(VERTICAL_NUMBER, HORIZONTAL_NUMBER);

        for (int i = 0 ; i < VERTICAL_NUMBER ; i++){
            for (int j = 0 ; j < HORIZONTAL_NUMBER ; j++){
                MSUnit bomb = new MSUnit(i * VERTICAL_NUMBER + j, false
                        ,new RectF(0.1f * lenMin + j * lenItem + 0.01f * lenItem
                            ,0.2f * lenMin + i * lenItem + 0.01f * lenItem
                            ,0.1f * lenMin + (j+1)*lenItem - 0.01f * lenItem
                            ,0.2f * lenMin + (i+1)*lenItem - 0.01f * lenItem));
                bombSparseArray.put(bomb.getId() , bomb);

                if ( i > 0 ){

                    //与上关联
                    MSUnit tBomb = bombSparseArray.get((i - 1) * VERTICAL_NUMBER + j );
                    bomb.settUnit(tBomb);
                    tBomb.setbUnit(bomb);

                    //与上左关联
                    if (j > 0){
                        MSUnit ltBomb = bombSparseArray.get((i - 1) * VERTICAL_NUMBER + j - 1);
                        bomb.setLtUnit(ltBomb);
                        ltBomb.setRbUnit(bomb);
                    }
                    //与上右关联
                    if (j < HORIZONTAL_NUMBER - 1){
                        MSUnit rtBomb = bombSparseArray.get((i - 1) * VERTICAL_NUMBER + j + 1 );
                        bomb.setRtUnit(rtBomb);
                        rtBomb.setLbUnit(bomb);
                    }
                }

                //与左关联
                if (j > 0){
                    MSUnit lBomb = bombSparseArray.get(bomb.getId() - 1);
                    bomb.setlUnit(lBomb);
                    lBomb.setrUnit(bomb);
                }

            }
        }

        //获取游戏实操作页面所占矩形
        MSUnit bombFirst = bombSparseArray.get(0);
        MSUnit bombLast = bombSparseArray.get(VERTICAL_NUMBER * HORIZONTAL_NUMBER - 1);
        gameRectF.set(bombFirst.rectF.left , bombFirst.rectF.top , bombLast.rectF.right , bombLast.rectF.bottom);

        Random random = new Random();
        int bombNumber = 0;
        while (bombNumber < BOMB_NUMBER){
            int index = random.nextInt(VERTICAL_NUMBER * HORIZONTAL_NUMBER);
            MSUnit bomb = bombSparseArray.get(index);
            if (!bomb.isBomb()){
                bomb.setBomb(true);
                bombNumber++;
            }
        }

        for (int i = 0 ; i < VERTICAL_NUMBER * HORIZONTAL_NUMBER ; i++){
            MSUnit bomb = bombSparseArray.get(i);
            bomb.initBombNumber();//计算一遍每一个周围的炸弹数量
        }
        startTime = System.currentTimeMillis();
        state = 2;
    }

    private long startTime;
    private long endTime;

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mIsDrawing = true;
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mIsDrawing = false;
    }

    @Override
    public void run() {
        while (mIsDrawing){
            long startTime = System.currentTimeMillis();

            if (2 == state && null != bombSparseArray && !MSUnit.isOver){
                int number = 0;
                for (int i = 0 ; i < VERTICAL_NUMBER * HORIZONTAL_NUMBER ; i++){
                    MSUnit bomb = bombSparseArray.get(i);
                    if (!bomb.isOpen)number++;
                }
                if (number <= BOMB_NUMBER){
                    MSUnit.isOver = true;
                }
            }
            if (MSUnit.isOver){
                state = 3;
            }else endTime = System.currentTimeMillis();
            draw();
            while (System.currentTimeMillis() - startTime < 20L){
                Thread.yield();//耗时少于刷新间隔，线程等待
            }
        }
    }

    public static final int VERTICAL_NUMBER = 9;
    public static final int HORIZONTAL_NUMBER = 9;
    public static final int BOMB_NUMBER = 10;

    private void draw(){
        try {
            mCanvas = mHolder.lockCanvas();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (null != mCanvas){

                mCanvas.drawColor(Color.BLACK);

                if (null != bombSparseArray){
                    for (int i = 0 ; i < VERTICAL_NUMBER ; i++){
                        for (int j = 0 ; j < HORIZONTAL_NUMBER ; j++){
                            MSUnit bomb = bombSparseArray.get(i * HORIZONTAL_NUMBER + j);
                            if (null != bomb) bomb.draw(mCanvas , p);
                        }
                    }
                }

                if (MSUnit.isOver){
                    p.setTextSize(3*20);
                    if (MSUnit.isBoom){
                        //挂了
                        p.setColor(Color.RED);
                        mCanvas.drawText("你挂了",gameRectF.centerX(),gameRectF.bottom + density*20f , p);
                    }else {
                        //通关了
                        p.setColor(Color.GREEN);
                        mCanvas.drawText("恭喜你，过关了！",gameRectF.centerX(),gameRectF.bottom + density*20f , p);
                    }
                }

                String text = TimeUtils.formatTimeDJS(endTime - startTime);
                p.setColor(Color.RED);
                p.setTextSize(15f*density);
                mCanvas.drawText(text , gameRectF.centerX() , gameRectF.top * 0.8f , p);

                mHolder.unlockCanvasAndPost(mCanvas);
            }
        }

    }

    private float downX;//按下 X 坐标
    private float downY;//按下 Y 坐标

    /*
    * 触摸事件
    * */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                click(downX,downY,event.getX(),event.getY());
                break;
        }
        return true;
    }

    private boolean isCalcClick;

    private void click(float downX , float downY , float upX , float upY){
        if (isCalcClick)return;
        isCalcClick = true;
        if (MSUnit.isOver){
            if (MSUnit.isBoom){
                // TODO: 2018/12/8 挂了
            }else {
                // TODO: 2018/12/8 本局过关了
            }
        }else {
            if (gameRectF.contains(downX,downY)){
                //按下是在游戏页面内
                if (gameRectF.contains(upX,upY)){
                    //抬起也在矩形内
                    for (int i = 0 ; i < VERTICAL_NUMBER * HORIZONTAL_NUMBER ; i++){
                        MSUnit bomb = bombSparseArray.get(i);
                        if (bomb.rectF.contains(downX,downY)){
                            //按下在该Bomb内
                            if (bomb.rectF.contains(upX,upY)){
                                //抬起也在，判定为有效点击
                                bomb.click(clickType);
                            }
                            break;
                        }
                    }
                }
            }
        }
        isCalcClick = false;
    }

}
