package com.jgkj.bxxc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by fangzhou on 2016/11/22.
 */
public class ChangePwdActivity extends Activity implements View.OnClickListener{
    private Button back_forward,changePwdSubmit;
    private TextView title;
    private EditText oldPwd,newPwd,newPwd2;
    private String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changepassword);
        //初始化布局
        init();
    }
    private void init() {
        back_forward = (Button) findViewById(R.id.button_backward);
        back_forward.setVisibility(View.VISIBLE);
        back_forward.setOnClickListener(this);
        title = (TextView) findViewById(R.id.text_title);
        title.setText("修改密码");
        //获取输入框的值
        oldPwd = (EditText) findViewById(R.id.oldpwd);
        newPwd = (EditText) findViewById(R.id.newpwd);
        newPwd2 = (EditText) findViewById(R.id.newpwd_second);
        //完成修改
        changePwdSubmit = (Button) findViewById(R.id.changePwdSubmit);
        changePwdSubmit.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_backward:
                finish();
                break;
            case R.id.changePwdSubmit:
                Intent intent = new Intent();
                intent.setClass(ChangePwdActivity.this,LoginActivity.class);
                startActivity(intent);
                break;
        }
    }
}
