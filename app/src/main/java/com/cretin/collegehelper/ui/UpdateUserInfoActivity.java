package com.cretin.collegehelper.ui;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cretin.collegehelper.R;
import com.cretin.collegehelper.adapter.CropOptionAdapter;
import com.cretin.collegehelper.model.UserModel;
import com.cretin.collegehelper.popwindow.SelectPopupWindow;
import com.cretin.collegehelper.utils.CropOption;
import com.cretin.collegehelper.utils.ImageUtils;
import com.cretin.collegehelper.utils.SelectPicUtils;
import com.cretin.collegehelper.views.CircleTransform;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;


@EActivity(R.layout.activity_update_user_info)
public class UpdateUserInfoActivity extends AppCompatActivity implements View.OnClickListener, SelectPopupWindow.OnPopWindowClickListener {
    @ViewById
    ImageView ivUpdateInfoBack;
    @ViewById
    TextView tvUpdateInfoCommit;
    @ViewById
    EditText tvUpdateInfoNickname;
    @ViewById
    EditText tvUpdateInfoSex;
    @ViewById
    EditText tvUpdateInfoAge;
    @ViewById
    EditText tvUpdateInfoSignature;
    @ViewById
    EditText tvUpdateInfoWork;
    @ViewById
    EditText tvUpdateInfoCommpany;
    @ViewById
    EditText tvUpdateInfoSchool;
    @ViewById
    EditText tvUpdateInfoEmail;
    @ViewById
    ImageView ivUpdateInfoIcon;
    private UserModel mUserModel;
    private String nickname;
    private String age;
    private String sex;
    private String signature;
    private String work;
    private String commpany;
    private String school;
    private String email;
    private static final int PICK_FROM_CAMERA = 100;
    private static final int CROP_FROM_CAMERA = 101;
    private static final int PICK_FROM_FILE = 102;

    /***
     * 4.4以下(也就是kitkat以下)的版本
     */
    public static final int KITKAT_LESS = 201;
    /***
     * 4.4以上(也就是kitkat以上)的版本,当然也包括最新出的5.0棒棒糖
     */
    public static final int KITKAT_ABOVE = 202;
    private Uri imageUri;
    private SelectPopupWindow menuWindow;

    @AfterViews
    public void init() {
        getSupportActionBar().hide();

        mUserModel = (UserModel) getIntent().getSerializableExtra("usermodel");

        if (TextUtils.isEmpty(mUserModel.getAvater())) {
            Picasso.with(this).load(R.mipmap.default_icon).transform(new CircleTransform()).into(ivUpdateInfoIcon);
        } else {
            Picasso.with(this).load(mUserModel.getAvater()).transform(new CircleTransform()).into(ivUpdateInfoIcon);
        }

        nickname = mUserModel.getNickName();
        age = mUserModel.getAge();
        signature = mUserModel.getSignature();
        sex = mUserModel.getSex();
        work = mUserModel.getWork();
        commpany = mUserModel.getCommpany();
        school = mUserModel.getSchool();
        email = mUserModel.getEmail();

        //检查数据
        check();

        addListener();
    }

    private void addListener() {
        tvUpdateInfoEmail.setOnClickListener(this);
        tvUpdateInfoAge.setOnClickListener(this);
        tvUpdateInfoNickname.setOnClickListener(this);
        tvUpdateInfoSignature.setOnClickListener(this);
        tvUpdateInfoWork.setOnClickListener(this);
        tvUpdateInfoSchool.setOnClickListener(this);
        tvUpdateInfoEmail.setOnClickListener(this);
        tvUpdateInfoSex.setOnClickListener(this);
        ivUpdateInfoBack.setOnClickListener(this);
        ivUpdateInfoIcon.setOnClickListener(this);
        tvUpdateInfoCommit.setOnClickListener(this);
    }

    private void check() {
        if (!TextUtils.isEmpty(nickname)) {
            tvUpdateInfoNickname.setText(nickname);
        }

        if (!TextUtils.isEmpty(age)) {
            tvUpdateInfoAge.setText(age);
        }

        if (!TextUtils.isEmpty(signature)) {
            tvUpdateInfoSignature.setText(signature);
        }

        if (!TextUtils.isEmpty(sex)) {
            tvUpdateInfoSex.setText(sex);
        }

        if (!TextUtils.isEmpty(work)) {
            tvUpdateInfoWork.setText(work);
        }

        if (!TextUtils.isEmpty(commpany)) {
            tvUpdateInfoCommpany.setText(commpany);
        }

        if (!TextUtils.isEmpty(school)) {
            tvUpdateInfoSchool.setText(school);
        }

        if (!TextUtils.isEmpty(email)) {
            tvUpdateInfoEmail.setText(email);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_update_info_commit:
                saveData();
                break;
            case R.id.tv_update_info_sex:
                final RadioGroup radioGroup = new RadioGroup(this);
                final RadioButton radioButton1 = new RadioButton(this);
                radioButton1.setText("男");
                radioButton1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                radioButton1.setPadding(20, 0, 20, 0);
                radioGroup.addView(radioButton1);
                RadioButton radioButton2 = new RadioButton(this);
                radioButton2.setText("女");
                radioButton1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                radioButton2.setPadding(20, 0, 20, 0);
                radioGroup.addView(radioButton2);
                new AlertDialog.Builder(this).setTitle("请选择性别").setView(radioGroup).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        tvUpdateInfoSex.setText(radioGroup.getCheckedRadioButtonId() == radioButton1.getId() ? "男" : "女");
                    }
                }).show();
                break;
            case R.id.iv_update_info_back:
                UpdateUserInfoActivity.this.finish();
                break;
            case R.id.iv_update_info_icon:
                showAlertDialog();
                break;
        }
    }

    //显示对话框
    private void showAlertDialog() {
        menuWindow = new SelectPopupWindow(this, this, SelectPopupWindow.TYPE_SELECT_PIC);
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int winHeight = getWindow().getDecorView().getHeight();
        menuWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, winHeight - rect.bottom);
    }

    private void saveData() {
        nickname = tvUpdateInfoNickname.getText().toString();
        sex = tvUpdateInfoSex.getText().toString();
        age = tvUpdateInfoAge.getText().toString();
        signature = tvUpdateInfoSignature.getText().toString();
        work = tvUpdateInfoWork.getText().toString();
        commpany = tvUpdateInfoCommpany.getText().toString();
        school = tvUpdateInfoSchool.getText().toString();
        email = tvUpdateInfoEmail.getText().toString();

        UserModel newUser = new UserModel();
        newUser.setAge(age + "岁");
        newUser.setCommpany(commpany);
        newUser.setNickName(nickname);
        newUser.setSchool(school);
        newUser.setSex(sex);
        newUser.setWork(work);
        newUser.setSignature(signature);
        newUser.setEmail(email);
        newUser.update(this, mUserModel.getObjectId(), new UpdateListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(UpdateUserInfoActivity.this, "更新资料成功", Toast.LENGTH_SHORT).show();
                UpdateUserInfoActivity.this.finish();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(UpdateUserInfoActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == KITKAT_LESS) {
            imageUri = data.getData();
            doCrop();
        } else if (requestCode == KITKAT_ABOVE) {
            // 先将这个uri转换为path，然后再转换为uri
            imageUri = data.getData();
            String thePath = SelectPicUtils.getInstance().getPath(this, imageUri);
            imageUri = Uri.fromFile(new File(thePath));
            doCrop();
        } else if (requestCode == CROP_FROM_CAMERA) {
            Picasso.with(this).load(new File(imageUri.getPath())).transform(new CircleTransform()).into(ivUpdateInfoIcon);
            //在此上传头像
            uploadAvatar();
        } else if (requestCode == PICK_FROM_CAMERA) {
            doCrop();
        }
    }

    private void uploadAvatar() {
        final BmobFile bmobFile = new BmobFile(new File(imageUri.getPath()));
        bmobFile.uploadblock(this, new UploadFileListener() {

            @Override
            public void onSuccess() {
                UserModel u = new UserModel();
                u.setAvater(bmobFile.getFileUrl(UpdateUserInfoActivity.this));
                u.update(UpdateUserInfoActivity.this, mUserModel.getObjectId(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(UpdateUserInfoActivity.this, "头像同步成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int i, String s) {

                    }
                });
            }

            @Override
            public void onProgress(Integer value) {
            }

            @Override
            public void onFailure(int code, String msg) {
                Toast.makeText(UpdateUserInfoActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void doCrop() {
        final ArrayList<CropOption> cropOptions = new ArrayList<>();
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(imageUri, "image/*");
        List<ResolveInfo> list = getPackageManager().queryIntentActivities(
                intent, 0);
        int size = list.size();

        if (size == 0) {
            Toast.makeText(this, "图片裁剪失败", Toast.LENGTH_SHORT)
                    .show();
            return;
        } else {
            intent.putExtra("outputX", 300);
            intent.putExtra("outputY", 300);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            intent.putExtra("noFaceDetection", true); // no face detection

            // only one
            if (size == 1) {
                Intent i = new Intent(intent);
                ResolveInfo res = list.get(0);
                i.setComponent(new ComponentName(res.activityInfo.packageName,
                        res.activityInfo.name));
                startActivityForResult(i, CROP_FROM_CAMERA);
            } else {
                // many crop app
                for (ResolveInfo res : list) {
                    final CropOption co = new CropOption();
                    co.title = getPackageManager().getApplicationLabel(
                            res.activityInfo.applicationInfo);
                    co.icon = getPackageManager().getApplicationIcon(
                            res.activityInfo.applicationInfo);
                    co.appIntent = new Intent(intent);
                    co.appIntent
                            .setComponent(new ComponentName(
                                    res.activityInfo.packageName,
                                    res.activityInfo.name));
                    cropOptions.add(co);
                }

                CropOptionAdapter adapter = new CropOptionAdapter(
                        getApplicationContext(), cropOptions);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("choose a app");
                builder.setAdapter(adapter,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                startActivityForResult(
                                        cropOptions.get(item).appIntent,
                                        CROP_FROM_CAMERA);
                            }
                        });

                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                        if (imageUri != null) {
                            getContentResolver().delete(imageUri, null, null);
                            imageUri = null;
                        }
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }

    @Override
    public void onPopWindowClickListener(View view) {
        switch (view.getId()) {
            case R.id.btn_select_pic_camera:
                Intent intent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                imageUri = Uri.fromFile(new File(ImageUtils.getFileRootPath(), "avatar_"
                        + String.valueOf(System.currentTimeMillis())
                        + ".png"));
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, PICK_FROM_CAMERA);
                break;
            case R.id.btn_select_pic_album:
                if (Build.VERSION.SDK_INT < 19) {
                    Intent intentLess = new Intent();
                    intentLess.setType("image/*");
                    intentLess.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intentLess, KITKAT_LESS);
                } else {
                    Intent intentAbove = new Intent();
                    intentAbove.setType("image/*");
                    intentAbove.setAction(Intent.ACTION_OPEN_DOCUMENT);
                    startActivityForResult(intentAbove, KITKAT_ABOVE);
                }
                break;
        }
    }
}
