package com.cretin.collegehelper.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cretin.collegehelper.R;
import com.cretin.collegehelper.model.PaperResultModel;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by cretin on 4/26/16.
 */
public class ListViewPaperManagerAdapter extends CommonAdapter<PaperResultModel> {
    private SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
    private boolean[] flag;
    private boolean mFlags;

    public boolean ismFlags() {
        return mFlags;
    }

    public void setmFlags(boolean mFlags) {
        this.mFlags = mFlags;
    }

    public ListViewPaperManagerAdapter(Context context, List<PaperResultModel> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
    }

    @Override
    public void convert(ViewHolders holder, PaperResultModel item, final int position) {
        TextView tvCommit = holder.getView(R.id.tv_item_listview_paper_manager_commit);
        TextView tvCommiter = holder.getView(R.id.tv_item_listview_paper_manager_commiter);
        TextView tvScore = holder.getView(R.id.tv_item_listview_paper_manager_score);
        TextView tvState = holder.getView(R.id.tv_item_listview_paper_manager_state);
        TextView tvAnswerDes = holder.getView(R.id.tv_item_listview_paper_manager_answer_des);
        TextView tvAnswer = holder.getView(R.id.tv_item_listview_paper_manager_answer);
        final ImageView ivFlag = holder.getView(R.id.iv_item_listview_paper_flag);
        ImageView ivYellow = holder.getView(R.id.iv_item_listview_paper_manager_yellow);

        tvScore.setText("分数: " + item.getScore() + "分");

        tvAnswer.setText("你的答案:" + item.getAnswer());

        if (item.getScore() < 60) {
            //不合格
            tvState.setText("状态:不合格");
            tvState.setTextColor(mContext.getResources().getColor(R.color.red));
        } else {
            //合格
            tvState.setText("状态:合格");
            tvState.setTextColor(mContext.getResources().getColor(R.color.green));
        }

        tvCommit.setText("提交时间:" + format.format(item.getCommitTime()));

        tvAnswerDes.setText("答案描述:" + item.getAnswerDes());

        String commiter = item.getAuthor().getNickName();
        if (TextUtils.isEmpty(commiter)) {
            commiter = item.getAuthor().getUsername();
        }
        tvCommiter.setText("提交用户:" + commiter);

        if (mFlags) {
            if (flag != null && flag.length != 0) {
                ivFlag.setVisibility(View.VISIBLE);
                ivYellow.setVisibility(View.GONE);
                if (flag[position]) {
                    ivFlag.setImageResource(R.mipmap.ok2x);
                } else {
                    ivFlag.setImageResource(R.mipmap.okcopy2x);
                }
            } else {
                flag = new boolean[mDatas.size()];
                ivFlag.setVisibility(View.GONE);
                ivYellow.setVisibility(View.VISIBLE);
            }

            ivFlag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (flag[position]) {
                        flag[position] = false;
                        ivFlag.setImageResource(R.mipmap.okcopy2x);
                    } else {
                        flag[position] = true;
                        ivFlag.setImageResource(R.mipmap.ok2x);
                    }
                }
            });
        } else {
            ivFlag.setVisibility(View.GONE);
            ivYellow.setVisibility(View.VISIBLE);
        }
    }

    public void showManager(boolean flags) {
        mFlags = flags;
        notifyDataSetChanged();
    }
}
