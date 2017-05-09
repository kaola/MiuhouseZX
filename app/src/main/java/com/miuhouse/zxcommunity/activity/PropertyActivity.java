package com.miuhouse.zxcommunity.activity;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.adapter.PropertySelectAdapter;
import com.miuhouse.zxcommunity.application.MyApplication;
import com.miuhouse.zxcommunity.bean.Property;
import com.miuhouse.zxcommunity.bean.PropertyListBean;
import com.miuhouse.zxcommunity.http.FinalData;
import com.miuhouse.zxcommunity.http.GsonRequest;
import com.miuhouse.zxcommunity.http.VolleySingleton;
import com.miuhouse.zxcommunity.utils.ToastUtils;
import com.miuhouse.zxcommunity.widget.ProgressFragment;
import com.miuhouse.zxcommunity.widget.StatusCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 小区选择
 * Created by kings on 1/11/2016.
 */
public class PropertyActivity extends BaseActivity implements AMapLocationListener {

    private PropertySelectAdapter propertyAdapter;
    //小区列表
    private List<Property> propertyList = new ArrayList<>();

    //城市定位
    public AMapLocationClient mLocationClient = null;
    //声明配置定位参数对象
    public AMapLocationClientOption mLocationOption = null;

    public MyApplication myApplication;

    public ProgressFragment progress;

    @Override
    public void initTitle() {

        Toolbar mToolbar = (Toolbar) findViewById(R.id.titlebar);
        mToolbar.setTitle("");
        mToolbar.setNavigationIcon(R.mipmap.back_black);
        setSupportActionBar(mToolbar);
        StatusCompat.compat(this, StatusCompat.COLOR_DEFAULT);
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(R.string.property);
        title.setTextColor(Color.parseColor("#1E2129"));
        title.setClickable(true);

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_property);
        myApplication = MyApplication.getInstance();
        RecyclerView recyclerProperty = (RecyclerView) findViewById(R.id.recycler_property);
        recyclerProperty.setLayoutManager(new LinearLayoutManager(this));
        propertyAdapter = new PropertySelectAdapter(this, propertyList);
        recyclerProperty.setAdapter(propertyAdapter);
        //添加分割线
        propertyAdapter.setOnCheckmarkItemClickListener(new PropertySelectAdapter.OnCheckmarkClickListener() {
            @Override
            public void onCheckItemClick(int position) {

                setResult(RESULT_OK, getIntent().putExtra(("property"), propertyList.get(position).getName()).putExtra(("propertyID"), propertyList.get(position).getId()));
                finish();
            }
        });
        progress = ProgressFragment.newInstance();
        progress.show(this.getSupportFragmentManager(), "property");
        Log.i("TAG", "city=" + myApplication.getCity());

//        if (MyUtils.isEmpty(myApplication.getCity())) {
//            setparameter();
//        } else {
        sendRequesData();
//        }

    }

    private void setparameter() {

        mLocationClient = new AMapLocationClient(this.getApplicationContext());
        mLocationOption = new AMapLocationClientOption();
        mLocationClient.setLocationListener(this);

        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(true);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();

    }


    private void sendRequesData() {
        String url = FinalData.URL_VALUE + "propertyList";
        Map<String, Object> map = new HashMap<>();
//        map.put("city", city);
        GsonRequest<PropertyListBean> request = new GsonRequest<>(Request.Method.POST, url, PropertyListBean.class, map, getListener(), new ErrorCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
                progress.dismiss();
            }
        });
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private Response.Listener<PropertyListBean> getListener() {
        return new Response.Listener<PropertyListBean>() {
            @Override
            public void onResponse(PropertyListBean str) {
                progress.dismiss();
                if (str.getCode() == 0 && str.getList() != null && str.getList().size() > 0) {
                    Log.i("TAG", "str.getList()=" + str.getList().size());
                    propertyList.addAll(str.getList());
                    propertyAdapter.notifyDataSetChanged();
//                    }
                } else {
//                    linearAddCourse.setVisibility(View.VISIBLE);
                }
            }
        };
    }

    @Override

    public void initData() {
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
            this.finish();
        }

        return super.onOptionsItemSelected(item);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null)
            mLocationClient.onDestroy();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        Log.i("TAG", "onLocationChanged");
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                String city = aMapLocation.getCity();

                int length = city.length() - 1;
                city = city.substring(0, length);
                myApplication.setCity(city);
                sendRequesData();
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                progress.dismiss();
                ToastUtils.showToast(this, R.string.location_error);
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }
}
