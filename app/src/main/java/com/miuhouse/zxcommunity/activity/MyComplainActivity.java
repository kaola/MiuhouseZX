package com.miuhouse.zxcommunity.activity;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.adapter.ComplainAdapter;
import com.miuhouse.zxcommunity.application.MyApplication;
import com.miuhouse.zxcommunity.bean.BaseBean;
import com.miuhouse.zxcommunity.bean.Complain;
import com.miuhouse.zxcommunity.http.FinalData;
import com.miuhouse.zxcommunity.http.GsonRequest;
import com.miuhouse.zxcommunity.http.VolleySingleton;
import com.miuhouse.zxcommunity.utils.Constants;
import com.miuhouse.zxcommunity.utils.MyUtils;
import com.miuhouse.zxcommunity.utils.SPUtils;
import com.miuhouse.zxcommunity.widget.StatusCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by khb on 2016/12/19.
 */
public class MyComplainActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView replyList;
    private SwipeRefreshLayout refresh;
    private ComplainAdapter adapter;

    private int page = 1;
    private int refresh_status = CANREFRESH;
    private static final int CANREFRESH = 1;
    private static final int CANLOADMORE = 2;
    private static final int LOADFAILED = -1;
    private static final int LOADING = 0;
    private int[] types ;
    private int type;
    private String title;
    private List<Complain> list;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuItem actionItem = menu.findItem(R.id.action);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initTitle() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.titlebar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.mipmap.back_black);
        setSupportActionBar(toolbar);
        StatusCompat.compat(this, StatusCompat.COLOR_DEFAULT);
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(this.title);
        title.setTextColor(Color.parseColor("#1E2129"));
        title.setClickable(true);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_mycomplainlist);
        replyList = (RecyclerView) findViewById(R.id.complainList);
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        list = new ArrayList<>();
        adapter = new ComplainAdapter(this, list);
        replyList.setAdapter(adapter);
        replyList.setLayoutManager(new LinearLayoutManager(this));
        refresh.setOnRefreshListener(this);
        replyList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int lastVisibleItem;
            private int firstVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == adapter.getItemCount()
                        && firstVisibleItem != 0
                        && !replyList.canScrollVertically(1)) {  //recycleview能否向上滚动
                    if (refresh_status != LOADING) {
                        if (refresh_status == CANLOADMORE) {
                            page += 1;
                        } else if (refresh_status == LOADFAILED) {
                            page = page;
                        }
                        refresh_status = LOADING;
                        getAllComplains(page);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                firstVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                lastVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
            }
        });

        getAllComplains(page);
    }

    @Override
    public void initData() {
        type = getIntent().getIntExtra("type", 0);
        if (type == Complain.REPAIR) {
            title = "报修记录";
            types = new int[1];
            types[0] = 1;
        } else if (type == Complain.COMPLAIN) {
            title = "投诉记录";
            types = new int[2];
            types[0] = 2;
            types[1] = 3;
        }else if (type == Complain.LIKE){
            title = "表扬记录";
            types = new int[1];
            types[0] = 4;
        }else {
            title="记录";
        }
    }

    @Override
    public void initVariables() {

    }

    @Override
    public String getTag() {
        return null;
    }

    @Override
    public void onRefresh() {
        page = 1;
        if (refresh_status != LOADING) {
            refresh_status = LOADING;
            getAllComplains(page);
        }
    }

    private void getAllComplains(final int page){
        loading();
        String url = FinalData.URL_VALUE + "repairComplainList";
        Map<String, Object> map = new HashMap<>();
        map.put("propertyId", SPUtils.getSPData(Constants.PROPERTYID, 0L));
        map.put("ownerId", MyApplication.getInstance().getUserBean().getId());
        map.put("types", types);
        map.put("page", page);
        map.put("pageSize", 10);
        GsonRequest<ComplainListBean> request = new GsonRequest<>(Request.Method.POST,
                url, ComplainListBean.class, map,
                new Response.Listener<ComplainListBean>() {
                    @Override
                    public void onResponse(ComplainListBean complainListBean) {
                        refresh.setRefreshing(false);
                        if (page == 1){
                            list.clear();
                        }
                        list.addAll(complainListBean.getRepairComplainList());
                        if (MyUtils.isEmptyList(list)){
                            refresh_status = CANREFRESH;
                            showToast("暂无内容");
                            return ;
                        }
                        refresh_status = CANLOADMORE;
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        refresh.setRefreshing(false);
                        refresh_status = LOADFAILED;
                        showToast("服务器错误");
                    }
        });
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void loading(){
        refresh_status = LOADING;
        refresh.post(new Runnable() {
            @Override
            public void run() {
                refresh.setRefreshing(true);
            }
        });
    }

    class ComplainListBean extends BaseBean{
        List<Complain> repairComplainList ;

        public List<Complain> getRepairComplainList() {
            return repairComplainList;
        }

        public void setRepairComplainList(List<Complain> repairComplainList) {
            this.repairComplainList = repairComplainList;
        }
    }
}
