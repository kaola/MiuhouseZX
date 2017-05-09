package com.miuhouse.zxcommunity.bean;

import java.util.List;

/**
 * Created by khb on 2016/12/20.
 */
public class Complain {
    public static final int REPAIR = 1;
    public static final int COMPLAIN = 2;
    public static final int ADVICE = 3;
    public static final int LIKE = 4;

    String id;
    long propertyId;
    String ownerId;
    String content;
    long createTime;
    List<String> images;
    int type;

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

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
