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
 * 楼栋 Created by kings on 1/8/2016.
 */
public class UpdateBuildActivity extends BaseActivity {

    private EditText etBuild;
    private EditText etUnit;
    private String build;
    private String unit;

    @Override
    public void initTitle() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.titlebar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.mipmap.back_black);
        setSupportActionBar(toolbar);
        StatusCompat.compat(this, StatusCompat.COLOR_DEFAULT);
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText("楼栋");
        title.setTextColor(Color.parseColor("#1E2129"));
        title.setClickable(true);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_update_build);
        etBuild = (EditText) findViewById(R.id.et_build);
        etUnit = (EditText) findViewById(R.id.etunit);
        if (build != null) {
            etBuild.setText(build);
            etBuild.setSelection(build.length());
        }
        if (unit != null) {
            etUnit.setText(unit);
            etUnit.setSelection(unit.length());
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void initVariables() {
        build = getIntent().getStringExtra("Build");
        unit = getIntent().getStringExtra("Unit");
    }

    @Override
    public String getTag() {
        return null;
    }

    @Override
    public void onBackPressed() {
        finishActivity();
    }

    /**
     * 对用户按home icon进行处理
     */
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
        setResult(RESULT_OK, getIntent().putExtra("message", etBuild.getText().toString())
            .putExtra("unit", etUnit.getText().toString()));
        finish();
    }
}
