package com.jgkj.bxxc;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by fangzhou on 2016/11/19.
 * 提交个人信息照片的结果
 */
public class SubmitPicActivity extends Activity {
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submitpic);
        init();
    }
    private void init() {
        title = (TextView) findViewById(R.id.text_title);
        title.setText("提交状态");
    }
}
