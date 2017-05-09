package com.miuhouse.zxcommunity.utils;

import com.hyphenate.easeui.EaseConstant;

/**
 * 存放各类常量
 * Created by khb on 2016/1/27.
 */
public class Constants extends EaseConstant {

    public static final String INTENT_ACTION_LOGOUT = "com.miuhouse.community.action.LOGOUT";

    public final static String SMSSDK_COUNTRYCODE = "86";
    public final static String SMSSDK_APP_KEY = "1bc0bb3c13e30";
    public final static String SMSSDK_APP_SECRET = "8c0edc7cda313ada0579470a57c18c4e";

    public final static int LEASE = 11;
    public final static int SELL = 12;

    public final static int HOME = 0;
    public final static int LOOKINGFORTEACHER = 1;
    public final static int MESSAGE = 2;
    public final static int NOTIFICATION = 3;
    public final static int CONVERSATION = 4;
    public final static int MAP = 5;
    public final static int WUYEREPLY = 6;
    //最多几张图片
    public static final int MAX_NUM = 9;

    //    从服务器返回的图片url前缀，图片服务器地址
    public final static String IMGURL_HEAD = "http://img.miuhouse.com";
    //    环信账号设置
    public final static String EASEACCOUNTHEAD = "community";

    public final static String PASSWORD = "1234456";
    //    App临时账号头
    public final static String TEMPACCOUNTHEAD = "CM";
    //    环信消息扩展的参数名
    public static final String NICKNAME = "nickname";
    public static final String HEAD = "head";
    //    保存本地sp的参数名
    public static final String TEMPNAME = "tempname";
    public static final String PROPERTYID = "propertyid";
    public static final String ZXQK = "zxqk";   //装修情况
    public static final String CX = "cx";   //朝向
    public static final String PTSS = "ptss";   //配套设置
    public static final String ESFLABEL = "esflabel";   //二手房标签
    public static final String ZFLABEL = "zflabel";   //租房标签
    public static final String JZLX = "jzlx";   //建筑类型
    public static final String ESFPRICE = "esfPrice";   //售房价格
    public static final String FLOORAGE = "floorAge";   //楼龄
    public static final String ZFPRICE = "zfPrice";   //租房价格

    public static final String FX = "fx";   //房型
    public static final String REPLYCOUNT = "reply_count";
    public static final int TYPE_GLCS = 10;   //购楼常识
    public static final int TYPE_SNZX = 11;   //室内装修
}
