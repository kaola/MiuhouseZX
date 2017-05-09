package com.miuhouse.zxcommunity.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/** 自定义的LinearLayoutManager，
 * 可以让RecyclerView的高度自适应，
 * 需要BaseRVAdapter和其中BaseViewHolder的构造方法提供获取item高度的方法
 * 和BaseRVAdapter配套使用
 * Created by khb on 2016/1/12.
 */
public class MyLinearLayoutManager extends LinearLayoutManager {

    private Context context;
    private BaseRVAdapter adapter;
    public MyLinearLayoutManager(Context context,BaseRVAdapter adapter) {
        super(context);
        this.context = context;
        this.adapter = adapter;
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
//        Recyclerview的第一个item
        View view = recycler.getViewForPosition(0);
        if (view != null){
            try { //不能使用 View view = recycler.getViewForPosition(0);
                // measureChild(view, widthSpec, heightSpec);
                // int measuredHeight view.getMeasuredHeight(); 这个高度不准确
                if(adapter!=null&&adapter.getItemHeight()>0) {
                    int measuredWidth = View.MeasureSpec.getSize(widthSpec);
                    int measuredHeight = adapter.getItemHeight();
                    setMeasuredDimension(measuredWidth, measuredHeight*getItemCount());
                } else{
                    super.onMeasure(recycler,state,widthSpec,heightSpec);
                }
            }catch (Exception e){
                super.onMeasure(recycler,state,widthSpec,heightSpec);
            }
        }
    }



}
