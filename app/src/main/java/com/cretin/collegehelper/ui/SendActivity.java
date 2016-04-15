package com.cretin.collegehelper.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cretin.collegehelper.R;
import com.cretin.collegehelper.adapter.GridViewAdapter;
import com.cretin.collegehelper.globaldata.GlobalData;
import com.cretin.collegehelper.model.Images;
import com.cretin.collegehelper.services.SendTopicIntentService;
import com.cretin.collegehelper.views.NoScroolGridView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;


@EActivity(R.layout.activity_send)
public class SendActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String ACTION_ADD_IMAGES = "ACTION_ADD_IMAGES";
    @ViewById
    ImageView ivCommentSendBack;
    @ViewById
    RelativeLayout relaCommonSendActionbar;
    @ViewById
    EditText edCommonSendComtent;
    @ViewById
    NoScroolGridView gvCommonSendPics;
    @ViewById
    Button btnCommonSendSend;
    @ViewById
    LinearLayout llCommonSendBottomContainer;
    //GridView的适配器
    private GridViewAdapter adapter;
    private List<Images> mListImage = new ArrayList<>();
    private int mCurrentImagesCount = 0;

    private Images image;

    private String currId;
    private String currName;
    private int type;

    @AfterViews
    public void init() {
        getSupportActionBar().hide();

        getIntentDatas(getIntent());

        //给GridView添加“添加”图片
        for (int i = 0; i < 1; i++) {
            image = new Images(Images.TYPE_RES);
            image.setRes(R.mipmap.publishv2_add_photo2x);
            mListImage.add(image);
        }

        adapter = new GridViewAdapter(SendActivity.this, mListImage, R.layout.item_gridview_common_sendflow);
        gvCommonSendPics.setAdapter(adapter);

        gvCommonSendPics.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mCurrentImagesCount < 9 && position == mListImage.size() - 1) {
                    Intent intent = new Intent(SendActivity.this, CameraPhotoActivity_.class);
                    startActivity(intent);
                } else {

                }
            }
        });

        addListener();

        //让其他activity访问
        GlobalData.getInstance().setSendFlowActivity(this);
    }

    private void addListener() {
        ivCommentSendBack.setOnClickListener(this);
        btnCommonSendSend.setOnClickListener(this);
    }

    private void getIntentDatas(Intent intent) {
        currId = intent.getStringExtra("id");
        currName = intent.getStringExtra("name");
        type = intent.getIntExtra("type", 0);
    }

    /**
     * 从其他界面使用newTask方式返回到本界面, 获取新的intent并更新相应的值.
     *
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // 添加了更多的图片
        String action = intent.getAction();
        if (TextUtils.isEmpty(action))
            return;

        if (action.equals(ACTION_ADD_IMAGES)) {
            List<Images> globalImagesList = GlobalData.getInstance().getImagesList();

            if (globalImagesList.isEmpty()) {
                return;
            }

            if (!mListImage.isEmpty()) {
                mListImage.remove(mListImage.size() - 1);
            }

            mListImage.addAll(globalImagesList);
            mCurrentImagesCount = mListImage.size();
            GlobalData.getInstance().setImagesList(new ArrayList<Images>());
            if (mListImage.size() < 9) {
                Images images = new Images(Images.TYPE_RES);
                images.setRes(R.mipmap.publishv2_add_photo2x);
                mListImage.add(images);
            }

            adapter.notifyDataSetChanged();
            return;
        }
    }

    //发flow
    private void sendFlow() {
        if (TextUtils.isEmpty(edCommonSendComtent.getText().toString()) && mListImage.size() <= 1) {
            Toast.makeText(SendActivity.this, "内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Images> toSendListImage = new ArrayList<>();
        toSendListImage.addAll(mListImage);
        if (toSendListImage.size() < 9) {
            toSendListImage.remove(toSendListImage.size() - 1);
        }

        GlobalData.getInstance().setToSendImagesList(toSendListImage);
        GlobalData.getInstance().setContent(edCommonSendComtent.getText().toString());

        Toast.makeText(SendActivity.this, "发送中...", Toast.LENGTH_SHORT).show();
        SendTopicIntentService.startActionSendTopic(SendActivity.this);
        justGoBack();

        // 返回
    }


    // 当前界面已有图片数量
    public int getCurrentCount() {
        return mCurrentImagesCount;
    }

    @Override
    public void onBackPressed() {
        checkBeforeGoBack();
    }

    //检查是否需要保存
    private void checkBeforeGoBack() {
        if (!TextUtils.isEmpty(currId) ||
                !TextUtils.isEmpty(edCommonSendComtent.getText().toString()) ||
                mListImage.size() > 1
                ) {
            new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("放弃本次发布?")
                    .setPositiveButton("放弃", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            justGoBack();
                        }

                    })
                    .setNegativeButton("继续编辑", null)
                    .show();
            return;
        }

        justGoBack();
    }

    private void justGoBack() {
        Intent intent = new Intent(SendActivity.this, MainActivity_.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_common_send_send:
                sendFlow();
                break;
            case R.id.iv_comment_send_back:
                checkBeforeGoBack();
                break;
        }
    }
}
