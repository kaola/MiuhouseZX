package com.miuhouse.zxcommunity.widget;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.adapter.BaseRVAdapter;
import com.miuhouse.zxcommunity.adapter.MyLinearLayoutManager;
import com.miuhouse.zxcommunity.adapter.PopupAdapter;
import com.miuhouse.zxcommunity.utils.MyUtils;

import java.util.List;

/** 简单自定义的popupWindow，样式固定，只暴露点击事件
 * Created by khb on 2016/1/18.
 */
public class MyPopupWindow extends PopupWindow implements PopupWindow.OnDismissListener {

    private Activity activity;
    private View layoutView;
    private List<String> list;

    public View getParentView() {
        return parentView;
    }

    private View parentView;
    private RecyclerView popupList;
    private OnItemClickListener mOnItemClickListener;
    private PopupWindow popupWindow;

    public int gravity = -1;

    @Override
    public void onDismiss() {
        MyUtils.backgroundAlpha(activity, 1f);
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public MyPopupWindow(Activity activity, List<String> list, View parentView){
        super(activity.getLayoutInflater().inflate(R.layout.popup, null),
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.activity = activity;
        this.layoutView = getContentView();
        this.list = list;
        this.parentView = parentView;
        initView();
    }

    private void initView() {
        popupList = (RecyclerView) layoutView.findViewById(R.id.popupList);
        if (!MyUtils.isEmptyList(list)) {

            PopupAdapter mAdapter = new PopupAdapter(activity, list);
            popupList.setLayoutManager(new MyLinearLayoutManager(activity, mAdapter));
//        popupList.setLayoutManager(new LinearLayoutManager(activity));
            popupList.setAdapter(mAdapter);
            mAdapter.setOnPopupItemClickListener(new PopupAdapter.OnPopupItemClickListener() {
                @Override
                public void onClick(View view, int position) {

                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(position);
                    }
                }
            });
        }
//        popupWindow = new PopupWindow(layoutView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new BitmapDrawable());
//        setAnimationStyle(R.style.popupAnimation);
        setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        setTouchable(true);
        setOutsideTouchable(true);
        setFocusable(true);
        update();
//        showAsDropDown(parentView, 0, 10);
        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    dismiss();
                    return true;
                }
                return false;
            }
        });

        setOnDismissListener(this);
    }

    public void setPopupAdapter(BaseRVAdapter adapter){

    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public MyPopupWindow setShowLocation(int xoff, int yoff){
        if (gravity == -1) {
            showAsDropDown(parentView, xoff, yoff);
        }else{
            showAtLocation(parentView, gravity, xoff, yoff);
        }
        return this;
    }

    public MyPopupWindow setGravity(int gravity){
        if (gravity >= 0){
            this.gravity = gravity;
        }
        return  this;
    }

    public MyPopupWindow setAnimation(int animationStyle){
        setAnimationStyle(animationStyle);
        return this;
    }

}
