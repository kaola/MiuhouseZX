package com.miuhouse.zxcommunity.bean;

/**
 * Created by kings on 3/23/2016.
 */
public class IndexImages {
    //    "id": "9aa3c17d-4384-4db0-b1df-f2076e3bfc84",
//            "propertyId": 4,
//            "type": 1,
//            "title": "优惠券",
//            "description": "优惠不断抢不停",
//            "image": "http://img.miuhouse.com/upload/indexImage/20160317/d0debeaf-a85e-4c31-a9a2-91a73e87ac7d.png"
    private String id;
    private long propertyId;
    private int type;
    private String title;
    private String description;
    private String image;

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
