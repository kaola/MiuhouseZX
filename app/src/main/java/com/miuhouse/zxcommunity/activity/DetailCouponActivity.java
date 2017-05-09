package com.miuhouse.zxcommunity.activity;

import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.bean.CouponBean;
import com.miuhouse.zxcommunity.bean.DetailCouponBean;
import com.miuhouse.zxcommunity.http.FinalData;
import com.miuhouse.zxcommunity.http.GsonRequest;
import com.miuhouse.zxcommunity.http.VolleySingleton;
import com.miuhouse.zxcommunity.utils.MyUtils;
import com.miuhouse.zxcommunity.widget.StatusCompat;

import java.util.HashMap;
import java.util.Map;

/**
 * 优惠券详细
 * Created by kings on 1/14/2016.
 */
public class DetailCouponActivity extends BaseActivity {
    private static final String TAG = "DetailCouponActivity";
    private TextView tvTiltle;
    private TextView tvTime;
    private TextView tvAddress;
    private TextView tvDetail;
    private String id;

    @Override
    public void initTitle() {
        Toolbar mToobar = (Toolbar) findViewById(R.id.titlebar);
        mToobar.setNavigationIcon(R.mipmap.back_black);
        mToobar.setTitle("");
        setSupportActionBar(mToobar);
        StatusCompat.compat(this, StatusCompat.COLOR_DEFAULT);
        TextView title = (TextView) findViewById(R.id.title);
        title.setText("优惠券详情");
        title.setTextColor(Color.parseColor("#1E2129"));
        title.setClickable(true);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_coupon_detail);
        tvTiltle = (TextView) findViewById(R.id.tv_title);
        tvTime = (TextView) findViewById(R.id.tv_time);
        tvAddress = (TextView) findViewById(R.id.tv_address);
        tvDetail = (TextView) findViewById(R.id.tv_content);
    }

    @Override
    public void initData() {
        sendRequestData();
    }

    @Override
    public void initVariables() {
        id = getIntent().getStringExtra("id");
    }

    @Override
    public String getTag() {
        return null;
    }


    private void sendRequestData() {
        String url = FinalData.URL_VALUE + "coupon";
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        GsonRequest<DetailCouponBean> request = new GsonRequest<>(Request.Method.POST, url, DetailCouponBean.class, map, new Response.Listener<DetailCouponBean>() {
            @Override
            public void onResponse(DetailCouponBean detailCouponBean) {
                fillUi(detailCouponBean.getCoupons());
            }
        }, new ErrorCallback());
        request.setTag(TAG);
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void fillUi(CouponBean mCouponBean) {
        tvTiltle.setText(mCouponBean.getTitle());
        tvTime.setText("有效期 " + MyUtils.getYearTime(mCouponBean.getCreateTime()) + "~" + MyUtils.getYearTime(mCouponBean.getEndTime()));
        tvAddress.setText("仅限此店使用" + "(" + mCouponBean.getAddress() + ")");
        tvDetail.setText(mCouponBean.getNotice());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
