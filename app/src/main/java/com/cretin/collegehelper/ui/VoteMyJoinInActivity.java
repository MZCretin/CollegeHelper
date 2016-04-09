package com.cretin.collegehelper.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.cretin.collegehelper.R;
import com.cretin.collegehelper.model.UserModel;
import com.cretin.collegehelper.model.VoteModel;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;

public class VoteMyJoinInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_my_join_in);

        init();
    }

    private void init() {
        // 查询喜欢这个帖子的所有用户，因此查询的是用户表
        BmobQuery<VoteModel> query = new BmobQuery<VoteModel>();
        UserModel post = BmobUser.getCurrentUser(this, UserModel.class);
        //likes是Post表中的字段，用来存储所有喜欢该帖子的用户
        query.addWhereRelatedTo("joinList", new BmobPointer(post));
        query.findObjects(this, new FindListener<VoteModel>() {

            @Override
            public void onSuccess(List<VoteModel> object) {
                // TODO Auto-generated method stub
                Log.i("life", "查询个数："+object.size());
            }

            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
                Log.i("life", "查询失败："+code+"-"+msg);
            }
        });
    }
}
