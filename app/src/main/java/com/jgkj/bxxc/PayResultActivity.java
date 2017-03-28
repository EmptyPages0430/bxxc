package com.jgkj.bxxc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by fangzhou on 2016/11/18.
 * 用户支付返回结果，展示支付成功还是支付失败
 */
public class PayResultActivity extends Activity implements View.OnClickListener{
    private Button button_forward;
    private ImageView success,failure;
    private Intent intent;
    private TextView price;

    private LinearLayout shibai,chenggong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payresult);
        init();
        data();
    }
    private void data() {
        intent = getIntent();
        int payResult = intent.getIntExtra("result",-1);
        if(payResult==1){
            double count = intent.getDoubleExtra("price",3688.00);
            price.setText(count+"");
            success.setVisibility(View.VISIBLE);
            failure.setVisibility(View.GONE);
            button_forward.setTag(1);
        }else if(payResult==0){
            failure.setVisibility(View.VISIBLE);
            success.setVisibility(View.GONE);
            chenggong.setVisibility(View.GONE);
            shibai.setVisibility(View.VISIBLE);
            button_forward.setTag(0);
        }
    }

    private void init() {
        button_forward = (Button) findViewById(R.id.button_forward);
        button_forward.setText("完成");
        button_forward.setVisibility(View.VISIBLE);
        button_forward.setOnClickListener(this);
        success = (ImageView) findViewById(R.id.success);
        failure = (ImageView) findViewById(R.id.failure);
        price = (TextView) findViewById(R.id.price);

        chenggong = (LinearLayout) findViewById(R.id.chenggong);
        shibai = (LinearLayout) findViewById(R.id.shibai);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_forward:
                int tag = Integer.parseInt(button_forward.getTag().toString());
                int uid = intent.getIntExtra("uid",-1);
                switch (tag){
                    case 0:
                        Intent login = new Intent();
                        login.setClass(PayResultActivity.this, HomeActivity.class);
                        login.putExtra("FromActivity", "MySetting");
                        startActivity(login);
                        finish();
                        break;
                    case 1:
                        Intent successIntent = new Intent();
                        successIntent.setClass(PayResultActivity.this,RegisterDetailActivity2.class);
                        successIntent.putExtra("uid",uid);
                        startActivity(successIntent);
                        finish();
                        break;
                }
                break;
        }
    }
}
