package com.cretin.collegehelper.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.cretin.collegehelper.BaseApp;
import com.cretin.collegehelper.constants.LocalKeysStorage;
import com.cretin.collegehelper.model.LocationModel;
import com.orhanobut.hawk.Hawk;

/**
 * Created by cretin on 16/2/20.
 */
public class LocationService extends Service implements AMapLocationListener {
    public static final long LOCATION_UPDATE_MIN_TIME = 10 * 1000;
    public static final float LOCATION_UPDATE_MIN_DISTANCE = 5;
    public static final String INTENT_ACTION_UPDATE_DATA = "AMapLocationInfo";
    public static final String INTENT_ACTION_UPDATE_DATA_EXTRA_LATITUDE = "AMapLocationLatitude";
    public static final String INTENT_ACTION_UPDATE_DATA_EXTRA_LONGITUDE = "AMapLocationLongitude";

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;

    private static LocationService service;

    //记录时间 每两分钟更新一次数据库的位置信息
    private int time = 0;

    public LocationService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        //使用参数为Context的方法，Service也是Context实例，

        service = this;
        //是四大组件之一
        mLocationClient = new AMapLocationClient(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(LOCATION_UPDATE_MIN_TIME);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.setLocationListener(this);
        //启动定位
        mLocationClient.startLocation();
    }

    public static LocationService getLocationService(){
        return service;
    }

    //开始定位
    public void startAMapLocation(){
        if(mLocationClient!=null){
            mLocationClient.startLocation();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.onDestroy();//销毁定位客户端。
        }
        //设置为null是为了提醒垃圾回收器回收资源
        mLocationClient = null;
    }



    //当位置发生变化的时候调用这个方法。
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        // 如果位置获取错误则不作处理，退出本方法
        // 返回错误码如果为0则表明定位成功，反之则定位失败
        //在虚拟机测试的时候，返回错误码31，为未知错误
        //如果使用虚拟机测试的时候遇到这个问题，建议使用真机测试。
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
//                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
//                aMapLocation.getLatitude();//获取纬度
//                aMapLocation.getLongitude();//获取经度
//                aMapLocation.getAccuracy();//获取精度信息
//                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                Date date = new Date(aMapLocation.getTime());
//                df.format(date);//定位时间
//                aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
//                aMapLocation.getCountry();//国家信息
//                aMapLocation.getProvince();//省信息
//                aMapLocation.getCity();//城市信息
//                aMapLocation.getDistrict();//城区信息
//                aMapLocation.getStreet();//街道信息
//                aMapLocation.getStreetNum();//街道门牌号信息
//                aMapLocation.getCityCode();//城市编码
//                aMapLocation.getAdCode();//地区编码
                // 发送广播传送地点位置信息到地图显示界面
                // 当数据正常获取的时候，把位置信息通过广播发送到接受方,
                // 也就是需要处理这些数据的组件。
                LocationModel model = new LocationModel();
                model.setAddress(aMapLocation.getAddress());
                model.setLatitude(aMapLocation.getLatitude());
                model.setLongitude(aMapLocation.getLongitude());
                model.setLocation(aMapLocation.getLongitude()+","+aMapLocation.getLatitude());
                if(time%120==0){
                    Hawk.put(LocalKeysStorage.LOCATION_DATA,model);
                }

                BaseApp.getInstance().setLocationModel(model);
                time += 10;

            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
