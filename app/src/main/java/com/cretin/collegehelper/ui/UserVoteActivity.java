package com.cretin.collegehelper.ui;

import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cretin.collegehelper.R;
import com.cretin.collegehelper.model.UserModel;
import com.cretin.collegehelper.model.VoteModel;
import com.cretin.collegehelper.model.VoteResultModel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;


@EActivity(R.layout.activity_user_vote)
public class UserVoteActivity extends AppCompatActivity {

    @ViewById
    ImageView ivVotingBack;
    @ViewById
    TextView tvVotingCommit;
    @ViewById
    TextView tvVotingTitle;
    @ViewById
    TextView tvVotingContent;
    @ViewById
    TextView tvVotingAuthor;
    @ViewById
    EditText edVotingUserContent;
    private VoteModel voteModel;

    @AfterViews
    public void init() {
        getSupportActionBar().hide();

        voteModel = (VoteModel) getIntent().getSerializableExtra("info");
        if (voteModel != null) {
            tvVotingAuthor.setText("创建人:" + voteModel.getAuthor().getUsername());
            tvVotingTitle.setText(voteModel.getVoteTitle());
            tvVotingContent.setText(voteModel.getVoteDestribe());
        }

        addListener();
    }

    private void addListener() {
        ivVotingBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserVoteActivity.this.finish();
            }
        });

        tvVotingCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commit();
            }
        });
    }

    /**
     * 提交数据
     */
    private void commit() {
        String content = edVotingUserContent.getText().toString();
        if(TextUtils.isEmpty(content)){
            Toast.makeText(UserVoteActivity.this, "不能提交空数据", Toast.LENGTH_SHORT).show();
            return;
        }

        VoteResultModel voteResult = new VoteResultModel();
        voteResult.setVoteInfo(voteModel);
        voteResult.setVoteTime(System.currentTimeMillis());
        voteResult.setUserVoteContent(content);
        voteResult.setVoteUser(BmobUser.getCurrentUser(this, UserModel.class));
        voteResult.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(UserVoteActivity.this, "投票成功！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(UserVoteActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
