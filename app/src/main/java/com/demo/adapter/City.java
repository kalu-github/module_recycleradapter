package com.demo.adapter;

public class City {
    private String name;    //城市名
    private String province;    //所属省份
    private int icon;    //所属省份

    public City(String name, String province, int icon) {
        this.name = name;
        this.province = province;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}