package com.cretin.collegehelper.popwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.cretin.collegehelper.R;

/**
 * Created by cretin on 16/3/4.
 */
public class SelectPopupWindow extends PopupWindow implements View.OnClickListener {
    public static final int TYPE_MAIN_HOME = 0;
    public static final int TYPE_SETTING = 1;
    public static final int TYPE_SELECT_PIC = 2;
    private View mMenuView;
    private OnPopWindowClickListener listener;

    public SelectPopupWindow(Activity context, OnPopWindowClickListener listener, int type) {
        initView(context, listener, type, false);
    }

    public SelectPopupWindow(Activity context, OnPopWindowClickListener listener, int type, boolean flag) {
        initView(context, listener, type, flag);
    }


    private void initView(Activity context, OnPopWindowClickListener listener, int type, boolean flag) {
        this.listener = listener;
        //设置按钮监听
        if (type == TYPE_MAIN_HOME) {
            initViewMain(context, flag);
        }

        if (type == TYPE_SETTING) {
            initViewSetting(context);
        }

        if (type == TYPE_SELECT_PIC) {
            initViewSelectedPic(context);
        }

        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.dialog_style);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    private void initViewSelectedPic(Activity context) {
        Button btnCamera, btnPhoto, btnCancel;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.layout_popwindow_dialog_select_pic, null);
        btnCamera = (Button) mMenuView.findViewById(R.id.btn_select_pic_camera);
        btnPhoto = (Button) mMenuView.findViewById(R.id.btn_select_pic_album);
        btnCancel = (Button) mMenuView.findViewById(R.id.btn_select_pic_cancel);

        btnCamera.setOnClickListener(this);
        btnPhoto.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    private void initViewMain(Activity context, boolean flag) {
        Button btnShare, btnJubao, btnDelete, btnCancel;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.layout_popwindow_dialog_main, null);
        btnShare = (Button) mMenuView.findViewById(R.id.btn_main_share);
        btnJubao = (Button) mMenuView.findViewById(R.id.btn_main_jubao);
        btnDelete = (Button) mMenuView.findViewById(R.id.btn_main_delete);
        btnCancel = (Button) mMenuView.findViewById(R.id.btn_main_cancel);

        btnShare.setOnClickListener(this);
        btnJubao.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        if (flag) {
            btnDelete.setText("删除");
            btnDelete.setVisibility(View.VISIBLE);
        } else {
            btnDelete.setVisibility(View.GONE);
        }
    }

    private void initViewSetting(Activity context) {
        Button btnAbout, btnExit, btnUpdate, btnCancel;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.layout_popwindow_dialog_setting, null);
        btnAbout = (Button) mMenuView.findViewById(R.id.btn_setting_about);
        btnExit = (Button) mMenuView.findViewById(R.id.btn_setting_exit);
        btnUpdate = (Button) mMenuView.findViewById(R.id.btn_setting_update);
        btnCancel = (Button) mMenuView.findViewById(R.id.btn_setting_cancel);

        btnUpdate.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnExit.setOnClickListener(this);
        btnAbout.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        listener.onPopWindowClickListener(v);
        dismiss();
    }


    public interface OnPopWindowClickListener {
        void onPopWindowClickListener(View view);
    }

}
