package com.miuhouse.zxcommunity.bean;

/**
 * Created by khb on 2016/1/8.
 */
public class Estate {
    private String id;
    /**
     * 所在城市
     */
    private String city;
    /**
     * 小区名称
     */
    private String name;
    /**
     * 小区代码
     */
    private int cityId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    @Override
    public String toString() {
        return "Estate{" +
                "id='" + id + '\'' +
                ", city='" + city + '\'' +
                ", name='" + name + '\'' +
                ", cityId=" + cityId +
                '}';
    }
}
