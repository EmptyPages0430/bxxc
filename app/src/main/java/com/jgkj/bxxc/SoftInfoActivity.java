package com.jgkj.bxxc;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jgkj.bxxc.tools.FileCacheUtils;

import java.io.File;

/**
 * Created by fangzhou on 2016/11/5.
 */
public class SoftInfoActivity extends Activity implements View.OnClickListener{
    private Button back_forward;
    private TextView text_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.softinfo);
        init();
    }

    private void init() {
        Log.i("Action","这是软件信息页面");
        back_forward = (Button) findViewById(R.id.button_backward);
        back_forward.setVisibility(View.VISIBLE);
        text_title = (TextView) findViewById(R.id.text_title);
        text_title.setText("软件信息");
        back_forward.setOnClickListener(this);
        File file = new File("mnt/sdcard/android/data/com.jgkj.bxxc");
        try {
            String str = FileCacheUtils.getCacheSize(file);
            Log.i("Action","本地应用缓存大小为："+str);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
