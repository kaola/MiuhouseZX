package com.miuhouse.zxcommunity.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by kings on 1/11/2016.
 */
public class PropertyListBean extends BaseBean implements Serializable {
    private ArrayList<Property> list;

    public ArrayList<Property> getList() {
        return list;
    }

    public void setList(ArrayList<Property> list) {
        this.list = list;
    }
}
