package com.cretin.collegehelper.adapter;

import android.content.Context;
import android.widget.TextView;

import com.cretin.collegehelper.R;
import com.cretin.collegehelper.model.VoteResultModel;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by cretin on 4/6/16.
 */
public class VoteDetailsListViewAdapter extends CommonAdapter<VoteResultModel>{
    public VoteDetailsListViewAdapter(Context context, List<VoteResultModel> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
    }

    @Override
    public void convert(ViewHolders holder, VoteResultModel item, int position) {
        ((TextView)holder.getView(R.id.tv_vote_details_id)).setText("用户ID:"+item.getAuthor().getUsername());
        SimpleDateFormat format=new SimpleDateFormat("yyyy/MM/dd HH:mm");
        ((TextView)holder.getView(R.id.tv_vote_details_time)).setText(format.format(item.getVoteTime()));
        ((TextView)holder.getView(R.id.tv_vote_details_title)).setText(item.getVoteTitle());
        ((TextView)holder.getView(R.id.tv_vote_details_des)).setText(item.getVoteDes());
        ((TextView)holder.getView(R.id.tv_vote_details_content)).setText(item.getVoteContent());
    }
}
