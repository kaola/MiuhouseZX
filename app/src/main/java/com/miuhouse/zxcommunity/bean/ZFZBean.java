package com.miuhouse.zxcommunity.bean;

import java.io.Serializable;

/**
 * Created by kings on 3/18/2016.
 */
public class ZFZBean implements Serializable {
    private String id;
    private long propertyId;
    private String title;

    private String name;
    /**
     * 新房房间范围
     */
    private String minHuxing;
    private String maxHuxing;
    /**
     * 二手房，租房的户型名字
     */
    private String huxing;
    /**
     * 二手房，租房。房屋图片
     */
    private String image;

    /**
     * 新房房屋图片
     */
    private String headUrl;
    /**
     * 二手房，租房的价格
     */
    private int price;
    /**
     * 新房价格
     */
    private int avgPrice;

    private String remark;
    private String roomsArea; //面积
    private String city;    //城市名
    private String area;    //地区
    private String street;  //街道


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(long propertyId) {
        this.propertyId = propertyId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRoomsArea() {
        return roomsArea;
    }

    public void setRoomsArea(String roomsArea) {
        this.roomsArea = roomsArea;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getMinHuxing() {
        return minHuxing;
    }

    public void setMinHuxing(String minHuxing) {
        this.minHuxing = minHuxing;
    }

    public String getMaxHuxing() {
        return maxHuxing;
    }

    public void setMaxHuxing(String maxHuxing) {
        this.maxHuxing = maxHuxing;
    }

    public String getHuxing() {
        return huxing;
    }

    public void setHuxing(String huxing) {
        this.huxing = huxing;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public int getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(int avgPrice) {
        this.avgPrice = avgPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
