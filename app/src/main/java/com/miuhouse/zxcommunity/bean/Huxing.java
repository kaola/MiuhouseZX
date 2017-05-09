package com.miuhouse.zxcommunity.bean;

import java.util.ArrayList;

/** 户型信息
 * Created by khb on 2016/5/4.
 */
public class Huxing {
    String id;
    ArrayList<String> images;   //户型图片
    String title;   //户型标题
    String apartment;   //户型
    double area;    //面积
    String zxqk;    //装修情况
    String dfl; //得房率
    int price;  //价格 元/平米
    String cx;  //朝向
    long createTime;    //创建时间
    String newPropertyId;   //关联小区的id
    String comment; //户型点评

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public String getZxqk() {
        return zxqk;
    }

    public void setZxqk(String zxqk) {
        this.zxqk = zxqk;
    }

    public String getDfl() {
        return dfl;
    }

    public void setDfl(String dfl) {
        this.dfl = dfl;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCx() {
        return cx;
    }

    public void setCx(String cx) {
        this.cx = cx;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getNewPropertyId() {
        return newPropertyId;
    }

    public void setNewPropertyId(String newPropertyId) {
        this.newPropertyId = newPropertyId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Huxing{" +
                "id='" + id + '\'' +
                ", images=" + images +
                ", title='" + title + '\'' +
                ", apartment='" + apartment + '\'' +
                ", area=" + area +
                ", zxqk='" + zxqk + '\'' +
                ", dfl='" + dfl + '\'' +
                ", price=" + price +
                ", cx='" + cx + '\'' +
                ", createTime=" + createTime +
                ", newPropertyId='" + newPropertyId + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
