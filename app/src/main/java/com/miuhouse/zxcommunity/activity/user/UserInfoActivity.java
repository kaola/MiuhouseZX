package com.miuhouse.zxcommunity.activity.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.activity.BaseActivity;
import com.miuhouse.zxcommunity.activity.LoginActivity;
import com.miuhouse.zxcommunity.activity.PropertyActivity;
import com.miuhouse.zxcommunity.application.MyApplication;
import com.miuhouse.zxcommunity.bean.HeadUrl;
import com.miuhouse.zxcommunity.bean.StatusBean;
import com.miuhouse.zxcommunity.bean.UserBean;
import com.miuhouse.zxcommunity.db.AccountDBTask;
import com.miuhouse.zxcommunity.http.FinalData;
import com.miuhouse.zxcommunity.http.GsonRequest;
import com.miuhouse.zxcommunity.http.VolleySingleton;
import com.miuhouse.zxcommunity.utils.MyAsyn;
import com.miuhouse.zxcommunity.utils.MyUtils;
import com.miuhouse.zxcommunity.utils.StringUtils;
import com.miuhouse.zxcommunity.utils.UpdatePhotoUtils;
import com.miuhouse.zxcommunity.widget.ProgressFragment;
import com.miuhouse.zxcommunity.widget.StatusCompat;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kings on 1/8/2016.
 */
public class UserInfoActivity extends BaseActivity implements View.OnClickListener {
    public static final int TAKE_PICTURE = 16;// 拍照
    public static final int RESULT_LOAD_IMAGE = 17;// 从相册中选择
    public static final int CUT_PHOTO_REQUEST_CODE = 18;

    private static final int NICE_NAME = 0;
    private static final int NAME = 1;
    private static final int PHONE = 2;
    private static final int BUILD = 3;
    private static final int PROPERTY = 4;

    private RelativeLayout relativeNicename;

    private TextView tvPropertyName;

    private TextView tvNicename;

    private TextView tvPhone;

    private TextView tvName;

    private TextView tvBuild;

    private UserBean mUserBean;

    private String intBuild;
    private String intUnit;
    private Uri photoUri;
    private String imagePath;
    //头像上传的地址
    private String strHeadUrl;
    private ImageView imgAvatar;
    private String mID;

    private ProgressFragment progress;

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1 && msg.obj != null) {
                // 显示图片
                //                isBitmap = true;
                imgAvatar.setImageBitmap((Bitmap) msg.obj);
                new MyAsyn(context, getAsynResponse(), imagePath).execute();
            }
        }
    };

    private String propertyName;
    private long propertyID;

    private MyAsyn.AsyncResponse getAsynResponse() {
        return new MyAsyn.AsyncResponse() {

            @Override
            public void processFinish(String result) {

                // TODO Auto-generated method stub
                Gson gson = new Gson();
                HeadUrl headUrl = gson.fromJson(result, HeadUrl.class);
                if (headUrl.getCode() == 0) {
                    strHeadUrl = headUrl.getImages().get(0);
                } else {
                    showToast("头像上传失败");
                }
            }

            @Override
            public void processError() {

            }
        };
    }

    @Override
    public void initTitle() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.titlebar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.mipmap.back_black);
        setSupportActionBar(toolbar);
        StatusCompat.compat(this, StatusCompat.COLOR_DEFAULT);
        TextView title = (TextView) findViewById(R.id.title);
        title.setText("我的资料");
        title.setClickable(true);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_user);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvNicename = (TextView) findViewById(R.id.tv_nicename);
        tvPhone = (TextView) findViewById(R.id.tv_phone);
        tvBuild = (TextView) findViewById(R.id.tv_build);
        imgAvatar = (ImageView) findViewById(R.id.avatar);
        tvPropertyName = (TextView) findViewById(R.id.tv_property);

        initFill();

        findViewById(R.id.relative_nicename).setOnClickListener(this);
        findViewById(R.id.relative_phone).setOnClickListener(this);
        findViewById(R.id.relative_build).setOnClickListener(this);
        findViewById(R.id.relative_name).setOnClickListener(this);
        findViewById(R.id.relative_avatar).setOnClickListener(this);
        findViewById(R.id.relative_property).setOnClickListener(this);
    }

    private void initFill() {
        if (!StringUtils.isEmpty(mUserBean.getMobile())) {

            tvPhone.setText(mUserBean.getMobile());
        }
        if (!StringUtils.isEmpty(mUserBean.getName())) {

            tvName.setText(mUserBean.getName());
        }
        if (mUserBean.getNickName() != null) {

            tvNicename.setText(mUserBean.getNickName());
        }
        if (mUserBean.getHeadUrl() != null) {

            Glide.with(this)
                .load(strHeadUrl)
                .placeholder(R.mipmap.ic_launcher)
                .override(MyUtils.dip2px(this, 50), MyUtils.dip2px(this, 50))
                .into(imgAvatar);
        }
        if (mUserBean.getPropertyName() != null) {
            tvPropertyName.setText(mUserBean.getPropertyName());
        }
        showTextViewBuildNumber(intUnit, intBuild, tvBuild);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initVariables() {
        mUserBean = MyApplication.getInstance().getUserBean();
        if (mUserBean != null) {
            intUnit = mUserBean.getUnit();
            intBuild = mUserBean.getBuild();
            strHeadUrl = mUserBean.getHeadUrl();
            mID = mUserBean.getId();
            propertyID = mUserBean.getPropertyId();

            if (!StringUtils.isEmpty(mUserBean.getPropertyName())) {
                propertyName = mUserBean.getPropertyName();
            }
            intUnit = mUserBean.getUnit();
            intBuild = mUserBean.getBuild();
        } else {

        }
    }

    @Override
    public String getTag() {
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuItem actionItem = menu.findItem(R.id.action);
        actionItem.setVisible(true);
        actionItem.setTitle("提交");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action:
                showProgress();
                sendRequest();
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.relative_nicename:
                startActivityForResult(
                    new Intent(this, UpdateNiceNameActivity.class).putExtra("niceName",
                        tvNicename.getText().toString()), NICE_NAME);
                break;
            case R.id.relative_name:
                startActivityForResult(new Intent(this, UpdateNameActivity.class).putExtra("name",
                    tvName.getText().toString()), NAME);
                break;
            case R.id.relative_build:
                startActivityForResult(
                    new Intent(this, UpdateBuildActivity.class).putExtra("Unit", intUnit)
                        .putExtra("Build", intBuild), BUILD);
                break;
            case R.id.relative_phone:
                startActivityForResult(new Intent(this, UpdatePhoneActivity.class).putExtra("phone",
                    tvPhone.getText().toString()), PHONE);
                break;
            case R.id.relative_avatar:
                //获取头像
                UpdatePhotoUtils.startPhotoZoom(this);
                break;
            case R.id.relative_property:

                startActivityForResult(new Intent(this, PropertyActivity.class), PROPERTY);
                break;
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, final Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == NICE_NAME) {
                String message = data.getStringExtra("message");

                tvNicename.setText(message);
            } else if (requestCode == NAME) {
                String message = data.getStringExtra("message");

                tvName.setText(message);
            } else if (requestCode == PHONE) {
                String message = data.getStringExtra("message");

                tvPhone.setText(message);
            } else if (requestCode == BUILD) {
                String message = data.getStringExtra("message");

                String unit = data.getStringExtra("unit");
                showTextViewBuildNumber(unit, message, tvBuild);
            } else if (requestCode == PROPERTY) {
                propertyName = data.getStringExtra("property");
                propertyID = data.getLongExtra("propertyID", 0);
                Log.i("TAG", "propertyID=" + propertyID);
                tvPropertyName.setText(propertyName);
            }
            if (requestCode == RESULT_LOAD_IMAGE
                || requestCode == TAKE_PICTURE
                || requestCode == CUT_PHOTO_REQUEST_CODE) {

                new Thread() {
                    @Override
                    public void run() {
                        Bitmap bitmap = null;
                        //获取图片路径
                        if (RESULT_LOAD_IMAGE == requestCode) {
                            String imageAddress = null;

                            if (data == null) {
                                return;
                            }
                            Uri selectedImageUri = data.getData();
                            imageAddress = UpdatePhotoUtils.getImageAddress();

                            //图片裁剪
                            imagePath = UpdatePhotoUtils.getImagePath(imageAddress);
                            Log.i("TAG", "imagePath=" + imagePath);
                            UpdatePhotoUtils.startPhotoZoomOne(selectedImageUri,
                                UserInfoActivity.this, imageAddress);
                        } else {
                            if (requestCode == TAKE_PICTURE) {
                                String imageAddress = null;
                                imageAddress = UpdatePhotoUtils.getImageAddress();
                                // 拍摄图片
                                imagePath = UpdatePhotoUtils.getImagePath(imageAddress);
                                UpdatePhotoUtils.startPhotoZoomOne(photoUri, UserInfoActivity.this,
                                    imageAddress);
                            } else if (requestCode == CUT_PHOTO_REQUEST_CODE) {
                                if (bitmap == null && !StringUtils.isEmpty(imagePath)) {
                                    bitmap = MyUtils.getBitmapByPath(imagePath);
                                    Log.i("TAG", "bitmap=" + bitmap);
                                    Message msg = new Message();
                                    msg.what = 1;
                                    msg.obj = bitmap;
                                    handler.sendMessage(msg);
                                    //                                    }
                                }
                            }
                        }
                    }

                    ;
                }.start();
            }
        }
    }

    /**
     * 提交修改信息
     */
    private void sendRequest() {

        String urlPath = FinalData.URL_VALUE + "ownerUpdate";
        Map<String, Object> map = new HashMap<>();
        map.put("name", tvName.getText().toString());
        map.put("nickName", tvNicename.getText().toString());
        Log.i("TAG", "ID=" + mUserBean.getId());
        map.put("id", mID);
        map.put("headUrl", strHeadUrl);
        map.put("mobile", tvPhone.getText().toString());
        map.put("build", intBuild);
        map.put("unit", intUnit);
        map.put("propertyId", propertyID);

        GsonRequest<StatusBean> custom =
            new GsonRequest<>(Request.Method.POST, urlPath, StatusBean.class, map,
                getVerifyListener(), new ErrorCallback());
        VolleySingleton.getInstance(this).addToRequestQueue(custom);
    }

    private Response.Listener<StatusBean> getVerifyListener() {
        return new Response.Listener<StatusBean>() {

            @Override
            public void onResponse(StatusBean baseBean) {
                if (baseBean.getCode() == 0) {
                    UserBean userBean = new UserBean();
                    userBean.setName(tvName.getText().toString());
                    userBean.setNickName(tvNicename.getText().toString());
                    userBean.setId(mUserBean.getId());
                    userBean.setHeadUrl(strHeadUrl);
                    userBean.setMobile(tvPhone.getText().toString());
                    userBean.setBuild(intBuild);
                    userBean.setUnit(intUnit);
                    userBean.setPropertyId(propertyID);
                    userBean.setPropertyName(propertyName);
                    userBean.setStatus(baseBean.getStatus());
                    AccountDBTask.saveUserBean(userBean);
                    MyApplication.getInstance().setmUserBean(userBean);
                }
                progress.dismissAllowingStateLoss();
                UserInfoActivity.this.sendBroadcast(
                    new Intent(LoginActivity.INTENT_ACTION_USER_CHANGE));
                finish();
            }
        };
    }

    private Response.ErrorListener getVerifyErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progress.dismissAllowingStateLoss();
            }
        };
    }

    private void showProgress() {
        progress = ProgressFragment.newInstance();
        progress.show(getSupportFragmentManager(), "user");
    }

    private void showTextViewBuildNumber(String unit, String buildNumber, TextView tvBuild) {
        Log.i("TAG", "unit=" + unit);
        Log.i("TAG", "buildNumber=" + buildNumber);
        if (!StringUtils.isEmpty(buildNumber) && !StringUtils.isEmpty(unit)) {
            intBuild = buildNumber;
            intUnit = unit;
            tvBuild.setText(buildNumber + "栋" + unit + "室");
        } else if (StringUtils.isEmpty(buildNumber) && !StringUtils.isEmpty(unit)) {
            tvBuild.setText(unit + "室");
        } else if (!StringUtils.isEmpty(buildNumber) && StringUtils.isEmpty(unit)) {
            tvBuild.setText(buildNumber + "栋");
        } else {
            tvBuild.setText("");
        }
    }
}
