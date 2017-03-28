package com.jgkj.bxxc;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jgkj.bxxc.bean.Code;
import com.jgkj.bxxc.tools.PictureOptimization;
import com.umeng.socialize.UMShareAPI;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.OkHttpClient;

/**
 * 注册页面
 */
public class RegisterActivity extends Activity implements OnClickListener {
    private TimeCount time;
    private PictureOptimization po;
    private EditText phone_editText, phone_code_editText;
    private LinearLayout phone_code_linear, ll;
    private Button getCode_btn;
    private TextView countDown;
    private EditText scan_pwd, scan_pwd_second;
    //加载对话框
    private ProgressDialog dialog, dialogRegister;
    private Button button_backward;
    private TextView title;
    private OkHttpClient mOkHttpClient;

    private class SendCode {
        private int code;
        private String reason;

        public int getCode() {
            return code;
        }

        public String getReason() {
            return reason;
        }
    }

    private String path = "http://www.baixinxueche.com/index.php/Home/Api/msg";
    //请求
    private String url = "http://www.baixinxueche.com/index.php/Home/Api/allregister";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        //注册页面
        getCode_btn = (Button) findViewById(R.id.getCode_btn);
        //倒计时监听
        countDown = (TextView) findViewById(R.id.countDown);
        countDown.setOnClickListener(this);
        phone_code_linear = (LinearLayout) findViewById(R.id.phone_code_linear);
        scan_pwd = (EditText) findViewById(R.id.scan_pwd);
        scan_pwd_second = (EditText) findViewById(R.id.scan_pwd_second);
        //手机号码输入框
        phone_editText = (EditText) findViewById(R.id.phone_editText);
        phone_code_editText = (EditText) findViewById(R.id.phone_code_editText);

        ll = (LinearLayout) findViewById(R.id.register_id);
        time = new TimeCount(60000, 1000);//构造CountDownTimer对象
        getCode_btn.setOnClickListener(this);

        po = new PictureOptimization();
        ll.setBackgroundDrawable(PictureOptimization.bitmapToDrawble(PictureOptimization.decodeSampledBitmapFromResource(getResources(),
                R.drawable.background_null, 480, 760), RegisterActivity.this));

        //返回按钮
        button_backward = (Button) findViewById(R.id.button_backward);
        button_backward.setVisibility(View.VISIBLE);
        button_backward.setOnClickListener(this);
        //标题
        title = (TextView) findViewById(R.id.text_title);
        title.setText("免费注册");
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

    private void sendMeg(String phone) {
        OkHttpUtils
                .post()
                .url(path)
                .addParams("phone", phone)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        dialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "发送失败", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        dialog.dismiss();   //关闭progressdialog
                        Gson gson = new Gson();
                        final Code code = gson.fromJson(s, Code.class);
                        if (code.getCode() == 200) {
                            Toast.makeText(RegisterActivity.this, "发送成功", Toast.LENGTH_LONG).show();
                            phone_code_linear.setVisibility(View.VISIBLE);
                            phone_editText.setVisibility(View.GONE);
                            scan_pwd.setVisibility(View.VISIBLE);
                            scan_pwd_second.setVisibility(View.VISIBLE);
                            getCode_btn.setText("完成注册");
                            time.start();
                        } else {
                            Toast.makeText(RegisterActivity.this, code.getReason(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    //注册请求
    private void registerBtn(String phone, String phoneCode, String password) {
        OkHttpUtils
                .post()
                .url(url)
                .addParams("password", password)
                .addParams("phone", phone)
                .addParams("msg", phoneCode)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        if(dialogRegister.isShowing()){
                            dialogRegister .dismiss();
                        }
                        Toast.makeText(RegisterActivity.this, "请求异常", Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onResponse(String s, int i) {
                        if(dialogRegister.isShowing()){
                            dialogRegister .dismiss();
                        }
                        Gson gson = new Gson();
                        SendCode code = gson.fromJson(s, SendCode.class);
                        if (code.getCode() == 200) {
                            Toast.makeText(RegisterActivity.this, code.getReason(), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent();
                            intent.setClass(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(RegisterActivity.this, code.getReason(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
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
                        Toast.makeText(RegisterActivity.this, "手机号码不能为空", Toast.LENGTH_SHORT).show();
                    /*
                     *Matcher m = p.matcher(phone)此方法是用来操作字符串
                     *m.matches()返回一个boolean类型的值，就是字符串与正则的匹配结果
                     */
                    } else if (!p.matcher(phone).matches()) {
                        Toast.makeText(RegisterActivity.this, "您输入的手机号段不存在或位数不正确", Toast.LENGTH_SHORT).show();
                    } else {
                        dialog = ProgressDialog.show(RegisterActivity.this, null, "玩命发送中...");
                        String phoneNo = phone_editText.getText().toString().trim();
                        sendMeg(phoneNo);
                    }
                } else if (str.equals("完成注册")) {
                    String pwd1 = scan_pwd.getText().toString().trim();
                    String pwd2 = scan_pwd_second.getText().toString().trim();
                    if (phone_editText.getText().toString().equals("") || phone_editText.getText().toString() == null) {
                        Toast.makeText(RegisterActivity.this, "手机号码不能为空", Toast.LENGTH_SHORT).show();
                    } else if (phone_code_editText.getText().toString().equals("") || phone_code_editText.getText().toString() == null) {
                        Toast.makeText(RegisterActivity.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                    } else if (pwd1.equals("") || pwd1 == null) {
                        Toast.makeText(RegisterActivity.this, "您还没有输入密码", Toast.LENGTH_SHORT).show();
                    } else if (pwd2.equals("") || pwd2 == null) {
                        Toast.makeText(RegisterActivity.this, "请您再次输入密码", Toast.LENGTH_SHORT).show();
                    } else if (!pwd1.equals(pwd2)) {
                        Toast.makeText(RegisterActivity.this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
                    } else if (pwd1.length()<6||pwd2.length()<6) {
                        Toast.makeText(RegisterActivity.this, "密码长度不能低于6位", Toast.LENGTH_SHORT).show();
                    } else {
                        dialogRegister = ProgressDialog.show(RegisterActivity.this, null, "玩命发送中...");
                        registerBtn(phone_editText.getText().toString().trim(),
                                phone_code_editText.getText().toString().trim(),
                                pwd1);
                    }
                }
                break;
            case R.id.countDown:
                String str1 = countDown.getText().toString();
                if (str1.equals("重新验证")) {
                    dialog = ProgressDialog.show(RegisterActivity.this, null, "玩命发送中...");
                    String phoneNo = phone_editText.getText().toString().trim();
                    sendMeg(phoneNo);
                    time.start();
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
