package com.jgkj.bxxc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by fangzhou on 2017/1/20.
 */

public class WebViewActivity extends Activity implements View.OnClickListener{
    private TextView title;
    private Button back;
    private WebView webView;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);
        initView();
    }

    private void initView() {
        title = (TextView) findViewById(R.id.text_title);
        back = (Button) findViewById(R.id.button_backward);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(this);

        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        title.setText(intent.getStringExtra("title"));
        webView.loadUrl(url);

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
