package com.jgkj.bxxc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by fangzhou on 2016/10/22.
 * JPush推送跳转页面
 */
public class TestActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
        tv.setText("用户自定义打开的Activity");
        Intent intent = getIntent();
        if (null != intent) {
            Bundle bundle = getIntent().getExtras();
            String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
            String content = bundle.getString(JPushInterface.EXTRA_ALERT);
            String type = bundle.getString(JPushInterface.EXTRA_EXTRA);
            tv.setText("Title : " + title + "  " + "Content : " + content
                    + "  " + "type : " + type);
        }
        addContentView(tv, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
    }
}
