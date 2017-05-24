package com.coolweather.android.db;

import org.litepal.crud.DataSupport;

/**
 * Created by hlw on 2017/5/23.
 */

public class County extends DataSupport {
    private int id;
    private String countyName;
    private String countyCode;
    private String cityName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getCountyCode() {
        return countyCode;
    }

    public void setCountyCode(String countyCode) {
        this.countyCode = countyCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
