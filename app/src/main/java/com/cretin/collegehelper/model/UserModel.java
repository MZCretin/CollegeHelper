package com.cretin.collegehelper.model;

import java.io.Serializable;
import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * Created by cretin on 4/3/16.
 */
public class UserModel extends BmobUser implements Serializable{
    private String id;
    private String nickName;
    private String phoneNum;
    private String psw;
    private String signature;
    private List<UserModel> members;
    private List<VoteModel> joinedVotes;

    public List<VoteModel> getJoinedVotes() {
        return joinedVotes;
    }

    public void setJoinedVotes(List<VoteModel> joinedVotes) {
        this.joinedVotes = joinedVotes;
    }

    public String getSignature() {
        return signature;
    }

    public List<UserModel> getMembers() {
        return members;
    }

    public void setMembers(List<UserModel> members) {
        this.members = members;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserModel userModel = (UserModel) o;

        return !(id != null ? !id.equals(userModel.id) : userModel.id != null);

    }
}
