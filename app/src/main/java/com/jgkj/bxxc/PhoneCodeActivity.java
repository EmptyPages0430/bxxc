package com.jgkj.bxxc;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by fangzhou on 2017/3/27.
 * <p>
 * 绑定支付宝验证码
 */

public class PhoneCodeActivity extends Activity implements View.OnClickListener {
    private Button button_backward, button_forward;
    private TextView title, countDown;
    private EditText phone_code_editText;
    private TimeCount time;
    private boolean isMail ;
    private LinearLayout layout1,layout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_code);
        initView();
    }

    private void initView() {
        button_backward = (Button) findViewById(R.id.button_backward);
        title = (TextView) findViewById(R.id.text_title);
        button_backward.setVisibility(View.VISIBLE);
        button_backward.setOnClickListener(this);
        title.setText("验证码");
        button_forward = (Button) findViewById(R.id.button_forward);
        button_forward.setText("完成");
        button_forward.setVisibility(View.VISIBLE);
        button_forward.setOnClickListener(this);
        countDown = (TextView) findViewById(R.id.countDown);
        countDown.setOnClickListener(this);
        time = new TimeCount(60000, 1000);
        phone_code_editText = (EditText) findViewById(R.id.phone);

        layout1 = (LinearLayout) findViewById(R.id.layout1);
        layout2 = (LinearLayout) findViewById(R.id.layout2);

        isMail = getIntent().getBooleanExtra("isMail",false);
        if(!isMail){
            layout1.setVisibility(View.GONE);
            phone_code_editText.setVisibility(View.GONE);
            layout2.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_backward:
                finish();
                break;
            case R.id.countDown:
                time.start();
                break;
            case R.id.button_forward:
                if (phone_code_editText.getText().toString().equals("") || phone_code_editText == null) {
                    Toast.makeText(PhoneCodeActivity.this, "您还没有输入验证码", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PhoneCodeActivity.this, button_forward.getText().toString(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            countDown.setText("重新获取验证码");
            countDown.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            countDown.setClickable(false);
            countDown.setText(millisUntilFinished / 1000 + "秒后重新发送");
        }
    }
}
