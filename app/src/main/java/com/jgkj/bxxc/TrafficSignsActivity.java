package com.jgkj.bxxc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.jgkj.bxxc.adapter.TrafficSignAdapter;

/**
 * Created by fangzhou on 2017/3/25.
 */

public class TrafficSignsActivity extends Activity implements AdapterView.OnItemClickListener{
    private TextView title;
    private Button back;
    private ListView listView;
    private TrafficSignAdapter adapter;
    private String[] strSigns = new String[]{"指示标志","禁令标志","警告标志"
            ,"辅助标志","指路标志","道路交通标线","道路施工标志","旅游标志"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taffic_signs);
        initView();
        initData();
    }

    private void initData() {
        adapter = new TrafficSignAdapter(this,strSigns);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    private void initView() {
        title = (TextView) findViewById(R.id.text_title);
        title.setText("交通标志");
        back = (Button) findViewById(R.id.button_backward);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        listView = (ListView) findViewById(R.id.listView);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        TextView textView = (TextView) view.findViewById(R.id.signs_text);
        Intent intent = new Intent();
        switch (i){
            case 0:
                intent.setClass(TrafficSignsActivity.this, WebViewActivity.class);
                intent.putExtra("url", "http://www.baixinxueche.com/webshow/keyi/zsbz.html");
                intent.putExtra("title", textView.getText().toString());
                startActivity(intent);
                break;
            case 1:
                intent.setClass(TrafficSignsActivity.this, WebViewActivity.class);
                intent.putExtra("url", "http://www.baixinxueche.com/webshow/keyi/jlbz.html");
                intent.putExtra("title", textView.getText().toString());
                startActivity(intent);
                break;
            case 2:
                intent.setClass(TrafficSignsActivity.this, WebViewActivity.class);
                intent.putExtra("url", "http://www.baixinxueche.com/webshow/keyi/jgbz.html");
                intent.putExtra("title", textView.getText().toString());
                startActivity(intent);
                break;
            case 3:
                intent.setClass(TrafficSignsActivity.this, WebViewActivity.class);
                intent.putExtra("url", "http://www.baixinxueche.com/webshow/keyi/fzbz.html");
                intent.putExtra("title", textView.getText().toString());
                startActivity(intent);
                break;
            case 4:
                intent.setClass(TrafficSignsActivity.this, WebViewActivity.class);
                intent.putExtra("url", "http://www.baixinxueche.com/webshow/keyi/dlbz.html");
                intent.putExtra("title", textView.getText().toString());
                startActivity(intent);
                break;
            case 5:
                intent.setClass(TrafficSignsActivity.this, WebViewActivity.class);
                intent.putExtra("url", "http://www.baixinxueche.com/webshow/keyi/dlbx.html");
                intent.putExtra("title", textView.getText().toString());
                startActivity(intent);
                break;
            case 6:
                intent.setClass(TrafficSignsActivity.this, WebViewActivity.class);
                intent.putExtra("url", "http://www.baixinxueche.com/webshow/keyi/dlsg.html ");
                intent.putExtra("title", textView.getText().toString());
                startActivity(intent);
                break;
            case 7:
                intent.setClass(TrafficSignsActivity.this, WebViewActivity.class);
                intent.putExtra("url", "http://www.baixinxueche.com/webshow/keyi/lybz.html");
                intent.putExtra("title", textView.getText().toString());
                startActivity(intent);
                break;

        }

    }
}
