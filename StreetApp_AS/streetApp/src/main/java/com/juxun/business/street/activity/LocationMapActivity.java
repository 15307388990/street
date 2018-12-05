package com.juxun.business.street.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLayoutChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiBoundSearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.juxun.business.street.adapter.LocationNameSearchAdapter;
import com.juxun.business.street.bean.AddsBean;
import com.juxun.business.street.service.LocationService;
import com.juxun.business.street.util.Tools;
import com.juxun.business.street.widget.ClearEditText;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yl.ming.efengshe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LocationActivity 定位搜索
 */
public class LocationMapActivity extends BaseActivity implements OnLayoutChangeListener {
    @ViewInject(R.id.lv_list)
    private ListView lv_list;
    @ViewInject(R.id.lv_list2)
    private ListView lv_list2;
    @ViewInject(R.id.ll_map)
    private LinearLayout ll_map;
    @ViewInject(R.id.tv_close)
    private TextView tv_close;// 取消
    @ViewInject(R.id.ed_clear)
    private ClearEditText ed_clear;// 搜索文本
    @ViewInject(R.id.tv_city)
    private TextView tv_city;

    @ViewInject(R.id.rb_all)
    private RadioButton rb_all;
    @ViewInject(R.id.rb_plot)
    private RadioButton rb_plot;
    @ViewInject(R.id.rb_office)
    private RadioButton rb_office;
    @ViewInject(R.id.rb_school)
    private RadioButton rb_school;
    @ViewInject(R.id.iv_location)
    private ImageView iv_location;
    @ViewInject(R.id.ll_mian_layout)
    private LinearLayout ll_mian_layout;

    private List<PoiInfo> list;
    private LocationNameSearchAdapter mAdapter;
    PoiSearch mPoiSearch;
    PoiSearch mPoiSearch2;
    private ImageView iv_arr;
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private double lat, lng;
    private String keyword = "全部";
    private MapStatusUpdate msu;
    private LatLng latLng;
    private List<PoiInfo> list2;
    private LocationNameSearchAdapter locationNameSearchAdapter;
    private boolean isSearch = true;
    // 屏幕高度
    private int screenHeight = 0;
    // 软件盘弹起后�?占高度阀�?
    private int keyHeight = 0;
    private String city;
    private LocationService locationService;
    private AddsBean mAddsBean;
    private LatLng mlatLng;// 当前定位的经纬度；

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_map);
        ViewUtils.inject(this);

        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch2 = PoiSearch.newInstance();
        initTitle();
        title.setText("更多地址");
        // 获取屏幕高度
        screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        // �?值设置为屏幕高度�?1/3
        keyHeight = screenHeight / 3;
        ll_mian_layout.addOnLayoutChangeListener(this);
        initView();
        startLocation();
    }

    /**
     * 开始进行定位
     */
    private void startLocation() {
        // -----------location config ------------
        locationService = new LocationService(getApplicationContext());
        // 获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        // 注册监听
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        locationService.start();// 定位SDK
        // start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request
        loading();
    }

    /*****
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            dismissLoading();
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                // 定位成功以后 保存当前位置
                mAddsBean.setLat(location.getLatitude() + "");// 纬度
                mAddsBean.setLng(location.getLongitude() + "");// 经度
                mAddsBean.setCity(location.getCity().replace("市", ""));
                locationService.unregisterListener(mListener); // 注销掉监听
                locationService.stop(); // 停止定位服务
                latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mlatLng = new LatLng(location.getLatitude(), location.getLongitude());
                tv_city.setText(mAddsBean.getCity());
                initMap();
            } else {
                Tools.showToast(getApplicationContext(), "定位失败");
            }
        }
    };

    private void initView() {
        mAddsBean = new AddsBean();
        mMapView = (MapView) findViewById(R.id.mapview);
        mBaiduMap = mMapView.getMap();

        /*** --------------------------- *******/
        list2 = new ArrayList<>();
        locationNameSearchAdapter = new LocationNameSearchAdapter(this, list2);
        lv_list2.setAdapter(locationNameSearchAdapter);
        list = new ArrayList<>();
        mAdapter = new LocationNameSearchAdapter(getApplicationContext(), list);
        lv_list.setAdapter(mAdapter);
        // getShopList();
        lv_list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (arg2 == list.size()) {
                    return;
                }
                PoiInfo poi = list.get(arg2);
                AddsBean addsBean = new AddsBean();
                addsBean.setLat(poi.location.latitude + "");// 纬度
                addsBean.setLng(poi.location.longitude + "");// 经度
                addsBean.setArea_name(poi.name);// 地址名称
                addsBean.setAddress(poi.address);// 详细地址
                addsBean.setCity(tv_city.getText().toString());
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("addsbean", addsBean);
                intent.putExtras(bundle);
                setResult(3, intent);
                finish();
            }
        });
        lv_list2.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                PoiInfo poi = list2.get(arg2);
                Intent intent = new Intent();
                AddsBean addsBean = new AddsBean();
                addsBean.setLat(poi.location.latitude + "");// 纬度
                addsBean.setLng(poi.location.longitude + "");// 经度
                addsBean.setCity(tv_city.getText().toString());
                addsBean.setArea_name(poi.name);// 地址名称
                addsBean.setAddress(mAddsBean.getCity());// 详细地址
                Bundle bundle = new Bundle();
                bundle.putSerializable("addsbean", addsBean);
                intent.putExtras(bundle);
                setResult(3, intent);
                finish();
            }
        });
        tv_close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                list2.clear();
                ed_clear.setText("");
                tv_close.setVisibility(View.GONE);
                ll_map.setVisibility(View.VISIBLE);
                lv_list2.setVisibility(View.GONE);
                /** 隐藏软键 **/
                View view = getWindow().peekDecorView();
                if (view != null) {
                    InputMethodManager inputmanger = (InputMethodManager) getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });
        rb_all.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                keyword = "全部";
                locationAll();
            }
        });
        rb_plot.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                keyword = "小区";
                getShopList();
            }
        });
        rb_office.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                keyword = "写字楼";
                getShopList();
            }
        });
        rb_school.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                keyword = "学校";
                getShopList();
            }
        });
        iv_location.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mBaiduMap.setMapStatus(msu);
            }
        });
        tv_city.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LocationMapActivity.this, SelectCityActivity.class);
                intent.putExtra("city", mAddsBean.getCity());
                startActivityForResult(intent, 1);
            }
        });

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() != KeyEvent.ACTION_UP) {
            if (ed_clear.getText().toString().length() > 0) {
                isSearch = false;
                /* 隐藏软键�? */
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager.isActive()) {
                    inputMethodManager
                            .hideSoftInputFromWindow(LocationMapActivity.this.getCurrentFocus().getWindowToken(), 0);
                }
                getShopList2();
            } else {
                Tools.showToast(LocationMapActivity.this, "请输入搜索关键字");
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    private void initMap() {
        // 普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setBuildingsEnabled(true);
        mBaiduMap.setTrafficEnabled(true);
        mBaiduMap.setMyLocationEnabled(true);

        // 在地图上添加Marker，并显示
        msu = MapStatusUpdateFactory.newLatLngZoom(latLng, 16.0f);
        mBaiduMap.setMapStatus(msu);
        locationAll();
        mBaiduMap.setOnMapStatusChangeListener(new OnMapStatusChangeListener() {

            @Override
            public void onMapStatusChangeStart(MapStatus arg0) {
                // 手势操作地图，设置地图状态等操作导致地图状态开始改变。

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus arg0) {
                // 地图状态变化中
            }

            @Override
            public void onMapStatusChange(MapStatus arg0) {
                // 地图状态改变结束
                latLng = arg0.target;
                if (keyword.equals("全部")) {
                    locationAll();
                } else {
                    getShopList();
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        // 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        // 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            String city = data.getStringExtra("cname");
            if (city.equals(mAddsBean.getCity())) {
                latLng = mlatLng;
                initMap();
            } else {
                initAdds(city);
            }
            tv_city.setText(city);
        }
    }

    private void initAdds(String city) {
        // 新建编码查询对象
        GeoCoder geocode = GeoCoder.newInstance();
        // 新建查询对象要查询的条件
        GeoCodeOption GeoOption = new GeoCodeOption().city(city).address(city + "人民医院");
        // 发起地理编码请求
        geocode.geocode(GeoOption);
        // 设置查询结果监听�?
        geocode.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {

            /**
             * 反地理编码查询结果回调函�?
             *
             * @param result
             *            反地理编码查询结�?
             */
            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {

            }

            /**
             * 地理编码查询结果回调函数
             *            地理编码查询结果
             */
            @Override
            public void onGetGeoCodeResult(GeoCodeResult arg0) {
                latLng = arg0.getLocation();
                initMap();
            }

        });
    }

    // 全部
    private void locationAll() {
        // 新建编码查询对象
        GeoCoder geocode = GeoCoder.newInstance();
        // 新建查询对象要查询的条件
        ReverseGeoCodeOption GeoOption = new ReverseGeoCodeOption().location(latLng);
        // 发起地理编码请求
        geocode.reverseGeoCode(GeoOption);

        // LatLngBounds bounds = new
        // LatLngBounds.Builder().include(latLng).include(latLng).build();
        // mPoiSearch.searchInBound(new
        // PoiBoundSearchOption().bound(bounds).keyword("小区").pageCapacity(5));
        geocode.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
                list = arg0.getPoiList();
                if (list != null) {
                    mAdapter.updateListView(list);
                }
            }

            @Override
            public void onGetGeoCodeResult(GeoCodeResult arg0) {
            }
        });

    }

    private void getShopList() {
        LatLngBounds bounds = new LatLngBounds.Builder().include(latLng).include(latLng).build();
        mPoiSearch.searchInBound(new PoiBoundSearchOption().bound(bounds).keyword(keyword).pageCapacity(10));
        loading();
        mPoiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {

            @Override
            public void onGetPoiResult(PoiResult arg0) {
                dismissLoading();
                list = arg0.getAllPoi();
                if (list != null) {
                    mAdapter.updateListView(list);
                } else {
                    Tools.showToast(getApplicationContext(), "没有数据");
                }
            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult arg0) {

            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult arg0) {

            }
        });
    }

    private void getShopList2() {
        LatLngBounds bounds = new LatLngBounds.Builder().include(latLng).include(latLng).build();
        mPoiSearch2.searchInBound(
                new PoiBoundSearchOption().bound(bounds).keyword(ed_clear.getText().toString()).pageCapacity(10));
        mPoiSearch2.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {

            @Override
            public void onGetPoiResult(PoiResult arg0) {
                List<PoiInfo> poiInfos = arg0.getAllPoi();
                if (poiInfos != null) {
                    list2 = poiInfos;
                } else {
                    list2.clear();
                    Tools.showToast(LocationMapActivity.this, "没有搜索到相关信息?");
                }
                locationNameSearchAdapter.updateListView(list2);
            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult arg0) {

            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult arg0) {

            }
        });
    }

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            int resultCode = json.getInt("resultCode");
            String msg = json.getString("resultMsg");
            String resultJson = json.getString("resultJson");
            if (resultCode == 0) {

            } else {
                Tools.showToast(getApplicationContext(), msg);
            }
        } catch (JSONException e) {
            Tools.showToast(getApplicationContext(), "解析数据错误");
        }
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight,
                               int oldBottom) {
        // old是改变前的左上右下坐标点值，没有old的是改变后的左上右下坐标点�??

        // System.out.println(oldLeft + " " + oldTop +" " + oldRight + " " +
        // oldBottom);
        // System.out.println(left + " " + top +" " + right + " " + bottom);

        // 现在认为只要控件将Activity向上推的高度超过�?1/3屏幕高，就认为软键盘弹起
        if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
            tv_close.setVisibility(View.VISIBLE);
            ll_map.setVisibility(View.GONE);
            lv_list2.setVisibility(View.VISIBLE);

        } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
            // if (isSearch) {
            // tv_close.setVisibility(View.GONE);
            // ll_map.setVisibility(View.VISIBLE);
            // lv_list2.setVisibility(View.GONE);
            // }
        }
    }
}
