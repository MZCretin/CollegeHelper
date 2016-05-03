package com.cretin.collegehelper.model;

import java.io.Serializable;
import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by cretin on 4/24/16.
 */
public class PaperResultModel extends BmobObject implements Serializable {
    private List<PaperModel> content;
    private long commitTime;
    private double score;
    private String answer;
    private UserModel author;
    private String answerDes;
    private String title;
    private UserModel fromAuthor;
    private String period;

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public UserModel getFromAuthor() {
        return fromAuthor;
    }

    public void setFromAuthor(UserModel fromAuthor) {
        this.fromAuthor = fromAuthor;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAnswerDes() {
        return answerDes;
    }

    public void setAnswerDes(String answerDes) {
        this.answerDes = answerDes;
    }

    public List<PaperModel> getContent() {
        return content;
    }

    public void setContent(List<PaperModel> content) {
        this.content = content;
    }

    public long getCommitTime() {
        return commitTime;
    }

    public void setCommitTime(long commitTime) {
        this.commitTime = commitTime;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public UserModel getAuthor() {
        return author;
    }

    public void setAuthor(UserModel author) {
        this.author = author;
    }
}
