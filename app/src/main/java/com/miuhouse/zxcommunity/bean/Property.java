package com.miuhouse.zxcommunity.bean;

import java.io.Serializable;

/**
 * Created by kings on 1/11/2016.
 */
public class Property implements Serializable {

    private long id;
    private String city;
    private int cityId;
    private String name;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
