package com.miuhouse.zxcommunity.manager;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.miuhouse.zxcommunity.http.CustomStringRequest;
import com.miuhouse.zxcommunity.http.FinalData;
import com.miuhouse.zxcommunity.http.VolleySingleton;
import com.miuhouse.zxcommunity.utils.Constants;
import com.miuhouse.zxcommunity.utils.L;
import com.miuhouse.zxcommunity.utils.SPUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by khb on 2016/3/31.
 */
public class DictManager {

    private static VolleySingleton instance;

    private DictManager() {
    }

    private static class DictManagerBuilder {
        private static DictManager instance = new DictManager();
    }

    public static DictManager getInstance(Context context){
        instance = VolleySingleton.getInstance(context);
        return DictManagerBuilder.instance;
    }

    private String[] types = new String[]{
            "zxqk", //装修情况
            "cx",   //朝向
            "ptss", //配套设施
            "esflabel", //售房标签
            "zflabel",  //租房标签
            "jzlx", //建筑类型
            "esfPrice", //售房价格
            "floorAge", //楼龄
            "zfPrice",  //租房价格
            "fx"   //房型
    };

    public void init(){
        String url = FinalData.URL_VALUE + "getDictListByTypes";
        Map<String, Object> map = new HashMap<>();
        map.put("types", Arrays.asList(types));
        CustomStringRequest request = new CustomStringRequest(Request.Method.POST, url, map,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            JSONObject jsonDicts = jsonObject.getJSONObject("dicts");
                            if(jsonObject.getInt("code") == 0){
                                JSONArray zxqk = jsonDicts.getJSONArray(Constants.ZXQK);
                                SPUtils.saveSPData(Constants.ZXQK, zxqk.toString());

                                JSONArray cx = jsonDicts.getJSONArray(Constants.CX);
                                SPUtils.saveSPData(Constants.CX, cx.toString());

                                JSONArray ptss = jsonDicts.getJSONArray(Constants.PTSS);
                                SPUtils.saveSPData(Constants.PTSS, ptss.toString());

                                JSONArray esflabel = jsonDicts.getJSONArray(Constants.ESFLABEL);
                                SPUtils.saveSPData(Constants.ESFLABEL,esflabel.toString());

                                JSONArray zflabel = jsonDicts.getJSONArray(Constants.ZFLABEL);
                                SPUtils.saveSPData(Constants.ZFLABEL,zflabel.toString());

                                JSONArray jzlx = jsonDicts.getJSONArray(Constants.JZLX);
                                SPUtils.saveSPData(Constants.JZLX, jzlx.toString());

                                JSONArray esfprice = jsonDicts.getJSONArray(Constants.ESFPRICE);
                                SPUtils.saveSPData(Constants.ESFPRICE, esfprice.toString());

                                JSONArray floorage = jsonDicts.getJSONArray(Constants.FLOORAGE);
                                SPUtils.saveSPData(Constants.FLOORAGE, floorage.toString());

                                JSONArray zfprice = jsonDicts.getJSONArray(Constants.ZFPRICE);
                                SPUtils.saveSPData(Constants.ZFPRICE, zfprice.toString());

                                JSONArray fx = jsonDicts.getJSONArray(Constants.FX);
                                SPUtils.saveSPData(Constants.FX, fx.toString());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        L.e("初始化房源字典数据失败");
                    }
                });
        instance.addToRequestQueue(request);
    }



}
