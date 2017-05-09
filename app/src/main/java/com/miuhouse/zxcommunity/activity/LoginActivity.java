package com.miuhouse.zxcommunity.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.activity.user.UserInfoActivity;
import com.miuhouse.zxcommunity.application.MyApplication;
import com.miuhouse.zxcommunity.bean.UserBean;
import com.miuhouse.zxcommunity.bean.UserBeanInfo;
import com.miuhouse.zxcommunity.db.AccountDBTask;
import com.miuhouse.zxcommunity.http.FinalData;
import com.miuhouse.zxcommunity.http.GsonRequest;
import com.miuhouse.zxcommunity.http.VolleySingleton;
import com.miuhouse.zxcommunity.utils.AesUtil;
import com.miuhouse.zxcommunity.utils.Constants;
import com.miuhouse.zxcommunity.utils.MyUtils;
import com.miuhouse.zxcommunity.utils.SPUtils;
import com.miuhouse.zxcommunity.utils.StringUtils;
import com.miuhouse.zxcommunity.utils.ToastUtils;
import com.miuhouse.zxcommunity.widget.NotCheckedPopupWindow;
import com.miuhouse.zxcommunity.widget.ProgressFragment;
import com.miuhouse.zxcommunity.widget.StatusCompat;

import java.util.HashMap;
import java.util.Map;

//import com.easemob.chat.EMChatManager;
//import com.easemob.chat.EMGroupManager;


/**
 * 登录
 * Created by khb on 2015/8/19.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    public static final String INTENT_ACTION_USER_CHANGE = "com.miuhouse.community.action.USER_CHANGE";

    private static final String TAG = "LoginActivity";
    public static final int REGISTER_TAG = 0;
    public static final int FORGET_TAG = 1;
    private EditText etPassword;

    private EditText etPhone;

    private ImageView imgDelectPhone;

    private ImageView imgDelectPassword;

    private TextView tvRegister; //注册

    private ProgressFragment progressFragment;

    private Button btnLogin;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            showPopupWindow();
        }
    };

    @Override
    public void initTitle() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.titlebar);
        mToolbar.setBackgroundColor(Color.parseColor("#EF5839"));
        mToolbar.setTitle("");
        mToolbar.setNavigationIcon(R.mipmap.back_white);
        setSupportActionBar(mToolbar);
        StatusCompat.compat(this, StatusCompat.COLOR_TITLE);
        TextView tvTiltle = (TextView) findViewById(R.id.title);
        tvTiltle.setTextColor(getResources().getColor(android.R.color.white));
        tvTiltle.setText("快速登录");
    }

    @Override
    public void initView() {

        setContentView(R.layout.activity_login);
        etPhone = (EditText) findViewById(R.id.et_user);

        imgDelectPhone = (ImageView) findViewById(R.id.img_phone);

        imgDelectPassword = (ImageView) findViewById(R.id.img_password);

        etPassword = (EditText) findViewById(R.id.et_password);


        tvRegister = (TextView) findViewById(R.id.tv_register);

        etPhone.setInputType(InputType.TYPE_CLASS_NUMBER);

        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
        imgDelectPhone.setOnClickListener(this);
        imgDelectPassword.setOnClickListener(this);
        imgDelectPassword.setOnClickListener(this);
        tvRegister.setOnClickListener(this);

        findViewById(R.id.tv_forget).setOnClickListener(this);
        findViewById(R.id.img_phone).setOnClickListener(this);

        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            //输入完
            @Override
            public void afterTextChanged(Editable s) {
                if (StringUtils.isEmpty(etPhone.getText().toString())) {
                    imgDelectPhone.setVisibility(View.GONE);
                } else {
                    imgDelectPhone.setVisibility(View.VISIBLE);
                }
            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (StringUtils.isEmpty(etPassword.getText().toString())) {
                    imgDelectPassword.setVisibility(View.GONE);
                } else {
                    imgDelectPassword.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void initData() {
//        List<String>

    }

    @Override
    public void initVariables() {

    }

    @Override
    public String getTag() {
        return null;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_login:
                localLogin();
                break;
            case R.id.tv_forget:
                //忘记密码
                Bundle forgetBundle = new Bundle();
                forgetBundle.putInt("TAG", FORGET_TAG);
                startActivity(RegisterActivity.class, forgetBundle);
                break;
            case R.id.img_phone:
                etPhone.setText("");
                break;
            case R.id.img_password:
                etPassword.setText("");
                break;
            case R.id.tv_register:
                //注册
                Bundle mBundle = new Bundle();
                mBundle.putInt("TAG", REGISTER_TAG);
                startActivity(RegisterActivity.class, mBundle);
                finish();
                break;
        }
    }

    private void localLogin() {
        progressFragment = ProgressFragment.newInstance();
        progressFragment.setCancelable(false);
        String strPassword = etPassword.getText().toString().trim();
        String strUser = etPhone.getText().toString().trim();
        if (StringUtils.isEmpty(strUser)) {
            etPhone.setError("请输入手机号码");
            etPhone.requestFocus();
            return;
        }
        if (!StringUtils.isMobile(strUser)) {
            etPhone.setError("手机号码格式不对");
            etPhone.requestFocus();
            return;
        }
        if (StringUtils.isEmpty(strPassword)) {
            etPassword.setError("请输入密码");
            etPassword.requestFocus();
            return;
        }
        progressFragment.show(getSupportFragmentManager(), "login");
        String urlPath = FinalData.URL_VALUE + "login";
        Map<String, Object> map = new HashMap<>();
        map.put("mobile", strUser);
        try {
            map.put("password", AesUtil.Encrypt(strPassword, AesUtil.cKey));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        map.put("deviceToken", SPUtils.getSPData("token", "token"));
        map.put("type", 4);
        initLogin(map, urlPath);
    }

    private void initLogin(Map<String, Object> map, String urlPath) {
        GsonRequest<UserBeanInfo> custom = new GsonRequest<>(Request.Method.POST, urlPath, UserBeanInfo.class, map, getListener(), getErrorListener());
        VolleySingleton.getInstance(this).addToRequestQueue(custom);
    }

    private Response.Listener<UserBeanInfo> getListener() {
        return new Response.Listener<UserBeanInfo>() {
            @Override
            public void onResponse(UserBeanInfo userBean) {
                if (userBean != null) {
                    if (userBean.getCode() == 0) {
                        AccountDBTask.saveUserBean(userBean.getUserInfo());
                        MyApplication.getInstance().setmUserBean(userBean.getUserInfo());
//                        loginHuanXin(userBean.getUserInfo().getMobile());
                        String id = userBean.getUserInfo().getId();
                        easeAccountLogIn(id, userBean.getUserInfo().getStatus());
                        //当status等于0 审核中 弹出一个对话框 提示审核
//                        if (userBean.getUserInfo().getStatus() == 0) {
//                        showPopupWindow();
//                        }
                    } else
                        ToastUtils.showToast(context, userBean.getMsg());
                    progressFragment.dismissAllowingStateLoss();

                } else {
                    ToastUtils.showToast(context, "登录失败");
                    progressFragment.dismissAllowingStateLoss();

                }
            }
        };
    }

    private void handleLoginSuccess() {
        Log.i("TAG", "handleLoginSuccess");
        this.sendBroadcast(new Intent(INTENT_ACTION_USER_CHANGE));
    }

    private void easeAccountLogIn(String id, final int status) {
        showLogD(" is ease account loggedin before " + EMClient.getInstance().isLoggedInBefore());
        String loginId = Constants.EASEACCOUNTHEAD + id;
//        登录账号前，如果未登出之前的账号会出现异常
        if (EMClient.getInstance().isLoggedInBefore()) {
            easeAccountLogOut(id, status);
            return;
        }
        EMClient.getInstance().login(loginId, Constants.PASSWORD, new EMCallBack() {
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                // 更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
                boolean updatenick = EMClient.getInstance().updateCurrentUserNick(
                        MyApplication.currentUserNick.trim());
                if (!updatenick) {
                    Log.e(getClass().getName(), "update current user nick fail");
                }
                Log.i("TAG", "onSuccess");
                //异步获取当前用户的昵称和头像(从自己服务器获取，demo使用的一个第三方服务)
//                DemoHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();
                showLogD("Fuck Yeah!");
//                登录成功后开启推送服务
//                PushAgent pushAgent = PushAgent.getInstance(activity);
//                pushAgent.enable();
//                pushAgent.onAppStart();

                if (status == UserBean.NEED_CHECKED) {
                    progressFragment.dismissAllowingStateLoss();

                    handler.sendEmptyMessage(1);
                } else {
                    progressFragment.dismissAllowingStateLoss();

                    handleLoginSuccess();
                    setResult(RESULT_OK);
                    finish();
                }
            }

            @Override
            public void onError(int i, String s) {
                showLogD("HX-- Fuck No! " + s);
                Log.i("TAG", "onError");

                progressFragment.dismissAllowingStateLoss();
                handleLoginSuccess();
                finish();
//                showToast(LoginActivity.this.getResources().getString(R.string.login_failed));
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

    private void easeAccountLogOut(final String id, final int status) {
        EMClient.getInstance().logout(false, new EMCallBack() {
            @Override
            public void onSuccess() {
                showLogD("HX-- last account logged out");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                easeAccountLogIn(id, status);
            }

            @Override
            public void onError(int i, String s) {
                showLogD("HX-- last account logging out failed");
                progressFragment.dismiss();
                showToast(getResources().getString(R.string.logout_error));
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

    private Response.ErrorListener getErrorListener() {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {

                progressFragment.dismissAllowingStateLoss();
                if (volleyError instanceof TimeoutError)
                    ToastUtils.showToast(context, "请求超时");
                else if (volleyError instanceof NoConnectionError)
                    ToastUtils.showToast(context, "没有网络连接");
                else if (volleyError instanceof ServerError)
                    ToastUtils.showToast(context, "服务器异常 登录失败");
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);

    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }


    private void showPopupWindow() {
        if (progressFragment != null) {
            progressFragment.dismiss();
        }
        NotCheckedPopupWindow popup = new NotCheckedPopupWindow(this, findViewById(R.id.linear));
        popup.setOnCancelClickListener(new NotCheckedPopupWindow.OnCancelClickListener() {
            @Override
            public void onCancel() {
                handleLoginSuccess();
                setResult(RESULT_OK);
                finish();
            }
        });
        popup.setOnMainClickListener(new NotCheckedPopupWindow.OnMainClickListener() {
            @Override
            public void onClick() {
                handleLoginSuccess();
                startActivity(UserInfoActivity.class);
                finish();
            }
        });
        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                MyUtils.backgroundAlpha(activity, 1f);
            }
        });
        popup.show();
    }
}

