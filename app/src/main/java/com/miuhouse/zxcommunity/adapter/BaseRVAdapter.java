package com.miuhouse.zxcommunity.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import java.util.List;

/**
 * Created by khb on 2015/8/24.
 */
public abstract class BaseRVAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    public List<T> mList;
    public Activity mContext;
    public LayoutInflater mLayoutInflater;
    private int itemHeight;
    public BaseRVAdapter(Activity mContext, List<T> mList){
        this.mContext = mContext;
        this.mList = mList;
        this.mLayoutInflater = mContext.getLayoutInflater();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public abstract RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) ;

    @Override
    public abstract void onBindViewHolder(RecyclerView.ViewHolder holder, int position) ;

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public int getItemHeight(){
        return itemHeight;
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder{

        public BaseViewHolder(final View itemView) {
            super(itemView);
//            记录item的高度，用于MyLinearLayoutManager显示RecyclerView时调整高度
            itemView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    itemHeight = itemView.getMeasuredHeight();
                    return true;
                }
            });
        }
    }

}
