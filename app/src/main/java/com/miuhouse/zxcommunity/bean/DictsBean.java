package com.miuhouse.zxcommunity.bean;

import java.io.Serializable;

/**
 * Created by kings on 3/30/2016.
 */
public class DictsBean extends BaseBean implements Serializable {
    private SaleRoomDictBean dicts;

    public SaleRoomDictBean getDicts() {
        return dicts;
    }

    public void setDicts(SaleRoomDictBean dicts) {
        this.dicts = dicts;
    }
}
