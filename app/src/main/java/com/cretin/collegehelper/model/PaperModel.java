package com.cretin.collegehelper.model;

/**
 * Created by cretin on 4/23/16.
 */
public class PaperModel {
    private String title;
    private String answer;
    private int answerIndex;

    public int getAnswerIndex() {
        return answerIndex;
    }

    public void setAnswerIndex(int answerIndex) {
        this.answerIndex = answerIndex;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

}
