package com.cretin.collegehelper.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.cretin.collegehelper.views.ImageFragment;

import java.util.List;

/**
 * Created by csonezp on 15-12-28.
 */
public class ImageViewPagerAdapter extends FragmentStatePagerAdapter {
    private static final String IMAGE_URL = "image";

    List<String> mDatas;
    private boolean iconFlag;
    private Context context;

    public ImageViewPagerAdapter(FragmentManager fm, List data, boolean iconFlag, Context context) {
        super(fm);
        mDatas = data;
        this.iconFlag = iconFlag;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        String url = mDatas.get(position);
        Fragment fragment = ImageFragment.newInstance(url,iconFlag,position,context);
        return fragment;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }
}
