package com.miuhouse.zxcommunity.bean;

/**
 * Created by khb on 2015/12/30.
 */
public class TestNews {
    /**
     * "id": "dd0f91e8-f280-495f-a6c2-8c0a4a52f429",
     "title": "疯狂的足球！英门将足总杯倒钩绝平震惊英伦",
     "image": null,
     "createTime": 1443441049869,
     "originalUrl": "http://sports.qq.com/a/20150928/002559.htm",
     "source": "腾讯网"

     */
    String id;
    String title;
    String image;
    long createTime;
    String originalUrl;
    String source;

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

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
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
