package com.miuhouse.zxcommunity.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.miuhouse.zxcommunity.utils.L;

/** 自定义ViewGroup，水平添加childView时，若childView宽度超出边界，则自动换行
 * Created by khb on 2016/4/13.
 */
public class AutoColumnLinearLayout extends ViewGroup {

    private int containerWidth;
    private int containerHeight;

    private int childHorizontalSpacing = 10;    //子view水平间距
    private int childVerticalSpacing = 10;      //子view垂直间距

    private int totalSpanCount = 0;     //设置的行数，默认为0，表示不限制行数

    public AutoColumnLinearLayout(Context context) {
        super(context);
    }

    public AutoColumnLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoColumnLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setTotalSpanCount(int totalSpanCount) {
        this.totalSpanCount = totalSpanCount;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        containerWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        containerHeight = MeasureSpec.getSize(heightMeasureSpec) - getPaddingBottom() - getPaddingTop();
        int containerWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int containerHeightMode = MeasureSpec.getMode(heightMeasureSpec);

        int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(containerWidth, MeasureSpec.AT_MOST);
        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(containerHeight,
                containerHeightMode == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST : containerHeightMode);

        L.i("containerWidth: " + containerWidth + " ------- containerHeight: " + containerHeight);
        int mUsedWidth = 0;
        int spanCount = 1;
        for (int i=0; i<getChildCount(); i++){
            View childView = getChildAt(i);
            childView.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();
            containerHeight = childHeight > containerHeight ? childHeight : containerHeight;
            L.i("CHILD " + i + " ---- width: " + childWidth + " ____ height: " + childHeight);
            mUsedWidth += childWidth + childHorizontalSpacing;
            if (mUsedWidth > containerWidth){  //超出了父控件宽度，换行
                if (totalSpanCount == 0 || (totalSpanCount>(spanCount+1))){     //没达到设定的行数
                    spanCount++;
                    containerHeight += childHeight;
                    containerHeight += childVerticalSpacing;
                }
            }else{  //没超出
            }
        }

        containerHeight += getPaddingBottom() + getPaddingTop();
//        设定父控件宽高
        setMeasuredDimension(widthMeasureSpec, resolveSize(containerHeight, heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        L.i("ViewGroup changed: " + changed + " -- left: " + l +" -- top: " + t + "-- right: " + r + " -- bottom: " + b);
        int mUsedWidth = l;
        int spanWidth = 0;
        int rowHeight = 0;
        int spanCount = 1;
        for (int i = 0; i<getChildCount(); i++){
            View childView = getChildAt(i);
            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();
            mUsedWidth += childWidth + childHorizontalSpacing;
            if (mUsedWidth > containerWidth) {   //该childview超出边界，换行
                if ( totalSpanCount == 0 || (totalSpanCount>(spanCount+1))){  //没有超过限定行数
                    spanCount++;
                    mUsedWidth = l;
                    spanWidth = 0;
                    rowHeight += childHeight + childVerticalSpacing;
//            坐标值是相对于父控件的
                    childView.layout(spanWidth, rowHeight, spanWidth + childWidth, rowHeight + childHeight);
                }
            }else{
//            坐标值是相对于父控件的
                childView.layout(spanWidth, rowHeight, spanWidth + childWidth, rowHeight + childHeight);
            }
            spanWidth += childWidth + childHorizontalSpacing;
            L.i("left:" + (spanWidth) + " top:" + rowHeight + " right:" + (spanWidth + childWidth) + " bottom:" + (rowHeight + childHeight));
        }

    }
}

