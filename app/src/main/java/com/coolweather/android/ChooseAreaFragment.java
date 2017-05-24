package com.coolweather.android;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.coolweather.android.db.City;
import com.coolweather.android.db.County;
import com.coolweather.android.db.Province;
import com.coolweather.android.util.HttpUtil;
import com.coolweather.android.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by hlw on 2017/5/23.
 */

public class ChooseAreaFragment extends Fragment {
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;
    private ProgressDialog progressDialog;
    private TextView titleText;
    private Button backButton;
    private ListView listView;
    private ArrayAdapter<String>adapter;
    private List<String > dataList = new ArrayList<>();
    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;
    private County selectedCounty;
    private Province selectedProvince;
    private City selectedCity;
    private int currentLevel;

    private  int datafrom=1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area,container,false);
        titleText = (TextView)view.findViewById(R.id.title_text);
        backButton = (Button)view.findViewById(R.id.back_button);
        listView = (ListView)view.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == LEVEL_PROVINCE)
                {
                    selectedProvince = provinceList.get(position);
                    queryCities(selectedProvince.getProvinceName());
                }
                else if (currentLevel == LEVEL_CITY)
                {
                    selectedCity = cityList.get(position);
                    queryCounties(selectedCity.getCityName());
                }
                else if (currentLevel == LEVEL_COUNTY)
                {
                    selectedCounty = countyList.get(position);

                }

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLevel == LEVEL_COUNTY)
                {
                    queryCities(selectedProvince.getProvinceName());

                }
                else if (currentLevel == LEVEL_CITY)
                {
                    queryProvinces("中国");
                }

            }
        });

        queryProvinces("中国");
    }



    private void queryFromServer(String address, final String parentValue, final String type) {
        showProgressDialog();
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String  responseText = response.body().string();
                //System.out.println(type);
                //System.out.println(responseText);
                boolean result = false;
                if ("province".equals(type))
                {
                    result = Utility.handleProvinceResponse(responseText,parentValue);
                }
                else  if ("city".equals(type))
                {
                    result = Utility.handleCityResponse(responseText,selectedProvince.getProvinceName());
                }
                else  if ("county".equals(type))
                {
                    result = Utility.handleCountyResponse(responseText,selectedCity.getCityName());
                }

                if (result)
                {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type))
                            {
                                queryProvinces(parentValue);
                            }
                            else if ("city".equals(type))
                            {
                                queryCities(parentValue);
                            }
                            else if ("county".equals(type))
                            {
                                queryCounties(parentValue);
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(),"加载失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void showProgressDialog() {
        if (progressDialog == null)
        {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载......");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }
    private void closeProgressDialog() {
        if (progressDialog != null)
        {
            progressDialog.dismiss();
        }
    }

    private void queryProvinces(String parentValue) {
        titleText.setText(parentValue);
        backButton.setVisibility(View.GONE);
        provinceList = DataSupport.where("countryName = ?",parentValue).find(Province.class);
        if (provinceList.size()>0 && datafrom==1)
        {
            dataList.clear();
            for (Province province : provinceList)
            {
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        }
        else
        {
            String address = "https://cdn.heweather.com/china-city-list.json";
            queryFromServer(address,"中国","province");
        }
    }

    private void queryCities(String parentValue) {
        titleText.setText(parentValue);
        backButton.setVisibility(View.VISIBLE);
        cityList = DataSupport.where("provinceName = ?",parentValue).find(City.class);
        if (cityList.size() > 0 && datafrom==1)
        {
            dataList.clear();
            for (City city : cityList)
            {
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_CITY;
        }
        else
        {

            String address = "https://cdn.heweather.com/china-city-list.json";

            queryFromServer(address,parentValue,"city");
        }
    }

    private void queryCounties(String parentValue) {
        titleText.setText(parentValue);
        backButton.setVisibility(View.VISIBLE);
        countyList = DataSupport.where("cityName = ?",parentValue).find(County.class);
        if (countyList.size() > 0 && datafrom==1)
        {
            dataList.clear();
            for (County county : countyList)
            {
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_COUNTY;
        }
        else
        {

            String address = "https://cdn.heweather.com/china-city-list.json";

            queryFromServer(address,parentValue,"county");
        }
    }
}
