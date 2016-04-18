package com.cretin.collegehelper.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.cretin.collegehelper.globaldata.GlobalData;
import com.cretin.collegehelper.model.FlowModel;
import com.cretin.collegehelper.model.Images;
import com.cretin.collegehelper.model.UserModel;
import com.cretin.collegehelper.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;

public class SendTopicIntentService extends IntentService {
    private static final String ACTION_SEND_TOPIC = "ACTION_SEND_TOPIC";

    public SendTopicIntentService() {
        super("SendTopicIntentService");
    }

    /**
     * 方便处理的静态方法
     *
     * @see IntentService
     */
    public static void startActionSendTopic(Context context) {
//        GlobalData.getInstance();
        Intent intent = new Intent(context, SendTopicIntentService.class);
        intent.setAction(ACTION_SEND_TOPIC);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SEND_TOPIC.equals(action)) {
                //同步处理
                sendTopic();
            }
        }
    }

    private void sendTopic() {
        List<Images> toSendListImage = GlobalData.getInstance().getToSendImagesList();
        GlobalData.getInstance().setToSendImagesList(new ArrayList<Images>()); //发完flow后清空内存
        final String[] filePath = new String[toSendListImage.size()];
        if (filePath.length == 0) {
            FlowModel flowModel = new FlowModel();
            flowModel.setAuthor(BmobUser.getCurrentUser(SendTopicIntentService.this, UserModel.class));
            flowModel.setResourceUrl(null);
            flowModel.setSendTime(System.currentTimeMillis());
            flowModel.setTopicTontent(GlobalData.getInstance().getContent());
            flowModel.save(SendTopicIntentService.this, new SaveListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(SendTopicIntentService.this, "发送成功", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(int i, String s) {
                    Toast.makeText(SendTopicIntentService.this, s, Toast.LENGTH_SHORT).show();
                }
            });
            return;
        }
        //保存每个图片然后上传
        for (int i = 0; i < toSendListImage.size(); i++) {
            Images img = toSendListImage.get(i);
            //保存本地
            String fileName = String.valueOf(System.currentTimeMillis() + Math.round(Math.random() * 10000)) + ".jpg";
            String localPath = ImageUtils.getFileRootPath() + fileName;

            Bitmap toSaveBitmap = img.getmBitmap();
            ImageUtils.saveBitmapToFile(toSaveBitmap, fileName);
            filePath[i] = localPath;
        }
        if (filePath.length > 0) {
            Bmob.uploadBatch(this, filePath, new UploadBatchListener() {

                @Override
                public void onSuccess(List<BmobFile> files, List<String> urls) {
                    if (urls.size() == filePath.length) {
                        FlowModel flowModel = new FlowModel();
                        flowModel.setAuthor(BmobUser.getCurrentUser(SendTopicIntentService.this, UserModel.class));
                        flowModel.setResourceUrl(urls);
                        flowModel.setSendTime(System.currentTimeMillis());
                        flowModel.setTopicTontent(GlobalData.getInstance().getContent());
                        flowModel.setSpotlight(GlobalData.getInstance().getAddress());
                        flowModel.save(SendTopicIntentService.this, new SaveListener() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(SendTopicIntentService.this, "发送成功", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(int i, String s) {
                                Toast.makeText(SendTopicIntentService.this, s, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }


                @Override
                public void onError(int statuscode, String errormsg) {
                    Toast.makeText(SendTopicIntentService.this, errormsg, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
                }
            });
        }
    }
}
