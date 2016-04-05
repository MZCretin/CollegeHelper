package com.cretin.collegehelper.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.cretin.collegehelper.BaseApp;
import com.cretin.collegehelper.R;

public class SpalshActivity extends AppCompatActivity {
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //代表第一次进入
                case 0:
                    if(BaseApp.getInstance().getUserModel()==null){
                        Intent intent = new Intent(SpalshActivity.this, LoginActivity_.class);
                        startActivity(intent);
                        SpalshActivity.this.finish();
                    }else{
                        Intent intent = new Intent(SpalshActivity.this, MainActivity_.class);
                        startActivity(intent);
                        SpalshActivity.this.finish();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                msg.what = 0;
                handler.sendMessage(msg);
            }
        }, 1 * 1000);
    }
}
