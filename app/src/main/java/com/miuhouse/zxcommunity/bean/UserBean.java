package com.miuhouse.zxcommunity.bean;

import java.io.Serializable;

/**
 * Created by kings on 1/6/2016.
 */
public class UserBean implements Serializable {

    private String id;//用户id
    private String nickName;//用户昵称
    private String headUrl;//用户头像
    private String name;//用户真是名字
    private long propertyId;//小区ID
    private String build;//楼栋号
    private String mobile;//电话号码
    private String unit;//房号
    private String propertyName;
    

    private int status; //用户审核状态

    public final static int UNCHECKED = 0;  //审核中
    public final static int CHECKED = 1;    //审核通过
    public final static int CHECKED_NOT_OK = -1; //审核未通过
    public final static int BANNED = -2;    //被禁
    public final static int NEED_CHECKED=2;// 请审核

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

   
   

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(long propertyId) {
        this.propertyId = propertyId;
    }

    public String getBuild() {
        return build;
    }

    public void setBuild(String build) {
        this.build = build;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
