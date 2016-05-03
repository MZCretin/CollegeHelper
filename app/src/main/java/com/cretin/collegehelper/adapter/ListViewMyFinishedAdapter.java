package com.cretin.collegehelper.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.cretin.collegehelper.R;
import com.cretin.collegehelper.model.PaperResultModel;

import java.util.List;

/**
 * Created by cretin on 4/26/16.
 */
public class ListViewMyFinishedAdapter extends CommonAdapter<PaperResultModel> {
    public ListViewMyFinishedAdapter(Context context, List<PaperResultModel> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
    }

    @Override
    public void convert(ViewHolders holder, PaperResultModel item, int position) {
        TextView tvTitle = holder.getView(R.id.tv_item_listview_paper_myfinished_title);
        TextView tvCreated = holder.getView(R.id.tv_item_listview_paper_myfinished_created);
        TextView tvCreate = holder.getView(R.id.tv_item_listview_paper_myfinished_create);
        TextView tvState = holder.getView(R.id.tv_item_listview_paper_myfinished_state);
        TextView tvScore = holder.getView(R.id.tv_item_listview_paper_myfinished_score);
        TextView tvAnswer = holder.getView(R.id.tv_item_listview_paper_myfinished_answer);

        tvTitle.setText(item.getTitle());
        String fromUser = item.getFromAuthor().getNickName();
        if (TextUtils.isEmpty(fromUser)) {
            fromUser = item.getFromAuthor().getUsername();
        }
        tvCreated.setText("创建人:" + fromUser);

        tvScore.setText("分数: " + item.getScore()+"分");

        tvAnswer.setText("你的答案:"+item.getAnswer());

        if (item.getScore() < 60) {
            //不合格
            tvState.setText("状态:不合格");
            tvState.setTextColor(mContext.getResources().getColor(R.color.red));
        } else {
            //合格
            tvState.setText("状态:合格");
            tvState.setTextColor(mContext.getResources().getColor(R.color.green));
        }

        tvCreate.setText(item.getPeriod());

    }
}
