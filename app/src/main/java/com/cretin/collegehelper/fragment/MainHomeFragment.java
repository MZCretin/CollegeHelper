package com.cretin.collegehelper.fragment;


import android.support.v4.app.Fragment;
import android.widget.ListView;
import android.widget.Toast;

import com.cretin.collegehelper.R;
import com.cretin.collegehelper.adapter.MainDiscoverAdapter;
import com.cretin.collegehelper.model.FlowModel;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

@EFragment(R.layout.fragment_main_home)
public class MainHomeFragment extends Fragment implements SwipyRefreshLayout.OnRefreshListener {
    @ViewById
    ListView listviewMainDiscover;
    @ViewById
    SwipyRefreshLayout swipyListviewDiscover;
    private int mCursor;
    private MainDiscoverAdapter adapter;
    private List<FlowModel> list;

    public MainHomeFragment() {
    }

    @AfterViews
    public void init() {
        swipyListviewDiscover.setOnRefreshListener(this);

        list = new ArrayList<>();
        adapter = new MainDiscoverAdapter(getActivity(), list, R.layout.item_listview_discover);
        listviewMainDiscover.setAdapter(adapter);

        getData(mCursor);
    }

    private void getData(final int cursor) {
        BmobQuery<FlowModel> query = new BmobQuery<>();
        query.include("author");
        query.setLimit(10);
        query.setSkip(cursor);
        mCursor += 10;
        query.order("-createdAt");
        query.findObjects(getActivity(), new FindListener<FlowModel>() {
            @Override
            public void onSuccess(List<FlowModel> object) {
                if (cursor == 0) {
                    list.clear();
                }
                list.addAll(object);
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
}
