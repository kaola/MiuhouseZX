package com.miuhouse.zxcommunity.http.okhttp;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.miuhouse.zxcommunity.http.okhttp.builder.OkHttpPostBuilder;
import com.miuhouse.zxcommunity.http.okhttp.callback.OkHttpCallback;
import com.miuhouse.zxcommunity.http.okhttp.request.RequestCall;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * Created by khb on 2015/12/31.
 */
public class OkHttpUtils  {

    public static final long DEFAULT_TIMEOUT = 100000;
    public static final String TAG = "OkHttpUtils";
    private OkHttpClient mOkHttpClient;
    private static OkHttpUtils mInstance;
    private boolean debug;
    private String tag;
    private Handler mHandler;

    private OkHttpUtils(){
        mOkHttpClient = new OkHttpClient();
        mOkHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
        mHandler = new Handler(Looper.getMainLooper());
        if (true){
            mOkHttpClient.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                return true;
                }
            });
        }
    }

    public static OkHttpUtils getInstance(){
        if (mInstance == null){
            synchronized ( OkHttpUtils.class){
                if (mInstance == null){
                    mInstance = new OkHttpUtils();
                }
            }
        }
        return mInstance;
    }

    public OkHttpClient getOkHttpClient(){
        return mOkHttpClient;
    }

    public OkHttpUtils debug(String tag){
        debug = true;
        this.tag = tag;
        return this;
    }

    public static OkHttpPostBuilder post(){
        return new OkHttpPostBuilder();
    }

    public Handler getHandler(){
        return mHandler;
    }

    public void execute(final RequestCall requestCall, OkHttpCallback callback){
        if (debug){
            if (TextUtils.isEmpty(tag)){
                tag = TAG;
            }
            Log.d(tag, "{method:" + requestCall.getRequest().method() + ", detail:" + requestCall.getOkHttpRequest().toString() + "}");
        }
        if (callback == null){
            callback = OkHttpCallback.CALLBACK_DEFAULT;
        }
        final OkHttpCallback finalCallback = callback;
        requestCall.getCall().enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                sendFailResultCallback(request, e, finalCallback);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Log.i("tag", "response code : "+response.code());
                if (response.code() >= 400 && response.code() <= 599) {
                    try{
                        sendFailResultCallback(requestCall.getRequest(), new RuntimeException(response.body().string()), finalCallback);
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                    return;
                }

                try {
                    Object o = finalCallback.parseNetworkResponse(response);
                    sendSuccessResultCallback(o, finalCallback);
                } catch (IOException e)
                {
                    sendFailResultCallback(response.request(), e, finalCallback);
                }
            }
        });
    }

    public void sendFailResultCallback(final Request request, final Exception e, final OkHttpCallback callback){
        if (callback == null){
            return ;
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(request, e);
                callback.onAfter();
            }
        });
    }

    public void sendSuccessResultCallback(final Object response, final OkHttpCallback callback){
        if (callback == null){
            return ;
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onResponse(response);
                callback.onAfter();
            }
        });
    }

}
