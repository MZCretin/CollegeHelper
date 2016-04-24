package com.cretin.collegehelper.ui;

import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cretin.collegehelper.BaseApp;
import com.cretin.collegehelper.R;
import com.cretin.collegehelper.adapter.AddMemberListViewAdapter;
import com.cretin.collegehelper.eventbus.NotifyAddMemberSuccess;
import com.cretin.collegehelper.model.UserModel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

@EActivity(R.layout.activity_add_members)
public class AddMembersActivity extends AppCompatActivity implements AddMemberListViewAdapter.FlagChangeListener, View.OnClickListener {
    @ViewById
    ImageView ivAddMemberBack;
    @ViewById
    TextView tvAddMemberAdd;
    @ViewById
    EditText edAddMemberName;
    @ViewById
    ListView listviewAddMemberMembers;
    @ViewById
    TextView tvAddMemberDone;
    private AddMemberListViewAdapter adapter;
    private List<UserModel> list = new ArrayList<>();
    private List<UserModel> listId = new ArrayList<>();

    @AfterViews
    public void init() {
        getSupportActionBar().hide();

        adapter = new AddMemberListViewAdapter(this, list, R.layout.item_add_member_listview);
        adapter.setListener(this);
        listviewAddMemberMembers.setAdapter(adapter);

        //从本地获取数据添加
        if (BaseApp.getInstance().getUserModel().getMembers() != null) {
            for (UserModel userModel : BaseApp.getInstance().getUserModel().getMembers()) {
                list.add(userModel);
            }
            adapter.notifyDataSetChanged();
        }


        //给EditText添加监听
        edAddMemberName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s.toString())) {
                    tvAddMemberAdd.setTextColor(getResources().getColor(R.color.main_back_pressed));
                    tvAddMemberAdd.setEnabled(true);
                } else {
                    tvAddMemberAdd.setTextColor(getResources().getColor(R.color.divider_color));
                    tvAddMemberAdd.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        addListener();
    }

    private void addListener() {
        ivAddMemberBack.setOnClickListener(this);
        tvAddMemberAdd.setOnClickListener(this);
        tvAddMemberDone.setOnClickListener(this);
    }

    @Override
    public void onFlagChangeListener(boolean flag, UserModel userId) {
        if (flag) {
            if (!listId.contains(userId))
                listId.add(userId);
        } else {
            if (listId.contains(userId))
                listId.remove(userId);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_add_member_back:
                this.finish();
                break;
            case R.id.tv_add_member_add:
                addMember();
                break;
            case R.id.tv_add_member_done:
                if (!listId.isEmpty()) {
                    addMemberSuccess();
                } else {
                    Toast.makeText(AddMembersActivity.this, "未选择任何成员", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void addMemberSuccess() {
        final UserModel userModel = BmobUser.getCurrentUser(this, UserModel.class);
        userModel.setMembers(listId);
        userModel.update(this, new UpdateListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(AddMembersActivity.this, "成员添加成功", Toast.LENGTH_SHORT).show();
                BaseApp.getInstance().setUserModel(userModel);
                NotifyAddMemberSuccess notifyAddMemberSuccess = new NotifyAddMemberSuccess();
                notifyAddMemberSuccess.setJoinList(listId);
                EventBus.getDefault().post(notifyAddMemberSuccess);
                AddMembersActivity.this.finish();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(AddMembersActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addMember() {
        String phone = edAddMemberName.getText().toString();
        BmobQuery<UserModel> query = new BmobQuery<UserModel>();
        query.addWhereEqualTo("username", phone);
        query.findObjects(this, new FindListener<UserModel>() {
            @Override
            public void onSuccess(List<UserModel> object) {
                if (object != null) {
                    edAddMemberName.setText("");
                    if (list.contains(object.get(0))) {
                        Toast.makeText(AddMembersActivity.this, "您已添加该用户", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    list.add(object.get(0));
                    adapter.notifyDataSetChanged();
                    Toast.makeText(AddMembersActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(int code, String msg) {
                Toast.makeText(AddMembersActivity.this, "未找到该用户", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
