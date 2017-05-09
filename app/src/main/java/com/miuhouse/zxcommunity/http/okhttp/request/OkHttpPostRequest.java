package com.miuhouse.zxcommunity.http.okhttp.request;

import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.util.Map;

/**
 * Created by khb on 2016/1/5.
 */
public class OkHttpPostRequest extends OkHttpRequest {
    public OkHttpPostRequest(String url, Object tag, Map<String, String> headers, Map<String, String> params) {
        super(url, tag, headers, params);
    }

    @Override
    protected RequestBody buildRequestBody() {
        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
        addParams(builder);
        return builder.build();
    }

    @Override
    protected Request buildRequest(Request.Builder build, RequestBody requestBody) {
        return builder.post(requestBody).build();
    }

    private void addParams(MultipartBuilder builder) {
        if (params != null && !params.isEmpty()){
            for (String key : params.keySet()){
                builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
                        RequestBody.create(null, params.get(key)));
            }
        }
    }

}
