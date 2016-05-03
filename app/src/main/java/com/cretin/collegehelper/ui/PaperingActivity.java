package com.cretin.collegehelper.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cretin.collegehelper.R;
import com.cretin.collegehelper.adapter.ListViewNewPaperAdapter;
import com.cretin.collegehelper.model.PaperModel;
import com.cretin.collegehelper.model.PaperResultModel;
import com.cretin.collegehelper.model.PaperSendModel;
import com.cretin.collegehelper.model.UserModel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

@EActivity(R.layout.activity_papering)
public class PaperingActivity extends AppCompatActivity {
    @ViewById
    ImageView ivPaperingBack;
    @ViewById
    TextView tvPaperingLasttime;
    @ViewById
    ListView listviewPapering;
    @ViewById
    TextView tvPaperingCommit;
    private PaperSendModel paperSendModel;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
    private List<PaperModel> list;
    private ListViewNewPaperAdapter adapter;
    private long timeLast;
    private Timer timer = new Timer();
    private double score;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 1:
                    timeLast -= 1000;
                    String strMin = "";
                    String strSec = "";
                    String strHour = "";
                    int hour = (int) (timeLast / 1000 / 60 / 60);
                    int minute = (int) (timeLast / 1000 / 60 % 60);
                    int second = (int) (timeLast / 1000 % 60);
                    if (hour < 10) {
                        strHour = "0" + hour;
                    } else {
                        strHour = hour + "";
                    }
                    if (minute < 10) {
                        strMin = "0" + minute;
                    } else {
                        strMin = minute + "";
                    }
                    if (second < 10) {
                        strSec = "0" + second;
                    } else {
                        strSec = second + "";
                    }
                    tvPaperingLasttime.setText(strHour + ":" + strMin + ":" + strSec);
                    if (hour == 0 && minute == 0 && second == 0) {
                        //时间到
                        timer.cancel();
                        Toast.makeText(PaperingActivity.this, "时间到,系统正在批改试卷...", Toast.LENGTH_SHORT).show();
                        commit(true);
                        return;
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            Message msg = new Message();
            msg.what = 1;
            handler.sendMessage(msg);
        }
    };

    @AfterViews
    public void init() {
        getSupportActionBar().hide();

        paperSendModel = (PaperSendModel) getIntent().getSerializableExtra("papermodel");

        if (paperSendModel != null) {
            list = paperSendModel.getTestContent();
            timeLast = paperSendModel.getCreateTime() + paperSendModel.getPeriod() * 1000 * 60 - System.currentTimeMillis();
        } else {
            list = new ArrayList<>();
        }
        adapter = new ListViewNewPaperAdapter(this, list, R.layout.item_listview_new_paper, ListViewNewPaperAdapter.TYPE_STUDENT);
        listviewPapering.setAdapter(adapter);

        if (timeLast > 0) {
            timer.schedule(task, 0, 1000);
        }

        tvPaperingCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //提交操作
                commit(false);
            }
        });

        ivPaperingBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBeforeGoBack();
            }
        });
    }

    //提交本地答案
    private void commit(boolean timeFlag) {
        if (timeFlag) {
            final String answer = getAnswerString();
            PaperResultModel paperModel = new PaperResultModel();
            paperModel.setCommitTime(System.currentTimeMillis());
            paperModel.setAnswer(answer.split("--")[0]);
            paperModel.setAnswerDes(answer.split("--")[1]);
            paperModel.setAuthor(BmobUser.getCurrentUser(PaperingActivity.this, UserModel.class));
            paperModel.setScore(score);
            paperModel.setContent(list);
            paperModel.setTitle(paperSendModel.getTitle());
            paperModel.setFromAuthor(paperSendModel.getAuthor());
            long tempTime = paperSendModel.getCreateTime() + (paperSendModel.getPeriod() * 1000 * 60);
            String strPeriod = format.format(paperSendModel.getCreateTime()) + "~" + format.format(tempTime);
            paperModel.setPeriod(strPeriod);
            paperModel.save(PaperingActivity.this, new SaveListener() {

                @Override
                public void onSuccess() {
                    removeInfo(answer);
                }

                @Override
                public void onFailure(int arg0, String arg1) {
                    Toast.makeText(PaperingActivity.this, arg1, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            //检查题目的正确答案是否填写
            boolean flag = false;
            String message = "";
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getCorrectAnswer() == 0) {
                    message += (i + 1) + "、";
                    flag = true;
                }
            }
            if (flag) {
                Toast.makeText(PaperingActivity.this, "您还有第" + message + "题没有完成", Toast.LENGTH_SHORT).show();
                return;
            }

            new AlertDialog.Builder(this).setMessage("确认提交吗").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final String answer = getAnswerString();
                    PaperResultModel paperModel = new PaperResultModel();
                    paperModel.setCommitTime(System.currentTimeMillis());
                    paperModel.setAnswer(answer.split("--")[0]);
                    paperModel.setAnswerDes(answer.split("--")[1]);
                    paperModel.setAuthor(BmobUser.getCurrentUser(PaperingActivity.this, UserModel.class));
                    paperModel.setScore(score);
                    paperModel.setContent(list);
                    paperModel.setTitle(paperSendModel.getTitle());
                    paperModel.setFromAuthor(paperSendModel.getAuthor());
                    long tempTime = paperSendModel.getCreateTime() + (paperSendModel.getPeriod() * 1000 * 60);
                    String strPeriod = format.format(paperSendModel.getCreateTime()) + "~" + format.format(tempTime);
                    paperModel.setPeriod(strPeriod);
                    paperModel.save(PaperingActivity.this, new SaveListener() {

                        @Override
                        public void onSuccess() {
                            removeInfo(answer);
                        }

                        @Override
                        public void onFailure(int arg0, String arg1) {
                            Toast.makeText(PaperingActivity.this, arg1, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).setNegativeButton("取消", null).show();
        }
    }

    private void removeInfo(final String answer) {
        BmobRelation relation = paperSendModel.getJoinList();
        relation.remove(BmobUser.getCurrentUser(PaperingActivity.this, UserModel.class));
        paperSendModel.setJoinList(relation);
        paperSendModel.update(this, new UpdateListener() {

            @Override
            public void onSuccess() {
                new AlertDialog.Builder(PaperingActivity.this).setTitle("成绩公布")
                        .setMessage("你的答案:\n" + answer.split("--")[0] + "\n提交反馈:\n" + answer.split("--")[1] + "\n分数:" + score + "分").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PaperingActivity.this.finish();
                    }
                }).show();
            }

            @Override
            public void onFailure(int arg0, String arg1) {
                Toast.makeText(PaperingActivity.this, arg1, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getAnswerString() {
        String answer = "";
        String answerDes = "";
        int correct = 0;
        for (PaperModel p : list) {
            switch (p.getAnswerIndex()) {
                case 0:
                    answer += "空 ";
                    break;
                case 1:
                    answer += "A ";
                    break;
                case 2:
                    answer += "B ";
                    break;
                case 3:
                    answer += "C ";
                    break;
                case 4:
                    answer += "D ";
                    break;
            }

            if (p.getAnswerIndex() == p.getCorrectAnswer()) {
                answerDes += "对 ";
                correct++;
            } else {
                answerDes += "错 ";
            }
        }
        score = ((double) correct * 100) / list.size();
        return answer + "--" + answerDes;
    }

    @Override
    public void onBackPressed() {
        checkBeforeGoBack();
    }

    //检查是否需要保存
    private void checkBeforeGoBack() {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("测试尚未结束,放弃本次测试?")
                .setPositiveButton("放弃", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PaperingActivity.this.finish();
                    }

                })
                .setNegativeButton("继续", null)
                .show();
        return;
    }
}
