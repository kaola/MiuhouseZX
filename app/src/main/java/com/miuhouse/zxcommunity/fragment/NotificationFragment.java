package com.miuhouse.zxcommunity.fragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.activity.LoginActivity;
import com.miuhouse.zxcommunity.activity.NotificationDetailActivity;
import com.miuhouse.zxcommunity.adapter.NotificationAdapter;
import com.miuhouse.zxcommunity.bean.BaseBean;
import com.miuhouse.zxcommunity.bean.Notification;
import com.miuhouse.zxcommunity.db.NotificationDao;
import com.miuhouse.zxcommunity.http.FinalData;
import com.miuhouse.zxcommunity.http.GsonRequest;
import com.miuhouse.zxcommunity.http.VolleySingleton;
import com.miuhouse.zxcommunity.utils.Constants;
import com.miuhouse.zxcommunity.utils.MyUtils;
import com.miuhouse.zxcommunity.utils.SPUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by khb on 2016/3/16.
 */
public class NotificationFragment extends BaseFragment {

    private RecyclerView list;
    private List<Notification> mDataList;
    private NotificationAdapter mAdapter;

    private int mTag;
    private final int LOADMORE = 1;
    private final int REFRESH = 2;
    private int page = 1;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (!MyUtils.isLoggedIn()){
                mRefresh.setVisibility(View.GONE);
                emptyView.setVisibility(View.GONE);
            }else {
                /*mDataList.clear();
                mDataList.addAll(mDao.getDataListByPropertyId(SPUtils.getSPData(Constants.PROPERTYID, 0L)));
                if (MyUtils.isEmptyList(mDataList)){
                    emptyView.setVisibility(View.VISIBLE);
                    mRefresh.setVisibility(View.GONE);
                }else{
                    mRefresh.setVisibility(View.VISIBLE);
                }
                mAdapter.notifyDataSetChanged();*/
                getNotificationList();
            }
        }
    };
    private NotificationDao mDao;
    private SwipeRefreshLayout mRefresh;
    private ViewGroup emptyView;

    @Override
    public void initData() {
//        if (!MyUtils.isLoggedIn()){
//        }

        mDao = new NotificationDao(activity);
        mDataList = new ArrayList<>();
//        mAdapter = new NotificationAdapter(activity, mDataList);
//        mDataList = mDao.getDataListByPropertyId();
//        if (MyUtils.isEmptyList(mDataList)){
        mTag = REFRESH;
//        getNotificationList();
//        }
    }

    public void getNotificationList() {
        mDataList.clear();
        mDataList.addAll(mDao.getDataListByPropertyId(SPUtils.getSPData(Constants.PROPERTYID, 0L)));
        if (!MyUtils.isEmptyList(mDataList)){
            mRefresh.setVisibility(View.VISIBLE);
            mRefresh.setRefreshing(false);
//            getNotificationList();
//            mAdapter = new NotificationAdapter(activity, mDataList);
//            list.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            return;
        }

        String url = FinalData.URL_VALUE + "getNoticeList";
        Map<String, Object> map = new HashMap<>();
//        propertyid数据统一来自于本地
        map.put("propertyId", SPUtils.getSPData(Constants.PROPERTYID, 0L));
        if (mTag == REFRESH){
            page = 1;
        }else{
            page = page++;
        }
        map.put("page", page);
        map.put("pageSize", 20);
        GsonRequest<NotificationListBean> request = new GsonRequest<NotificationListBean>(Request.Method.POST, url, NotificationListBean.class, map,
                new Response.Listener<NotificationListBean>() {
                    @Override
                    public void onResponse(NotificationListBean notificationListBean) {
                        mRefresh.setRefreshing(false);
                         if (notificationListBean != null && !MyUtils.isEmptyList(notificationListBean.getList())){
                            mRefresh.setVisibility(View.VISIBLE);
                            mDataList.addAll(0, notificationListBean.getList());
                            for (int i = 0; i < mDataList.size(); i ++){
                                mDataList.get(i).setIsRead(true);
//                                try {
                                    mDao.addData(mDataList.get(i));
//                                }catch (SQLiteException e){
//                                    e.printStackTrace();
//                                    continue;
//                                }
                            }
                            mAdapter.notifyDataSetChanged();
                        }else{
                            mRefresh.setVisibility(View.GONE);
                            emptyView.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        mRefresh.setRefreshing(false);
                        mRefresh.setVisibility(View.GONE);
                    }
        });
        VolleySingleton.getInstance(activity).addToRequestQueue(request);
    }

    @Override
    public View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_notification, null);
        emptyView = (ViewGroup) view.findViewById(R.id.empty);
        emptyView.findViewById(R.id.tv_empty_subtext).setVisibility(View.GONE);
        mRefresh = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        view.findViewById(R.id.loginNow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity, LoginActivity.class));
            }
        });
        list = (RecyclerView) view.findViewById(R.id.notificationList);
        list.setLayoutManager(new LinearLayoutManager(activity));
        mAdapter = new NotificationAdapter(activity, mDataList);
        list.setAdapter(mAdapter);

        if (!MyUtils.isLoggedIn()){
            mRefresh.setVisibility(View.GONE);
            emptyView.setVisibility(View.GONE);
//            return view.findViewById(R.id.loginTip);
        }else {
            getNotificationList();
            mAdapter.setOnNotificationClickListener(new NotificationAdapter.OnNotificationClickListener() {
                @Override
                public void onNotificationClick(View view, int position) {
                    startActivity(new Intent(activity, NotificationDetailActivity.class)
                            .putExtra(NotificationDetailActivity.NOTIFICATION, mDataList.get(position)));
                }
            });
            mRefresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
            mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    mTag = REFRESH;
                    getNotificationList();
                }
            });
        }
//        list.addOnScrollListener(new RecyclerView.OnScrollListener() {
//
//            private int lastVisibleItem;
//            private int firstVisibleItem;
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == RecyclerView.SCROLL_STATE_IDLE
//                        && lastVisibleItem + 1 == mAdapter.getItemCount()
//                        && firstVisibleItem != 0) {
////                    handler.sendEmptyMessage(0);
//                    mTag = LOADMORE;
//                    getNotificationList();
//                }
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                lastVisibleItem = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastVisibleItemPosition();
//                firstVisibleItem = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
//            }
//        });
        return view;
    }

    public void refresh() {
        handler.sendEmptyMessage(0);
    }

    private class NotificationListBean extends BaseBean{
        public List<Notification> getList() {
            return list;
        }

        public void setList(List<Notification> list) {
            this.list = list;
        }

        private List<Notification> list;

    }
}
