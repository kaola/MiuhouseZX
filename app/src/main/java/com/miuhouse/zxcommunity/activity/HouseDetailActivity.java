package com.miuhouse.zxcommunity.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hyphenate.easeui.EaseConstant;
import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.activity.photo.GalleryActivity;
import com.miuhouse.zxcommunity.adapter.EquipAdapter;
import com.miuhouse.zxcommunity.adapter.GuessAdapter;
import com.miuhouse.zxcommunity.bean.House;
import com.miuhouse.zxcommunity.http.CustomStringRequest;
import com.miuhouse.zxcommunity.http.FinalData;
import com.miuhouse.zxcommunity.http.GsonRequest;
import com.miuhouse.zxcommunity.http.VolleySingleton;
import com.miuhouse.zxcommunity.utils.Constants;
import com.miuhouse.zxcommunity.utils.MyUtils;
import com.miuhouse.zxcommunity.widget.AutoColumnLinearLayout;
import com.miuhouse.zxcommunity.widget.MyGridView;
import com.miuhouse.zxcommunity.widget.ProgressFragment;
import com.miuhouse.zxcommunity.widget.StatusCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by khb on 2016/1/18.
 */
public class HouseDetailActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = "HouseDetailActivity";
    public final static String HOUSE = "house";
    public final static String TAG_PURPOSE = "tag";
    public static final String CAN_MODIFY = "canModify";
    public static final String ID = "id";

    private House house;
    private ImageView mIvHouse;
    private TextView mTvHouseTitle;
    private TextView mTvPriceTotal;
    private TextView mTvPricePer;
    private TextView mTvHouseLocation;
    private AutoColumnLinearLayout mTagList;
    private TextView mArea;
    private TextView mType;
    private TextView mDecoration;
    private TextView mFacing;
    private TextView mFloors;
    private LinearLayout mYearLayout;
    private TextView mYear;
    private TextView mDesc;
    private TextView mLayout;
    private ImageView mLayoutDiagram;
    private ImageView mLocationImg;
    private RelativeLayout mLocationLayout;
    private MyGridView mEquipments;
    private MyGridView mGVGuessList;
    private List<String> tagList = new ArrayList<>();
    private TextView mSurrounding;

    private int mTag;
    private List<House> mGuessList = new ArrayList<>();
    private GuessAdapter mGuessAdapter;
    private GuessAdapter mGAdapter;
    private PopupWindow popupWindow;
    private boolean canModify;
    private TextView mTvImgPosition;

    private final int REQ_UPDATE = 1;
    private String id;
    private TextView title;
    private ScrollView container;
    private ViewGroup nonetwork;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuItem actionItem = menu.findItem(R.id.action);
        if (canModify) {
            actionItem.setVisible(true);
            actionItem.setTitle("修改信息");
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action:
                startActivityForResult(new Intent(activity, ReleaseHouseActivity.class)
                        .putExtra(ReleaseHouseActivity.TAG_FUNCTON, mTag)
                        .putExtra(ReleaseHouseActivity.HOUSE, house)
                        , REQ_UPDATE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initTitle() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.titlebar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.mipmap.back_black);
        setSupportActionBar(toolbar);
        StatusCompat.compat(this, StatusCompat.COLOR_DEFAULT);
        /*
      分享按钮
     */
        findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUtils.handleShare(HouseDetailActivity.this, "http://zx.miuhouse.com/mobile/saleRoomInfo/"
                        + house.getId(), house.getTitle() + " " + house.getPrice() + "万/套", (house.getImages() != null && house.getImages().size() > 0) ? house.getImages().get(0) : null);
            }
        });
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_housedetail);
        container = (ScrollView) findViewById(R.id.scrollView);
        mIvHouse = (ImageView) findViewById(R.id.iv_house);
        mTvImgPosition = (TextView) findViewById(R.id.img_position);
        mTvHouseTitle = (TextView) findViewById(R.id.tv_house_title);
        mTvPriceTotal = (TextView) findViewById(R.id.tv_house_price_total);
        mTvPricePer = (TextView) findViewById(R.id.tv_house_price_per);
        mTvHouseLocation = (TextView) findViewById(R.id.tv_house_location);


        mArea = (TextView) findViewById(R.id.area);
        mType = (TextView) findViewById(R.id.type);
        mDecoration = (TextView) findViewById(R.id.decoration);
        mFacing = (TextView) findViewById(R.id.facing);
        mFloors = (TextView) findViewById(R.id.floors);
        mYearLayout = (LinearLayout) findViewById(R.id.yearLimitLayout);
        mYear = (TextView) findViewById(R.id.yearLimit);
        mEquipments = (MyGridView) findViewById(R.id.equipments);
        mDesc = (TextView) findViewById(R.id.desc);
        mLayout = (TextView) findViewById(R.id.layout);
        mLayoutDiagram = (ImageView) findViewById(R.id.layout_diagram);
        mLocationLayout = (RelativeLayout) findViewById(R.id.locationLayout);
        mSurrounding = (TextView) findViewById(R.id.surrounding);
        mLocationImg = (ImageView) findViewById(R.id.locationImg);
        mGVGuessList = (MyGridView) findViewById(R.id.guessList);
//        猜你喜欢列表刷新后页面会跳到猜你喜欢顶部，
//        这里解决方案是先将列表隐藏，到滚到底部时再显示列表
        mGVGuessList.setVisibility(View.GONE);
        container.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        int offY = v.getScrollY();
                        int visibleHeight = v.getHeight();
                        int actualHeight = container.getChildAt(0).getMeasuredHeight();
                        if ((offY + visibleHeight) == actualHeight) {
                            mGVGuessList.setVisibility(View.VISIBLE);
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int offYy = v.getScrollY();
                        int visibleHeightt = v.getHeight();
                        int actualHeightt = container.getChildAt(0).getMeasuredHeight();
                        if ((offYy + visibleHeightt) == actualHeightt) {
                            mGVGuessList.setVisibility(View.VISIBLE);
                        }
                        break;
                }
                return false;
            }
        });
        nonetwork = (ViewGroup) findViewById(R.id.nonetwork);
        nonetwork.setVisibility(View.GONE);
        nonetwork.findViewById(R.id.iv_reload).setOnClickListener(this);
        findViewById(R.id.consult).setVisibility(View.GONE);
        setupView();
    }

    private void setupView() {
        if (house == null) {
            return;
        }
        title = (TextView) findViewById(R.id.title);
        title.setTextColor(Color.parseColor("#1E2129"));
        title.setText(house.getPropertyName());
        if (!MyUtils.isEmptyList(house.getImages())) {
            Glide.with(activity).load(house.getImages().get(0)).placeholder(R.mipmap.default_error_big).into(mIvHouse);
            mTvImgPosition.setText("1/" + house.getImages().size());
        } else {
            mTvImgPosition.setVisibility(View.GONE);
        }
        mIvHouse.setOnClickListener(this);
        mTvHouseTitle.setText(house.getTitle());
        mTvPriceTotal.setText(house.getPrice() + "万/套");
        mTvPricePer.setText("(自己算)");
        mTvHouseLocation.setText("地址：" + house.getAddress() + (house.getStress() == null ? "" : house.getStress()));
        mArea.setText(house.getArea() + "平米");
        mType.setText(house.getJzlx());
        mDecoration.setText(house.getZxqk());
        mFacing.setText(house.getCx());
        mFloors.setText(house.getHouseNum());
        mYear.setText(house.getCqnx());
        if (!MyUtils.isEmptyList(tagList)) {
            tagList.clear();
        }
        if (house.getLabel() != null) {
            tagList.addAll(house.getLabel());
        }
        mDesc.setText(house.getRemark());
        mLayout.setText(house.getHuxing());
        findViewById(R.id.layout_diagram).setOnClickListener(this);
        if (house.getHuxingImage() != null) {
            Glide.with(activity).load(house.getHuxingImage()).placeholder(R.mipmap.default_error_big).into(mLayoutDiagram);
        }
        mLocationLayout.setOnClickListener(this);
        mSurrounding.setText(house.getAddress());
        if (house.getMapUrl() != null) {
            Glide.with(activity).load(house.getMapUrl()).placeholder(R.mipmap.default_error_big).into(mLocationImg);
        }
//        添加标签
        List<String> mLabelList = new ArrayList<>();
        mLabelList.addAll(house.getLabel());
        mTagList = (AutoColumnLinearLayout) findViewById(R.id.tagList);
        for (int i = 0; i < mLabelList.size(); i++) {
            TextView item = createTagItem(mLabelList.get(i), i + 1);
            mTagList.addView(item);
        }

        if (mTag == Constants.LEASE) {
            EquipAdapter mEAdapter = new EquipAdapter(this, house.getPtss());
            mEquipments.setAdapter(mEAdapter);
            mTvPriceTotal.setText(house.getPrice() + "元/月");
            mTvPricePer.setVisibility(View.GONE);
            mYearLayout.setVisibility(View.GONE);
        } else {
            int totalPrice = house.getPrice();
            double area = house.getArea();
            int pricePerSquare = (int) Math.rint(totalPrice * 10000 / area);
            mTvPriceTotal.setText(totalPrice + "万/套");
            mTvPricePer.setText("(" + pricePerSquare + "元/平米)");
            mEquipments.setVisibility(View.GONE);
            findViewById(R.id.equipContainer).setVisibility(View.GONE);
        }
        mGAdapter = new GuessAdapter(this, mGuessList);
        mGVGuessList.setAdapter(mGAdapter);
        mGVGuessList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(activity, HouseDetailActivity.class)
                        .putExtra(TAG_PURPOSE, mTag)
                        .putExtra(ID, mGuessList.get(position).getId()));
            }
        });
        if (canModify) { //canmodify 表示是查看用户自己发布的房源，可以进入修改页面，不用显示咨询，不用显示猜你喜欢
            findViewById(R.id.consult).setVisibility(View.GONE);
            findViewById(R.id.guessContainer).setVisibility(View.GONE);
            return;
        } else {
            findViewById(R.id.consult).setVisibility(View.VISIBLE);
        }
        getGuessList();
    }

    /**
     * 生成房源标签TextView
     *
     * @param text     文字
     * @param position 第几个
     * @return
     */
    private TextView createTagItem(String text, int position) {
        TextView tagItem = new TextView(this);
        tagItem.setPadding(MyUtils.dip2px(this, 5), MyUtils.dip2px(this, 3), MyUtils.dip2px(this, 5), MyUtils.dip2px(this, 3));
        tagItem.setTextColor(getResources().getColorStateList(R.color.white_pure));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.rightMargin = MyUtils.dip2px(this, 10);
        tagItem.setLayoutParams(params);
        tagItem.setText(text);
        int mod = position % 3;
        switch (mod) {
            case 1:
                tagItem.setBackgroundResource(R.color.bg_green);
                break;
            case 2:
                tagItem.setBackgroundResource(R.color.bg_red);
                break;
            case 0:
                tagItem.setBackgroundResource(R.color.bg_blue);
                break;
        }
        return tagItem;
    }

    @Override
    public void initData() {
        house = (House) getIntent().getSerializableExtra(HOUSE);
        mTag = getIntent().getIntExtra(TAG_PURPOSE, -1);
        Log.i("TAG", "mTag=" + mTag);
        canModify = getIntent().getBooleanExtra(CAN_MODIFY, false);
        id = getIntent().getStringExtra(ID);
        if (house == null) {
            getHouseById(id);
        }
    }

    private void getHouseById(String id) {

        final ProgressFragment progress = ProgressFragment.newInstance();
        progress.setMessage(getResources().getString(R.string.loading));
        progress.show(getSupportFragmentManager(), getClass().getName());

        String url = FinalData.URL_VALUE + (mTag == Constants.LEASE ? "roomInfo" : "saleRoomInfo");
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        CustomStringRequest request = new CustomStringRequest(Request.Method.POST, url, map,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            progress.dismiss();
                            JSONObject jsonObject = new JSONObject(s);
                            int code = jsonObject.getInt("code");
                            if (code == 0) {
//                                JSONObject jsonHouse = jsonObject.getJSONObject(mTag == Constants.LEASE ? "zf" : "esf");
                                Gson gson = new Gson();
                                house = gson.fromJson(jsonObject.getString(mTag == Constants.LEASE ? "zf" : "esf"), House.class);
                                setupView();
                            } else {
                                nonetwork.setVisibility(View.VISIBLE);
                                findViewById(R.id.consult).setVisibility(View.GONE);
                                container.setVisibility(View.GONE);
                                ((TextView) nonetwork.findViewById(R.id.tv_main)).setText(getResources().getString(R.string.loading_failed));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progress.dismiss();
                        if (!MyUtils.isNetworkConnected(context)) {
                            findViewById(R.id.nonetwork).setVisibility(View.VISIBLE);
                            findViewById(R.id.scrollView).setVisibility(View.GONE);
                            findViewById(R.id.consult).setVisibility(View.GONE);
                            return;
                        }
                        nonetwork.setVisibility(View.VISIBLE);
                        findViewById(R.id.consult).setVisibility(View.GONE);
                        container.setVisibility(View.GONE);
                        ((TextView) nonetwork.findViewById(R.id.tv_main)).setText(getResources().getString(R.string.request_failed));
                    }
                });
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    @Override
    public void initVariables() {

    }

    @Override
    public String getTag() {
        return TAG;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_house:
                if (!MyUtils.isEmptyList(house.getImages())) {
                    startActivity(new Intent(activity, GalleryActivity.class)
                            .putStringArrayListExtra("imgPath", (ArrayList<String>) house.getImages()));
                }
                break;
            case R.id.locationLayout:
                startActivity(new Intent(this, MapActivity.class)
                        .putExtra(MapActivity.LocX, house.getLoc().getY())
                        .putExtra(MapActivity.LocY, house.getLoc().getX())
                        .putExtra(MapActivity.TITLE, house.getPropertyName()));
                break;
            case R.id.layout_diagram:
                if (!MyUtils.isEmpty(house.getHuxingImage())) {
                    ArrayList<String> images = new ArrayList<>();
                    images.add(house.getHuxingImage());
                    startActivity(new Intent(activity, GalleryActivity.class)
                            .putStringArrayListExtra("imgPath", images));
                }
                break;
            case R.id.iv_reload:
                getHouseById(id);
                nonetwork.setVisibility(View.GONE);
                container.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void getGuessList() {
        String url;
        if (mTag == Constants.SELL) {
            url = FinalData.URL_VALUE + "likeSaleList";
        } else {
            url = FinalData.URL_VALUE + "likeZfList";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("propertyId", house.getPropertyId());   //显示和该房源同一小区的的房子
        map.put("page", 1);
        map.put("pageSize", 20);
        GsonRequest<HouseListActivity.HouseBean> gRequest = new GsonRequest<HouseListActivity.HouseBean>(Request.Method.POST, url, HouseListActivity.HouseBean.class, map,
                new Response.Listener<HouseListActivity.HouseBean>() {
                    @Override
                    public void onResponse(HouseListActivity.HouseBean houseBean) {
                        if (houseBean != null && houseBean.getCode() == 0) {
                            if (MyUtils.isEmptyList(houseBean.getList())) {
                                return;
                            }
                            if (!MyUtils.isEmptyList(mGuessList)) {
                                mGuessList.clear();
                            }
                            mGuessList.addAll(houseBean.getList());
                            mGAdapter.notifyDataSetChanged();
                        } else {
//                            showToast("请求发生错误，请稍后再试");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
//                showToast("请求发生错误，请稍后再试");
            }
        });
        gRequest.setTag(TAG);
        VolleySingleton.getInstance(activity).addToRequestQueue(gRequest);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_UPDATE) {
            finish();
        }
    }

    public void telConsult(View view) {
        if (house != null && house.getMobile() != null) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                } else {
                    Toast.makeText(this, "权限已被禁止，要想重新开启，请在手机的权限管理中找到" + getResources().getString(R.string.app_name) + "应用，找到拨打电话权限并选择允许", Toast.LENGTH_LONG).show();
                }
            } else {
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri data = Uri.parse("tel:" + house.getMobile());
                intent.setData(data);
                startActivity(intent);
            }

        }
    }

    public void onlineConsult(View view) {
        startActivity(new Intent(activity, ChatActivity.class)
                .putExtra(EaseConstant.EXTRA_USER_ID, Constants.EASEACCOUNTHEAD + house.getOwnerId())
                .putExtra("nickname", house.getNickname()));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri data = Uri.parse("tel:" + house.getMobile());
                intent.setData(data);
                startActivity(intent);
            } else {
                Toast.makeText(this, "权限被拒绝", Toast.LENGTH_LONG).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
