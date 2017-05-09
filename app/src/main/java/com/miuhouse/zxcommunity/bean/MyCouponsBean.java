package com.miuhouse.zxcommunity.bean;

import java.io.Serializable;

/**
 * 我的优惠券
 * Created by kings on 1/13/2016.
 */
public class MyCouponsBean implements Serializable {
    private String id;
    private String ownerId;
    private String couponId;
    private CouponBean coupon;
    private int status;
    //-1表示已经过期 ，0表示可以使用
    private int type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public CouponBean getCoupon() {
        return coupon;
    }

    public void setCoupon(CouponBean coupon) {
        this.coupon = coupon;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
