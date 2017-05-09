package com.miuhouse.zxcommunity.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.miuhouse.zxcommunity.R;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.DialogPlusBuilder;
import com.orhanobut.dialogplus.ListHolder;
import com.orhanobut.dialogplus.OnItemClickListener;

/**
 * Created by khb on 2016/3/31.
 */
public class BottomListDrawer {

    private DialogPlusBuilder dialogPlusBuilder;
    private DialogPlus dialogPlus;
    private final TextView title;

    public BottomListDrawer(Context context){
        dialogPlusBuilder = DialogPlus.newDialog(context);
        dialogPlusBuilder.setGravity(Gravity.BOTTOM);
        dialogPlusBuilder.setContentHolder(new ListHolder());
        dialogPlusBuilder.setExpanded(false)
                .setCancelable(true)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
//        默认Header
        View header = LayoutInflater.from(context).inflate(R.layout.dialog_head_price, null);
        title = (TextView) header.findViewById(R.id.dialog_title);
        dialogPlusBuilder.setHeader(header);
//        左侧取消按钮，可能是由于事件被抢占，无法调用onClick，在此取消
        header.findViewById(R.id.tv_dismiss).setVisibility(View.GONE);

    }

//    public DialogPlus create(){
//        dialogPlus = dialogPlusBuilder.create();
//        return dialogPlus;
//    }

    public void show(){
        dialogPlus = dialogPlusBuilder.create();
        dialogPlus.show();
    }

    public BottomListDrawer setTitle(String title){
        this.title.setText(title);
        return this;
    }

    public BottomListDrawer setHeader(View view){
        dialogPlusBuilder.setHeader(view);
        return this;
    }

    public BottomListDrawer setHeader(int resId){
        dialogPlusBuilder.setHeader(resId);
        return this;
    }

    public BottomListDrawer setAdapter(BaseAdapter adapter){
        dialogPlusBuilder.setAdapter(adapter);
        return this;
    }

    public void dismiss(){
        if (dialogPlus != null) {
            dialogPlus.dismiss();
        }
    }

    public BottomListDrawer setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        dialogPlusBuilder.setOnItemClickListener(mOnItemClickListener);
        return this;
    }


    /*private interface OnDismissListener{
        void onDismiss();
    }
    private OnDismissListener mOnDismissListener;
    public void setOnDismissListener(OnDismissListener mOnDismissListener){
        this.mOnDismissListener = mOnDismissListener;
    }*/

}
