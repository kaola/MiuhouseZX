package com.miuhouse.zxcommunity.bean;

import java.io.Serializable;

/**
 * 资讯
 *
 * @author kings
 */
public class NewsInfoBean implements Serializable {

    //newsType为0表示社会新闻，为1表示小区新闻
    public static final int PROPERTY_NEWS = 1;
    public static final int WORLD_NEWS = 0;
    private int newsType;
    private String id;
    private String title;
    private String image;
    private String createTime;
    private String originalUrl;
    private String source;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNewsType() {
        return newsType;
    }

    public void setNewsType(int newsType) {
        this.newsType = newsType;
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

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

}
