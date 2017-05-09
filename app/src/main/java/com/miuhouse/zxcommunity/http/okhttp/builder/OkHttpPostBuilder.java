package com.miuhouse.zxcommunity.http.okhttp.builder;

import com.miuhouse.zxcommunity.http.okhttp.request.OkHttpPostRequest;
import com.miuhouse.zxcommunity.http.okhttp.request.RequestCall;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Created by khb on 2015/12/31.
 */
public class OkHttpPostBuilder extends OkHttpRequestBuilder {

    @Override
    public OkHttpRequestBuilder url(String url) {
        this.url = url;
        return this;
    }

    @Override
    public OkHttpRequestBuilder tag(Object tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public OkHttpRequestBuilder headers(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    @Override
    public OkHttpRequestBuilder addHeader(String key, String value) {
        if (this.headers == null){
            headers = new IdentityHashMap<>();
        }
        headers.put(key, value);
        return this;
    }

    @Override
    public OkHttpRequestBuilder params(Map<String, String> params) {
        this.params = params;
        return this;
    }

    @Override
    public OkHttpRequestBuilder addParam(String key, String value) {
        if (this.params == null){
            params = new IdentityHashMap<>();
        }
        params.put(key, value);
        return this;
    }

    @Override
    public RequestCall build() {
        return new OkHttpPostRequest(url, tag, headers, params).build();
    }
}
