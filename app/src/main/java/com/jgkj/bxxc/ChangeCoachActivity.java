package com.jgkj.bxxc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jgkj.bxxc.adapter.MyCoachAdapter;
import com.jgkj.bxxc.bean.CoachDetailAction;
import com.jgkj.bxxc.tools.RefreshLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by fangzhou on 2016/12/28.
 * 更改教练，展示可选教练
 */

public class ChangeCoachActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener ,
        SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener{
    private Button back;
    private TextView title;
    private ListView listView;
    private MyCoachAdapter adapter;
    private int zhuangtai;
    private int uid;
    private int page = 1;
    private String token;
    private RefreshLayout swipeCoach;
    private List<CoachDetailAction.Result> list;
    private String OldcoachShowUrl = "http://www.baixinxueche.com/index.php/Home/Apiupdata/coachShow";
    private String coachShowUrl = "http://www.baixinxueche.com/index.php/Home/Apiupdatasecond/coachSure";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changecoach);
        initView();
        getNewCoach(page+"");
    }

    private void getNewCoach(String page) {
        OkHttpUtils
                .post()
                .url(coachShowUrl)
                .addParams("page", page)
                .addParams("zhuangtai", zhuangtai+"")
                .addParams("uid", uid+"")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        Toast.makeText(ChangeCoachActivity.this, "网络状态不佳,请检查网络", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        listView.setTag(s);
                        if (listView.getTag().toString() != null) {
                            setAdapter();
                        } else {
                            Toast.makeText(ChangeCoachActivity.this, "网络状态不佳,请检查网络", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void setAdapter() {
        String jsonStr = listView.getTag().toString();
        Gson gson = new Gson();
        CoachDetailAction coachDetailAction = gson.fromJson(jsonStr, CoachDetailAction.class);
        switch (swipeCoach.getTag().toString()){
            case "refresh":
                if (coachDetailAction.getCode() == 200) {
                    list.addAll(coachDetailAction.getResult());
                    adapter = new MyCoachAdapter(ChangeCoachActivity.this,list);
                    listView.setAdapter(adapter);
                }else{
                    Toast.makeText(ChangeCoachActivity.this,coachDetailAction.getReason(), Toast.LENGTH_SHORT).show();
                }
                break;
            case "onload":
                if (coachDetailAction.getCode() == 200) {
                    list.addAll(coachDetailAction.getResult());
                    adapter.notifyDataSetChanged();
                }else{
                    page--;
                    Toast.makeText(ChangeCoachActivity.this,coachDetailAction.getReason(), Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    private void initView() {
        list= new ArrayList<>();

        back = (Button) findViewById(R.id.button_backward);
        back.setVisibility(View.VISIBLE);
        title = (TextView) findViewById(R.id.text_title);
        title.setText("更改教练");
        back.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        swipeCoach = (RefreshLayout) findViewById(R.id.swipeCoach);
        //上拉刷新
        swipeCoach.setColorSchemeResources(R.color.color_bule2, R.color.color_bule, R.color.color_bule2, R.color.color_bule3);
        swipeCoach.setTag("refresh");
        swipeCoach.setOnRefreshListener(this);
        swipeCoach.setOnLoadListener(this);
        Intent intent = getIntent();
        zhuangtai = intent.getIntExtra("zhuangtai",0);
        uid = intent.getIntExtra("uid",-1);
        token = intent.getStringExtra("token");
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
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        TextView coachId = (TextView) view.findViewById(R.id.coachId);
        Intent intent = new Intent();
        intent.setClass(ChangeCoachActivity.this, ReservationActivity.class);
        intent.putExtra("coachId", coachId.getText().toString().trim());
        intent.putExtra("isChange", 0);
        intent.putExtra("zhuangtai", zhuangtai);
        intent.putExtra("token",token);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRefresh() {
        swipeCoach.postDelayed(new Runnable() {
            @Override
            public void run() {
                page = 1;
                list.clear();
                swipeCoach.setTag("refresh");
                getNewCoach(page + "");
                swipeCoach.setRefreshing(false);
            }
        }, 2000);
    }

    @Override
    public void onLoad() {
        swipeCoach.postDelayed(new Runnable() {
            @Override
            public void run() {
                page++;
                swipeCoach.setTag("onload");
                getNewCoach(page + "");
                swipeCoach.setLoading(false);
                adapter.notifyDataSetChanged();
            }
        }, 2000);
    }
}
