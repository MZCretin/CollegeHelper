package com.cretin.collegehelper.ui;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cretin.collegehelper.BaseApp;
import com.cretin.collegehelper.R;
import com.cretin.collegehelper.adapter.FirstPageViewPagerAdapter;
import com.cretin.collegehelper.eventbus.NotifyForgetPswSuccess;
import com.cretin.collegehelper.model.UserModel;
import com.cretin.collegehelper.utils.CheckUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.RequestSMSCodeListener;
import cn.bmob.v3.listener.SaveListener;

@EActivity(R.layout.activity_login)
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int FORGETPSW_REQUEST_CODE = 0;
    public static final int FORGETPSW_RESULT_CODE = 1;
    @ViewById
    TextView tvFirstpageRegister;
    @ViewById
    ImageView ivLogo;
    @ViewById
    LinearLayout layoutLinerContainer;
    @ViewById
    ViewPager vpFirstpageContainer;
    @ViewById
    TextView tvFirstpageForgetPsw;

    private FirstPageViewPagerAdapter adapter;
    private List<View> list;
    private InputMethodManager imm;
    /**
     * 登录界面控件
     */
    private EditText etLoginPhonenum;
    private EditText etLoginPsw;
    private ImageView ivLoginLogin;
    /**
     * 注册界面控件
     */
    private EditText etRegisterPhonenum;
    private EditText etRegisterCaptcha;
    private TextView tvRegisterSend;
    private EditText etRegisterPsw;
    private ImageView ivRegisterRegister;
    private TextView tvRegisterTerms;

    private boolean flag = false;

    private String phoneNumLogin;
    private String pswLogin;
    private String phoneNumRegister;
    private String pswRegister;
    private String code;

    private ProgressDialog loginDia;

    @AfterViews
    public void init() {
        getSupportActionBar().hide();
        list = new ArrayList<>();

        //往list中添加View视图
        addView();
        adapter = new FirstPageViewPagerAdapter(list);
        vpFirstpageContainer.setAdapter(adapter);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        vpFirstpageContainer.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    tvFirstpageForgetPsw.setVisibility(View.VISIBLE);
                } else {
                    tvFirstpageForgetPsw.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    //添加视图View
    private void addView() {
        View viewLeft = getLayoutInflater().inflate(R.layout.layout_login, null);
        View viewRight = getLayoutInflater().inflate(R.layout.layout_register, null);
        initInflaterView(viewLeft, viewRight);
        list.add(viewLeft);
        list.add(viewRight);    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 对引入的布局进行控件的初始化
     */
    private void initInflaterView(View viewLeft, View viewRight) {
        etLoginPhonenum = (EditText) viewLeft.findViewById(R.id.et_login_phonenum);
        etLoginPsw = (EditText) viewLeft.findViewById(R.id.et_login_psw);
        ivLoginLogin = (ImageView) viewLeft.findViewById(R.id.iv_login_login);

        ivLoginLogin.setOnClickListener(this);

        etRegisterPhonenum = (EditText) viewRight.findViewById(R.id.et_register_phonenum);
        etRegisterCaptcha = (EditText) viewRight.findViewById(R.id.et_register_captcha);
        tvRegisterSend = (TextView) viewRight.findViewById(R.id.tv_register_send);
        etRegisterPsw = (EditText) viewRight.findViewById(R.id.et_register_psw);
        ivRegisterRegister = (ImageView) viewRight.findViewById(R.id.iv_register_register);
        tvRegisterTerms = (TextView) viewRight.findViewById(R.id.tv_register_terms);

        tvRegisterSend.setOnClickListener(this);
        ivRegisterRegister.setOnClickListener(this);
        tvRegisterTerms.setOnClickListener(this);
    }

    @Click({R.id.tv_firstpage_login, R.id.tv_firstpage_register, R.id.iv_logo, R.id.tv_firstpage_forget_psw})
    public void onClicks(View view) {
        InputMethodManager m = null;
        switch (view.getId()) {
            case R.id.tv_firstpage_login:
                showLayout(m);
                vpFirstpageContainer.setCurrentItem(0);
                tvFirstpageForgetPsw.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_firstpage_register:
                showLayout(m);
                vpFirstpageContainer.setCurrentItem(1);
                tvFirstpageForgetPsw.setVisibility(View.GONE);
                break;
            case R.id.iv_logo:
                if (flag) {
                    ObjectAnimator.ofFloat(ivLogo, "translationY", -400, dipToPixels(LoginActivity.this, 0)).setDuration(500).start();
                    ObjectAnimator.ofFloat(vpFirstpageContainer, "translationY", -800, dipToPixels(LoginActivity.this, 0)).setDuration(500).start();
                    ObjectAnimator.ofFloat(layoutLinerContainer, "translationY", -740, dipToPixels(LoginActivity.this, 0)).setDuration(500).start();
                    flag = false;
                }
                break;
            case R.id.tv_firstpage_forget_psw:
                Intent intent = new Intent(this, ForgetPswActivity_.class);
                startActivityForResult(intent, FORGETPSW_REQUEST_CODE);
                break;
        }
    }


    /**
     * 处理布局隐藏与显示
     */
    public void showLayout(InputMethodManager m) {
        if (!flag) {
            ObjectAnimator.ofFloat(vpFirstpageContainer, "translationY", 0, dipToPixels(LoginActivity.this, -400)).setDuration(500).start();
            ObjectAnimator.ofFloat(layoutLinerContainer, "translationY", 0, dipToPixels(LoginActivity.this, -370)).setDuration(500).start();
            ObjectAnimator.ofFloat(ivLogo, "translationY", 0, dipToPixels(LoginActivity.this, -200)).setDuration(500).start();
        }
        flag = true;
        if (!imm.isActive()) {
            m = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    //变换转换
    public static float dipToPixels(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_login_login:
                login();
                break;
            case R.id.tv_register_send:
                getCaptcha();
                break;
            case R.id.iv_register_register:
                register();
                break;
            case R.id.tv_register_terms:
                break;
        }
    }


    /**
     * 注册操作
     */
    private void register() {
        phoneNumRegister = etRegisterPhonenum.getText().toString();
        pswRegister = etRegisterPsw.getText().toString();
        code = etRegisterCaptcha.getText().toString();
        if (TextUtils.isEmpty(phoneNumRegister)) {
            Toast.makeText(LoginActivity.this, "手机号不能为空！", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(code)) {
            Toast.makeText(LoginActivity.this, "验证码不能为空！", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(pswRegister)) {
            Toast.makeText(LoginActivity.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
        } else if (!CheckUtils.isPasswordLengthOk(pswRegister)) {
            Toast.makeText(LoginActivity.this, "密码长度不合法！", Toast.LENGTH_SHORT).show();
        } else {
            final UserModel user = new UserModel();
            user.setMobilePhoneNumber(phoneNumRegister);//设置手机号码（必填）
            user.setUsername(phoneNumRegister);                  //设置用户名，如果没有传用户名，则默认为手机号码
            user.setPassword(pswRegister);                  //设置用户密码
            user.setId(System.currentTimeMillis()+"mxn");
            user.setPsw(pswRegister);
            user.signOrLogin(this, code, new SaveListener() {

                @Override
                public void onSuccess() {
                    Toast.makeText(LoginActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                    BaseApp.getInstance().setUserModel(user);
                    Intent intent = new Intent(LoginActivity.this, MainActivity_.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

                @Override
                public void onFailure(int code, String msg) {
                    Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * 获取验证码
     */
    private void getCaptcha() {
        phoneNumRegister = etRegisterPhonenum.getText().toString();
        if (TextUtils.isEmpty(phoneNumRegister)) {
            Toast.makeText(LoginActivity.this, "手机号不能为空！", Toast.LENGTH_SHORT).show();
        } else {
            BmobSMS.requestSMSCode(this, phoneNumRegister,"仙念助手", new RequestSMSCodeListener() {

                @Override
                public void done(Integer smsId,BmobException ex) {
                    if(ex==null){//验证码发送成功
                        Toast.makeText(LoginActivity.this, "验证码发送成功", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(LoginActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    /**
     * 登录操作
     */
    private void login() {
        phoneNumLogin = etLoginPhonenum.getText().toString();
        pswLogin = etLoginPsw.getText().toString();

        if (TextUtils.isEmpty(phoneNumLogin)) {
            Toast.makeText(LoginActivity.this, "手机号不能为空！", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(pswLogin)) {
            Toast.makeText(LoginActivity.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
        } else {
            final UserModel user = new UserModel();
            user.setUsername(phoneNumLogin);
            user.setPassword(pswLogin);
            user.setMobilePhoneNumber(phoneNumLogin);//设置手机号码（必填）
            user.login(this, new SaveListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    BmobQuery<UserModel> query = new BmobQuery<>();
                    query.addWhereEqualTo("username", phoneNumLogin);
                    query.findObjects(LoginActivity.this, new FindListener<UserModel>() {
                        @Override
                        public void onSuccess(List<UserModel> object) {
                            if (object != null) {
                                BaseApp.getInstance().setUserModel(object.get(0));
                            }
                        }

                        @Override
                        public void onError(int code, String msg) {
                        }
                    });
                    Intent intent = new Intent(LoginActivity.this, MainActivity_.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                @Override
                public void onFailure(int code, String msg) {
                    Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Subscribe
    public void pswChangeSuccess(NotifyForgetPswSuccess event){
        vpFirstpageContainer.setCurrentItem(0);
        etLoginPhonenum.setText(event.getPhone());
    }
}



