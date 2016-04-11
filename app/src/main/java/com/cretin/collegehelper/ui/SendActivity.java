package com.cretin.collegehelper.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
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
import com.cretin.collegehelper.model.FlowModel;
import com.cretin.collegehelper.model.Images;
import com.cretin.collegehelper.model.UserModel;
import com.cretin.collegehelper.utils.ImageUtils;
import com.cretin.collegehelper.views.NoScroolGridView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;


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

        new UploadAsync().execute(toSendListImage);
        Toast.makeText(SendActivity.this, "发送中...", Toast.LENGTH_SHORT).show();
        justGoBack();

        // 返回
    }

    private class UploadAsync extends AsyncTask<List<Images>, Void, Void> {

        @Override
        protected Void doInBackground(List<Images>... params) {
            String[] filePath = new String[params[0].size()];
            final List<String> urlList = new ArrayList<>();
            //保存每个图片然后上传
            for (int i = 0; i < params[0].size(); i++) {
                Images img = params[0].get(i);
                //保存本地
                String fileName = String.valueOf(System.currentTimeMillis() + Math.round(Math.random() * 10000)) + ".jpg";
                String localPath = ImageUtils.getFileRootPath() + fileName;

                Bitmap toSaveBitmap = img.getmBitmap();
                ImageUtils.saveBitmapToFile(toSaveBitmap, fileName);
//                if (toSaveBitmap != null) {
//                    toSaveBitmap.recycle();
//                    toSaveBitmap = null;
//                }
                filePath[i] = localPath;
            }
            if (filePath.length > 0) {
                Bmob.uploadBatch(SendActivity.this, filePath, new UploadBatchListener() {

                    @Override
                    public void onSuccess(List<BmobFile> files, List<String> urls) {
                        urlList.clear();
                        urlList.addAll(urls);
                    }

                    @Override
                    public void onError(int statuscode, String errormsg) {
                    }

                    @Override
                    public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
                        if(totalPercent == 100){
                            FlowModel flowModel = new FlowModel();
                            flowModel.setAuthor(BmobUser.getCurrentUser(SendActivity.this, UserModel.class));
                            flowModel.setResourceUrl(urlList);
                            flowModel.setSendTime(System.currentTimeMillis());
                            flowModel.setTopicTontent(edCommonSendComtent.getText().toString());
                            flowModel.save(SendActivity.this, new SaveListener() {
                                @Override
                                public void onSuccess() {
                                    Toast.makeText(SendActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(int i, String s) {
                                    Toast.makeText(SendActivity.this, s, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            }
            return null;
        }

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
        //TODO 检查
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

    //region EventBus
    private List<Object> mEventSubscriberList;

    private void registerEventBus() {
        mEventSubscriberList = new ArrayList<>();
//        mEventSubscriberList.add(new EventSubscriber());
        mEventSubscriberList.add(this);

        for (Object obj : mEventSubscriberList) {
            EventBus.getDefault().register(obj);
        }
    }

    private void unRegisterEventBus() {
        for (Object obj : mEventSubscriberList) {
            EventBus.getDefault().unregister(obj);
        }
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

    public static class MessageStringEvent {
        public final String message;

        public MessageStringEvent(String message) {
            this.message = message;
        }
    }

    public static class MessageIntegerEvent {
        public final Integer message;

        public MessageIntegerEvent(Integer message) {
            this.message = message;
        }
    }

    @Subscribe
    public void onEvent(MessageStringEvent event) {
    }

    @Subscribe
    public void onEvent(MessageIntegerEvent event) {

    }
}
