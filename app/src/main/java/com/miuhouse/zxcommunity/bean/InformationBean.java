package com.miuhouse.zxcommunity.bean;

import java.io.Serializable;

/**
 * 资讯
 * @author kings
 *
 */
public class InformationBean implements Serializable{

    private String id;
    private String title;
    private String image;
    private String createTime;
    private String commentSize;
    private String likeSize;
    private String likeId;
    private int propertyId;
    private String description;
    private String type;
    private String typeName;
    
    public String getCommentSize() {
        return commentSize;
    }
    public void setCommentSize(String commentSize) {
        this.commentSize = commentSize;
    }
    public String getLikeSize() {
        return likeSize;
    }
    public void setLikeSize(String likeSize) {
        this.likeSize = likeSize;
    }
    public String getLikeId() {
        return likeId;
    }
    public void setLikeId(String likeId) {
        this.likeId = likeId;
    }
    public int getPropertyId() {
        return propertyId;
    }
    public void setPropertyId(int propertyId) {
        this.propertyId = propertyId;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getTypeName() {
        return typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public String getCreateTime() {
        return createTime;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    
}
