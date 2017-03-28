package com.jgkj.bxxc;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jgkj.bxxc.bean.Code;
import com.jgkj.bxxc.tools.Base64;
import com.jgkj.bxxc.tools.PictureOptimization;
import com.umeng.socialize.UMShareAPI;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import okhttp3.Call;

/**
 * Create by fangzhou date 2016/10/forgot
 * 找回密码
 */
public class CallbackActivity extends Activity implements View.OnClickListener {
    private TimeCount time;
    private PictureOptimization po;
    private EditText phone_editText, phone_code_editText;
    private LinearLayout phone_code_linear, ll;
    private Button getCode_btn;
    private TextView countDown;
    private EditText scan_pwd, scan_pwd_second;
    //加载对话框
    private ProgressDialog dialog;
    private Button button_backward;
    private TextView title;

    private String phoneNo;
    private String sendMsg = "http://www.baixinxueche.com/index.php/Home/Apitokenmsg/remsg";

    private String callbackUrl = "http://www.baixinxueche.com/index.php/Home/Apitokenmsg/backpwd";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.callback);
        init();
    }

    private void init() {
        //注册页面
        getCode_btn = (Button) findViewById(R.id.getCode_btn);
        //倒计时监听
        countDown = (TextView) findViewById(R.id.countDown);
        countDown.setOnClickListener(this);
        phone_code_linear = (LinearLayout) findViewById(R.id.phone_code_linear);
        scan_pwd = (EditText) findViewById(R.id.scan_newpwd);
        scan_pwd_second = (EditText) findViewById(R.id.scan_newpwd_second);
        //手机号码输入框
        phone_editText = (EditText) findViewById(R.id.phone_editText);
        phone_code_editText = (EditText) findViewById(R.id.phone_code_editText);

        ll = (LinearLayout) findViewById(R.id.callback_id);
        time = new TimeCount(60000, 1000);//构造CountDownTimer对象
        getCode_btn.setOnClickListener(this);

        po = new PictureOptimization();
        ll.setBackgroundDrawable(PictureOptimization.bitmapToDrawble(PictureOptimization.decodeSampledBitmapFromResource(getResources(),
                R.drawable.background_null, 480, 760), CallbackActivity.this));
        //返回按钮
        button_backward = (Button) findViewById(R.id.button_backward);
        button_backward.setVisibility(View.VISIBLE);
        button_backward.setOnClickListener(this);
        //标题
        title = (TextView) findViewById(R.id.text_title);
        title.setText("找回密码");
    }

    /**
     * 发送验证码
     * @param phone 手机号
     */
    private void sendMeg(String phone) {
        OkHttpUtils
                .post()
                .url(sendMsg)
                .addParams("phone", phone)
                .addParams("token", Base64.encode((new SimpleDateFormat("yyyy-MM-dd").format(new Date())+phone).getBytes()))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        dialog.dismiss();
                        Toast.makeText(CallbackActivity.this, "发送失败", Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onResponse(String s, int i) {
                        dialog.dismiss();   //关闭progressdialog
                        Gson gson = new Gson();
                        final Code code = gson.fromJson(s, Code.class);
                        if (code.getCode() == 200) {
                            Toast.makeText(CallbackActivity.this, code.getReason(), Toast.LENGTH_LONG).show();
                            phone_code_linear.setVisibility(View.VISIBLE);
                            scan_pwd.setVisibility(View.VISIBLE);
                            scan_pwd_second.setVisibility(View.VISIBLE);
                            getCode_btn.setText("完成");
                            time.start();
                        } else {
                            Toast.makeText(CallbackActivity.this, code.getReason(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    /**
     * 找回密码
     * @param phone 手机号
     * @param password 密码
     * @param msg 验证码
     */
    private void callbackPwd(String phone, String password, String msg) {
        OkHttpUtils
                .post()
                .url(callbackUrl)
                .addParams("phone", phone)
                .addParams("password", password)
                .addParams("msg", msg)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        dialog.dismiss();
                        Toast.makeText(CallbackActivity.this, "发送失败", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        dialog.dismiss();   //关闭progressdialog
                        Gson gson = new Gson();
                        final Code code = gson.fromJson(s, Code.class);
                        if (code.getCode() == 200) {
                            Intent intent = new Intent();
                            intent.setClass(CallbackActivity.this, LoginActivity.class);
                            startActivity(intent);
                            Toast.makeText(CallbackActivity.this, "密码重置成功，请重新登录！", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(CallbackActivity.this, code.getReason(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }
        @Override
        public void onFinish() {//计时完毕时触发
            countDown.setText("重新验证");
            countDown.setClickable(true);
        }
        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            countDown.setClickable(false);
            countDown.setText(millisUntilFinished / 1000 + "秒");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.getCode_btn:
                String str = getCode_btn.getText().toString().trim();
                final String phone = phone_editText.getText().toString().trim();
                Pattern p = Pattern.compile("^((13[0-9])|(15[0-9])|(18[0,1,2,3,5-9])|(17[0-8])|(147)|(145))\\d{8}$");// 正则表达式
                if (str.equals("获取验证码")) {
                    if (phone_editText.getText().toString().equals("") || phone_editText.getText().toString() == null) {
                        Toast.makeText(CallbackActivity.this, "手机号码不能为空", Toast.LENGTH_SHORT).show();
                    /**
                      *Matcher m = p.matcher(phone)此方法是用来操作字符串
                      *m.matches()返回一个boolean类型的值，就是字符串与正则的匹配结果
                      */
                    } else if (!p.matcher(phone).matches()) {
                        Toast.makeText(CallbackActivity.this, "您输入的手机号段不存在或位数不正确", Toast.LENGTH_SHORT).show();
                    } else {
                        dialog = ProgressDialog.show(CallbackActivity.this, null, "玩命发送中...");
                        phoneNo = phone_editText.getText().toString().trim();
                        phone_editText.setVisibility(View.GONE);
                        time.start();
                        sendMeg(phoneNo);
                    }
                } else if (str.equals("完成")) {

                    String pwd1 = scan_pwd.getText().toString().trim();
                    String pwd2 = scan_pwd_second.getText().toString().trim();
                    if (phone_editText.getText().toString().equals("") || phone_editText.getText().toString() == null) {
                        Toast.makeText(CallbackActivity.this, "手机号码不能为空", Toast.LENGTH_SHORT).show();
                    } else if (phone_code_editText.getText().toString().equals("") || phone_code_editText.getText().toString() == null) {
                        Toast.makeText(CallbackActivity.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                    } else if (pwd1.equals("") || pwd1 == null) {
                        Toast.makeText(CallbackActivity.this, "您还没有输入密码", Toast.LENGTH_SHORT).show();
                    } else if (pwd2.equals("") || pwd2 == null) {
                        Toast.makeText(CallbackActivity.this, "请您再次输入密码", Toast.LENGTH_SHORT).show();
                    } else if (!pwd1.equals(pwd2)) {
                        Toast.makeText(CallbackActivity.this, "两次输入密码一致", Toast.LENGTH_SHORT).show();
                    } else {
                        dialog = ProgressDialog.show(this,null,"修改密码中...");
                        callbackPwd(phone_editText.getText().toString().trim(), pwd1, phone_code_editText.getText().toString().trim());
                    }
                }
                break;
            case R.id.countDown:
                String str1 = countDown.getText().toString();
                if (str1.equals("重新验证")) {
                    time.start();
                    sendMeg(phoneNo);
                }
                break;
            case R.id.button_backward:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
//┏┓　　　┏┓
//┏┛┻━━━┛┻┓
//┃　　　　　　　┃ 　
//┃　　　━　　　┃
//┃　┳┛　┗┳　┃
//┃　　　　　　　┃
//┃　　　┻　　　┃
//┃　　　　　　　┃
//┗━┓　　　┏━┛
//    ┃　　　┃   神兽保佑　　　　　　　　
//    ┃　　　┃   代码无忧！
//    ┃　　　┗━━━┓
//    ┃　　　　　　　┣┓
//    ┃　　　　　　　┏┛
//    ┗┓┓┏━┳┓┏┛
// 	    ┃┫┫　┃┫┫
//      ┗┻┛　┗┻┛

