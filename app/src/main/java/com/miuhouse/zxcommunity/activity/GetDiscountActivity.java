package com.miuhouse.zxcommunity.activity;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.adapter.DiscountAdapter;
import com.miuhouse.zxcommunity.bean.Discount;
import com.miuhouse.zxcommunity.bean.PropertyInfo;
import com.miuhouse.zxcommunity.http.CustomStringRequest;
import com.miuhouse.zxcommunity.http.FinalData;
import com.miuhouse.zxcommunity.http.VolleySingleton;
import com.miuhouse.zxcommunity.utils.Constants;
import com.miuhouse.zxcommunity.utils.MyCount;
import com.miuhouse.zxcommunity.utils.StringUtils;
import com.miuhouse.zxcommunity.widget.StatusCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by khb on 2016/5/5.
 */
public class GetDiscountActivity extends BaseActivity {

    private EventHandler eventHandler;
    private EditText mEtPhone;
    private TextView mTvGetVerify;
    private EditText mEtVerify;
    private ListView mRvDiscountList;
    private MyCount mc;
    private List<Discount> discounts;
    private PropertyInfo property;
    private TextView title;
    private String phone;
    private TextView commit;

    @Override
    public void initTitle() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.titlebar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.mipmap.back_black);
        setSupportActionBar(toolbar);
        StatusCompat.compat(this, StatusCompat.COLOR_DEFAULT);
        title = (TextView) findViewById(R.id.title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_getdiscount);
        mEtPhone = (EditText) findViewById(R.id.phone);
        mTvGetVerify = (TextView) findViewById(R.id.getVerify);
        mEtVerify = (EditText) findViewById(R.id.verify);
        mRvDiscountList = (ListView) findViewById(R.id.discountList);
        DiscountAdapter dAdapter = new DiscountAdapter(this, discounts);
        mRvDiscountList.setAdapter(dAdapter);
        TextView mTvProperty = (TextView) findViewById(R.id.property);
        mTvProperty.setText(property.getName());
        TextView mTvPrice = (TextView) findViewById(R.id.price);
        mTvPrice.setText(property.getAvgPrice() + "元/平米");
        commit = (TextView) findViewById(R.id.commit);
    }

    @Override
    public void initData() {
        discounts = (List<Discount>) getIntent().getSerializableExtra("discounts");
        property = (PropertyInfo) getIntent().getSerializableExtra("property");
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
            public void afterEvent(int event, int result, final Object data) {
                showLogD("event = " + event + "  result = " + result);
                showLogD("=======GetVerifyResult========");
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
                            }
                        });
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        //获取验证码成功
                        Log.w("TAG", "get code success " + data);
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                        //返回支持发送验证码的国家列表
                        ArrayList<HashMap<String, Object>> countryList = (ArrayList<HashMap<String, Object>>) data;
                        Log.d("TAG", "countryList = " + countryList.toString());
                    }
                } else {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                ((Throwable) data).printStackTrace();
                                Throwable throwable = (Throwable) data;
                                throwable.printStackTrace();
                                JSONObject object = null;
                                object = new JSONObject(throwable.getMessage());
                                String des = object.optString("detail");//错误描述
                                int status = object.optInt("status");//错误代码
                                showLogD("SMS status " + status);
//                                showToast(des);
                                switch (status) {
                                    case 463:
                                        showToast("您的号码今天发送次数已达上限");
                                        break;
                                    case 468:
                                        showToast("验证码错误");
                                        break;
                                    default:
                                        showToast("验证码验证失败");
                                        break;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                showToast("验证码验证失败!!!");
                            }
//                                    progress.dismissAllowingStateLoss();
                        }
                    });

                }
            }
        };
        SMSSDK.registerEventHandler(eventHandler);
        SMSSDK.getSupportedCountries();
    }

    public void sendCode(View view) {
        phone = mEtPhone.getText().toString().trim();
        if (StringUtils.isEmpty(phone)) {
            mEtPhone.setError("请输入手机号码");
            mEtPhone.requestFocus();
            return;
        }
        if (!StringUtils.isMobile(phone)) {
            mEtPhone.setError("手机号码格式不对");
            mEtPhone.requestFocus();
            return;
        }
        SMSSDK.getVerificationCode("86", phone);
        if (mc == null) {
            mc = new MyCount(60000, 1000, mTvGetVerify, this); // 第一参数是总的时间，第二个是间隔时间 都是毫秒为单位
        }
        mc.start();
    }

    public void commit(View view){
        String vCode = mEtVerify.getText().toString();
        SMSSDK.submitVerificationCode(Constants.SMSSDK_COUNTRYCODE, mEtPhone.getText().toString().trim(), vCode);
    }

    private void sendRequest(){
        commit.setEnabled(false);
        String url = FinalData.URL_VALUE + "discountEntry";
        Map<String, Object> map = new HashMap<>();
        map.put("mobile", phone);
        map.put("newPropertyId", property.getId());
        final CustomStringRequest request = new CustomStringRequest(Request.Method.POST, url, map,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        commit.setEnabled(true);
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            int code = jsonObject.getInt("code");
                            String msg = jsonObject.getString("msg");
                            if (code == 0){
                                showToast(getResources().getString(R.string.request_sent_successfully));
                                finish();
                            }else {
                                showToast(msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            showToast("服务器出错");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        commit.setEnabled(true);
                        showToast(getResources().getString(R.string.request_failed));
                        mEtVerify.setText("");
                    }
                });
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }


    @Override
    protected void onDestroy() {
        SMSSDK.unregisterAllEventHandler();
        super.onDestroy();
    }




    @Override
    public void initVariables() {

    }

    @Override
    public String getTag() {
        return null;
    }
}
