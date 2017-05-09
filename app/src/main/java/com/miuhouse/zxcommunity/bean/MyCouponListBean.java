package com.miuhouse.zxcommunity.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by kings on 1/13/2016.
 */
public class MyCouponListBean extends BaseBean implements Serializable {

    private ArrayList<MyCouponsBean> myCoupons;

    public ArrayList<MyCouponsBean> getMyCoupons() {
        return myCoupons;
    }

    public void setMyCoupons(ArrayList<MyCouponsBean> myCoupons) {
        this.myCoupons = myCoupons;
    }
}
