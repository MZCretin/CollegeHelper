package com.cretin.collegehelper.ui;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cretin.collegehelper.R;
import com.cretin.collegehelper.adapter.ImageViewPagerAdapter;
import com.cretin.collegehelper.views.HackyViewPager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EActivity(R.layout.activity_see_big_bitmap)
public class SeeBigBitmapActivity extends AppCompatActivity {
    @ViewById
    public HackyViewPager pager;
    @ViewById
    public ImageView ivSeeBigBitmapClose;
    @ViewById
    TextView tvSeeBigBitmapDes;
    @ViewById
    public TextView tvSeeBigBitmapCurrindex;
    @ViewById
    public LinearLayout llSeeBigBitmapContainer;
    private ImageViewPagerAdapter adapter;
    private List<String> listurl;
    public static SeeBigBitmapActivity activity;
    private int currIndex;
    private boolean iconFlag;
    private String message;

    @AfterViews
    public void init() {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        getSupportActionBar().hide();
        activity = this;
        currIndex = Integer.parseInt(getIntent().getStringExtra("index"));
        //内存消耗严重
        listurl = (List<String>) getIntent().getSerializableExtra("list");
        iconFlag = getIntent().getBooleanExtra("iconFlag", iconFlag);
        message = getIntent().getStringExtra("message");
        tvSeeBigBitmapCurrindex.setText(currIndex + "/" + listurl.size());
        tvSeeBigBitmapDes.setText(message);


        pager = (HackyViewPager) findViewById(R.id.pager);

        adapter = new ImageViewPagerAdapter(getSupportFragmentManager(), listurl, iconFlag, this);
        pager.setAdapter(adapter);
        pager.setCurrentItem(currIndex - 1);

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tvSeeBigBitmapCurrindex.setText((position + 1) + "/" + listurl.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Click({R.id.iv_see_big_bitmap_close})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_see_big_bitmap_close:
                this.finish();
                overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
        overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
    }
}
