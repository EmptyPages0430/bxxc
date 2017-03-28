package com.jgkj.bxxc;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jgkj.bxxc.bean.CarPickUpRoute;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class CarSendActivity extends Activity implements View.OnClickListener{
	private Button back;
	private TextView title;
	private WebView webView;
	private RadioButton radio_btn01,radio_btn02;
	private ProgressDialog dialog;
	private int schoolId ;
	private String name;
	private CarPickUpRoute carPickUpRoute;
	private TextView textView;
	private Dialog routeDialog,psDialog;
	private Button route2;
	private Button route1;
	private Button cancel;
	private View dialogView;
	private String placePath = "http://www.baixinxueche.com/index.php/Home/Apitoken/schoolLine";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
		init();
		dialog = ProgressDialog.show(CarSendActivity.this, null, "玩命加载中...");
		getData();
	}

	/**
	 * 网络请求加载路线
	 */
	private void getData() {
		OkHttpUtils
                .post()
                .url(placePath)
				.addParams("id",schoolId+"")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        if(dialog.isShowing()){
                            dialog.dismiss();
                        }
                        Toast.makeText(CarSendActivity.this, "请检查网络", Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onResponse(String s, int i) {
						webView.setTag(s);
                        if (webView.getTag() != null) {
                            addAdapter();
                        } else {
                            Toast.makeText(CarSendActivity.this, "网络状态不佳,请检查网络", Toast.LENGTH_LONG).show();
                        }
                    }
                });
	}
	private void addAdapter(){
		if(dialog.isShowing()){
			dialog.dismiss();
		}
		String tag = webView.getTag().toString();
		Gson gson = new Gson();
		carPickUpRoute = gson.fromJson(tag,CarPickUpRoute.class);
		if(carPickUpRoute.getCode()==200){
			webView.loadUrl(carPickUpRoute.getResult().get(0).getRoute());
			textView.setText("\u3000\u3000"+carPickUpRoute.getResult().get(0).getInfo());
		}else{
			textView.setText("\u3000\u3000"+carPickUpRoute.getReason());
			Toast.makeText(CarSendActivity.this, carPickUpRoute.getReason(), Toast.LENGTH_LONG).show();
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void init() {
		back = (Button) findViewById(R.id.button_backward);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(this);
		title = (TextView) findViewById(R.id.text_title);
		title.setText("车接车送");
		webView = (WebView) findViewById(R.id.webView);

		webView.setWebChromeClient(new WebChromeClient());
		webView.setWebViewClient(new WebViewClient());
		webView.getSettings().setJavaScriptEnabled(true);
//		webView.loadUrl("http://www.baixinxueche.com/car.html");
		radio_btn01 = (RadioButton) findViewById(R.id.radio_btn01);
		radio_btn02 = (RadioButton) findViewById(R.id.radio_btn02);
		radio_btn01.setOnClickListener(this);
		radio_btn02.setOnClickListener(this);
		schoolId = getIntent().getIntExtra("schoolId",0);
		name = getIntent().getStringExtra("name");
		title.setText(name);
		textView = (TextView) findViewById(R.id.textView);

	}
	//路线选择dialog
	public void showDialog() {
		routeDialog = new Dialog(CarSendActivity.this, R.style.ActionSheetDialogStyle);
		// 填充对话框的布局
		dialogView = LayoutInflater.from(CarSendActivity.this).inflate(R.layout.route_dialog, null);
		// 初始化控件
		route1 = (Button) dialogView.findViewById(R.id.route1);
		route2 = (Button) dialogView.findViewById(R.id.route2);
		cancel = (Button) dialogView.findViewById(R.id.cancel);
		cancel.setOnClickListener(this);
		route2.setOnClickListener(this);
		route1.setOnClickListener(this);
		// 将布局设置给Dialog
		routeDialog.setContentView(dialogView);
		// 获取当前Activity所在的窗体
		Window dialogWindow = routeDialog.getWindow();
		// 设置dialog横向充满
		dialogWindow.setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		dialogWindow.setGravity(Gravity.BOTTOM);
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.y = 20;// 设置Dialog距离底部的距离
		dialogWindow.setAttributes(lp);
		routeDialog.show();// 显示对话框
	}
	//接送时间
	public void timeDialog() {
		psDialog = new Dialog(CarSendActivity.this, R.style.ActionSheetDialogStyle);
		// 填充对话框的布局
		dialogView = LayoutInflater.from(CarSendActivity.this).inflate(R.layout.pickup_send_time, null);
		// 将布局设置给Dialog
		psDialog.setContentView(dialogView);
		// 获取当前Activity所在的窗体
		Window dialogWindow = psDialog.getWindow();
		// 设置dialog横向充满
		dialogWindow.setLayout(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		dialogWindow.setGravity(Gravity.CENTER);
		psDialog.show();// 显示对话框
	}
	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.button_backward:
				finish();
				break;
			case R.id.radio_btn01:
				showDialog();
				break;
			case R.id.radio_btn02:
				timeDialog();
				break;
			case R.id.route1:
				routeDialog.dismiss();
				if(carPickUpRoute.getCode()==200){
					if(radio_btn01.getText().toString().equals("线路一")){
						Toast.makeText(CarSendActivity.this, "已经是线路一了！", Toast.LENGTH_LONG).show();
					}else{
						radio_btn01.setText("线路一");
						webView.loadUrl(carPickUpRoute.getResult().get(0).getRoute());
						textView.setText("\u3000\u3000"+carPickUpRoute.getResult().get(0).getInfo());
					}
				}else{
					textView.setText("\u3000\u3000"+carPickUpRoute.getReason());
					Toast.makeText(CarSendActivity.this, carPickUpRoute.getReason(), Toast.LENGTH_LONG).show();
				}
				break;
			case R.id.route2:
				routeDialog.dismiss();
				if(carPickUpRoute.getCode()==200){
					if(carPickUpRoute.getResult().size()>1){
						if(radio_btn01.getText().toString().equals("线路二")){
							Toast.makeText(CarSendActivity.this, "已经是线路二了！", Toast.LENGTH_LONG).show();
						}else{
							radio_btn01.setText("线路二");
							webView.loadUrl(carPickUpRoute.getResult().get(1).getRoute());
							textView.setText("\u3000\u3000"+carPickUpRoute.getResult().get(0).getInfo());
						}
					}else{
						Toast.makeText(CarSendActivity.this, "只有一条接送路线", Toast.LENGTH_LONG).show();
					}
				}else{
					textView.setText("\u3000\u3000"+carPickUpRoute.getReason());
					Toast.makeText(CarSendActivity.this, carPickUpRoute.getReason(), Toast.LENGTH_LONG).show();
				}
				break;
			case R.id.cancel:
				routeDialog.dismiss();
				break;
		}
	}
}
