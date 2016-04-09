package com.cretin.collegehelper.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.cretin.collegehelper.BaseApp;
import com.cretin.collegehelper.R;
import com.cretin.collegehelper.adapter.VoteManagerListViewAdapter;
import com.cretin.collegehelper.model.VoteModel;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

@EActivity(R.layout.activity_vote_manger)
public class VoteMangerActivity extends AppCompatActivity implements SwipyRefreshLayout.OnRefreshListener{
    @ViewById
    ImageView ivAddMemberBack;
    @ViewById
    ListView listviewVoteManager;
    @ViewById
    SwipyRefreshLayout swipyListviewVoteManager;
    private VoteManagerListViewAdapter adapter;
    private List<VoteModel> list;

    @AfterViews
    public void init(){
        getSupportActionBar().hide();

        swipyListviewVoteManager.setOnRefreshListener(this);

        list = new ArrayList<>();
        adapter = new VoteManagerListViewAdapter(this,list,R.layout.item_listview_vote_manager);
        listviewVoteManager.setAdapter(adapter);

        initData();

//        VoteResultModel voteResult = new VoteResultModel();
//        voteResult.setVoteTitle("评选最佳班委");
//        voteResult.setVoteDes("请在班委中选出最好的和最差的！");
//        voteResult.setUserName("13687227320");
//        voteResult.setVoteContent("我觉得张三表现的不好的，也挺负责的，李四就挺不错的，不能以身作则。恰恰跟之前的是反的哦");
//        voteResult.setCreateUserName("18681522959");
//        voteResult.setVoteTime(System.currentTimeMillis());
//        voteResult.save(this, new SaveListener() {
//            @Override
//            public void onSuccess() {
//                Log.d("","");
//            }
//
//            @Override
//            public void onFailure(int i, String s) {
//                Log.d("","");
//            }
//        });

        listviewVoteManager.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(VoteMangerActivity.this,VoteDetailsActivity_.class);
                intent.putExtra("voteModel",list.get(position));
                startActivity(intent);
            }
        });

    }

    private void initData() {
        BmobQuery<VoteModel> query = new BmobQuery<>();
        query.addWhereEqualTo("userName", BaseApp.getInstance().getUserModel().getUsername());
        query.findObjects(this, new FindListener<VoteModel>() {
            @Override
            public void onSuccess(List<VoteModel> object) {
                swipyListviewVoteManager.setRefreshing(false);
                list.clear();
                list.addAll(object);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onError(int code, String msg) {
                swipyListviewVoteManager.setRefreshing(false);
                Toast.makeText(VoteMangerActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        initData();
    }
}
