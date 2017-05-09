package com.miuhouse.zxcommunity.activity;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.adapter.HistoryPassportAdapter;
import com.miuhouse.zxcommunity.application.MyApplication;
import com.miuhouse.zxcommunity.bean.HistoryPassListBean;
import com.miuhouse.zxcommunity.bean.HistoryPassportBean;
import com.miuhouse.zxcommunity.http.FinalData;
import com.miuhouse.zxcommunity.http.GsonRequest;
import com.miuhouse.zxcommunity.http.VolleySingleton;
import com.miuhouse.zxcommunity.widget.StatusCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 历史通行证
 * Created by kings on 3/16/2016.
 */
public class HistoryPassportActivity extends BaseActivity implements HistoryPassportAdapter.OnCheckmarkClickListener {
    private static final String TAG = "HistoryPassportActivity";

    private final static int LOADING_MORE = 1;
    private final static int REFRESH_MORE = 2;
    private RecyclerView mRecyclerView;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private int page = 1;

    private int lastVisibleItem;

    private LinearLayoutManager mLayoutManager;

    private HistoryPassportAdapter adapter;

    private List<HistoryPassportBean> mList = new ArrayList<>();

    private String ownId;

    @Override
    public void initTitle() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.titlebar);
        mToolbar.setNavigationIcon(R.mipmap.back_black);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        StatusCompat.compat(this, StatusCompat.COLOR_DEFAULT);
        TextView title = (TextView) findViewById(R.id.title);
        title.setText("历史访客通行");
        title.setTextColor(Color.parseColor("#1E2129"));
        title.setClickable(true);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_history_passport);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_news);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2, R.color.refresh_progress_3);
        adapter = new HistoryPassportAdapter(this, mList);
        adapter.setOnCheckmarkItemClickListener(this);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()) {
                    mSwipeRefreshLayout.setRefreshing(true);
                    page++;
                    sendRequest(LOADING_MORE);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                page++;
                page = 1;
                sendRequest(REFRESH_MORE);
            }
        });
        mSwipeRefreshLayout.setRefreshing(true);
        sendRequest(REFRESH_MORE);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initVariables() {
        ownId = MyApplication.getInstance().getUserBean().getId();

    }

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public void onCheckItemClick(int position) {

    }

    private void sendRequest(final int flag) {
        String urlPath = FinalData.URL_VALUE + "passCardList";
        Map<String, Object> map = new HashMap<>();
        map.put("page", page);
        map.put("pageSize", 20);
        map.put("ownerId", ownId);
        map.put("propertyId", 4);
        GsonRequest<HistoryPassListBean> request = new GsonRequest<>(Request.Method.POST, urlPath, HistoryPassListBean.class, map, new Response.Listener<HistoryPassListBean>() {
            @Override
            public void onResponse(HistoryPassListBean newInfoListBean) {

                if (flag == REFRESH_MORE) {
                    if (mList.size() > 0) {
                        mList.clear();
                    }
                }
                Log.i("TAG", "SIZE=" + newInfoListBean.getPassCards().size());
                mList.addAll(newInfoListBean.getPassCards());
                adapter.notifyDataSetChanged();
                if (mSwipeRefreshLayout.isRefreshing())
                    mSwipeRefreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (mSwipeRefreshLayout.isRefreshing())
                    mSwipeRefreshLayout.setRefreshing(false);
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
}
