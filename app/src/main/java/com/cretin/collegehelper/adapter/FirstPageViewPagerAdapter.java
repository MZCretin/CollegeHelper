package com.cretin.collegehelper.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by cretin on 15/12/29.
 */
public class FirstPageViewPagerAdapter extends PagerAdapter {
    private List<View> list;

    public FirstPageViewPagerAdapter(List<View> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list!=null&&!list.isEmpty()?list.size():0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(list.get(position));
        return list.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(list.get(position));
    }
}
