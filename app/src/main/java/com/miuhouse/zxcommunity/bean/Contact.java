package com.miuhouse.zxcommunity.bean;

/**
 * Created by khb on 2016/3/23.
 */
public class Contact {
    private String hxid;
    private String head;
    private String nickname;

    public String getHxid() {
        return hxid;
    }

    public void setHxid(String hxid) {
        this.hxid = hxid;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "hxid='" + hxid + '\'' +
                ", head='" + head + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
