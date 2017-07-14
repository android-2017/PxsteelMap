package com.pxsteel.pxsteelmap;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;

/**
 * Created by YJ on 2017/7/6.
 */

public class PoiActivity extends Activity {
    private Button pio;
    private TextView tv_result;
    private PoiSearch mPoiSearch;

    private EditText mcity;
    private EditText mkeyword;
    private EditText mpageNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.pio);

        initView();

    }

    private void initView() {
        pio = (Button) findViewById(R.id.pio);
        tv_result = (TextView) findViewById(R.id.tv_result);
        mcity = (EditText) findViewById(R.id.mcity);
        mkeyword = (EditText) findViewById(R.id.mkeyword);
        mpageNum = (EditText) findViewById(R.id.mpageNum);

        pio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_result.setText("");
//        第一步，创建POI检索实例
                mPoiSearch = PoiSearch.newInstance();
//        第二步，创建POI检索监听者；
                OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener() {
                    public void onGetPoiResult(PoiResult result) {
                        //获取POI检索结果
                        if (result.error == SearchResult.ERRORNO.NO_ERROR) {// 检索结果正常返回
                            int i = 1;
                            for (PoiInfo info : result.getAllPoi()) {
                                tv_result.append(i + "、名称：" + info.name
                                        + "\n电话：" + info.phoneNum
                                        + "\n地址：" + info.address
                                        + "\n经度：" + info.location.latitude
                                        + "\n纬度：" + info.location.longitude + "\n"
                                );
                                i++;
                            }
                        }
                    }

                    public void onGetPoiDetailResult(PoiDetailResult result) {
                        //获取Place详情页检索结果
                    }

                    @Override
                    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
                    }
                };
//        第三步，设置POI检索监听者；
                mPoiSearch.setOnGetPoiSearchResultListener(poiListener);
//        第四步，发起检索请求；
                mPoiSearch.searchInCity((new PoiCitySearchOption())
                        .city(mcity.getText().toString())
                        .keyword(mkeyword.getText().toString())
                        .pageNum(Integer.parseInt(mpageNum.getText().toString())));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            //        第五步，释放POI检索实例；
            mPoiSearch.destroy();
        } catch (Exception e) {
        }
    }
}
