package com.miuhouse.zxcommunity.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by kings on 3/30/2016.
 */
public class SaleRoomDictBean implements Serializable {
    private List<String> cx; //朝向
    private List<String> label;//标签
    private List<String> zxqk;//室内装修
    private List<String> jzlx;//建筑类型
    private List<String> fx;//房型
    private List<String> price;//价格
    private List<String> floorAge;//楼龄
    private List<String> esfPrice;//买房价格
    private List<String> zfPrice;//租房价格

    public List<String> getZfPrice() {
        return zfPrice;
    }

    public void setZfPrice(List<String> zfPrice) {
        this.zfPrice = zfPrice;
    }

    public List<String> getEsfPrice() {
        return esfPrice;
    }

    public void setEsfPrice(List<String> esfPrice) {
        this.esfPrice = esfPrice;
    }

    public List<String> getCx() {
        return cx;
    }

    public void setCx(List<String> cx) {
        this.cx = cx;
    }

    public List<String> getLabel() {
        return label;
    }

    public void setLabel(List<String> label) {
        this.label = label;
    }

    public List<String> getZxqk() {
        return zxqk;
    }

    public void setZxqk(List<String> zxqk) {
        this.zxqk = zxqk;
    }

    public List<String> getJzlx() {
        return jzlx;
    }

    public void setJzlx(List<String> jzlx) {
        this.jzlx = jzlx;
    }

    public List<String> getFx() {
        return fx;
    }

    public void setFx(List<String> fx) {
        this.fx = fx;
    }

    public List<String> getPrice() {
        return price;
    }

    public void setPrice(List<String> price) {
        this.price = price;
    }

    public List<String> getFloorAge() {
        return floorAge;
    }

    public void setFloorAge(List<String> floorAge) {
        this.floorAge = floorAge;
    }
}
