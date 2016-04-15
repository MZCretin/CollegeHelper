package com.cretin.collegehelper.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.cretin.collegehelper.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by cretin on 16/2/17.
 */

/**
 * 注意：
 * 在测试的时候由于没有请求网络数据 所以在此用的是本地的图片 所以在真正上线使用的时候 要将下面的Picasso加载图片换成网路图片地址
 */
public class CommonGridViewAdapter extends CommonAdapter<String> {
    private Context context;

    public CommonGridViewAdapter(Context context, List<String> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
        this.context = context;
    }

    @Override
    public void convert(ViewHolders holder, String item, int postion) {
        ImageView image = holder.getView(R.id.iv_item_gridview_sendflow);
        Picasso.with(context)
                .load(item)
                .resize(200,200)
                .centerCrop()
                .into(image);
    }
}
