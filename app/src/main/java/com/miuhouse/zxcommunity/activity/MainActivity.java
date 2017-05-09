package com.miuhouse.zxcommunity.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.NetUtils;
import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.application.MyApplication;
import com.miuhouse.zxcommunity.bean.Contact;
import com.miuhouse.zxcommunity.bean.Notification;
import com.miuhouse.zxcommunity.db.AccountDBTask;
import com.miuhouse.zxcommunity.db.ContactDao;
import com.miuhouse.zxcommunity.db.NotificationDao;
import com.miuhouse.zxcommunity.fragment.FragmentFactory;
import com.miuhouse.zxcommunity.fragment.HomeFragment;
import com.miuhouse.zxcommunity.fragment.MapFragment;
import com.miuhouse.zxcommunity.fragment.MessageFragment;
import com.miuhouse.zxcommunity.fragment.MyFragment;
import com.miuhouse.zxcommunity.http.CustomStringRequest;
import com.miuhouse.zxcommunity.http.FinalData;
import com.miuhouse.zxcommunity.http.VolleySingleton;
import com.miuhouse.zxcommunity.manager.DictManager;
import com.miuhouse.zxcommunity.utils.Constants;
import com.miuhouse.zxcommunity.utils.L;
import com.miuhouse.zxcommunity.utils.MyUtils;
import com.miuhouse.zxcommunity.utils.SPUtils;
import com.miuhouse.zxcommunity.widget.MyDot;
import com.miuhouse.zxcommunity.widget.StatusCompat;
import com.umeng.message.IUmengCallback;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity
    //        implements EMEventListener
{
    public static final String TAG = "MainActivity";
    public static final int REQUSET_OK = 1;
    public static MainActivity mainActivity;

    private Button btnMe, btnMessage, btnHome, btnMap;
    private FragmentManager fm;
    private Context context = this;
    //    private PushAgent mPushAgent;

    private final static int HOME = 0;
    private final static int LOOKINGFORTEACHER = 1;
    private final static int MESSAGE = 2;

    private Button startLocation;
    private FragmentTransaction ft;
    //    private TextView unreadLabel;

    private Toolbar mToolbar;
    private EaseConversationListFragment conversationListFragment;
    private MessageFragment messageFragment;
    private RelativeLayout optionTitleLayout;
    private MyDot dot;

    public MainActivity() {
    }
    //    private EaseContactListFragment easeContactListFragment;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //        注释此行，不保存Fragment状态，这样当该activity被回收时，attach的Fragment也会一通被回收
        //        此段代码是为了解决fragment.getActivity()返回为null的问题
        //        super.onSaveInstanceState(outState);
    }

    @Override
    public void initTitle() {
        StatusCompat.compat(this, StatusCompat.COLOR_DEFAULT);
    }

    public Toolbar getmToolbar() {
        return mToolbar;
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_main);
        //        StatusCompat.compat(activity, StatusCompat.COLOR_TITLE);
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        btnHome = (Button) findViewById(R.id.bt_home);
        btnMessage = (Button) findViewById(R.id.bt_message);
        btnMe = (Button) findViewById(R.id.bt_me);
        btnMap = (Button) findViewById(R.id.bt_map);
        dot = (MyDot) findViewById(R.id.unreadDot);

        messageFragment = (MessageFragment) FragmentFactory.getFragment(Constants.MESSAGE);
        messageFragment.setConversationListItemClickListener(
            new MessageFragment.ConversationListItemClickListener() {
                @Override
                public void onListItemClicked(EMConversation conversation) {
                    startActivity(new Intent(MainActivity.this, ChatActivity.class).putExtra(
                        EaseConstant.EXTRA_USER_ID, conversation.getUserName()));
                }
            });

        ft.add(R.id.rl_fragment_contanier,
            (HomeFragment) FragmentFactory.getFragment(Constants.HOME), "HOME")
            .add(R.id.rl_fragment_contanier, messageFragment, "MESSAGE")
            .hide(messageFragment)
            .add(R.id.rl_fragment_contanier,
                (MyFragment) FragmentFactory.getFragment(Constants.LOOKINGFORTEACHER),
                "LOOKINGFORTEACHER")
            .hide((MyFragment) FragmentFactory.getFragment(Constants.LOOKINGFORTEACHER))
            .add(R.id.rl_fragment_contanier,
                (MapFragment) FragmentFactory.getFragment(Constants.MAP), "MAP")
            .hide((MapFragment) FragmentFactory.getFragment(Constants.MAP));
        ft.show((HomeFragment) FragmentFactory.getFragment(HOME)).commit();
        btnHome.setCompoundDrawablesWithIntrinsicBounds(null,
            getResources().getDrawable(R.mipmap.nav_ico_shouye_pre), null, null);
        btnHome.setTextColor(Color.parseColor("#ffff4444"));
        //        bt_home.setSelected(true);

    }

    @Override
    public void initData() {
        showLogI("time :" + System.currentTimeMillis());
        DictManager.getInstance(this).init();

        //        String device_token = UmengRegistrar.getRegistrationId(context);
        //        showLogD("device_token " + device_token);
        //        SPUtils.saveSPData("token", device_token);
        //        initLocation();
        //        mLocationClient.start();

        if (!MyUtils.isLoggedIn()) { //未登录时生成临时账号
            MyUtils.generateTempAccount();
        } else { //登录了但是未设置昵称时，生成临时昵称
            //        当本地有临时昵称时，不用生成新的
            //            if (SPUtils.getSPData(Constants.TEMPNAME, null) != null){
            //                return ;
            //            }
            //            if (MyUtils.isEmpty(AccountDBTask.getUserBean().getNickName())){
            //                MyUtils.generateTempNickname();
            //            }
        }
    }

    @Override
    public void initVariables() {
        MyUtils.init(this);
    }

    @Override
    public String getTag() {
        return TAG;
    }

    //    public IUmengRegisterCallback mRegisterCallback = new IUmengRegisterCallback() {
    //
    //        @Override
    //        public void onRegistered(String registrationId) {
    //            String pkgName = getApplicationContext().getPackageName();
    //            String info = String.format("enabled:%s  isRegistered:%s  DeviceToken:%s " +
    //                            "SdkVersion:%s AppVersionCode:%s AppVersionName:%s",
    //                    mPushAgent.isEnabled(), mPushAgent.isRegistered(),
    //                    mPushAgent.getRegistrationId(), MsgConstant.SDK_VERSION,
    //                    UmengMessageDeviceConfig.getAppVersionCode(MainActivity.this), UmengMessageDeviceConfig.getAppVersionName(MainActivity.this));
    //            SPUtils.saveSPData("token", mPushAgent.getRegistrationId());
    //            showLogD(info);
    //        }
    //    };

    /**
     * 首页底部按钮的事件
     */
    public void showFragment(View v) {
        switch (v.getId()) {
            case R.id.bt_home:
                fm.beginTransaction()
                    .hide(messageFragment)
                    .hide(FragmentFactory.getFragment(Constants.LOOKINGFORTEACHER))
                    .hide(FragmentFactory.getFragment(Constants.MAP))
                    .show(FragmentFactory.getFragment(Constants.HOME))
                    .commit();
                //                bt_home.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.ic_launcher), null, null);
                btnMe.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.mipmap.nav_ico_me), null, null);
                btnMessage.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.mipmap.nav_ico_xiaoxi), null, null);
                btnMap.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.mipmap.nav_ico_map), null, null);
                btnHome.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.mipmap.nav_ico_shouye_pre), null, null);
                btnHome.setTextColor(Color.parseColor("#ffff4444"));
                btnMap.setTextColor(Color.parseColor("#ffb2b2b2"));
                btnMe.setTextColor(Color.parseColor("#ffb2b2b2"));
                btnMessage.setTextColor(Color.parseColor("#ffb2b2b2"));
                break;
            case R.id.bt_map:
                fm.beginTransaction()
                    .hide(messageFragment)
                    .hide(FragmentFactory.getFragment(Constants.LOOKINGFORTEACHER))
                    .hide(FragmentFactory.getFragment(Constants.HOME))
                    .show(FragmentFactory.getFragment(Constants.MAP))
                    .commit();
                //                bt_home.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.ic_launcher), null, null);
                btnMe.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.mipmap.nav_ico_me), null, null);
                btnMessage.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.mipmap.nav_ico_xiaoxi), null, null);
                btnMap.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.mipmap.nav_ico_map_pre), null, null);
                btnHome.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.mipmap.nav_ico_shouye), null, null);
                btnHome.setTextColor(Color.parseColor("#ffb2b2b2"));
                btnMap.setTextColor(Color.parseColor("#ffff4444"));
                btnMe.setTextColor(Color.parseColor("#ffb2b2b2"));
                btnMessage.setTextColor(Color.parseColor("#ffb2b2b2"));
                break;
            case R.id.bt_me:
                //                if (!MyUtils.isLoggedIn()){
                //                    showToast(getResources().getString(R.string.user_not_login));
                //                    startActivity(new Intent(activity, LoginActivity.class));
                //                    return ;
                //                }
                fm.beginTransaction()
                    .hide(FragmentFactory.getFragment(Constants.HOME))
                    .hide(messageFragment)
                    .hide(FragmentFactory.getFragment(Constants.MAP))
                    .show(FragmentFactory.getFragment(Constants.LOOKINGFORTEACHER))
                    .commit();
                //                startActivity(new Intent(this, SubjectFirstCategoryActivity.class));
                //                bt_home.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.ic_launcher), null, null);
                btnMe.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.mipmap.nav_ico_me_pre), null, null);
                btnMessage.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.mipmap.nav_ico_xiaoxi), null, null);
                btnMap.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.mipmap.nav_ico_map), null, null);
                btnHome.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.mipmap.nav_ico_shouye), null, null);
                btnMe.setTextColor(Color.parseColor("#ffff4444"));
                btnMap.setTextColor(Color.parseColor("#ffb2b2b2"));
                btnHome.setTextColor(Color.parseColor("#ffb2b2b2"));
                btnMessage.setTextColor(Color.parseColor("#ffb2b2b2"));
                break;
            case R.id.bt_message:
                fm.beginTransaction()
                    .hide(FragmentFactory.getFragment(Constants.HOME))
                    .hide(FragmentFactory.getFragment(Constants.MAP))
                    .hide(FragmentFactory.getFragment(Constants.LOOKINGFORTEACHER))
                    .show(messageFragment)
                    .commit();
                //                bt_home.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.ic_launcher), null, null);
                btnMe.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.mipmap.nav_ico_me), null, null);
                btnMessage.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.mipmap.nav_ico_xiaoxi_pre), null, null);
                btnMap.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.mipmap.nav_ico_map), null, null);
                btnHome.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.mipmap.nav_ico_shouye), null, null);
                btnMessage.setTextColor(Color.parseColor("#ffff4444"));
                btnMap.setTextColor(Color.parseColor("#ffb2b2b2"));
                btnHome.setTextColor(Color.parseColor("#ffb2b2b2"));
                btnMe.setTextColor(Color.parseColor("#ffb2b2b2"));
                break;
        }
    }

    private void setListener() {
        mPushAgent.setNotificationClickHandler(new UmengNotificationClickHandler() {
            @Override
            public void handleMessage(Context context, UMessage uMessage) {
                //                showToast("message`s been clicked.");
                super.handleMessage(context, uMessage);
            }
        });
        mPushAgent.setMessageHandler(new UmengMessageHandler() {
            @Override
            public void handleMessage(Context context, UMessage uMessage) {
                //                showToast("handleMessage " + uMessage.text);
                showLogD("= = = = = handleMessage : " + uMessage.text);
                if (null != uMessage.extra && uMessage.extra.size() > 0) {
                    Map<String, String> extra = uMessage.extra;
                    int type = Integer.parseInt(extra.get("type"));
                    switch (type) {
                        case 1:     //物业回复
                            int count = SPUtils.getSPData(Constants.REPLYCOUNT, 0);
                            count++;
                            SPUtils.saveSPData(Constants.REPLYCOUNT, count);
                            break;
                        case 2:     //验证消息
                            AccountDBTask.updateStatus(
                                MyApplication.getInstance().getUserBean().getId(),
                                Integer.parseInt(extra.get("status")));
                            MainActivity.this.sendBroadcast(new Intent(LoginActivity.INTENT_ACTION_USER_CHANGE));
                            break;
                        case 3:     //公告
                            Notification notification = new Notification();
                            notification.setId(extra.get("id"));
                            notification.setTitle(uMessage.title);
                            notification.setDescription(uMessage.text);
                            notification.setCreateTime(System.currentTimeMillis());
                            notification.setSendTime(System.currentTimeMillis() + "");
                            notification.setContent(extra.get("content"));
                            //                notification.setSendTime(extra.get("sendTime"));
                            //                notification.setSendTime(Data);
                            notification.setPropertyId(Integer.parseInt(extra.get("propertyId")));
                            notification.setIsRead(false);
                            NotificationDao nDao = new NotificationDao(activity);
                            long result = nDao.addData(notification);
                            break;
                    }
                    refreshUI();
                }
                super.handleMessage(context, uMessage);
            }
        });
    }

    //实现ConnectionListener接口
    private class MyConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showLogI("环信服务器已连接");
                    //                    showToast("环信服务器已连接");
                    showLogD("current  user: " + EMClient.getInstance().getCurrentUser());
                }
            });
        }

        @Override
        public void onDisconnected(final int error) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (error == EMError.USER_REMOVED) {
                        // 显示帐号已经被移除
                        showLogI("帐号已经被移除");
                        //                        showToast("帐号已经被移除");
                    } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                        // 显示帐号在其他设备登陆
                        showLogI("帐号在其他设备登陆");
                        //                        showToast("帐号在其他设备登陆");
                    } else {
                        if (NetUtils.hasNetwork(MainActivity.this)) {
                            //连接不到聊天服务器
                            showLogI("连接不到聊天服务器");
                            //                            showToast("连接不到聊天服务器");
                        } else {
                            //当前网络不可用，请检查网络设置
                            showLogI("当前网络不可用，请检查网络设置");
                            //                            showToast("当前网络不可用，请检查网络设置");
                        }
                    }
                }
            });
        }
    }

    EMMessageListener emMessageListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> list) {
            ContactDao dao = new ContactDao(activity);
            EMMessage message = list.get(0);
            String id = message.getFrom();
            try {
                //                        从消息扩展中获取对方的昵称和头像
                String head = message.getStringAttribute(Constants.HEAD);
                String nickname = message.getStringAttribute(Constants.NICKNAME);
                L.i("head " + head + " ; nickname " + nickname);
                //                        判断本地是否已经有该id的头像和昵称，如果没有，添加至本地
                //                        如果已有，更新本地数据
                Contact contact = dao.getContactById(id);
                if (contact == null) {
                    //                            getContactFromServer(id);
                    dao.addContact(id, head, nickname);
                } else {
                    dao.updateContact(id, head, nickname);
                }
            } catch (HyphenateException e) {
                e.printStackTrace();
            }
            //                    runOnUiThread(new Runnable() {
            //                        @Override
            //                        public void run() {
            //                            showToast("You`ve got a message");
            //                        }
            //                    });
            showLogI("On message received");
            EaseUI.getInstance().getNotifier().viberateAndPlayTone(message);
            refreshUI();
            //                    conversationListFragment.refresh();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> list) {
            showToast("You`ve got a cmdessage");
        }

        @Override
        public void onMessageReadAckReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageDeliveryAckReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageChanged(EMMessage emMessage, Object o) {

        }
    };

    private void getContactFromServer(String id) {
        String url = FinalData.URL_VALUE + "getUserHeadUrlListByHxIds";
        List<String> idList = new ArrayList<>();
        idList.add(id);
        Map<String, Object> map = new HashMap<>();
        map.put("ids", idList);
        CustomStringRequest request = new CustomStringRequest(Request.Method.POST, url, map,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(s);
                        int code = jsonObject.getInt("code");
                        if (code == 0) {
                            JSONArray jsonArray = jsonObject.getJSONArray("list");
                            if (jsonArray != null && jsonArray.length() > 0) {
                                ContactDao dao = new ContactDao(activity);
                                dao.addContact(jsonArray.getString(0),
                                    jsonArray.getString(1),
                                    jsonArray.getString(2));
                            } else {

                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            });
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    public void refreshUI() {
        runOnUiThread(new Runnable() {
            public void run() {
                //                 刷新bottom bar消息未读数
                updateUnreadLabel();
                // 当前页面如果为聊天历史页面，刷新此页面
                ((MessageFragment) FragmentFactory.getFragment(Constants.MESSAGE)).refresh(
                    Constants.WUYEREPLY);
                ((MessageFragment) FragmentFactory.getFragment(Constants.MESSAGE)).refresh(
                    Constants.NOTIFICATION);
                //              if (fm.findFragmentByTag("MESSAGE") != null) {
                //                ((MessageFragment) FragmentFactory.getFragment(MESSAGE)).refresh();
                //              }
            }
        });
    }

    //    /** 刷新未读消息数
    //     *//*
    public void updateUnreadLabel() {
        //
        int count = getUnreadMsgCountTotal();
        if (count > 0) {
            dot.setVisibility(View.VISIBLE);
        } else {
            dot.setVisibility(View.GONE);
        }
    }

    /**
     * 获取未读消息数
     */
    public int getUnreadMsgCountTotal() {
        int unreadMsgCountTotal = 0;
        unreadMsgCountTotal = EMClient.getInstance().chatManager().getUnreadMsgsCount();
        long pid = SPUtils.getSPData(Constants.PROPERTYID, 0L);
        return unreadMsgCountTotal + +new NotificationDao(this).getUnreadDataCountByPropertyId(pid);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //        return super.onCreateOptionsMenu(menu);
        return false;
    }

    //    @Override
    ////    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    ////        if (requestCode == REQUSET_OK) {
    ////            if (resultCode == RESULT_OK) {
    //                String propertyName = data.getStringExtra("property");
    //                int propertyId = data.getIntExtra("propertyID", 0);
    //                Log.i("TAG", "propertyName=" + propertyName + "propertyId=" + propertyId);
    ////                title.setText(propertyName);
    //            }
    //        }
    //    }

    @Override
    protected void onResume() {
        super.onResume();
        showLogD("--------IS HX CONNECTED-------- " + EMClient.getInstance().isConnected());
        showLogD(
            "--------IS HX ACCOUNT LOGIN-------- " + EMClient.getInstance().isLoggedInBefore());
        //        没有登录就不开启推送
        if (mPushAgent != null) {
            if (!MyUtils.isLoggedIn()) {
                mPushAgent.disable(new IUmengCallback() {
                    @Override
                    public void onSuccess() {
                        Log.i("umeng push", "由于未登录，推送关闭");
                    }

                    @Override
                    public void onFailure(String s, String s1) {
                        Log.i("umeng push", "推送关闭失败");
                    }
                });
            } else {
                mPushAgent.enable(new IUmengCallback() {
                    @Override
                    public void onSuccess() {
                        Log.i("umeng push", "推送打开");
                    }

                    @Override
                    public void onFailure(String s, String s1) {
                        Log.i("umeng push", "推送打开失败");
                    }
                });
                Log.i("umeng push", "push on");
            }
        }
        //        showLogD(" PUSHAGENT "+ mPushAgent.isEnabled());
        setListener();
        //      注册一个监听连接状态的listener
        EMClient.getInstance().addConnectionListener(new MyConnectionListener());
        EMClient.getInstance().chatManager().addMessageListener(emMessageListener);
        refreshUI();
        //        if (!isConflict && !isCurrentAccountRemoved) {
        //        updateUnreadLabel();
        //            updateUnreadAddressLable();
        //            EMChatManager.getInstance().activityResumed();
        //        }

        // unregister this event listener when this activity enters the
        // background
        //        DemoHXSDKHelper sdkHelper = (DemoHXSDKHelper) DemoHXSDKHelper.getInstance();
        //        sdkHelper.pushActivity(this);

        // register the event listener when enter the foreground
        //        EMChatManager.getInstance().registerEventListener(this,
        //                new EMNotifierEvent.Event[]{EMNotifierEvent.Event.EventNewMessage, EMNotifierEvent.Event.EventOfflineMessage, EMNotifierEvent.Event.EventConversationListChanged});
    }

    @Override
    protected void onStop() {
        EMClient.getInstance().chatManager().removeMessageListener(emMessageListener);
        super.onStop();
    }
}
