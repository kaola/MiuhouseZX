package com.miuhouse.zxcommunity.bean;

import java.io.Serializable;
import java.util.ArrayList;

/** 楼盘信息
 * Created by khb on 2016/5/4.
 */
public class PropertyInfo implements Serializable {

    String id;
    String name;
    int minHuxing;  //最小户型
    int maxHuxing;  //最大户型
    String roomArea;    //面积
    int avgPrice;       //均价
    ArrayList<String> label;    //标签
    String province;    //省份
    int cityId;     //城市
    String city;    //城市名
    String area;    //地区
    String street;  //街道
    String developers;  //开发商
    String startTime;   //开盘时间 yyyy年MM月dd日
    String launchTime;  //交房时间
    String type;    //物业类型
    int cqnx;    //产权年限
    String jzlx;    //建筑类型
    String zxqk;    //装修情况
    float jzmj;    //建筑面积
    float zdmj;     //占地面积
    float rjl;      //容积率
    float lhl;      //绿化率
    int ghhs;   //规划户数
    int cws;    //车位数
    String company;     //物业公司
    float wyf;  //物业费
    String slc; //售楼处
    ArrayList<String> images;   //图片
    String mapUrl;      //地图图片
    double locX;    //纬度
    double locY;    //经度
    long createTime;    //创建时间
    int order;  //顺序？
    String mobile;  //联系电话
    String remark; //楼盘简介

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    public String getMapUrl() {
        return mapUrl;
    }

    public void setMapUrl(String mapUrl) {
        this.mapUrl = mapUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMinHuxing() {
        return minHuxing;
    }

    public void setMinHuxing(int minHuxing) {
        this.minHuxing = minHuxing;
    }

    public int getMaxHuxing() {
        return maxHuxing;
    }

    public void setMaxHuxing(int maxHuxing) {
        this.maxHuxing = maxHuxing;
    }

    public String getRoomArea() {
        return roomArea;
    }

    public void setRoomArea(String roomArea) {
        this.roomArea = roomArea;
    }

    public int getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(int avgPrice) {
        this.avgPrice = avgPrice;
    }

    public ArrayList<String> getLabel() {
        return label;
    }

    public void setLabel(ArrayList<String> label) {
        this.label = label;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
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

    public String getDevelopers() {
        return developers;
    }

    public void setDevelopers(String developers) {
        this.developers = developers;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getLaunchTime() {
        return launchTime;
    }

    public void setLaunchTime(String launchTime) {
        this.launchTime = launchTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCqnx() {
        return cqnx;
    }

    public void setCqnx(int cqnx) {
        this.cqnx = cqnx;
    }

    public String getJzlx() {
        return jzlx;
    }

    public void setJzlx(String jzlx) {
        this.jzlx = jzlx;
    }

    public String getZxqk() {
        return zxqk;
    }

    public void setZxqk(String zxqk) {
        this.zxqk = zxqk;
    }

    public float getJzmj() {
        return jzmj;
    }

    public void setJzmj(float jzmj) {
        this.jzmj = jzmj;
    }

    public float getZdmj() {
        return zdmj;
    }

    public void setZdmj(float zdmj) {
        this.zdmj = zdmj;
    }

    public float getRjl() {
        return rjl;
    }

    public void setRjl(float rjl) {
        this.rjl = rjl;
    }

    public float getLhl() {
        return lhl;
    }

    public void setLhl(float lhl) {
        this.lhl = lhl;
    }

    public int getGhhs() {
        return ghhs;
    }

    public void setGhhs(int ghhs) {
        this.ghhs = ghhs;
    }

    public int getCws() {
        return cws;
    }

    public void setCws(int cws) {
        this.cws = cws;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public float getWyf() {
        return wyf;
    }

    public void setWyf(float wyf) {
        this.wyf = wyf;
    }

    public String getSlc() {
        return slc;
    }

    public void setSlc(String slc) {
        this.slc = slc;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public double getLocX() {
        return locX;
    }

    public void setLocX(double locX) {
        this.locX = locX;
    }

    public double getLocY() {
        return locY;
    }

    public void setLocY(double locY) {
        this.locY = locY;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "PropertyInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", minHuxing=" + minHuxing +
                ", maxHuxing=" + maxHuxing +
                ", roomArea='" + roomArea + '\'' +
                ", avgPrice=" + avgPrice +
                ", label=" + label +
                ", province='" + province + '\'' +
                ", cityId=" + cityId +
                ", city='" + city + '\'' +
                ", area='" + area + '\'' +
                ", street='" + street + '\'' +
                ", developers='" + developers + '\'' +
                ", startTime='" + startTime + '\'' +
                ", launchTime='" + launchTime + '\'' +
                ", type='" + type + '\'' +
                ", cqnx=" + cqnx +
                ", jzlx='" + jzlx + '\'' +
                ", zxqk='" + zxqk + '\'' +
                ", jzmj=" + jzmj +
                ", zdmj=" + zdmj +
                ", rjl=" + rjl +
                ", lhl=" + lhl +
                ", ghhs=" + ghhs +
                ", cws=" + cws +
                ", company='" + company + '\'' +
                ", wyf=" + wyf +
                ", slc='" + slc + '\'' +
                ", images=" + images +
                ", mapUrl='" + mapUrl + '\'' +
                ", locX=" + locX +
                ", locY=" + locY +
                ", createTime=" + createTime +
                ", order=" + order +
                ", mobile='" + mobile + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
