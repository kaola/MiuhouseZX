package com.miuhouse.zxcommunity.http;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.miuhouse.zxcommunity.utils.MyUtils;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;

/**
 * 自定义Volley request 通过json解析返回一个对象
 *
 * @param <T>
 * @author Administrator
 */
public class GsonRequest<T> extends Request<T> {
    //服务器和本地时间差
    public static long deltaBetweenServerAndClientTime;
    private final Class<T> clazz;
    private final Map<String, String> params;
    private final Listener<T> listener;

    /**
     * Make a GET request and return a parsed object from JSON.
     *
     * @param url     URL of the request to make
     * @param clazz   Relevant class object, for Gson's reflection
     * @param headers Map of request headers
     */
    public GsonRequest(int httpMethod, String url, Class<T> clazz, Map<String, Object> headers, Listener<T> listener, ErrorListener errorListener) {
        super(httpMethod, url, errorListener);
        JSONObject jsonObj = new JSONObject();
        String json = "";
        try {
            // 前四个参数为固定参数
            jsonObj.put(FinalData.PHONE_TYPE, FinalData.PHONE_TYPE_VALUE);
            jsonObj.put(FinalData.IMEI, FinalData.IMEI_VALUE);
            jsonObj.put(FinalData.VERSION_CODE, FinalData.VERSION_CODE_VALUE);
            // 判断是否还有其他参数需发送到服务器
            if (headers != null) {
                Set<Entry<String, Object>> set = headers.entrySet();
                for (Entry<String, Object> entry : set) {
                    jsonObj.put(entry.getKey(), entry.getValue());
                }
            }

            // 转为json格式String
            json = jsonObj.toJSONString();
            Log.i("TAG", "原始json=" + json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 拼接请求地址
        // reqParams;
        String md5 = FinalData.PHONE_TYPE_VALUE + FinalData.IMEI_VALUE + FinalData.VERSION_CODE_VALUE + FinalData.APP_KEY_VALUE;
        md5 = MyUtils.md5String(md5);
        // 请求数据
        Map<String, String> params = new HashMap<String, String>();

        params.put(FinalData.MD5, md5);
        params.put(FinalData.TRANSDATA, json);

        this.clazz = clazz;
        this.params = params;
        this.listener = listener;
    }



    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Charset", "UTF-8");
        headers.put("Accept-Encoding", "gzip,deflate");
        return headers;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {

        return params;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        long date = HttpHeaderParser.parseCacheHeaders(response).serverDate;
        Log.i("TAG", "DATE=" + date);
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
        deltaBetweenServerAndClientTime = date - System.currentTimeMillis();
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            Log.i("TAG", "返回json=" + json);
            Gson gson = new Gson();
            return Response.success(gson.fromJson(json, clazz), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

}
