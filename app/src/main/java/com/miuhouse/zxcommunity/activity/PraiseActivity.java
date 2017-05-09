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
import com.miuhouse.zxcommunity.utils.Constants;
import com.miuhouse.zxcommunity.utils.ToastUtils;
import com.miuhouse.zxcommunity.widget.ProgressFragment;
import com.miuhouse.zxcommunity.widget.StatusCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 表扬物业
 * Created by kings on 2/1/2016.
 */
public class PraiseActivity extends BaseActivity {

    private static final String TAG = "BaoxiuActivity";
    private static final int REQUEST_IMAGE = 3;
    //房屋内
    private static final int HOUSE = 5;
    //公共区
    private static final int COMMON = 6;
    //其他
    private static final int OTHER = 7;
    private ArrayList<String> mSelectPath;

    private EditText etInput;
    private GridView mGridView;
    private ProgressFragment progress;
    private UpdateImageAdapter updateAdapter;

    private ArrayList<String> imageList = new ArrayList<>();

    private int type = 4;
    private String theLarge;
    private long propertyID;

    @Override
    public void initTitle() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.titlebar);
        mToolbar.setNavigationIcon(R.mipmap.back_black);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        StatusCompat.compat(this, StatusCompat.COLOR_DEFAULT);
        TextView title = (TextView) findViewById(R.id.title);
        title.setText("表扬物业");
        title.setTextColor(Color.parseColor("#1E2129"));
        title.setClickable(true);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_baoxiu);

        findViewById(R.id.relative_radio).setVisibility(View.GONE);
        etInput = (EditText) findViewById(R.id.et_feedback);
        mGridView = (GridView) findViewById(R.id.gv_baoxiu);

        etInput.setHint("如果觉得物业不错，请鼓励一下他们");

        progress = ProgressFragment.newInstance();

        mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));

        updateAdapter = new UpdateImageAdapter(this);
        mGridView.setAdapter(updateAdapter);
        updateAdapter.setShape(true);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (imageList.size() == position)
                    intetnMultiImageSelector();
            }
        });

        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.show(PraiseActivity.this.getSupportFragmentManager(), "baoxiu");
                update();
            }
        });

        updateAdapter.setOnDelectClickListener(new UpdateImageAdapter.OnDelectClickListener() {
            @Override
            public void onDelectClick(int position) {
                updateAdapter.select(imageList.get(position));
                imageList.remove(imageList.get(position));
            }
        });

        findViewById(R.id.action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity, MyComplainActivity.class)
                        .putExtra("type", Complain.LIKE));
            }
        });
    }

    private void intetnMultiImageSelector() {
        int selectedMode = MultiImageSelectorActivity.MODE_MULTI;

        Intent intent = new Intent(this, MultiImageSelectorActivity.class);
        // 是否显示拍摄图片
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        // 最大可选择图片数量
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, Constants.MAX_NUM);
        // 选择模式
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, selectedMode);
        // 默认选择
        if (mSelectPath != null && mSelectPath.size() > 0) {
            intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, mSelectPath);
        }
        startActivityForResult(intent, REQUEST_IMAGE);
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
        return TAG;
    }


    private void update() {
        String urlPath = FinalData.URL_VALUE + "saveRepairComplain";
        Map<String, Object> map = new HashMap<String, Object>();
        String str = etInput.getText().toString().trim();
        map.put("content", str);
        map.put("ownerId", AccountDBTask.getUserBean().getId());
        map.put("type", type);
        map.put("propertyId", propertyID);
        if (updateAdapter.getImageUrls().size() > 0)
            map.put("images", updateAdapter.getImageUrls());
        CustomStringRequest custom = new CustomStringRequest(Request.Method.POST, urlPath, map, getListener(), getErrorListener());
        custom.setTag(TAG);
        VolleySingleton.getInstance(context).addToRequestQueue(custom);
    }

    public Response.Listener<String> getListener() {
        return new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                // TODO Auto-generated method stub
                progress.dismissAllowingStateLoss();
                Gson gson = new Gson();
                BaseBean message = gson.fromJson(response, BaseBean.class);
                ToastUtils.showToast(PraiseActivity.this, message.getMsg());
                etInput.setText("");

            }
        };
    }

    public Response.ErrorListener getErrorListener() {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                progress.dismissAllowingStateLoss();

                ToastUtils.showToast(PraiseActivity.this, "请求失败");
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                StringBuilder sb = new StringBuilder();

                imageList.addAll(mSelectPath);
                updateAdapter.addData(imageList);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

}
