package com.cretin.collegehelper.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.cretin.collegehelper.R;
import com.cretin.collegehelper.adapter.ListViewPaperMyCreateAdapter;
import com.cretin.collegehelper.model.PaperSendModel;
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

@EActivity(R.layout.activity_paper_my_created)
public class PaperMyCreatedActivity extends AppCompatActivity {
    @ViewById
    ImageView ivPaperMyCreatedBack;
    @ViewById
    ListView listviewPaperMyCreated;
    @ViewById
    SwipyRefreshLayout swipyListviewMyCreated;
    private List<PaperSendModel> list;
    private ListViewPaperMyCreateAdapter adapter;

    @AfterViews
    public void init() {
        getSupportActionBar().hide();

        list = new ArrayList<>();
        adapter = new ListViewPaperMyCreateAdapter(this, list, R.layout.item_listview_paper_my_created);
        listviewPaperMyCreated.setAdapter(adapter);

        swipyListviewMyCreated.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                initData();
            }
        });

        initData();

        ivPaperMyCreatedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PaperMyCreatedActivity.this.finish();
            }
        });

        listviewPaperMyCreated.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PaperMyCreatedActivity.this,PaperDetailsActivity_.class);
                intent.putExtra("papersendmodel",list.get(position));
                startActivity(intent);
            }
        });
        
        listviewPaperMyCreated.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PaperMyCreatedActivity.this,PaperManagerActivity_.class);
                intent.putExtra("papersendmodel",list.get(position));
                startActivity(intent);
                return true;
            }
        });
    }

    private void initData() {
        BmobQuery<PaperSendModel> query = new BmobQuery<>();
        query.order("-createdAt");
        query.addWhereEqualTo("author", BmobUser.getCurrentUser(this, UserModel.class));
        query.include("author");
        query.findObjects(this, new FindListener<PaperSendModel>() {
            @Override
            public void onSuccess(List<PaperSendModel> object) {
                list.clear();
                list.addAll(object);
                adapter.notifyDataSetChanged();
                swipyListviewMyCreated.setRefreshing(false);
            }

            @Override
            public void onError(int code, String msg) {
                Toast.makeText(PaperMyCreatedActivity.this, msg, Toast.LENGTH_SHORT).show();
                swipyListviewMyCreated.setRefreshing(false);
            }
        });
    }
}
