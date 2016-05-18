package com.cretin.collegehelper.ui;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.cretin.collegehelper.R;
import com.cretin.collegehelper.adapter.VoteMyJoinUnFinishedAdapter;
import com.cretin.collegehelper.model.UserModel;
import com.cretin.collegehelper.model.VoteResultModel;
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

@EActivity(R.layout.activity_vote_my_join_in)
public class VoteMyJoinInActivity extends AppCompatActivity implements SwipyRefreshLayout.OnRefreshListener{
    @ViewById
    ImageView ivVoteMyJoininBack;
    @ViewById
    ListView listviewVoteMyJoinin;
    @ViewById
    SwipyRefreshLayout swipyListviewVoteMyJoinin;
    private List<VoteResultModel> list;
    private VoteMyJoinUnFinishedAdapter adapter;

    @AfterViews
    public void init(){
        getSupportActionBar().hide();
        list = new ArrayList<>();
        adapter = new VoteMyJoinUnFinishedAdapter(this,list,R.layout.item_listview_vote_unfinished);
        listviewVoteMyJoinin.setAdapter(adapter);

        swipyListviewVoteMyJoinin.setOnRefreshListener(this);

        initData();

        ivVoteMyJoininBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VoteMyJoinInActivity.this.finish();
            }
        });
    }

    private void initData() {
        BmobQuery<VoteResultModel> query = new BmobQuery<>();
        query.addWhereEqualTo("voteUser", BmobUser.getCurrentUser(this, UserModel.class));
        query.include("voteInfo,voteUser");
        query.findObjects(this, new FindListener<VoteResultModel>() {
            @Override
            public void onSuccess(List<VoteResultModel> object) {
                list.clear();
                list.addAll(object);
                adapter.notifyDataSetChanged();
                swipyListviewVoteMyJoinin.setRefreshing(false);
            }
            @Override
            public void onError(int code, String msg) {
                Toast.makeText(VoteMyJoinInActivity.this, msg, Toast.LENGTH_SHORT).show();
                swipyListviewVoteMyJoinin.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        initData();
    }
}
