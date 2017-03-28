package com.jgkj.bxxc;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by fangzhou on 2017/2/25.
 * 活动中心
 */

public class MyActivity extends Activity implements View.OnClickListener {
    private WebView webView;
    private Button back;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myactivity);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void initView() {
        back = (Button) findViewById(R.id.button_backward);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(this);
        title = (TextView) findViewById(R.id.text_title);
        title.setText("活动中心");
        webView = (WebView) findViewById(R.id.webView);
        /**
         * 在WebView的开发过程中当你需要使用到一些高级功能可以通过设置WebChromeClient从而来辅助WebView处理
         * JavaScript 的对话框、网站图标、网站title、加载进度等
         */
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://www.baixinxueche.com/index.php/Home/Index/tishi.html");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_backward:
                finish();
                break;
        }
    }
}
