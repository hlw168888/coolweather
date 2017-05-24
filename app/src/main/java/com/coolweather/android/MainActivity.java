package com.coolweather.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.coolweather.android.db.City;
import com.coolweather.android.db.County;
import com.coolweather.android.db.Province;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Connector.getDatabase();
        DataSupport.deleteAll(Province.class,"id>=0");
        DataSupport.deleteAll(City.class,"id>=0");
        DataSupport.deleteAll(County.class,"id>=0");
        setContentView(R.layout.activity_main);
    }
}
