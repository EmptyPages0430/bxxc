package com.jgkj.bxxc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

/**
 * Created by fangzhou on 2017/3/27.
 * <p>
 * 添加支付宝账户
 */

public class AddAlipayActivity extends Activity implements View.OnClickListener, TextWatcher {
    private Button button_backward, button_forward;
    private TextView title;
    private EditText phone;
    private boolean isMail = false;
    private Pattern p = Pattern.compile("^((13[0-9])|(15[0-9])|(18[0,1,2,3,5-9])|(17[0-8])|(147)|(145))\\d{8}$");// 正则表达式
    private Pattern mail = Pattern.compile("^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_alipay);
        initView();
    }

    private void initView() {
        button_backward = (Button) findViewById(R.id.button_backward);
        title = (TextView) findViewById(R.id.text_title);
        button_backward.setVisibility(View.VISIBLE);
        button_backward.setOnClickListener(this);
        title.setText("添加支付宝");
        button_forward = (Button) findViewById(R.id.button_forward);
        button_forward.setText("下一步");
        button_forward.setOnClickListener(this);
        phone = (EditText) findViewById(R.id.phone);
        phone.addTextChangedListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.button_backward:
                finish();
                break;

            case R.id.button_forward:
                Intent intent = new Intent();
                intent.setClass(AddAlipayActivity.this, PhoneCodeActivity.class);
                intent.putExtra("isMail",isMail);
                startActivity(intent);
                break;

        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String str = phone.getText().toString().trim();
        if (p.matcher(str).matches()) {
            button_forward.setVisibility(View.VISIBLE);
            isMail = false;
        } else if(mail.matcher(str).matches()){
            button_forward.setVisibility(View.VISIBLE);
            isMail = true;
        }else {
            button_forward.setVisibility(View.GONE);
        }

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

}
