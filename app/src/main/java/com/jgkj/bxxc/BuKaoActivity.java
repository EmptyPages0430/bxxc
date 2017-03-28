package com.jgkj.bxxc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jgkj.bxxc.bean.MyPayResult;
import com.jgkj.bxxc.bean.ShowRePay;
import com.jgkj.bxxc.tools.PayResult;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by fangzhou on 2017/1/5.
 * 补考展示页面
 */

public class BuKaoActivity extends Activity implements View.OnClickListener {
    private TextView title, name, classType, car, phone, ordername, orderfee,coach_Price;
    private Button back;
    private ImageView img, weixin_isCheck, aipay_isCheck;
    private LinearLayout weixin_layout, aipay_layout, fuwutiaokuan;
    private RelativeLayout layout;

    private ShowRePay showRePay;
    //服务条款
    private ImageView isCheck;
    private boolean aipayflag = false, weixinFlag = false, serFlag = false;
    private int uid;
    private Button payInfo;

    private String payUrl = "http://www.baixinxueche.com/index.php/Home/Aliappretext/retestPay";
    private String orderUrl = "http://www.baixinxueche.com/index.php/Home/Apiupdata/retest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bukao);
        initView();
        getintent();
    }
    private void getintent(){
        Intent intent = getIntent();
        uid = intent.getIntExtra("uid",-1);
        getData(uid+"",orderUrl);
    }

    private void initView() {
        weixin_isCheck = (ImageView) findViewById(R.id.weixin_isCheck);
        weixin_layout = (LinearLayout) findViewById(R.id.weixin_layout);
        weixin_layout.setOnClickListener(this);
        aipay_layout = (LinearLayout) findViewById(R.id.aipay_layout);
        aipay_isCheck = (ImageView) findViewById(R.id.aipay_isCheck);
        aipay_layout.setOnClickListener(this);
        //服务条款
        fuwutiaokuan = (LinearLayout) findViewById(R.id.fuwutiaokuan);
        fuwutiaokuan.setOnClickListener(this);
        payInfo = (Button) findViewById(R.id.payInfo);
        payInfo.setOnClickListener(this);
        coach_Price = (TextView) findViewById(R.id.coach_Price);

        isCheck = (ImageView) findViewById(R.id.isCheck);
        title = (TextView) findViewById(R.id.text_title);
        title.setText("补考详情");
        back = (Button) findViewById(R.id.button_backward);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(this);
        name = (TextView) findViewById(R.id.name);
        classType = (TextView) findViewById(R.id.classType);
        car = (TextView) findViewById(R.id.car);
        phone = (TextView) findViewById(R.id.phone);
        ordername = (TextView) findViewById(R.id.orderName);
        orderfee = (TextView) findViewById(R.id.orderfee);
        img = (ImageView) findViewById(R.id.headImg);
        layout = (RelativeLayout) findViewById(R.id.layout);

    }
    private void isClick(){
        if((aipayflag||weixinFlag)&&serFlag){
            payInfo.setClickable(true);
            payInfo.setBackgroundColor(getResources().getColor(R.color.themeColor));
        }else{
            payInfo.setBackgroundColor(getResources().getColor(R.color.right_bg));
            payInfo.setClickable(false);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.payInfo:
                isClick();
                if (aipayflag == false && weixinFlag == false) {
                    Toast.makeText(BuKaoActivity.this, "请首先选择支付方式", Toast.LENGTH_SHORT).show();
                } else {
                    if (weixinFlag) {
                        Toast.makeText(BuKaoActivity.this, "暂未开通微信支付哦！", Toast.LENGTH_SHORT).show();
                    } else {
                        sendaiPay(uid+"",showRePay.getResult().getBaixin_state()+"",showRePay.getResult().getRefee()+"");
                    }
                }
                break;
            case R.id.button_backward:
                finish();
                break;
            case R.id.fuwutiaokuan:
                if (!serFlag) {
                    isCheck.setImageResource(R.drawable.right);
                    serFlag = true;
                    isClick();
                } else {
                    isCheck.setImageResource(R.drawable.check_background);
                    serFlag = false;
                    isClick();
                }
                break;
            case R.id.weixin_layout:
                if (aipayflag) {
                    aipay_isCheck.setImageResource(R.drawable.check_background);
                    aipayflag = false;
                    weixin_isCheck.setImageResource(R.drawable.right);
                    weixinFlag = true;
                    isClick();
                } else {
                    if (!weixinFlag) {
                        weixin_isCheck.setImageResource(R.drawable.right);
                        weixinFlag = true;
                        isClick();
                    } else {
                        weixin_isCheck.setImageResource(R.drawable.check_background);
                        weixinFlag = false;
                        isClick();
                    }
                }
                break;
            case R.id.aipay_layout:
                if (weixinFlag) {
                    weixin_isCheck.setImageResource(R.drawable.check_background);
                    weixinFlag = false;
                    aipay_isCheck.setImageResource(R.drawable.right);
                    aipayflag = true;
                    isClick();
                } else {
                    if (!aipayflag) {
                        aipay_isCheck.setImageResource(R.drawable.right);
                        aipayflag = true;
                        isClick();
                    } else {
                        aipay_isCheck.setImageResource(R.drawable.check_background);
                        aipayflag = false;
                        isClick();
                    }
                }
                break;

        }

    }
    private void getData(String uid, String refreashUrl) {
        OkHttpUtils
                .post()
                .url(refreashUrl)
                .addParams("uid", uid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        Toast.makeText(BuKaoActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onResponse(String s, int i) {
                        layout.setTag(s);
                        if(layout.getTag()!=null){
                            setData();
                        }
                    }
                });
    }

    /**
     * 给控件填充值
     */
    private void setData(){
        String str = layout.getTag().toString();
        Gson gson = new Gson();
        showRePay = gson.fromJson(str,ShowRePay.class);
        if(showRePay.getCode()==200){
            String url = showRePay.getResult().getFile();
            ShowRePay.Result res = showRePay.getResult();
            if (!url.endsWith(".jpg") && !url.endsWith(".jpeg") && !url.endsWith(".png") &&
                    !url.endsWith(".GIF") && !url.endsWith(".PNG") && !url.endsWith(".JPG") && !url.endsWith(".gif")) {
                Glide.with(this).load("http://www.baixinxueche.com/Public/Home/img/default.png").into(img);
            } else {
                Glide.with(this).load(url).into(img);
            }
            name.setText(res.getName());
            car.setText("所报班级："+res.getCar());
            classType.setText("所报班级："+res.getClass_class());
            phone.setText("手机号："+res.getPhone());
            orderfee.setText("￥"+res.getRefee());
            if(res.getBaixin_state()==1){
                ordername.setText("百信学车-科目一补考费");
            }else if(res.getBaixin_state()==2){
                ordername.setText("百信学车-科目二补考费");
            }else if(res.getBaixin_state()==3){
                ordername.setText("百信学车-科目三补考费");
            }
            coach_Price.setText("总金额:￥"+res.getRefee());
        }else{
            Toast.makeText(BuKaoActivity.this, showRePay.getReason(), Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 交补考费
     * @param uid
     */
    private void sendaiPay(String uid, String baixin_state, String fee) {
        OkHttpUtils
                .post()
                .url(payUrl)
                .addParams("uid", uid)
                .addParams("baixin_state", baixin_state)
                .addParams("refee",fee)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        Toast.makeText(BuKaoActivity.this, "加载失败", Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onResponse(String s, int i) {
                        Gson gson = new Gson();
                        final MyPayResult myPayResult = gson.fromJson(s,MyPayResult.class);
                        if(myPayResult.getCode()==200){
                            final int SDK_PAY_FLAG = 1;
                            final Handler mHandler = new Handler() {
                                @SuppressWarnings("unused")
                                public void handleMessage(Message msg) {
                                    switch (msg.what) {
                                        case SDK_PAY_FLAG: {
                                            PayResult payResult = new PayResult((String) msg.obj);
                                            /**
                                             * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                                             * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                                             * docType=1) 建议商户依赖异步通知
                                             */
                                            String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                                            String resultStatus = payResult.getResultStatus();
                                            // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                                            if (TextUtils.equals(resultStatus, "9000")) {
                                                Toast.makeText(BuKaoActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                                                finish();
                                            } else {
                                                // 判断resultStatus 为非"9000"则代表可能支付失败
                                                // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                                                if (TextUtils.equals(resultStatus, "8000")) {
                                                    Toast.makeText(BuKaoActivity.this, "支付结果确认中,请勿重新付款", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                } else {
                                                    // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                                                    Toast.makeText(BuKaoActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                            break;
                                        }
                                    }
                                }
                            };
                            Runnable payRunnable = new Runnable() {
                                @Override
                                public void run() {
                                    // 构造PayTask 对象
                                    PayTask alipay = new PayTask(BuKaoActivity.this);
                                    // 调用支付接口，获取支付结果
                                    String result = alipay.pay(myPayResult.getResult(), true);
                                    Message msg = new Message();
                                    msg.what = SDK_PAY_FLAG;
                                    msg.obj = result;
                                    mHandler.sendMessage(msg);
                                }
                            };
                            // 必须异步调用
                            Thread payThread = new Thread(payRunnable);
                            payThread.start();
                        }else if(myPayResult.getCode() ==400){
                            Toast.makeText(BuKaoActivity.this, myPayResult.getReason(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }






}
