package com.miuhouse.zxcommunity.bean;

import java.util.List;

public class NewInfoListBean {

    private int code;
    private String msg;
    private List<NewsInfoBean> newsInfos;
    private List<NewsInfoBean> otherInfos;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<NewsInfoBean> getNewsInfos() {
        return newsInfos;
    }

    public void setNewsInfos(List<NewsInfoBean> newsInfos) {
        this.newsInfos = newsInfos;
    }

    public List<NewsInfoBean> getOtherInfos() {
        return otherInfos;
    }

    public void setOtherInfos(List<NewsInfoBean> otherInfos) {
        this.otherInfos = otherInfos;
    }
}
