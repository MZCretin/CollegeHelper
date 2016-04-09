package com.cretin.collegehelper.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cretin.collegehelper.R;
import com.cretin.collegehelper.adapter.VoteDetailsListViewAdapter;
import com.cretin.collegehelper.model.VoteModel;
import com.cretin.collegehelper.model.VoteResultModel;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

@EActivity(R.layout.activity_vote_details)
public class VoteDetailsActivity extends AppCompatActivity implements SwipyRefreshLayout.OnRefreshListener {
    @ViewById
    ImageView ivCreateVotewBack;
    @ViewById
    TextView tvCreateVoteTitle;
    @ViewById
    TextView tvCreateVoteOutport;
    @ViewById
    TextView tvCreateVoteTitles;
    @ViewById
    TextView tvCreateVoteDes;
    @ViewById
    TextView tvCreateVoteTime;
    @ViewById
    ListView listviewVoteDetails;
    @ViewById
    SwipyRefreshLayout swipyListviewVoteDetails;
    private List<VoteResultModel> list;
    private VoteDetailsListViewAdapter adapter;
    private VoteModel currVoteModel;
    private ProgressDialog progressDialog;

    @AfterViews
    public void init() {
        getSupportActionBar().hide();

        swipyListviewVoteDetails.setOnRefreshListener(this);

        currVoteModel = (VoteModel) getIntent().getSerializableExtra("voteModel");
        if (currVoteModel != null) {
            tvCreateVoteTitle.setText(currVoteModel.getVoteTitle());
            tvCreateVoteDes.setText(currVoteModel.getVoteDestribe());
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            tvCreateVoteTime.setText("创建时间:" + format.format(currVoteModel.getCreateTime()));
            tvCreateVoteTitles.setText(currVoteModel.getVoteTitle());
        }

        list = new ArrayList<>();
        adapter = new VoteDetailsListViewAdapter(this, list, R.layout.item_listview_vote_details);
        listviewVoteDetails.setAdapter(adapter);

        initData();

        ivCreateVotewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VoteDetailsActivity.this.finish();
            }
        });

        tvCreateVoteOutport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMsgDialog();
            }
        });
    }

    private void showMsgDialog() {
        final EditText editText = new EditText(this);
        editText.setHint("请输入要保存的文件名");
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("创建文件名").setView(
                editText).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(TextUtils.isEmpty(editText.getText().toString())){
                    Toast.makeText(VoteDetailsActivity.this, "文件名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                outPort(editText.getText().toString());
            }
        }).setNegativeButton("取消", null).create();
        dialog.show();
    }

    private void outPort(String fileName) {
        try {
            File f = null;
            String state = Environment.getExternalStorageState();
            if(state.equals(Environment.MEDIA_MOUNTED)){
                f = new File(Environment.getExternalStorageDirectory()+"/collegehelper/");
                if(!f.exists()){
                    f.mkdir();
                }
            }
            progressDialog = ProgressDialog.show(this,"","请稍后...");
            // 创建或打开Excel文件
            WritableWorkbook book = Workbook.createWorkbook(new File(f,fileName + ".xls"));
            SimpleDateFormat format=new SimpleDateFormat("yyyy/MM/dd hh:mm");

            // 生成名为“第一页”的工作表,参数0表示这是第一页
            WritableSheet sheet1 = book.createSheet("第一页", 0);

            // 在Label对象的构造函数中,元格位置是第一列第一行(0,0)以及单元格内容为test
            Label label = new Label(0, 0, "投票标题");
            Label label1 = new Label(1, 0, "投票内容");
            Label label2 = new Label(2, 0, "用户投票反馈内容");
            Label label3 = new Label(3, 0, "投票创建时间");
            Label label4 = new Label(4, 0, "用户提交时间");
            Label label5 = new Label(5, 0, "用户ID");

            // 将定义好的单元格添加到工作表中
            sheet1.addCell(label);
            sheet1.addCell(label1);
            sheet1.addCell(label2);
            sheet1.addCell(label3);
            sheet1.addCell(label4);
            sheet1.addCell(label5);

            for (int i = 0; i < list.size(); i++) {
                VoteResultModel v = list.get(i);
                Label l = new Label(0, i+1, v.getVoteInfo().getVoteTitle());
                Label l1 = new Label(1, i+1, v.getVoteInfo().getVoteDestribe());
                Label l2 = new Label(2, i+1, v.getUserVoteContent());
                Label l3 = new Label(3, i+1, format.format(v.getVoteTime()));
                Label l4 = new Label(4, i+1, format.format(v.getVoteTime()));
                Label l5 = new Label(5, i+1, v.getVoteUser().getUsername());
                sheet1.addCell(l);
                sheet1.addCell(l1);
                sheet1.addCell(l2);
                sheet1.addCell(l3);
                sheet1.addCell(l4);
                sheet1.addCell(l5);
            }

            // 写入数据并关闭文件
            book.write();
            book.close();
            progressDialog.dismiss();
            Toast.makeText(VoteDetailsActivity.this, "文件在"+"mnt/sdcard/collegehelper/" + fileName + ".xls", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            progressDialog.dismiss();
            System.out.println(e);
        }
    }

    private void initData() {
        BmobQuery<VoteModel> innerQuery = new BmobQuery<>();
        String[] friendIds = {currVoteModel.getObjectId()};//好友的objectId数组
        innerQuery.addWhereContainedIn("objectId", Arrays.asList(friendIds));
//查询帖子
        BmobQuery<VoteResultModel> query = new BmobQuery<>();
        query.addWhereMatchesQuery("voteInfo", "VoteModel", innerQuery);
        query.include("voteInfo,voteUser");
        query.findObjects(this, new FindListener<VoteResultModel>() {
            @Override
            public void onSuccess(List<VoteResultModel> object) {
                swipyListviewVoteDetails.setRefreshing(false);
                list.clear();
                list.addAll(object);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int code, String msg) {
                swipyListviewVoteDetails.setRefreshing(false);
                Toast.makeText(VoteDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        initData();
    }
}
