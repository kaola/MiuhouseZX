package com.miuhouse.zxcommunity.http;

public class FinalData {

    // 用户的状态
    public final static int ONLINE_VALUE = 1; // 在线
    public final static int OFFLINE_VALUE = 2; // 离线
    // 向后台请求的url
    public final static String IMAGE_URL_UPLOAD = "http://upload.miuhouse.com/app/";// 上传
    // public final static String IMAGE_URL_UPLOAD = "http://192.168.1.124:8080/app/";// 上传
    // public final static String IMAGE_URL_UPLOAD = "http://192.168.1.124:8080/app/";// 上传
    /**
     * 向服务器请求图片的url头
     */
    public final static String IMAGE_URL = "http://img.miuhouse.com";

    public final static String URL_YUNDUO = "http://yunduo.miuhouse.com/app/";

    //    服务器地址
    public final static String URL_HEAD = "http://zx.miuhouse.com";        //瞄房正兴社区
//    public final static String URL_HEAD = "http://app.miuhouse.com";
//    public final static String URL_HEAD = "http://192.168.1.185:8080";



    //    public final static String URL_VALUE = "http://cloud.miuhouse.com/app/";
//    public final static String URL_VALUE = "http://192.168.1.117:8080/app/";
    public final static String URL_VALUE = URL_HEAD + "/app/";


    public final static String URL_VALUE_COMMON = "http://192.168.1.108:8080/appcommon/";

    //闪屏页
    public final static String URL_INDEX = URL_HEAD + "/mobile/index";
    //    public final static String URL_VALUE_COMMON = "http://api.miuhouse.com/appcommon/";
//    public final static String URL_VALUE = "http://api.miuhouse.com/appteacher/";
    // 分享链接
    public final static String URL_SHARE = "http://cloud.miuhouse.com/down";
    //    public final static String URL_INDEX = "http://cloud.miuhouse.com/index";
    //广告
    public final static String URL_OTHER = URL_HEAD + "/mobile/other/";
    //活动
    public final static String URL_HUODONG = URL_HEAD + "/mobile/huodong/";
    //使用说明
    public final static String URL_EXPLAIN = URL_HEAD + "/mobile/explain";
    //社区新闻
    public final static String URL_NEWS = URL_HEAD + "/mobile/news/";
    // 向后台请求时发送的公共参数
    public final static String APP_KEY_VALUE = "hothz";
    public final static String PHONE_TYPE_VALUE = "3"; // 手机类型，1-安卓系统
    public static String VERSION_CODE_VALUE;
    public static String IMEI_VALUE;
    // 向后台请求时发送的参数字段
    public final static String MD5 = "md5";
    public final static String TRANSDATA = "transData";
    public final static String PHONE_TYPE = "deviceType";
    public final static String VERSION_CODE = "version_code";
    public final static String IMEI = "imei";
}
