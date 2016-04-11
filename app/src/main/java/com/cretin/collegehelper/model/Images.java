package com.cretin.collegehelper.model;

import android.graphics.Bitmap;

import com.cretin.collegehelper.utils.ImageUtils;

import java.io.Serializable;

/**
 * Created by cretin on 16/2/19.
 */
public class Images implements Serializable {
    public static final int TYPE_FILE = 0;
    public static final int TYPE_URL = 1;
    public static final int TYPE_RES = 2;
    private Bitmap mBitmap;
    private Bitmap mEditedBitmap;
    private String path= "";
    private int res;
    private int type;
    private String name;
    private Flag mFlag;
    private boolean viewoFlag;
    private int mCurrentFilter = 0;

    public boolean isViewoFlag() {
        return viewoFlag;
    }

    public void setViewoFlag(boolean viewoFlag) {
        this.viewoFlag = viewoFlag;
    }

    public Flag getmFlag() {
        return mFlag;
    }

    public void setmFlag(Flag mFlag) {
        this.mFlag = mFlag;
    }

    public Images(int type) {
        this.type = type;
    }

    public Bitmap getmBitmap() {
        if (mBitmap == null) {
            mBitmap = ImageUtils.decodeBitmapFromFSDCard(getPath());
        }

        return mBitmap;
    }

    public void setmBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    public Bitmap getmEditedBitmap() {
        return mEditedBitmap;
    }

    public void setmEditedBitmap(Bitmap mEditedBitmap) {
        this.mEditedBitmap = mEditedBitmap;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getmCurrentFilter() {
        return mCurrentFilter;
    }

    public void setmCurrentFilter(int mCurrentFilter) {
        this.mCurrentFilter = mCurrentFilter;
    }

    private class Flag {
        boolean colorTag;
        boolean filter;

        public boolean isColorTag() {
            return colorTag;
        }

        public void setColorTag(boolean colorTag) {
            this.colorTag = colorTag;
        }

        public boolean isFilter() {
            return filter;
        }

        public void setFilter(boolean filter) {
            this.filter = filter;
        }
    }
}
