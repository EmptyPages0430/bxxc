package com.jgkj.bxxc.tools;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;


/**
 * 初始化BaiduMap
 */
public class MapBase{
	// 定位相关
    public LocationClient mLocClient;
    
    public MyLocationListenner myListener = new MyLocationListenner();
    public LocationMode mCurrentMode;
    public BitmapDescriptor mCurrentMarker;
    public static final int accuracyCircleFillColor = 0xAAFFFF88;
    public static final int accuracyCircleStrokeColor = 0xAA00FF00;

    public MapView mMapView;
    public BaiduMap mBaiduMap;
    public Button btn;

 // UI相关
    public OnCheckedChangeListener radioButtonListener;
    public Button requestLocButton,button2;
    boolean isFirstLoc = true; // 是否首次定位
	public void mapView(Context context, View view) {
		  // 地图初始化
        mMapView = (MapView) view;
        mBaiduMap = mMapView.getMap();
     // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(context);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mCurrentMode = LocationMode.FOLLOWING;
        mBaiduMap
                .setMyLocationConfigeration(new MyLocationConfiguration(
                        mCurrentMode, true, mCurrentMarker));
        mLocClient.start();
	}

	

	/**
	 * 定位SDK监听函数
	 */

public class MyLocationListenner implements BDLocationListener {

    @Override
	public void onReceiveLocation(BDLocation location) {
    	// map view 销毁后不在处理新接收的位置
        if (location == null || mMapView == null) {
            return;
        }
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(location.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100).latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();
        mBaiduMap.setMyLocationData(locData);
        if (isFirstLoc) {
            isFirstLoc = false;
            LatLng ll = new LatLng(location.getLatitude(),
                    location.getLongitude());
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(ll).zoom(18.0f);
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        }
    }

    public void onReceivePoi(BDLocation poiLocation) {
    }
}


}
