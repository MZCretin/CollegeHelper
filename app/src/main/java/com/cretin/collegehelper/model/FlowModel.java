package com.cretin.collegehelper.model;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by cretin on 4/11/16.
 */
public class FlowModel extends BmobObject {
    private UserModel author;
    private long sendTime;
    private List<String> resourceUrl;
    private String topicTontent;
    private String spotlight;
    private List<CommentModel> commentModelList;

    public List<CommentModel> getCommentModelList() {
        return commentModelList;
    }

    public void setCommentModelList(List<CommentModel> commentModelList) {
        this.commentModelList = commentModelList;
    }

    public String getSpotlight() {
        return spotlight;
    }

    public void setSpotlight(String spotlight) {
        this.spotlight = spotlight;
    }

    public UserModel getAuthor() {
        return author;
    }

    public void setAuthor(UserModel author) {
        this.author = author;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }

    public List<String> getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(List<String> resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public String getTopicTontent() {
        return topicTontent;
    }

    public void setTopicTontent(String topicTontent) {
        this.topicTontent = topicTontent;
    }
}
