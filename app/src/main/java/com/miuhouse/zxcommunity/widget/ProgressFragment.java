package com.miuhouse.zxcommunity.widget;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.view.ContextThemeWrapper;

/**
 * Created by kings on 8/26/2015.
 */
public class ProgressFragment extends DialogFragment {

    private String message;
    private ProgressDialog dialog;
    private static ProgressFragment  progressFragment;

    public static ProgressFragment newInstance() {
        if (progressFragment==null){
            synchronized(ProgressFragment.class){
                if(progressFragment==null){
                    progressFragment  = new ProgressFragment();
                }
            }
        }
//        ProgressFragment frag = new ProgressFragment();
        progressFragment.setRetainInstance(true);

        return progressFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            dialog = new ProgressDialog(new ContextThemeWrapper(getActivity(), android.R.style.Theme_Holo_Light_Dialog));
        } else {
            dialog = new ProgressDialog(getActivity());
        }
        if (message == null) {
            dialog.setMessage("正加载中...");
        } else {
            dialog.setMessage(message);
        }
        dialog.setIndeterminate(false);
        dialog.setCancelable(true);

        return dialog;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void onCancel(DialogInterface dialog) {


        super.onCancel(dialog);
    }
}

