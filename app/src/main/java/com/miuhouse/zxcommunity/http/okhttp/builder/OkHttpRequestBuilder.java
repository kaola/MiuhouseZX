package com.miuhouse.zxcommunity.http.okhttp.builder;

import com.miuhouse.zxcommunity.http.okhttp.request.RequestCall;

import java.util.Map;

/**
 * Created by khb on 2015/12/31.
 */
public abstract class OkHttpRequestBuilder {
    protected String url;
    protected  Object tag;
    protected Map<String, String> headers;
    protected Map<String, String> params;

    public abstract OkHttpRequestBuilder url(String url);
    public abstract OkHttpRequestBuilder tag(Object tag);
    public abstract OkHttpRequestBuilder headers(Map<String, String> headers);
    public abstract OkHttpRequestBuilder addHeader(String key, String value);
    public abstract OkHttpRequestBuilder params(Map<String, String> params);
    public abstract OkHttpRequestBuilder addParam(String key, String value);
    public abstract RequestCall build();
}
