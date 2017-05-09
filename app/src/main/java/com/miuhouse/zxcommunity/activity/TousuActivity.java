package com.miuhouse.zxcommunity.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.activity.album.MultiImageSelectorActivity;
import com.miuhouse.zxcommunity.adapter.UpdateImageAdapter;
import com.miuhouse.zxcommunity.application.MyApplication;
import com.miuhouse.zxcommunity.bean.BaseBean;
import com.miuhouse.zxcommunity.bean.Complain;
import com.miuhouse.zxcommunity.db.AccountDBTask;
import com.miuhouse.zxcommunity.http.CustomStringRequest;
import com.miuhouse.zxcommunity.http.FinalData;
import com.miuhouse.zxcommunity.http.VolleySingleton;
import com.miuhouse.zxcommunity.utils.ToastUtils;
import com.miuhouse.zxcommunity.widget.StatusCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 投诉
 * Created by kings on 2/1/2016.
 */
public class TousuActivity extends BaseActivity {
    private static final int REQUEST_IMAGE = 3;
    private ArrayList<String> mSelectPath;
    private RadioGroup rogType;
    private EditText etInput;
    private GridView mGridView;
    private UpdateImageAdapter adapter;
    // 投诉2，建议3
    private int type = 2;
    private long id;
    private String theLarge;
    private boolean isBiaoyang;
    private ArrayList<String> imageList = new ArrayList<>();
    private long propertyID;

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
    public void initTitle() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.titlebar);
        mToolbar.setNavigationIcon(R.mipmap.back_black);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        StatusCompat.compat(this, StatusCompat.COLOR_DEFAULT);
        TextView title = (TextView) findViewById(R.id.title);
        title.setText("投诉");
        title.setTextColor(Color.parseColor("#1E2129"));
        title.setClickable(true);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_complain);
        etInput = (EditText) findViewById(R.id.et_feedback);
        mGridView = (GridView) findViewById(R.id.gv_tousu);
        mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        rogType = (RadioGroup) findViewById(R.id.rog_type);
        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
        adapter = new UpdateImageAdapter(this);
        mGridView.setAdapter(adapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (imageList.size() == position)
                    intetnMultiImageSelector();
            }
        });
        rogType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if (R.id.ckb_house == checkedId) {
                    type = 5;
                } else if (R.id.ckb_common == checkedId) {
                    type = 6;
                } else if (R.id.ckb_other == checkedId) {
                    type = 7;
                }
            }
        });
        findViewById(R.id.action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity, MyComplainActivity.class)
                        .putExtra("type", Complain.COMPLAIN));
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void initVariables() {

        propertyID = MyApplication.getInstance().getUserBean().getPropertyId();
    }

    @Override
    public String getTag() {
        return null;
    }

    private void update() {

        String urlPath = FinalData.URL_VALUE + "saveRepairComplain";
        Map<String, Object> map = new HashMap<String, Object>();
        String str = etInput.getText().toString().trim();
        map.put("content", str);
        map.put("ownerId", AccountDBTask.getUserBean().getId());
        map.put("type", type);
        map.put("propertyId", propertyID);
        if (adapter.getImageUrls().size() > 0)
            map.put("images", adapter.getImageUrls());
        CustomStringRequest custom = new CustomStringRequest(Request.Method.POST, urlPath, map, getListener(), getErrorListener());
        VolleySingleton.getInstance(this).addToRequestQueue(custom);
    }

    public Response.Listener<String> getListener() {
        return new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                // TODO Auto-generated method stub
                Gson gson = new Gson();
                BaseBean message = gson.fromJson(response, BaseBean.class);
                ToastUtils.showToast(TousuActivity.this, message.getMsg());
                etInput.setText("");
                imageList.clear();
                adapter.addData(imageList);
            }
        };
    }

    public Response.ErrorListener getErrorListener() {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub

            }
        };
    }

    private void intetnMultiImageSelector() {
        int selectedMode = MultiImageSelectorActivity.MODE_MULTI;
        int maxNum = 9;
        Intent intent = new Intent(this, MultiImageSelectorActivity.class);
        // 是否显示拍摄图片
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        // 最大可选择图片数量
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, maxNum);
        // 选择模式
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, selectedMode);
        // 默认选择
        if (mSelectPath != null && mSelectPath.size() > 0) {
            intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, mSelectPath);
        }
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                imageList.addAll(mSelectPath);
                adapter.addData(imageList);
            }
        }
    }
}
