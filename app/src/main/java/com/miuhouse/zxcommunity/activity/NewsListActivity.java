package com.miuhouse.zxcommunity.activity;

import android.content.Intent;
import android.graphics.Color;
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
import com.miuhouse.zxcommunity.adapter.ImprovedNewsAdapter;
import com.miuhouse.zxcommunity.adapter.NewsAdapter;
import com.miuhouse.zxcommunity.bean.NewInfoListBean;
import com.miuhouse.zxcommunity.bean.NewsInfoBean;
import com.miuhouse.zxcommunity.http.FinalData;
import com.miuhouse.zxcommunity.http.GsonRequest;
import com.miuhouse.zxcommunity.http.VolleySingleton;
import com.miuhouse.zxcommunity.widget.StatusCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by khb on 2016/11/7.
 */
public class NewsListActivity extends BaseActivity implements NewsAdapter.OnCheckmarkClickListener, ImprovedNewsAdapter.OnCheckmarkClickListener {

    private String title;
    private long propertyId;
    private RecyclerView newsList;
    private SwipeRefreshLayout refresh;
    private List<NewsInfoBean> mList;
    private NewsAdapter newsAdapter;

    public static final String TITLE = "title";
    public static final String PROPERTYID = "propertyId";
    public static final String TYPE = "type";
    private int type;
    private ImprovedNewsAdapter zxNewsAdapter;

    @Override
    public void initTitle() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.titlebar);
        mToolbar.setTitle("");
        mToolbar.setNavigationIcon(R.mipmap.back_black);
        setSupportActionBar(mToolbar);
        StatusCompat.compat(this, StatusCompat.COLOR_DEFAULT);
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(this.title);
        title.setTextColor(Color.parseColor("#1E2129"));
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
        setContentView(R.layout.activity_newslist);
        if (title.equals("社区动态")) {
            findViewById(R.id.zxAd).setVisibility(View.GONE);
            findViewById(R.id.tvZxNews).setVisibility(View.GONE);
        }
        newsList = (RecyclerView) findViewById(R.id.newsList);
        mList = new ArrayList<>();
        if (type == 6) {
            newsAdapter = new NewsAdapter(this, mList);
            newsAdapter.setOnCheckmarkItemClickListener(this);
            newsList.setAdapter(newsAdapter);
        } else if (type == 5) {
            zxNewsAdapter = new ImprovedNewsAdapter(this, mList);
            zxNewsAdapter.setOnCheckmarkItemClickListener(this);
            newsList.setAdapter(zxNewsAdapter);
        }
        newsList.setLayoutManager(new LinearLayoutManager(this));
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNewsList(type);
            }
        });
        getNewsList(type);
    }

    @Override
    public void initData() {
    }

    @Override
    public void initVariables() {
        title = getIntent().getStringExtra(TITLE);
        propertyId = getIntent().getLongExtra(PROPERTYID, 0L);
        type = getIntent().getIntExtra(TYPE, 0);
        Log.i("TAG", "type=" + type);
    }

    @Override
    public String getTag() {
        return null;
    }

    private void getNewsList(final int type) {
        refresh.post(new Runnable() {
            @Override
            public void run() {
                refresh.setRefreshing(true);
            }
        });
        String url = FinalData.URL_VALUE + "otherList";
        Map<String, Object> params = new HashMap<>();
        params.put("type", type);
        params.put("propertyId", propertyId);
        params.put("page", 1);
        params.put("pageSize", 20);
        GsonRequest<NewInfoListBean> request =
                new GsonRequest<>(Request.Method.POST,
                        url, NewInfoListBean.class,
                        params,
                        new Response.Listener<NewInfoListBean>() {
                            @Override
                            public void onResponse(NewInfoListBean newInfoListBean) {

                                if (type == 6) {
                                    if (mList.size() > 0) {
                                        mList.clear();
                                    }
                                    mList.addAll(newInfoListBean.getOtherInfos());
                                    newsAdapter.notifyDataSetChanged();
                                } else if (type == 5) {
                                    if (mList.size() > 0) {
                                        mList.clear();
                                    }
                                    mList.addAll(newInfoListBean.getOtherInfos());
                                    zxNewsAdapter.notifyDataSetChanged();
                                } else if (type == 7) {
                                    startActivity(new Intent(NewsListActivity.this, BrowseActivity.class)
                                                    .putExtra(BrowseActivity.BROWSER_KEY, "http://zx.miuhouse.com/mobile/corp/7")
                                                    .putExtra("shareImageUrl", newInfoListBean.getOtherInfos().get(0).getImage())
                                                    .putExtra("shareContent", newInfoListBean.getOtherInfos().get(0).getTitle())
                                    );
                                } else if (type == 8) {
                                    startActivity(new Intent(NewsListActivity.this, BrowseActivity.class)
                                            .putExtra(BrowseActivity.BROWSER_KEY, "http://zx.miuhouse.com/mobile/corp/8")
                                            .putExtra("shareImageUrl", newInfoListBean.getOtherInfos().get(0).getImage())
                                            .putExtra("shareContent", newInfoListBean.getOtherInfos().get(0).getTitle()));
                                } else if (type == 9) {
                                    startActivity(new Intent(NewsListActivity.this, BrowseActivity.class)
                                                    .putExtra(BrowseActivity.BROWSER_KEY, "http://zx.miuhouse.com/mobile/corp/9")
                                                    .putExtra("shareImageUrl", newInfoListBean.getOtherInfos().get(0).getImage())
                                                    .putExtra("shareContent", newInfoListBean.getOtherInfos().get(0).getTitle())
                                    );
                                }
                                if (refresh.isRefreshing()) {
                                    refresh.setRefreshing(false);
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (refresh.isRefreshing())
                            refresh.setRefreshing(false);
                    }
                });
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    @Override
    public void onCheckItemClick(int position) {
        Intent intent = new Intent(activity, BrowseActivity.class);
//        if (tag == 1) {
        Log.i("TAG", "mList=" + mList.get(position).getId());
        intent.putExtra(BrowseActivity.BROWSER_KEY, "http://zx.miuhouse.com/mobile/other/" + mList.get(position).getId())
                .putExtra("shareContent", mList.get(position).getTitle());
//        } else {
//            intent.putExtra(BrowseActivity.BROWSER_KEY, "http://cloud.miuhouse.com/mobile/crawnews/" + mList.get(position).getId());
//        }
        startActivity(intent);
    }

    public void refreshNews(View view) {
        getNewsList(type);
    }

    public void goToIntro(View view) {
//        startActivity(new Intent(this, BrowseActivity.class)
//                        .putExtra(BrowseActivity.BROWSER_KEY, "http://zx.miuhouse.com/mobile/corp/7")

//        );
        getNewsList(7);
    }

    public void goToStructure(View view) {
//        startActivity(new Intent(this, BrowseActivity.class)
//                .putExtra(BrowseActivity.BROWSER_KEY, "http://zx.miuhouse.com/mobile/corp/8"));
        getNewsList(8);

    }

    public void goToCulture(View view) {
//        startActivity(new Intent(this, BrowseActivity.class)
//                .putExtra(BrowseActivity.BROWSER_KEY, "http://zx.miuhouse.com/mobile/corp/9"));
        getNewsList(9);

    }
}
