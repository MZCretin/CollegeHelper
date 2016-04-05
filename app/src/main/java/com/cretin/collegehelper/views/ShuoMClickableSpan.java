package com.cretin.collegehelper.views;

/**
 * Created by cretin on 16/3/2.
 */

import android.content.Context;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.cretin.collegehelper.R;


public class ShuoMClickableSpan extends ClickableSpan {
    Context context;

    public TextAreaClickListener getListener() {
        return listener;
    }

    public void setListener(TextAreaClickListener listener) {
        this.listener = listener;
    }

    private TextAreaClickListener listener;

    public ShuoMClickableSpan(Context context) {
        super();
        this.context = context;
    }


    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(context.getResources().getColor(R.color.main_back_pressed));
    }


    @Override
    public void onClick(View widget) {
        listener.onTextClick();
    }

    public interface TextAreaClickListener {
        void onTextClick();
    }

}
