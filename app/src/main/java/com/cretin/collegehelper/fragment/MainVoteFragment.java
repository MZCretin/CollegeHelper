package com.cretin.collegehelper.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.RelativeLayout;

import com.cretin.collegehelper.R;
import com.cretin.collegehelper.ui.CreateNewVoteActivity_;
import com.cretin.collegehelper.ui.VoteMangerActivity_;
import com.cretin.collegehelper.ui.VoteMyJoinInActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_main_vote)
public class MainVoteFragment extends Fragment implements View.OnClickListener {
    @ViewById
    RelativeLayout relaCreateVoteContainer;
    @ViewById
    RelativeLayout relaVoteManagerContainer;
    @ViewById
    RelativeLayout relaVoteTongjiContainer;
    @ViewById
    RelativeLayout relaVoteMyJoininContainer;

    public MainVoteFragment() {
    }

    @AfterViews
    public void init() {
        relaCreateVoteContainer.setOnClickListener(this);
        relaVoteManagerContainer.setOnClickListener(this);
        relaVoteTongjiContainer.setOnClickListener(this);
        relaVoteMyJoininContainer.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rela_create_vote_container:
                startActivity(new Intent(getActivity(), CreateNewVoteActivity_.class));
                break;
            case R.id.rela_vote_manager_container:
                startActivity(new Intent(getActivity(), VoteMangerActivity_.class));
                break;
            case R.id.rela_vote_tongji_container:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("由于版本改动，萌萌哒的我已经将统计功能合并到投票管理里面啦！您可以通过投票管理中的导出功能，得到最后的统计材料文件！");
                builder.setTitle("提示");
                builder.setPositiveButton("我知道啦", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                break;
            case R.id.rela_vote_my_joinin_container:
                startActivity(new Intent(getActivity(), VoteMyJoinInActivity_.class));
                break;
        }
    }
}
