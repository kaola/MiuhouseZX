package com.miuhouse.zxcommunity.widget;

import android.app.Activity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;

import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.utils.MyUtils;

/**
 * 提示未验证账号，或账号仍在审核的弹出窗口
 * Created by khb on 2016/4/6.
 */
public class NotCheckedPopupWindow extends PopupWindow implements PopupWindow.OnDismissListener {

    private Activity activity;
    private View layoutView;

    public View getParentView() {
        return parentView;
    }

    private View parentView;
//    private OnItemClickListener mOnItemClickListener;

    public int gravity = -1;

    @Override
    public void onDismiss() {

    }

    public NotCheckedPopupWindow(Activity activity, View parentView) {
        super(activity.getLayoutInflater().inflate(R.layout.item_popup_checking, null),
                MyUtils.getScreenWidth(activity) - MyUtils.dip2px(activity, 60), MyUtils.dip2px(activity, 300), true);
        this.activity = activity;
        this.layoutView = getContentView();
        this.parentView = parentView;
        initView();
    }

    private void initView() {
        MyUtils.backgroundAlpha(activity, 0.5f);
        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        layoutView.findViewById(R.id.rounded_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mOnCancelClickListener != null) {
                    mOnCancelClickListener.onCancel();
                }
            }
        });
        layoutView.findViewById(R.id.btn_experience).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnMainClickListener != null) {
                    mOnMainClickListener.onClick();
                }
            }
        });
        setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                MyUtils.backgroundAlpha(activity, 1f);
            }
        });
    }

    public void show() {
        showAtLocation(parentView, Gravity.CENTER, 0, 0);
    }

    private OnCancelClickListener mOnCancelClickListener;

    public interface OnCancelClickListener {
        void onCancel();
    }

    public void setOnCancelClickListener(OnCancelClickListener mOnCancelClickListener) {
        this.mOnCancelClickListener = mOnCancelClickListener;
    }

    private OnMainClickListener mOnMainClickListener;

    public interface OnMainClickListener {
        void onClick();
    }

    public void setOnMainClickListener(OnMainClickListener mOnMainClickListener) {
        this.mOnMainClickListener = mOnMainClickListener;
    }

}
