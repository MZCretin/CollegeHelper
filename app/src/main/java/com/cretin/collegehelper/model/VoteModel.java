package com.cretin.collegehelper.model;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by cretin on 4/4/16.
 */
public class VoteModel extends BmobObject {
    private long createTime;
    private String userName;
    private String voteTitle;
    private String voteDestribe;
    private List<UserModel> joinList;
    private int joinCount;
    private boolean verifierFlag;

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getVoteTitle() {
        return voteTitle;
    }

    public void setVoteTitle(String voteTitle) {
        this.voteTitle = voteTitle;
    }

    public String getVoteDestribe() {
        return voteDestribe;
    }

    public void setVoteDestribe(String voteDestribe) {
        this.voteDestribe = voteDestribe;
    }

    public List<UserModel> getJoinList() {
        return joinList;
    }

    public void setJoinList(List<UserModel> joinList) {
        this.joinList = joinList;
    }

    public int getJoinCount() {
        return joinCount;
    }

    public void setJoinCount(int joinCount) {
        this.joinCount = joinCount;
    }

    public boolean isVerifierFlag() {
        return verifierFlag;
    }

    public void setVerifierFlag(boolean verifierFlag) {
        this.verifierFlag = verifierFlag;
    }
}
