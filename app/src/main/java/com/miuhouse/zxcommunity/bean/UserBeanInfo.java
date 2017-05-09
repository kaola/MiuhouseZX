package com.miuhouse.zxcommunity.bean;

import java.io.Serializable;

/**
 * Created by kings on 1/6/2016.
 */
public class UserBeanInfo extends BaseBean implements Serializable {

    public UserBean userInfo;

    public UserBean getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserBean userInfo) {
        this.userInfo = userInfo;
    }
}
