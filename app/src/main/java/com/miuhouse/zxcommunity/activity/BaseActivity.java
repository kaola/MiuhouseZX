package com.miuhouse.zxcommunity.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.application.ActivityManager;
import com.miuhouse.zxcommunity.http.VolleySingleton;
import com.miuhouse.zxcommunity.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

//import com.umeng.common.message.UmengMessageDeviceConfig;


/**
 * 项目
 * Created by khb on 2015/8/19.
 */
public abstract class BaseActivity extends FinalActivity {

    //    protected MyApplication mApplication;
    protected Context context;
    protected Activity activity;

    protected PushAgent mPushAgent;

    /**
     * 处理错误信息
     * 连接失败 服务器处理出错
     **/
    public class ErrorCallback implements Response.ErrorListener {

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            NetworkResponse response = volleyError.networkResponse;


            if (volleyError instanceof NetworkError) {
            } else if (volleyError instanceof ServerError) {
                //服务器出错 重试或者显示相应的错误（服务器处理出错了）
                ToastUtils.showToast(context, "提交失败请重试");
            } else if (volleyError instanceof AuthFailureError) {
                //登录 出错
            } else if (volleyError instanceof ParseError) {
                ToastUtils.showToast(context, "解析出错，请重试");
            } else if (volleyError instanceof NoConnectionError) {
                //连接出错
                ToastUtils.showToast(context, "网络连接失败，请重试");

            } else if (volleyError instanceof TimeoutError) {
                //超时
                ToastUtils.showToast(context, "网络连接超时，请重试");

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mApplication = MyApplication.getInstance();
        context = this;
        activity = this;
        ActivityManager.getInstance().pushActivity(this);
//        if (MyUtils.isLoggedIn()) {
        mPushAgent = PushAgent.getInstance(context);
        mPushAgent.onAppStart();
//            Log.i("umeng push", "onAppStart  "+ mPushAgent.isPushCheck());
//        }
        initVariables();
        initData();
        initView();
        initTitle();
    }

//    public IUmengRegisterCallback mRegisterCallback = new IUmengRegisterCallback() {
//
//        @Override
//        public void onRegistered(String registrationId) {
//            String pkgName = getApplicationContext().getPackageName();
//            String info = String.format("enabled:%s  isRegistered:%s  DeviceToken:%s " +
//                            "SdkVersion:%s AppVersionCode:%s AppVersionName:%s",
//                    mPushAgent.isEnabled(), mPushAgent.isRegistered(),
//                    mPushAgent.getRegistrationId(), MsgConstant.SDK_VERSION,
//                    UmengMessageDeviceConfig.getAppVersionCode(activity), UmengMessageDeviceConfig.getAppVersionName(activity));
////            SPUtils.saveSPData("token", mPushAgent.getRegistrationId());
//            Log.i("TAG", "updateStatus:" + info);
//            Log.i("TAG", "=============================");
//        }
//    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.action).setVisible(false);
        return true;
    }

    @Override
    protected void onStart() {
        MobclickAgent.onPause(this);
        super.onStart();
    }

    @Override
    protected void onResume() {
        MobclickAgent.onResume(this);
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        if (VolleySingleton.getInstance(context).getRequestQueue() != null && getTag() != null) {
            VolleySingleton.getInstance(context).getRequestQueue().cancelAll(getTag());
        }
        super.onStop();

    }

    @Override
    protected void onDestroy() {
//        MyUtils.hideSoftKeyboard(getCurrentFocus());
        ActivityManager.getInstance().popActivity(this);
        super.onDestroy();
    }

    public void back(View view) {
        finish();
    }


    public void getLoadingView() {

    }

    public View getEmptyView() {
        View emptyView = getLayoutInflater().inflate(R.layout.exceptionview, null);
        return emptyView;
    }

    public void getBadNetworkView() {

    }

    /**
     * 初始化标题栏
     */
    public abstract void initTitle();

    /**
     * 处理页面展示
     */
    public abstract void initView();

    /**
     * 处理数据
     */
    public abstract void initData();

    /**
     * 初始化变量
     */
    public abstract void initVariables();

    public abstract String getTag();

}
