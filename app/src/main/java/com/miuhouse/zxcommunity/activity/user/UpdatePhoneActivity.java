package com.miuhouse.zxcommunity.activity.user;

import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.activity.BaseActivity;
import com.miuhouse.zxcommunity.widget.StatusCompat;

/**
 * Created by kings on 1/8/2016.
 */
public class UpdatePhoneActivity extends BaseActivity {
    private EditText etPhone;
    private String phone;

    @Override
    public void initTitle() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.titlebar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.mipmap.back_black);
        setSupportActionBar(toolbar);
        StatusCompat.compat(this, StatusCompat.COLOR_DEFAULT);
        TextView title = (TextView) findViewById(R.id.title);
        title.setText("电话号码");
        title.setTextColor(Color.parseColor("#1E2129"));
        title.setClickable(true);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_update_nicename);
        etPhone = (EditText) findViewById(R.id.et_nicename);
        etPhone.setRawInputType(InputType.TYPE_CLASS_PHONE);
        etPhone.setText(phone);
        etPhone.setSelection(phone.length());
    }

    @Override
    public void initData() {
        phone = getIntent().getStringExtra("phone");

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

    private void finishActivity() {
        setResult(RESULT_OK, getIntent().putExtra("message", etPhone.getText().toString().trim()));
        finish();
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
}
