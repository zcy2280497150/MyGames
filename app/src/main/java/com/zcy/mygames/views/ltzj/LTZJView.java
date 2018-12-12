package com.zcy.mygames.views.ltzj;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.zcy.mygames.R;
import com.zcy.mygames.views.BaseGameView;

public class LTZJView extends BaseGameView {

    public LTZJView(Context context) {
        super(context);
    }

    public LTZJView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LTZJView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
        initMyFighter();
    }

    private Fighter fighter;


    private void initMyFighter(){
        fighter = Fighter.createFighter(Fighter.TYPE_MY,5,100,0f,0f,10,R.drawable.icon_ltzj_p06d,R.drawable.icon_bullet,0.45f , 0.9f , 0.55f , 0.96f);
    }

    @Override
    protected void drawUi(Canvas canvas) {

        float width = getWidth();
        float height = getHeight();

        if (Fighter.getEnemyNumber() < 10){
            Fighter.createEnemy();
        }

        Fighter.calcAll();
        Fighter.drawAll(canvas,p,width,height);

    }

    private float previousX;
    private float previousY;
    private float pointerCount;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                break;
            default:
                if (1 == pointerCount && 1 == event.getPointerCount())
                translation(event.getX() - previousX,event.getY()-previousY);
                break;
        }
        previousX = event.getX();
        previousY = event.getY();
        pointerCount = event.getPointerCount();
        return true;
    }

    private void translation(float x, float y ){
        fighter.translation(x/getWidth()*2f,y/getHeight()*2f);
    }

}
