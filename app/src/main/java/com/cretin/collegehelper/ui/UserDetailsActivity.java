package com.cretin.collegehelper.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cretin.collegehelper.BaseApp;
import com.cretin.collegehelper.R;
import com.cretin.collegehelper.adapter.MainDiscoverAdapter;
import com.cretin.collegehelper.eventbus.NotifyCommentResult;
import com.cretin.collegehelper.model.CommentModel;
import com.cretin.collegehelper.model.FlowModel;
import com.cretin.collegehelper.model.UserModel;
import com.cretin.collegehelper.utils.BigBitmapUtils;
import com.cretin.collegehelper.utils.DateUtils;
import com.cretin.collegehelper.views.CircleTransform;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

@EActivity(R.layout.activity_user_details)
public class UserDetailsActivity extends AppCompatActivity implements SwipyRefreshLayout.OnRefreshListener {
    @ViewById
    ImageView ivUserDetailsInfoBack;
    @ViewById
    TextView tvUserDetailInfoName;
    @ViewById
    ListView listviewUserDetailInfo;
    @ViewById
    SwipyRefreshLayout userinfoSwipeRefreshLayout;
    @ViewById
    EditText edittextCommentAddcommentDetails;
    @ViewById
    Button btnCommentSendDetails;
    @ViewById
    RelativeLayout relaCommentDetails;
    private FlowModel mFlowModel;
    private int currCommentPosition = -1;
    private ImageView icon;
    private TextView userId;
    private TextView userDes;
    private TextView flow;
    private TextView age;
    private UserModel mUserModel;
    private int mCursor;
    private MainDiscoverAdapter adapter;
    private List<FlowModel> list;


    @AfterViews
    public void init() {
        getSupportActionBar().hide();

        mUserModel = (UserModel) getIntent().getSerializableExtra("usermodel");

        list = new ArrayList<>();
        adapter = new MainDiscoverAdapter(this, list, R.layout.item_listview_discover, MainDiscoverAdapter.TYPE_USER_DETAILS, NotifyCommentResult.TYPE_INFO_ACTIVITY);
        listviewUserDetailInfo.setAdapter(adapter);

        addHeadViews();

        if (mUserModel != null) {
            addHeadViewData(mUserModel);
            getData(0, mUserModel);
        }

        userinfoSwipeRefreshLayout.setOnRefreshListener(this);

        ivUserDetailsInfoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDetailsActivity.this.finish();
            }
        });

        btnCommentSendDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendComment();
            }
        });

        listviewUserDetailInfo.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (currCommentPosition > firstVisibleItem + visibleItemCount ||
                        currCommentPosition < firstVisibleItem) {
                    if (relaCommentDetails.getVisibility() == View.VISIBLE) {
                        relaCommentDetails.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void addHeadViewData(final UserModel userModel) {
        BmobQuery<UserModel> query = new BmobQuery<>();
        query.addWhereEqualTo("username", userModel.getUsername());
        query.findObjects(this, new FindListener<UserModel>() {
            @Override
            public void onSuccess(List<UserModel> object) {
                userinfoSwipeRefreshLayout.setRefreshing(false);
                if (object != null && !object.isEmpty()) {
                    UserModel userModel = object.get(0);
                    BaseApp.getInstance().setUserModel(userModel);
                    userId.setText("用户ID:" + userModel.getId());
                    userDes.setText(userModel.getSignature());
                    String nickName = userModel.getNickName();
                    if (TextUtils.isEmpty(nickName)) {
                        nickName = userModel.getUsername();
                    }
                    tvUserDetailInfoName.setText(nickName);
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
                userinfoSwipeRefreshLayout.setRefreshing(false);
                Toast.makeText(UserDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

        if (TextUtils.isEmpty(userModel.getAvater())) {
            Picasso.with(this).load(R.mipmap.default_icon).transform(new CircleTransform()).into(icon);
        } else {
            Picasso.with(this).load(userModel.getAvater()).transform(new CircleTransform()).into(icon);
        }

        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> listUrl = new ArrayList<>();
                if (!TextUtils.isEmpty(userModel.getAvater())) {
                    listUrl.add(userModel.getAvater());
                    BigBitmapUtils.seeBigBitmap(0, listUrl, UserDetailsActivity.this, false);
                } else {
                    listUrl.add(userModel.getAvater());
                    BigBitmapUtils.seeBigBitmap(0, listUrl, UserDetailsActivity.this, true);
                }
            }
        });
    }

    private void addHeadViews() {
        View view = LayoutInflater.from(this).inflate(R.layout.item_listview_mine_head, null);
        icon = (ImageView) view.findViewById(R.id.iv_mine_user_icon);
        userId = (TextView) view.findViewById(R.id.tv_mine_user_nickname);
        userDes = (TextView) view.findViewById(R.id.tv_mine_user_des);
        flow = (TextView) view.findViewById(R.id.tv_mine_flow);
        age = (TextView) view.findViewById(R.id.tv_mine_age);

        listviewUserDetailInfo.addHeaderView(view);
    }

    private void getData(final int cursor, UserModel userModel) {
        BmobQuery<FlowModel> query = new BmobQuery<>();
        query.include("author");
        query.setLimit(10);
        query.setSkip(cursor);
        query.addWhereEqualTo("author", userModel);
        query.order("-createdAt");
        query.findObjects(this, new FindListener<FlowModel>() {
            @Override
            public void onSuccess(List<FlowModel> object) {
                if (cursor == 0) {
                    list.clear();
                }
                mCursor += object.size();
                list.addAll(object);
                if (object.isEmpty()) {
                    Toast.makeText(UserDetailsActivity.this, "没有更多数据", Toast.LENGTH_SHORT).show();
                }
                adapter.notifyDataSetChanged();
                userinfoSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(int code, String msg) {
                Toast.makeText(UserDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
                userinfoSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        if (SwipyRefreshLayoutDirection.TOP == direction) {
            getData(0, mUserModel);
            addHeadViewData(mUserModel);
        } else if (SwipyRefreshLayoutDirection.BOTTOM == direction) {
            getData(mCursor, mUserModel);
        }
    }

    //发送评论
    private void sendComment() {
        String content = edittextCommentAddcommentDetails.getText().toString();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, "评论内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        UserModel user = BmobUser.getCurrentUser(this, UserModel.class);
        CommentModel commentModel = new CommentModel();
        commentModel.setComment(content);
        commentModel.setFromUser(user);
        commentModel.setCreated(System.currentTimeMillis());
        List<CommentModel> list = mFlowModel.getCommentModelList();
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(commentModel);
        mFlowModel.setCommentModelList(list);
        mFlowModel.update(this, new UpdateListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(UserDetailsActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(UserDetailsActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
        edittextCommentAddcommentDetails.setText("");
        relaCommentDetails.setVisibility(View.GONE);
    }

    @Subscribe
    public void notifyComment(NotifyCommentResult event) {
        if (event.getType() == NotifyCommentResult.TYPE_INFO_ACTIVITY) {
            String hint;
            if (relaCommentDetails.getVisibility() == View.GONE) {
                relaCommentDetails.setVisibility(View.VISIBLE);
            } else {
                relaCommentDetails.setVisibility(View.GONE);
            }
            if (currCommentPosition != event.getPosition()) {
                relaCommentDetails.setVisibility(View.VISIBLE);
                currCommentPosition = event.getPosition();
                mFlowModel = event.getFlowModel();
                edittextCommentAddcommentDetails.setText("");
                if (TextUtils.isEmpty(event.getFlowModel().getAuthor().getNickName())) {
                    hint = event.getFlowModel().getAuthor().getUsername();
                } else {
                    hint = event.getFlowModel().getAuthor().getNickName();
                }
                edittextCommentAddcommentDetails.setHint("评论:" + hint);
            }
        }
    }

}
