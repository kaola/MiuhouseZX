package com.miuhouse.zxcommunity.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.view.LayoutInflater;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.activity.BrowseActivity;
import com.miuhouse.zxcommunity.adapter.NewsAdapter;
import com.miuhouse.zxcommunity.bean.NewInfoListBean;
import com.miuhouse.zxcommunity.bean.NewsInfoBean;
import com.miuhouse.zxcommunity.http.FinalData;
import com.miuhouse.zxcommunity.http.GsonRequest;
import com.miuhouse.zxcommunity.http.VolleySingleton;
import com.miuhouse.zxcommunity.utils.Constants;
import com.miuhouse.zxcommunity.utils.SPUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 资讯
 * Created by kings on 1/27/2016.
 */
public class NewsFragment extends BaseFragment implements NewsAdapter.OnCheckmarkClickListener {

    private final static int LOADING_MORE = 1;
    private final static int REFRESH_MORE = 2;
    private View view;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int page = 1;
    private NewsAdapter newsAdapter;
    private List<NewsInfoBean> mList = new ArrayList<>();
    private int lastVisibleItem;
    private LinearLayoutManager mLayoutManager;
    private int tag;
    private long cityId;
    private final static int PAGE_SIZE = 20;

    @Override
    public void initData() {
        tag = getArguments().getInt("tag");
        cityId = getArguments().getLong("cityId", 0);
    }

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.fragment_news, null);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_news);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2, R.color.refresh_progress_3);
        newsAdapter = new NewsAdapter(getActivity(), mList);
        newsAdapter.setOnCheckmarkItemClickListener(this);
        mRecyclerView.setAdapter(newsAdapter);
        mRecyclerView.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == newsAdapter.getItemCount()) {
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
        return view;
    }

    private void sendRequest(final int flag) {
        Map<String, Object> map = new HashMap<>();
        String urlPath = null;
        if (tag == 0) {
            urlPath = "http://cloud.miuhouse.com/app/" + "crawNews";
            if (cityId != 0) {
                map.put("cityId", cityId);
            } else {
                map.put("cityId", 59);
            }

        } else if (tag == 1) {
            urlPath = FinalData.URL_VALUE + "news";
            map.put("type", 14);
            map.put("propertyId", SPUtils.getSPData(Constants.PROPERTYID, 0L));
        }
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
                mList.addAll(newInfoListBean.getNewsInfos());
                newsAdapter.notifyDataSetChanged();
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
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

    @Override
    public void onCheckItemClick(int position) {
        Intent intent = new Intent(activity, BrowseActivity.class);
        if (tag == 1) {
            intent.putExtra(BrowseActivity.BROWSER_KEY, FinalData.URL_NEWS + mList.get(position).getId());
            intent.putExtra("shareContent", mList.get(position).getTitle());
            intent.putExtra("shareImageUrl", mList.get(position).getImage());
        } else {
            intent.putExtra(BrowseActivity.BROWSER_KEY, "http://cloud.miuhouse.com/mobile/crawnews/" + mList.get(position).getId());
            intent.putExtra("shareContent", mList.get(position).getTitle());
            intent.putExtra("shareImageUrl", mList.get(position).getImage());
        }
        startActivity(intent);
    }
}
