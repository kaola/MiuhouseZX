package com.miuhouse.zxcommunity.utils;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.widget.Toast;

import java.lang.ref.WeakReference;

/**
 * Created by kings on 1/5/2016.
 */
public class DoubleClickExitHelp {
    private boolean isDoubleClick;
    private Handler mHandler;
    private Toast mToast;
    private WeakReference<Activity> mActivity;

    public DoubleClickExitHelp(Activity mActivity) {
        this.mActivity = new WeakReference<Activity>(mActivity);
        mHandler = new Handler(Looper.myLooper());
    }

    public boolean isKeyDown(int keyCode, KeyEvent event) {

        if (keyCode != event.KEYCODE_BACK)
            return false;
        if (isDoubleClick) {
            mHandler.removeCallbacks(onBackTimeRunnable);
            if (mToast != null) {
                mToast.cancel();
            }
            return true;
        } else {
            isDoubleClick = true;
            if (mToast == null) {
                mToast = Toast.makeText(mActivity.get(), "再次点击退出应用", Toast.LENGTH_SHORT);
            }
            mToast.show();
            mHandler.postDelayed(onBackTimeRunnable, 2000);
            return true;
        }
    }

    private Runnable onBackTimeRunnable = new Runnable() {
        @Override
        public void run() {
            isDoubleClick = false;
            if (mToast != null) {
                mToast.cancel();
            }
        }
    };
}
