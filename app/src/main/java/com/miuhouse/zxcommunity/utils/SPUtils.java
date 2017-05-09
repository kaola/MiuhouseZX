package com.miuhouse.zxcommunity.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.miuhouse.zxcommunity.application.MyApplication;

import org.json.JSONException;

import java.util.List;


/**
 * Created by khb on 2015/8/20.
 */
public class SPUtils {
    private static String CONFIG = "config";
    private static SharedPreferences sharedPreferences;

    //写入
    public static void saveSPData(String key, String value) {
        Context context = MyApplication.getInstance();
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        }
        sharedPreferences.edit().putString(key, value).commit();
    }

    //读出
    public static String getSPData(String key, String defValue) {
        Context context = MyApplication.getInstance();
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        }
        return sharedPreferences.getString(key, defValue);
    }

    //写入
    public static void saveSPData(String key, int value) {
        Context context = MyApplication.getInstance();
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        }
        sharedPreferences.edit().putInt(key, value).commit();
    }

    //读出
    public static int getSPData(String key, int defValue) {
        Context context = MyApplication.getInstance();
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        }
        return sharedPreferences.getInt(key, defValue);
    }

    //写入
    public static void saveSPData(String key, boolean value) {
        Context context = MyApplication.getInstance();
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        }
        sharedPreferences.edit().putBoolean(key, value).commit();
    }

    //读出
    public static Boolean getSPData(String key, boolean defValue) {
        Context context = MyApplication.getInstance();
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        }
        return sharedPreferences.getBoolean(key, defValue);
    }

    //写入
    public static void saveSPData(String key, long value) {
        Context context = MyApplication.getInstance();
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        }
        sharedPreferences.edit().putLong(key, value).commit();
    }

    //读出
    public static long getSPData(String key, long defValue) {
        Context context = MyApplication.getInstance();
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        }
        return sharedPreferences.getLong(key, defValue);
    }

    //写入，保存数组
    public static void saveSPData(String key, List<String> list) throws JSONException {
        Context context = MyApplication.getInstance();
        String jsonString = toJson(list);
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        }
        sharedPreferences.edit().putString(key, jsonString).commit();
    }

    //读出，读取数组
    public static String[] getSPData(String key) {
        Context context = MyApplication.getInstance();
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        }
        String valueString = sharedPreferences.getString(key, null);
        if (valueString != null){
            return toArray(valueString);
        }else{
            return null;
        }
    }

    /**
     * 为将List<String>保存到本地，而将其转为json格式的String
     * @param strings
     * @return
     */
    public static String toJson(List<String> strings){
        Gson gson = new Gson();
        String jsonArray = gson.toJson(strings);
        return jsonArray;
    }

    /**
     * 取出来的是json格式的String，将其转为String数组
     * @param jsonStringArray
     * @return
     */
    public static String[] toArray(String jsonStringArray){
        Gson gson = new Gson();
        String[] strings = gson.fromJson(jsonStringArray, String[].class);
        return strings;
    }



}
