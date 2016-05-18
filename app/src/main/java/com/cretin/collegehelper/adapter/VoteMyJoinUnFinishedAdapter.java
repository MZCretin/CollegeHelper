package com.cretin.collegehelper.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.cretin.collegehelper.R;
import com.cretin.collegehelper.model.VoteResultModel;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by cretin on 5/3/16.
 */
public class VoteMyJoinUnFinishedAdapter extends CommonAdapter<VoteResultModel> {
    private SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");

    public VoteMyJoinUnFinishedAdapter(Context context, List<VoteResultModel> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
    }

    @Override
    public void convert(ViewHolders holder, VoteResultModel item, int position) {
        TextView title = holder.getView(R.id.tv_vote_unfinished_title);
        TextView time = holder.getView(R.id.tv_vote_unfinished_time);
        TextView create = holder.getView(R.id.tv_vote_unfinished_author);
        TextView content = holder.getView(R.id.tv_vote_unfinished_content);
        TextView feedback = holder.getView(R.id.tv_vote_unfinished_feedback);

        title.setText(item.getVoteInfo().getVoteTitle());
        time.setText(format.format(item.getVoteInfo().getCreateTime()));
        String createName = item.getVoteUser().getNickName();
        if (TextUtils.isEmpty(createName)) {
            createName = item.getVoteUser().getUsername();
        }
        create.setText("创建人:" + createName);
        content.setText("投票内容:"+item.getVoteInfo().getVoteDestribe());
        feedback.setText("你的反馈:"+item.getUserVoteContent());
    }
}
