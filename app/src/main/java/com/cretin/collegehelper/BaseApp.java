package com.cretin.collegehelper;

import android.app.Application;
import android.content.Context;
import android.view.WindowManager;
import android.widget.Toast;

import com.cretin.collegehelper.constants.LocalKeysStorage;
import com.cretin.collegehelper.model.UserModel;
import com.cretin.collegehelper.net.NetChangeObserver;
import com.cretin.collegehelper.net.NetWorkUtil;
import com.cretin.collegehelper.net.NetworkStateReceiver;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.LogLevel;

import cn.bmob.v3.Bmob;


/**
 * Created by ycdyus on 2015/10/9.
 */
public class BaseApp extends Application {
    private static BaseApp app;
    private UserModel userModel;
    private int windowWidth;
    /**
     * 网络状态  observer
     */
    private static NetChangeObserver observer = new NetChangeObserver() {
        @Override
        public void onConnect(NetWorkUtil.NetType type) {
            super.onConnect(type);
            switch (type) {
                case WIFI:
                    showToast("WIFI已连接");
                    break;
                case GNET_3:
                    showToast("移动3G网络已连接!!!");
                    break;
                default:
                    showToast("网络已连接");
                    break;
            }
        }

        @Override
        public void onDisConnect() {
            super.onDisConnect();
            showToast("网络未连接");
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        initNetState();

        Bmob.initialize(this, "ee3cfb7f92e0ffc42dc42ba799079182");

        Hawk.init(this)
                .setEncryptionMethod(HawkBuilder.EncryptionMethod.NO_ENCRYPTION)
                .setStorage(HawkBuilder.newSqliteStorage(this))
                .setLogLevel(LogLevel.FULL)
                .build();
    }

    public static BaseApp getInstance() {
        return app;
    }

    /**
     * 初始化网络监听
     */
    private void initNetState() {
        NetworkStateReceiver.registerNetworkStateReceiver(this);
        NetworkStateReceiver.registerObserver(observer);
    }

    public UserModel getUserModel() {
        if (userModel == null) {
            userModel = Hawk.get(LocalKeysStorage.LOCAL_USERINFO);
        }
        return userModel;
    }

    public void setUserModel(UserModel user) {
        userModel = user;
        Hawk.put(LocalKeysStorage.LOCAL_USERINFO, user);
    }

    /**
     * 注销网络监听
     */
    public static void unegisterNetworkStateReceiver() {
        NetworkStateReceiver.removeRegisterObserver(observer);
        NetworkStateReceiver.unRegisterNetworkStateReceiver(app);
    }

    /**
     * 弹出Toast
     */
    public static void showToast(String msg) {
        Toast.makeText(app, msg, Toast.LENGTH_SHORT).show();
    }

    public int getWindowWidth() {
        if (windowWidth <= 0) {
            WindowManager wm = (WindowManager) this
                    .getSystemService(Context.WINDOW_SERVICE);
            windowWidth = wm.getDefaultDisplay().getWidth();
        }
        return windowWidth;
    }
}
