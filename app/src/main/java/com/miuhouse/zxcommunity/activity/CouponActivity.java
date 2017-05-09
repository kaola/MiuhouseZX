package com.miuhouse.zxcommunity.activity;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.adapter.CouponAdapter;
import com.miuhouse.zxcommunity.application.MyApplication;
import com.miuhouse.zxcommunity.bean.CouponBean;
import com.miuhouse.zxcommunity.bean.CouponListBean;
import com.miuhouse.zxcommunity.http.FinalData;
import com.miuhouse.zxcommunity.http.GsonRequest;
import com.miuhouse.zxcommunity.http.VolleySingleton;
import com.miuhouse.zxcommunity.utils.MyUtils;
import com.miuhouse.zxcommunity.utils.ToastUtils;
import com.miuhouse.zxcommunity.widget.StatusCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 优惠券
 * Created by kings on 1/12/2016.
 */
public class CouponActivity extends SwipeRefreshBaseActivity {
    private static final int ADD_DATA = 0;//加载更多
    private static final int CLEAR_DATA = 1;//刷新
    private static final int STATE_LOAD_MORE = 0;//服务器还有数据
    private static final int STATE_NO_MORE = 1; //加载返回的数据少于20条 表示服务器已经没有数据了
    public static final String TAG = "CouponActivity";
    private static final int PAGE_SIZE=20;
    private RecyclerView recyclerCoupon;
    private CouponAdapter mCouponAdapter;
    private List<CouponBean> mList = new ArrayList<>();
    private LinearLayoutManager mLayoutManager;
    private int lastVisibleItem;
    private int page = 1;
    private int listViewState;

    private String userId;
    private long propertyID;

    @Override
    public void initTitle() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.titlebar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.mipmap.back_black);
        setSupportActionBar(toolbar);
        StatusCompat.compat(this, StatusCompat.COLOR_DEFAULT);
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(R.string.coupon);
        title.setTextColor(Color.parseColor("#1E2129"));
        title.setClickable(true);
    }

    @Override
    public void initData() {


    }

    @Override
    public void initView() {
        super.initView();
        recyclerCoupon = (RecyclerView) findViewById(R.id.coupon_result);
        mCouponAdapter = new CouponAdapter(this, mList, userId);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerCoupon.setLayoutManager(mLayoutManager);
        recyclerCoupon.setAdapter(mCouponAdapter);
        recyclerCoupon.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //SCROLL_STATE_DELE滑动后停止
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mCouponAdapter.getItemCount() && listViewState == STATE_LOAD_MORE) {
                    mSwipeRefreshLayout.setRefreshing(true);
                    page++;
                    sendRequesData(ADD_DATA);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();

            }
        });
        mSwipeRefreshLayout.setRefreshing(true);
        sendRequesData(ADD_DATA);

    }

    private void sendRequesData(int tag) {
        String url = FinalData.URL_VALUE + "couponList";
        Map<String, Object> map = new HashMap<>();
        map.put("propertyId", propertyID);
        map.put("ownerId", userId);
        map.put("page", page);
        map.put("pageSize", PAGE_SIZE);
        GsonRequest<CouponListBean> request = new GsonRequest<>(Request.Method.POST, url, CouponListBean.class, map, getListener(tag), getErrorListener());
        request.setTag(TAG);
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private Response.ErrorListener getErrorListener() {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                setRefreshing(false);
                ToastUtils.showToast(CouponActivity.this, "获取数据失败");
            }
        };
    }

    private Response.Listener<CouponListBean> getListener(final int tag) {
        return new Response.Listener<CouponListBean>() {
            @Override
            public void onResponse(CouponListBean str) {
                if (mSwipeRefreshLayout.isRefreshing()) {
                    setRefreshing(false);

                }
//                showToast("请求发生错误，请稍后再试");

                if (str.getCode() == 0 && str.getCoupons() != null) {
//
                    if (MyUtils.isEmptyList(str.getCoupons())) {
                        showToast("暂无优惠券");
                        return;
                    }
                    if (str.getCoupons().size() < 20) {
                        listViewState = CouponActivity.STATE_NO_MORE;
                    } else {
                        listViewState = CouponActivity.STATE_LOAD_MORE;
                    }
                    if (tag == ADD_DATA) {
                        mList.addAll(str.getCoupons());
                    } else {
                        mList.clear();
                        mList.addAll(str.getCoupons());
                    }

                    mCouponAdapter.notifyDataSetChanged();
//                    }
                } else {
                    showToast("请求发生错误，请稍后再试");

                }
            }
        };
    }

    @Override
    public void initVariables() {

        userId = MyApplication.getInstance().getUserBean().getId();
        propertyID = MyApplication.getInstance().getUserBean().getPropertyId();

    }

    @Override
    public String getTag() {
        return TAG;
    }


    @Override
    protected int provideContentViewId() {
        return R.layout.activity_coupon;
    }

    @Override
    public void requestDataRefresh() {
        page = 1;
        sendRequesData(CLEAR_DATA);
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
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
//    }
}
