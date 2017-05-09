package com.miuhouse.zxcommunity.bean;

import java.io.Serializable;

/**
 * Created by kings on 3/16/2016.
 */
public class HistoryPassportBean implements Serializable {
    private String visitorName;
    private long visitTime;

    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }

    public long getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(long visitTime) {
        this.visitTime = visitTime;
    }
}
