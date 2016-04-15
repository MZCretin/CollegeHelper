package com.cretin.collegehelper.utils;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.picasso.Callback;


/**
 * Created by cretin on 16/3/19.
 */
public class ImageLoadedCallback implements Callback {
    ProgressBar progressBar;
    Context context;

    public ImageLoadedCallback(ProgressBar progBar, Context context) {
        progressBar = progBar;
        this.context = context;
    }

    @Override
    public void onSuccess() {
        if (this.progressBar != null) {
            this.progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onError() {
        if (this.progressBar != null) {
            this.progressBar.setVisibility(View.GONE);
        }
        Toast.makeText(context, "加载失败！", Toast.LENGTH_SHORT).show();
    }
}
