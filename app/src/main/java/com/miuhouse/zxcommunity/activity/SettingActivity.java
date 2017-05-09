package com.miuhouse.zxcommunity.activity;

import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.application.MyApplication;
import com.miuhouse.zxcommunity.widget.StatusCompat;


/**
 * Created by kings on 2/1/2016.
 */
public class SettingActivity extends BaseActivity {
    @Override
    public void initTitle() {

        Toolbar mToolbar = (Toolbar) findViewById(R.id.titlebar);
        mToolbar.setNavigationIcon(R.mipmap.back_black);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        StatusCompat.compat(this, StatusCompat.COLOR_DEFAULT);
        TextView title = (TextView) findViewById(R.id.title);
        title.setText("设置");
        title.setTextColor(Color.parseColor("#1E2129"));
        title.setClickable(true);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_setting);
        findViewById(R.id.relative_about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(AboutActivity.class);
            }
        });
        findViewById(R.id.btn_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.getInstance().logout();
                logout();
                finish();
            }
        });

    }

    private void logout() {
        EMClient.getInstance().logout(false, new EMCallBack() {
            @Override
            public void onSuccess() {
                showLogD("HX-- last account logged out");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(int i, String s) {
                showLogD("HX-- last account logging out failed");
                showToast(getResources().getString(R.string.logout_error));
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void initVariables() {

    }

    @Override
    public String getTag() {
        return null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
