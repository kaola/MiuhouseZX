package com.miuhouse.zxcommunity.bean;

import java.io.Serializable;

/**
 * Created by kings on 4/8/2016.
 */
public class HuodongBean implements Serializable {

    private int id;
    private String image;
    private String headUrl;

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
