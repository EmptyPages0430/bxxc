package com.jgkj.bxxc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by fangzhou on 2017/3/27.
 * <p>
 * 我的设置--我的账户
 */

public class Setting_AccountActivity extends Activity implements View.OnClickListener {
    private Button back_forward;
    private TextView title;
    private LinearLayout bindingAlipay,myOrder;
    private int uid;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_account);
        initView();
        getData();
    }

    private void getData() {
        Intent intent = getIntent();
        uid = intent.getIntExtra("uid",-1);
        token = intent.getStringExtra("token");
    }

    private void initView() {
        back_forward = (Button) findViewById(R.id.button_backward);
        title = (TextView) findViewById(R.id.text_title);
        back_forward.setVisibility(View.VISIBLE);
        back_forward.setOnClickListener(this);
        title.setText("我的账户");

        bindingAlipay = (LinearLayout) findViewById(R.id.bindingAlipay);
        myOrder = (LinearLayout) findViewById(R.id.myOrder);
        bindingAlipay.setOnClickListener(this);
        myOrder.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.button_backward:
                finish();
                break;
            case R.id.bindingAlipay:
                intent.setClass(Setting_AccountActivity.this, BindingAlipayActivity.class);
                startActivity(intent);
                break;
            case R.id.myOrder:
                intent.setClass(Setting_AccountActivity.this, MyOrderActivity.class);
                intent.putExtra("uid", uid);
                intent.putExtra("token", token);
                startActivity(intent);
                break;

        }
    }
}
