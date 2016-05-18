package com.cretin.collegehelper.fragment;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cretin.collegehelper.R;
import com.cretin.collegehelper.ui.NewPaperActivity_;
import com.cretin.collegehelper.ui.PaperMyCreatedActivity_;
import com.cretin.collegehelper.ui.PaperMyFinishedActivity_;
import com.cretin.collegehelper.ui.PaperMyJoinedActivity_;
import com.cretin.collegehelper.ui.ShowWebViewActivity_;
import com.cretin.collegehelper.ui.TeacherOnLineActivity_;

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
    ImageView ivOnline;
    @ViewById
    RelativeLayout relaOnlineContainer;
    @ViewById
    ImageView ivNewPaper;
    @ViewById
    RelativeLayout relaNewPaperContainer;
    @ViewById
    ImageView ivSendedPaper;
    @ViewById
    RelativeLayout relaSendedPaperContainer;
    @ViewById
    ImageView ivMyJoined;
    @ViewById
    RelativeLayout relaMyJoinedContainer;
    @ViewById
    ImageView ivMyFinished;
    @ViewById
    RelativeLayout relaMyFinishedContainer;
    private String[] urlRes = new String[]{"http://210.42.38.26:84/jwc_glxt/", "http://jwc.ctgu.edu.cn/news_more.asp?lm=&lm2=67&lmname=0&open=1&n=30&tj=0&hot=0&lryname=&tcolor=999999", "http://jwc.ctgu.edu.cn/news_more.asp?lm=&lm2=69&lmname=0&open=1&n=30&tj=0&hot=0&lryname=&tcolor=999999", "http://jwc.ctgu.edu.cn/news_more.asp?lm=&lm2=68&lmname=0&open=1&n=30&tj=0&hot=0&lryname=&tcolor=999999"};
    private int res[] = new int[]{R.mipmap.jiaowuchu, R.mipmap.jiaowutongzhi, R.mipmap.kaoshizhuanlan, R.mipmap.jishishiwu};

    public MainCourseFragment() {
        // Required empty public constructor
    }

    @AfterViews
    public void init() {
        kannerCourse.setImagesRes(res);
        List<ImageView> listImageView = kannerCourse.getImageViewList();

        for (int i = 0; i < listImageView.size(); i++) {
            final int finalI = i;
            listImageView.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ShowWebViewActivity_.class);
                    intent.putExtra("url", urlRes[finalI-1]);
                    startActivity(intent);
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

        relaMyJoinedContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PaperMyJoinedActivity_.class));
            }
        });

        relaMyFinishedContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PaperMyFinishedActivity_.class));
            }
        });

        relaSendedPaperContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PaperMyCreatedActivity_.class));
            }
        });

        relaOnlineContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TeacherOnLineActivity_.class));
            }
        });
    }

}
