package com.cretin.collegehelper.views;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.cretin.collegehelper.R;
import com.cretin.collegehelper.ui.SeeBigBitmapActivity;
import com.cretin.collegehelper.utils.ImageLoadedCallback;
import com.squareup.picasso.Picasso;


public class ImageFragment extends Fragment implements OnDropListener{
    private static final String IMAGE_URL = "image";
    PhotoView image;
    private String imageUrl;
    private boolean flag;
    //记录是否是头像的大图
    private boolean iconFlag;
    private boolean videoFlag;

    private static Context mContext;
    private ProgressBar progressBar;


    private boolean playFlag;

    public boolean isIconFlag() {
        return iconFlag;
    }

    public void setIconFlag(boolean iconFlag) {
        this.iconFlag = iconFlag;
    }


    public ImageFragment() {
        // Required empty public constructor
    }

    public static ImageFragment newInstance(String param1, Boolean iconFlag, int position, Context context) {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putString(IMAGE_URL, param1);
        args.putBoolean("iconFlag", iconFlag);
        args.putInt("currposition", position);
        mContext = context;
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imageUrl = getArguments().getString(IMAGE_URL);
            setIconFlag(getArguments().getBoolean("iconFlag"));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;
        view = inflater.inflate(R.layout.fragment_image, container, false);
        image = (PhotoView) view.findViewById(R.id.image);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar_image);
        image.setmDropListener(this);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == false) {
                    flag = true;
                    SeeBigBitmapActivity.activity.llSeeBigBitmapContainer.setVisibility(View.GONE);
                    SeeBigBitmapActivity.activity.ivSeeBigBitmapClose.setVisibility(View.GONE);
                } else {
                    flag = false;
                    SeeBigBitmapActivity.activity.llSeeBigBitmapContainer.setVisibility(View.VISIBLE);
                    SeeBigBitmapActivity.activity.ivSeeBigBitmapClose.setVisibility(View.VISIBLE);
                }
            }
        });
        image.enable();
        if (iconFlag) {
            Picasso.with(getActivity()).load(R.mipmap.default_icon).into(image,new ImageLoadedCallback(progressBar, mContext));
        } else {
            Picasso.with(getActivity()).load(imageUrl).into(image, new ImageLoadedCallback(progressBar, mContext));
        }
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void dropDown(float absDeltaX, float absDeltaY, float oldX, float oldY, float currX, float currY) {
        if (!image.isZoonUp()) {
            if (2 * absDeltaX < absDeltaY) {
                SeeBigBitmapActivity.activity.finish();
                SeeBigBitmapActivity.activity.overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
            }
        }
    }
}
