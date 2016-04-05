package com.cretin.collegehelper.ui;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cretin.collegehelper.R;
import com.cretin.collegehelper.eventbus.NotifyForgetPswSuccess;
import com.cretin.collegehelper.utils.CheckUtils;
import com.cretin.collegehelper.utils.TimeCount;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.RequestSMSCodeListener;
import cn.bmob.v3.listener.ResetPasswordByCodeListener;


@EActivity(R.layout.activity_forget_psw)
public class ForgetPswActivity extends AppCompatActivity {
    @ViewById
    TextView ivForgetAdd;
    @ViewById
    EditText etForgetCaptcha;
    @ViewById
    TextView tvForgetSend;
    @ViewById
    EditText etForgetPsw;
    @ViewById
    ImageView ivForgetVisiable;
    @ViewById
    ImageView ivForgetUpdate;
    @ViewById
    EditText etForgetPhonenum;

    private String mobile;
    private String newPassword;
    private String code;

    //记录密码是否可见  默认不可见
    private boolean flag = false;

    //处理获取验证码倒计时
    private TimeCount timer;


    @AfterViews
    public void init() {
        getSupportActionBar().hide();
    }

    @Click({R.id.iv_forget_add, R.id.tv_forget_send, R.id.iv_forget_visiable, R.id.iv_forget_update})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_forget_add:
                break;
            case R.id.tv_forget_send:
                //发送忘记密码的验证码
                sendCaptcha();
                break;
            case R.id.iv_forget_visiable:
                if (flag) {
                    ivForgetVisiable.setImageResource(R.mipmap.close);
                    etForgetPsw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    flag = false;
                } else {
                    ivForgetVisiable.setImageResource(R.mipmap.open);
                    etForgetPsw.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    flag = true;
                }
                break;
            case R.id.iv_forget_update:
                //修改密码操作
                updatePsw();
                break;
        }
    }

    /**
     * 修改密码
     */
    private void updatePsw() {
        mobile = etForgetPhonenum.getText().toString();
        newPassword = etForgetPsw.getText().toString();
        code = etForgetCaptcha.getText().toString();
        if (TextUtils.isEmpty(mobile)) {
            Toast.makeText(ForgetPswActivity.this, "手机号不能为空！", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(code)) {
            Toast.makeText(ForgetPswActivity.this, "请输入验证码！", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(newPassword)) {
            Toast.makeText(ForgetPswActivity.this, "请输入新密码！", Toast.LENGTH_SHORT).show();
        } else if (!CheckUtils.isPasswordLengthOk(newPassword)) {
            Toast.makeText(ForgetPswActivity.this, "密码长度至少6位", Toast.LENGTH_SHORT).show();
        } else {
            BmobUser.resetPasswordBySMSCode(this, code, newPassword, new ResetPasswordByCodeListener() {

                @Override
                public void done(BmobException ex) {
                    if (ex == null) {
                        Toast.makeText(ForgetPswActivity.this, "密码重置成功", Toast.LENGTH_SHORT).show();
                        NotifyForgetPswSuccess notifyForgetPswSuccess = new NotifyForgetPswSuccess();
                        notifyForgetPswSuccess.setPhone(mobile);
                        EventBus.getDefault().post(notifyForgetPswSuccess);
                        ForgetPswActivity.this.finish();
                    } else {
                        Toast.makeText(ForgetPswActivity.this, "重置失败：code =" + ex.getErrorCode() + ",msg = " + ex.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    //发送忘记密码的验证码
    private void sendCaptcha() {
        mobile = etForgetPhonenum.getText().toString();
        if (TextUtils.isEmpty(mobile)) {
            Toast.makeText(ForgetPswActivity.this, "手机号不能为空！", Toast.LENGTH_SHORT).show();
        } else {
            BmobSMS.requestSMSCode(this, mobile, "仙念助手", new RequestSMSCodeListener() {

                @Override
                public void done(Integer smsId, BmobException ex) {
                    if (ex == null) {//验证码发送成功
                        Toast.makeText(ForgetPswActivity.this, "验证码发送成功", Toast.LENGTH_SHORT).show();
                        timer = new TimeCount(60000, 1000, tvForgetSend);//构造CountDownTimer对象
                        timer.start();
                        tvForgetSend.setTextColor(Color.rgb(195, 200, 204));
                    } else {
                        Toast.makeText(ForgetPswActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                        if (timer != null)
                            timer.onFinish();
                    }
                }
            });
        }

    }

}

