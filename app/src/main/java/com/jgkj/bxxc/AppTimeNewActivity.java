package com.jgkj.bxxc;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jgkj.bxxc.adapter.StuSubNewAdapter;
import com.jgkj.bxxc.bean.CreateDay_Time;
import com.jgkj.bxxc.bean.MyCoachAction;
import com.jgkj.bxxc.tools.RefreshLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by fangzhou on 2017/1/18.
 * <p>
 * 预约时间
 */

public class AppTimeNewActivity extends Activity implements View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener {
    private TextView title;
    private Button back;
    private ListView listView;

    private RefreshLayout swipeLayout;
    private String uid;
    private String cid;
    private String token,state;
    private MyCoachAction coachAction;

    private StuSubNewAdapter adapter;
    private CreateDay_Time createday;
    private List<CreateDay_Time> list;
    private ProgressDialog dialog;
    private TextView textView;

    private String myCoachUrl = "http://www.baixinxueche.com/index.php/Home/Apiupdatasecond/remyCoachAgain";
    private String mynewCoachUrl = "http://www.baixinxueche.com/index.php/Home/Apiapplytoken/remyCoachAgainFive";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apptime);
        initView();
        getMyCoach(uid);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        dialog = ProgressDialog.show(this, null, "加载中...");
        title = (TextView) findViewById(R.id.text_title);
        title.setText("预约课时");
        back = (Button) findViewById(R.id.button_backward);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.listView);
        textView = (TextView) findViewById(R.id.textView);

        //下拉刷新
        swipeLayout = (RefreshLayout) findViewById(R.id.refresh);
        swipeLayout.setColorSchemeResources(R.color.color_bule2, R.color.color_bule, R.color.color_bule2, R.color.color_bule3);
        swipeLayout.setOnRefreshListener(this);
        Intent intent = getIntent();
        uid = intent.getIntExtra("uid", -1) + "";
        cid = intent.getStringExtra("cid");
        token = intent.getStringExtra("token");
        state = intent.getStringExtra("state");
        if(!state.equals("科目二正在进行中")&&!state.equals("科目三正在进行中")){
            textView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            textView.setText("您的状态暂时不可预约学车");
        }
    }

    /**
     * 获取数据并且设置值
     */
    private void getListData() {
        String str = listView.getTag().toString();
        Gson gson = new Gson();
        coachAction = gson.fromJson(str, MyCoachAction.class);
        list = new ArrayList<>();
        List<String> strList = new ArrayList<>();
        if (coachAction.getCode() == 200) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            List<MyCoachAction.Result.Res1> res1 = coachAction.getResult().getSubject();
            for (int i = 0; i < res1.size(); i++) {
                List<String> time = null;
                List<Boolean> isApp = null;
                List<Integer> count = null;
                if (!strList.contains(res1.get(i).getTimeone())) {
                    time = new ArrayList<>();
                    isApp = new ArrayList<>();
                    count = new ArrayList<>();
                    time.add(res1.get(i).getTime_slot());
                    isApp.add(false);
                    count.add(res1.get(i).getCount());
                    createday = new CreateDay_Time(res1.get(i).getTimeone(), time,
                            isApp, count);
                    strList.add(res1.get(i).getTimeone());
                    list.add(createday);
                } else {
                    int index = strList.indexOf(res1.get(i).getTimeone());
                    list.get(index).getTime().add(res1.get(i).getTime_slot());
                    list.get(index).getIsApp().add(false);
                    list.get(index).getCount().add(res1.get(i).getCount());
                }
            }
        } else {
            Toast.makeText(AppTimeNewActivity.this, coachAction.getReason(), Toast.LENGTH_SHORT).show();
        }

        List<MyCoachAction.Result.Res2> res2 = coachAction.getResult().getStusubject();

        for (int i = 0; i < list.size(); i++) {
            for (int k = 0; k < res2.size(); k++) {
                if (res2.get(k).getDay().equals(list.get(i).getDay())) {
                    int size = list.get(i).getIsApp().size();
                    for (int j = 0; j < size; j++) {
                        if (res2.get(k).getTime_slot().equals(list.get(i).getTime().get(j))) {
                            list.get(i).getIsApp().set(j, true);
                        }
                    }
                }
            }
        }
        if (list.isEmpty()) {
            textView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        } else {
            adapter = new StuSubNewAdapter(AppTimeNewActivity.this, list, cid, uid,token);
            listView.setAdapter(adapter);
        }
    }

    /**
     * 获取教练信息和排课时间表，并且还有我的预约时间表
     *
     * @param uid 用户id
     */
    private void getMyCoach(String uid) {
        OkHttpUtils
                .post()
                .url(mynewCoachUrl)
                .addParams("uid", uid)
                .addParams("token", token)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        Toast.makeText(AppTimeNewActivity.this, "请检查网络", Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onResponse(String s, int i) {
                        listView.setTag(s);
                        if (listView.getTag() != null) {
                            getListData();
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sp = getSharedPreferences("getSubjectSet", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isChange", false);
        editor.commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_backward:
                finish();
                break;
        }
    }

    @Override
    public void onRefresh() {
        swipeLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                textView.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                getMyCoach(uid);
                swipeLayout.setRefreshing(false);
            }
        }, 2000);
    }
}
