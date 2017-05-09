package com.miuhouse.zxcommunity.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.utils.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by khb on 2016/11/7.
 */
public class MapFragment extends BaseFragment implements GeocodeSearch.OnGeocodeSearchListener, RadioGroup.OnCheckedChangeListener, PoiSearch.OnPoiSearchListener {

    private Bundle bundle;
    private MapView map;
    private AMap aMap;
    private LatLng latLng;
    private RegeocodeAddress regeocodeAddress;
    private String cityName;
    private RadioGroup mRadioGroup;
    private PoiSearch.Query query;
    private MarkerOptions markerOptions;
    private int checkedId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        bundle = savedInstanceState;
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initData() {
//        locX = getIntent().getDoubleExtra(LocX, 0);
//        locY = getIntent().getDoubleExtra(LocY, 0);
    }

    @Override
    public View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_map, null);
        map = (MapView) view.findViewById(R.id.map);
        map.onCreate(bundle);
        aMap = map.getMap();

//        aMap.setLocationSource(this);
//        aMap.getUiSettings().setMyLocationButtonEnabled(true);
//        aMap.setMyLocationEnabled(true);

        // 自定义系统定位小蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.mipmap.icon_location));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
        // myLocationStyle.anchor(int,int)//设置小蓝点的锚点
        myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
        aMap.setMyLocationStyle(myLocationStyle);
//        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
//        不让父控件拦截地图的触摸事件，避免滑动地图时整个页面也跟着滑动
        aMap.setOnMapTouchListener(new AMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                map.getParent().requestDisallowInterceptTouchEvent(true);
            }
        });
        showEstateLocation();

        CameraPosition cameraPosition = new CameraPosition(latLng, 16, 0, 30);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        aMap.moveCamera(cameraUpdate);
        GeocodeSearch geocodeSearch = new GeocodeSearch(activity);
        geocodeSearch.setOnGeocodeSearchListener(this);
        LatLonPoint latLonPoint = new LatLonPoint(latLng.latitude, latLng.longitude);
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200, GeocodeSearch.AMAP);
        geocodeSearch.getFromLocationAsyn(query);


        mRadioGroup = (RadioGroup) view.findViewById(R.id.map_radioGroup);
        mRadioGroup.setOnCheckedChangeListener(this);
        return view;
    }

    private void showEstateLocation() {
        latLng = new LatLng(24.274248, 116.134163); //御景东方坐标
        markerOptions = new MarkerOptions();
        markerOptions.position(latLng).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ico_map_point));
        aMap.addMarker(markerOptions);
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        regeocodeAddress = regeocodeResult.getRegeocodeAddress();
        cityName = regeocodeAddress.getCity();
        L.i(regeocodeAddress.getCity());
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        this.checkedId = checkedId;
        switch (checkedId){
            case R.id.radio_bus:
                startPoiAroundSearch("公交");
                break;
            case R.id.radio_edu:
                startPoiAroundSearch("学校");
                break;
            case R.id.radio_shop:
                startPoiAroundSearch("超市");
                break;
            case R.id.radio_hospital:
                startPoiAroundSearch("医院");
                break;
            case R.id.radio_subway:
                startPoiAroundSearch("地铁");
                break;
            case R.id.radio_bank:
                startPoiAroundSearch("银行");
                break;
        }
    }

    //    周边关键词搜索，显示30条数据，搜索半径为一公里
    private void startPoiAroundSearch(String keyWord){
        query = new PoiSearch.Query(keyWord,"",cityName);
        query.setPageSize(30);// 设置每页最多返回多少条 poiitem
        query.setPageNum(0);//设置查第一页
        PoiSearch poiSearch = new PoiSearch(activity, query);
        poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(
                markerOptions.getPosition().latitude,
                markerOptions.getPosition().longitude), 1000));
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        if (poiResult.getQuery().equals(query)) {// 是否是同一条
//            result = poiResult;
            ArrayList<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
//            PoiItem poiItem = poiItems.get(0);
            List<SuggestionCity> suggestionCities = poiResult
                    .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
            if (poiItems != null && poiItems.size() > 0) {
                aMap.clear();// 清理之前的图标
                showEstateLocation();
//                PoiOverlay poiOverlay = new PoiOverlay(aMap, poiItems);
//                poiOverlay.removeFromMap();
//                poiOverlay.addToMap();


                for (int n = 0; n < poiItems.size(); n++){
                    PoiItem poiItem = poiItems.get(n);
                    LatLng latLng = new LatLng(poiItem.getLatLonPoint().getLatitude(), poiItem.getLatLonPoint().getLongitude());
                    MarkerOptions marker = new MarkerOptions();
                    int resId = -1;
                    switch (checkedId){
                        case R.id.radio_bus:
                            resId = R.mipmap.ico_map_bus;
                            break;
                        case R.id.radio_edu:
                            resId = R.mipmap.ico_map_edu;
                            break;
                        case R.id.radio_shop:
                            resId = R.mipmap.ico_map_shop;
                            break;
                        case R.id.radio_hospital:
                            resId = R.mipmap.ico_map_hospital;
                            break;
                        case R.id.radio_subway:
                            resId = R.mipmap.ico_map_subway;
                            break;
                        case R.id.radio_bank:
                            resId = R.mipmap.ico_map_bank;
                            break;
                    }
                    View v = createPoiIcon(resId, poiItem.getTitle(), poiItem.getSnippet());
                    marker.position(latLng).icon(BitmapDescriptorFactory.fromView(v))
                            .title(poiItem.getTitle())
                            .snippet(poiItem.getSnippet());
                    aMap.addMarker(marker);
                }
//                nextButton.setClickable(true);// 设置下一页可点
            }
        }
    }

    private View createPoiIcon(int resId, final String name, final String address){
        ImageView icon = new ImageView(activity);
        icon.setImageResource(resId);
        return icon;
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
//        deactivate();
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        map.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        map.onSaveInstanceState(outState);
    }
}
