package com.cretin.collegehelper.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import cn.bmob.push.PushConstants;

/**
 * Created by cretin on 4/25/16.
 */
public class MyPushMessageReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        if(intent.getAction().equals(PushConstants.ACTION_MESSAGE)){
            Log.d("bmob", "客户端收到推送内容："+intent.getStringExtra("msg"));
            Toast.makeText(context, intent.getStringExtra("msg"), Toast.LENGTH_SHORT).show();
        }
    }

}