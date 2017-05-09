package com.miuhouse.zxcommunity.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.activity.user.UserInfoActivity;
import com.miuhouse.zxcommunity.application.MyApplication;
import com.miuhouse.zxcommunity.bean.BaseBean;
import com.miuhouse.zxcommunity.bean.UserBean;
import com.miuhouse.zxcommunity.http.FinalData;
import com.miuhouse.zxcommunity.http.GsonRequest;
import com.miuhouse.zxcommunity.http.VolleySingleton;
import com.miuhouse.zxcommunity.slidedatetimepicker.SlideDateTimeListener;
import com.miuhouse.zxcommunity.slidedatetimepicker.SlideDateTimePicker;
import com.miuhouse.zxcommunity.utils.MyUtils;
import com.miuhouse.zxcommunity.utils.StringUtils;
import com.miuhouse.zxcommunity.utils.ToastUtils;
import com.miuhouse.zxcommunity.widget.NotCheckedPopupWindow;
import com.miuhouse.zxcommunity.widget.PassportDialogBuilder;
import com.miuhouse.zxcommunity.widget.StatusCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 访客通行
 * Created by kings on 3/10/2016.
 */
public class PassportActivity extends BaseActivity {
    private static final String TAG = "PassportActivity";
    private static final int NOT_DRIVE_CAR = 0;
    private static final int DRIVE_CAR = 1;
    // 业主姓名
    private String ownerName;
    //业主电话号码
    private String ownerTelephone;
    //业主楼栋号
    private String ownerBuild;

    // 访问时间
    private Date visitorDate;
    //小区名字
    private String ownerProperty;
    //生产通行证
    private Button btnSubmitPassport;
    private TextView tvDate;
    //业主姓名
    private TextView tvName;
    //业主电话号码
    private TextView tvPhone;
    //楼栋号
    private TextView tvBuildNumber;
    //女士
    private TextView tvOptionLift;
    //男士
    private TextView tvOptionRight;
    //访客名字
    private EditText etVisitiorName;
    //访客数量
    private EditText etVisitiorCount;
    //用户id
    private String ownerId;
    //小区id
    private long propertyID;


    @Override
    public void initTitle() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.titlebar);
        mToolbar.setNavigationIcon(R.mipmap.back_black);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        StatusCompat.compat(this, StatusCompat.COLOR_DEFAULT);
        TextView title = (TextView) findViewById(R.id.title);
        title.setText("访客通行");
        title.setTextColor(Color.parseColor("#1E2129"));
        title.setClickable(true);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_passport);
        btnSubmitPassport = (Button) findViewById(R.id.btn_submit_passport);
        tvDate = (TextView) findViewById(R.id.tv_date);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvPhone = (TextView) findViewById(R.id.tv_phone);
        tvBuildNumber = (TextView) findViewById(R.id.tv_house);
        tvOptionLift = (TextView) findViewById(R.id.tv_option_left);
        tvOptionRight = (TextView) findViewById(R.id.tv_option_right);
        etVisitiorName = (EditText) findViewById(R.id.et_visitor_name);
        etVisitiorCount = (EditText) findViewById(R.id.et_visitor_count);
        tvOptionLift.setSelected(true);
        tvOptionRight.setSelected(false);
        tvOptionRight.setTextColor(getResources().getColor(R.color.text_black_57));

        btnSubmitPassport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPassCard();
            }
        });
        findViewById(R.id.frame_passport_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SlideDateTimePicker.Builder(getSupportFragmentManager())
                        .setListener(listener)
                        .setInitialDate(new Date())
                        .build()
                        .show();
            }
        });
        tvOptionLift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvOptionLift.setSelected(true);
                tvOptionRight.setSelected(false);
                tvOptionRight.setTextColor(getResources().getColor(R.color.text_black_57));
                tvOptionLift.setTextColor(getResources().getColor(R.color.white));
            }
        });
        tvOptionRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvOptionLift.setSelected(false);
                tvOptionRight.setSelected(true);
                tvOptionRight.setTextColor(getResources().getColor(R.color.white));
                tvOptionLift.setTextColor(getResources().getColor(R.color.text_black_57));
            }
        });
        tvName.setText(ownerName);
        tvPhone.setText(ownerTelephone);
        tvBuildNumber.setText(ownerBuild);
    }


    /**
     * 生成通行证
     */
    private void requestPassCard() {
        final String urlPath = FinalData.URL_VALUE + "passCard";
        Map<String, Object> map = new HashMap<>();
        if (ownerId == null) {
            startActivity(LoginActivity.class);
        }
        if (StringUtils.isEmpty(getEtNameValue())) {
            etVisitiorName.setError("请填写访客名字");
            etVisitiorName.requestFocus();
            return;
        }
        if (getVisitiorCount() <= 0) {
            etVisitiorCount.setError("请填写访客人数");
            etVisitiorCount.requestFocus();
            return;
        }
        if (visitorDate == null) {
            ToastUtils.showToast(this, "请填写访问时间");
            return;
        }
        map.put("ownerId", ownerId);
        map.put("ownerName", ownerName);
        map.put("mobile", ownerTelephone);
        map.put("louDong", ownerBuild);
        map.put("visitorName", ownerBuild);
        map.put("louDong", ownerBuild);
        map.put("visitorName", getEtNameValue());
        map.put("visitorSex", getVisitiorGender());
        map.put("isDrive", getIsDriveCar());
        map.put("number", getEtVisitiorCount());
        map.put("visitTime", visitorDate);
        map.put("propertyId", propertyID);
        GsonRequest<BaseBean> request = new GsonRequest<>(Request.Method.POST, urlPath, BaseBean.class, map, new Response.Listener<BaseBean>() {
            @Override
            public void onResponse(BaseBean baseBean) {
                if (baseBean.getCode() == 0) {
                    Bundle bundle = new Bundle();
                    bundle.putString("name", getEtNameValue());
                    bundle.putString("louDong", ownerBuild);
                    bundle.putString("propertyName", ownerProperty);
                    bundle.putString("time", tvDate.getText().toString());
                    startActivity(PassportCardActivity.class, bundle);

                }

            }
        }, new ErrorCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
            }
        });
        request.setTag(TAG);
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initVariables() {
        UserBean mUserBean = MyApplication.getInstance().getUserBean();
        ownerId = mUserBean.getId();
        ownerProperty = mUserBean.getPropertyName();
        ownerBuild = mUserBean.getUnit();
        ownerName = mUserBean.getName();
        ownerTelephone = mUserBean.getMobile();
        propertyID = mUserBean.getPropertyId();
        int ownerStatus = mUserBean.getStatus();
        PassportDialogBuilder.isShowDialog(ownerStatus,this);

    }

    @Override
    public String getTag() {
        return TAG;
    }

    private SlideDateTimeListener listener = new SlideDateTimeListener() {
        SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd");

        @Override
        public void onDateTimeSet(Date date) {
            visitorDate = date;
            tvDate.setText(mFormatter.format(date));
        }

        // Optional cancel listener
        @Override
        public void onDateTimeCancel() {

        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        if (item.getItemId() == R.id.action) {
            startActivity(HistoryPassportActivity.class);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuItem actionItem = menu.findItem(R.id.action);
        actionItem.setVisible(true);
        actionItem.setTitle("历史");
        return true;
    }

    /**
     * 获取访客名字
     *
     * @return
     */
    private String getEtNameValue() {

        return etVisitiorName.getText().toString().trim();
    }

    /**
     * 获取用户输入的访客数量
     *
     * @return
     */
    private String getEtVisitiorCount() {
        return etVisitiorCount.getText().toString().trim();
    }

    private int getVisitiorCount() {
        return getEtVisitiorCount() != null && !StringUtils.isEmpty(getEtVisitiorCount()) ? Integer.parseInt(getEtVisitiorCount()) : 0;
    }

    /**
     * 获取访客性别
     *
     * @return
     */
    private String getVisitiorGender() {
        if (tvOptionLift.isSelected()) {
            return tvOptionLift.getText().toString();
        } else {
            return tvOptionRight.getText().toString();
        }
    }

    /**
     * 访客是否驾驶车
     *
     * @return
     */
    private int getIsDriveCar() {
        if (findViewById(R.id.checkBox).isSelected()) {
            return DRIVE_CAR;
        } else {
            return NOT_DRIVE_CAR;
        }
    }

    /**
     * 获取访问时间
     *
     * @return
     */
    private String getVisitiorDate() {
        return tvDate.getText().toString();
    }

    private void showPopupWindow() {

        NotCheckedPopupWindow popup = new NotCheckedPopupWindow(this, findViewById(R.id.linear));
        popup.setOnCancelClickListener(new NotCheckedPopupWindow.OnCancelClickListener() {
            @Override
            public void onCancel() {
                setResult(RESULT_OK);
                finish();
            }
        });
        popup.setOnMainClickListener(new NotCheckedPopupWindow.OnMainClickListener() {
            @Override
            public void onClick() {
                startActivity(UserInfoActivity.class);
                finish();
            }
        });
        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                MyUtils.backgroundAlpha(activity, 1f);
            }
        });
        popup.show();
    }
}
