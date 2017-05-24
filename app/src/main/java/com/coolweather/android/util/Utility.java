package com.coolweather.android.util;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.coolweather.android.db.City;
import com.coolweather.android.db.County;
import com.coolweather.android.db.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by hlw on 2017/5/23.
 */

public class Utility {
    public static boolean handleProvinceResponse(String response,String parentValue)
    {
        Log.d("hlw",""+parentValue);
        HashMap map = new HashMap();
        if (!TextUtils.isEmpty(response))
        {
            try {
                JSONArray allProvinces = new JSONArray(response);;
                for (int i=0;i<allProvinces.length();i++)
                {
                    JSONObject provinceObject = allProvinces.getJSONObject(i);
                    String countryName = provinceObject.getString("countryZh");
                    String provinceName=provinceObject.getString("provinceZh");
                    boolean flag=map.containsKey(provinceName);
                   if (flag==false && countryName.equals(parentValue)) {
                       map.put(provinceName,countryName);
                       Log.d("hlw", "countryName:" + countryName + "          provinceName:" + provinceName);
                       // }

                       Province province = new Province();
                       //Object key = iterator.next();
                       //Object val = map.get(key);
                       province.setProvinceName(provinceName);
                       province.setCountryName(countryName);
                       province.save();
                   }
                }


                return true;
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean handleCityResponse(String response,String provincevcName)
    {

        HashMap map = new HashMap();
        if (!TextUtils.isEmpty(response))
        {
            try {
                JSONArray allProvinces = new JSONArray(response);;
                for (int i=0;i<allProvinces.length();i++)
                {
                    JSONObject provinceObject = allProvinces.getJSONObject(i);

                    String provinceZh = provinceObject.getString("provinceZh");
                    String cityCode =provinceObject.getString("id");
                    String cityName = provinceObject.getString("leaderZh");
                    boolean flag=map.containsKey(cityName);
                    Log.d("hlw","provinceZh:"+provinceZh +"     provincevcName:"+provincevcName+"   cityName:"+cityName);
                    if (flag==false && provinceZh.equals(provincevcName))
                    {
                        map.put(cityName,cityCode);
                        Log.d("hlw",""+cityName);
                        City city = new City();
                        city.setCityName(cityName);
                        city.setCityCode(cityCode);
                        city.setProvinceName(provincevcName);
                        city.save();
                    }
                }

                return true;
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean handleCountyResponse(String response,String cityName)
    {
        Log.d("hlw",""+cityName);
        HashMap map = new HashMap();
        if (!TextUtils.isEmpty(response))
        {
            try {
                JSONArray allProvinces = new JSONArray(response);;
                for (int i=0;i<allProvinces.length();i++)
                {
                    JSONObject provinceObject = allProvinces.getJSONObject(i);

                    String leaderZh = provinceObject.getString("leaderZh");
                    String countyName = provinceObject.getString("cityZh");
                    String countyCode = provinceObject.getString("id");
                    boolean flag=map.containsKey( countyName);
                    if (flag==false && leaderZh.equals(cityName))
                    {
                        map.put(countyName,countyCode);
                        Log.d("hlw",""+provinceObject.getString("cityZh"));
                        County county = new County();
                        county.setCountyName( countyName);
                        county.setCountyCode(countyCode);
                        county.setCityName(cityName);
                        county.save();
                    }
                }


                return true;
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        return false;
    }
}
