package com.cretin.collegehelper.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cretin.collegehelper.BaseApp;
import com.cretin.collegehelper.R;
import com.cretin.collegehelper.eventbus.NotifyCommentResult;
import com.cretin.collegehelper.model.CommentModel;
import com.cretin.collegehelper.model.FlowModel;
import com.cretin.collegehelper.model.UserModel;
import com.cretin.collegehelper.popwindow.SelectPopupWindow;
import com.cretin.collegehelper.ui.ReportActivity_;
import com.cretin.collegehelper.ui.UserDetailsActivity_;
import com.cretin.collegehelper.utils.BigBitmapUtils;
import com.cretin.collegehelper.views.CircleTransform;
import com.cretin.collegehelper.views.NoScroolGridView;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.DeleteListener;

/**
 * Created by cretin on 4/11/16.
 */
public class MainDiscoverAdapter extends CommonAdapter<FlowModel> implements SelectPopupWindow.OnPopWindowClickListener {
    private SelectPopupWindow menuWindow;
    public static final int TYPE_USER_DETAILS = 1;
    private FlowModel mFlowModel;
    private int type;
    private int pagetype;

    public MainDiscoverAdapter(Context context, List<FlowModel> mDatas, int itemLayoutId, int type,int pagetype) {
        super(context, mDatas, itemLayoutId);
        this.type = type;
        this.pagetype = pagetype;
    }

    @Override
    public void convert(ViewHolders holder, final FlowModel item, final int position) {
        ImageView avater = holder.getView(R.id.iv_item_rollr_local_avatar);
        TextView nickName = holder.getView(R.id.tv_item_rollr_local_name);
        TextView time = holder.getView(R.id.tv_item_rollr_local_flowtime);
        NoScroolGridView gridview = holder.getView(R.id.gv_item_rollr_local_photots);
        TextView content = holder.getView(R.id.tv_item_rollr_local_content);
        ImageView more = holder.getView(R.id.iv_item_rollr_local_more);
        ImageView comment = holder.getView(R.id.iv_item_rollr_local_comment);
        TextView spotlight = holder.getView(R.id.tv_item_rollr_local_qunboname);
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

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getAuthor().getId().equals(BmobUser.getCurrentUser(mContext, UserModel.class).getId())) {
                    showPopWindow((Activity) mContext, true, item);
                } else {
                    showPopWindow((Activity) mContext, false, item);
                }
            }
        });

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

        String address = item.getSpotlight();
        if (TextUtils.isEmpty(address) || address.equals("定位失败")) {
            address = "暂无地理位置信息";
        }
        spotlight.setText(address);

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
        } else {
            gridview.setVisibility(View.GONE);
            singleImage.setVisibility(View.GONE);
        }

        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //评论
                doComment(item, position);
            }
        });

        //获取所有评论
        getAllComment(item, container);

        if (type != TYPE_USER_DETAILS) {
            avater.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, UserDetailsActivity_.class);
                    intent.putExtra("usermodel", item.getAuthor());
                    mContext.startActivity(intent);
                }
            });
        }
    }

    private void getAllComment(FlowModel flowModel, final LinearLayout commentContainer) {
        commentContainer.removeAllViews();
        if (flowModel.getCommentModelList() == null) {
            return;
        }
        for (final CommentModel comment : flowModel.getCommentModelList()) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_spotlight_details_comment, null);
            TextView tvCommentName = (TextView) view.findViewById(R.id.tv_item_rollr_local_comment_name);
            TextView tvCommentDetails = (TextView) view.findViewById(R.id.tv_item_rollr_local_comment_details);
            LinearLayout.LayoutParams tvCommentNameParams = (LinearLayout.LayoutParams) tvCommentName.getLayoutParams();
            LinearLayout.LayoutParams tvCommentDetailsParams = (LinearLayout.LayoutParams) tvCommentDetails.getLayoutParams();
            tvCommentName.setLayoutParams(tvCommentNameParams);
            tvCommentDetails.setLayoutParams(tvCommentDetailsParams);
            String nickName = comment.getFromUser().getNickName();
            if (TextUtils.isEmpty(nickName)) {
                nickName = comment.getFromUser().getUsername();
            }
            tvCommentName.setText(nickName);
            tvCommentDetails.setText(comment.getComment());

//            tvCommentName.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    IntentUtils.intentToUserInfo(comment.getFromUserId(), mContext);
//                }
//            });

            commentContainer.addView(view);
        }
    }

    //评论操作
    private void doComment(FlowModel flowModel, int position) {
        NotifyCommentResult result = new NotifyCommentResult();
        result.setType(pagetype);
        result.setFlowModel(flowModel);
        result.setPosition(position);
        EventBus.getDefault().post(result);
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

    //显示对话框
    private void showPopWindow(Activity activity, boolean flag, FlowModel flowModel) {
        mFlowModel = flowModel;
        menuWindow = new SelectPopupWindow(activity, this, SelectPopupWindow.TYPE_MAIN_HOME, flag);
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int winHeight = activity.getWindow().getDecorView().getHeight();
        menuWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM, 0, winHeight - rect.bottom);
    }

    @Override
    public void onPopWindowClickListener(View view) {
        switch (view.getId()) {
            case R.id.btn_main_share:
                share();
                break;
            case R.id.btn_main_jubao:
                report();
                break;
            case R.id.btn_main_delete:
                delete();
                break;
        }
    }

    //删除
    private void delete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("动态删除后无法恢复,确定删除？");
        builder.setTitle("提示");
        builder.setPositiveButton("任性为之", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mFlowModel.delete(mContext, new DeleteListener() {

                    @Override
                    public void onSuccess() {
                        Toast.makeText(mContext, "动态删除成功", Toast.LENGTH_SHORT).show();
                        mDatas.remove(mFlowModel);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                    }
                });
            }
        });
        builder.setNegativeButton("容朕想想", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    //举报
    private void report() {
        Intent intent = new Intent(mContext, ReportActivity_.class);
        intent.putExtra("flowmodel", mFlowModel);
        mContext.startActivity(intent);
    }

    private void share() {
        String shareContent;
        if (TextUtils.isEmpty(mFlowModel.getTopicTontent())) {
            shareContent = "用户" + mFlowModel.getAuthor().getUsername() + "正在使用校园助手分享他此时的动态,快去和他一起互动吧";
        } else {
            shareContent = "用户" + mFlowModel.getAuthor().getUsername() + "正在使用校园助手分享他此时的动态【" + mFlowModel.getTopicTontent() + "】,快去和他一起互动吧";
        }
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareContent);
        shareIntent.setType("text/plain");

        //设置分享列表的标题，并且每次都显示分享列表
        mContext.startActivity(Intent.createChooser(shareIntent, "分享到"));
    }
}
