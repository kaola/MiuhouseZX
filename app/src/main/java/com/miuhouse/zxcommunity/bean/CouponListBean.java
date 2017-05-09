package com.miuhouse.zxcommunity.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 优惠券列表数据
 * Created by kings on 1/12/2016.
 */
public class CouponListBean extends BaseBean implements Serializable {
    private ArrayList<CouponBean> coupons;

    public ArrayList<CouponBean> getCoupons() {
        return coupons;
    }

    public void setCoupons(ArrayList<CouponBean> coupons) {
        this.coupons = coupons;
    }
}
