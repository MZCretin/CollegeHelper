package com.cretin.collegehelper.model;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by cretin on 4/4/16.
 */
public class VoteModel extends BmobObject implements Serializable{
    private long createTime;
    private String userName;
    private String voteTitle;
    private String voteDestribe;
    private BmobRelation joinList;
    private int joinCount;
    private int verifireFlag;
    private UserModel author;

    public UserModel getAuthor() {
        return author;
    }

    public void setAuthor(UserModel author) {
        this.author = author;
    }

    public BmobRelation getJoinList() {
        return joinList;
    }

    public void setJoinList(BmobRelation joinList) {
        this.joinList = joinList;
    }

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

    public int getJoinCount() {
        return joinCount;
    }

    public void setJoinCount(int joinCount) {
        this.joinCount = joinCount;
    }

    public int getVerifireFlag() {
        return verifireFlag;
    }

    public void setVerifireFlag(int verifireFlag) {
        this.verifireFlag = verifireFlag;
    }
}
