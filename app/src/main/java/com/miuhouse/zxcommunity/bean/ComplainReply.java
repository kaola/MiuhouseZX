package com.miuhouse.zxcommunity.bean;

/**
 * Created by khb on 2016/12/22.
 */
public class ComplainReply {
    String repairComplainId;
    String ownerId;
    String content;
    long createTime;

    public String getRepairComplainId() {
        return repairComplainId;
    }

    public void setRepairComplainId(String repairComplainId) {
        this.repairComplainId = repairComplainId;
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
}
