package com.cretin.collegehelper.views;

import android.view.MotionEvent;

/**
 * Created by cretin on 16/3/1.
 */
public class RotateGestureDetector {

    private static final int MAX_DEGREES_STEP = 120;

    private OnRotateListener mListener;

    public void setDropListener(OnDropListener dropListener) {
        this.dropListener = dropListener;
    }

    private OnDropListener dropListener;

    private float mPrevSlope;
    private float mCurrSlope;

    private float x1;
    private float y1;
    private float x2;
    private float y2;

    private float oldX;
    private float oldY;

    public RotateGestureDetector(OnRotateListener l) {
        mListener = l;
    }

    public void onTouchEvent(MotionEvent event) {

        final int Action = event.getActionMasked();

        switch (Action) {
            case MotionEvent.ACTION_DOWN:
                oldX = event.getX();
                oldY = event.getY();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
                if (event.getPointerCount() == 2) mPrevSlope = caculateSlope(event);
                break;
            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() > 1) {
                    mCurrSlope = caculateSlope(event);

                    double currDegrees = Math.toDegrees(Math.atan(mCurrSlope));
                    double prevDegrees = Math.toDegrees(Math.atan(mPrevSlope));

                    double deltaSlope = currDegrees - prevDegrees;

                    if (Math.abs(deltaSlope) <= MAX_DEGREES_STEP) {
                        if (mListener != null)
                            mListener.onRotate((float) deltaSlope, (x2 + x1) / 2, (y2 + y1) / 2);
                    }
                    mPrevSlope = mCurrSlope;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (event.getPointerCount() == 1) {
                    if (Math.abs(oldX - event.getX(0)) < Math.abs(oldY - event.getY(0))) {
                        //代表是上下滑动
                        if(oldY < event.getY()){
                            //代表向下运动
                            if (dropListener != null)
                                dropListener.dropDown(Math.abs(oldX - event.getX()), Math.abs(oldY - event.getY()),oldX,oldY,event.getX(),event.getY());
                        }
                    }
                }
            default:
                break;
        }
    }

    private float caculateSlope(MotionEvent event) {
        x1 = event.getX(0);
        y1 = event.getY(0);
        x2 = event.getX(1);
        y2 = event.getY(1);
        return (y2 - y1) / (x2 - x1);
    }
}

interface OnRotateListener {
    void onRotate(float degrees, float focusX, float focusY);
}

interface OnDropListener {
    void dropDown(float absDeltaX, float absDeltaY, float oldX, float oldY, float currX, float currY);
}