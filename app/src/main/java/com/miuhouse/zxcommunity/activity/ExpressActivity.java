package com.miuhouse.zxcommunity.activity;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.application.MyApplication;
import com.miuhouse.zxcommunity.bean.BaseBean;
import com.miuhouse.zxcommunity.bean.UserBean;
import com.miuhouse.zxcommunity.http.FinalData;
import com.miuhouse.zxcommunity.http.GsonRequest;
import com.miuhouse.zxcommunity.http.VolleySingleton;
import com.miuhouse.zxcommunity.utils.MyUtils;
import com.miuhouse.zxcommunity.utils.ToastUtils;
import com.miuhouse.zxcommunity.widget.PassportDialogBuilder;
import com.miuhouse.zxcommunity.widget.ProgressFragment;
import com.miuhouse.zxcommunity.widget.StatusCompat;

import java.util.HashMap;
import java.util.Map;

/**
 * 快递代收
 * Created by kings on 3/16/2016.
 */
public class ExpressActivity extends BaseActivity {


    private static final String TAG = "ExpressActivity";
    // 业主名字
    private EditText etName;
    //楼栋
    private EditText etBuild;
    //时间
    private EditText etTime;
    //用户id
    private String ownerId;
    //小区id
    private long propertyID;

    private ProgressFragment progress;


    @Override
    public void initTitle() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.titlebar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.mipmap.back_black);
        setSupportActionBar(toolbar);
        StatusCompat.compat(this, StatusCompat.COLOR_DEFAULT);
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(R.string.express);
        title.setClickable(true);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_express);
        etName = (EditText) findViewById(R.id.et_name);
        etBuild = (EditText) findViewById(R.id.et_build);
        etTime = (EditText) findViewById(R.id.et_time);
        findViewById(R.id.btn_submit_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void initVariables() {
        if (!MyUtils.isLoggedIn()) {
            startActivity(LoginActivity.class);
            finish();
        } else {
            UserBean mUserBean = MyApplication.getInstance().getUserBean();
            ownerId = mUserBean.getId();
            propertyID = mUserBean.getPropertyId();
            int status = mUserBean.getStatus();
            //判断是否绑定小区
            PassportDialogBuilder.isShowDialog(status, this);
        }
    }

    @Override
    public String getTag() {
        return TAG;
    }

    /**
     * 提交消息
     */
    public void sendRequest() {

        String urlPath = FinalData.URL_VALUE + "express";

        Map<String, Object> map = new HashMap<>();
        if (MyUtils.isEmpty(getEtName())) {
            etName.setError("请输入姓名");
            etName.requestFocus();
            return;
        }
        if (MyUtils.isEmpty(getEtBuild())) {
            etBuild.setError("请输入楼栋号");
            etBuild.requestFocus();
            return;
        }
        if (MyUtils.isEmpty(getEtTime())) {
            etTime.setError("请输入时间");
            etTime.requestFocus();
            return;
        }
        map.put("ownerId", ownerId);
        map.put("ownerName", getEtName());
        map.put("louDong", getEtBuild());
        map.put("receiveTime", getEtTime());
        map.put("propertyId", propertyID);

        progress = ProgressFragment.newInstance();
        progress.show(getSupportFragmentManager(), "express");

        GsonRequest<BaseBean> request = new GsonRequest<>(Request.Method.POST, urlPath, BaseBean.class, map, new Response.Listener<BaseBean>() {
            @Override
            public void onResponse(BaseBean baseBean) {
                progress.dismissAllowingStateLoss();
                ToastUtils.showToast(ExpressActivity.this, baseBean.getMsg());
            }
        }, new ErrorCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
                progress.dismissAllowingStateLoss();

            }
        });
        request.setTag(TAG);
        VolleySingleton.getInstance(this).addToRequestQueue(request);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 业主名字
     *
     * @return
     */
    public String getEtName() {
        return etName.getText().toString().trim();
    }

    /**
     * 楼栋号
     *
     * @return
     */
    public String getEtBuild() {
        return etBuild.getText().toString().trim();
    }

    /**
     * 接受时间
     *
     * @return
     */
    public String getEtTime() {
        return etTime.getText().toString().trim();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyUtils.hideSoftKeyboard(etBuild);
    }
}
