package com.miuhouse.zxcommunity.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.adapter.NewPropertyAdapter;
import com.miuhouse.zxcommunity.bean.BaseBean;
import com.miuhouse.zxcommunity.http.FinalData;
import com.miuhouse.zxcommunity.http.GsonRequest;
import com.miuhouse.zxcommunity.http.VolleySingleton;
import com.miuhouse.zxcommunity.utils.MyUtils;
import com.miuhouse.zxcommunity.widget.ProgressFragment;
import com.miuhouse.zxcommunity.widget.StatusCompat;
import com.miuhouse.zxcommunity.widget.expandmenu.view.DoubleListFilterView;
import com.miuhouse.zxcommunity.widget.expandmenu.view.ExpandMenuView;
import com.miuhouse.zxcommunity.widget.expandmenu.view.SingleListFilterView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by khb on 2016/4/29.
 */
public class NewHouseListActivity extends BaseActivity {

    private ArrayList<String> cityCategory;
    private SparseArray<List<String>> districtCategory;
    private List<String> layoutCategory;
    private List<String> priceCategory;
    private ExpandMenuView mSelectionMenu;
    private RecyclerView mRvList;

    private long cityId;
    private String area;
    private int apartment;
    private int minPrice;
    private int maxPrice;
    private List<CityArea> list;
    private SwipeRefreshLayout refresh;

    @Override
    public void initTitle() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.titlebar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.mipmap.back_black);
        setSupportActionBar(toolbar);
        StatusCompat.compat(this, StatusCompat.COLOR_DEFAULT);
        TextView title = (TextView) findViewById(R.id.title);

        title.setText("新房速递");
        title.setTextColor(Color.parseColor("#1E2129"));
        title.setClickable(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mSelectionMenu.closeView();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        mSelectionMenu.closeView();
        super.onBackPressed();
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_newhouse_list);
        mSelectionMenu = (ExpandMenuView) findViewById(R.id.selectionMenu);
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNewHouseList(cityId, area, apartment);
            }
        });
        mRvList = (RecyclerView) findViewById(R.id.houseList);

    }

    private void initMenu() {
        DoubleListFilterView doubleList = new DoubleListFilterView(context, "选择区域", cityCategory, districtCategory, 0, 0);
        doubleList.setOnSelectListener(new DoubleListFilterView.OnSelectListener() {
            @Override
            public void getValue(String showText, int superPosition, int position) {
                showLogD(showText);
                mSelectionMenu.closeView();
                cityId = list.get(superPosition).getId();
                area = list.get(superPosition).getArea().get(position);
                mSelectionMenu.setTitle(list.get(superPosition).getName() + " " + area, 0);
                getNewHouseList(cityId, area, apartment);
            }
        });
        SingleListFilterView singleList = new SingleListFilterView(context, layoutCategory, "选择户型");
        singleList.setOnSelectListener(new SingleListFilterView.OnSelectListener() {
            @Override
            public void getValue(String showText, int position) {
                mSelectionMenu.closeView();
                mSelectionMenu.setTitle(showText, 1);
                apartment = position + 1;
                getNewHouseList(cityId, area, apartment);
            }
        });
        SingleListFilterView priceList = new SingleListFilterView(context, priceCategory, "价格区间");
        priceList.setOnSelectListener(new SingleListFilterView.OnSelectListener() {
            @Override
            public void getValue(String showText, int position) {
                mSelectionMenu.closeView();
                mSelectionMenu.setTitle(showText, 2);
                switch (position) {
                    case 0:
                        minPrice = 0;
                        maxPrice = 5000;
                        break;
                    case 1:
                        minPrice = 5000;
                        maxPrice = 7000;
                        break;
                    case 2:
                        minPrice = 7000;
                        maxPrice = 9000;
                        break;
                    case 3:
                        minPrice = 9000;
                        maxPrice = 12000;
                        break;
                    case 4:
                        minPrice = 12000;
                        maxPrice = 15000;
                        break;
                    case 5:
                        minPrice = 15000;
                        maxPrice = 0;
                        break;
                }
                getNewHouseList(cityId, area, apartment);
            }
        });

        ArrayList<View> viewList = new ArrayList<>();
        viewList.add(doubleList);
        viewList.add(singleList);
        viewList.add(priceList);
        ArrayList<String> categoryTitleList = new ArrayList<>();
        categoryTitleList.add("选择区域");
        categoryTitleList.add("选择户型");
        categoryTitleList.add("价格区间");
        mSelectionMenu.setValue(categoryTitleList, viewList);
        mSelectionMenu.setOnButtonClickListener(new ExpandMenuView.OnButtonClickListener() {
            @Override
            public void onClick(int selectPosition, boolean isChecked) {

            }
        });
    }

    @Override
    public void initData() {
        getProperties();
        getNewHouseList(cityId, area, apartment);
    }

    public void getData(CityAreaBean data) {

            cityCategory = new ArrayList<>();
            districtCategory = new SparseArray<>();
            list = data.getCitys();
            for (int i=0; i< list.size(); i++){
                CityArea c = list.get(i);
                cityCategory.add(c.getName());
                List<String> areaList = new ArrayList<>();
                if (!MyUtils.isEmptyList(c.getArea())){
                    for (String area :
                            c.getArea()) {
                        areaList.add(area);
                    }
                }
                districtCategory.put(i, areaList);
            }

            layoutCategory = new ArrayList<>();
            for (int i = 0; i < 6; i++){
                layoutCategory.add(i+1+"居室");
            }
        String [] priceStringList = new String[]{"5000以下", "5000-7000", "7000-9000", "9000-12000", "12000-15000", "15000以上"};
        priceCategory = Arrays.asList(priceStringList);

        initMenu();
    }

    private void getProperties(){
        String url = FinalData.URL_VALUE + "cityList";
        Map<String, Object> map = new HashMap<>();
        GsonRequest<CityAreaBean> request = new GsonRequest<>(Request.Method.POST, url, CityAreaBean.class, map,
                new Response.Listener<CityAreaBean>() {
                    @Override
                    public void onResponse(CityAreaBean data) {
                        if (data!=null && data.getCode()==0 && !MyUtils.isEmptyList(data.getCitys()))
                        getData(data);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void getNewHouseList(long cityId, String area, int apartment){
//        SwipeRefreshLayout和正在加载的fragment有一个就可以了

        final ProgressFragment progress = new ProgressFragment();
        if (refresh==null || !refresh.isRefreshing()) {
            progress.setMessage(getResources().getString(R.string.loading));
            progress.show(getSupportFragmentManager(), getClass().getName());
        }
        String url = FinalData.URL_VALUE + "newPropertyList";
        Map<String, Object> map = new HashMap<>();
        if (cityId>0) {
            map.put("cityId", cityId);
        }
        if (!TextUtils.isEmpty(area)) {
            map.put("area", area);
        }
        if (apartment>0) {
            map.put("apartment", apartment);
        }
        if (minPrice>0){
            map.put("avgPriceMin", minPrice);
        }
        if (maxPrice>0){
            map.put("avgPriceMax", maxPrice);
        }
        map.put("page", 1);
        map.put("pageSize", 10);
        GsonRequest<NewPropertyInfoBean> request = new GsonRequest<>(Request.Method.POST, url, NewPropertyInfoBean.class, map,
                new Response.Listener<NewPropertyInfoBean>() {
                    @Override
                    public void onResponse(NewPropertyInfoBean newPropertyInfoBean) {
                        if (!refresh.isRefreshing()) {
                            progress.dismiss();
                        }else {
                            refresh.setRefreshing(false);
                        }
                        final List<NewPropertyInfo> list = newPropertyInfoBean.getNewPropertyInfos();
                        NewPropertyAdapter adapter = new NewPropertyAdapter(activity, list);
                        mRvList.setLayoutManager(new LinearLayoutManager(activity));
                        mRvList.setAdapter(adapter);
                        if (newPropertyInfoBean!=null
                                && newPropertyInfoBean.getCode() == 0
                                && !MyUtils.isEmptyList(list)){
                            adapter.setOnNewPropertyClickListener(new NewPropertyAdapter.OnNewPropertyClickListener() {
                                @Override
                                public void onPropertyClick(View view, int position) {
                                    startActivity(new Intent(context, NewHouseActivity.class)
                                            .putExtra("id", list.get(position).getId()));
                                }
                            });
                        }else {
                            mRvList.getAdapter().notifyDataSetChanged();
                            showToast("暂无房源");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progress.dismiss();

                    }
                });
        VolleySingleton.getInstance(this).addToRequestQueue(request);

    }

    private class NewPropertyInfoBean extends BaseBean{
        List<NewPropertyInfo> newPropertyInfos;

        public List<NewPropertyInfo> getNewPropertyInfos() {
            return newPropertyInfos;
        }

        public void setNewPropertyInfos(List<NewPropertyInfo> newPropertyInfos) {
            this.newPropertyInfos = newPropertyInfos;
        }
    }

    public class NewPropertyInfo{
        String id;
        String name;    //楼盘名称
        int minHuxing;  //最小居室
        int maxHuxing;  //最大居室
        String roomArea;    //面积
        int avgPrice;   //平均价格
        List<String> label; //标签
        String city;    //城市
        String area;    //地区
        String headUrl; //
        int hasDiscount;    //是否有优惠 0：无 1：有

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getMinHuxing() {
            return minHuxing;
        }

        public void setMinHuxing(int minHuxing) {
            this.minHuxing = minHuxing;
        }

        public int getMaxHuxing() {
            return maxHuxing;
        }

        public void setMaxHuxing(int maxHuxing) {
            this.maxHuxing = maxHuxing;
        }

        public String getRoomArea() {
            return roomArea;
        }

        public void setRoomArea(String roomArea) {
            this.roomArea = roomArea;
        }

        public int getAvgPrice() {
            return avgPrice;
        }

        public void setAvgPrice(int avgPrice) {
            this.avgPrice = avgPrice;
        }

        public List<String> getLabel() {
            return label;
        }

        public void setLabel(List<String> label) {
            this.label = label;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getHeadUrl() {
            return headUrl;
        }

        public void setHeadUrl(String headUrl) {
            this.headUrl = headUrl;
        }

        public int getHasDiscount() {
            return hasDiscount;
        }

        public void setHasDiscount(int hasDiscount) {
            this.hasDiscount = hasDiscount;
        }

        @Override
        public String toString() {
            return "NewPropertyInfo{" +
                    "name='" + name + '\'' +
                    ", minHuxing=" + minHuxing +
                    ", maxHuxing=" + maxHuxing +
                    ", roomArea='" + roomArea + '\'' +
                    ", avgPrice=" + avgPrice +
                    ", label=" + label +
                    ", city='" + city + '\'' +
                    ", area='" + area + '\'' +
                    ", headUrl='" + headUrl + '\'' +
                    ", hasDiscount=" + hasDiscount +
                    '}';
        }
    }





    @Override
    public void initVariables() {

    }

    @Override
    public String getTag() {
        return null;
    }

    private class CityAreaBean extends BaseBean {
        List<CityArea> citys;

        public List<CityArea> getCitys() {
            return citys;
        }

        public void setCitys(List<CityArea> citys) {
            this.citys = citys;
        }
    }

    private class CityArea {
        long id;
        String name;
        List<String> area;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getArea() {
            return area;
        }

        public void setArea(List<String> area) {
            this.area = area;
        }
    }
}
