package com.miuhouse.zxcommunity.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by khb on 2016/1/6.
 */
public class StatusCompat {
    public static final int INVALID_VAL = -1;
    public static final int COLOR_DEFAULT = Color.parseColor("#000000");
    public static final int COLOR_TITLE = Color.parseColor("#EF5839");
    private static View statusCompatView;

    public static void compat(Activity activity, int statusColor) {
//        如果android版本为5.0+，直接设置状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (statusColor != INVALID_VAL) {
                activity.getWindow().setStatusBarColor(statusColor);
            }
            return;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            int color = COLOR_DEFAULT;
//            利用系统API获取根布局
            ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
            if (statusColor != INVALID_VAL) {
                color = statusColor;
            }
//            手动建一个view覆盖空出来的状态栏
            statusCompatView = new View(activity);
            ViewGroup.LayoutParams params =
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            getStatusBarHeight(activity));
            statusCompatView.setLayoutParams(params);
            statusCompatView.setBackgroundColor(color);
            contentView.addView(statusCompatView);
        }
    }

    public static void undoCompat(Activity activity){
        ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
        contentView.removeView(statusCompatView);
    }

    private static int getStatusBarHeight(Context context) {
        int result = 0;
//        应该是从系统配置文件中获取status_bar_height的值，即状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
