package com.cretin.collegehelper.ui;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cretin.collegehelper.BaseApp;
import com.cretin.collegehelper.R;
import com.cretin.collegehelper.adapter.CommonGridViewAdapter;
import com.cretin.collegehelper.model.CommentModel;
import com.cretin.collegehelper.model.FlowModel;
import com.cretin.collegehelper.model.ReportModel;
import com.cretin.collegehelper.model.UserModel;
import com.cretin.collegehelper.utils.BigBitmapUtils;
import com.cretin.collegehelper.views.CircleTransform;
import com.cretin.collegehelper.views.NoScroolGridView;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

@EActivity(R.layout.activity_report)
public class ReportActivity extends AppCompatActivity {
    @ViewById
    ImageView ivReportBack;
    @ViewById
    TextView tvReportCommit;
    @ViewById
    ImageView ivItemReportAvatar;
    @ViewById
    TextView tvItemReportName;
    @ViewById
    TextView tvItemReportFlowtime;
    @ViewById
    TextView tvItemReportContent;
    @ViewById
    NoScroolGridView gvItemReportPhotots;
    @ViewById
    ImageView ivItemReportPhoto;
    @ViewById
    TextView tvItemReportQunboname;
    @ViewById
    LinearLayout llItemRreportContainer;
    @ViewById
    LinearLayout llReportContainer;
    @ViewById
    EditText edReportContent;
    private FlowModel currFlowModel;

    @AfterViews
    public void init() {
        getSupportActionBar().hide();

        currFlowModel = (FlowModel) getIntent().getSerializableExtra("flowmodel");
        if (currFlowModel != null)
            initData(currFlowModel);

        ivReportBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(edReportContent.getText().toString())){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ReportActivity.this);
                    builder.setMessage("您编辑了举报内容,确认要放弃吗?");
                    builder.setTitle("提示");
                    builder.setPositiveButton("不举报了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            ReportActivity.this.finish();
                        }
                    });
                    builder.setNegativeButton("继续举报", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                }
            }
        });

        tvReportCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ReportActivity.this);
                builder.setMessage("确认提交吗?");
                builder.setTitle("提示");
                builder.setPositiveButton("是的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        commit();
                    }
                });
                builder.setNegativeButton("算了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });
    }

    //提交举报内容
    private void commit() {
        String content = edReportContent.getText().toString();
        ReportModel reportModel = new ReportModel();
        reportModel.setCommitTime(System.currentTimeMillis());
        reportModel.setCommitUser(BmobUser.getCurrentUser(this, UserModel.class));
        reportModel.setReportedFlow(currFlowModel);
        reportModel.setReportContent(content);
        reportModel.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(ReportActivity.this, "提交成功,感谢您做出的贡献", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(ReportActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
        finish();
    }

    private void initData(final FlowModel item) {
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
                    ViewGroup.LayoutParams layoutParams = ivItemReportPhoto.getLayoutParams();
                    if (width > height) {
                        layoutParams.width = windowWidth;
                        layoutParams.height = (int) (((float) windowWidth / (float) width) * height);
                    } else {
                        layoutParams.height = windowWidth;
                        layoutParams.width = (int) (((float) windowWidth / (float) height) * width);
                    }
                    ivItemReportPhoto.setLayoutParams(layoutParams);
                }
            }
        }


        if (TextUtils.isEmpty(item.getAuthor().getAvater())) {
            Picasso.with(this).load(R.mipmap.default_icon).transform(new CircleTransform()).into(ivItemReportAvatar);
        } else {
            Picasso.with(this).load(item.getAuthor().getAvater()).transform(new CircleTransform()).into(ivItemReportAvatar);
        }
        String nickNameStr = item.getAuthor().getNickName();
        if (TextUtils.isEmpty(nickNameStr)) {
            nickNameStr = item.getAuthor().getUsername();
        }
        tvItemReportName.setText(nickNameStr);
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
        tvItemReportFlowtime.setText(timeStr);
        String contentStr = item.getTopicTontent();
        if (TextUtils.isEmpty(contentStr)) {
            tvItemReportContent.setVisibility(View.GONE);
        } else {
            tvItemReportContent.setVisibility(View.VISIBLE);
            tvItemReportContent.setText(contentStr);
        }

        String address = item.getSpotlight();
        if (TextUtils.isEmpty(address) || address.equals("定位失败")) {
            address = "暂无地理位置信息";
        }
        tvItemReportQunboname.setText(address);

        if (item.getResourceUrl() != null) {
            if (!item.getResourceUrl().isEmpty() && item.getResourceUrl().size() == 1) {
                gvItemReportPhotots.setVisibility(View.GONE);
                ivItemReportPhoto.setVisibility(View.VISIBLE);
                ivItemReportPhoto.setMaxHeight(BaseApp.getInstance().getWindowWidth());
                ivItemReportPhoto.setMaxWidth(BaseApp.getInstance().getWindowWidth());
                new DownImageAsyncTask().execute(item.getResourceUrl().get(0));
                Picasso.with(this).load(item.getResourceUrl().get(0)).into(ivItemReportPhoto);
                ivItemReportPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BigBitmapUtils.seeBigBitmap(0, item.getResourceUrl(), ReportActivity.this);
                    }
                });
            } else {
                gvItemReportPhotots.setVisibility(View.VISIBLE);
                ivItemReportPhoto.setVisibility(View.GONE);
                addFlowPic(item.getResourceUrl(), gvItemReportPhotots);
            }
        } else {
            gvItemReportPhotots.setVisibility(View.GONE);
            ivItemReportPhoto.setVisibility(View.GONE);
        }

        //获取所有评论
        getAllComment(item, llItemRreportContainer);
    }

    private void getAllComment(FlowModel flowModel, final LinearLayout commentContainer) {
        commentContainer.removeAllViews();
        if (flowModel.getCommentModelList() == null) {
            return;
        }
        for (final CommentModel comment : flowModel.getCommentModelList()) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_spotlight_details_comment, null);
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

            commentContainer.addView(view);
        }
    }

    //添加图片操作
    private void addFlowPic(final List<String> flows, NoScroolGridView gridView) {
        gridView.setVisibility(View.VISIBLE);
        CommonGridViewAdapter gridViewAdapter = new CommonGridViewAdapter(this, flows, R.layout.item_gridview_sendflow);
        gridView.setAdapter(gridViewAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BigBitmapUtils.seeBigBitmap(position, flows, ReportActivity.this);
            }
        });
    }
}
