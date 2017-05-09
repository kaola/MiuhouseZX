package com.miuhouse.zxcommunity.http;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.miuhouse.zxcommunity.utils.MyUtils;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 自定义Volley的request 返回String
 * 
 * @author Administrator
 * 
 */
public class CustomStringRequest extends Request<String> {
    private Map<String, String> params;
    private Response.Listener<String> listener;

    public CustomStringRequest(int method, String url, Map<String, Object> map, Response.Listener<String> listener, ErrorListener errorListener) {
	super(method, url, errorListener);
	// 
	JSONObject jsonObj = new JSONObject();
	String json = "";
	try {
	    // 前四个参数为固定参数
	    jsonObj.put(FinalData.PHONE_TYPE, FinalData.PHONE_TYPE_VALUE);
	    jsonObj.put(FinalData.IMEI, FinalData.IMEI_VALUE);
	    jsonObj.put(FinalData.VERSION_CODE, FinalData.VERSION_CODE_VALUE);
	    // 判断是否还有其他参数需发送到服务器
	    if (map != null) {
		Set<Entry<String, Object>> set = map.entrySet();
		for (Entry<String, Object> entry : set) {
			Log.i("TAG","CustomStringRequest");

			jsonObj.put(entry.getKey(), entry.getValue());
		}
	    }
	    // 转为json格式String
	    json = jsonObj.toJSONString();
	    Log.i("TAG", "json=" + json);
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
	this.params = params;
	this.listener = listener;
    }

    /**
     * 设置Http头文件
     */
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
	
	return super.getHeaders();
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
	
	String parsed;
	try {
	    parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
	} catch (UnsupportedEncodingException e) {
	    parsed = new String(response.data);
	}
	return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(String response) {
	
	listener.onResponse(response);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
	
	return params;
    }
}
