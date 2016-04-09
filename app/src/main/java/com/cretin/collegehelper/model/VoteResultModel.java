package com.cretin.collegehelper.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by cretin on 4/5/16.
 */
public class VoteResultModel extends BmobObject{
    private String createUserName;
    private UserModel author;
    private long voteTime;
    private String voteTitle;
    private String voteDes;
    private String voteContent;
    private UserModel voteUser;

    public UserModel getAuthor() {
        return author;
    }

    public void setAuthor(UserModel author) {
        this.author = author;
    }

    public UserModel getVoteUser() {
        return voteUser;
    }

    public void setVoteUser(UserModel voteUser) {
        this.voteUser = voteUser;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public long getVoteTime() {
        return voteTime;
    }

    public void setVoteTime(long voteTime) {
        this.voteTime = voteTime;
    }

    public String getVoteTitle() {
        return voteTitle;
    }

    public void setVoteTitle(String voteTitle) {
        this.voteTitle = voteTitle;
    }

    public String getVoteDes() {
        return voteDes;
    }

    public void setVoteDes(String voteDes) {
        this.voteDes = voteDes;
    }

    public String getVoteContent() {
        return voteContent;
    }

    public void setVoteContent(String voteContent) {
        this.voteContent = voteContent;
    }
}
