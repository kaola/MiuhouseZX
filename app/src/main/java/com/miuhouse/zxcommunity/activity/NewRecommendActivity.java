package com.miuhouse.zxcommunity.activity;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.adapter.IndexAdapter2;
import com.miuhouse.zxcommunity.bean.IndexBean;
import com.miuhouse.zxcommunity.bean.ZFZBean;
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
 * Created by khb on 2016/11/7.
 */
public class NewRecommendActivity extends BaseActivity {

    private ListView houseList;
    private SwipeRefreshLayout refresh;
    private long propertyId;
    private double locX;
    private double locY;
    private List<ZFZBean> list;
    private IndexAdapter2 adapter;

    @Override
    public void initTitle() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.titlebar);
        mToolbar.setTitle("");
        mToolbar.setNavigationIcon(R.mipmap.back_black);
        setSupportActionBar(mToolbar);
        StatusCompat.compat(this, StatusCompat.COLOR_DEFAULT);
        TextView title = (TextView) findViewById(R.id.title);
        title.setText("新房推荐");
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
        setContentView(R.layout.activity_newrecommend);
        houseList = (ListView) findViewById(R.id.newsList);
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getHouseList();
            }
        });
        list = new ArrayList<>();
        getHouseList();
    }

    @Override
    public void initData() {

    }

    @Override
    public void initVariables() {
        propertyId = getIntent().getLongExtra("propertyId", 0);
        locX = getIntent().getDoubleExtra("locX", 0d);
        locY = getIntent().getDoubleExtra("locY", 0d);
    }

    @Override
    public String getTag() {
        return null;
    }

    private void getHouseList() {
//        refresh.post(new Runnable() {
//            @Override
//            public void run() {
//                refresh.setRefreshing(true);
//            }
//        });
        String url = FinalData.URL_VALUE + "index";
        Map<String, Object> map = new HashMap<>();
//        Log.i("TAG", "propertyId=" + propertyId);
//        if (propertyId > PROPERTYID_DEFAULT) {
            map.put("propertyId", propertyId);
//        } else if (propertyId == PROPERTYID_DEFAULT && locX != 0 && locY != 0) {
            map.put("locX", locX);
            map.put("locY", locY);
//        }
//        没登录时传IMEI号，后台注册一个临时环信账号，账号即IMEI
//        if (!MyUtils.isLoggedIn()) {
//            map.put("imeis", MyApplication.getInstance().getIMEI());
//        }
        GsonRequest<IndexBean> request = new GsonRequest<>(Request.Method.POST, url, IndexBean.class, map, new Response.Listener<IndexBean>() {
            @Override
            public void onResponse(IndexBean indexBean) {
                refresh.setRefreshing(false);
//                将返回的propertyId保存本地
//                如果有返回的propertyId，说明请求时没有传propertyId，就保存
//                如果没有返回的propertyId，说明请求前已经有传，就保存传的propertyId
//                if (indexBean.getPropertyId() > 0) {
//                    SPUtils.saveSPData(Constants.PROPERTYID, indexBean.getPropertyId());
//                } else {
//                    SPUtils.saveSPData(Constants.PROPERTYID, propertyId);
//                }
//                cityId = indexBean.getCityId();
//                fillUI(indexBean, propertyName);

//                list.clear();
//                bannerBean.clear();
                //租房和售房放在一个ListView 前面放的是售房，后面是租房
//                listViewXF = new MyListView(activity);
//                listViewSM = new MyListView(activity);
//                listViewCZ = new MyListView(activity);
//                IndexAdapter adapterXF = new IndexAdapter(activity, indexBean.getNewPropertyInfos(), IndexAdapter.XINFANG);
//                IndexAdapter adapterSM = new IndexAdapter(activity, indexBean.getEsfs(), IndexAdapter.SHOUMAI);
//                IndexAdapter adapterCZ = new IndexAdapter(activity, indexBean.getZfs(), IndexAdapter.CHUZU);
                if (!MyUtils.isEmptyList(list)){
                    list.clear();
                }
                if (MyUtils.isEmptyList(indexBean.getNewPropertyInfos())){
                    Toast.makeText(NewRecommendActivity.this, "暂无内容", Toast.LENGTH_SHORT).show();
                    return ;
                }
                list.addAll(indexBean.getNewPropertyInfos());
                adapter = new IndexAdapter2(activity, list, IndexAdapter2.XINFANG);
                houseList.setAdapter(adapter);
                adapter.notifyDataSetChanged();
//                listViewXF.setAdapter(adapterXF);
//                listViewSM.setAdapter(adapterSM);
//                listViewCZ.setAdapter(adapterCZ);
//                houselist_container.removeAllViews();
//                houselist_container.addView(listViewXF);
//                houselist_container.addView(listViewSM);
//                houselist_container.addView(listViewCZ);

//                bannerBean.addAll(indexBean.getBanners());
//                initBanner(indexBean.getBanners().size());
//                for (int i = 0; i < indexBean.getBanners().size(); i++) {
//                    // TOP 广告原先是170dp BOTTOM广告原先是140dp
//                    Glide.with(getActivity()).load(indexBean.getBanners().get(i).getImage()).override(MyUtils.getScreenWidth(getActivity()), (MyUtils.dip2px(activity, 105)))
//                            .into(imgViewBanner.get(i));
//                }
//                if (!MyUtils.isLoggedIn()) {
//                    loginTempHX();
//                } else {
//                二次刷新公告页面，因为第一次没拿到propertyId，很无奈
//                    ((NotificationFragment) FragmentFactory.getFragment(Constants.NOTIFICATION)).getNotificationList();
//                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                L.e(volleyError.toString());
                refresh.setRefreshing(false);
                Toast.makeText(NewRecommendActivity.this, "暂无内容", Toast.LENGTH_SHORT).show();
            }
        });
        request.setTag(MainActivity.TAG);
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}
