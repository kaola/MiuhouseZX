package com.hyphenate.easeui.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.hyphenate.easeui.R;
import com.hyphenate.easeui.widget.StatusCompat;

public abstract class EaseBaseFragment extends Fragment{
//    protected EaseTitleBar titleBar;
    protected InputMethodManager inputMethodManager;
    protected android.support.v7.widget.Toolbar titleBar;
    protected TextView title;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        titleBar = (EaseTitleBar) getView().findViewById(R.id.title_bar);
        titleBar = (android.support.v7.widget.Toolbar) getView().findViewById(R.id.title_bar);
        titleBar.setTitle("");
        title = (TextView) getView().findViewById(R.id.title);
        titleBar.setNavigationIcon(R.mipmap.back_black);
        ((AppCompatActivity)getActivity()).setSupportActionBar(titleBar);
        StatusCompat.compat(getActivity(), StatusCompat.COLOR_DEFAULT);
        title.setTextColor(Color.parseColor("#1E2129"));
        title.setClickable(true);
        initView();
        setUpView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.action).setVisible(false);
    }

    /**
     * 显示标题栏
     */
    public void ShowTitleBar(){
        if(titleBar != null){
            titleBar.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏标题栏
     */
    public void hideTitleBar(){
        if(titleBar != null){
            titleBar.setVisibility(View.GONE);
        }
    }

    protected void hideSoftKeyboard() {
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 初始化控件
     */
    protected abstract void initView();

    /**
     * 设置属性，监听等
     */
    protected abstract void setUpView();


}
