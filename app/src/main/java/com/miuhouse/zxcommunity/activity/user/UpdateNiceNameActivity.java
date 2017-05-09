package com.miuhouse.zxcommunity.activity.user;

import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.activity.BaseActivity;
import com.miuhouse.zxcommunity.widget.StatusCompat;

/**
 * 修改用户昵称
 * Created by kings on 1/8/2016.
 */
public class UpdateNiceNameActivity extends BaseActivity {
    private EditText etNiceName;
    private String niceName;

    @Override
    public void initTitle() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.titlebar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.mipmap.back_black);
        setSupportActionBar(toolbar);
        StatusCompat.compat(this, StatusCompat.COLOR_DEFAULT);
        TextView tvTitle = (TextView) findViewById(R.id.title);
        tvTitle.setText("昵称");
        tvTitle.setTextColor(Color.parseColor("#1E2129"));
        tvTitle.setClickable(true);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_update_nicename);
        etNiceName = (EditText) findViewById(R.id.et_nicename);
        etNiceName.setText(niceName);
        etNiceName.setSelection(niceName.length());
    }

    @Override
    public void initData() {
        niceName = getIntent().getStringExtra("niceName");
    }

    @Override
    public void initVariables() {

    }

    @Override
    public String getTag() {
        return null;
    }


    @Override
    public void onBackPressed() {
        finishActivity();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finishActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void finishActivity() {
        setResult(RESULT_OK, getIntent().putExtra("message", etNiceName.getText().toString().trim()));
        finish();
    }
}
