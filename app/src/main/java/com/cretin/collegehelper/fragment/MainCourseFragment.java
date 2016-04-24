package com.cretin.collegehelper.fragment;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cretin.collegehelper.R;
import com.cretin.collegehelper.ui.NewPaperActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import krelve.view.Kanner;

/**
 * A simple {@link Fragment} subclass.
 */
@EFragment(R.layout.fragment_main_course)
public class MainCourseFragment extends Fragment {
    @ViewById
    RelativeLayout relaHeadMain;
    @ViewById
    Kanner kannerCourse;
    @ViewById
    ImageView ivNewPaper;
    @ViewById
    RelativeLayout relaNewPaperContainer;
    @ViewById
    ImageView ivSendedPaper;
    @ViewById
    RelativeLayout relaSendedPaperContainer;
    @ViewById
    ImageView ivPapering;
    @ViewById
    RelativeLayout relaPaperingContainer;
    @ViewById
    ImageView ivMyJoined;
    @ViewById
    RelativeLayout relaMyJoinedContainer;
    private int res[] = new int[]{R.mipmap.jiaowuchu,R.mipmap.jiaowutongzhi,R.mipmap.kaoshizhuanlan,R.mipmap.jishishiwu};

    public MainCourseFragment() {
        // Required empty public constructor
    }

    @AfterViews
    public void init(){

        kannerCourse.setImagesRes(res);
        List<ImageView> listImageView = kannerCourse.getImageViewList();

        for (ImageView iv : listImageView) {
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        addListener();
    }

    private void addListener() {
        relaNewPaperContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NewPaperActivity_.class));
            }
        });
    }

}
