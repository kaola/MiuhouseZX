package com.miuhouse.zxcommunity.http.okhttp.callback;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by khb on 2015/12/31.
 */
public abstract class OkHttpCallback<T> {
//    以下回调在UI线程中执行
    public void onBefore(Request request){
    }

    public void inProgress(float progress){
    }

    public void onAfter(){
    }

//    以下回调在子线程中调用
    public abstract String parseNetworkResponse(Response response) throws IOException;
    public abstract void onError(Request request, Exception e);
    public abstract void onResponse(T response);

    public static OkHttpCallback CALLBACK_DEFAULT = new OkHttpCallback() {
        @Override
        public String parseNetworkResponse(Response response) throws IOException {
            return response.body().toString();
        }

        @Override
        public void onError(Request request, Exception e) {

        }

        @Override
        public void onResponse(Object response) {

        }
    };


}
