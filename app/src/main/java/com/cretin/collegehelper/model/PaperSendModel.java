package com.cretin.collegehelper.model;

import java.io.Serializable;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by cretin on 4/23/16.
 */
public class PaperSendModel extends BmobObject implements Serializable{
    private long createTime;
    private UserModel author;
    private BmobRelation joinList;
    private String title;
    private int period;
    private List<PaperModel> testContent;

    public List<PaperModel> getTestContent() {
        return testContent;
    }

    public void setTestContent(List<PaperModel> testContent) {
        this.testContent = testContent;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }
}
