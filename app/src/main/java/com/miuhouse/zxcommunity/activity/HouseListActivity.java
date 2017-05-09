package com.miuhouse.zxcommunity.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.adapter.HouseListAdapter;
import com.miuhouse.zxcommunity.application.MyApplication;
import com.miuhouse.zxcommunity.bean.BaseBean;
import com.miuhouse.zxcommunity.bean.Estate;
import com.miuhouse.zxcommunity.bean.House;
import com.miuhouse.zxcommunity.http.FinalData;
import com.miuhouse.zxcommunity.http.GsonRequest;
import com.miuhouse.zxcommunity.http.VolleySingleton;
import com.miuhouse.zxcommunity.utils.Constants;
import com.miuhouse.zxcommunity.utils.MyUtils;
import com.miuhouse.zxcommunity.utils.SPUtils;
import com.miuhouse.zxcommunity.utils.StringUtils;
import com.miuhouse.zxcommunity.widget.MyPopupWindow;
import com.miuhouse.zxcommunity.widget.ProgressFragment;
import com.miuhouse.zxcommunity.widget.StatusCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by khb on 2016/1/6.
 */
public class HouseListActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "HouseListActivity";
    private RecyclerView mRvHouseList;
    private List<House> mHouseList;
    private List<Estate> mEstateList = new ArrayList<>();
    private String[] mLayoutList;
    private String[] mSellPriceList = new String[]{"50万以下", "50万-70万", "70万-90万", "90万-110万", "110万-130万", "130万-150万", "150万以上"};
    private String[] mLeasePriceList = new String[]{"1500以下", "1500-2000", "2000-2500", "2500-3000", "3000-3500", "3500-4000", "4000以上"};
    private TextView mTvHouseLayout;
    private TextView mTvHouseArea;
    private PopupWindow popupWindow;
    private HouseListAdapter mAdapter;
    private ArrayList<String> estateStringList = new ArrayList<String>();
    private SwipeRefreshLayout mRefresh;

    private long estateId = 0;
    private String huxing;
    private int minPrice;
    private int maxPrice;
    private int page = 1;
    private int lastVisibleItem;
    private LinearLayoutManager mLayoutManager;

    //    private final int MSG_ESTATE = 1;
//    private final int MSG_LAYOUT = 2;
//    对请求数据的处理，默认是刷新list，如果是加载更多则是在原list上加数据
    private final int TAG_DEFAULT = 1;
    private final int TAG_ADD = 2;

    public final static String TAG_PURPOSE = "houseTag";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            switch (msg.what){
//                case MSG_ESTATE:
//                    page = 1;
//                    getHouseList(estateId, huxing, page);
//                    break;
//            }
            showToast("已拉到底部");
        }
    };
    private int firstVisibleItem;
    private int tag;
    private RelativeLayout content;
    private House testhouse;
    private TextView mTvHousePrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initTitle() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.titlebar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.mipmap.back_black);
        setSupportActionBar(toolbar);
        StatusCompat.compat(this, StatusCompat.COLOR_DEFAULT);
        TextView title = (TextView) findViewById(R.id.title);

        title.setText(tag == Constants.SELL ? "二手房" : "租房");
        title.setTextColor(Color.parseColor("#1E2129"));
        title.setClickable(true);
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
        setContentView(R.layout.activity_houseforsale);

        mTvHouseArea = (TextView) findViewById(R.id.houseArea);
        mTvHouseLayout = (TextView) findViewById(R.id.houseLayout);
        mTvHousePrice = (TextView) findViewById(R.id.housePrice);
        mRvHouseList = (RecyclerView) findViewById(R.id.houseList);
        mTvHouseArea.setOnClickListener(this);
        mTvHouseLayout.setOnClickListener(this);
        mTvHousePrice.setOnClickListener(this);
        mAdapter = new HouseListAdapter(activity, mHouseList, tag);
        mAdapter.setOnHouseClickListener(new HouseListAdapter.OnHouseClickListener() {
            @Override
            public void onHouseClick(View view, int position) {
                goToHouseDetail(mHouseList.get(position));
            }
        });
        mLayoutManager = new LinearLayoutManager(activity);
        mRvHouseList.setHasFixedSize(true);
        mRvHouseList.setLayoutManager(mLayoutManager);
        mRvHouseList.setAdapter(mAdapter);

        content = (RelativeLayout) findViewById(R.id.contentLayout);

        mRefresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        mRefresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getHouseList(estateId, huxing, page, TAG_DEFAULT);
            }
        });

        mRvHouseList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == mAdapter.getItemCount()
                        && firstVisibleItem != 0) {
                    getHouseList(estateId, huxing, page, TAG_ADD);
//                    handler.sendEmptyMessage(0);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
                firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();
            }
        });
        if (!MyUtils.isNetworkConnected(this)) {
            mRefresh.setVisibility(View.GONE);
            ViewGroup nonetworkview = (ViewGroup) findViewById(R.id.nonetwork);
            nonetworkview.findViewById(R.id.iv_reload).setOnClickListener(this);
        }
    }

    @Override
    public void initData() {
        tag = getIntent().getIntExtra(TAG_PURPOSE, -1);
        mHouseList = new ArrayList<>();
        mLayoutList = SPUtils.getSPData(Constants.FX);
        ArrayList<String> strings = new ArrayList<>();
        for (int i=0; i<mLayoutList.length; i++){
            strings.add(mLayoutList[i]);
        }
        strings.add(0, "全部");
        mLayoutList = new String[strings.size()];
        for (int i=0; i<strings.size(); i++){
            mLayoutList[i] = strings.get(i);
        }
        estateId = SPUtils.getSPData(Constants.PROPERTYID, 0L);
        getEstateList();
        getHouseList(estateId, null, page, TAG_DEFAULT);
    }

    private void getHouseList(long estateId, String huxing, int currentPage, final int tag) {
        if (!MyUtils.isNetworkConnected(this)) {
            return;
        }
        final ProgressFragment progress = new ProgressFragment();
        progress.setMessage(getResources().getString(R.string.loading));
        progress.show(getSupportFragmentManager(), getClass().getName());
        String url = FinalData.URL_VALUE + (this.tag == Constants.SELL ? "saleRoomList" : "roomList");
        Map<String, Object> map = new HashMap<>();
        if (estateId > 0) {
            map.put("propertyId", estateId);
        } else if (!StringUtils.isEmpty(huxing)) {
            map.put("huxing", huxing);
        }
        if (tag == TAG_ADD) {
            this.page += 1;
            currentPage = this.page;
        } else {
            this.page = 1;
            currentPage = 1;
        }
        if (minPrice != 0){
            map.put("priceMin", minPrice);
        }
        if (maxPrice != 0){
            map.put("priceMax", maxPrice);
        }
        map.put("page", currentPage);
        map.put("pageSize", 20);
        GsonRequest<HouseBean> gRequest = new GsonRequest<HouseBean>(Request.Method.POST, url, HouseBean.class, map,
                new Response.Listener<HouseBean>() {
                    @Override
                    public void onResponse(HouseBean houseBean) {
                        progress.dismiss();
                        mRefresh.setVisibility(View.VISIBLE);
                        if (houseBean != null && houseBean.getCode() == 0) {
                            if (!MyUtils.isEmptyList(houseBean.getList())) {
//                                mHouseList.clear();

                                if (tag == TAG_ADD) {
                                    mHouseList.addAll(houseBean.getList());
                                } else if (tag == TAG_DEFAULT) {
                                    mHouseList.clear();
//                                    mHouseList.add(testhouse);
                                    mHouseList.addAll(houseBean.getList());
                                }
                            } else {
                                if (tag == TAG_DEFAULT) {
                                    mHouseList.clear();
                                    showToast("暂无房源");
                                } else if (tag == TAG_ADD) {
                                    page = 1;
                                }
                            }

                            mAdapter.notifyDataSetChanged();
                        } else {
                            ViewGroup nonetworkview = (ViewGroup) findViewById(R.id.nonetwork);
                            TextView tvMain = (TextView) nonetworkview.findViewById(R.id.tv_main);
                            tvMain.setText("数据错误");
                            TextView tvSub = (TextView) nonetworkview.findViewById(R.id.tv_subtext);
                            tvSub.setText("请稍后再试");
                            showToast("数据错误，请稍后再试");
                        }
                        if (mRefresh.isRefreshing()) {
                            mRefresh.setRefreshing(false);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progress.dismiss();
                mRefresh.setVisibility(View.GONE);
                ViewGroup nonetworkview = (ViewGroup) findViewById(R.id.nonetwork);
                TextView tvMain = (TextView) nonetworkview.findViewById(R.id.tv_main);
                tvMain.setText("请求发生错误");
                TextView tvSub = (TextView) nonetworkview.findViewById(R.id.tv_subtext);
                tvSub.setText("请稍后再试");
                nonetworkview.findViewById(R.id.iv_reload).setVisibility(View.INVISIBLE);
//                showToast("请求发生错误，请稍后再试");
            }
        });
        gRequest.setTag(TAG);
        VolleySingleton.getInstance(activity).addToRequestQueue(gRequest);
    }

    private void getEstateList() {
        String url = FinalData.URL_VALUE + "propertyList";
        Map<String, Object> map = new HashMap<>();
        map.put("city", MyApplication.getInstance().getCity());
        GsonRequest<EstateBean> gRequest = new GsonRequest<EstateBean>(Request.Method.POST, url, EstateBean.class, map,
                new Response.Listener<EstateBean>() {
                    @Override
                    public void onResponse(EstateBean estateBean) {
                        estateStringList.add(0, "全部");
                        if (estateBean != null && estateBean.getCode() == 0) {
                            mEstateList = estateBean.getList();
                            for (Estate estate : mEstateList) {
                                estateStringList.add(estate.getName());
                            }
                        } else {
                            showToast("未找到附近小区");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                showToast("连接发生错误");
            }
        });
        VolleySingleton.getInstance(context).addToRequestQueue(gRequest);
    }

    @Override
    public void initVariables() {

    }

    @Override
    public String getTag() {
        return TAG;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.houseArea:
                popup(estateStringList, mTvHouseArea);
                break;
            case R.id.houseLayout:
                popup(Arrays.asList(mLayoutList), mTvHouseLayout);
                break;
            case R.id.housePrice:
                if (tag == Constants.SELL) {
                    popup(Arrays.asList(mSellPriceList), mTvHousePrice);
                }else if (tag == Constants.LEASE){
                    popup(Arrays.asList(mLeasePriceList), mTvHousePrice);
                }
                break;
            case R.id.iv_reload:
                getHouseList(estateId, null, page, TAG_DEFAULT);
                break;
        }
    }

    private void goToHouseDetail(House house) {
        Intent intent = new Intent(this, HouseDetailActivity.class);
//        intent.putExtra(HouseDetailActivity.ESTATE_NAME, es)
        intent.putExtra(HouseDetailActivity.HOUSE, house);
        intent.putExtra(HouseDetailActivity.TAG_PURPOSE, tag);
        startActivity(intent);
    }

    /**
     * 显示popupwindow
     *
     * @param list 显示在popupwindow上的数据
     * @param view popupwindow的parentview
     */
    private void popup(List<String> list, View view) {

        final MyPopupWindow popupWindow = new MyPopupWindow(this, list, view).setShowLocation(0, 10).setAnimation(R.style.popupAnimation);
        popupWindow.setOnItemClickListener(new MyPopupWindow.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (popupWindow.getParentView().getId() == R.id.houseArea) {
                    if (position == 0) { //选择全部
                        estateId = 0;
                    } else {
                        estateId = Long.parseLong(mEstateList.get(position - 1).getId());
                    }
                    huxing = null;
                    minPrice = 0;
                    maxPrice = 0;
                } else if (popupWindow.getParentView().getId() == R.id.houseLayout) {
                    estateId = 0;
                    if (position == 0) { //选择全部
                        huxing = null;
                    } else {
                        huxing = mLayoutList[position];
                    }
                    minPrice = 0;
                    maxPrice = 0;
                } else if (popupWindow.getParentView().getId() == R.id.housePrice) {
                    estateId = 0;
                    huxing = null;
                    if (tag == Constants.SELL) {
                        switch (position) {
                            case 0:
                                minPrice = 0;
                                maxPrice = 50;
                                break;
                            case 1:
                                minPrice = 50;
                                maxPrice = 70;
                                break;
                            case 2:
                                minPrice = 70;
                                maxPrice = 90;
                                break;
                            case 3:
                                minPrice = 90;
                                maxPrice = 110;
                                break;
                            case 4:
                                minPrice = 110;
                                maxPrice = 130;
                                break;
                            case 5:
                                minPrice = 130;
                                maxPrice = 150;
                                break;
                            case 6:
                                minPrice = 150;
                                maxPrice = 0;
                                break;
                        }
                    }else if (tag == Constants.LEASE){
                        switch (position) {
                            case 0:
                                minPrice = 0;
                                maxPrice = 1500;
                                break;
                            case 1:
                                minPrice = 1500;
                                maxPrice = 2000;
                                break;
                            case 2:
                                minPrice = 2000;
                                maxPrice = 2500;
                                break;
                            case 3:
                                minPrice = 2500;
                                maxPrice = 3000;
                                break;
                            case 4:
                                minPrice = 3000;
                                maxPrice = 3500;
                                break;
                            case 5:
                                minPrice = 3500;
                                maxPrice = 4000;
                                break;
                            case 6:
                                minPrice = 4000;
                                maxPrice = 0;
                                break;
                        }
                    }
                }
                page = 1;
                getHouseList(estateId, huxing, page, TAG_DEFAULT);
                popupWindow.dismiss();
            }
        });
    }

    public class EstateBean extends BaseBean {
        private List<Estate> list;

        public List<Estate> getList() {
            return list;
        }

        public void setList(List<Estate> list) {
            this.list = list;
        }
    }

    public class HouseBean extends BaseBean {
        private List<House> list;

        public List<House> getList() {
            return list;
        }

        public void setList(List<House> list) {
            this.list = list;
        }
    }
}
