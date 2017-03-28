package com.jgkj.bxxc;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by fangzhou on 2016/11/15.
 */
public class PayMethodActivity extends Activity implements View.OnClickListener{
    private Button back_forward;
    private TextView textTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_paymethod);
        init();
    }

    private void init() {
        //标题栏
        back_forward = (Button) findViewById(R.id.button_backward);
        back_forward.setVisibility(View.VISIBLE);
        back_forward.setOnClickListener(this);
        textTitle = (TextView) findViewById(R.id.text_title);
        textTitle.setText("选择支付方式");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_backward:
                finish();
                break;
        }
    }
}
