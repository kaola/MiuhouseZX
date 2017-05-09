package com.miuhouse.zxcommunity.application;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.support.multidex.MultiDexApplication;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.util.EMLog;
import com.miuhouse.zxcommunity.bean.UserBean;
import com.miuhouse.zxcommunity.db.AccountDBTask;
import com.miuhouse.zxcommunity.utils.Constants;
import com.miuhouse.zxcommunity.utils.CrashHandler;
import com.miuhouse.zxcommunity.utils.L;
import com.miuhouse.zxcommunity.utils.SPUtils;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.socialize.PlatformConfig;

import java.util.List;

/**
 * Created by khb on 2015/12/28.
 */
public class MyApplication extends MultiDexApplication {

    private static MyApplication instance;
    private UserBean mUserBean;
    private long deltaBetweenServerAndClientTime;
    private EMMessageListener messageListener;
    private EaseUI easeUI;
    private String city;

    /**
     * 当前用户nickname,为了苹果推送不是userid而是昵称
     */
    public static String currentUserNick = "";
    private PushAgent mPushAgent;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
//        MultiDex.install(this);
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
        initPlatformConfig();
        EMOptions options = new EMOptions();
//        添加好友不需要验证
        options.setAcceptInvitationAlways(true);
        options.setAutoLogin(true);
        easeUI = EaseUI.getInstance();
        if (easeUI.init(getApplicationContext(), options)) {
//        EMClient.getInstance().init(getApplicationContext(), options);
            EMClient.getInstance().setDebugMode(true);
        }
        //开启友盟推送服务
        mPushAgent = PushAgent.getInstance(this);
        mPushAgent.setDebugMode(false);
//注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                String pkgName = getApplicationContext().getPackageName();
                String info = String.format(" DeviceToken:%s ", deviceToken);
                SPUtils.saveSPData("token", mPushAgent.getRegistrationId());
                Log.i("umeng push", info);
            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });
    }

    /**
     * 初始化分享配置
     * 微博 qq
     */
    private void initPlatformConfig() {
        PlatformConfig.setWeixin("wxa4d8fc1beec59204", "eee7fec10731f01faf6a77bfdc4b655f");
        PlatformConfig.setQQZone("1105792713", "1O8VHSOcUJFaV30j");
    }


    public static MyApplication getInstance() {
        return instance;
    }

    /**
     * 获取App安装包信息
     *
     * @return
     */
    public PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }

    /**
     * 获取手机IMEI号
     *
     * @return
     */
    public String getIMEI() {
        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }


    /**
     * 获取用户信息
     *
     * @return
     */
    public UserBean getUserBean() {
        if (mUserBean == null) {
            mUserBean = AccountDBTask.getUserBean();
        }
        return mUserBean;
    }

    public void setmUserBean(UserBean mUserBean) {
        this.mUserBean = mUserBean;
    }

    protected void registerEventListener() {
        messageListener = new EMMessageListener() {
            private BroadcastReceiver broadCastReceiver = null;

            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                for (EMMessage message : messages) {
                    EMLog.d("TAG", "onMessageReceived id : " + message.getMsgId());
                    //应用在后台，不需要刷新UI,通知栏提示新消息
                    if (!easeUI.hasForegroundActivies()) {
                        L.i("后台收到消息");
                        easeUI.getNotifier().onNewMsg(message);
                    }
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                /*for (EMMessage message : messages) {
                    EMLog.d(TAG, "收到透传消息");
                    //获取消息body
                    EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
                    final String action = cmdMsgBody.action();//获取自定义action

                    //获取扩展属性 此处省略
                    //message.getStringAttribute("");
                    EMLog.d(TAG, String.format("透传消息：action:%s,message:%s", action,message.toString()));
                    final String str = appContext.getString(com.hyphenate.easeui.R.string.receive_the_passthrough);

                    final String CMD_TOAST_BROADCAST = "hyphenate.demo.cmd.toast";
                    IntentFilter cmdFilter = new IntentFilter(CMD_TOAST_BROADCAST);

                    if(broadCastReceiver == null){
                        broadCastReceiver = new BroadcastReceiver(){

                            @Override
                            public void onReceive(Context context, Intent intent) {
                                // TODO Auto-generated method stub
                                Toast.makeText(appContext, intent.getStringExtra("cmd_value"), Toast.LENGTH_SHORT).show();
                            }
                        };

                        //注册广播接收者
                        appContext.registerReceiver(broadCastReceiver,cmdFilter);
                    }

                    Intent broadcastIntent = new Intent(CMD_TOAST_BROADCAST);
                    broadcastIntent.putExtra("cmd_value", str+action);
                    appContext.sendBroadcast(broadcastIntent, null);
                }*/
            }

            @Override
            public void onMessageReadAckReceived(List<EMMessage> messages) {
            }

            @Override
            public void onMessageDeliveryAckReceived(List<EMMessage> message) {
            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {

            }
        };

        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    public void logout() {
        if (AccountDBTask.getUserBean() != null) {
            AccountDBTask.clear();
        }
        this.mUserBean = null;
        Intent intent = new Intent(Constants.INTENT_ACTION_LOGOUT);
        sendBroadcast(intent);
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
