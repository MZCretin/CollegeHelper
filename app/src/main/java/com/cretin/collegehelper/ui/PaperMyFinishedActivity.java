package com.cretin.collegehelper.ui;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.cretin.collegehelper.R;
import com.cretin.collegehelper.adapter.ListViewMyFinishedAdapter;
import com.cretin.collegehelper.model.PaperResultModel;
import com.cretin.collegehelper.model.UserModel;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

@EActivity(R.layout.activity_paper_my_finished)
public class PaperMyFinishedActivity extends AppCompatActivity {
    @ViewById
    ImageView ivMyFinishedBack;
    @ViewById
    ListView listviewMyFinished;
    @ViewById
    SwipyRefreshLayout swipyListviewMyFinished;
    private List<PaperResultModel> list;
    private ListViewMyFinishedAdapter adapter;

    @AfterViews
    public void init() {
        getSupportActionBar().hide();

        list = new ArrayList<>();
        adapter = new ListViewMyFinishedAdapter(this,list,R.layout.item_listview_paper_myfinished);
        listviewMyFinished.setAdapter(adapter);

        swipyListviewMyFinished.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                initData();
            }
        });

        initData();

        ivMyFinishedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PaperMyFinishedActivity.this.finish();
            }
        });

    }

    private void initData() {
        BmobQuery<PaperResultModel> query = new BmobQuery<>();
        query.order("-createdAt");
        query.addWhereEqualTo("author", BmobUser.getCurrentUser(this, UserModel.class));
        query.include("author,fromAuthor");
        query.findObjects(this, new FindListener<PaperResultModel>() {
            @Override
            public void onSuccess(List<PaperResultModel> object) {
                list.clear();
                list.addAll(object);
                adapter.notifyDataSetChanged();
                swipyListviewMyFinished.setRefreshing(false);
            }

            @Override
            public void onError(int code, String msg) {
                Toast.makeText(PaperMyFinishedActivity.this, msg, Toast.LENGTH_SHORT).show();
                swipyListviewMyFinished.setRefreshing(false);
            }
        });
    }
}
