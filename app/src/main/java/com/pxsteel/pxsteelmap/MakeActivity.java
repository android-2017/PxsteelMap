package com.pxsteel.pxsteelmap;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;

/**
 * Created by YJ on 2017/7/6.
 */

public class MakeActivity extends Activity {

    private MapView mMapView;
    private BaiduMap mBaiduMap;

    private Button btn1;//标  注
    private Button btn2;//显示圆
    private Button btn3;//文字
    private Button btn4;//弹出窗口
    private Button btn5;

    private View pop;
    private TextView tv;

    private Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SDKInitializer.initialize(getApplicationContext());

        setContentView(R.layout.make);

        initView();
        initLocation();

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMarker(27.6496485773, 113.8594900005);//萍乡 秋收起义
                //28.2440762271,113.0597814595 长沙 世界之窗
                //27.6496485773,113.8594900005 萍乡 秋收起义
                //28.6885984562,115.8688118847 南昌 秋水广场
                //27.6385023428,113.7435619762 萍乡 萍钢大厦
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCircle(27.6496485773, 113.8594900005);//萍乡 秋收起义
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showText(27.6496485773, 113.8594900005);//萍乡 秋收起义
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng llText = new LatLng(27.6496485773, 113.8594900005);//萍乡 秋收起义
                displayInfoWindows(llText);
            }
        });
    }

    /**
     * 显示弹出窗口覆盖物
     */
    private void displayInfoWindows(final LatLng latLng) {
        // 创建infowindow展示的view
        Button btn = new Button(getApplicationContext());
        btn.setBackgroundResource(R.drawable.popup);
        btn.setText("~点点我吧~");
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
                .fromView(btn);
        // infowindow点击事件
        InfoWindow.OnInfoWindowClickListener infoWindowClickListener = new InfoWindow.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick() {
                reverseGeoCode(latLng);
                //隐藏InfoWindow
                mBaiduMap.hideInfoWindow();
            }
        };
        // 创建infowindow
        InfoWindow infoWindow = new InfoWindow(bitmapDescriptor, latLng, -47,
                infoWindowClickListener);
        // 显示InfoWindow
        mBaiduMap.showInfoWindow(infoWindow);
    }

    private void showText(double x, double y) {
        //定义文字所显示的坐标点
        LatLng llText = new LatLng(x, y);
        //构建文字Option对象，用于在地图上添加文字
        OverlayOptions textOption = new TextOptions()
                .bgColor(0xAAFFFF00)
                .fontSize(24)
                .fontColor(0xFFFF00FF)
                .text("百度地图SDK")
                .rotate(-30)
                .position(llText);
        //在地图上添加该文字对象并显示
        mBaiduMap.addOverlay(textOption);
    }

    private void showCircle(double x, double y) {

        LatLng point = new LatLng(x, y);
        CircleOptions circle = new CircleOptions();
        circle.center(point);//中心
        circle.radius(1000);//半径 米
        circle.fillColor(0x60FF0000);//颜色
        circle.stroke(new Stroke(10, 0xAA00FF00));//边框 米
        mBaiduMap.addOverlay(circle);

    }

    /**
     * 初始地点
     */
    private void initLocation() {
        //定义Maker坐标点
        LatLng point = new LatLng(27.6496485773, 113.8594900005);//萍乡 秋收起义
        MapStatusUpdate mapUpdate = MapStatusUpdateFactory.newLatLng(point);
        mBaiduMap.setMapStatus(mapUpdate);

        mMapView.showZoomControls(true);//缩放
        mMapView.showScaleControl(false);//比例尺
    }

    /**
     * 标注并定位
     *
     * @param x
     * @param y
     */
    public void showMarker(double x, double y) {
        //定义Maker坐标点
        LatLng point = new LatLng(x, y);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_gcoding);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .zIndex(9)  //设置marker所在层级
                .draggable(true)  //设置手势拖拽
                .icon(bitmap);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);
        MapStatusUpdate mapUpdate = MapStatusUpdateFactory.newLatLng(point);
        mBaiduMap.setMapStatus(mapUpdate);
    }

    private void initView() {

        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);
        btn5 = (Button) findViewById(R.id.btn5);

        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public boolean onMapPoiClick(MapPoi arg0) {
                return false;
            }

            @Override
            public void onMapClick(LatLng latLng) {
                displayInfoWindows(latLng);
            }
        });

        mBaiduMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker arg0) {
            }

            @Override
            public void onMarkerDragEnd(Marker arg0) {
                Toast.makeText(
                        MakeActivity.this,
                        "拖拽结束，新位置：" + arg0.getPosition().latitude + ", "
                                + arg0.getPosition().longitude,
                        Toast.LENGTH_LONG).show();
                reverseGeoCode(arg0.getPosition());
            }

            @Override
            public void onMarkerDrag(Marker arg0) {
            }
        });
    }

    /**
     * 反地理编码得到地址信息
     */
    private void reverseGeoCode(LatLng latLng) {
        // 创建地理编码检索实例
        GeoCoder geoCoder = GeoCoder.newInstance();
        OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
            // 反地理编码查询结果回调函数
            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                if (result == null
                        || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    // 没有检测到结果
                    Toast.makeText(MakeActivity.this, "抱歉，未能找到结果",
                            Toast.LENGTH_LONG).show();
                }
                Toast.makeText(MakeActivity.this,
                        "位置：" + result.getAddress(), Toast.LENGTH_LONG)
                        .show();
            }

            // 地理编码查询结果回调函数
            @Override
            public void onGetGeoCodeResult(GeoCodeResult result) {
                if (result == null
                        || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    // 没有检测到结果
                }
            }
        };
        // 设置地理编码检索监听者
        geoCoder.setOnGetGeoCodeResultListener(listener);
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
        // 释放地理编码检索实例
        // geoCoder.destroy();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
}
