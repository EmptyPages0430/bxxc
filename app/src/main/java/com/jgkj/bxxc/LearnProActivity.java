package com.jgkj.bxxc;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jgkj.bxxc.adapter.MyLearnProAdapter;
import com.jgkj.bxxc.bean.LearnProAction;
import com.jgkj.bxxc.bean.UserInfo;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by fangzhou on 2016/11/5.
 * 学车进程时间轴
 */
public class LearnProActivity extends Activity implements View.OnClickListener {
    private Button back_forward;
    private TextView text_title;
    private ImageView proImage;
    private ListView listView;
    private MyLearnProAdapter adapter;
    private List<LearnProAction> list = null;
    private TextView textTitle;
    private LearnProAction action, action2, action3, action4, action5, action6, action7, action8;
    private TextView username, progress;
    private String name, state, url;
    private ImageView userImage;
    private String token;
    //前往下一步
    private TextView gotoComplete;
    //提交申请
    private Dialog dialog;
    private View inflate;
    private TextView dialog_textView, dialog_sure, dialog_cancel;
    private String payUrl = "http://www.baixinxueche.com/index.php/Home/Aliappretext/retestPay";
    private UserInfo.Result result;
    private String refreashUrl = "http://www.baixinxueche.com/index.php/Home/Apialltoken/refresh";
    private String mianqianUrl = "http://www.baixinxueche.com/index.php/Home/Apialltoken/apply";
    private String sub1Url = "http://www.baixinxueche.com/index.php/Home/Apialltoken/applyone";
    private String sub2Url = "http://www.baixinxueche.com/index.php/Home/Apitokenupdata/applySubjectStudy";
    private String applyUrl = "http://www.baixinxueche.com/index.php/Home/Apitokenupdata/applySubjectTestAgain";
    private String sub4Url = "http://www.baixinxueche.com/index.php/Home/Apialltoken/applyFour";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.process);
        initView();
        SharedPreferences sp = getSharedPreferences("USER",
                Activity.MODE_PRIVATE);
        String str = sp.getString("userInfo", null);
        Gson gson = new Gson();
        UserInfo userInfo = gson.fromJson(str, UserInfo.class);
        UserInfo.Result res = userInfo.getResult();
        refreshInfo(res.getUid() + "", refreashUrl);
        getData();
        init();
    }

    private void getData() {
        SharedPreferences sp = getSharedPreferences("USER",
                Activity.MODE_PRIVATE);
        String str = sp.getString("userInfo", null);
        Gson gson = new Gson();
        UserInfo userInfo = gson.fromJson(str, UserInfo.class);
        result = userInfo.getResult();
        state = result.getState();
        name = result.getName();
        if (result.getName().equals("") || result == null) {
            name = result.getPhone();
        } else {
            name = result.getName();
        }
        url = result.getPic();
    }

    private void initView() {
        back_forward = (Button) findViewById(R.id.button_backward);
        back_forward.setVisibility(View.VISIBLE);
        back_forward.setOnClickListener(this);
        textTitle = (TextView) findViewById(R.id.text_title);

        gotoComplete = (TextView) findViewById(R.id.gotoComplete);
        gotoComplete.setOnClickListener(this);
        userImage = (ImageView) findViewById(R.id.userImage);
        username = (TextView) findViewById(R.id.username);
        progress = (TextView) findViewById(R.id.progress);
        listView = (ListView) findViewById(R.id.listView);
        Intent intent = getIntent();
        token = intent.getStringExtra("token");

    }

    //初始化布局
    private void init() {

        if (!url.endsWith(".jpg") && !url.endsWith(".jpeg") && !url.endsWith(".png") &&
                !url.endsWith(".GIF") && !url.endsWith(".PNG") && !url.endsWith(".JPG") && !url.endsWith(".gif")) {
            Glide.with(this).load("http://www.baixinxueche.com/Public/Home/img/default.png").into(userImage);
        } else {
            Glide.with(this).load(url).placeholder(R.drawable.head1).error(R.drawable.head1).into(userImage);
        }
        username.setText(name);

        progress.setText("当前状态：" + state);
        list = new ArrayList<>();
        textTitle.setText("学成进程");
        //初始化时间轴
        action = new LearnProAction("报名",
                "\u3000\u3000如果您还未报名，请您前往教练中心，预选心目中的好教练，完成缴费后即可报名成功",
                "未完成");
        action2 = new LearnProAction("完善信息",
                "\u3000\u3000您可以在缴费完成之后上传信息和照片，分别为身份证正反面各一张，上半身照片一张，五指健全照片一张，具体样式，请参考样式图。",
                "未完成");
        action3 = new LearnProAction("面签/体检",
                "\u3000\u3000预约面签/体检时请提前上传个人信息和照片",
                "未完成");
        action4 = new LearnProAction("科目一",
                "\u3000\u3000面签/体检完成之后就可以自主预约科目一考试了",
                "未完成");
        action5 = new LearnProAction("科目二",
                "\u3000\u3000在您科目一考试通过之后，您就可以进入科目二学习，学习完成后可以自主预约科目二的考试",
                "未完成");
        action6 = new LearnProAction("科目三",
                "\u3000\u3000在您科目二考试通过之后，您就可以进入科目三学习，学习完成后可以自主预约科目三的考试！",
                "未完成");
        action7 = new LearnProAction("科目四",
                "\u3000\u3000当您科目三通过之后您就可以自主预约科目四的考试。",
                "未完成");
        action8 = new LearnProAction("成功拿证",
                "\u3000\u3000在通过科目四考试之后3个工作日左右，我们将下发驾驶证",
                "未完成");
        addAction();
        list.add(action);
        list.add(action2);
        list.add(action3);
        list.add(action4);
        list.add(action5);
        list.add(action6);
        list.add(action7);
        list.add(action8);
        adapter = new MyLearnProAdapter(LearnProActivity.this, list);
        listView.setAdapter(adapter);
    }

    //更改时间轴的内容
    private void addAction() {
        if (state.equals("未报名")) {
            gotoComplete.setText(R.string.gotobaoming);
        } else if (state.equals("报名成功")) {
            action.setProisComplete("已完成");
            gotoComplete.setText(R.string.complete);
        } else if (state.startsWith("完善信息") && (state.contains("失败") || state.contains("未通过"))) {
            action.setProisComplete("已完成");
            action2.setProisComplete("审核失败");
            gotoComplete.setText("重新上传照片");
        } else if (state.startsWith("完善信息") && state.endsWith("审核中")) {
            action.setProisComplete("已完成");
            action2.setProisComplete("审核中");
            gotoComplete.setText("信息审核中");
        } else if (state.startsWith("完善信息") && (state.endsWith("通过") || state.contains("成功"))) {
            action.setProisComplete("已完成");
            gotoComplete.setText(R.string.mianqian);
            action2.setProisComplete("已完成");
        } else if ((state.contains("面签") || state.contains("体检")) && state.contains("审核中")) {
            action.setProisComplete("已完成");
            action2.setProisComplete("已完成");
            gotoComplete.setText("正在审核");
            action3.setProisComplete("审核中");
        } else if ((state.contains("面签") || state.contains("体检")) && (state.endsWith("通过") || state.contains("成功"))) {
            action.setProisComplete("已完成");
            action2.setProisComplete("已完成");
            gotoComplete.setText("申请科目一");
            action3.setProisComplete("已完成");
        } else if (state.startsWith("科目一") && (state.endsWith("未通过") || state.endsWith("失败") || state.contains("未成功"))) {
            action.setProisComplete("已完成");
            action2.setProisComplete("已完成");
            gotoComplete.setText(R.string.sub11);
            action3.setProisComplete("已完成");
            action4.setProisComplete("失败");
        } else if (state.startsWith("科目一") && state.endsWith("审核中")) {
            action.setProisComplete("已完成");
            action2.setProisComplete("已完成");
            gotoComplete.setText("科目一审核中");
            action3.setProisComplete("已完成");
            action4.setProisComplete("审核中");
        } else if (state.startsWith("科目一") && state.endsWith("审核通过")) {
            action.setProisComplete("已完成");
            action2.setProisComplete("已完成");
            gotoComplete.setText(R.string.sub1);
            action3.setProisComplete("已完成");
            action4.setProisComplete("已完成");
        } else if (state.startsWith("科目一") && (state.endsWith("通过") || state.contains("成功"))) {
            action.setProisComplete("已完成");
            action2.setProisComplete("已完成");
            gotoComplete.setText(R.string.subing2);
            action3.setProisComplete("已完成");
            action4.setProisComplete("已完成");
        } else if (state.contains("科目二") && (state.endsWith("失败") || state.endsWith("未通过"))) {
            action.setProisComplete("已完成");
            action2.setProisComplete("已完成");
            gotoComplete.setText(R.string.sub21);
            action3.setProisComplete("已完成");
            action4.setProisComplete("已完成");
            action5.setProisComplete("失败");
        } else if (state.contains("科目二") && (state.endsWith("学习中") || state.endsWith("进行中"))) {
            action.setProisComplete("已完成");
            action2.setProisComplete("已完成");
            action3.setProisComplete("已完成");
            gotoComplete.setText(R.string.sub2);
            action4.setProisComplete("已完成");
            action5.setProisComplete("进行中");
        } else if (state.contains("科目二") && (state.endsWith("准备考试"))) {
            action.setProisComplete("已完成");
            action2.setProisComplete("已完成");
            action3.setProisComplete("已完成");
            gotoComplete.setText("准备考试中");
            action4.setProisComplete("已完成");
            action5.setProisComplete("进行中");
        } else if (state.contains("科目二") && (state.endsWith("完成") || state.endsWith("通过"))) {
            action.setProisComplete("已完成");
            action2.setProisComplete("已完成");
            action3.setProisComplete("已完成");
            gotoComplete.setText(R.string.subing3);
            action4.setProisComplete("已完成");
            action5.setProisComplete("已完成");
        } else if (state.contains("科目三") && (state.endsWith("失败") || state.endsWith("未通过"))) {
            action.setProisComplete("已完成");
            action2.setProisComplete("已完成");
            action3.setProisComplete("已完成");
            action4.setProisComplete("已完成");
            gotoComplete.setText(R.string.sub31);
            action5.setProisComplete("已完成");
            action6.setProisComplete("失败");
        } else if (state.contains("科目三") && (state.endsWith("学习中") || state.endsWith("进行中"))) {
            action.setProisComplete("已完成");
            action2.setProisComplete("已完成");
            action3.setProisComplete("已完成");
            action4.setProisComplete("已完成");
            action5.setProisComplete("已完成");
            gotoComplete.setText(R.string.sub3);
            action6.setProisComplete("进行中");
        } else if (state.contains("科目三") && (state.endsWith("准备考试"))) {
            action.setProisComplete("已完成");
            action2.setProisComplete("已完成");
            action3.setProisComplete("已完成");
            action4.setProisComplete("已完成");
            action5.setProisComplete("已完成");
            gotoComplete.setText("准备考试中");
            action6.setProisComplete("进行中");
        } else if (state.contains("科目三") && (state.endsWith("完成") || state.endsWith("通过"))) {
            action.setProisComplete("已完成");
            action2.setProisComplete("已完成");
            action3.setProisComplete("已完成");
            action4.setProisComplete("已完成");
            gotoComplete.setText(R.string.subing4);
            action5.setProisComplete("已完成");
            action6.setProisComplete("已完成");
        } else if (state.contains("科目四") && (state.endsWith("失败") || state.endsWith("未通过"))) {
            action.setProisComplete("已完成");
            action2.setProisComplete("已完成");
            action3.setProisComplete("已完成");
            action4.setProisComplete("已完成");
            gotoComplete.setText("重新申请考试");
            action5.setProisComplete("已完成");
            action6.setProisComplete("失败");
        } else if (state.contains("科目四") && (state.endsWith("审核中") || state.endsWith("进行中"))) {
            action.setProisComplete("已完成");
            action2.setProisComplete("已完成");
            action3.setProisComplete("已完成");
            action4.setProisComplete("已完成");
            action5.setProisComplete("已完成");
            gotoComplete.setText(R.string.sub4);
            action6.setProisComplete("已完成");
            action7.setProisComplete("进行中");
        } else if (state.contains("科目四") && (state.endsWith("准备考试"))) {
            action.setProisComplete("已完成");
            action2.setProisComplete("已完成");
            action3.setProisComplete("已完成");
            action4.setProisComplete("已完成");
            action5.setProisComplete("已完成");
            gotoComplete.setText("准备考试中");
            action6.setProisComplete("已完成");
            action7.setProisComplete("进行中");
        } else if (state.contains("科目四") && (state.endsWith("通过") || state.endsWith("已完成"))) {
            action.setProisComplete("已完成");
            action2.setProisComplete("已完成");
            action3.setProisComplete("已完成");
            action4.setProisComplete("已完成");
            gotoComplete.setText(R.string.comTotal);
            action5.setProisComplete("已完成");
            action6.setProisComplete("已完成");
            action7.setProisComplete("已完成");
        } else if (state.equals("成功拿证")) {
            action.setProisComplete("已完成");
            action2.setProisComplete("已完成");
            action3.setProisComplete("已完成");
            action4.setProisComplete("已完成");
            action5.setProisComplete("已完成");
            action6.setProisComplete("已完成");
            gotoComplete.setText(R.string.comTotal);
            action7.setProisComplete("已完成");
            action8.setProisComplete("已完成");
            action8.setContext("百信学车预祝您驾驶愉快！");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_backward:
                finish();
                break;
            case R.id.gotoComplete:
                gotoCom();
                break;
            case R.id.dialog_cancel:
                dialog.dismiss();
                break;
        }
    }

    //创建dialog
    private void createSureDialog(final String str) {
        dialog = new Dialog(LearnProActivity.this, R.style.ActionSheetDialogStyle);
        // 填充对话框的布局
        inflate = LayoutInflater.from(LearnProActivity.this).inflate(
                R.layout.sure_cancel_dialog, null);
        // 初始化控件
        dialog_textView = (TextView) inflate.findViewById(R.id.dialog_textView);
        dialog_textView.setText("确定提交申请吗？");
        dialog_sure = (TextView) inflate.findViewById(R.id.dialog_sure);
        dialog_cancel = (TextView) inflate.findViewById(R.id.dialog_cancel);
        dialog_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (str.equals("申请面签体检")) {
                    refreshInfo(result.getUid() + "", mianqianUrl);
                } else if (str.equals("申请科目一")) {
                    refreshInfo(result.getUid() + "", sub1Url);
                } else if (str.equals("申请科目二")) {
                    refreshInfo(result.getUid() + "", sub2Url);
                } else if (str.equals("申请科目三")) {
                    refreshInfo(result.getUid() + "", sub2Url);
                } else if (str.equals("科目二正在进行中")) {
                    applykaoshi(result.getUid() + "", applyUrl, state);
                } else if (str.equals("科目三正在进行中")) {
                    applykaoshi(result.getUid() + "", applyUrl, state);
                } else if (str.equals("科目四正在进行中")) {
                    applykaoshi(result.getUid() + "", applyUrl, state);
                } else if (str.equals("申请科目四")) {
                    applykaoshi(result.getUid() + "", sub4Url, state);
                }
                //当每次完成后刷新当前页面
                refresh();
                dialog.dismiss();
            }
        });
        dialog_cancel.setOnClickListener(this);
        // 将布局设置给Dialog
        dialog.setContentView(inflate);
        // 获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        // 设置dialog宽度
        dialogWindow.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置Dialog从窗体中间弹出
        dialogWindow.setGravity(Gravity.CENTER);
        dialog.show();
    }

    //点击事件
    private void gotoCom() {
        Intent intent = new Intent();
        if (gotoComplete.getText().toString().equals("前去报名")) {
            intent.setClass(LearnProActivity.this, HomeActivity.class);
            intent.putExtra("FromActivity", "SimpleCoachActivity");
            startActivity(intent);
            finish();
        } else if (gotoComplete.getText().toString().equals("前去完善信息")) {
            intent.setClass(LearnProActivity.this, RegisterDetailActivity2.class);
            startActivity(intent);
            finish();
        } else if (gotoComplete.getText().toString().equals("重新上传照片")) {
            intent.setClass(LearnProActivity.this, RegisterDetailActivity2.class);
            startActivity(intent);
            finish();
        } else if (gotoComplete.getText().toString().equals("申请面签体检")) {
            createSureDialog("申请面签体检");
        } else if (gotoComplete.getText().toString().equals("申请科目一")) {
            createSureDialog("申请科目一");
        } else if (gotoComplete.getText().toString().equals("科目一未通过，请缴补考费")) {
            intent.setClass(LearnProActivity.this, BuKaoActivity.class);
            intent.putExtra("uid", result.getUid());
            startActivity(intent);
        } else if (gotoComplete.getText().toString().equals("申请科目二")) {
            createSureDialog("申请科目二");
        } else if (gotoComplete.getText().toString().equals("预约科目二考试")) {
            createSureDialog("科目二正在进行中");
        } else if (gotoComplete.getText().toString().equals("科目二未通过，请缴补考费")) {
            intent.setClass(LearnProActivity.this, BuKaoActivity.class);
            intent.putExtra("uid", result.getUid());
            startActivity(intent);
        } else if (gotoComplete.getText().toString().equals("申请科目三")) {
            createSureDialog("申请科目三");
        } else if (gotoComplete.getText().toString().equals("预约科目三考试")) {
            createSureDialog("科目三正在进行中");
        } else if (gotoComplete.getText().toString().equals("科目三未通过，请缴补考费")) {
            intent.setClass(LearnProActivity.this, BuKaoActivity.class);
            intent.putExtra("uid", result.getUid());
            startActivity(intent);
        } else if (gotoComplete.getText().toString().equals("申请科目四")) {
            createSureDialog("申请科目四");
        } else if (gotoComplete.getText().toString().equals("预约科目四考试") ||
                gotoComplete.getText().toString().equals("重新申请考试")) {
            createSureDialog("科目四正在进行中");
        } else if (gotoComplete.getText().toString().equals("准备考试中")) {
            Toast.makeText(LearnProActivity.this, "考试申请已提交，请勿重复点击", Toast.LENGTH_SHORT).show();
        } else if (gotoComplete.getText().toString().equals("信息审核中")) {
            Toast.makeText(LearnProActivity.this, "信息已提交，请勿耐心等待", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onRestart() {
        refreshInfo(result.getUid() + "", refreashUrl);
        super.onRestart();
    }

    private void applykaoshi(String uid, String refreashUrl, String state) {
        OkHttpUtils
                .post()
                .url(refreashUrl)
                .addParams("uid", uid)
                .addParams("token", token)
                .addParams("state", state)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        Toast.makeText(LearnProActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        Gson gson = new Gson();
                        UserInfo userInfo = gson.fromJson(s, UserInfo.class);
                        if (userInfo.getCode() == 200) {
                            gotoComplete.setText("准备考试中");
                        }
                        Toast.makeText(LearnProActivity.this, userInfo.getReason(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void refreshInfo(String uid, String refreashUrl) {
        OkHttpUtils
                .post()
                .url(refreashUrl)
                .addParams("uid", uid)
                .addParams("token", token)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        Toast.makeText(LearnProActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        Gson gson = new Gson();
                        UserInfo userInfo = gson.fromJson(s, UserInfo.class);
                        Toast.makeText(LearnProActivity.this, userInfo.getReason(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void refresh() {
        OkHttpUtils
                .post()
                .url(refreashUrl)
                .addParams("uid", result.getUid() + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        Toast.makeText(LearnProActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        Gson gson = new Gson();
                        UserInfo userInfo = gson.fromJson(s, UserInfo.class);
                        if (userInfo.getCode() == 200) {
                            /**
                             * 保存登录状态
                             */
                            SharedPreferences sp = getSharedPreferences("USER", Activity.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.clear();
                            editor.putInt("isfirst", 1);
                            editor.putString("userInfo", s);
                            editor.commit();
                            getData();
                            init();
                        }
                    }
                });
    }
}
