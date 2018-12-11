package com.zcy.mygames.views.ltzj;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.zcy.mygames.APP;
import com.zcy.mygames.R;
import com.zcy.mygames.utils.BitmapUtils;
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
        fighter = Fighter.createFighter(Fighter.TYPE_MY,R.drawable.icon_ltzj_p06d,0.45f , 0.9f , 0.55f , 0.96f);
    }

    @Override
    protected void drawUi(Canvas canvas) {
        float width = getWidth();
        float height = getHeight();

        fighter.draw(canvas,p,width,height);

    }

    private float previousX;
    private float previousY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                break;
            default:
                translation(event.getX() - previousX,event.getY()-previousY);
                break;
        }
        previousX = event.getX();
        previousY = event.getY();
        return true;
    }

    private void translation(float x, float y ){
        fighter.translation(x/(float) getWidth(),y/(float) getHeight());
    }

}
