package com.jgkj.bxxc;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jgkj.bxxc.adapter.LearnHisAdapter;
import com.jgkj.bxxc.bean.LearnHisAction;
import com.jgkj.bxxc.bean.UserInfo;
import com.jgkj.bxxc.tools.RefreshLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by fangzhou on 2016/12/29.
 * 学车记录及评价
 */

public class LearnHisActivity extends Activity implements View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener {
    private ListView listView;
    private LearnHisAdapter adapter;
    private RefreshLayout swipehis;
    private TextView title;

    private Dialog sureDialog;
    private ProgressDialog dialog;
    private View sureView;
    private TextView dialog_textView, dialog_sure, dialog_cancel;
    private String str;
    private String sureBtn, cancelBtn;
    private String token;

    private RadioButton radio_btn01, radio_btn02;
    private Button back_btn, baoming;
    private List<LearnHisAction.Result> list = new ArrayList();
    private String learnUrl = "http://www.baixinxueche.com/index.php/Home/Apitokenupdata/commentShowApplyTest";
    private String notComeUrl = "http://www.baixinxueche.com/index.php/Home/Apitokenupdata/commentNotTo";
    private String applyTest = "http://www.baixinxueche.com/index.php/Home/Apitokenupdata/applySubjectTestAgain";

    private int page = 1;
    private TextView tips;
    private String state;

    //flag代表是否是已学车还是未学车
    private boolean flag = true;

    private int uid;
    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.learnhis);
        initView();
        Intent intent = getIntent();
        uid = intent.getIntExtra("uid", -1);
        state = intent.getStringExtra("state");
        token = intent.getStringExtra("token");
        getHis(page + "");
        isShowBtn();
    }

    //初始化控件
    private void initView() {
        listView = (ListView) findViewById(R.id.listView);
        swipehis = (RefreshLayout) findViewById(R.id.swipehis);
        title = (TextView) findViewById(R.id.text_title);
        title.setText("学车记录");
        back_btn = (Button) findViewById(R.id.button_backward);
        back_btn.setVisibility(View.VISIBLE);
        back_btn.setOnClickListener(this);
        //上拉刷新
        swipehis.setColorSchemeResources(R.color.color_bule2, R.color.color_bule, R.color.color_bule2, R.color.color_bule3);
        swipehis.setOnRefreshListener(this);
        swipehis.setTag("refresh");
        swipehis.setOnLoadListener(this);
        radio_btn01 = (RadioButton) findViewById(R.id.radio_btn01);
        radio_btn02 = (RadioButton) findViewById(R.id.radio_btn02);
        radio_btn01.setOnClickListener(this);
        radio_btn02.setOnClickListener(this);
        baoming = (Button) findViewById(R.id.baoming);
        baoming.setOnClickListener(this);
        tips = (TextView) findViewById(R.id.tips);
    }

    /**
     * 获取学车已学记录
     *
     * @param page 分页
     */
    private void getHis(String page) {
        OkHttpUtils
                .post()
                .url(learnUrl)
                .addParams("uid", uid + "")
                .addParams("page", page)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        Toast.makeText(LearnHisActivity.this, "发送失败", Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onResponse(String s, int i) {
                        listView.setTag(s);
                        if (listView.getTag() != null) {
                            setAdapter();
                        }
                    }
                });
    }

    /**
     * 给控件填充参数
     * 已学课时
     */
    private void setAdapter() {
        String str = listView.getTag().toString();
        Gson gson = new Gson();
        LearnHisAction action = gson.fromJson(str, LearnHisAction.class);
        switch (swipehis.getTag().toString()) {
            case "refresh":
                if (action.getCode() == 200) {
                    list.clear();
                    list.addAll(action.getResult());
                    adapter = new LearnHisAdapter(LearnHisActivity.this, list,1,token,uid);
                    listView.setAdapter(adapter);
                } else {
                    Toast.makeText(LearnHisActivity.this, action.getReason(), Toast.LENGTH_SHORT).show();
                }
                break;
            case "onload":
                if (action.getCode() == 200) {
                    list.addAll(action.getResult());
                    adapter.notifyDataSetChanged();
                } else {
                    page--;
                    Toast.makeText(LearnHisActivity.this, action.getReason(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void isShowBtn() {
        if ((state.startsWith("科目二") && state.endsWith("进行中")) ||
                state.startsWith("科目三") && state.endsWith("进行中") ||
                state.startsWith("科目四") && state.endsWith("进行中") ||
                state.startsWith("科目四") && state.endsWith("未通过")) {
            baoming.setEnabled(true);
            baoming.setBackgroundColor(getResources().getColor(R.color.themeColor));
        }
    }

    /**
     * 获取学车未学记录
     *
     * @param page 分页
     */
    private void getNotHis(String page) {
        OkHttpUtils
                .post()
                .url(notComeUrl)
                .addParams("uid", uid + "")
                .addParams("page", page)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        Toast.makeText(LearnHisActivity.this, "发送失败", Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onResponse(String s, int i) {
                        listView.setTag(s);
                        if (listView.getTag() != null) {
                            setNotAdapter();
                        }
                    }
                });
    }

    /**
     * 是否修改教练
     */
    public void createSureDialog() {
        sureDialog = new Dialog(LearnHisActivity.this, R.style.ActionSheetDialogStyle);
        // 填充对话框的布局
        sureView = LayoutInflater.from(LearnHisActivity.this).inflate(
                R.layout.sure_cancel_dialog, null);
        // 初始化控件
        dialog_textView = (TextView) sureView.findViewById(R.id.dialog_textView);
        dialog_textView.setText("确定申请考试吗？");
        dialog_sure = (TextView) sureView.findViewById(R.id.dialog_sure);
        dialog_cancel = (TextView) sureView.findViewById(R.id.dialog_cancel);

        dialog_sure.setText("确定");
        dialog_cancel.setText("取消");

        dialog_sure.setOnClickListener(this);
        dialog_cancel.setOnClickListener(this);
        // 将布局设置给Dialog
        sureDialog.setContentView(sureView);
        // 获取当前Activity所在的窗体
        Window dialogWindow = sureDialog.getWindow();
        dialogWindow.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置Dialog从窗体中间弹出
        dialogWindow.setGravity(Gravity.CENTER);
        sureDialog.show();
    }

    /**
     * 给控件填充参数
     * 未学课时
     */
    private void setNotAdapter() {
        String str = listView.getTag().toString();
        Gson gson = new Gson();
        LearnHisAction action = gson.fromJson(str, LearnHisAction.class);
        switch (swipehis.getTag().toString()) {
            case "refresh":
                if (action.getCode() == 200) {
                    list.clear();
                    list.addAll(action.getResult());
                    adapter = new LearnHisAdapter(LearnHisActivity.this, list,0,token,uid);
                    listView.setAdapter(adapter);
                } else {
                    Toast.makeText(LearnHisActivity.this, action.getReason(), Toast.LENGTH_SHORT).show();
                }
                break;
            case "onload":
                if (action.getCode() == 200) {
                    list.addAll(action.getResult());
                    adapter.notifyDataSetChanged();
                } else {
                    page--;
                    Toast.makeText(LearnHisActivity.this, action.getReason(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_backward:
                finish();
                break;
            case R.id.radio_btn01:
                page = 1;
                list.clear();
                flag = true;
                adapter = new LearnHisAdapter(LearnHisActivity.this, list,1,token,uid);
                listView.setAdapter(adapter);
                tips.setText("这里显示您已约时间段，并且已确定学车！");
                radio_btn01.setBackgroundResource(R.drawable.bg_selector);
                radio_btn01.setTextColor(getResources().getColor(R.color.redTheme));
                radio_btn02.setBackgroundResource(R.color.white);
                radio_btn02.setTextColor(getResources().getColor(R.color.right_bg));
                getHis(page + "");
                break;
            case R.id.radio_btn02:
                page = 1;
                list.clear();
                flag = false;
                adapter = new LearnHisAdapter(LearnHisActivity.this, list,0,token,uid);
                listView.setAdapter(adapter);
                tips.setText("这里显示您已约时间段，但是并未到场学车！");
                radio_btn02.setBackgroundResource(R.drawable.bg_selector);
                radio_btn02.setTextColor(getResources().getColor(R.color.redTheme));
                radio_btn01.setBackgroundResource(R.color.white);
                radio_btn01.setTextColor(getResources().getColor(R.color.right_bg));
                getNotHis(page + "");
                break;
            case R.id.baoming:
                if (baoming.isEnabled()) {
                    createSureDialog();
                } else {
                    Toast.makeText(LearnHisActivity.this, "您暂时不能报名哦！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.dialog_sure:
                dialog = ProgressDialog.show(LearnHisActivity.this, null,"申请中...");
                applykaoshi(uid + "");
                sureDialog.dismiss();
                break;
            case R.id.dialog_cancel:
                sureDialog.dismiss();
                break;
        }
    }

    private void applykaoshi(String uid) {
        OkHttpUtils
                .post()
                .url(applyTest)
                .addParams("uid", uid)
                .addParams("state", state)
                .addParams("token", token)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        if(dialog.isShowing()){
                            dialog.dismiss();
                        }
                        Toast.makeText(LearnHisActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onResponse(String s, int i) {
                        if(dialog.isShowing()){
                            dialog.dismiss();
                        }
                        Gson gson = new Gson();
                        UserInfo userInfo = gson.fromJson(s, UserInfo.class);
                        if (userInfo.getCode() == 200) {
                            baoming.setEnabled(false);
                            baoming.setBackgroundColor(getResources().getColor(R.color.gray));
                        }
                        Toast.makeText(LearnHisActivity.this, userInfo.getReason(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onResume() {
        page = 1;
        list.clear();
        swipehis.setTag("refresh");
        if (flag) {
            getHis(page + "");
        } else {
            getNotHis(uid + "");
        }
        swipehis.setRefreshing(false);
        super.onResume();
    }

    //下拉刷新
    @Override
    public void onRefresh() {
        swipehis.postDelayed(new Runnable() {
            @Override
            public void run() {
                page = 1;
                list.clear();
                swipehis.setTag("refresh");
                if (flag) {
                    getHis(page + "");
                } else {
                    getNotHis(uid + "");
                }
                swipehis.setRefreshing(false);
            }
        }, 2000);
    }

    //上拉加载
    @Override
    public void onLoad() {
        swipehis.postDelayed(new Runnable() {
            @Override
            public void run() {
                page++;
                swipehis.setTag("onload");
                if (flag) {
                    getHis(page + "");
                } else {
                    getNotHis(uid + "");
                }
                swipehis.setLoading(false);
            }
        }, 2000);
    }

}
