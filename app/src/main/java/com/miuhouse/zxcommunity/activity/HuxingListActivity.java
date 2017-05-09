package com.miuhouse.zxcommunity.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.adapter.HuxingAdapter;
import com.miuhouse.zxcommunity.bean.Huxing;
import com.miuhouse.zxcommunity.bean.PropertyInfo;
import com.miuhouse.zxcommunity.http.FinalData;
import com.miuhouse.zxcommunity.http.GsonRequest;
import com.miuhouse.zxcommunity.http.VolleySingleton;
import com.miuhouse.zxcommunity.utils.L;
import com.miuhouse.zxcommunity.utils.MyUtils;
import com.miuhouse.zxcommunity.widget.StatusCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by khb on 2016/11/24.
 */
public class HuxingListActivity extends BaseActivity {

    public static final String PROPERTYID = "propertyId";
    public static final String PROPERTY = "property";
    private ListView lvHuxingList;
    private HuxingAdapter adapter;
    private SwipeRefreshLayout refresh;
    private long propertyId;
    private List<Huxing> huxingList;
    private PropertyInfo property;

    @Override
    public void initTitle() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.titlebar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.mipmap.back_black);
        setSupportActionBar(toolbar);
        StatusCompat.compat(this, StatusCompat.COLOR_DEFAULT);
        TextView title = (TextView) findViewById(R.id.title);

        title.setText("所有户型");
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
        setContentView(R.layout.activity_huxinglist);
        lvHuxingList = (ListView) findViewById(R.id.huxingList);
        huxingList = new ArrayList<>();
        adapter = new HuxingAdapter(this, huxingList);
        lvHuxingList.setAdapter(adapter);
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        refresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        refresh.setEnabled(false);
        getHuxingList();
    }

    private void getHuxingList() {
        refresh.post(new Runnable() {
            @Override
            public void run() {
                refresh.setRefreshing(true);
            }
        });
        String url = FinalData.URL_VALUE + "newHuxingList";
        Map<String, Object> map = new HashMap<>();
        map.put("newPropertyId", property.getId());
        map.put("page", 1);
        map.put("pageSize", 50);
        GsonRequest<HuxingActivity.HuxingListBean> request = new GsonRequest<>(Request.Method.POST, url, HuxingActivity.HuxingListBean.class, map,
                new Response.Listener<HuxingActivity.HuxingListBean>() {
                    @Override
                    public void onResponse(final HuxingActivity.HuxingListBean otherBean) {
                        refresh.setRefreshing(false);
                        if (otherBean.getCode() == 0 && !MyUtils.isEmptyList(otherBean.getNewHuxings())) {
                            huxingList.clear();
                            huxingList.addAll(otherBean.getNewHuxings());
                            adapter.notifyDataSetChanged();
                            lvHuxingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    startActivity(new Intent(activity, HuxingActivity.class)
                                            .putExtra("id", otherBean.getNewHuxings().get(position).getId())
                                            .putExtra("property", property));
                                }
                            });
                        }else {
                            Toast.makeText(context, "该楼盘暂无户型数据", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        L.e(volleyError.toString());
                        refresh.setRefreshing(false);
                        Toast.makeText(context, "服务器错误，稍后重试", Toast.LENGTH_SHORT).show();
                    }
                });
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    @Override
    public void initData() {
//        propertyId = getIntent().getLongExtra(PROPERTYID, 0);
        property = (PropertyInfo) getIntent().getSerializableExtra(PROPERTY);
    }

    @Override
    public void initVariables() {

    }

    @Override
    public String getTag() {
        return null;
    }

}
