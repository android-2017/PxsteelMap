package com.pxsteel.pxsteelmap;

import android.app.Activity;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

/**
 * Created by YJ on 2017/7/6.
 */

public class encodingActivity extends Activity {

    private Button encoding;
    private Button Fencoding;

    private EditText mcity, maddress, mx, my;
    private TextView result;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.encoding);

        initView();

        findViewById(R.id.encoding).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //正编码
                //新建编码查询对象
                GeoCoder geocoder = GeoCoder.newInstance();
                //新建查询条件
                GeoCodeOption geo = new GeoCodeOption().city(mcity.getText().toString())
                        .address(maddress.getText().toString());
                //请求编码
                geocoder.geocode(geo);
                //监听
                geocoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
                    /**
                     *  正编码
                     * @param geoCodeResult
                     */
                    @Override
                    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
                        result.setText("结果：\n" + geoCodeResult.getLocation()
                                + "\n经度：" + geoCodeResult.getLocation().latitude
                                + "\n纬度：" + geoCodeResult.getLocation().longitude);
                    }

                    @Override
                    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                    }
                });
            }
        });
        findViewById(R.id.Fencoding).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double x = new Double(mx.getText().toString());
                double y = new Double(my.getText().toString());
                //反编码
                //新建编码查询对象
                GeoCoder geocoder = GeoCoder.newInstance();
                //新建查询条件
                ReverseGeoCodeOption reverse = new ReverseGeoCodeOption()
                        .location(new LatLng(x,y));
                geocoder.reverseGeoCode(reverse);
                geocoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
                    @Override
                    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
                    }
                    /**
                     * 反编码
                     * @param reverseGeoCodeResult
                     */
                    @Override
                    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                        result.setText("结果：\n" + reverseGeoCodeResult.getAddress());
                    }
                });

            }
        });
    }

    private void initView() {
        mcity = (EditText) findViewById(R.id.mcity);
        maddress = (EditText) findViewById(R.id.maddress);
        mx = (EditText) findViewById(R.id.mx);
        my = (EditText) findViewById(R.id.my);
        result = (TextView) findViewById(R.id.result);
    }
}
