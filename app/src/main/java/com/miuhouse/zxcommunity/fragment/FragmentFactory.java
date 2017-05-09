package com.miuhouse.zxcommunity.fragment;

import android.support.v4.app.Fragment;

import com.miuhouse.zxcommunity.utils.Constants;

import java.util.HashMap;

/**
 * 获取主页面的Fragment，如果有就直接获取它的对象，没有就新建
 * Created by khb on 2015/8/19.
 */
public class FragmentFactory {
    private static HashMap<Integer, Fragment> hashMap = new HashMap<Integer, Fragment>();

    public static BaseFragment getFragment(int position) {
        BaseFragment baseFragment = null;
        if (hashMap.containsKey(position)) {
            if (hashMap.get(position) != null) {
                baseFragment = (BaseFragment) hashMap.get(position);
            }
        } else {
            switch (position) {
                case Constants.HOME: // 首页
                    baseFragment = new HomeFragment();
                    break;
                case Constants.LOOKINGFORTEACHER: //
                    baseFragment = new MyFragment();
                    break;
                case Constants.MAP:
                    baseFragment = new MapFragment();
                    break;
                case Constants.MESSAGE: //消息
                    baseFragment = new MessageFragment();
                    break;
                case Constants.NOTIFICATION:
                    baseFragment = new NotificationFragment();
                    break;
                case Constants.CONVERSATION://环信会话消息列表
                    baseFragment = new ConversationFragment();
                    break;
                case Constants.WUYEREPLY:   //物业回复
                    baseFragment = new WuyeMessageFragment();
                    break;
            }
            hashMap.put(position, baseFragment);
        }
        return baseFragment;
    }

}
