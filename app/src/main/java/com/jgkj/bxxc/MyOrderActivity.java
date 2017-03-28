package com.jgkj.bxxc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jgkj.bxxc.adapter.MyBukaoOrderAdapter;
import com.jgkj.bxxc.adapter.MyOrderAdapter;
import com.jgkj.bxxc.bean.BuKaoOrder;
import com.jgkj.bxxc.bean.MyOrder;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by fangzhou on 2016/11/22.
 * 我的订单,报名考试订单和补考费订单
 */
public class MyOrderActivity extends Activity implements View.OnClickListener {
    private Button back_forward;
    private TextView title;
    private MyOrderAdapter adapter;
    private ListView listView;
    private TextView textView;
    private BuKaoOrder order;
    private String token;

    //报名费订单
    private String url = "http://www.baixinxueche.com/index.php/Home/Apialltoken/order";
    //补考订单
    private String buKaoUrl = "http://www.baixinxueche.com/index.php/Home/Apitokenupdata/reTestOrder";

    private RadioButton radio_btn01,radio_btn02;
    private int uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myorder);
        init();
        Intent intent = getIntent();
        uid = intent.getIntExtra("uid",-1);
        token = intent.getStringExtra("token");
        if(uid!=-1){
            getOrder(uid+"");
        }else{
            textView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            textView.setText("暂无订单信息");
        }
    }

    //初始化布局
    private void init() {
        back_forward = (Button) findViewById(R.id.button_backward);
        title = (TextView) findViewById(R.id.text_title);
        back_forward.setVisibility(View.VISIBLE);
        back_forward.setOnClickListener(this);
        title.setText("我的订单");

        radio_btn01 = (RadioButton) findViewById(R.id.radio_btn01);
        radio_btn02 = (RadioButton) findViewById(R.id.radio_btn02);
        radio_btn01.setOnClickListener(this);
        radio_btn02.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.listView);
        textView = (TextView) findViewById(R.id.textView);
    }

    private void getOrder(String uid) {
        OkHttpUtils
                .post()
                .url(url)
                .addParams("uid", uid)
                .addParams("token", token)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        Toast.makeText(MyOrderActivity.this, "请检查网络", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        Gson gson = new Gson();
                        MyOrder myOrder = gson.fromJson(s, MyOrder.class);
                        List<MyOrder.Result> list = new ArrayList<MyOrder.Result>();
                        if (myOrder.getCode() == 200) {
                            List<MyOrder.Result> results = myOrder.getResult();
                            list.addAll(results);
                        }
                        if (myOrder.getNocode() == 200) {
                            List<MyOrder.Result> results = myOrder.getNoresult();
                            list.addAll(results);
                        }
                        if(list.size()==0){
                            textView.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.GONE);
                            textView.setText("暂无订单信息");
                        }else {
                            Intent intent = getIntent();
                            int uId = intent.getIntExtra("uid",-1);
                            MyOrderAdapter adapter = new MyOrderAdapter(uId+"",MyOrderActivity.this, list);
                            listView.setAdapter(adapter);
                        }
                    }
                });
    }
    private void getBukaoOrder(String uid) {
        OkHttpUtils
                .post()
                .url(buKaoUrl)
                .addParams("uid", uid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        Toast.makeText(MyOrderActivity.this, "请检查网络", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        Gson gson = new Gson();
                        BuKaoOrder buKaoOrder = gson.fromJson(s, BuKaoOrder.class);
                        List<BuKaoOrder.Result> list = new ArrayList<BuKaoOrder.Result>();
                        if (buKaoOrder.getCode() == 200) {
                            List<BuKaoOrder.Result> results = buKaoOrder.getResult();
                            list.addAll(results);
                        }
                        if (buKaoOrder.getNocode() == 200) {
                            List<BuKaoOrder.Result> results = buKaoOrder.getNoresult();
                            list.addAll(results);
                        }
                        if(list.size()==0){
                            textView.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.GONE);
                            textView.setText("暂无订单信息");
                        }else {
                            Intent intent = getIntent();
                            int uId = intent.getIntExtra("uid",-1);
                            MyBukaoOrderAdapter adapter = new MyBukaoOrderAdapter(uId+"",
                                    MyOrderActivity.this, list,token);
                            listView.setAdapter(adapter);
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_backward:
                finish();
                break;
            case R.id.radio_btn01:
                textView.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                radio_btn01.setBackgroundResource(R.drawable.bg_selector);
                radio_btn01.setTextColor(getResources().getColor(R.color.redTheme));
                radio_btn02.setBackgroundResource(R.color.white);
                radio_btn02.setTextColor(getResources().getColor(R.color.right_bg));
                getOrder(uid+"");
                break;
            case R.id.radio_btn02:
                textView.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                radio_btn02.setBackgroundResource(R.drawable.bg_selector);
                radio_btn02.setTextColor(getResources().getColor(R.color.redTheme));
                radio_btn01.setBackgroundResource(R.color.white);
                radio_btn01.setTextColor(getResources().getColor(R.color.right_bg));
                getBukaoOrder(uid+"");
                break;

        }
    }
}
