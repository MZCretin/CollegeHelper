package com.cretin.collegehelper.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.cretin.collegehelper.R;
import com.cretin.collegehelper.adapter.ListViewPaperMyJoinAdapter;
import com.cretin.collegehelper.model.PaperSendModel;
import com.cretin.collegehelper.model.UserModel;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

@EActivity(R.layout.activity_paper_my_joined)
public class PaperMyJoinedActivity extends AppCompatActivity {

    @ViewById
    ImageView ivPaperMyjoinedBack;
    @ViewById
    ListView listviewPaperMyJoinin;
    @ViewById
    SwipyRefreshLayout swipyListviewPaperMyjoined;
    private List<PaperSendModel> list;
    private ListViewPaperMyJoinAdapter adapter;

    @AfterViews
    public void init() {
        getSupportActionBar().hide();

        list = new ArrayList<>();
        adapter = new ListViewPaperMyJoinAdapter(this, list, R.layout.item_listview_paper_myjoinin);
        listviewPaperMyJoinin.setAdapter(adapter);

        initData();

        swipyListviewPaperMyjoined.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                initData();
            }
        });

        listviewPaperMyJoinin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PaperMyJoinedActivity.this, PaperingActivity_.class);
                intent.putExtra("papermodel", list.get(position));
                startActivity(intent);
            }
        });
    }

    private void initData() {
        BmobQuery<UserModel> innerQuery = new BmobQuery<>();
        UserModel users = BmobUser.getCurrentUser(this, UserModel.class);
        String[] friendIds = {users.getObjectId()};//好友的objectId数组
        innerQuery.addWhereContainedIn("objectId", Arrays.asList(friendIds));
        //查询帖子
        BmobQuery<PaperSendModel> query = new BmobQuery<>();
        query.addWhereMatchesQuery("joinList", "_User", innerQuery);
        query.order("-createdAt");
        query.include("author");
        query.findObjects(this, new FindListener<PaperSendModel>() {
            @Override
            public void onSuccess(List<PaperSendModel> object) {
                list.clear();
                list.addAll(object);
                adapter.notifyDataSetChanged();
                swipyListviewPaperMyjoined.setRefreshing(false);
            }

            @Override
            public void onError(int code, String msg) {
                Toast.makeText(PaperMyJoinedActivity.this, msg, Toast.LENGTH_SHORT).show();
                swipyListviewPaperMyjoined.setRefreshing(false);
            }
        });
    }
}
