package com.cretin.collegehelper.eventbus;

import com.cretin.collegehelper.model.UserModel;

import java.util.List;

/**
 * Created by cretin on 4/4/16.
 */
public class NotifyAddMemberSuccess {
    private List<UserModel> joinList;

    public List<UserModel> getJoinList() {
        return joinList;
    }

    public void setJoinList(List<UserModel> joinList) {
        this.joinList = joinList;
    }
}
