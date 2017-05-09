package com.miuhouse.zxcommunity.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.miuhouse.zxcommunity.bean.UserBeanInfo;
import com.miuhouse.zxcommunity.db.AccountDBTask;
import com.miuhouse.zxcommunity.http.FinalData;
import com.miuhouse.zxcommunity.http.GsonRequest;
import com.miuhouse.zxcommunity.http.VolleySingleton;
import com.miuhouse.zxcommunity.utils.AesUtil;
import com.miuhouse.zxcommunity.utils.Constants;
import com.miuhouse.zxcommunity.utils.MyCount;
import com.miuhouse.zxcommunity.utils.MyUtils;
import com.miuhouse.zxcommunity.utils.SPUtils;
import com.miuhouse.zxcommunity.utils.StringUtils;
import com.miuhouse.zxcommunity.utils.ToastUtils;
import com.miuhouse.zxcommunity.widget.NotCheckedPopupWindow;
import com.miuhouse.zxcommunity.widget.PassportDialogBuilder;
import com.miuhouse.zxcommunity.widget.ProgressFragment;
import com.miuhouse.zxcommunity.widget.StatusCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;


/**
 * 注册和重置密码
 * Created by khb on 2015/8/19.
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private final static int PASSWORD = 0; //密码
    private final static int PHONE = 2; //手机号码
    /*验证码*/
//    private final String SMSSDK_COUNTRYCODE = "86";

    //    private final static String SMSSDK_APP_KEY = "a38245d89da2";
//    private final static String SMSSDK_APP_SECRET = "84db45a5b7dcde895fc72f47edf53447";
    private final static String SMSSDK_APP_KEY = "cb5f2c4ff20c";
    private final static String SMSSDK_APP_SECRET = "c66e6130d4ecfc5836cef6b3b52d64d2";
    private EventHandler eventHandler;
    /*end*/
    private EditText etPhone;
    private EditText etPassword;
    private Button btnRight;
    //    private EditText etTwoPassword;
    private EditText etCode;
    private TextView btnSendCode;
    private MyCount mc;
    private boolean isMsg = false;
    private ProgressFragment progress;
    private String strPhone;
    private String password;
    private ImageView imgDelectPassword;
    //    private ImageView imgDelectPasswordTwo;
    private ImageView imgDelectPhone;
    private ImageView imgDelectCode;
    private int tag;
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
        tvTiltle.setText("手机注册");
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_register);
        etPhone = (EditText) findViewById(R.id.et_user);
        etPassword = (EditText) findViewById(R.id.et_password);
        btnRight = (Button) findViewById(R.id.btn_register);
        etCode = (EditText) findViewById(R.id.et_code);
        btnSendCode = (TextView) findViewById(R.id.btn_send_code);

        imgDelectPassword = (ImageView) findViewById(R.id.img_password);
        imgDelectPhone = (ImageView) findViewById(R.id.img_phone);
        imgDelectCode = (ImageView) findViewById(R.id.img_code);
        if (tag == LoginActivity.FORGET_TAG) {
            etPassword.setHint("新密码");
            btnRight.setText("重置密码");
        } else {
            etPassword.setHint("密码");
            btnRight.setText("注册");
        }
        btnSendCode.setOnClickListener(this);
        btnRight.setOnClickListener(this);
        imgDelectPassword.setOnClickListener(this);
        imgDelectPhone.setOnClickListener(this);
        imgDelectCode.setOnClickListener(this);
        etPassword.addTextChangedListener(getTextWatcher(PASSWORD));
        etPhone.addTextChangedListener(getTextWatcher(PHONE));

    }

    @Override
    public void initData() {
        SMSSDK.initSDK(activity, Constants.SMSSDK_APP_KEY, Constants.SMSSDK_APP_SECRET);
        eventHandler = new EventHandler() {
            @Override
            public void onRegister() {
                super.onRegister();
            }

            @Override
            public void onUnregister() {
                super.onUnregister();
            }

            @Override
            public void beforeEvent(int i, Object o) {
                super.beforeEvent(i, o);
            }

            @Override
            public void afterEvent(int event, int result, Object data) {
                Log.w("TAG", "event = " + event + "  result = " + result);
                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                        Map<String, Object> map = (HashMap<String, Object>) data;
                        Log.w("TAG", "EVENT_SUBMIT_VERIFICATION_CODE " + map.toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sendRequest();
//                                goToStepTwo();
                            }
                        });
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        //获取验证码成功
                        isMsg = true;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showToast(getResources().getString(R.string.verifycode_sent));
                            }
                        });
                        Log.w("TAG", "get code success");
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                        //返回支持发送验证码的国家列表
                        ArrayList<HashMap<String, Object>> countryList = (ArrayList<HashMap<String, Object>>) data;
                        Log.d("TAG", "countryList = " + countryList.toString());
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showToast(activity, "验证码验证失败");
                            if (progress!=null)
                            progress.dismissAllowingStateLoss();
                        }
                    });

                    ((Throwable) data).printStackTrace();
                }
            }
        };
        SMSSDK.registerEventHandler(eventHandler);
        SMSSDK.getSupportedCountries();

    }

    @Override
    public void initVariables() {
        MyUtils.init(RegisterActivity.this);
        tag = getIntent().getIntExtra("TAG", 0);
    }

    @Override
    public String getTag() {
        return null;
    }


    private TextWatcher getTextWatcher(final int index) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                switch (index) {
                    case PASSWORD:
                        if (StringUtils.isEmpty(etPassword.getText().toString()))
                            imgDelectPassword.setVisibility(View.GONE);
                        else
                            imgDelectPassword.setVisibility(View.VISIBLE);
                        break;

                    case PHONE:
                        if (StringUtils.isEmpty(etPhone.getText().toString()))
                            imgDelectPhone.setVisibility(View.GONE);
                        else
                            imgDelectPhone.setVisibility(View.VISIBLE);
                        break;

                }
            }
        };
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_register:
                //注册
                register();
                break;
            case R.id.btn_send_code:
                sendCode();
                break;
            case R.id.img_password:
                etPassword.setText("");
                break;

            case R.id.img_phone:
                etPhone.setText("");
                break;
            case R.id.img_code:
                etCode.setText("");
                break;
        }

    }

    private void sendCode() {
        strPhone = etPhone.getText().toString().trim();
        if (StringUtils.isEmpty(strPhone)) {
            etPhone.setError("请输入手机号码");
            etPhone.requestFocus();
            return;
        }
        if (!StringUtils.isMobile(strPhone)) {
            etPhone.setError("手机号码格式不对");
            etPhone.requestFocus();
            return;
        }
        SMSSDK.getVerificationCode("86", strPhone);
        if (mc == null) {
            mc = new MyCount(60000, 1000, btnSendCode, this); // 第一参数是总的时间，第二个是间隔时间 都是毫秒为单位
        }
        mc.start();
    }


    private void register() {
        password = etPassword.getText().toString().trim();
        if (StringUtils.isEmpty(password)) {
            etPassword.setError("请输入您的密码");
            etPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            etPassword.setError("密码必须在6-12位之间");
            etPassword.requestFocus();
            return;
        }
        if (password.length() > 12) {
            etPassword.setError("密码必须在6-12位之间");
            etPassword.requestFocus();
            return;
        }


//        if (!cbxPrivacy.isChecked()) {
//            ToastUtils.showToast(getActivity(), "请选择使用协议和隐私条款");
//            return;
//        }
        String vCode = etCode.getText().toString();

//        sendRequest();

        if (StringUtils.isEmpty(vCode)) {
            ToastUtils.showToast(activity, "请输入验证码");
            return;
        }
        if (!StringUtils.isMobile(etPhone.getText().toString().trim())) {
            ToastUtils.showToast(activity, "请输入正确的电话格式");
            return;
        }
        SMSSDK.submitVerificationCode(Constants.SMSSDK_COUNTRYCODE, etPhone.getText().toString().trim(), vCode);
//
        progress = ProgressFragment.newInstance();
//        progress.show(getSupportFragmentManager(), "register");

    }

    private void sendRequest() {
     if (progress==null){
         progress = ProgressFragment.newInstance();

     }
        progress.show(getSupportFragmentManager(), "register");


        String urlPath;
        if (tag == LoginActivity.FORGET_TAG) {
            urlPath = FinalData.URL_VALUE + "reset";
        } else {
            urlPath = FinalData.URL_VALUE + "regist";

        }
        Map<String, Object> map = new HashMap<>();
        map.put("mobile", etPhone.getText().toString().trim());
        try {

            map.put("password", AesUtil.Encrypt(etPassword.getText().toString().trim(), AesUtil.cKey));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        map.put("deviceToken", SPUtils.getSPData("token", "token"));
        map.put("type", 4);
        GsonRequest<UserBeanInfo> custom = new GsonRequest<>(Request.Method.POST, urlPath, UserBeanInfo.class, map, getVerifyListener(), getVerifyErrorListener());
        VolleySingleton.getInstance(RegisterActivity.this).addToRequestQueue(custom);
    }

    public Response.Listener<UserBeanInfo> getVerifyListener() {
        return new Response.Listener<UserBeanInfo>() {

            @Override
            public void onResponse(UserBeanInfo response) {
                // TODO Auto-generated method stub

                if (response.getCode() == 0) {
//                    ToastUtils.showToast(RegisterActivity.this, response.getMsg());
                    if (response != null) {
                        AccountDBTask.saveUserBean(response.getUserInfo());
                        MyApplication.getInstance().setmUserBean(response.getUserInfo());
//                        loginHuanXin(userBean.getUserInfo().getMobile());
                        String id = response.getUserInfo().getId();
                        easeAccountLogIn(id, response.getUserInfo().getStatus());
                    }
                } else {
                    ToastUtils.showToast(RegisterActivity.this, response.getMsg());
                    progress.dismissAllowingStateLoss();

                }

            }
        };
    }

    private void handleLoginSuccess() {
        Log.i("TAG", "handleLoginSuccess");
        this.sendBroadcast(new Intent(LoginActivity.INTENT_ACTION_USER_CHANGE));
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
//                progressFragment.dismissAllowingStateLoss();
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                // 更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
                boolean updatenick = EMClient.getInstance().updateCurrentUserNick(
                        MyApplication.currentUserNick.trim());
                if (!updatenick) {
                    Log.e(getClass().getName(), "update current user nick fail");
                }
                Log.i("TAG", "onSuccess");
                handleLoginSuccess();
                //异步获取当前用户的昵称和头像(从自己服务器获取，demo使用的一个第三方服务)
//                DemoHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();
                showLogD("Fuck Yeah!");
//                登录成功后开启推送服务
//                PushAgent pushAgent = PushAgent.getInstance(activity);
//                pushAgent.enable();
//                pushAgent.onAppStart();
                progress.dismissAllowingStateLoss();

                if (status == 0) {

//                    handler.sendEmptyMessage(1);
                    PassportDialogBuilder passportDialogBuilder = PassportDialogBuilder.getInstance(RegisterActivity.this);
                    passportDialogBuilder.setButton1Click(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setResult(RESULT_OK);
                            finish();
                        }
                    }).setButton2Click(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(UserInfoActivity.class);
                            finish();
                        }
                    });
                    passportDialogBuilder.show();
                } else {
                    setResult(RESULT_OK);
                    finish();
                }
            }

            @Override
            public void onError(int i, String s) {
                showLogD("HX-- Fuck No! " + s);
                Log.i("TAG", "onError");

                progress.dismiss();
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
                progress.dismiss();
                showToast(getResources().getString(R.string.logout_error));
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

    public Response.ErrorListener getVerifyErrorListener() {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.i("TAG", "onErrorResponse=" + error.getMessage());
                progress.dismissAllowingStateLoss();
                if (error instanceof TimeoutError)
                    ToastUtils.showToast(context, "请求超时");
                else if (error instanceof NoConnectionError)
                    ToastUtils.showToast(context, "没有网络连接");
                else if (error instanceof ServerError)
                    ToastUtils.showToast(context, "服务器异常 注册失败");
            }
        };
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mc != null) {
            mc.cancel();
        }
        SMSSDK.unregisterEventHandler(eventHandler);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showPopupWindow() {
        if (progress != null) {
            progress.dismiss();
        }
        NotCheckedPopupWindow popup = new NotCheckedPopupWindow(this, findViewById(R.id.linear));
        popup.setOnCancelClickListener(new NotCheckedPopupWindow.OnCancelClickListener() {
            @Override
            public void onCancel() {
                setResult(RESULT_OK);
                finish();
            }
        });
        popup.setOnMainClickListener(new NotCheckedPopupWindow.OnMainClickListener() {
            @Override
            public void onClick() {
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
