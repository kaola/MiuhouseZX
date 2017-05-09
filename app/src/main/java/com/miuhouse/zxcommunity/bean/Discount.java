package com.miuhouse.zxcommunity.bean;

import java.io.Serializable;

/** 楼盘优惠
 * Created by khb on 2016/5/4.
 */
public class Discount implements Serializable{
    String id;
    String information; //优惠信息
    String newPropertyId;   //关联小区的id

    public String getNewPropertyName() {
        return newPropertyName;
    }

    public void setNewPropertyName(String newPropertyName) {
        this.newPropertyName = newPropertyName;
    }

    String newPropertyName; //小区名

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getNewPropertyId() {
        return newPropertyId;
    }

    public void setNewPropertyId(String newPropertyId) {
        this.newPropertyId = newPropertyId;
    }

    @Override
    public String toString() {
        return "Discount{" +
                "id='" + id + '\'' +
                ", information='" + information + '\'' +
                ", newPropertyId='" + newPropertyId + '\'' +
                ", newPropertyName='" + newPropertyName + '\'' +
                '}';
    }
}
