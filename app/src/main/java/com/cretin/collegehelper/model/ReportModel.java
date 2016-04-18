package com.cretin.collegehelper.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by cretin on 4/18/16.
 */
public class ReportModel extends BmobObject{
    private long commitTime;
    private UserModel commitUser;
    private FlowModel reportedFlow;
    private String reportContent;

    public long getCommitTime() {
        return commitTime;
    }

    public void setCommitTime(long commitTime) {
        this.commitTime = commitTime;
    }

    public UserModel getCommitUser() {
        return commitUser;
    }

    public void setCommitUser(UserModel commitUser) {
        this.commitUser = commitUser;
    }

    public FlowModel getReportedFlow() {
        return reportedFlow;
    }

    public void setReportedFlow(FlowModel reportedFlow) {
        this.reportedFlow = reportedFlow;
    }

    public String getReportContent() {
        return reportContent;
    }

    public void setReportContent(String reportContent) {
        this.reportContent = reportContent;
    }
}
