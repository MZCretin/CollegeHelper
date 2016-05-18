package com.cretin.collegehelper.eventbus;

import com.cretin.collegehelper.model.FlowModel;

/**
 * Created by cretin on 4/17/16.
 */
public class NotifyCommentResult {
    public static final int TYPE_MAIN = 0;
    public static final int TYPE_INFO_FRAGMENT = 1;
    public static final int TYPE_INFO_ACTIVITY = 2;
    private FlowModel flowModel;
    private int position;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

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
