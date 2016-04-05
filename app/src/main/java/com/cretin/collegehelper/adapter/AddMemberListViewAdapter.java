package com.cretin.collegehelper.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cretin.collegehelper.R;
import com.cretin.collegehelper.model.UserModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cretin on 16/1/27.
 */
public class AddMemberListViewAdapter extends CommonAdapter<UserModel> {
    private FlagChangeListener listener;
    private Map<Integer, Boolean> isCheckMap = new HashMap<Integer, Boolean>();

    public FlagChangeListener getListener() {
        return listener;
    }

    public void setListener(FlagChangeListener listener) {
        this.listener = listener;
    }

    public AddMemberListViewAdapter(Context context, List<UserModel> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
    }

    @Override
    public void convert(ViewHolders holder, final UserModel item, final int position) {
//        Picasso.with(mContext).load(ThumbnailUtils.getThumbnailIconSmall(item.getPersonality().getAvatar()))
//                .transform(new CircleTransform()).into((ImageView) holder.getView(R.id.item_iv_selectmember_icon));
        final ImageView imageFlag = holder.getView(R.id.item_iv_selectmember_flag);
        TextView nickName = holder.getView(R.id.item_tv_selectmember_name);
        TextView des = holder.getView(R.id.item_iv_selectmember_des);
        String nickNameStr = item.getNickName();
        if(TextUtils.isEmpty(nickNameStr)){
            nickNameStr = item.getUsername();
        }
        nickName.setText(nickNameStr);
        //找到需要选中的条目
        if (isCheckMap != null && isCheckMap.containsKey(position)) {
            imageFlag.setImageResource(R.mipmap.ok2x);
        } else {
            imageFlag.setImageResource(R.mipmap.okcopy2x);
        }
        des.setText(item.getSignature());
        imageFlag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCheckMap != null && isCheckMap.containsKey(position)) {
                    //将选中的放入hashmap中
                    isCheckMap.remove(position);
                    imageFlag.setImageResource(R.mipmap.okcopy2x);
                    if (listener != null) {
                        listener.onFlagChangeListener(false, item);
                    }
                } else {
                    //取消选中的则剔除
                    isCheckMap.put(position, true);
                    imageFlag.setImageResource(R.mipmap.ok2x);
                    if (listener != null) {
                        listener.onFlagChangeListener(true, item);
                    }
                }
            }
        });
    }

    public interface FlagChangeListener {
        void onFlagChangeListener(boolean flag, UserModel user);
    }
}
