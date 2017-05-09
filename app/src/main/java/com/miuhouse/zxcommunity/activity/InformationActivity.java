package com.miuhouse.zxcommunity.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.adapter.InformationAdapter;
import com.miuhouse.zxcommunity.adapter.NewsAdapter;
import com.miuhouse.zxcommunity.bean.InformationBean;
import com.miuhouse.zxcommunity.bean.NewInfoListBean;
import com.miuhouse.zxcommunity.bean.NewsInfoBean;
import com.miuhouse.zxcommunity.http.FinalData;
import com.miuhouse.zxcommunity.http.GsonRequest;
import com.miuhouse.zxcommunity.http.VolleySingleton;
import com.miuhouse.zxcommunity.utils.Constants;
import com.miuhouse.zxcommunity.utils.SPUtils;
import com.miuhouse.zxcommunity.widget.StatusCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kings on 12/16/2016.
 */
public class InformationActivity extends BaseActivity implements InformationAdapter.OnCheckmarkClickListener {

    private final static int LOADING_MORE = 1;
    private final static int REFRESH_MORE = 2;

    private int type;

    private TextView title;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int page = 1;
    private InformationAdapter informationAdapter;
    private List<NewsInfoBean> mList = new ArrayList<>();
    private int lastVisibleItem;
    private LinearLayoutManager mLayoutManager;
    private final static int PAGE_SIZE = 20;

    @Override
    public void initTitle() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.titlebar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.mipmap.back_black);
        setSupportActionBar(toolbar);
        StatusCompat.compat(this, StatusCompat.COLOR_DEFAULT);
        title = (TextView) findViewById(R.id.title);
        if (type == Constants.TYPE_GLCS) {
            title.setText("购房常识");
        } else if (type == Constants.TYPE_SNZX) {
            title.setText("室内装修");
        }
        findViewById(R.id.share).setVisibility(View.GONE);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_information);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_news);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2, R.color.refresh_progress_3);
        informationAdapter = new InformationAdapter(this, mList, this);
        mRecyclerView.setAdapter(informationAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == informationAdapter.getItemCount()) {
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
                page = 1;
                sendRequest(REFRESH_MORE);
            }
        });
        mSwipeRefreshLayout.setRefreshing(true);
        sendRequest(REFRESH_MORE);
    }

    private void sendRequest(final int flag) {
        Map<String, Object> map = new HashMap<>();
        String urlPath = null;

        urlPath = FinalData.URL_VALUE + "otherList";
        map.put("type", type);
        map.put("propertyId", SPUtils.getSPData(Constants.PROPERTYID, 0L));
        map.put("page", page);
        map.put("pageSize", PAGE_SIZE);
        GsonRequest<NewInfoListBean> request = new GsonRequest<>(Request.Method.POST, urlPath, NewInfoListBean.class, map, new Response.Listener<NewInfoListBean>() {
            @Override
            public void onResponse(NewInfoListBean newInfoListBean) {

                if (flag == REFRESH_MORE) {
                    if (mList.size() > 0) {
                        mList.clear();
                    }
                }
                if (newInfoListBean.getOtherInfos() != null)
                    mList.addAll(newInfoListBean.getOtherInfos());
                informationAdapter.notifyDataSetChanged();
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
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    @Override
    public void initData() {
        type = getIntent().getIntExtra("type", 0);

    }

    @Override
    public void initVariables() {

    }

    @Override
    public String getTag() {
        return null;
    }

    @Override
    public void onCheckItemClick(int position) {
        Intent intent = new Intent(activity, BrowseActivity.class);
        intent.putExtra(BrowseActivity.BROWSER_KEY, FinalData.URL_OTHER + mList.get(position).getId());
        intent.putExtra("shareContent", mList.get(position).getTitle());
        intent.putExtra("shareImageUrl", mList.get(position).getImage());
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        item.getItemId()==android.R.id.home;
        Log.i("TAG", "item=" + item.getItemId());
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
