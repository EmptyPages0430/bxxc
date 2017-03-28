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
 *
 * 绑定支付宝
 */

public class BindingAlipayActivity extends Activity implements View.OnClickListener{
    private Button button_backward,button_forward;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.binding_alipay);
        initView();
    }

    private void initView() {
        button_backward = (Button) findViewById(R.id.button_backward);
        title = (TextView) findViewById(R.id.text_title);
        button_backward.setVisibility(View.VISIBLE);
        button_backward.setOnClickListener(this);
        title.setText("支付宝账号");
        button_forward = (Button) findViewById(R.id.button_forward);
        button_forward.setVisibility(View.VISIBLE);
        button_forward.setText("添加");
        button_forward.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_backward:
                finish();
                break;
            case R.id.button_forward:
                Intent intent = new Intent();
                intent.setClass(BindingAlipayActivity.this,AddAlipayActivity.class);
                startActivity(intent);
                break;

        }
    }
}
