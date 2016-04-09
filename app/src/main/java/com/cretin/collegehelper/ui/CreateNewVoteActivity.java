package com.cretin.collegehelper.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cretin.collegehelper.BaseApp;
import com.cretin.collegehelper.R;
import com.cretin.collegehelper.eventbus.NotifyAddMemberSuccess;
import com.cretin.collegehelper.model.UserModel;
import com.cretin.collegehelper.model.VoteModel;
import com.cretin.collegehelper.views.ShuoMClickableSpan;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.SaveListener;

@EActivity(R.layout.activity_create_new_vote)
public class CreateNewVoteActivity extends AppCompatActivity implements ShuoMClickableSpan.TextAreaClickListener {
    @ViewById
    ImageView ivCreateVotewBack;
    @ViewById
    TextView tvCreateVoteTitle;
    @ViewById
    EditText edCreateVoteName;
    @ViewById
    EditText edCreateVoteDes;
    @ViewById
    TextView tvCreateVoteDeclare;
    @ViewById
    TextView tvCreateVoteCreate;
    private List<UserModel> joinList;

    @AfterViews
    public void init() {
        getSupportActionBar().hide();

        SpannableString atUserSpann = new SpannableString("添加成员");
        ShuoMClickableSpan clickSpan = new ShuoMClickableSpan(this);
        atUserSpann.setSpan(clickSpan, 0, String.valueOf("添加成员").length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        clickSpan.setListener(this);
        tvCreateVoteDeclare.append(atUserSpann);
        tvCreateVoteDeclare.setMovementMethod(LinkMovementMethod.getInstance());

        ivCreateVotewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewVoteActivity.this.finish();
            }
        });

        tvCreateVoteCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createVote();
            }
        });
    }

    private void createVote() {
        String voteTitle = edCreateVoteName.getText().toString();
        String des = edCreateVoteDes.getText().toString();
        if (TextUtils.isEmpty(voteTitle)) {
            Toast.makeText(CreateNewVoteActivity.this, "投票名称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(des)) {
            Toast.makeText(CreateNewVoteActivity.this, "投票描述不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (joinList == null) {
            Toast.makeText(CreateNewVoteActivity.this, "请添加成员", Toast.LENGTH_SHORT).show();
            return;
        }

//        final VoteModel voteModel = new VoteModel();
//        voteModel.setJoinList(joinList);
//        voteModel.setCreateTime(System.currentTimeMillis());
//        voteModel.setUserName(BaseApp.getInstance().getUserModel().getUsername());
//        voteModel.setJoinCount(joinList.size());
//        voteModel.setVoteTitle(voteTitle);
//        voteModel.setVoteDestribe(des);
//        voteModel.save(this, new SaveListener() {
//            @Override
//
//            }
//
//            @Override
//            public void onFailure(int i, String s) {
//                Toast.makeText(CreateNewVoteActivity.this, s, Toast.LENGTH_SHORT).show();
//            }
//        });

        VoteModel voteModel = new VoteModel();
        voteModel.setCreateTime(System.currentTimeMillis());
        voteModel.setUserName(BaseApp.getInstance().getUserModel().getUsername());
        voteModel.setJoinCount(joinList.size());
        voteModel.setVoteTitle(voteTitle);
        voteModel.setVoteDestribe(des);
//将当前用户添加到Post表中的likes字段值中，表明当前用户喜欢该帖子
        BmobRelation relation = new BmobRelation();
//将当前用户添加到多对多关联中
        for (UserModel user : joinList) {
            relation.add(user);
        }
//多对多关联指向`post`的`likes`字段
        voteModel.setJoinList(relation);
        voteModel.save(this, new SaveListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                Log.i("life", "多对多关联添加成功");
                Toast.makeText(CreateNewVoteActivity.this, "创建成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int arg0, String arg1) {
                // TODO Auto-generated method stub
                Log.i("life", "多对多关联添加失败");
            }
        });

        CreateNewVoteActivity.this.finish();
    }

    @Override
    public void onTextClick() {
        startActivity(new Intent(this, AddMembersActivity_.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void addMemberNotify(NotifyAddMemberSuccess event) {
        joinList = event.getJoinList();
    }
}