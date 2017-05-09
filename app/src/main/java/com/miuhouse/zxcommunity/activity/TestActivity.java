package com.miuhouse.zxcommunity.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.http.FinalData;
import com.miuhouse.zxcommunity.http.okhttp.OkHttpUtils;
import com.miuhouse.zxcommunity.http.okhttp.callback.OkHttpCallback;
import com.miuhouse.zxcommunity.utils.L;
import com.miuhouse.zxcommunity.utils.MyUtils;
import com.miuhouse.zxcommunity.widget.StatusCompat;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
//import com.umeng.message.UmengRegistrar;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by khb on 2015/12/30.
 */
public class TestActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initTitle() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.titlebar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.mipmap.back_white);
        setSupportActionBar(toolbar);
        StatusCompat.compat(this, StatusCompat.COLOR_TITLE);
        TextView title = (TextView) findViewById(R.id.title);
        title.setClickable(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action:

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initView() {
        setContentView(R.layout.test);
    }

    @Override
    public void initData() {

//        String device_token = UmengRegistrar.getRegistrationId(context);
//        showLogD("device_token " + device_token);

        String url = FinalData.URL_VALUE + "bankList";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("page", 1);
        map.put("pageSize", 20);
        map.put("propertyId", 4);
        JSONObject jsonObj = new JSONObject();
        String json = "";
        try {
            // 前四个参数为固定参数
            jsonObj.put(FinalData.PHONE_TYPE, FinalData.PHONE_TYPE_VALUE);
            jsonObj.put(FinalData.IMEI, FinalData.IMEI_VALUE);
            jsonObj.put(FinalData.VERSION_CODE, FinalData.VERSION_CODE_VALUE);
            // 判断是否还有其他参数需发送到服务器
            if (map != null) {
                Set<Map.Entry<String, Object>> set = map.entrySet();
                for (Map.Entry<String, Object> entry : set) {
                    jsonObj.put(entry.getKey(), entry.getValue());
                }
            }

            // 转为json格式String
            json = jsonObj.toJSONString();
        }catch (Exception e){
         e.printStackTrace();
        }
        String md5 = FinalData.PHONE_TYPE_VALUE + FinalData.IMEI_VALUE + FinalData.VERSION_CODE_VALUE + FinalData.APP_KEY_VALUE;
        md5 = MyUtils.md5String(md5);
//        使用okhttp进行网络请求
        /*RequestBody body = new FormEncodingBuilder()
                .add(FinalData.MD5, md5)
                .add(FinalData.TRANSDATA, json)
                .build();
        com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder().url(url).post(body).build();
        OkHttpClient mOKOkHttpClient = new OkHttpClient();
        mOKOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(com.squareup.okhttp.Request request, IOException e) {
                showLogD("===error===");
            }

            @Override
            public void onResponse(com.squareup.okhttp.Response response) throws IOException {
                showLogD("=========="+response.body().string());
            }              =-
        });*/
//      封装过的okhttp框架
        OkHttpUtils.post().url(url)
                .addParam(FinalData.MD5, md5)
                .addParam(FinalData.TRANSDATA, json)
                .build()
                .execute(new OkHttpCallback() {
                    @Override
                    public String parseNetworkResponse(Response response) throws IOException {
                        L.i("parseNetworkResponse " + response.body().string());
                        return response.body().toString();
                    }

                    @Override
                    public void onError(Request request, Exception e) {
                        L.i("onError " + request.toString());
                    }

                    @Override
                    public void onResponse(Object response) {
                        L.i("onResponse " + response.toString());
                    }
                });
    }

    @Override
    public void initVariables() {

    }

    @Override
    public String getTag() {
        return null;
    }


}
