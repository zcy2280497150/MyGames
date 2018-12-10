package com.zcy.mygames.views.ltzj;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;

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

    private Bitmap fjBitmap;
    private Bitmap bgBitmap;

    private RectF rectF;
    private RectF rectFBg;

    @Override
    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
        bgBitmap = BitmapFactory.decodeResource(getResources() , R.drawable.icon_bg);
        fjBitmap = BitmapFactory.decodeResource(getResources() , R.drawable.icon_ltzj_p06d);
        rectF = new RectF();
        rectFBg = new RectF();
    }

    @Override
    protected void drawUi(Canvas canvas) {
        float width = getWidth();
        float height = getHeight();

        float len = width / 10f;

        rectFBg.set(0f,0f,width,height);
        canvas.drawBitmap(bgBitmap,null , rectFBg ,p);
        rectF.set(width/2f - len , height * 0.9f - 2f * len , width/2f + len , height * 0.9f);
        canvas.drawBitmap(fjBitmap , null,rectF , p);
    }

}
