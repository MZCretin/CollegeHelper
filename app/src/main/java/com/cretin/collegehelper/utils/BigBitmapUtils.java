package com.cretin.collegehelper.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.cretin.collegehelper.R;
import com.cretin.collegehelper.ui.SeeBigBitmapActivity_;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cretin on 16/3/4.
 */
public class BigBitmapUtils {
    /**
     * 该类是为了看大图而准备的
     * 使用说明  position 是在看多图的时候，当前点击的图片的下标 这样到看大图界面的时候就会显示当前下标的图片
     *          如果只有一张图片就选择不带改参数的方法
     *          list<String>  这个是图片的url路径集合
     *          iconFlag 用来标记当前这张图片是否为头像 因为我们丢头像的处理有不同的缩放方式
     *          message 改变量记录用于在看大图的界面显示的信息 若不想显示就设为“”即可
     * @param position
     * @param listString
     * @param mContext
     * @param iconFlag
     * @param message
     */
    public static void seeBigBitmap(int position, List<String> listString, Context mContext, boolean iconFlag, String message) {
        Intent intent = new Intent(mContext, SeeBigBitmapActivity_.class);
        intent.putExtra("list", (Serializable) listString);
        intent.putExtra("index", String.valueOf(position + 1));
        intent.putExtra("iconFlag", iconFlag);
        intent.putExtra("message", message);
        mContext.startActivity(intent);
        ((Activity) mContext).overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }

    public static void seeBigBitmap(int position, List<String> listString, Context mContext) {
        seeBigBitmap(position, listString, mContext, false, null);
    }

    public static void seeBigBitmap(int position, List<String> listString, Context mContext, String message) {
        seeBigBitmap(position, listString, mContext, false, message);
    }

    public static void seeBigBitmap(int position, List<String> listString, Context mContext, boolean iconFlag) {
        seeBigBitmap(position, listString, mContext, iconFlag, null);
    }
    public static void seeBigBitmap(List<String> listString, Context mContext, boolean iconFlag) {
        seeBigBitmap(0, listString, mContext, iconFlag, null);
    }
    public static void seeBigBitmap(List<String> listString, Context mContext) {
        seeBigBitmap(0, listString, mContext, false, null);
    }
}
