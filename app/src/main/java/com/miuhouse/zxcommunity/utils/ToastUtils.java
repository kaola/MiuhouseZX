package com.miuhouse.zxcommunity.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.miuhouse.zxcommunity.R;


/**
 * Created by khb on 2015/8/20.
 */
public class ToastUtils {

    public static Toast toast = null;
    private static String lastToast = "";
    private static long lastToastTime;

    private ToastUtils() {

    }

    public static void showToast(Context context, String message) {
        if (toast == null) {
            long time = System.currentTimeMillis();
            if (!message.equalsIgnoreCase(lastToast) || Math.abs(lastToastTime - time) > 2000) {
                View layout = LayoutInflater.from(context).inflate(R.layout.toast_layout, null);
                TextView textView = (TextView) layout.findViewById(R.id.toast_tx);
                textView.setText(message);
                Toast toast = new Toast(context);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.setView(layout);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.show();
                lastToast = message;
                lastToastTime = System.currentTimeMillis();
            }
        } else {
            toast.setText(message);
            toast.show();
        }
    }

    public static void showToast(Context context, int resId) {
        showToast(context, context.getString(resId));
    }

}
