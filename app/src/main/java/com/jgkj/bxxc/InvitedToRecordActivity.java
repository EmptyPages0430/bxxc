package com.jgkj.bxxc;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.jgkj.bxxc.adapter.InvitedToRecordAdapter;

/**
 * Created by fangzhou on 2017/3/25.
 */

public class InvitedToRecordActivity extends Activity implements AdapterView.OnItemClickListener{
    private TextView title;
    private Button back;
    private ListView listView;
    private InvitedToRecordAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invited_to_record);
        initView();
        initData();
    }

    private void initData() {
        adapter = new InvitedToRecordAdapter(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    private void initView() {
        title = (TextView) findViewById(R.id.text_title);
        title.setText("邀请记录");
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

    }
}
