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
 * 修改姓名
 * Created by kings on 1/8/2016.
 */
public class UpdateNameActivity extends BaseActivity {
    private EditText etName;
    private String name;

    @Override
    public void initTitle() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.titlebar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.mipmap.back_black);
        setSupportActionBar(toolbar);
        StatusCompat.compat(this, StatusCompat.COLOR_DEFAULT);
        TextView tvTitle = (TextView) findViewById(R.id.title);
        tvTitle.setText("姓名");
        tvTitle.setTextColor(Color.parseColor("#1E2129"));
        tvTitle.setClickable(true);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_update_nicename);
        etName = (EditText) findViewById(R.id.et_nicename);
        etName.setText(name);
        etName.setSelection(name.length());
    }

    @Override
    public void initData() {
        name = getIntent().getStringExtra("name");
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
        switch (item.getItemId()) {
            case android.R.id.home:
                finishActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finishActivity();
    }

    private void finishActivity() {
        setResult(RESULT_OK, getIntent().putExtra("message", etName.getText().toString().trim()));
        finish();
    }
}
