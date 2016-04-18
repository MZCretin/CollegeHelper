package com.cretin.collegehelper.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
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


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("当您提交一个投票的内容之后,我们将在2个工作日内审核您的投票;当审核通过之后,您添加的成员才会在他自己的用户系统下看到您新建的投票;注意,如果您提交了违法内容,将审核不通过;多次这样将直接导致您无法再使用该系统");
        builder.setTitle("提示");
        builder.setPositiveButton("了解", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void createVote() {
        final String voteTitle = edCreateVoteName.getText().toString();
        final String des = edCreateVoteDes.getText().toString();
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
        VoteModel voteModel = new VoteModel();
        voteModel.setCreateTime(System.currentTimeMillis());
        voteModel.setJoinCount(joinList.size());
        voteModel.setVoteTitle(voteTitle);
        voteModel.setVoteDestribe(des);
        BmobRelation relation = new BmobRelation();
        for (UserModel user : joinList) {
            relation.add(user);
        }
        voteModel.setJoinList(relation);
        UserModel users = BaseApp.getInstance().getUserModel();
        voteModel.setAuthor(users);
        voteModel.save(CreateNewVoteActivity.this, new SaveListener() {

            @Override
            public void onSuccess() {
                Toast.makeText(CreateNewVoteActivity.this, "创建成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int arg0, String arg1) {
                Toast.makeText(CreateNewVoteActivity.this, arg1, Toast.LENGTH_SHORT).show();
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
