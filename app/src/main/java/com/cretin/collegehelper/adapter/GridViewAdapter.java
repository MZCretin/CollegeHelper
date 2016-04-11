package com.cretin.collegehelper.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ImageView;

import com.cretin.collegehelper.R;
import com.cretin.collegehelper.model.Images;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by cretin on 15/12/24.
 */
public class GridViewAdapter extends CommonAdapter<Images> {
    private Context context;

    public GridViewAdapter(Context context, List<Images> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
        this.context = context;
    }

    @Override
    public void convert(ViewHolders holder, final Images item, int postion) {
        ImageView image = holder.getView(R.id.iv_item_gridview_common_sendflow);
        ImageView delete = holder.getView(R.id.iv_item_gridview_common_delete);
        if (postion != 9 && postion == mDatas.size() - 1) {
            delete.setVisibility(View.GONE);
        } else {
            delete.setVisibility(View.VISIBLE);
        }

        if (item.getType() == Images.TYPE_RES) {
            Picasso.with(context)
                    .load(item.getRes())
                    .fit()
                    .into(image);
        } else if (item.getType() == Images.TYPE_FILE) {
            image.setImageBitmap(item.getmBitmap());
        } else if (item.getType() == Images.TYPE_URL) {
            Picasso.with(context)
                    .load(item.getPath())
                    .into(image);
        }

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(mContext)
                        .setMessage("确定删掉这张图片?")
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mDatas.remove(item);
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("放弃", null)
                        .show();

            }
        });
    }
}
