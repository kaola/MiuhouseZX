package com.miuhouse.zxcommunity.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.activity.album.MultiImageSelectorActivity;
import com.miuhouse.zxcommunity.adapter.EquipCheckableAdapter;
import com.miuhouse.zxcommunity.adapter.ImageSelectAdapter;
import com.miuhouse.zxcommunity.adapter.SimpleAdapter;
import com.miuhouse.zxcommunity.application.MyApplication;
import com.miuhouse.zxcommunity.bean.House;
import com.miuhouse.zxcommunity.bean.UserBean;
import com.miuhouse.zxcommunity.http.CustomStringRequest;
import com.miuhouse.zxcommunity.http.FinalData;
import com.miuhouse.zxcommunity.http.VolleySingleton;
import com.miuhouse.zxcommunity.utils.Constants;
import com.miuhouse.zxcommunity.utils.L;
import com.miuhouse.zxcommunity.utils.MyCount;
import com.miuhouse.zxcommunity.utils.MyUtils;
import com.miuhouse.zxcommunity.utils.SPUtils;
import com.miuhouse.zxcommunity.utils.StringUtils;
import com.miuhouse.zxcommunity.widget.BottomListDrawer;
import com.miuhouse.zxcommunity.widget.MyGridView;
import com.miuhouse.zxcommunity.widget.MyPopupWindow;
import com.miuhouse.zxcommunity.widget.StatusCompat;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by khb on 2016/1/25.
 */
public class ReleaseHouseActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    public static final String HOUSE = "house";
    private EditText mEtHouseTitle;
    private TextView mTvHouseType;
    private EditText mEtHousePrice;
//    private TextView mTvHouseTag;
//    private EditText mEtHouseArea;
//    private TextView mTvHouseLayout;
//    private LinearLayout mLlHouseFloor;
    private EditText mEtBuilding;
    private EditText mEtRoomNum;
    private TextView mTvHouseFacing;
    private MyGridView mGvEquipList;
    private TextView mTvHouseDecoration;
    private EditText mEtHouseDesc;
    private EditText mEtName;
    private EditText mEtPhone;
    private TextView mTvGetVerify;
    private EditText mEtVerify;

    private String[] mEquipList = SPUtils.getSPData(Constants.PTSS);
    private TextView commit;

    public static final String TAG_FUNCTON = "function";
    private int tag;
    private EquipCheckableAdapter eAdapter;

    private MyPopupWindow popup;
    private String[] mHouseTypeList = SPUtils.getSPData(Constants.JZLX);
    private String[] mHouseTagList;
//    private String[] mHouseLayoutList = SPUtils.getSPData(Constants.FX);
    private String[] mHouseFacingList = SPUtils.getSPData(Constants.CX);
    private String[] mDecorationList = SPUtils.getSPData(Constants.ZXQK);
    private String phone;
    private MyCount mc;
    private EventHandler eventHandler;
    private MyGridView mGVImageList;
    private List<String> mImageList = new ArrayList<>();
    private ImageSelectAdapter mImageAdapter;

    private static final int REQUEST_IMAGE = 3;
    private ArrayList<String> mSelectPath;
    private House house;
    private String tempStrTag;
    private List<String> tagList;
    private MyGridView mGVTagList;
    private EquipCheckableAdapter tagAdapter;
    private EditText mEtBuildYear;
    private EditText mEtChanquan;

    @Override
    public void initTitle() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.titlebar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.mipmap.back_black);
        setSupportActionBar(toolbar);
        StatusCompat.compat(this, StatusCompat.COLOR_DEFAULT);
        TextView title = (TextView) findViewById(R.id.title);
        if (house == null) {
            if (tag == Constants.LEASE) {
                title.setText("我要出租");
            } else {
                title.setText("我要卖房");
            }
        }else{
            title.setText("修改信息");
        }

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

        setContentView(R.layout.activity_releasehouse);
        mEtHouseTitle = (EditText) findViewById(R.id.houseTitle);
        mTvHouseType = (TextView) findViewById(R.id.houseType);
        mTvHouseType.setOnClickListener(this);
        mEtHousePrice = (EditText) findViewById(R.id.housePrice);
//        mTvHouseTag = (TextView) findViewById(R.id.houseTag);
//        mTvHouseTag.setOnClickListener(this);
        mGVTagList = (MyGridView) findViewById(R.id.tagList);
//        mEtHouseArea = (EditText) findViewById(R.id.houseArea);
//        mTvHouseLayout = (TextView) findViewById(R.id.houseLayout);
//        mTvHouseLayout.setOnClickListener(this);
//        mLlHouseFloor = (LinearLayout) findViewById(R.id.ll_houseFloors);
        mEtBuilding = (EditText) findViewById(R.id.building);
        mEtRoomNum = (EditText) findViewById(R.id.roomNum);
        mTvHouseFacing = (TextView) findViewById(R.id.houseFacing);
        mTvHouseFacing.setOnClickListener(this);
        mGvEquipList = (MyGridView) findViewById(R.id.equipList);
        mTvHouseDecoration = (TextView) findViewById(R.id.houseDecoration);
        mTvHouseDecoration.setOnClickListener(this);
        mEtBuildYear = (EditText) findViewById(R.id.buildYear);
        mEtChanquan = (EditText) findViewById(R.id.chanquan);

        mEtHouseDesc = (EditText) findViewById(R.id.houseDesc);
//        mEtName = (EditText) findViewById(R.id.name);
        mEtPhone = (EditText) findViewById(R.id.phone);
        mTvGetVerify = (TextView) findViewById(R.id.getVerify);
        mTvGetVerify.setOnClickListener(this);
        mEtVerify = (EditText) findViewById(R.id.verify);

        commit = (TextView) findViewById(R.id.commit);
        commit.setOnClickListener(this);

        mGVImageList = (MyGridView) findViewById(R.id.imageList);
        mGVImageList.setOnItemClickListener(this);

        TextView tvUnit = (TextView) findViewById(R.id.unit);
        LinearLayout equipListLayout = (LinearLayout) findViewById(R.id.equipListLayout);
        if (tag == Constants.LEASE) {
            mHouseTagList = SPUtils.getSPData(Constants.ZFLABEL);
            if (house!=null) {
                eAdapter = new EquipCheckableAdapter(this, Arrays.asList(mEquipList), house.getPtss());
            }else{
                eAdapter = new EquipCheckableAdapter(this, Arrays.asList(mEquipList), null);
            }
            findViewById(R.id.buildYearLayout).setVisibility(View.GONE);
            findViewById(R.id.chanquanLayout).setVisibility(View.GONE);
            mGvEquipList.setAdapter(eAdapter);
            equipListLayout.setVisibility(View.VISIBLE);
            tvUnit.setText("元/月");
        }else {
            mHouseTagList = SPUtils.getSPData(Constants.ESFLABEL);
            equipListLayout.setVisibility(View.GONE);
            tvUnit.setText("万/套");
        }
        tagList = new ArrayList<String>();
        if (house != null){     //house == null 时表示是修改房源页面，显示该房源的信息
            mImageList = house.getImages();
            mEtHouseTitle.setText(house.getTitle());
//            mTvHouseLayout.setText(house.getHuxing());
            tagList = house.getLabel();
            tempStrTag = null;
            if (!MyUtils.isEmptyList(tagList)) {

                for (int i = 0; i < tagList.size(); i++) {
                    if (i == 0){
                        tempStrTag = tagList.get(i);
                    }else {
                        tempStrTag += " "+tagList.get(i);
                    }
                }
            }
//            mTvHouseTag.setText(tempStrTag);
            mEtHousePrice.setText(house.getPrice() + "");
//            mEtHouseArea.setText(house.getArea() + "");
            mEtBuildYear.setText(house.getJznd());
            mEtChanquan.setText(house.getCqnx());
            mTvHouseType.setText(house.getJzlx());
            mEtBuilding.setText(house.getBuildName());
            mEtRoomNum.setText(house.getHouseNum() + "");
            mTvHouseFacing.setText(house.getCx());
            mTvHouseDecoration.setText(house.getZxqk());
            mEtHouseDesc.setText(house.getRemark());
        }
        mImageAdapter = new ImageSelectAdapter(this, mImageList);
        mGVImageList.setAdapter(mImageAdapter);
        if (house!=null) {
            tagAdapter = new EquipCheckableAdapter(this, Arrays.asList(mHouseTagList), house.getLabel());
        }else{
            tagAdapter = new EquipCheckableAdapter(this, Arrays.asList(mHouseTagList), null);
        }
        mGVTagList.setAdapter(tagAdapter);
    }

    @Override
    public void initData() {

        if (!MyUtils.isLoggedIn()){
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        tag = getIntent().getIntExtra(TAG_FUNCTON, -1);
        house = (House) getIntent().getSerializableExtra(HOUSE);
        SMSSDK.initSDK(activity, Constants.SMSSDK_APP_KEY, Constants.SMSSDK_APP_SECRET);
        eventHandler = new EventHandler() {
            @Override
            public void onRegister() {
                super.onRegister();
            }

            @Override
            public void onUnregister() {
                super.onUnregister();
            }

            @Override
            public void beforeEvent(int i, Object o) {
                super.beforeEvent(i, o);
            }

            @Override
            public void afterEvent(int event, int result, final Object data) {
                showLogD("event = " + event + "  result = " + result);
                showLogD("=======GetVerifyResult========");
                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                        Map<String, Object> map = (HashMap<String, Object>) data;
                        Log.w("TAG", "EVENT_SUBMIT_VERIFICATION_CODE " + map.toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sendRequest();
                            }
                        });
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        //获取验证码成功
                        Log.w("TAG", "get code success " + data);
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                        //返回支持发送验证码的国家列表
                        ArrayList<HashMap<String, Object>> countryList = (ArrayList<HashMap<String, Object>>) data;
                        Log.d("TAG", "countryList = " + countryList.toString());
                    }
                } else {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                ((Throwable) data).printStackTrace();
                                Throwable throwable = (Throwable) data;
                                throwable.printStackTrace();
                                JSONObject object = null;
                                object = new JSONObject(throwable.getMessage());
                                String des = object.optString("detail");//错误描述
                                int status = object.optInt("status");//错误代码
                                showLogD("SMS status " + status);
//                                showToast(des);
                                switch (status) {
                                    case 463:
                                        showToast("您的号码今天发送次数已达上限");
                                        break;
                                    case 468:
                                        showToast("验证码错误");
                                        break;
                                    default:
                                        showToast("验证码验证失败");
                                        break;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                showToast("验证码验证失败!!!");
                            }
//                                    progress.dismissAllowingStateLoss();
                        }
                    });

                }
            }
        };
        SMSSDK.registerEventHandler(eventHandler);
        SMSSDK.getSupportedCountries();
    }

    @Override
    public void initVariables() {

    }

    @Override
    public String getTag() {
        return null;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.commit:
                commit();
                break;
            case R.id.houseType:
                MyUtils.hideSoftKeyboard(mTvHouseType);
                SimpleAdapter adapter = new SimpleAdapter(activity, mHouseTypeList);
                final BottomListDrawer bottomListDrawer = new BottomListDrawer(activity);
                bottomListDrawer.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialogPlus, Object o, View view, int i) {
                        if (i!=0){
                            dialogPlus.dismiss();
                            mTvHouseType.setText(mHouseTypeList[i - 1]);
                        }
                    }
                });
                bottomListDrawer.setTitle("建筑类型");
                bottomListDrawer.setAdapter(adapter);
                bottomListDrawer.show();
                break;
//            case R.id.houseLayout:
//                MyUtils.hideSoftKeyboard(mTvHouseLayout);
//                SimpleAdapter layoutAdapter = new SimpleAdapter(activity, mHouseLayoutList);
//                final BottomListDrawer layoutDrawer = new BottomListDrawer(activity);
//                layoutDrawer.setOnItemClickListener(new OnItemClickListener() {
//                    @Override
//                    public void onItemClick(DialogPlus dialogPlus, Object o, View view, int i) {
//                        if (i!=0){
//                            dialogPlus.dismiss();
//                            mTvHouseLayout.setText(mHouseLayoutList[i - 1]);
//                        }
//                    }
//                });
//                layoutDrawer.setTitle("房型选择");
//                layoutDrawer.setAdapter(layoutAdapter);
//                layoutDrawer.show();
//                break;
            case R.id.houseFacing:
                MyUtils.hideSoftKeyboard(mTvHouseFacing);
                SimpleAdapter facingAdapter = new SimpleAdapter(activity, mHouseFacingList);
                final BottomListDrawer facingDrawer = new BottomListDrawer(activity);
                facingDrawer.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialogPlus, Object o, View view, int i) {
                        if (i != 0) {
                            dialogPlus.dismiss();
                            mTvHouseFacing.setText(mHouseFacingList[i - 1]);
                        }
                    }
                });
                facingDrawer.setTitle("建筑朝向");
                facingDrawer.setAdapter(facingAdapter);
                facingDrawer.show();
                break;
            case R.id.houseDecoration:
                MyUtils.hideSoftKeyboard(mTvHouseDecoration);
                SimpleAdapter decorAdapter = new SimpleAdapter(activity, mDecorationList);
                final BottomListDrawer decorDrawer = new BottomListDrawer(activity);
                decorDrawer.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialogPlus, Object o, View view, int i) {
                        if (i != 0) {
                            dialogPlus.dismiss();
                            mTvHouseDecoration.setText(mDecorationList[i - 1]);
                        }
                    }
                });
                decorDrawer.setTitle("装修情况");
                decorDrawer.setAdapter(decorAdapter);
                decorDrawer.show();
                break;
            case R.id.getVerify:
//                MyUtils.hideSoftKeyboard(mTvGetVerify);
                sendCode();
                break;


        }
    }

    private void sendRequest() {
        commit.setEnabled(false);
        UserBean user = MyApplication.getInstance().getUserBean();
        Map<String, Object> map = new HashMap<>();
        map.put("propertyId", user.getPropertyId());                //小区id
        map.put("title", mEtHouseTitle.getText().toString());       //标题
        List<String> mImageUrls = mImageAdapter.getImageUrls();
        if (!MyUtils.isEmptyList(mImageUrls)){
            map.put("images", mImageAdapter.getImageUrls());        //房源图片
        }
        String[] tempStrTag = new String[tagList.size()];
        for (int i = 0; i < tagList.size(); i++){
            tempStrTag[i] = tagList.get(i);
        }
//        map.put("label", tempStrTag);         //标签
        map.put("label", tagAdapter.getmCheckedList());
//        map.put("area", Double.valueOf(mEtHouseArea.getText().toString()));         //面积
        map.put("buildName", mEtBuilding.getText().toString());              //楼栋
        map.put("houseNum", mEtRoomNum.getText().toString());       //房号
        map.put("jzlx", mTvHouseType.getText().toString());         //建筑类型
        map.put("zxqk", mTvHouseDecoration.getText().toString());   //装修情况
        map.put("cx", mTvHouseFacing.getText().toString());         //朝向

//        map.put("ptss", mEquipList);

        map.put("remark", mEtHouseDesc.getText().toString());       //描述
//        map.put("huxing", mTvHouseLayout.getText().toString());     //户型
        map.put("ownerId", user.getId());

        map.put("price", mEtHousePrice.getText().toString());       //价格
        String url = null;
        if (house == null) {
//            新发布房源
            url = FinalData.URL_VALUE + (tag == Constants.SELL ? "saleRoom" : "room");
        }else {
//            修改原房源
            url = FinalData.URL_VALUE + (tag == Constants.SELL ? "saleRoomUpdate" : "roomUpdate");
            map.put("id", house.getId());
            map.put("type", 0); //上架状态
        }
        if (tag == Constants.SELL) {     //发布售房需要产权年限和建筑年代数据，价格为总房价，单位为万元
            map.put("cqnx", mEtChanquan.getText().toString().trim());
            map.put("jznd", mEtBuildYear.getText().toString().trim());
        } else {    //租房时需要配套设施
            List<String> checkedList = eAdapter.getmCheckedList();
            if (!MyUtils.isEmptyList(checkedList)) {
                map.put("ptss", checkedList);                           //配套设施
            }
        }
        showLogD("=======StartReleaseRequest========");
        CustomStringRequest request = new CustomStringRequest(Request.Method.POST, url, map,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        L.i("response : " + s);
                        try {
                            org.json.JSONObject object = new JSONObject(s);
                            int code = (int) object.get("code");
                            String msg = (String) object.get("msg");
                            switch (code){
                                case 0:
                                    if (house == null) {
                                        showToast("发布成功");
                                    }else{
                                        showToast("修改成功");
                                    }
                                    setResult(RESULT_OK);
                                    activity.finish();
                                    break;
                                case 5:
                                    showToast("发布的房源在该小区中不存在\n请检查楼栋或房号是否存在");
                                    try {
                                        Thread.sleep(500);
                                        commit.setEnabled(true);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                default:
                                    showToast("发布出错");
                                    try {
                                        Thread.sleep(500);
                                        commit.setEnabled(true);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        showToast(getResources().getString(R.string.house_submit_failed));
                        commit.setEnabled(true);
                    }
                });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void commit() {
        if (StringUtils.isEmpty(mEtHouseTitle.getText().toString())
//                || StringUtils.isEmpty(mTvHouseLayout.getText().toString())
                || StringUtils.isEmpty(mEtHousePrice.getText().toString())
                || StringUtils.isEmpty(mTvHouseType.getText().toString())
                || StringUtils.isEmpty(mEtBuilding.getText().toString())
                || StringUtils.isEmpty(mEtRoomNum.getText().toString())
                || StringUtils.isEmpty(mTvHouseFacing.getText().toString())
                || StringUtils.isEmpty(mTvHouseDecoration.getText().toString())){
            showToast("您有信息没有填完整");
            return ;
        }else if (tag == Constants.SELL && (StringUtils.isEmpty(mEtBuildYear.getText().toString())
                || StringUtils.isEmpty(mEtChanquan.getText().toString()))){
//            售房要填写建筑年代和产权年限
            showToast("您有信息没有填完整");
            return ;
        }

//        sendRequest();


        showLogD("=======SendVerifyCode========");
        String vCode = mEtVerify.getText().toString();
        SMSSDK.submitVerificationCode(Constants.SMSSDK_COUNTRYCODE, mEtPhone.getText().toString().trim(), vCode);
        if (tag == Constants.LEASE) {
            List<String> checkedList = new ArrayList<>();
            for (int i = 0; i < mEquipList.length; i++) {
                if (eAdapter.getItem(i) != null) {
                    checkedList.add((String) eAdapter.getItem(i));
                }
            }
            L.i(checkedList.toString());
        }

    }

    private void sendCode() {
        phone = mEtPhone.getText().toString().trim();
        if (StringUtils.isEmpty(phone)) {
            mEtPhone.setError("请输入手机号码");
            mEtPhone.requestFocus();
            return;
        }
        if (!StringUtils.isMobile(phone)) {
            mEtPhone.setError("手机号码格式不对");
            mEtPhone.requestFocus();
            return;
        }
        SMSSDK.getVerificationCode("86", phone);
        if (mc == null) {
            mc = new MyCount(60000, 1000, mTvGetVerify, this); // 第一参数是总的时间，第二个是间隔时间 都是毫秒为单位
        }
        mc.start();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0){
            intetnMultiImageSelector();
        }
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
                StringBuilder sb = new StringBuilder();


                mImageList.addAll(mSelectPath);
                mGVImageList.setAdapter(mImageAdapter);
                mImageAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onDestroy() {
        SMSSDK.unregisterAllEventHandler();
        super.onDestroy();
    }
}
