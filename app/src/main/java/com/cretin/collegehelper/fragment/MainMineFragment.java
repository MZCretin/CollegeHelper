package com.cretin.collegehelper.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cretin.collegehelper.BaseApp;
import com.cretin.collegehelper.R;
import com.cretin.collegehelper.adapter.MainDiscoverAdapter;
import com.cretin.collegehelper.model.FlowModel;
import com.cretin.collegehelper.model.UserModel;
import com.cretin.collegehelper.popwindow.SelectPopupWindow;
import com.cretin.collegehelper.ui.AboutActivity;
import com.cretin.collegehelper.ui.LoginActivity_;
import com.cretin.collegehelper.ui.UpdateUserInfoActivity_;
import com.cretin.collegehelper.utils.BigBitmapUtils;
import com.cretin.collegehelper.utils.DateUtils;
import com.cretin.collegehelper.views.CircleTransform;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;

@EFragment(R.layout.fragment_main_mine)
public class MainMineFragment extends Fragment implements SwipyRefreshLayout.OnRefreshListener, SelectPopupWindow.OnPopWindowClickListener {
    @ViewById
    TextView tvMineName;
    @ViewById
    ImageView ivMineSetting;
    @ViewById
    ListView listviewMine;
    @ViewById
    SwipyRefreshLayout meSwipeRefreshLayout;
    private ImageView icon;
    private TextView userId;
    private TextView userDes;
    private TextView flow;
    private TextView age;
    private MainDiscoverAdapter adapter;
    private List<FlowModel> list;
    private int mCursor;
    private SelectPopupWindow menuWindow;
    private UserModel userModel;

    public MainMineFragment() {

    }

    @AfterViews
    public void init() {
        list = new ArrayList<>();
        adapter = new MainDiscoverAdapter(getActivity(), list, R.layout.item_listview_discover, MainDiscoverAdapter.TYPE_USER_DETAILS);
        listviewMine.setAdapter(adapter);
        addHeadViews();
        meSwipeRefreshLayout.setOnRefreshListener(this);
        getData(mCursor);
        addHeadViewData();

        ivMineSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopWindow(getActivity());
            }
        });
    }

    //显示对话框
    private void showPopWindow(Activity activity) {
        menuWindow = new SelectPopupWindow(activity, this, SelectPopupWindow.TYPE_SETTING);
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int winHeight = activity.getWindow().getDecorView().getHeight();
        menuWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM, 0, winHeight - rect.bottom);
    }

    private void addHeadViewData() {
        BmobQuery<UserModel> query = new BmobQuery<>();
        query.addWhereEqualTo("username", BmobUser.getCurrentUser(getActivity(), UserModel.class).getUsername());
        query.findObjects(getActivity(), new FindListener<UserModel>() {
            @Override
            public void onSuccess(List<UserModel> object) {
                meSwipeRefreshLayout.setRefreshing(false);
                if (object != null && !object.isEmpty()) {
                    userModel = object.get(0);
                    BaseApp.getInstance().setUserModel(userModel);
                    userId.setText("用户ID:" + userModel.getId());
                    userDes.setText(userModel.getSignature());
                    String nickName = userModel.getNickName();
                    if (TextUtils.isEmpty(nickName)) {
                        nickName = userModel.getUsername();
                    }
                    tvMineName.setText(nickName);
                    String timeStr;
                    long temp = System.currentTimeMillis() - DateUtils.dataTurntoInt(userModel.getCreatedAt());
                    if (temp < (long) 60 * 60 * 24 * 30 * 1000) {
                        timeStr = (int) (temp / 1000 / 60 / 60 / 24) + "天";
                    } else {
                        timeStr = (int) (temp / 1000 / 60 / 60 / 24 / 30) + "月";
                    }
                    age.setText(timeStr);
                    if (userModel.getMembers() != null)
                        flow.setText(userModel.getMembers().size() + "个");
                }
            }

            @Override
            public void onError(int code, String msg) {
                meSwipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            }
        });

        if (TextUtils.isEmpty(BmobUser.getCurrentUser(getActivity(), UserModel.class).getAvater())) {
            Picasso.with(getActivity()).load(R.mipmap.default_icon).transform(new CircleTransform()).into(icon);
        } else {
            Picasso.with(getActivity()).load(BmobUser.getCurrentUser(getActivity(), UserModel.class).getAvater()).transform(new CircleTransform()).into(icon);
        }

        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> listUrl = new ArrayList<>();
                if (!TextUtils.isEmpty(BmobUser.getCurrentUser(getActivity(), UserModel.class).getAvater())) {
                    listUrl.add(BmobUser.getCurrentUser(getActivity(), UserModel.class).getAvater());
                    BigBitmapUtils.seeBigBitmap(0, listUrl, getActivity(), false);
                } else {
                    listUrl.add(BmobUser.getCurrentUser(getActivity(), UserModel.class).getAvater());
                    BigBitmapUtils.seeBigBitmap(0, listUrl, getActivity(), true);
                }
            }
        });
    }

    private void addHeadViews() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_listview_mine_head, null);
        icon = (ImageView) view.findViewById(R.id.iv_mine_user_icon);
        userId = (TextView) view.findViewById(R.id.tv_mine_user_nickname);
        userDes = (TextView) view.findViewById(R.id.tv_mine_user_des);
        flow = (TextView) view.findViewById(R.id.tv_mine_flow);
        age = (TextView) view.findViewById(R.id.tv_mine_age);

        listviewMine.addHeaderView(view);
    }


    private void getData(final int cursor) {
        BmobQuery<FlowModel> query = new BmobQuery<>();
        query.include("author");
        query.setLimit(10);
        query.setSkip(cursor);
        query.addWhereEqualTo("author", new BmobPointer(BmobUser.getCurrentUser(getActivity(), UserModel.class)));
        query.order("-createdAt");
        query.findObjects(getActivity(), new FindListener<FlowModel>() {
            @Override
            public void onSuccess(List<FlowModel> object) {
                meSwipeRefreshLayout.setRefreshing(false);
                if (object.isEmpty()) {
                    Toast.makeText(getActivity(), "没有更多数据", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (cursor == 0) {
                    list.clear();
                }
                mCursor += object.size();
                list.addAll(object);

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int code, String msg) {
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                meSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        if (SwipyRefreshLayoutDirection.TOP == direction) {
            getData(0);
            addHeadViewData();
        } else if (SwipyRefreshLayoutDirection.BOTTOM == direction) {
            getData(mCursor);
        }
    }

    @Override
    public void onPopWindowClickListener(View view) {
        switch (view.getId()) {
            case R.id.btn_setting_update:
                Intent intent = new Intent(getActivity(), UpdateUserInfoActivity_.class);
                if (userModel == null) {
                    userModel = BmobUser.getCurrentUser(getActivity(), UserModel.class);
                }
                intent.putExtra("usermodel", userModel);
                startActivity(intent);
                break;
            case R.id.btn_setting_exit:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("退出当前登录？");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BmobUser.logOut(getActivity());   //清除缓存用户对象
                        BaseApp.getInstance().setUserModel(null);
                        Intent intent = new Intent(getActivity(), LoginActivity_.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                break;
            case R.id.btn_setting_about:
                startActivity(new Intent(getActivity(),AboutActivity.class));
                break;
        }
    }
}
