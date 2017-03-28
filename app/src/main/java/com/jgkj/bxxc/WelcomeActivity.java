package com.jgkj.bxxc;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jgkj.bxxc.bean.Advertising;
import com.jgkj.bxxc.tools.PictureOptimization;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import cn.jpush.android.api.InstrumentedActivity;
import okhttp3.Call;


/**
 * Created by fangzhou on 2016/10/5.
 * <p>
 * 启动页，所有接口都没有进行加密
 */
public class WelcomeActivity extends InstrumentedActivity implements View.OnClickListener {
    private ImageView welcome_imageview;

    private PictureOptimization po;

    private Button skip;
    private Advertising ad;

    private Runnable delayRunable;
    private Handler handler = new Handler();
    //广告页，即广告图片的url获取
    private String adUrl = "http://www.baixinxueche.com/index.php/Home/Apitoken/nowStar";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        init();
    }


    //初始化控件，并请求广告页地址
    private void init() {
        skip = (Button) findViewById(R.id.skip);
        skip.getBackground().setAlpha(100);
        welcome_imageview = (ImageView) findViewById(R.id.welcome_imageview);
        po = new PictureOptimization();
        getAD();
    }

    private void isOpenAD() {
        String str = skip.getTag().toString();
        Gson gson = new Gson();
        ad = gson.fromJson(str, Advertising.class);
        if (ad.getCode() == 200) {
            //延迟跳转
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    skip.setVisibility(View.VISIBLE);
                    skip.setOnClickListener(WelcomeActivity.this);
                    welcome_imageview.setOnClickListener(WelcomeActivity.this);
                    Glide.with(WelcomeActivity.this).load(ad.getResult().getContent()).into(welcome_imageview);
                    handler.postDelayed(
                            delayRunable = new Runnable() {
                                public void run() {
                                    Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
                                    intent.putExtra("FromActivity", "WelcomeActivity");
                                    startActivity(intent);
                                    finish();
                                }
                            }, Long.parseLong(ad.getResult().getDuration() + "000")); //延迟3秒跳转
                }
            }, 3000); //延迟3秒跳转
        } else {
            Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
            intent.putExtra("FromActivity", "WelcomeActivity");
            startActivity(intent);
            finish();
        }
    }

    private void getAD() {
        OkHttpUtils
                .post()
                .url(adUrl)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
                                intent.putExtra("FromActivity", "WelcomeActivity");
                                startActivity(intent);
                                finish();
                            }
                        }, 3000); //延迟3秒跳转
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        skip.setTag(s);
                        if (skip.getTag() != null) {
                            isOpenAD();
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.skip:
                //即使是finish()，已启用的线程也会走，不会跟着Activity销毁而销毁因此我们需要利用
                //handler.removerCallbacks(Runable runable),方法取消指定已启用的线程
                handler.removeCallbacks(delayRunable);

                intent.setClass(WelcomeActivity.this, HomeActivity.class);
                intent.putExtra("FromActivity", "WelcomeActivity");
                startActivity(intent);
                finish();
                break;
            case R.id.welcome_imageview:
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(ad.getResult().getOpenUrl());
                intent.setData(content_url);
                startActivity(intent);
                finish();
                break;
        }
    }

}

