package com.cretin.collegehelper.model;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by cretin on 4/24/16.
 */
public class PaperResultModel extends BmobObject{
    private List<PaperModel> content;
    private long commitTime;
    private String correctAnswer;
    private String errorAnswer;
    private double score;
    private String answer;
    private UserModel author;

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

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getErrorAnswer() {
        return errorAnswer;
    }

    public void setErrorAnswer(String errorAnswer) {
        this.errorAnswer = errorAnswer;
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
