package com.miuhouse.zxcommunity.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.miuhouse.zxcommunity.R;


/**
 * Created by khb on 2016/4/5.
 */
public class MyDot extends View {

    private int color = Color.parseColor("#FFFFFF");

    public MyDot(Context context) {
        super(context, null);

    }

    public MyDot(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyDot);
        color = typedArray.getColor(R.styleable.MyDot_dotColor, color);
        typedArray.recycle();
    }

    public MyDot(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(color);
        canvas.drawCircle(getWidth()/2, getHeight()/2, Math.min(getWidth(), getHeight())/2, mPaint);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    public int setColor(int color) {
        this.color = color;
        return this.color;
    }

    public int setColor(int resId, Resources.Theme theme){
        this.color = getResources().getColor(resId, theme);
        return this.color;
    }








}
