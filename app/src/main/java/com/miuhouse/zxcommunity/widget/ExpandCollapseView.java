package com.miuhouse.zxcommunity.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.miuhouse.zxcommunity.R;

/**
 * Created by khb on 2016/4/27.
 */
public class ExpandCollapseView extends RelativeLayout {

    private Context context;
    private View mainBody;
    private View button;

    public ExpandCollapseView(Context context) {
        this(context, null);
    }

    public ExpandCollapseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public ExpandCollapseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView() {
        button = LayoutInflater.from(context).inflate(R.layout.layout_expand_collapse_button, null);
        RelativeLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(ALIGN_PARENT_BOTTOM);
        button.setLayoutParams(params);
        addView(button);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int height = getHeight();
        int measureedHeight = getMeasuredHeight();

        int containerWidth = MeasureSpec.getSize(widthMeasureSpec);
        int containerHeight = MeasureSpec.getSize(heightMeasureSpec);
        int containerWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int containerHeightMode = MeasureSpec.getMode(heightMeasureSpec);


        int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(containerWidth, MeasureSpec.AT_MOST);
        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(containerHeight,
                containerHeightMode == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST : containerHeightMode);
        mainBody = getChildAt(0);
        mainBody.measure(childWidthMeasureSpec, childHeightMeasureSpec);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
