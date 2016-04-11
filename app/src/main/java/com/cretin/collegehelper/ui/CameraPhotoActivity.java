package com.cretin.collegehelper.ui;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.cretin.collegehelper.R;
import com.cretin.collegehelper.adapter.FragmentViewPagerAdapter;
import com.cretin.collegehelper.fragment.CameraFragment;
import com.cretin.collegehelper.fragment.PhotosFragment;
import com.cretin.collegehelper.globaldata.GlobalData;
import com.cretin.collegehelper.model.Images;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_camera_photo)
public class CameraPhotoActivity extends AppCompatActivity implements PhotosFragment.Callback {

    @ViewById
    ViewPager vpCameraPhotoContainer;
    @ViewById
    TextView btnCameraPtohos;
    @ViewById
    TextView btnCameraCamera;
    private Images image;

    private FragmentViewPagerAdapter adapter;
    private List<Fragment> list;


    //记录整个Activity所被选择或者是拍照生成的图片的路径集合
    private List<Images> mLocalEditImageList;

    @AfterViews
    public void init() {
        getSupportActionBar().hide();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//
        mLocalEditImageList = new ArrayList<>();

        list = new ArrayList<>();
        list.add(new PhotosFragment());
        list.add(CameraFragment.newInstance());
        adapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), list);
        vpCameraPhotoContainer.setAdapter(adapter);

        btnCameraPtohos.setTextColor(getResources().getColor(R.color.main_back_pressed));
        btnCameraCamera.setTextColor(Color.parseColor("#666666"));

        vpCameraPhotoContainer.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    btnCameraPtohos.setTextColor(getResources().getColor(R.color.main_back_pressed));
                    btnCameraCamera.setTextColor(Color.parseColor("#666666"));
                }

                if (position == 1) {
                    btnCameraPtohos.setTextColor(Color.parseColor("#666666"));
                    btnCameraCamera.setTextColor(getResources().getColor(R.color.main_back_pressed));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mLocalEditImageList.clear();
        mLocalEditImageList.addAll(GlobalData.getInstance().getImagesList());
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        GlobalData.getInstance().setImagesList(mLocalEditImageList);
    }

    @Override
    public void onSingleImageSelected(String path) {
    }

    @Override
    public void onImageSelected(String path) {
        for (Images images : mLocalEditImageList) {
            if (images.getPath().equals(path)) {
                return;
            }
        }
        image = new Images(Images.TYPE_FILE);
        image.setPath(path);
        mLocalEditImageList.add(image);
        GlobalData.getInstance().setImagesList(mLocalEditImageList);
    }

    @Override
    public void onImageUnselected(String path) {
        Log.d("HHH", "onImageUnselected:" + path);
        for (Images images : mLocalEditImageList) {
            if (images.getPath().equals(path)) {
                mLocalEditImageList.remove(images);
                GlobalData.getInstance().setImagesList(mLocalEditImageList);
                return;
            }
        }
    }

    @Override
    public void onBtnContinueClick(View v) {
        if (mLocalEditImageList.isEmpty()) {
            Toast.makeText(CameraPhotoActivity.this, "请选择至少一张图片", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent nextIntent = new Intent(this, SendActivity_.class);
        nextIntent.setAction(SendActivity.ACTION_ADD_IMAGES);
        nextIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(nextIntent);
    }


    @Click({R.id.btn_camera_camera, R.id.btn_camera_ptohos})
    public void onCLick(View view) {
        switch (view.getId()) {
            case R.id.btn_camera_camera:
                vpCameraPhotoContainer.setCurrentItem(1);
                break;
            case R.id.btn_camera_ptohos:
                vpCameraPhotoContainer.setCurrentItem(0);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goBack();
    }

    /**
     * 处理返回的逻辑,需要
     */
    public void goBack() {
        GlobalData.getInstance().setImagesList(new ArrayList<Images>());
        if (!this.isFinishing()) {
            this.finish();
        }
    }

    public List<Images> getmLocalEditImageList() {
        return mLocalEditImageList;
    }

}
