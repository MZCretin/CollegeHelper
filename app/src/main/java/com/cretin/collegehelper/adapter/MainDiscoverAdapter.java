package com.cretin.collegehelper.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cretin.collegehelper.BaseApp;
import com.cretin.collegehelper.R;
import com.cretin.collegehelper.model.FlowModel;
import com.cretin.collegehelper.utils.BigBitmapUtils;
import com.cretin.collegehelper.views.CircleTransform;
import com.cretin.collegehelper.views.NoScroolGridView;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * Created by cretin on 4/11/16.
 */
public class MainDiscoverAdapter extends CommonAdapter<FlowModel> {
    public MainDiscoverAdapter(Context context, List<FlowModel> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
    }

    @Override
    public void convert(ViewHolders holder, final FlowModel item, int position) {
        ImageView avater = holder.getView(R.id.iv_item_rollr_local_avatar);
        TextView nickName = holder.getView(R.id.tv_item_rollr_local_name);
        TextView time = holder.getView(R.id.tv_item_rollr_local_flowtime);
        NoScroolGridView gridview = holder.getView(R.id.gv_item_rollr_local_photots);
        TextView content = holder.getView(R.id.tv_item_rollr_local_content);
        ImageView more = holder.getView(R.id.iv_item_rollr_local_more);
        ImageView like = holder.getView(R.id.iv_item_rollr_local_zan);
        ImageView comment = holder.getView(R.id.iv_item_rollr_local_comment);
        TextView spotlight = holder.getView(R.id.tv_item_rollr_local_qunboname);
        TextView liked = holder.getView(R.id.tv_item_rollr_local_beizan);
        LinearLayout container = holder.getView(R.id.ll_item_rollr_local_comment_container);
        final ImageView singleImage = holder.getView(R.id.iv_item_rollr_local_photo);

        class DownImageAsyncTask extends AsyncTask<String, Void, Bitmap> {

            @Override
            protected Bitmap doInBackground(String... params) {
                try {
                    URL imageURl = new URL(params[0]);
                    URLConnection con = imageURl.openConnection();
                    con.connect();
                    InputStream in = con.getInputStream();
                    return BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                if (bitmap != null) {
                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();
                    int windowWidth = BaseApp.getInstance().getWindowWidth() - 80;
                    ViewGroup.LayoutParams layoutParams = singleImage.getLayoutParams();
                    if (width > height) {
                        layoutParams.width = windowWidth;
                        layoutParams.height = (int) (((float) windowWidth / (float) width) * height);
                    } else {
                        layoutParams.height = windowWidth;
                        layoutParams.width = (int) (((float) windowWidth / (float) height) * width);
                    }
                    singleImage.setLayoutParams(layoutParams);
                }
            }
        }


        if (TextUtils.isEmpty(item.getAuthor().getAvater())) {
            Picasso.with(mContext).load(R.mipmap.default_icon).transform(new CircleTransform()).into(avater);
        } else {
            Picasso.with(mContext).load(item.getAuthor().getAvater()).transform(new CircleTransform()).into(avater);
        }
        String nickNameStr = item.getAuthor().getNickName();
        if (TextUtils.isEmpty(nickNameStr)) {
            nickNameStr = item.getAuthor().getUsername();
        }
        nickName.setText(nickNameStr);
        String timeStr;
        long temp = System.currentTimeMillis() - item.getSendTime();
        if (temp < (long) 60 * 1000) {
            timeStr = (int) (temp / 1000) + "秒前";
        } else if (temp < (long) 60 * 60 * 1000) {
            timeStr = (int) (temp / 1000 / 60) + "分钟前";
        } else if (temp < (long) 60 * 60 * 24 * 1000) {
            timeStr = (int) (temp / 1000 / 60 / 60) + "小时前";
        } else if (temp < (long) 60 * 60 * 24 * 30 * 1000) {
            timeStr = (int) (temp / 1000 / 60 / 60 / 24) + "天前";
        } else {
            timeStr = (int) (temp / 1000 / 60 / 60 / 24 / 30) + "月前";
        }
        time.setText(timeStr);
        String contentStr = item.getTopicTontent();
        if (TextUtils.isEmpty(contentStr)) {
            content.setVisibility(View.GONE);
        } else {
            content.setVisibility(View.VISIBLE);
            content.setText(contentStr);
        }

        spotlight.setText("软件产业基地");

        if (item.getResourceUrl() != null) {
            if (!item.getResourceUrl().isEmpty() && item.getResourceUrl().size() == 1) {
                gridview.setVisibility(View.GONE);
                singleImage.setVisibility(View.VISIBLE);
                singleImage.setMaxHeight(BaseApp.getInstance().getWindowWidth());
                singleImage.setMaxWidth(BaseApp.getInstance().getWindowWidth());
                new DownImageAsyncTask().execute(item.getResourceUrl().get(0));
                Picasso.with(mContext).load(item.getResourceUrl().get(0)).into(singleImage);
                singleImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BigBitmapUtils.seeBigBitmap(0, item.getResourceUrl(), mContext);
                    }
                });
            } else {
                gridview.setVisibility(View.VISIBLE);
                singleImage.setVisibility(View.GONE);
                addFlowPic(item.getResourceUrl(), gridview);
            }
        }else{
            gridview.setVisibility(View.GONE);
            singleImage.setVisibility(View.GONE);
        }
    }

    //添加图片操作
    private void addFlowPic(final List<String> flows, NoScroolGridView gridView) {
        gridView.setVisibility(View.VISIBLE);
        CommonGridViewAdapter gridViewAdapter = new CommonGridViewAdapter(mContext, flows, R.layout.item_gridview_sendflow);
        gridView.setAdapter(gridViewAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BigBitmapUtils.seeBigBitmap(position, flows, mContext);
            }
        });
    }
}
