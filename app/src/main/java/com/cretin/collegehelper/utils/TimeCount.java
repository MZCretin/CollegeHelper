package com.cretin.collegehelper.utils;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

/**
 * Created by cretin on 15/12/31.
 */
public class TimeCount extends CountDownTimer {
    private View view;
    public TimeCount(long millisInFuture, long countDownInterval, View view) {
        super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        this.view = view;
    }
    @Override
    public void onFinish() {//计时完毕时触发
        ((TextView)view).setText("重新验证");
        ((TextView)view).setClickable(true);
        ((TextView)view).setTextColor(Color.rgb(32, 153, 249));
    }
    @Override
    public void onTick(long millisUntilFinished){//计时过程显示
        ((TextView)view).setClickable(false);
        ((TextView)view).setText(millisUntilFinished / 1000 + "秒");
    }
}
