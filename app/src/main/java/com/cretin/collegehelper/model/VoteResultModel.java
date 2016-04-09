package com.cretin.collegehelper.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by cretin on 4/5/16.
 */
public class VoteResultModel extends BmobObject{
    private UserModel voteUser;
    private VoteModel voteInfo;
    private long voteTime;
    private String userVoteContent;

    public UserModel getVoteUser() {
        return voteUser;
    }

    public void setVoteUser(UserModel voteUser) {
        this.voteUser = voteUser;
    }

    public VoteModel getVoteInfo() {
        return voteInfo;
    }

    public void setVoteInfo(VoteModel voteInfo) {
        this.voteInfo = voteInfo;
    }

    public long getVoteTime() {
        return voteTime;
    }

    public void setVoteTime(long voteTime) {
        this.voteTime = voteTime;
    }

    public String getUserVoteContent() {
        return userVoteContent;
    }

    public void setUserVoteContent(String userVoteContent) {
        this.userVoteContent = userVoteContent;
    }
}
