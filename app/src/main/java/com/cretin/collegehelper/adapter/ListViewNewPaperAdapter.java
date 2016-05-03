package com.cretin.collegehelper.adapter;

import android.content.Context;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.cretin.collegehelper.R;
import com.cretin.collegehelper.model.PaperModel;

import java.util.List;

/**
 * Created by cretin on 4/23/16.
 */
public class ListViewNewPaperAdapter extends CommonAdapter<PaperModel> {
    public static final int TYPE_TEACHER = 0;
    public static final int TYPE_STUDENT = 1;
    public static final int TYPE_TEACHER_DETAILS = 2;
    private int type ;
    public ListViewNewPaperAdapter(Context context, List<PaperModel> mDatas, int itemLayoutId,int type) {
        super(context, mDatas, itemLayoutId);
        this.type = type;
    }

    @Override
    public void convert(ViewHolders holder, final PaperModel item, int position) {
        final RadioGroup radioGroup = holder.getView(R.id.rg_item_listview_new_paper);
        TextView title = holder.getView(R.id.tv_item_listview_new_paper_title);
        title.setText(item.getTitle());
        //解析答案
        String ssString = item.getAnswer();
        String strA = ssString.substring(ssString.indexOf("A)"), ssString.indexOf("B)"));
        String strB = ssString.substring(ssString.indexOf("B)"), ssString.indexOf("C)"));
        String strC = ssString.substring(ssString.indexOf("C)"), ssString.indexOf("D)"));
        String strD = ssString.substring(ssString.indexOf("D)"));
        ((RadioButton) radioGroup.getChildAt(0)).setText(strA);
        ((RadioButton) radioGroup.getChildAt(1)).setText(strB);
        ((RadioButton) radioGroup.getChildAt(2)).setText(strC);
        ((RadioButton) radioGroup.getChildAt(3)).setText(strD);

        radioGroup.setTag(item.getTitle());

        radioGroup.setOnCheckedChangeListener(null);
        radioGroup.clearCheck();

        if(type == TYPE_TEACHER){
            if (item.getCorrectAnswer() != 0) {
                ((RadioButton) radioGroup.getChildAt(item.getCorrectAnswer() - 1)).setChecked(true);
            }
        }else{
            if (item.getAnswerIndex() != 0) {
                ((RadioButton) radioGroup.getChildAt(item.getAnswerIndex() - 1)).setChecked(true);
            }
        }


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (group.getTag().equals(item.getTitle())) {
                    switch (checkedId) {
                        case R.id.rb_item_listview_new_paper_a:
                            if(type == TYPE_TEACHER){
                                item.setCorrectAnswer(1);
                            }else {
                                item.setAnswerIndex(1);
                            }
                            break;
                        case R.id.rb_item_listview_new_paper_b:
                            if(type == TYPE_TEACHER){
                                item.setCorrectAnswer(2);
                            }else {
                                item.setAnswerIndex(2);
                            }
                            break;
                        case R.id.rb_item_listview_new_paper_c:
                            if(type == TYPE_TEACHER){
                                item.setCorrectAnswer(3);
                            }else {
                                item.setAnswerIndex(3);
                            }
                            break;
                        case R.id.rb_item_listview_new_paper_d:
                            if(type == TYPE_TEACHER){
                                item.setCorrectAnswer(4);
                            }else {
                                item.setAnswerIndex(4);
                            }
                            break;
                    }
                }
            }
        });


    }
}
