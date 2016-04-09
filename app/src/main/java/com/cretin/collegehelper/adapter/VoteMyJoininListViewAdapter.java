package com.cretin.collegehelper.adapter;

import android.content.Context;
import android.widget.TextView;

import com.cretin.collegehelper.R;
import com.cretin.collegehelper.model.VoteModel;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by cretin on 4/5/16.
 */
public class VoteMyJoininListViewAdapter extends CommonAdapter<VoteModel>{
    public VoteMyJoininListViewAdapter(Context context, List<VoteModel> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
    }

    @Override
    public void convert(ViewHolders holder, VoteModel item, int position) {
        ((TextView)holder.getView(R.id.tv_vote_my_joinin_title)).setText(item.getVoteTitle());
        ((TextView)holder.getView(R.id.tv_vote_my_joinin_content)).setText(item.getVoteDestribe());
        SimpleDateFormat format=new SimpleDateFormat("yyyy/MM/dd hh:mm");
        ((TextView)holder.getView(R.id.tv_vote_my_joinin_time)).setText(format.format(item.getCreateTime()));
        ((TextView)holder.getView(R.id.tv_vote_my_joinin_author)).setText("创建人:"+item.getAuthor().getUsername());
    }
}
