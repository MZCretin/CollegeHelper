package com.cretin.collegehelper.ui;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.cretin.collegehelper.R;
import com.cretin.collegehelper.fragment.MainCourseFragment;
import com.cretin.collegehelper.fragment.MainHomeFragment_;
import com.cretin.collegehelper.fragment.MainMineFragment_;
import com.cretin.collegehelper.fragment.MainVoteFragment_;
import com.cretin.collegehelper.utils.FragmentTabUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements FragmentTabUtils.OnRgsExtraCheckedChangedListener {
    @ViewById
    FrameLayout mainFragmentContainer;
    @ViewById
    RadioGroup rgMainRg;
    @ViewById
    RadioButton rbMainHome;
    @ViewById
    RadioButton rbMainCourse;
    @ViewById
    RadioButton rbMainSocial;
    @ViewById
    RadioButton rbMainVote;
    @ViewById
    RadioButton rbMainMine;
    private List<Fragment> list = new ArrayList<>();

    private int[] resNormal = new int[]{R.mipmap.ic_tabbar_social_normal, R.mipmap.ic_tabbar_playground_normal, R.mipmap.group22x, R.mipmap.ic_tabbar_found_normal, R.mipmap.ic_tabbar_settings_normal};
    private int[] resPressed = new int[]{R.mipmap.ic_tabbar_social_pressed, R.mipmap.ic_tabbar_playground_pressed, R.mipmap.ic_tabbar_papers_pressed, R.mipmap.ic_tabbar_found_pressed, R.mipmap.ic_tabbar_settings_pressed};

    /**
     * 处理返回操作的变量
     */
    private long lastBackTime;

    @AfterViews
    public void init() {
        getSupportActionBar().hide();
        list.add(new MainHomeFragment_());
        list.add(new MainCourseFragment());
//        list.add(new MainSocialFragment());
        list.add(new MainVoteFragment_());
        list.add(new MainMineFragment_());
        new FragmentTabUtils(getSupportFragmentManager(), list, R.id.main_fragment_container, rgMainRg, this);

        rbMainSocial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SendActivity_.class));
            }
        });
    }


    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastBackTime > 1 * 1000) {
            lastBackTime = currentTime;
            Toast.makeText(MainActivity.this, "在按一次退出程序", Toast.LENGTH_SHORT).show();
        } else {
            MainActivity.this.finish();
        }
    }

    @Override
    public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId, int index) {
        if(index!=2) {
            goBack(index);
        }
    }

    /**
     * 还原所有状态
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void goBack(int index) {
        for (int i = 0; i < rgMainRg.getChildCount(); i++) {
            ((RadioButton) rgMainRg.getChildAt(i)).setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(resNormal[i]), null, null);
        }

        //让所有字颜色恢复出厂设置
        rbMainHome.setTextColor(getResources().getColor(R.color.main_bottom_radiogroup));
        rbMainCourse.setTextColor(getResources().getColor(R.color.main_bottom_radiogroup));
        rbMainSocial.setTextColor(getResources().getColor(R.color.main_bottom_radiogroup));
        rbMainVote.setTextColor(getResources().getColor(R.color.main_bottom_radiogroup));
        rbMainMine.setTextColor(getResources().getColor(R.color.main_bottom_radiogroup));

        switch (index) {
            case 0:
                rbMainHome.setTextColor(getResources().getColor(R.color.main_back_pressed));
                break;
            case 1:
                rbMainCourse.setTextColor(getResources().getColor(R.color.main_back_pressed));
                break;
            case 2:
                rbMainSocial.setTextColor(getResources().getColor(R.color.main_back_pressed));
                break;
            case 3:
                rbMainVote.setTextColor(getResources().getColor(R.color.main_back_pressed));
                break;
            case 4:
                rbMainMine.setTextColor(getResources().getColor(R.color.main_back_pressed));
                break;
        }

        ((RadioButton) rgMainRg.getChildAt(index)).setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(resPressed[index]), null, null);
    }
}
