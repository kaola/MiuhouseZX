package com.miuhouse.zxcommunity.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.http.FinalData;
import com.miuhouse.zxcommunity.utils.Constants;
import com.miuhouse.zxcommunity.utils.MyUtils;
import com.miuhouse.zxcommunity.utils.SPUtils;
import com.miuhouse.zxcommunity.utils.ToastUtils;
import com.miuhouse.zxcommunity.widget.StatusCompat;

/**
 * 功能库/
 * Created by kings on 1/22/2016.
 */
public class FunctionActivity extends BaseActivity implements View.OnClickListener {

    private final static long PROPERTYID_DEFAULT = 4;
    private long cityId;
    private String propertName;
    private String companyMobile;

    @Override
    public void initTitle() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.titlebar);
        mToolbar.setNavigationIcon(R.mipmap.back_black);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        TextView tvTitle = (TextView) findViewById(R.id.title);
        StatusCompat.compat(this, StatusCompat.COLOR_DEFAULT);
        tvTitle.setText("功能库");
        tvTitle.setTextColor(Color.parseColor("#1E2129"));
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_function);
        findViewById(R.id.linear_tousu).setOnClickListener(this);
        findViewById(R.id.linear_baoxiu).setOnClickListener(this);
        findViewById(R.id.linear_pay_zhe_fees).setOnClickListener(this);
        findViewById(R.id.linear_express).setOnClickListener(this);
        findViewById(R.id.linear_buy).setOnClickListener(this);
        findViewById(R.id.linear_rent).setOnClickListener(this);
        findViewById(R.id.linear_praise).setOnClickListener(this);
        findViewById(R.id.linear_jiaofei).setOnClickListener(this);
        findViewById(R.id.linear_xiaoqu).setOnClickListener(this);
        findViewById(R.id.linear_fabumaifang).setOnClickListener(this);
        findViewById(R.id.linear_fabuzufang).setOnClickListener(this);
        findViewById(R.id.linear_call_Property).setOnClickListener(this);
        findViewById(R.id.buyHouse).setOnClickListener(this);
        findViewById(R.id.rentHouse).setOnClickListener(this);
        findViewById(R.id.linear_glcs).setOnClickListener(this);
        findViewById(R.id.linear_snzx).setOnClickListener(this);
    }

    @Override
    public void initData() {
        cityId = getIntent().getLongExtra("cityId", 0);
        propertName = getIntent().getStringExtra("propertName");
        companyMobile = getIntent().getStringExtra("companyMobile");
    }

    @Override
    public void initVariables() {

    }

    @Override
    public String getTag() {
        return null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.linear_tousu:
                if (!MyUtils.isLoggedIn()) {
                    startActivity(LoginActivity.class);
                } else {
                    startActivity(TousuActivity.class);
                }
                break;
            case R.id.linear_baoxiu:
                if (!MyUtils.isLoggedIn()) {
                    startActivity(LoginActivity.class);
                } else {
                    startActivity(BaoxiuActivity.class);
                }
                break;
            case R.id.linear_pay_zhe_fees:
                //                if (!MyUtils.isLoggedIn()) {
                //                    startActivity(LoginActivity.class);
                //                } else {
                //                    startActivity(PassportActivity.class);
                //                }
                ToastUtils.showToast(this, "暂未开通");
                break;
            case R.id.linear_xiaoqu:
                activity.startActivity(new Intent(activity, NewsListActivity.class)
                    .putExtra(NewsListActivity.TITLE, "正兴简介")
                    .putExtra(NewsListActivity.PROPERTYID,
                        SPUtils.getSPData(Constants.PROPERTYID, PROPERTYID_DEFAULT))
                    .putExtra(NewsListActivity.TYPE, 5));
                break;
            case R.id.linear_express:
                activity.startActivity(
                    new Intent(activity, NewsActivity.class).putExtra("cityId", cityId));
                break;
            case R.id.linear_jiaofei:
                activity.startActivity(new Intent(activity, NewsListActivity.class)
                    .putExtra(NewsListActivity.TITLE, "社区动态")
                    .putExtra(NewsListActivity.PROPERTYID,
                        SPUtils.getSPData(Constants.PROPERTYID, PROPERTYID_DEFAULT))
                    .putExtra(NewsListActivity.TYPE, 6));
                break;
            case R.id.linear_buy:

                Bundle bundle = new Bundle();
                bundle.putInt(HouseListActivity.TAG_PURPOSE, Constants.SELL);
                startActivity(HouseListActivity.class, bundle);
                break;
            case R.id.linear_rent:
                Bundle bundleRent = new Bundle();
                bundleRent.putInt(HouseListActivity.TAG_PURPOSE, Constants.LEASE);
                startActivity(HouseListActivity.class, bundleRent);
                break;
            case R.id.linear_praise:
                if (!MyUtils.isLoggedIn()) {
                    startActivity(LoginActivity.class);
                } else {
                    startActivity(PraiseActivity.class);
                }
                break;
            case R.id.linear_fabumaifang:
                startActivity(new Intent(context, ReleaseHouseActivity.class)
                    .putExtra(ReleaseHouseActivity.TAG_FUNCTON, Constants.SELL));
                break;
            case R.id.linear_fabuzufang:
                startActivity(new Intent(context, ReleaseHouseActivity.class)
                    .putExtra(ReleaseHouseActivity.TAG_FUNCTON, Constants.LEASE));
                break;
            case R.id.linear_call_Property:
                call(companyMobile);
                break;
            case R.id.buyHouse:
                activity.startActivity(new Intent(activity, BrowseActivity.class)
                    .putExtra(BrowseActivity.BROWSER_KEY,
                        FinalData.URL_HEAD + "/mobile/xqwy/"
                            + SPUtils.getSPData(Constants.PROPERTYID, PROPERTYID_DEFAULT))
                    .putExtra("title", "小区无忧")
                    .putExtra("shareContent", propertName)
                );
                break;
            case R.id.rentHouse:
                activity.startActivity(new Intent(activity, NewHouseListActivity.class)
                    .putExtra("propertyId",
                        SPUtils.getSPData(Constants.PROPERTYID, PROPERTYID_DEFAULT))
                    .putExtra("locX", 0d).putExtra("locY", 0d));
                break;
            case R.id.linear_glcs:
                activity.startActivity(
                    new Intent(activity, InformationActivity.class).putExtra("type",
                        Constants.TYPE_GLCS));
                break;
            case R.id.linear_snzx:
                activity.startActivity(
                    new Intent(activity, InformationActivity.class).putExtra("type",
                        Constants.TYPE_SNZX));
                break;
        }
    }

    public void call(String phoneNumber) {
        if (phoneNumber != null) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CALL_PHONE)) {
                    ActivityCompat.requestPermissions(this,
                        new String[] {Manifest.permission.CALL_PHONE}, 1);
                } else {
                    Toast.makeText(this, "权限已被禁止，要想重新开启，请在手机的权限管理中找到"
                        + getResources().getString(R.string.app_name)
                        + "应用，找到拨打电话权限并选择允许", Toast.LENGTH_LONG).show();
                }
            } else {
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri data = Uri.parse("tel:" + phoneNumber);
                intent.setData(data);
                startActivity(intent);
            }
        }
    }
}
