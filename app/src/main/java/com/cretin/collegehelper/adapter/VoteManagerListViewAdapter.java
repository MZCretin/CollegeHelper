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
public class VoteManagerListViewAdapter extends CommonAdapter<VoteModel>{
    public VoteManagerListViewAdapter(Context context, List<VoteModel> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
    }

    @Override
    public void convert(ViewHolders holder, VoteModel item, int position) {
        ((TextView)holder.getView(R.id.tv_vote_manager_title)).setText(item.getVoteTitle());
        ((TextView)holder.getView(R.id.tv_vote_manager_des)).setText(item.getVoteDestribe());
        TextView verFlag = (TextView)holder.getView(R.id.tv_vote_manager_verflag);
        if(item.getVerifireFlag()==0){
            verFlag.setText("审核状态:"+"审核中...");
        }else if(item.getVerifireFlag()==1){
            verFlag.setText("审核状态:"+"审核成功");
        }else{
            verFlag.setText("审核状态:"+"审核失败");
        }
        SimpleDateFormat format=new SimpleDateFormat("yyyy/MM/dd HH:mm");
        ((TextView)holder.getView(R.id.tv_vote_manager_create_time)).setText(format.format(item.getCreateTime()));
    }
}
