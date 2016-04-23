package com.cretin.collegehelper.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cretin.collegehelper.R;
import com.cretin.collegehelper.adapter.MainDiscoverAdapter;
import com.cretin.collegehelper.eventbus.NotifyCommentResult;
import com.cretin.collegehelper.model.CommentModel;
import com.cretin.collegehelper.model.FlowModel;
import com.cretin.collegehelper.model.UserModel;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

@EFragment(R.layout.fragment_main_home)
public class MainHomeFragment extends Fragment implements SwipyRefreshLayout.OnRefreshListener {
    @ViewById
    ListView listviewMainDiscover;
    @ViewById
    SwipyRefreshLayout swipyListviewDiscover;
    @ViewById
    EditText edittextCommentAddcomment;
    @ViewById
    Button btnCommentSend;
    @ViewById
    RelativeLayout relaComment;
    private int mCursor;
    private MainDiscoverAdapter adapter;
    private List<FlowModel> list;
    private FlowModel mFlowModel;
    private int currCommentPosition = -1;

    public MainHomeFragment() {
    }

    @AfterViews
    public void init() {
        swipyListviewDiscover.setOnRefreshListener(this);

        list = new ArrayList<>();
        adapter = new MainDiscoverAdapter(getActivity(), list, R.layout.item_listview_discover,0);
        listviewMainDiscover.setAdapter(adapter);

        getData(mCursor);

        btnCommentSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendComment();
            }
        });

        listviewMainDiscover.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(currCommentPosition>firstVisibleItem+visibleItemCount||
                        currCommentPosition<firstVisibleItem){
                    if(relaComment.getVisibility()==View.VISIBLE){
                        relaComment.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    //发送评论
    private void sendComment() {
        String content  = edittextCommentAddcomment.getText().toString();
        if(TextUtils.isEmpty(content)){
            Toast.makeText(getActivity(), "评论内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        UserModel user = BmobUser.getCurrentUser(getActivity(), UserModel.class);
        CommentModel commentModel = new CommentModel();
        commentModel.setComment(content);
        commentModel.setFromUser(user);
        commentModel.setCreated(System.currentTimeMillis());
        List<CommentModel> list = mFlowModel.getCommentModelList();
        if(list == null){
            list = new ArrayList<>();
        }
        list.add(commentModel);
        mFlowModel.setCommentModelList(list);
        mFlowModel.update(getActivity(), new UpdateListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(getActivity(), "评论成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
            }
        });
        edittextCommentAddcomment.setText("");
        relaComment.setVisibility(View.GONE);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void getData(final int cursor) {
        BmobQuery<FlowModel> query = new BmobQuery<>();
        query.include("author");
        query.setLimit(10);
        query.setSkip(cursor);
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
                swipyListviewDiscover.setRefreshing(false);
            }

            @Override
            public void onError(int code, String msg) {
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                swipyListviewDiscover.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        if (SwipyRefreshLayoutDirection.TOP == direction) {
            getData(0);
            mCursor = 0;
        } else if (SwipyRefreshLayoutDirection.BOTTOM == direction) {
            getData(mCursor);
        }
    }

    @Subscribe
    public void notifyComment(NotifyCommentResult event) {
        String hint;
        if (relaComment.getVisibility() == View.GONE) {
            relaComment.setVisibility(View.VISIBLE);
        }else {
            relaComment.setVisibility(View.GONE);
        }
        if(currCommentPosition != event.getPosition()){
            relaComment.setVisibility(View.VISIBLE);
            currCommentPosition = event.getPosition();
            mFlowModel = event.getFlowModel();
            edittextCommentAddcomment.setText("");
            if (TextUtils.isEmpty(event.getFlowModel().getAuthor().getNickName())) {
                hint = event.getFlowModel().getAuthor().getUsername();
            } else {
                hint = event.getFlowModel().getAuthor().getNickName();
            }
            edittextCommentAddcomment.setHint("评论:" + hint);
        }
    }
}
