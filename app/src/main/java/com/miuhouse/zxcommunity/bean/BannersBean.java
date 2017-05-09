package com.miuhouse.zxcommunity.bean;

import java.io.Serializable;

/**
 * Created by kings on 3/21/2016.
 */
public class BannersBean implements Serializable {
    private String id;
    private String image;
    private String typeName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
