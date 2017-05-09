package com.miuhouse.zxcommunity.http.okhttp.request;

import com.miuhouse.zxcommunity.http.okhttp.callback.OkHttpCallback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.util.Map;

/**
 * Created by khb on 2015/12/31.
 */
public abstract class OkHttpRequest {

    protected String url;
    protected Object tag;
    protected Map<String, String> headers;
    protected Map<String, String> params;
    protected  Request.Builder builder = new Request.Builder();

    protected OkHttpRequest(String url, Object tag, Map<String, String> headers, Map<String, String> params){
        this.url = url;
        this.tag = tag;
        this.headers = headers;
        this.params = params;

        if (url == null){
//            Excepti
        }
    }

    protected abstract RequestBody buildRequestBody();
    protected abstract Request buildRequest(Request.Builder builder, RequestBody requestBody);
    protected RequestBody wrapRequestBody(RequestBody requestBody, final OkHttpCallback callback){
        return requestBody;
    }
    public Request generateRequest(OkHttpCallback callback){
        RequestBody requestBody = wrapRequestBody(buildRequestBody(), callback);
        prepareBuilder();
        return buildRequest(builder, requestBody);
    }
    private void prepareBuilder(){
        builder.url(url).tag(tag);
        appendHeaders();
    }

    protected void appendHeaders(){
        Headers.Builder headerBuilder = new Headers.Builder();
        if (headers == null || headers.isEmpty()){
            return ;
        }
        for (String key : headers.keySet()){
            headerBuilder.add(key, headers.get(key));
        }
        builder.headers(headerBuilder.build());
    }

    public RequestCall build(){
        return new RequestCall(this);
    }

}
