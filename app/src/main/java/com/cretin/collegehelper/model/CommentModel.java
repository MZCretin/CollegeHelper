package com.cretin.collegehelper.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by cretin on 4/11/16.
 */
public class CommentModel extends BmobObject{
    private String comment;
    private int likeCount;
    private long created;
    private UserModel fromUser;
    private String commentTimeDescription;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public UserModel getFromUser() {
        return fromUser;
    }

    public void setFromUser(UserModel fromUser) {
        this.fromUser = fromUser;
    }

    public String getCommentTimeDescription() {
        return commentTimeDescription;
    }

    public void setCommentTimeDescription(String commentTimeDescription) {
        this.commentTimeDescription = commentTimeDescription;
    }
}
