package com.cretin.collegehelper.ui;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.cretin.collegehelper.R;
import com.cretin.collegehelper.adapter.ListViewNewPaperAdapter;
import com.cretin.collegehelper.model.PaperModel;
import com.cretin.collegehelper.model.PaperSendModel;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_paper_details)
public class PaperDetailsActivity extends AppCompatActivity {
    @ViewById
    ImageView ivPaperDetailsBack;
    @ViewById
    ListView listviewPaperDetails;
    @ViewById
    SwipyRefreshLayout swipyListviewDetails;
    private List<PaperModel> list;
    private ListViewNewPaperAdapter adapter;
    private PaperSendModel currModel;

    @AfterViews
    public void init() {
        getSupportActionBar().hide();

        currModel = (PaperSendModel) getIntent().getSerializableExtra("papersendmodel");

        if (currModel != null) {
            list = currModel.getTestContent();
        } else {
            list = new ArrayList<>();
        }

        adapter = new ListViewNewPaperAdapter(this, list, R.layout.item_listview_new_paper, ListViewNewPaperAdapter.TYPE_TEACHER);
        listviewPaperDetails.setAdapter(adapter);

        ivPaperDetailsBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PaperDetailsActivity.this.finish();
            }
        });
    }
}
