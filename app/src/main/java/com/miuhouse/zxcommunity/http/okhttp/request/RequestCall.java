package com.miuhouse.zxcommunity.http.okhttp.request;

import com.miuhouse.zxcommunity.http.okhttp.OkHttpUtils;
import com.miuhouse.zxcommunity.http.okhttp.callback.OkHttpCallback;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

/**
 * Created by khb on 2015/12/31.
 */
public class RequestCall {

    private OkHttpRequest okHttpRequest;
    private long readTimeOut;
    private long writeTimeOut;
    private long connTimeOut;
    private Request request;
    private Call call;
    /**
     * OkHttpClient的一个浅拷贝
     */
    private OkHttpClient cloneClient;

    public RequestCall(OkHttpRequest request){
        this.okHttpRequest = request;
    }

    public RequestCall setReadTimeOut(long readTimeOut) {
        this.readTimeOut = readTimeOut;
        return this;
    }

    public RequestCall setWriteTimeOut(long writeTimeOut) {
        this.writeTimeOut = writeTimeOut;
        return this;
    }

    public RequestCall setConnTimeOut(long connTimeOut) {
        this.connTimeOut = connTimeOut;
        return this;
    }

    public Call generateCall(OkHttpCallback callBack){
        request = okHttpRequest.generateRequest(callBack);
        if (readTimeOut > 0 || writeTimeOut > 0 || connTimeOut > 0) {
//            如果有设置超时，就使用OkHttpClient的浅拷贝
//            至于为啥我也说不清
            cloneClient = OkHttpUtils.getInstance().getOkHttpClient().clone();
            readTimeOut = readTimeOut > 0 ? readTimeOut : OkHttpUtils.DEFAULT_TIMEOUT;
            writeTimeOut = writeTimeOut > 0 ? writeTimeOut : OkHttpUtils.DEFAULT_TIMEOUT;
            connTimeOut = connTimeOut > 0 ? connTimeOut : OkHttpUtils.DEFAULT_TIMEOUT;
            call = cloneClient.newCall(request);
        }else {
            call = OkHttpUtils.getInstance().getOkHttpClient().newCall(request);
        }
        return call;
    }

    public void execute(OkHttpCallback callback){
        generateCall(callback);
        if (callback != null){
            callback.onBefore(request);
        }
        OkHttpUtils.getInstance().execute(this, callback);
    }

    public Request getRequest(){
        return request;
    }

    public OkHttpRequest getOkHttpRequest(){
        return okHttpRequest;
    }

    public Call getCall(){
        return call;
    }

}
