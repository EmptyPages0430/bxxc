package com.jgkj.bxxc;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.google.gson.Gson;
import com.jgkj.bxxc.adapter.MyCoachAdapter;
import com.jgkj.bxxc.adapter.SchoolPlaceAdapter;
import com.jgkj.bxxc.bean.SchoolPlaceTotal;
import com.jgkj.bxxc.bean.SchoolShow;
import com.jgkj.bxxc.tools.MyOrientationListener;
import com.jgkj.bxxc.tools.PictureOptimization;
import com.jgkj.bxxc.tools.SelectPopupWindow;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class PlaceChooseActivity extends Activity implements
        OnClickListener, AdapterView.OnItemClickListener {
    // 定位相关
    public LocationClient mLocClient;
    public LocationMode mCurrentMode;
    public BitmapDescriptor mCurrentMarker;
    public static final int accuracyCircleFillColor = 0xAAFFFF88;
    public static final int accuracyCircleStrokeColor = 0xAA00FF00;

    private final MyLocationListenner myListener = new MyLocationListenner();
    public MapView mMapView;
    public BaiduMap mBaiduMap;
    //方向传感器
    private MyOrientationListener myOrientationListener;
    private int mXDirection;
    private double mCurrentLantitude, mCurrentLongitude;
    private float mCurrentAccracy;
    //popupWindow
    private SelectPopupWindow mPopupWindow = null;
    // UI相关
    boolean isFirstLoc = true; // 是否首次定位

    private Button btn_forward;
    private TextView title;
    //listView
    //教练排序展示
    private ListView listView;
    private MyCoachAdapter adapter;
    private Button place;
    private String[] city = new String[0];
    //请求url
    private String url = "http://www.baixinxueche.com/index.php/Home/Api/Coach";
    private int page = 1;
    //上拉刷新
    //Marker地图标签
    private LatLng point;
    private Marker mMarker;
    private String str;
    private InfoWindow mInfoWindow;

    private SchoolPlaceAdapter schAdapter;
    private List<SchoolShow> showList = new ArrayList<>();
    private SchoolShow schoolShow;
    private SchoolPlaceTotal schoolPlaceTotal;

    private Bitmap bitmap;
    private String placePath = "http://www.baixinxueche.com/index.php/Home/Apitoken/Apiarea";
    private BitmapDescriptor bitmapDefault,bitmapA,bitmapB,bitmapC,bitmapD,bitmapE,
            bitmapF,bitmapG,bitmapH,bitmapI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.placechoose);
        //初始化控件
        init();
        //初始化百度地图
        initMap();
        //获取场地信息，然后吧场地信息和百度地图上的marker对应起来进行排序
        getPlace();
    }

    private void getPlace() {
        OkHttpUtils
                .get()
                .url(placePath)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        Toast.makeText(PlaceChooseActivity.this, "请检查网络", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        listView.setTag(s);
                        if (listView.getTag() != null) {
                            addAdapter();
                        } else {
                            Toast.makeText(PlaceChooseActivity.this, "网络状态不佳,请检查网络", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void addAdapter() {
        Gson gson = new Gson();
        String str = listView.getTag().toString();
        schoolPlaceTotal = gson.fromJson(str, SchoolPlaceTotal.class);
        if (schoolPlaceTotal.getCode() == 200) {
            setPup(schoolPlaceTotal.getResult());
            setMyAdapter("0");
        } else {
            Toast.makeText(PlaceChooseActivity.this, schoolPlaceTotal.getReason(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setMyAdapter(String sid) {
        showList.clear();
        mBaiduMap.clear();
        List<SchoolPlaceTotal.Result> list1 = schoolPlaceTotal.getResult();
        List<SchoolPlaceTotal.Result.Res> listRes = list1.get(0).getResult();
        if (sid.equals("0")) {
            for (int i = 0; i < listRes.size(); i++) {
                allMarker();
                addShowList(i,listRes);
            }
            sort();
        } else {
            for (int i = 1; i < list1.size(); i++) {
                if (sid.equals(list1.get(i).getSid())) {
                    reMarker(i);
                    addShowClickList(i,list1);
                }
            }
        }
        schAdapter = new SchoolPlaceAdapter(PlaceChooseActivity.this, showList);
        listView.setAdapter(schAdapter);
    }

    /**
     * 重新标记
     * @param i
     */
    private void reMarker(int i){

        List<SchoolPlaceTotal.Result.Res> list1 = schoolPlaceTotal.getResult().get(i).getResult();
        for (int j = 0; j < list1.size(); j++) {
            double latitude = Double.parseDouble(list1.get(j).getLatitude());
            double longitude = Double.parseDouble(list1.get(j).getLongitude());
            point = new LatLng(latitude, longitude);
            OverlayOptions option = null;
            switch (j) {
                case 0:
                    option = new MarkerOptions().position(point).zIndex(j).icon(
                            bitmapA);
                    break;
                case 1:
                    option = new MarkerOptions().position(point).zIndex(j).icon(
                            bitmapB);
                    break;
                case 2:
                    option = new MarkerOptions().position(point).zIndex(j).icon(
                            bitmapC);
                    break;
                case 3:
                    option = new MarkerOptions().position(point).zIndex(j).icon(
                            bitmapD);
                    break;
                case 4:
                    option = new MarkerOptions().position(point).zIndex(j).icon(
                            bitmapE);
                    break;
                case 5:
                    option = new MarkerOptions().position(point).zIndex(j).icon(
                            bitmapF);
                    break;
                case 6:
                    option = new MarkerOptions().position(point).zIndex(j).icon(
                            bitmapG);
                    break;
                case 7:
                    option = new MarkerOptions().position(point).zIndex(j).icon(
                            bitmapH);
                    break;
                case 8:
                    option = new MarkerOptions().position(point).zIndex(j).icon(
                            bitmapI);
                    break;
                default:
                    option = new MarkerOptions().position(point).zIndex(j).icon(
                            bitmapDefault);
                    break;
            }
            mMarker = (Marker) mBaiduMap.addOverlay(option);

            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(point).zoom(17.0f);
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory
                    .newMapStatus(builder.build()));
            mBaiduMap.setOnMarkerClickListener(new markerClickListener());
        }
    }
    /**
     * 计算距离
     * @param i 循环i
     * @param listSch 校区地址信息
     */
    private void addShowClickList(int i,List<SchoolPlaceTotal.Result> listSch){
        List<SchoolPlaceTotal.Result.Res> list1 = schoolPlaceTotal.getResult().get(i).getResult();
        for (int j = 0; j < list1.size(); j++) {
            LatLng p1LL = new LatLng(mCurrentLantitude, mCurrentLongitude);
            LatLng p2LL = new LatLng(Double.parseDouble(list1.get(j).getLatitude()),
                    Double.parseDouble(list1.get(j).getLongitude()));
            double distance = DistanceUtil.getDistance(p1LL, p2LL)/ 1000;
            java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");

            schoolShow = new SchoolShow(list1.get(j).getId(), list1.get(j).getSid(),
                    list1.get(j).getSname(), list1.get(j).getFaddress(), list1.get(j).getLongitude(),
                    list1.get(j).getLatitude(), list1.get(j).getSfile(),
                    df.format(distance) + "km", listSch.get(i).getSchool_aera(),"");
            switch (j){
                case 0:
                    schoolShow.setMarker("A");
                    break;
                case 1:
                    schoolShow.setMarker("B");
                    break;
                case 2:
                    schoolShow.setMarker("C");
                    break;
                case 3:
                    schoolShow.setMarker("D");
                    break;
                case 4:
                    schoolShow.setMarker("E");
                    break;
                case 5:
                    schoolShow.setMarker("F");
                    break;
                case 6:
                    schoolShow.setMarker("G");
                    break;
                case 7:
                    schoolShow.setMarker("H");
                    break;
                case 8:
                    schoolShow.setMarker("I");
                    break;
                default:
                    schoolShow.setMarker("");
                    break;
            }
            showList.add(schoolShow);
        }

    }

    /**
     * 计算距离
     * @param i 循环i
     * @param listSch 校区地址信息
     */
    private void addShowList(int i,List<SchoolPlaceTotal.Result.Res> listSch){
            LatLng p1LL = new LatLng(mCurrentLantitude, mCurrentLongitude);
            LatLng p2LL = new LatLng(Double.parseDouble(listSch.get(i).getLatitude()),
                            Double.parseDouble(listSch.get(i).getLongitude()));
            double distance = DistanceUtil.getDistance(p1LL, p2LL)/ 1000;
            java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");

            schoolShow = new SchoolShow(listSch.get(i).getId(), listSch.get(i).getSid(),
                    listSch.get(i).getSname(), listSch.get(i).getFaddress(), listSch.get(i).getLongitude(),
                    listSch.get(i).getLatitude(), listSch.get(i).getSfile(),
                    df.format(distance) + "km", "","");
        switch (i){
            case 0:
                schoolShow.setMarker("A");
                break;
            case 1:
                schoolShow.setMarker("B");
                break;
            case 2:
                schoolShow.setMarker("C");
                break;
            case 3:
                schoolShow.setMarker("D");
                break;
            case 4:
                schoolShow.setMarker("E");
                break;
            case 5:
                schoolShow.setMarker("F");
                break;
            case 6:
                schoolShow.setMarker("G");
                break;
            case 7:
                schoolShow.setMarker("H");
                break;
            case 8:
                schoolShow.setMarker("I");
                break;
            default:
                schoolShow.setMarker("");
                break;
        }
        showList.add(schoolShow);
    }
    /**
     * 冒泡排序 根据距离远近进行排序
     */
    private void sort(){
        for(int i=0;i<showList.size();i++){
            for(int j=showList.size()-1;j>i;j--){
                SchoolShow sch = null;
                if(Double.parseDouble(showList.get(j).getDistance().replace("km",""))< Double.parseDouble(showList.get(j-1).getDistance().replace("km",""))){
                    sch = showList.get(j);
                    showList.set(j,showList.get(j-1));
                    showList.set(j-1,sch);
                }
            }
        }
    }

    /**
     * 全城的marker标记
     */
    private void allMarker(){
        List<SchoolPlaceTotal.Result.Res> listSch = schoolPlaceTotal.getResult().get(0).getResult();
        for (int j = 0; j < listSch.size(); j++) {
            double latitude = Double.parseDouble(listSch.get(j).getLatitude());
            double longitude = Double.parseDouble(listSch.get(j).getLongitude());
            point = new LatLng(latitude, longitude);
            OverlayOptions option = null;
            switch (j) {
                case 0:
                    option = new MarkerOptions().position(point).zIndex(j).icon(
                            bitmapA);
                    break;
                case 1:
                    option = new MarkerOptions().position(point).zIndex(j).icon(
                            bitmapB);
                    break;
                case 2:
                    option = new MarkerOptions().position(point).zIndex(j).icon(
                            bitmapC);
                    break;
                case 3:
                    option = new MarkerOptions().position(point).zIndex(j).icon(
                            bitmapD);
                    break;
                case 4:
                    option = new MarkerOptions().position(point).zIndex(j).icon(
                            bitmapE);
                    break;
                case 5:
                    option = new MarkerOptions().position(point).zIndex(j).icon(
                            bitmapF);
                    break;
                case 6:
                    option = new MarkerOptions().position(point).zIndex(j).icon(
                            bitmapG);
                    break;
                case 7:
                    option = new MarkerOptions().position(point).zIndex(j).icon(
                            bitmapH);
                    break;
                case 8:
                    option = new MarkerOptions().position(point).zIndex(j).icon(
                            bitmapI);
                    break;
                default:
                    option = new MarkerOptions().position(point).zIndex(j).icon(
                            bitmapDefault);
                    break;
            }
            mMarker = (Marker) mBaiduMap.addOverlay(option);

            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(point).zoom(17.0f);
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory
                    .newMapStatus(builder.build()));
            mBaiduMap.setOnMarkerClickListener(new markerClickListener());
        }
    }

    /**
     * 设置popWindow
     *
     * @param results
     */
    private void setPup(List<SchoolPlaceTotal.Result> results) {
        city = new String[results.size()];
        for (int i = 0; i < results.size(); i++) {
            city[i] = results.get(i).getSchool_aera();
        }
        allMarker();
    }

    /**
     * marker点击事件处理
     */
    private class markerClickListener implements BaiduMap.OnMarkerClickListener {
        @Override
        public boolean onMarkerClick(final Marker marker) {
            LatLng latLng = marker.getPosition();
            List<SchoolPlaceTotal.Result.Res> listSch = schoolPlaceTotal.getResult().get(0).getResult();
            int index = marker.getZIndex();
            double latitude = Double.parseDouble(listSch.get(index).getLatitude());
            double longitude = Double.parseDouble(listSch.get(index).getLongitude());
            if (latLng.latitude == latitude && latLng.longitude == longitude) {
                Button button = new Button(PlaceChooseActivity.this
                        .getApplicationContext());
                button.setBackgroundResource(R.drawable.qipao);
                button.setTextColor(getResources().getColor(R.color.black));
                button.setTextSize(12);
                button.setPadding(20, 20, 20, 40);
                button.setText(listSch.get(index).getFaddress());
                mInfoWindow = new InfoWindow(BitmapDescriptorFactory
                        .fromView(button), marker.getPosition(), -70, null);
                mBaiduMap.showInfoWindow(mInfoWindow);
            }
            return true;
        }
    }

    /**
     * 初始化地图
     */
    private void initMap() {
        // 地图初始化
        mMapView = (MapView) findViewById(R.id.placeMap);
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(PlaceChooseActivity.this);
        mLocClient.registerLocationListener(myListener);

        myOrientationListener = new MyOrientationListener(
                getApplicationContext());
        myOrientationListener.setOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
            @Override
            public void onOrientationChanged(float x) {
                mXDirection = (int) x;
                // 构造定位数据
                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(mCurrentAccracy)
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(mXDirection)
                        .latitude(mCurrentLantitude)
                        .longitude(mCurrentLongitude).build();
                // 设置定位数据
                mBaiduMap.setMyLocationData(locData);
                // 设置自定义图标
                mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                        mCurrentMode, true, mCurrentMarker));
            }
        });
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mCurrentMode = LocationMode.FOLLOWING;
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                mCurrentMode, true, mCurrentMarker));
        mLocClient.start();
    }

    /**
     * 初始化控件
     */
    private void init() {
        //标题栏初始化
        btn_forward = (Button) findViewById(R.id.button_backward);
        btn_forward.setVisibility(View.VISIBLE);
        btn_forward.setOnClickListener(this);
        title = (TextView) findViewById(R.id.text_title);
        title.setText("所有场地");
        place = (Button) findViewById(R.id.place);
        place.setVisibility(View.VISIBLE);
        place.setOnClickListener(this);

        bitmap = PictureOptimization.decodeSampledBitmapFromResource(getResources(),
                R.drawable.head1, 90, 90);
        listView = (ListView) findViewById(R.id.placeListView);
        listView.setOnItemClickListener(this);
        listView.setFocusable(false);
        bitmapDefault = BitmapDescriptorFactory
                .fromResource(R.drawable.red_car);
        bitmapA = BitmapDescriptorFactory
                .fromResource(R.drawable.a2);
        bitmapB = BitmapDescriptorFactory
                .fromResource(R.drawable.b1);
        bitmapC = BitmapDescriptorFactory
                .fromResource(R.drawable.c1);
        bitmapD = BitmapDescriptorFactory
                .fromResource(R.drawable.d1);
        bitmapE = BitmapDescriptorFactory
                .fromResource(R.drawable.e1);
        bitmapF = BitmapDescriptorFactory
                .fromResource(R.drawable.f1);
        bitmapG = BitmapDescriptorFactory
                .fromResource(R.drawable.g1);
        bitmapH = BitmapDescriptorFactory
                .fromResource(R.drawable.h1);
        bitmapI = BitmapDescriptorFactory
                .fromResource(R.drawable.i1);
    }

    @Override
    protected void onStart() {
        // 开启图层定位
        mBaiduMap.setMyLocationEnabled(true);
        if (!mLocClient.isStarted()) {
            mLocClient.start();
        }
        // 开启方向传感器
        myOrientationListener.start();
        super.onStart();
    }

    @Override
    public void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        // 关闭图层定位
        mBaiduMap.setMyLocationEnabled(false);
        mLocClient.stop();
        // 关闭方向传感器
        myOrientationListener.stop();
        super.onStop();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView schId = (TextView) view.findViewById(R.id.schId);
        TextView CityName = (TextView) view.findViewById(R.id.CityName);
        Intent intent = new Intent();
        intent.setClass(PlaceChooseActivity.this, SchoolCoachActivity.class);
        intent.putExtra("schId", schId.getText().toString().trim());
        intent.putExtra("schName", CityName.getText().toString().trim());
        startActivity(intent);
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(final BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            // 构造定位数据
            if (isFirstLoc) {
                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(location.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(mXDirection).latitude(location.getLatitude())
                        .longitude(location.getLongitude()).build();
                mCurrentAccracy = location.getRadius();
                mBaiduMap.setMyLocationData(locData);
            }
            mCurrentLantitude = location.getLatitude();
            mCurrentLongitude = location.getLongitude();
        }
    }

    /**
     * 选择完成回调接口
     */
    private SelectPopupWindow.SelectCategory selectCategory = new SelectPopupWindow.SelectCategory() {
        @Override
        public void selectCategory(Integer parentSelectposition, Integer childrenSelectposition) {
            String sid = schoolPlaceTotal.getResult().get(parentSelectposition).getSid();
            setMyAdapter(sid);
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_backward:
                finish();
                break;
            case R.id.place:
                isFirstLoc = false;
                if (city.length == 0) {
                    Toast.makeText(PlaceChooseActivity.this, "网络状态不佳，请稍后再试！", Toast.LENGTH_SHORT).show();
                } else {
                    if (mPopupWindow == null) {
                        mPopupWindow = new SelectPopupWindow(city, null, this, selectCategory);
                    }
                    mPopupWindow.showAsDropDown(place, -5, 0);
                }
                break;
        }
    }
}
