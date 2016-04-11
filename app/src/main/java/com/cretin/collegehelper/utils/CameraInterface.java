package com.cretin.collegehelper.utils;

/**
 * Created by cretin on 16/1/6.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;

import com.cretin.collegehelper.model.Images;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CameraInterface {
    private static final String TAG = CameraInterface.class.getSimpleName();
    private Camera mCamera;
    private Camera.Parameters mParams;
    private boolean isPreviewing = false;
    private float mPreviwRate = -1f;
    private static CameraInterface mCameraInterface;

    //记录当前摄像头的打开状态 0-后置 1-前置
    private int mCameraPosition = 0;
    private CamOpenOverCallback mCallback;

    // 记录是否正在录像,fasle为未录像, true 为正在录像
    private boolean recording;

    private MediaRecorder recorder;
    //录像文件
    private File myRecVideoFile;
    private String fileName;
    private String name = "";

    //保存当前的拍照路径
    private String picPath;


    /**
     * 打开关闭闪光灯
     *
     * @param open
     */
    public void openFlashlight(boolean open) {
        if (open) {
            mParams = mCamera.getParameters();
            mParams.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);//开启
            mCamera.setParameters(mParams);
        } else {
            mParams = mCamera.getParameters();
            mParams.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);//关闭
            mCamera.setParameters(mParams);
        }
    }

    public interface CamOpenOverCallback {
        void mCameraHasOpened();

        void takePhotoComplete(Images images);
    }

    private CameraInterface() {
    }

    //构造方法私有化
    public static synchronized CameraInterface getInstance() {
        if (mCameraInterface == null) {
            mCameraInterface = new CameraInterface();
        }
        return mCameraInterface;
    }

    /**
     * 打开Camera
     *
     * @param callback
     */
    public void doOpenCamera(CamOpenOverCallback callback) {
        Log.i(TAG, "Camera open....");
        mCamera = Camera.open();
        Log.i(TAG, "Camera open over....");
        callback.mCameraHasOpened();
    }

    /**
     * 开启预览
     *
     * @param holder
     * @param previewRate
     */
    public void doStartPreview(SurfaceHolder holder, float previewRate) {
        Log.i(TAG, "doStartPreview...");
        if (isPreviewing) {
            mCamera.stopPreview();
            return;
        }
        if (mCamera != null) {
            mParams = mCamera.getParameters();
            mParams.setPictureFormat(ImageFormat.JPEG);//设置拍照后存储的图片格式
            CamParaUtil.getInstance().printSupportPictureSize(mParams);
            CamParaUtil.getInstance().printSupportPreviewSize(mParams);
            //设置PreviewSize和PictureSize
            Size pictureSize = CamParaUtil.getInstance().getPropPictureSize(
                    mParams.getSupportedPictureSizes(), previewRate, 1280);
            mParams.setPictureSize(pictureSize.width, pictureSize.height);
            Size previewSize = CamParaUtil.getInstance().getPropPreviewSize(
                    mParams.getSupportedPreviewSizes(), previewRate, 1024);
            mParams.setPreviewSize(previewSize.width, previewSize.height);

            mCamera.setDisplayOrientation(90);

            CamParaUtil.getInstance().printSupportFocusMode(mParams);
            List<String> focusModes = mParams.getSupportedFocusModes();
            if (focusModes.contains("continuous-video")) {
                mParams.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            }
            mCamera.setParameters(mParams);

            try {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();//开启预览
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            isPreviewing = true;
            mPreviwRate = previewRate;

//            mParams = mCamera.getParameters(); //重新get一次
//            Log.i(TAG, "最终设置:PreviewSize--With = " + mParams.getPreviewSize().width
//                    + "Height = " + mParams.getPreviewSize().height);
//            Log.i(TAG, "最终设置:PictureSize--With = " + mParams.getPictureSize().width
//                    + "Height = " + mParams.getPictureSize().height);
        }
    }


    /**
     * 转换摄像头
     */
    public void changeCamera() {
        int mCameraCount = 0;
        Camera.CameraInfo mCameraInfo = new Camera.CameraInfo();
        mCameraCount = Camera.getNumberOfCameras(); // get mCameras number
        if (mCameraCount == 1) {
            Log.d(TAG, "未检测到前置摄像头");
        } else {
            if (mCamera != null) {
                mCamera.stopPreview();
                mCamera.release();
                isPreviewing = false;
            }
            Camera.getCameraInfo(mCameraPosition, mCameraInfo); // get mCamerainfo
            if (mCameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                mCameraPosition = 1;
                try {
                    mCamera = Camera.open(mCameraPosition);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            } else {
                mCameraPosition = 0;
                try {
                    mCamera = Camera.open(mCameraPosition);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 停止预览，释放Camera
     */
    public void doStopCamera() {
        if (null != mCamera) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            isPreviewing = false;
            mPreviwRate = -1f;
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * 拍照
     */
    public void doTakePicture(CamOpenOverCallback callback) {
        this.mCallback = callback;
        if (isPreviewing && (mCamera != null)) {
            mCamera.takePicture(mShutterCallback, null, mJpegPictureCallback);
        }
    }

    /**
     * 结束录像
     */
    public void stopRecoder() {
        if (recorder != null) {
            releaseMediaRecorder();
            recording = false;
        }
    }


    //开始录像
    public void recorder(SurfaceHolder holder) {
        if (!recording) {
            try {
                // 关闭预览并释放资源
                if (mCamera != null) {
                    mCamera.stopPreview();
                    mCamera.release();
                    mCamera = null;
                }
                recorder = new MediaRecorder();
                // 设置sdcard的路径
                fileName = Environment.getExternalStorageDirectory().getAbsolutePath();
                name = "video_" + System.currentTimeMillis() + ".mp4";
                fileName += File.separator + "Ruanko_Jobseeker" + File.separator + name;
                Log.d("HHHHH",fileName);
                // 声明视频文件对象
                myRecVideoFile = new File(fileName);
                if (!myRecVideoFile.exists()) {
                    myRecVideoFile.getParentFile().mkdirs();
                    myRecVideoFile.createNewFile();
                }
                recorder.reset();
                mCamera = Camera.open(mCameraPosition);
                // 设置摄像头预览顺时针旋转90度，才能使预览图像显示为正确的，而不是逆时针旋转90度的。
                mCamera.setDisplayOrientation(90);
                mCamera.unlock();
                recorder.setCamera(mCamera); //设置摄像头为相机recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);//视频源
                recorder.setAudioSource(MediaRecorder.AudioSource.MIC); // 录音源为麦克风		recorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_LOW)); //设置视频和声音的编码为系统自带的格式	recorder.setOutputFile(myRecVideoFile.getAbsolutePath());
                recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);//视频源
//                recorder.setAudioSource(MediaRecorder.VideoSource.DEFAULT);//视频源
//                recorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);//视频源

                CamcorderProfile profile = CamcorderProfile.get(CamcorderProfile.QUALITY_480P);

                String profileStr = String.format("audioBitrate: %s\n, videoBitRate: %s\n, videoFrameWidth: %s\n, videoFrameHeight: %s\n, videoFrameRate: %s\n, fileFormat: %s\n, videoCodec: %s\n",
                        profile.audioBitRate, profile.videoBitRate, profile.videoFrameWidth, profile.videoFrameHeight, profile.videoFrameRate, profile.fileFormat, profile.videoCodec);

                Log.d(TAG, profileStr);

                recorder.setProfile(profile); // 默认视频格式
                recorder.setVideoFrameRate(24); // 30
                recorder.setVideoEncodingBitRate(1 * 800 * 1024); // 6 * 10^6
                recorder.setAudioEncodingBitRate(100 * 1024); // 125 * 1024
                List<Size> list = mParams.getSupportedVideoSizes();
                for (int i = 0; i < list.size(); i++) {
                    Size size = list.get(i);
                    Log.d(TAG, "h " + size.height + " w " + size.width);
                    if (size.height * 4 == size.width * 3 && size.width > 600) {
                        recorder.setVideoSize(size.width, size.height);
                        break;
                    }
                }

//                recorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
//                recorder.setVideoEncodingBitRate(5*1024*1024);
//                recorder.setVideoFrameRate(24);

                recorder.setOutputFile(myRecVideoFile.getAbsolutePath());
                recorder.setPreviewDisplay(holder.getSurface()); // 预览
//                recorder.setMaxFileSize(1*1024*1024); //设置视频文件的最大值为10M,单位B
                recorder.setMaxDuration(15 * 1000);
//recorder.setMaxDuration(3*1000);//设置视频的最大时长，单位毫秒
//	recorder.setOrientationHint(90);//视频旋转90度，没有用
//                CamcorderProfile profile = new CamcorderProfile();

                recorder.prepare(); // 准备录像
                recorder.start(); // 开始录像

                recording = true; // 改变录制状态为正在录制
            } catch (IOException e1) {
                releaseMediaRecorder();
                recording = false;
            } catch (IllegalStateException e) {
                releaseMediaRecorder();
                recording = false;
            }
        } else {

        }
    }

    //释放recorder资源
    private void releaseMediaRecorder() {
        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;
        }
    }

    /*为了实现拍照的快门声音及拍照保存照片需要下面三个回调变量*/
    ShutterCallback mShutterCallback = new ShutterCallback()
            //快门按下的回调，在这里我们可以设置类似播放“咔嚓”声之类的操作。默认的就是咔嚓。
    {
        public void onShutter() {
            Log.i(TAG, "myShutterCallback:onShutter...");
        }
    };
    PictureCallback mRawCallback = new PictureCallback()
            // 拍摄的未压缩原数据的回调,可以为null
    {

        public void onPictureTaken(byte[] data, Camera mCamera) {
            Log.i(TAG, "myRawCallback:onPictureTaken...");

        }
    };
    PictureCallback mJpegPictureCallback = new PictureCallback()
            //对jpeg图像数据的回调,最重要的一个回调
    {
        public void onPictureTaken(byte[] data, Camera mCamera) {
            Bitmap b = null;
            Images images = new Images(Images.TYPE_FILE);
            if (null != data) {
                b = BitmapFactory.decodeByteArray(data, 0, data.length);//data是字节数据，将其解析成位图
                if(null != b)
                {
                    //设置FOCUS_MODE_CONTINUOUS_VIDEO)之后，myParam.set("rotation", 90)失效。
                    //图片竟然不能旋转了，故这里要旋转下
                    // 前置摄像头的旋转和后置的不一样
                    float rotateDegree = 90.0f;
                    Matrix matrix = new Matrix();
                    if (Camera.getNumberOfCameras() > 1) {
                        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
                        Camera.getCameraInfo(mCameraPosition, cameraInfo);
                        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
//                            rotateDegree = -rotateDegree;
                            matrix.postScale(-1, 1); //自拍水平翻转
                        }
                    }

                    matrix.postRotate(rotateDegree);

                    Bitmap rotaBitmap = ImageUtil.getBitmapFromMatrix(b, matrix);
                    images.setmBitmap(ImageUtils.compressBitmap(rotaBitmap));
                }
                mCamera.stopPreview();
                isPreviewing = false;
                mCallback.takePhotoComplete(images);
            }
        }
    };


}
