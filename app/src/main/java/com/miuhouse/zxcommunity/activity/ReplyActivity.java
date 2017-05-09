package com.miuhouse.zxcommunity.activity;

import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.db.AccountDBTask;
import com.miuhouse.zxcommunity.http.CustomStringRequest;
import com.miuhouse.zxcommunity.http.FinalData;
import com.miuhouse.zxcommunity.http.VolleySingleton;
import com.miuhouse.zxcommunity.widget.StatusCompat;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by khb on 2016/12/22.
 */
public class ReplyActivity extends BaseActivity {

    private TextView content;
    private String id;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initTitle() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.titlebar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.mipmap.back_black);
        setSupportActionBar(toolbar);
        StatusCompat.compat(this, StatusCompat.COLOR_DEFAULT);
        TextView title = (TextView) findViewById(R.id.title);
        title.setText("");
        title.setTextColor(Color.parseColor("#1E2129"));
        title.setClickable(true);
        TextView action = (TextView) findViewById(R.id.action);
        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commit();
            }
        });
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_reply);
        content = (TextView) findViewById(R.id.content);
    }

    @Override
    public void initData() {
        id = getIntent().getStringExtra("id");
    }

    @Override
    public void initVariables() {

    }

    @Override
    public String getTag() {
        return null;
    }

    private void commit(){
        String url = FinalData.URL_VALUE + "repairReply";
        Map<String, Object> map = new HashMap<>();
        map.put("repairComplainId", id);
        map.put("ownerId", AccountDBTask.getUserBean().getId());
        map.put("content", content.getText().toString());
        CustomStringRequest req = new CustomStringRequest(Request.Method.POST, url, map,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        setResult(RESULT_OK);

                        finish();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        showToast("提交失败");
                        finish();
                    }
                });
        VolleySingleton.getInstance(this).addToRequestQueue(req);
    }
}
