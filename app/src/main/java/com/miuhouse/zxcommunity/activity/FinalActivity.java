package com.miuhouse.zxcommunity.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.utils.L;
import com.miuhouse.zxcommunity.utils.ToastUtils;


/**所有activity可能需要用到的公共方法
 * Created by khb on 2015/8/20.
 */
public class FinalActivity extends AppCompatActivity {


    private final String TAG = this.getClass().getSimpleName();
    /**
     * 消息類型默認Default
     */
    public static int MSGTYPE_DEFAULT = 0;
    /**
     * 消息類型為Info
     */
    public static int MSGTYPE_INFO = 1;
    /**
     * 消息類型為Warning
     */
    public static int MSGTYPE_WARNING = 2;
    /**
     * 消息類型為Error
     */
    public static int MSGTYPE_ERROR = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

//    /**初始化**/
//    protected abstract void init();
//
//    /** 初始化监听器**/
//    protected abstract void initListener();

    /**  得到字符串資源 **/
    public String getResStr(int id)
    {
        return this.getResources().getString(id);
    }

    /** 显示Toast提示(来自res) **/
    protected  void showToast(int resId){
        ToastUtils.showToast(this, resId);
    }

    /** 显示Toast提示(来自String) **/
    protected void showToast(String text){
        ToastUtils.showToast(this, text);
    }



    /** Debug输出Log日志 **/
    protected void showLogD(String msg) {
        L.d(msg);
    }

    /** Error输出Log日志 **/
    protected void showLogE(String msg) {
        L.e(msg);
    }

    protected void showLogI(String msg){
        L.i(msg);
    }

    /** 通过Class跳转界面 **/
    public void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    /** 含有Bundle通过Class跳转界面 **/
    protected void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }else{
            showLogE("there is no activity can handle this intent: " + intent.getAction().toString());
        }
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_right);
    }

    /** 通过Action跳转界面 **/
    protected void startActivity(String action) {
        Intent intent = new Intent();
        intent.setAction(action);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }else{
            showLogE("there is no activity can handle this intent: " + intent.getAction().toString());
        }
    }

    /**含有Date通过Action跳转界面**/
    protected void startActivity(String action,Uri data) {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.setData(data);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }else{
            showLogE("there is no activity can handle this intent: " + intent.getAction().toString());
        }
    }

    /** 含有Bundle通过Action跳转界面 **/
    protected void startActivity(String action, Bundle bundle) {
        Intent intent = new Intent();
        intent.setAction(action);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }else{
            showLogE("there is no activity can handle this intent: " + intent.getAction().toString());
        }
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_right);
    }


    /** 含有标题、内容、两个按钮的对话框 **/
    protected void showAlertDialog(String title, String message,
                                   String positiveText,
                                   DialogInterface.OnClickListener onPositiveClickListener,
                                   String negativeText,
                                   DialogInterface.OnClickListener onNegativeClickListener) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveText, onPositiveClickListener)
                .setNegativeButton(negativeText, onNegativeClickListener).create();
        alertDialog.show();
    }

    /** 含有标题、内容、图标、两个按钮的对话框 **/
    protected void showAlertDialog(String title, String message,
                                   int icon, String positiveText,
                                   DialogInterface.OnClickListener onPositiveClickListener,
                                   String negativeText,
                                   DialogInterface.OnClickListener onNegativeClickListener) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle(title)
                .setMessage(message).setIcon(icon)
                .setPositiveButton(positiveText, onPositiveClickListener)
                .setNegativeButton(negativeText, onNegativeClickListener).create();
        alertDialog.show();
    }


    /**
     *
     * @Function: com.light.mycal.base.BaseActivity
     * @Description:根據不同的信息類型彈出不同等級的對話框
     *
     * @param i  資源id
     * @param iMsgType 信息類型
     *
     */
//    public void ShowMsgResStr(int i, int iMsgType) {
//        String sTitle = getResStr(R.string.app_name);
//        int iconId = 0;
//        if (iMsgType == MSGTYPE_INFO)
//        {
//            sTitle = getResStr(R.string.msgTypeInfo);
//            iconId = R.drawable.msgicon_info;
//        }
//        if (iMsgType == MSGTYPE_WARNING)
//        {
//            sTitle = getResStr(R.string.msgTypeWarning);
//            iconId = R.drawable.msgicon_warning;
//        }
//        if (iMsgType == MSGTYPE_ERROR)
//        {
//            sTitle = getResStr(R.string.msgTypeError);
//            iconId = R.drawable.msgicon_error;
//        }
//        AlertDialog.Builder dlg = new AlertDialog.Builder(this);
//        dlg.setMessage(getResStr(i));
//        dlg.setPositiveButton(getResStr(R.string.msgBoxButtonOk), null);
//        dlg.setTitle(sTitle);
//        dlg.setIcon(iconId);
//        dlg.create();
//        dlg.show();
//    }

    /** 带有右进右出动画的退出 **/
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_right);
    }

    /** 默认退出 **/
    protected void defaultFinish() {
        super.finish();
    }
}
