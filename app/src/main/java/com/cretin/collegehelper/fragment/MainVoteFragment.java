package com.cretin.collegehelper.fragment;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.RelativeLayout;

import com.cretin.collegehelper.R;
import com.cretin.collegehelper.ui.CreateNewVoteActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_main_vote)
public class MainVoteFragment extends Fragment implements View.OnClickListener{
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
    public void init(){
        relaCreateVoteContainer.setOnClickListener(this);
        relaVoteManagerContainer.setOnClickListener(this);
        relaVoteTongjiContainer.setOnClickListener(this);
        relaVoteMyJoininContainer.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rela_create_vote_container:
                startActivity(new Intent(getActivity(), CreateNewVoteActivity_.class));
                break;
            case R.id.rela_vote_manager_container:
                break;
            case R.id.rela_vote_tongji_container:
                break;
            case R.id.rela_vote_my_joinin_container:
                break;
        }
    }
}
