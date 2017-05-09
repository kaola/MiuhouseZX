package com.miuhouse.zxcommunity.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by khb on 2016/1/8.
 */
public class House implements Serializable{

    public static final int AVAILABLE = 0;
    public static final int UNAVAILABLE = 1;
    public static final int SOLD = 2;

    String id;
    long propertyId;
    String propertyName;    //楼盘名称
    String title;   //房屋描述标题
    String address; //地址

    String stress;  //地址（街道）

    List<String> images;    //图片
    List<String> label; //标签
    int price;  //售价
    double area;    //面积
    //    int floor;  //楼层
//    int totalFloor; //总楼层
    String jzlx;    //建筑类型
    String zxqk;    //装修情况
    String cx;  //朝向
    String cqnx;    //产权年限
    String jznd;    //建筑年代
    List<String> ptss;  //配套设施
    String remark;  //描述
    Location loc;   //大概是位置吧
    String mapUrl;  //位置图片
    String huxing;  //户型
    public String getHuxingImage() {
        return huxingImage;
    }

    public void setHuxingImage(String huxingImage) {
        this.huxingImage = huxingImage;
    }

    String huxingImage;   //户型图片

    String ownerId; //发布人Id
    String mobile;  //发布者的预留电话
    String nickname;    //发布人昵称
    int type;   //上架0 下架1
    int isCheck; //-1:未通过审核  0:审核中  1:通过审核
    public String getHouseNum() {
        return houseNum;
    }

    public void setHouseNum(String houseNum) {
        this.houseNum = houseNum;
    }

    public String getBuildName() {
        return buildName;
    }

    public void setBuildName(String buildName) {
        this.buildName = buildName;
    }

    String houseNum;    //房号

    String buildName;   //楼栋号
    public int getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(int isCheck) {
        this.isCheck = isCheck;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

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

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStress() {
        return stress;
    }

    public void setStress(String stress) {
        this.stress = stress;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<String> getLabel() {
        return label;
    }

    public void setLabel(List<String> label) {
        this.label = label;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }
//
//    public int getFloor() {
//        return floor;
//    }
//
//    public void setFloor(int floor) {
//        this.floor = floor;
//    }
//
//    public int getTotalFloor() {
//        return totalFloor;
//    }
//
//    public void setTotalFloor(int totalFloor) {
//        this.totalFloor = totalFloor;
//    }

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

    public String getCx() {
        return cx;
    }

    public void setCx(String cx) {
        this.cx = cx;
    }

    public String getCqnx() {
        return cqnx;
    }

    public void setCqnx(String cqnx) {
        this.cqnx = cqnx;
    }

    public String getJznd() {
        return jznd;
    }

    public void setJznd(String jznd) {
        this.jznd = jznd;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getHuxing() {
        return huxing;
    }

    public void setHuxing(String huxing) {
        this.huxing = huxing;
    }

//    public String getHxPic() {
//        return hxPic;
//    }
//
//    public void setHxPic(String hxPic) {
//        this.hxPic = hxPic;
//    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public List<String> getPtss() {
        return ptss;
    }

    public void setPtss(List<String> ptss) {
        this.ptss = ptss;
    }

    public Location getLoc() {
        return loc;
    }

    public void setLoc(Location loc) {
        this.loc = loc;
    }

    public String getMapUrl() {
        return mapUrl;
    }

    public void setMapUrl(String mapUrl) {
        this.mapUrl = mapUrl;
    }


}
