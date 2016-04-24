package com.cretin.collegehelper.ui;

import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cretin.collegehelper.R;
import com.cretin.collegehelper.adapter.ListViewNewPaperAdapter;
import com.cretin.collegehelper.model.PaperModel;
import com.cretin.collegehelper.model.PaperSendModel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@EActivity(R.layout.activity_papering)
public class PaperingActivity extends AppCompatActivity {
    @ViewById
    ImageView ivPaperingBack;
    @ViewById
    TextView tvPaperingLasttime;
    @ViewById
    ListView listviewPapering;
    private PaperSendModel paperSendModel;
    private List<PaperModel> list;
    private ListViewNewPaperAdapter adapter;
    private Timer mTimer;
    private long timeLast;

    @AfterViews
    public void init() {
        getSupportActionBar().hide();

        mTimer = new Timer();

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
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    timeLast -= 1000;
                    String timeStr = "";
                    int minute = (int) (timeLast / 1000 / 60);
                    int second = (int) (timeLast / 1000 % 60);
                    timeStr = minute + ":" + second;
                    tvPaperingLasttime.setText("倒计时: " + timeStr);
                }
            }, 0, 1000);
        }
    }
}
