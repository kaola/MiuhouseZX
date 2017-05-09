package com.miuhouse.zxcommunity.activity;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.utils.FileUtils;
import com.miuhouse.zxcommunity.widget.RoundLinearLayout;
import com.miuhouse.zxcommunity.widget.StatusCompat;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

/**
 * Created by kings on 3/10/2016.
 */
public class PassportCardActivity extends BaseActivity {

    private String propertyName;
    private String buliding;
    private String time;
    private String name;
    private RoundLinearLayout roundlinearPassportCard;
    private TextView tvName;
    private TextView tvDescription;
    private TextView tvPropertyName;
    private TextView btnShareWeixin;
    private String fileName;

    @Override
    public void initTitle() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.titlebar);
        mToolbar.setNavigationIcon(R.mipmap.back_black);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        StatusCompat.compat(this, StatusCompat.COLOR_DEFAULT);
        TextView title = (TextView) findViewById(R.id.title);
        title.setText("通行证");
        title.setTextColor(Color.parseColor("#1E2129"));
        title.setClickable(true);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_passport_image);
        roundlinearPassportCard = (RoundLinearLayout) findViewById(R.id.roundlinear_passport_card);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvDescription = (TextView) findViewById(R.id.tv_description);
        tvPropertyName = (TextView) findViewById(R.id.tv_property_name);
        btnShareWeixin = (TextView) findViewById(R.id.btn_share_weixin);
        tvName.setText("尊敬的" + name + "先生");
        tvDescription.setText("您的访客通行证已申请通过，请在" + time + "凭此通行证向门岗申请放行。");
        tvPropertyName.setText(propertyName + " " + buliding);
        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileName = FileUtils.save("card" + System.currentTimeMillis(), roundlinearPassportCard).getName();

            }
        });
        btnShareWeixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileName = FileUtils.save("card" + System.currentTimeMillis(), roundlinearPassportCard).getPath();

                shareImage(fileName);
            }
        });
    }

    private void shareImage(final String fileName) {
        UMImage image = new UMImage(this, BitmapFactory.decodeFile(fileName));
        new ShareAction(this)
                .setPlatform(SHARE_MEDIA.WEIXIN)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onResult(SHARE_MEDIA platform) {
                        Toast.makeText(context, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(SHARE_MEDIA platform, Throwable t) {
                        Toast.makeText(context, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA platform) {
                        Toast.makeText(context, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
                    }
                })
//                .withText("hello umeng video")
//                .withTargetUrl("http://www.baidu.com")
                .withMedia(image)
                .share();
    }

    @Override
    public void initData() {

    }

    @Override
    public void initVariables() {
        propertyName = getIntent().getStringExtra("propertyName");
        buliding = getIntent().getStringExtra("louDong");
        time = getIntent().getStringExtra("time");
        name = getIntent().getStringExtra("name");
    }

    @Override
    public String getTag() {
        return null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
