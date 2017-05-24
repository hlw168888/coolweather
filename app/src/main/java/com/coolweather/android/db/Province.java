package com.coolweather.android.db;

import org.litepal.crud.DataSupport;

/**
 * Created by hlw on 2017/5/23.
 */

public class Province extends DataSupport {

    private String provinceName;
    private String countryName;


    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}
