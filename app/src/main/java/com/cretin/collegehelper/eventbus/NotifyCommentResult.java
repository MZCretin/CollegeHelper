package com.cretin.collegehelper.eventbus;

import com.cretin.collegehelper.model.FlowModel;

/**
 * Created by cretin on 4/17/16.
 */
public class NotifyCommentResult {
    private FlowModel flowModel;
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public FlowModel getFlowModel() {
        return flowModel;
    }

    public void setFlowModel(FlowModel flowModel) {
        this.flowModel = flowModel;
    }
}
