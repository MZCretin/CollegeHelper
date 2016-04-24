package com.cretin.collegehelper.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.cretin.collegehelper.R;
import com.cretin.collegehelper.model.PaperSendModel;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by cretin on 4/24/16.
 */
public class ListViewPaperMyJoinAdapter extends CommonAdapter<PaperSendModel> {
    private SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");

    public ListViewPaperMyJoinAdapter(Context context, List<PaperSendModel> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
    }

    @Override
    public void convert(ViewHolders holder, PaperSendModel item, int position) {
        TextView title = holder.getView(R.id.tv_item_listview_paper_myjoinin_title);
        TextView period = holder.getView(R.id.tv_item_listview_paper_myjoinin_create);
        TextView author = holder.getView(R.id.tv_item_listview_paper_myjoinin_created);
        TextView state = holder.getView(R.id.tv_item_listview_paper_myjoinin_state);

        title.setText(item.getTitle());
        long tempTime = item.getCreateTime() + (item.getPeriod() * 1000 * 60);
        String strPeriod = format.format(item.getCreateTime()) + "~" + format.format(tempTime);
        period.setText(strPeriod);
        String userName = item.getAuthor().getNickName();
        if (TextUtils.isEmpty(userName)) {
            userName = item.getAuthor().getUsername();
        }
        author.setText("创建人:"+userName);
        long systemTime = System.currentTimeMillis();
        if (systemTime <= tempTime && systemTime >= item.getCreateTime()) {
            state.setText("状态:" + "未过期");
        } else {
            state.setText("状态:" + "已过期");
        }

    }
}
