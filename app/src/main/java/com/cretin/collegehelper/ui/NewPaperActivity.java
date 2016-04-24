package com.cretin.collegehelper.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cretin.collegehelper.R;
import com.cretin.collegehelper.adapter.ListViewNewPaperAdapter;
import com.cretin.collegehelper.eventbus.NotifyAddMemberSuccess;
import com.cretin.collegehelper.model.PaperModel;
import com.cretin.collegehelper.model.PaperSendModel;
import com.cretin.collegehelper.model.UserModel;
import com.cretin.collegehelper.openfile.CallbackBundle;
import com.cretin.collegehelper.openfile.OpenFileDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.SaveListener;

@EActivity(R.layout.activity_new_paper)
public class NewPaperActivity extends AppCompatActivity {
    @ViewById
    ImageView ivNewPaperBack;
    @ViewById
    TextView tvNewPaperCommit;
    @ViewById
    ListView listviewNewPaper;
    @ViewById
    LinearLayout linNewPaperHeadviewContainer;
    private ListViewNewPaperAdapter adapter;
    private List<PaperModel> list;
    private List<UserModel> joinList;

    @AfterViews
    public void init() {
        getSupportActionBar().hide();

        list = new ArrayList<>();
        adapter = new ListViewNewPaperAdapter(this, list, R.layout.item_listview_new_paper, ListViewNewPaperAdapter.TYPE_TEACHER);
        addEmptyView();
        listviewNewPaper.setAdapter(adapter);

        tvNewPaperCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((TextView) v).getText().equals("添加")) {
                    //添加成员操作
                    addMember();
                    return;
                }
                if (((TextView) v).getText().equals("提交")) {
                    commit();
                }
            }
        });

        ivNewPaperBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewPaperActivity.this.finish();
            }
        });
    }

    private void commit() {
        if (list.isEmpty()) {
            Toast.makeText(NewPaperActivity.this, "没有可以提交的试题", Toast.LENGTH_SHORT).show();
            return;
        }

        //检查题目的正确答案是否填写
        boolean flag = false;
        String message = "";
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getCorrectAnswer() == 0) {
                message += (i+1) + "、";
                flag = true;
            }
        }
        if(flag){
            Toast.makeText(NewPaperActivity.this, "您还有第"+message+"题没有完成", Toast.LENGTH_SHORT).show();
            return;
        }

        final EditText editText = new EditText(this);
        editText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        editText.setBackground(null);
        new AlertDialog.Builder(this).setTitle("请为这次测试设置一个标题").setView(editText).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String title = editText.getText().toString();
                if (TextUtils.isEmpty(title)) {
                    Toast.makeText(NewPaperActivity.this, "标题非要不可哦", Toast.LENGTH_SHORT).show();
                    return;
                }
                PaperSendModel paperModel = new PaperSendModel();
                paperModel.setCreateTime(System.currentTimeMillis());
                paperModel.setTitle(title);
                paperModel.setAuthor(BmobUser.getCurrentUser(NewPaperActivity.this, UserModel.class));
                paperModel.setPeriod(2);
                paperModel.setTestContent(list);
                BmobRelation relation = new BmobRelation();
                for (UserModel user : joinList) {
                    relation.add(user);
                }
                paperModel.setJoinList(relation);
                paperModel.save(NewPaperActivity.this, new SaveListener() {

                    @Override
                    public void onSuccess() {
                        Toast.makeText(NewPaperActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int arg0, String arg1) {
                        Toast.makeText(NewPaperActivity.this, arg1, Toast.LENGTH_SHORT).show();
                    }
                });

                NewPaperActivity.this.finish();
            }
        }).setNegativeButton("取消", null).show();

    }

    private void addMember() {
        startActivity(new Intent(this, AddMembersActivity_.class));
    }

    //添加空视图
    private void addEmptyView() {
        listviewNewPaper.setEmptyView(linNewPaperHeadviewContainer);

        linNewPaperHeadviewContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(0);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 0) {
            Map<String, Integer> images = new HashMap<>();
            images.put(OpenFileDialog.sRoot, R.mipmap.filedialog_root);
            images.put(OpenFileDialog.sParent, R.mipmap.filedialog_folder_up);
            images.put(OpenFileDialog.sFolder, R.mipmap.filedialog_folder);
            images.put("wav", R.mipmap.filedialog_wavfile);
            images.put(OpenFileDialog.sEmpty, R.mipmap.filedialog_root);
            Dialog dialog = OpenFileDialog.createDialog(id, this, "选择TXT测试文件", new CallbackBundle() {
                        @Override
                        public void callback(Bundle bundle) {
                            String filepath = bundle.getString("path");
                            readTxtFile(filepath);
                        }
                    },
                    ".txt;",
                    images);
            return dialog;
        }
        return null;
    }


    public void readTxtFile(String filePath) {
        try {
            File file = new File(filePath);
            int lins = 0;
            PaperModel paper = null;
            if (file.isFile() && file.exists()) { //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file));//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                list.clear();
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    if (lins % 2 == 0) {
                        paper = new PaperModel();
                        paper.setTitle(lineTxt);
                    } else {
                        if (lineTxt.contains("A)") && lineTxt.contains("B)") && lineTxt.contains("C)")) {
                            paper.setAnswer(lineTxt);
                            list.add(paper);
                        } else {
                            Toast.makeText(NewPaperActivity.this, "内容格式错误,请重新选择文件", Toast.LENGTH_SHORT).show();
                        }
                    }
                    lins++;
                }
                adapter.notifyDataSetChanged();
                read.close();
                if (list.isEmpty()) {
                    Toast.makeText(NewPaperActivity.this, "内容格式错误,请重新选择文件", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(NewPaperActivity.this, "找不到指定的文件", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(NewPaperActivity.this, "读取文件内容出错", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void addMemberNotify(NotifyAddMemberSuccess event) {
        joinList = event.getJoinList();
        tvNewPaperCommit.setText("提交");
    }
}
