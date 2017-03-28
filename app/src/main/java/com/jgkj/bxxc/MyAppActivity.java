package com.jgkj.bxxc;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.jgkj.bxxc.adapter.AppTotalAdapter;
import com.jgkj.bxxc.bean.AppAction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fangzhou on 2016/11/5.
 * 展示我的预约
 */
public class MyAppActivity extends Activity implements View.OnClickListener{
    private Button back_forward;
    private TextView text_title;
    private List<AppAction> list;
    private ListView listView;
    private AppTotalAdapter adapter;
    private AppAction action;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myapp);
        init();
    }

    private void init() {
        back_forward = (Button) findViewById(R.id.button_backward);
        back_forward.setVisibility(View.VISIBLE);
        text_title = (TextView) findViewById(R.id.text_title);
        text_title.setText("我的预约");
        back_forward.setOnClickListener(this);
        //实例化listView
        listView = (ListView) findViewById(R.id.myApp_ListView);
        list = new ArrayList<>();
        action = new AppAction("方舟","2016-11-11","科目二","越达驾校");
        list.add(action);
        adapter = new AppTotalAdapter(MyAppActivity.this,list);
        listView.setAdapter(adapter);
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
