package com.cretin.collegehelper.fragment;


import android.support.v4.app.Fragment;
import android.text.TextUtils;
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
import com.cretin.collegehelper.utils.DateUtils;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

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
public class MainMineFragment extends Fragment implements View.OnClickListener,SwipyRefreshLayout.OnRefreshListener{

    @ViewById
    TextView tvMineName;
    @ViewById
    ImageView ivMineSetting;
    @ViewById
    ImageView ivMineMore;
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

    public MainMineFragment() {

    }

    @AfterViews
    public void init(){
        list = new ArrayList<>();
        adapter = new MainDiscoverAdapter(getActivity(),list,R.layout.item_listview_discover);
        listviewMine.setAdapter(adapter);
        addHeadViews();
        meSwipeRefreshLayout.setOnRefreshListener(this);
        getData(mCursor);
        addHeadViewData();
    }

    private void addHeadViewData(){
        BmobQuery<UserModel> query = new BmobQuery<>();
        query.addWhereEqualTo("username", BmobUser.getCurrentUser(getActivity(), UserModel.class).getUsername());
        query.findObjects(getActivity(), new FindListener<UserModel>() {
            @Override
            public void onSuccess(List<UserModel> object) {
                meSwipeRefreshLayout.setRefreshing(false);
                if(object!=null&&!object.isEmpty()) {
                    UserModel userModel = object.get(0);
                    BaseApp.getInstance().setUserModel(userModel);
                    userId.setText("用户ID:"+userModel.getId());
                    userDes.setText(userModel.getSignature());
                    String nickName = userModel.getNickName();
                    if(TextUtils.isEmpty(nickName)){
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
                    flow.setText(userModel.getMembers().size()+"个");
                }
            }
            @Override
            public void onError(int code, String msg) {
                meSwipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
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

        icon.setOnClickListener(this);
        listviewMine.addHeaderView(view);
    }

    @Override
    public void onClick(View v) {

    }

    private void getData(final int cursor) {
        BmobQuery<FlowModel> query = new BmobQuery<>();
        query.include("author");
        query.setLimit(10);
        query.setSkip(cursor);
        query.addWhereEqualTo("author",new BmobPointer(BmobUser.getCurrentUser(getActivity(), UserModel.class)));
        query.order("-createdAt");
        query.findObjects(getActivity(), new FindListener<FlowModel>() {
            @Override
            public void onSuccess(List<FlowModel> object) {
                if (cursor == 0) {
                    list.clear();
                }
                mCursor += object.size();
                list.addAll(object);
                if(object.isEmpty()){
                    Toast.makeText(getActivity(), "没有更多数据", Toast.LENGTH_SHORT).show();
                }
                adapter.notifyDataSetChanged();
                meSwipeRefreshLayout.setRefreshing(false);
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
        if(SwipyRefreshLayoutDirection.TOP==direction){
            getData(0);
            addHeadViewData();
        }else if(SwipyRefreshLayoutDirection.BOTTOM==direction){
            getData(mCursor);
        }
    }
}
