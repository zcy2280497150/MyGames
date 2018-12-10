package com.zcy.mygames.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public abstract class BaseGameView extends SurfaceView implements SurfaceHolder.Callback , Runnable {

    public Paint p;//画笔
    public float density;
    public SurfaceHolder mHolder;
    public Canvas mCanvas;//绘图的画布
    public boolean mIsDrawing;//控制绘画线程的标志位

    public BaseGameView(Context context) {
        super(context);
        init(context ,null);
    }

    public BaseGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context ,attrs);
    }

    public BaseGameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context ,attrs);
    }

    protected void init(Context context, AttributeSet attrs) {
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
    }

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

    public static final long REFRESH_TIME_INTERVAL = 1000L/50;//刷新的间隔时长

    @Override
    public void run() {
        while (mIsDrawing){
            long startTime = System.currentTimeMillis();
            draw();
            while (System.currentTimeMillis() - startTime < REFRESH_TIME_INTERVAL){
                Thread.yield();//耗时少于刷新间隔，线程等待
            }
        }
    }

    private void draw(){
        try {
            mCanvas = mHolder.lockCanvas();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (null != mCanvas){
                mCanvas.drawColor(Color.BLACK);
                drawUi(mCanvas);
                mHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }

    protected abstract void drawUi(Canvas canvas);

}
