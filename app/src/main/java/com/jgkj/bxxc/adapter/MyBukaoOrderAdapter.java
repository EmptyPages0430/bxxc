package com.jgkj.bxxc.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jgkj.bxxc.R;
import com.jgkj.bxxc.bean.BuKaoOrder;
import com.jgkj.bxxc.bean.MyPayResult;
import com.jgkj.bxxc.bean.UserInfo;
import com.jgkj.bxxc.tools.PayResult;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;

public class MyBukaoOrderAdapter extends BaseAdapter {
    private Context context;
    private List<BuKaoOrder.Result> list;
    private LayoutInflater inflater;
    private BuKaoOrder.Result result;
    private String uid;
    private String token;
    private String rapayUrl = "http://www.baixinxueche.com/index.php/Home/Aliappretext/retestPayList";

    public MyBukaoOrderAdapter(String uid, Context context, List<BuKaoOrder.Result> list, String token) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
        this.uid = uid;
        this.token = token;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {

        return position;
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.order_listview, parent, false);
            viewHolder.orderNumber = (TextView) convertView
                    .findViewById(R.id.orderNumber);
            viewHolder.orderState = (TextView) convertView
                    .findViewById(R.id.orderState);
            viewHolder.coachName = (TextView) convertView
                    .findViewById(R.id.coachName);
            viewHolder.place = (TextView) convertView
                    .findViewById(R.id.place);
            viewHolder.mycar = (TextView) convertView
                    .findViewById(R.id.mycar);
            viewHolder.myclass = (TextView) convertView
                    .findViewById(R.id.myclass);
            viewHolder.price = (TextView) convertView
                    .findViewById(R.id.price);
            viewHolder.state = (TextView) convertView
                    .findViewById(R.id.state);
            viewHolder.pricename = (TextView) convertView
                    .findViewById(R.id.pricename);//state
            viewHolder.coachHead = (ImageView) convertView
                    .findViewById(R.id.coachHead);
            viewHolder.name = (TextView) convertView
                    .findViewById(R.id.name);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        result = list.get(position);
        viewHolder.orderNumber.setText("订单编号：" + result.getOut_trade_no());
        if (result.getTrade_status().equals("0")) {
            viewHolder.orderState.setText(R.string.pay);
            viewHolder.orderState.setTextColor(context.getResources().getColor(R.color.red));
            viewHolder.orderState.setTag(result.getBaixin_state()+","+result.getOut_trade_no());
            viewHolder.orderState.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String[] tag = view.getTag().toString().split(",");
                    int baixinState = Integer.parseInt(tag[0]);
                    SharedPreferences sp = context.getSharedPreferences("USER",
                            Activity.MODE_PRIVATE);
                    String str = sp.getString("userInfo", null);
                    Gson gson = new Gson();
                    UserInfo userInfo = gson.fromJson(str, UserInfo.class);
                    String state = userInfo.getResult().getState();
                    if ((state.startsWith("科目一未通过") && baixinState == 1) ||
                            ((state.startsWith("科目二未通过")  &&baixinState == 2)) ||
                            ((state.startsWith("科目三未通过")  && baixinState == 3))) {
                        sendaiPay(tag[1]);
                    } else {
                        Toast.makeText(context, "您已不处于该状态", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else if (result.getTrade_status().equals("1")) {
            viewHolder.orderState.setText("已付款");
            viewHolder.orderState.setTextColor(context.getResources().getColor(R.color.green));
        }
        viewHolder.name.setText("学员：");
        viewHolder.coachName.setText(result.getName());
        viewHolder.place.setText("所报班级：" + result.getClass_class());
        viewHolder.mycar.setText("所报车型：" + result.getCar());
        viewHolder.myclass.setVisibility(View.GONE);
        if (result.getBaixin_state().equals("1")) {
            viewHolder.pricename.setText("科目一补考费：");
        } else if (result.getBaixin_state().equals("2")) {
            viewHolder.pricename.setText("科目二补考费：");
        } else if (result.getBaixin_state().equals("3")) {
            viewHolder.pricename.setText("科目三补考费：");
        }
        viewHolder.price.setText("￥" + result.getRefee());
        Glide.with(context).load(result.getFile()).placeholder(R.drawable.head1).error(R.drawable.head1).into(viewHolder.coachHead);
        return convertView;
    }

    /**
     * 支付
     * @param orderNumber 订单号
     */
    private void sendaiPay(String orderNumber) {
        OkHttpUtils
                .post()
                .url(rapayUrl)
                .addParams("out_trade_no", orderNumber)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        Toast.makeText(context, "加载失败", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(final String s, int i) {
                        Gson gson = new Gson();
                        final MyPayResult myPayResult = gson.fromJson(s, MyPayResult.class);
                        if (myPayResult.getCode() == 200) {
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
                                                Toast.makeText(context, "支付成功，请重新进入刷新该页面", Toast.LENGTH_SHORT).show();
                                            } else {
                                                // 判断resultStatus 为非"9000"则代表可能支付失败
                                                // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                                                if (TextUtils.equals(resultStatus, "8000")) {
                                                    Toast.makeText(context, "支付结果确认中,请勿重新付款", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                                                    Toast.makeText(context, "支付失败", Toast.LENGTH_SHORT).show();
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
                                    PayTask alipay = new PayTask((Activity) context);
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
                        } else if (myPayResult.getCode() == 400) {
                            Toast.makeText(context, myPayResult.getReason(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    static class ViewHolder {
        public TextView orderNumber, orderState, coachName,name,
                place, mycar, myclass, price, pricename, state;
        public ImageView coachHead;

    }

}
