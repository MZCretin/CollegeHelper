package com.cretin.collegehelper.ui;

import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cretin.collegehelper.R;
import com.cretin.collegehelper.adapter.ListViewPaperManagerAdapter;
import com.cretin.collegehelper.model.PaperResultModel;
import com.cretin.collegehelper.model.PaperSendModel;
import com.cretin.collegehelper.model.UserModel;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

@EActivity(R.layout.activity_paper_manager)
public class PaperManagerActivity extends AppCompatActivity {
    @ViewById
    TextView tvPaperManagerManager;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
    @ViewById
    ImageView ivPaperManagerBack;
    @ViewById
    TextView tvPaperManagerTitle;
    @ViewById
    TextView ivPaperManagerCreate;
    @ViewById
    TextView ivPaperManagerCommitNum;
    @ViewById
    TextView ivPaperManagerUncommitNum;
    @ViewById
    TextView ivPaperManagerHigtest;
    @ViewById
    TextView ivPaperManagerHigtestName;
    @ViewById
    TextView ivPaperManagerLowest;
    @ViewById
    TextView ivPaperManagerLowestName;
    @ViewById
    TextView ivPaperManagerAverage;
    @ViewById
    ListView listviewPaperManager;
    @ViewById
    SwipyRefreshLayout swipyListviewPaperManager;
    private PaperSendModel mModel;
    private List<PaperResultModel> list;
    private ListViewPaperManagerAdapter adapter;

    @AfterViews
    public void init() {
        getSupportActionBar().hide();

        mModel = (PaperSendModel) getIntent().getSerializableExtra("papersendmodel");

        if (mModel != null) {
            long tempTime = mModel.getCreateTime() + (mModel.getPeriod() * 1000 * 60);
            String strPeriod = format.format(mModel.getCreateTime()) + "~" + format.format(tempTime);
            ivPaperManagerCreate.setText(strPeriod);

            tvPaperManagerTitle.setText(mModel.getTitle());
            ivPaperManagerUncommitNum.setText("未提交人数:" + mModel.getJoinList().getObjects().size() + "人");
        }

        list = new ArrayList<>();
        adapter = new ListViewPaperManagerAdapter(this, list, R.layout.item_listview_paper_mamager);
        listviewPaperManager.setAdapter(adapter);

        initData();

        swipyListviewPaperManager.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                initData();
            }
        });

        tvPaperManagerManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.showManager(true);
            }
        });
    }

    private void initData() {
        BmobQuery<PaperResultModel> query = new BmobQuery<>();
        query.order("-createdAt");
        query.addWhereEqualTo("fromAuthor", BmobUser.getCurrentUser(this, UserModel.class));
        query.addWhereEqualTo("title", mModel.getTitle());
        query.include("author,content");
        query.findObjects(this, new FindListener<PaperResultModel>() {
            @Override
            public void onSuccess(List<PaperResultModel> object) {
                list.clear();
                list.addAll(object);
                if (!list.isEmpty()) {
                    initView(list);
                } else {
                    Toast.makeText(PaperManagerActivity.this, "该测试暂无用户参与！", Toast.LENGTH_SHORT).show();
                }
                adapter.notifyDataSetChanged();
                swipyListviewPaperManager.setRefreshing(false);
            }

            @Override
            public void onError(int code, String msg) {
                Toast.makeText(PaperManagerActivity.this, msg, Toast.LENGTH_SHORT).show();
                swipyListviewPaperManager.setRefreshing(false);
            }
        });
    }

    private void initView(List<PaperResultModel> list) {
        ivPaperManagerCommitNum.setText("提交人数:" + list.size() + "人");
        double total = list.get(0).getScore();
        double max = list.get(0).getScore();
        double min = list.get(0).getScore();
        int maxIndex = 0;
        int minIndex = 0;
        for (int i = 1; i < list.size(); i++) {
            PaperResultModel model = list.get(i);
            total += model.getScore();
            if (max < model.getScore()) {
                max = model.getScore();
                maxIndex = i;
            }
            if (min > model.getScore()) {
                min = model.getScore();
                minIndex = i;
            }
        }
        ivPaperManagerHigtest.setText("成绩最高:" + max + "分");
        String hightestName = list.get(maxIndex).getAuthor().getNickName();
        if (TextUtils.isEmpty(hightestName)) {
            hightestName = list.get(maxIndex).getAuthor().getUsername();
        }
        ivPaperManagerHigtestName.setText("姓名:" + hightestName);
        ivPaperManagerLowest.setText("成绩最低:" + min + "分");
        String lowestName = list.get(minIndex).getAuthor().getNickName();
        if (TextUtils.isEmpty(lowestName)) {
            lowestName = list.get(minIndex).getAuthor().getUsername();
        }
        ivPaperManagerLowestName.setText("姓名:" + lowestName);
        ivPaperManagerAverage.setText("平均分:" + total / list.size() + "分");
    }


}
