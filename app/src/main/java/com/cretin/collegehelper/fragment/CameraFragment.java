package com.cretin.collegehelper.fragment;


import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.Toast;

import com.cretin.collegehelper.R;
import com.cretin.collegehelper.globaldata.GlobalData;
import com.cretin.collegehelper.model.Images;
import com.cretin.collegehelper.ui.CameraPhotoActivity;
import com.cretin.collegehelper.ui.SendActivity;
import com.cretin.collegehelper.ui.SendActivity_;
import com.cretin.collegehelper.utils.CameraInterface;
import com.cretin.collegehelper.utils.CameraSurfaceView;
import com.cretin.collegehelper.utils.DisplayUtil;
import com.cretin.collegehelper.views.TakePhotoView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CameraFragment extends Fragment implements CameraInterface.CamOpenOverCallback, View.OnClickListener {
    private int position;
    private CameraSurfaceView cameraSurfaceview;
    private float previewRate = 1.333333f;
    private ImageView ivFlashLight;
    private ImageView ivChangeCamera;
    //    private GPUImageView gpuImageView;
    private TakePhotoView takePhotoView;
//    private MyRelativeLayout myRelativeLayout;
//    private RelativeLayout container;

    private SurfaceHolder holder;

    public static final int REQUEST_IMAGE = 101;

    //记录手机尺寸
    private int SCREEN_WIDTH;
    private int SCREEN_HEIGHT;

    //记录摄像头闪光灯的打开状态
    private boolean flashLightFlag;
    private CameraInterface.CamOpenOverCallback callback;

    // 记录是否正在录像,fasle为未录像, true 为正在录像
    private boolean recording;

    //记录当前的文件数据
    private File currentFile;
    private String qqToken;

    private List<Images> mLocalEditImagesList = new ArrayList<>();

    public CameraFragment() {
        // Required empty public constructor
    }

    public static CameraFragment newInstance() {
        CameraFragment fragment = new CameraFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.callback = this;
        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        mLocalEditImagesList = ((CameraPhotoActivity) getActivity()).getmLocalEditImageList();
//        checkToken();
//        //初始化控件
        initView(view);
//
        initViewParams(view);
//        //获取屏幕尺寸
//        getPhoneDisplay();

        return view;
    }

    /**
     * 初始化控件
     */
    private void initView(final View view) {
//        gpuImageView = (GPUImageView) view.findViewById(R.id.image);
        cameraSurfaceview = (CameraSurfaceView) view.findViewById(R.id.camera_surfaceview);
        ivFlashLight = (ImageView) view.findViewById(R.id.iv_camera_flashlight);
        ivChangeCamera = (ImageView) view.findViewById(R.id.iv_camera_changecamera);
//        tvChangeCamera = (TextView) view.findViewById(R.id.tv_camera_change);
//        tvFlashLight = (TextView) view.findViewById(R.id.tv_camera_flashlight);
//        tvPhoto = (TextView) view.findViewById(R.id.tv_camera_picture);
//        tvSetting = (TextView) view.findViewById(R.id.tv_camera_setting);
        takePhotoView = (TakePhotoView) view.findViewById(R.id.iv_camera_takepic);
//        myRelativeLayout = (MyRelativeLayout) view.findViewById(R.id.layout_camera_myrelativelayout);
//        container = (RelativeLayout) view.findViewById(R.id.camera_relativelayout_container);

//        myRelativeLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getActivity(), "你点的是我哦!--RelativeLayout", Toast.LENGTH_SHORT).show();
//            }
//        });
        ivFlashLight.setOnClickListener(this);
        ivChangeCamera.setOnClickListener(this);
//        tvChangeCamera.setOnClickListener(this);
//        tvFlashLight.setOnClickListener(this);
//        tvPhoto.setOnClickListener(this);
//        tvSetting.setOnClickListener(this);

//        myRelativeLayout.setMyRelativeTouchCallBack(this);

        //对ImageView设置事件监听
        takePhotoView.setLongClickListener(new TakePhotoView.OnLongTakePhotoClickListener() {
            @Override
            public void onLongClick() {
                //长按模式
//                Toast.makeText(getActivity(), "开始录像！", Toast.LENGTH_SHORT).show();
//                if (holder != null) {
//                    CameraInterface.getInstance().recorder(holder);
//                }
            }

            @Override
            public void onLongClickUp() {
//                CameraInterface.getInstance().stopRecoder();
//                Toast.makeText(getActivity(), "录像结束", Toast.LENGTH_SHORT).show();
            }
        });
        takePhotoView.setShortClickListener(new TakePhotoView.OnTakePhotoClickListener() {
            @Override
            public void onClick() {
                // 如果已满9张图片则不能再照更多图片

                int currentSelectedImagesCount = GlobalData.getInstance().getImagesList().size();
                int currentEditedImagesCount = GlobalData.getInstance().getSendFlowActivity().getCurrentCount();
                if (9 == currentEditedImagesCount + currentSelectedImagesCount) {
                    Toast.makeText(getActivity(), "已选择了9张图片,请先发布", Toast.LENGTH_SHORT).show();
                    return;
                }

                CameraInterface.getInstance().doTakePicture(callback);
            }
        });
    }

    private void initViewParams(final View view) {
        final ViewTreeObserver vto = view.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                float height = view.getMeasuredHeight();
                ViewGroup.LayoutParams params = cameraSurfaceview.getLayoutParams();
                Point p = DisplayUtil.getScreenMetrics(getActivity());
                params.width = p.x;
                params.height = (int) (height * 0.763);
//                BaseApp.getInstance().viewheight = params.height;
//                BaseApp.getInstance().viewwidth = params.width;
                cameraSurfaceview.setLayoutParams(params);
                view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }

    /**
     * 获取手机分辨率
     */
    private void getPhoneDisplay() {
        Display display = getActivity().getWindowManager().getDefaultDisplay(); //Activity#getWindowManager()
        Point size = new Point();
        display.getSize(size);
        SCREEN_WIDTH = size.x;
        SCREEN_HEIGHT = size.y;
    }

    @Override
    public void mCameraHasOpened() {
        holder = cameraSurfaceview.getSurfaceHolder();
        CameraInterface.getInstance().doStartPreview(holder, previewRate);
    }

    @Override
    public void takePhotoComplete(Images images) {
        mLocalEditImagesList.add(images);
//        GlobalData.getInstance().setImagesList(mLocalEditImagesList);
        //测试阶段 将其转到编辑页面去
        Intent nextIntent = new Intent(getActivity(), SendActivity_.class);
        nextIntent.setAction(SendActivity.ACTION_ADD_IMAGES);
        nextIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivity(nextIntent);
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        Thread openThread = new Thread() {
            @Override
            public void run() {
                CameraInterface.getInstance().doOpenCamera(CameraFragment.this);
            }
        };
        openThread.start();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        CameraInterface.getInstance().doStopCamera();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CameraInterface.getInstance().doStopCamera();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_camera_flashlight:
                if (flashLightFlag) {
                    flashLightFlag = false;
                    ivFlashLight.setImageResource(R.mipmap.flashlight2x);
                } else {
                    flashLightFlag = true;
                    ivFlashLight.setImageResource(R.mipmap.flashlight_active2x);
                }
                CameraInterface.getInstance().openFlashlight(flashLightFlag);
                break;
            case R.id.iv_camera_changecamera:
                CameraInterface.getInstance().changeCamera();
                CameraInterface.getInstance().doStartPreview(cameraSurfaceview.getSurfaceHolder(), previewRate);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            holder = cameraSurfaceview.getSurfaceHolder();
            CameraInterface.getInstance().doStartPreview(holder, previewRate);
        }
    }
}

