package com.cretin.collegehelper.ui;

import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cretin.collegehelper.R;
import com.cretin.collegehelper.views.ProgressWebView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_show_web_view)
public class ShowWebViewActivity extends AppCompatActivity implements ProgressWebView.UrlTitleBack {

    @ViewById
    ImageView ivShowWebviewBack;
    @ViewById
    TextView ivShowWebviewTitle;
    @ViewById
    ProgressWebView ivShowWebviewWebview;
    private String url;

    @AfterViews
    public void init() {
        getSupportActionBar().hide();

        url = getIntent().getStringExtra("url");

        ivShowWebviewWebview.getSettings().setJavaScriptEnabled(true);
        ivShowWebviewWebview.loadUrl(url);
        ivShowWebviewWebview.setBack(this);
        ivShowWebviewWebview.getSettings().setBuiltInZoomControls(true);
        ivShowWebviewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
    }

    @Override
    public void getTitleBack(String title) {
        ivShowWebviewTitle.setText(title);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void goBack() {
        if (ivShowWebviewWebview.canGoBack()) {
            ivShowWebviewWebview.goBack();// 返回前一个页面}
        } else {
            ShowWebViewActivity.this.finish();
        }
    }
}
