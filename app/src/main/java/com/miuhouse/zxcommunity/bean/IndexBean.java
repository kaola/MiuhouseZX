package com.miuhouse.zxcommunity.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by kings on 3/18/2016.
 */
public class IndexBean extends BaseBean implements Serializable {
    private ArrayList<ZFZBean> newPropertyInfos;
    private ArrayList<ZFZBean> zfs;
    private ArrayList<ZFZBean> esfs;
    private CouponBean coupon;
    private ArrayList<BannersBean> banners;
    private ArrayList<IndexImages> indexImages;
    private ArrayList<NewsInfoBean> crawNewsInfo;
    //活动图片
    private HuodongBean huodongs;
    //当用户没有登录 使用服务器返回的默认的propertyID和propertyName
    private long propertyId;

    private String propertyName;

    private ArrayList<NewsInfoBean> newsInfo;

    private long cityId;

    private String companyMobile;

    public String getCompanyMobile() {
        return companyMobile;
    }

    public void setCompanyMobile(String companyMobile) {
        this.companyMobile = companyMobile;
    }

    public long getCityId() {
        return cityId;
    }


    public void setCityId(long cityId) {
        this.cityId = cityId;
    }

    public ArrayList<NewsInfoBean> getCrawNewsInfo() {
        return crawNewsInfo;
    }

    public void setCrawNewsInfo(ArrayList<NewsInfoBean> crawNewsInfo) {
        this.crawNewsInfo = crawNewsInfo;
    }

    public ArrayList<ZFZBean> getNewPropertyInfos() {
        return newPropertyInfos;
    }

    public void setNewPropertyInfos(ArrayList<ZFZBean> newPropertyInfos) {
        this.newPropertyInfos = newPropertyInfos;
    }

    public ArrayList<ZFZBean> getZfs() {
        return zfs;
    }

    public void setZfs(ArrayList<ZFZBean> zfs) {
        this.zfs = zfs;
    }

    public ArrayList<ZFZBean> getEsfs() {
        return esfs;
    }

    public void setEsfs(ArrayList<ZFZBean> esfs) {
        this.esfs = esfs;
    }

    public HuodongBean getHuodongs() {
        return huodongs;
    }

    public void setHuodongs(HuodongBean huodongs) {
        this.huodongs = huodongs;
    }

    public CouponBean getCoupon() {
        return coupon;
    }

    public void setCoupon(CouponBean coupon) {
        this.coupon = coupon;
    }

    public ArrayList<BannersBean> getBanners() {
        return banners;
    }

    public void setBanners(ArrayList<BannersBean> banners) {
        this.banners = banners;
    }

    public ArrayList<IndexImages> getIndexImages() {
        return indexImages;
    }

    public void setIndexImages(ArrayList<IndexImages> indexImages) {
        this.indexImages = indexImages;
    }

    public long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(long propertyId) {
        this.propertyId = propertyId;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public ArrayList<NewsInfoBean> getNewsInfo() {
        return newsInfo;
    }

    public void setNewsInfo(ArrayList<NewsInfoBean> newsInfo) {
        this.newsInfo = newsInfo;
    }
}
