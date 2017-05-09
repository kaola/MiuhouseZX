package com.miuhouse.zxcommunity.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

import com.miuhouse.zxcommunity.R;


/**
 * Created by khb on 2015/11/6.
 */
public class MyRoundImageView extends ImageView {

    private static final int RADIUS_DEFAULT = 8;
    private static final int TYPE_CIRCLE = 0;
    private static final int TYPE_ROUND = 1;
    private static final int LEFT = 0;
    private static final int TOP = 1;
    private static final int RIGHT = 2;
    private static final int BOTTOM = 3;
    private static final int ALL = -1;


    private int type;
    private Matrix mMatrix;
    private Paint mPaint;
    private int mWidth;
//    圆形图片的半径
    private int mRadius;
    private BitmapShader mBitmapShader;
    private RectF mRoundRect;
    private int mRoundRadius;

    private int side;

    public MyRoundImageView(Context context) {
//        super(context);
        this(context, null);
    }

    public MyRoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mMatrix = new Matrix();
        mPaint = new Paint();

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyRoundImageView);
        mRoundRadius = typedArray.getDimensionPixelSize(R.styleable.MyRoundImageView_radius,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, RADIUS_DEFAULT, getResources().getDisplayMetrics()));
        type = typedArray.getInt(R.styleable.MyRoundImageView_type, TYPE_CIRCLE);
        side = typedArray.getInt(R.styleable.MyRoundImageView_side, -1);
        typedArray.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (type == TYPE_ROUND) {
            mRoundRect = new RectF(0, 0, getWidth(), getHeight());
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (type == TYPE_CIRCLE){
//            搞成正方形
            mWidth = Math.min(getMeasuredWidth(), getMeasuredHeight());
            mRadius = mWidth / 2;
            setMeasuredDimension(mWidth, mWidth);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getDrawable() == null){
            return ;
        }
        setUpShader();
        if (type == TYPE_CIRCLE){
            canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);
        }else if (type == TYPE_ROUND){
            switch (side) {
                case ALL:
                    canvas.drawRoundRect(mRoundRect, mRoundRadius, mRoundRadius, mPaint);
                    break;
                case LEFT:
                    Rect blockL = new Rect(mRoundRadius, 0, getWidth(), getHeight());
                    canvas.drawRect(blockL, mPaint);
                    RectF rectFL = new RectF(0, 0, mRoundRadius * 2, getHeight());
                    canvas.drawRoundRect(rectFL, mRoundRadius, mRoundRadius, mPaint);
                    break;
                case TOP:
                    Rect blockT = new Rect(0, mRoundRadius, getWidth(), getHeight());
                    canvas.drawRect(blockT, mPaint);
                    RectF rectFT = new RectF(0, 0, getWidth(), mRoundRadius * 2);
                    canvas.drawRoundRect(rectFT, mRoundRadius, mRoundRadius, mPaint);
                    break;
                case RIGHT:
                    Rect blockR = new Rect(0, 0, getWidth() - mRoundRadius, getHeight());
                    canvas.drawRect(blockR, mPaint);
                    RectF rectFR = new RectF(getWidth() - mRoundRadius * 2, 0, getWidth(), getHeight());
                    canvas.drawRoundRect(rectFR, mRoundRadius, mRoundRadius, mPaint);
                    break;
                case BOTTOM:
                    Rect blockB = new Rect(0, 0, getWidth(), getHeight() - mRoundRadius);
                    canvas.drawRect(blockB, mPaint);
                    RectF rectFB = new RectF(0, getHeight() - mRoundRadius * 2, getWidth(), getHeight());
                    canvas.drawRoundRect(rectFB, mRoundRadius, mRoundRadius, mPaint);
                    break;
            }
//            canvas.drawRoundRect(mRoundRect, mRoundRadius, mRoundRadius, mPaint);
        }
    }

    private void setUpShader(){
        Drawable drawable = getDrawable();
        if (drawable == null){
            return ;
        }

        Bitmap bitmap = drawableToBitmap(drawable);
        mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        float scale = 1.0f;
//        计算图片放大或缩小的比例
        if (type == TYPE_CIRCLE){
            int bitmapSize = Math.min(bitmap.getHeight(), bitmap.getWidth());
            scale = mWidth * 1.0f / bitmapSize;
        }else if (type == TYPE_ROUND){
            scale = Math.max(getWidth() * 1.0f / bitmap.getWidth(), getHeight() * 1.0f / bitmap.getHeight());
        }
        mMatrix.setScale(scale, scale);
        mBitmapShader.setLocalMatrix(mMatrix);
        mPaint.setShader(mBitmapShader);
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable){
            BitmapDrawable bd = (BitmapDrawable)drawable;
            return bd.getBitmap();
        }
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        int w = getWidth();
        int h = getHeight();
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);

        return bitmap;
    }


    public void setBorderRadius(int radius){
        int radiusPixal = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP
        ,radius, getResources().getDisplayMetrics());
        if (mRoundRadius != radiusPixal){
            mRoundRadius = radiusPixal;
//            重绘view
            invalidate();
        }
    }

    public void setStyle(int type){
        if (this.type != type){
            this.type = type;
            if (this.type != TYPE_CIRCLE && this.type != TYPE_ROUND){
                this.type = TYPE_CIRCLE;
            }
//            重新排版view
            requestLayout();
        }
    }



}


