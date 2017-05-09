package com.miuhouse.zxcommunity.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.adapter.HouseListDeletableAdapter;
import com.miuhouse.zxcommunity.bean.House;
import com.miuhouse.zxcommunity.http.CustomStringRequest;
import com.miuhouse.zxcommunity.http.FinalData;
import com.miuhouse.zxcommunity.http.GsonRequest;
import com.miuhouse.zxcommunity.http.VolleySingleton;
import com.miuhouse.zxcommunity.utils.Constants;
import com.miuhouse.zxcommunity.utils.MyUtils;
import com.miuhouse.zxcommunity.widget.ProgressFragment;
import com.miuhouse.zxcommunity.widget.StatusCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by khb on 2016/2/1.
 */
public class MyHousesActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG="MyHousesActivity";
    public final static String TAG_SELL_OR_RENT = "houseTag";
    public static final String USERID = "userId";

    private int tag;
    private RelativeLayout mAddHouse;
    private RecyclerView mRVHouseList;
    private String userId;

    private List<House> mHouseList = new ArrayList<>();
    private HouseListDeletableAdapter mAdapter;
    private MenuItem actionItem;

    @Override
    public void initTitle() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.titlebar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.mipmap.back_black);
        setSupportActionBar(toolbar);
        StatusCompat.compat(this, StatusCompat.COLOR_DEFAULT);
        TextView title = (TextView) findViewById(R.id.title);
        if (tag == Constants.LEASE) {
            title.setText("租房管理");
        }else{
            title.setText("售房管理");
        }
        title.setTextColor(Color.parseColor("#1E2129"));
        title.setClickable(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        actionItem = menu.findItem(R.id.action);
        actionItem.setVisible(true);
        actionItem.setTitle("编辑");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.action:
                if (!mAdapter.isEditing()) {
                    mAdapter.startEditing();
                    item.setTitle("完成");
                }else{
                    mAdapter.finishEditing();
                    item.setTitle("编辑");
                }
                mAdapter.notifyDataSetChanged();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_myhouses);
        mAddHouse = (RelativeLayout) findViewById(R.id.addNewHouse);
        TextView mTvAddNewHouse = (TextView) findViewById(R.id.tv_addNewHouse);
        mTvAddNewHouse.setText(tag == Constants.SELL ? "添加新的售房" : "添加新的租房");

        mAddHouse.setOnClickListener(this);
        mRVHouseList = (RecyclerView) findViewById(R.id.houseList);
        mAdapter = new HouseListDeletableAdapter(this, mHouseList, tag);
        mRVHouseList.setLayoutManager(new LinearLayoutManager(this));
        mRVHouseList.setAdapter(mAdapter);
    }

    private void deleteHouse(final House house) {

        showDialog(house);

    }

    private void showDialog(final House house) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确认要将该房源下架？")
                .setTitle("警告")
                .setCancelable(true)
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String url = FinalData.URL_VALUE + (tag == Constants.SELL ? "saleRoomDown" : "roomDown");
                Map<String, Object> map = new HashMap<>();
                map.put("id", house.getId());
                map.put("type", House.UNAVAILABLE);
                CustomStringRequest request = new CustomStringRequest(Request.Method.POST, url, map,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                house.setType(House.UNAVAILABLE);
                                mAdapter.notifyDataSetChanged();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {

                            }
                        });
                request.setTag(TAG);
                VolleySingleton.getInstance(activity).addToRequestQueue(request);
            }
        });
        builder.create().show();

    }

    @Override
    public void initData() {
        if (!MyUtils.isLoggedIn()){
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        tag = getIntent().getIntExtra(TAG_SELL_OR_RENT, -1);
        userId = getIntent().getStringExtra(USERID);
    }

    private void getHouseList() {

        final ProgressFragment progress = new ProgressFragment();
        progress.setMessage(getResources().getString(R.string.loading));
        progress.show(getSupportFragmentManager(), getClass().getName());

        String url = FinalData.URL_VALUE +
                (tag == Constants.SELL ? "mySaleList" : "myZfList");
        Map<String, Object> map = new HashMap<>();
        map.put("ownerId", userId);
        map.put("page", 1);
        map.put("pageSize", 20);
        GsonRequest gsonRequest = new GsonRequest<HouseListActivity.HouseBean>(Request.Method.POST, url, HouseListActivity.HouseBean.class, map,
                new Response.Listener<HouseListActivity.HouseBean>() {
                    @Override
                    public void onResponse(HouseListActivity.HouseBean houseBean) {
                        progress.dismiss();
                        if (houseBean!=null && !MyUtils.isEmptyList(houseBean.getList())){
                            if (!MyUtils.isEmptyList(mHouseList)){
                                mHouseList.clear();
                            }
                            mHouseList.addAll(houseBean.getList());
                            mRVHouseList.setAdapter(mAdapter);
                            mAdapter.setOnHouseClickListener(new HouseListDeletableAdapter.OnHouseClickListener() {
                                @Override
                                public void onHouseClick(View view, int position) {
                                    startActivity(new Intent(activity, HouseDetailActivity.class)
                                            .putExtra(HouseDetailActivity.HOUSE, mHouseList.get(position))
                                            .putExtra(HouseDetailActivity.TAG_PURPOSE, tag)
                                            .putExtra(HouseDetailActivity.CAN_MODIFY, true));
                                }

                                @Override
                                public void onHouseDeleteClick(View view, int position) {
                                    deleteHouse(mHouseList.get(position));
                                }
                            });
                            mAdapter.notifyDataSetChanged();
                        }else{
                            showToast("没有找到您的发布房源");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progress.dismiss();
                        showToast("请求发生错误，请稍后再试");
                    }
                });
        VolleySingleton.getInstance(this).addToRequestQueue(gsonRequest);
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
        switch (v.getId()){
            case R.id.addNewHouse:
                startActivity(new Intent(this, ReleaseHouseActivity.class)
                .putExtra(ReleaseHouseActivity.TAG_FUNCTON, tag));
                break;
        }
    }

    @Override
    protected void onResume() {
        getHouseList();
        if (actionItem != null) {
            actionItem.setTitle("编辑");
        }
        super.onResume();
    }
}
