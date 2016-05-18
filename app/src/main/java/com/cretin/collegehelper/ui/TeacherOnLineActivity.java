package com.cretin.collegehelper.ui;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cretin.collegehelper.R;
import com.cretin.collegehelper.adapter.ChatMsgViewAdapter;
import com.cretin.collegehelper.model.ChatMsgEntity;
import com.cretin.collegehelper.model.UserModel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

@EActivity(R.layout.activity_teacher_on_line)
public class TeacherOnLineActivity extends AppCompatActivity {
    @ViewById
    ImageView ivOnlineBack;
    @ViewById
    ListView listviewOnline;
    @ViewById
    Button btnSendLine;
    @ViewById
    EditText etSendmessageLine;
    private UserModel mUserModel;
    @ViewById
    RelativeLayout rlBottomLine;
    private ChatMsgViewAdapter mAdapter;// 消息视图的Adapter
    private List<ChatMsgEntity> mDataArrays = new ArrayList<ChatMsgEntity>();// 消息对象数组
    private String userName;
    private Timer timer;
    private String mUserNames;

    @AfterViews
    public void init() {
        getSupportActionBar().hide();

        mUserModel = BmobUser.getCurrentUser(this, UserModel.class);
        userName = mUserModel.getNickName();
        mUserNames = mUserModel.getUsername();
        if (mUserModel.getNickName().isEmpty()) {
            userName = mUserModel.getUsername();
        }

        mAdapter = new ChatMsgViewAdapter(this, mDataArrays);
        listviewOnline.setAdapter(mAdapter);

        btnSendLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                initData();
            }
        }, 1000, 3000);

        ivOnlineBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TeacherOnLineActivity.this.finish();
            }
        });
    }

    /**
     * 发送消息
     */
    private void send() {
        String contString = etSendmessageLine.getText().toString();
        if (contString.length() > 0) {
            ChatMsgEntity entity = new ChatMsgEntity();
            entity.setName(userName);
            entity.setUserName(mUserModel.getUsername());
            entity.setDate(getDate());
            entity.setMessage(contString);
            entity.setMsgType(false);
            entity.setAvator(BmobUser.getCurrentUser(this, UserModel.class).getAvater());
            entity.save(this, new SaveListener() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailure(int i, String s) {

                }
            });

            mDataArrays.add(entity);
            mAdapter.notifyDataSetChanged();// 通知ListView，数据已发生改变

            etSendmessageLine.setText("");// 清空编辑框数据

            listviewOnline.setSelection(listviewOnline.getCount() - 1);// 发送一条消息时，ListView显示选择最后一项
        }
    }

    /**
     * 发送消息时，获取当前事件
     *
     * @return 当前时间
     */
    private String getDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

    private void initData() {
        BmobQuery<ChatMsgEntity> query = new BmobQuery<>();
        query.order("createdAt");
        query.include("author");
        query.findObjects(this, new FindListener<ChatMsgEntity>() {
            @Override
            public void onSuccess(List<ChatMsgEntity> object) {
                mDataArrays.clear();
                addData(object);
            }

            @Override
            public void onError(int code, String msg) {
                Toast.makeText(TeacherOnLineActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addData(List<ChatMsgEntity> object) {
        if (object != null) {
            for (ChatMsgEntity c : object) {
                if (c.getUserName().equals(mUserNames)) {
                    c.setMsgType(false);
                } else {
                    c.setMsgType(true);
                }
                mDataArrays.add(c);
            }

            mAdapter.notifyDataSetChanged();
            listviewOnline.setSelection(listviewOnline.getCount() - 1);// 发送一条消息时，ListView显示选择最后一项
        }
    }
}
