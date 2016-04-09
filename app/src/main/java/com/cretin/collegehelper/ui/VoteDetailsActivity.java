package com.cretin.collegehelper.ui;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cretin.collegehelper.BaseApp;
import com.cretin.collegehelper.R;
import com.cretin.collegehelper.adapter.VoteDetailsListViewAdapter;
import com.cretin.collegehelper.model.VoteModel;
import com.cretin.collegehelper.model.VoteResultModel;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

@EActivity(R.layout.activity_vote_details)
public class VoteDetailsActivity extends AppCompatActivity implements SwipyRefreshLayout.OnRefreshListener{
    @ViewById
    ImageView ivCreateVotewBack;
    @ViewById
    TextView tvCreateVoteTitle;
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

    @AfterViews
    public void init(){
        getSupportActionBar().hide();

        swipyListviewVoteDetails.setOnRefreshListener(this);

        currVoteModel = (VoteModel) getIntent().getSerializableExtra("voteModel");
        if(currVoteModel!=null){
            tvCreateVoteTitle.setText(currVoteModel.getVoteTitle());
            tvCreateVoteDes.setText(currVoteModel.getVoteDestribe());
            SimpleDateFormat format=new SimpleDateFormat("yyyy/MM/dd HH:mm");
            tvCreateVoteTime.setText("创建时间:"+format.format(currVoteModel.getCreateTime()));
        }

        list = new ArrayList<>();
        adapter = new VoteDetailsListViewAdapter(this,list,R.layout.item_listview_vote_details);
        listviewVoteDetails.setAdapter(adapter);

        initData();

        ivCreateVotewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VoteDetailsActivity.this.finish();
            }
        });
    }

    private void initData() {
        BmobQuery<VoteResultModel> query = new BmobQuery<>();
        query.addWhereEqualTo("createUserName", BaseApp.getInstance().getUserModel().getUsername());
        query.addWhereEqualTo("voteTitle",currVoteModel.getVoteTitle());
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
