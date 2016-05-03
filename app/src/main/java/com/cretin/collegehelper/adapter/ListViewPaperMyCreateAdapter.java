package com.cretin.collegehelper.adapter;

import android.content.Context;
import android.widget.TextView;

import com.cretin.collegehelper.R;
import com.cretin.collegehelper.model.PaperSendModel;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by cretin on 4/26/16.
 */
public class ListViewPaperMyCreateAdapter extends CommonAdapter<PaperSendModel>{
    private SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
    public ListViewPaperMyCreateAdapter(Context context, List<PaperSendModel> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
    }

    @Override
    public void convert(ViewHolders holder, PaperSendModel item, int position) {
        TextView tvTitle = holder.getView(R.id.tv_item_listview_paper_mycreate_title);
        TextView tvState = holder.getView(R.id.tv_item_listview_paper_mycreate_state);
        TextView tvCreate = holder.getView(R.id.tv_item_listview_paper_mycreate_create);

        tvTitle.setText(item.getTitle());

        long tempTime = item.getCreateTime() + (item.getPeriod() * 1000 * 60);
        String strPeriod = format.format(item.getCreateTime()) + "~" + format.format(tempTime);
        tvCreate.setText(strPeriod);

        tvState.setText("总体量:"+item.getTestContent().size()+"题");
    }
}
