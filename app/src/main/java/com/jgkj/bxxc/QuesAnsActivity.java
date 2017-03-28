package com.jgkj.bxxc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jgkj.bxxc.adapter.QuesAnsAdapter;

/**
 * Created by fangzhou on 2017/3/14.
 * <p>
 * 答疑解惑
 */

public class QuesAnsActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private ListView listView;
    private TextView title;
    private Button back;
    private String[] listItem = {"练车指南", "常见问题", "支付问题"};
    private QuesAnsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ques_ans);
        initView();
        initData();
    }

    //初始化数据
    private void initData() {
        adapter = new QuesAnsAdapter(QuesAnsActivity.this, listItem);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    //初始化控件
    private void initView() {
        listView = (ListView) findViewById(R.id.listView);

        title = (TextView) findViewById(R.id.text_title);
        title.setText("答疑解惑");
        back = (Button) findViewById(R.id.button_backward);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_backward:
                finish();
                break;

        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent();
        switch (i) {
            case 0:
                intent.setClass(QuesAnsActivity.this,WebViewActivity.class);
                intent.putExtra("url","http://www.baixinxueche.com/webshow/keer/guide.html");
                intent.putExtra("title","练车指南");
                startActivity(intent);
                break;
            case 1:
                intent.setClass(QuesAnsActivity.this,WebViewActivity.class);
                intent.putExtra("url","http://www.baixinxueche.com/webshow/keer/problem.html");
                intent.putExtra("title","常见问题");
                startActivity(intent);
                break;
            case 2:
                intent.setClass(QuesAnsActivity.this,WebViewActivity.class);
                intent.putExtra("url","http://www.baixinxueche.com/webshow/keer/pay.html");
                intent.putExtra("title","支付问题");
                startActivity(intent);
                break;

        }


    }
}
