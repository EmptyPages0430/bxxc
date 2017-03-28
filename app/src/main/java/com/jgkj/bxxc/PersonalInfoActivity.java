package com.jgkj.bxxc;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jgkj.bxxc.bean.UserInfo;
import com.jgkj.bxxc.tools.PictureOptimization;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Calendar;

import okhttp3.Call;

/**
 * 个人信息展示页面，并修改
 * 在mysetting中点击个人信息进去的页面
 */
public class PersonalInfoActivity extends Activity implements OnClickListener {
    private RelativeLayout head_background;
    private PictureOptimization po;
    private ImageView back_up;
    private TextView edit, choose_sex_man, choose_sex_woman, sex_id_textView;
    private TextView edit_personal_info, profile_textView, realName;

    private EditText nameEdit;
    private Dialog sex_dialog;
    private LinearLayout sex_id;
    private EditText profile_editText;
    private TextView text_title;
    private Button button_forward, button_backward;
    private View inflate;
    // 定义显示时间控件
    private Calendar calendar = Calendar.getInstance();
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;
    //修改密码
    private TextView changePassword;
    //读取个人信息
    private SharedPreferences sp;
    private String token;
    private UserInfo.Result result;
    private class Result{
        private int code;
        private String reason;
        public int getCode() {
            return code;
        }
        public String getReason() {
            return reason;
        }
    }

    private String editInfoOldUrl = "http://www.baixinxueche.com/index.php/Home/Api/changeinfo";
    private String refreashOldUrl = "http://www.baixinxueche.com/index.php/Home/Api/refresh";
    private String editInfoUrl = "http://www.baixinxueche.com/index.php/Home/Apiinfotoken/changeinfo";
    private String refreashUrl = "http://www.baixinxueche.com/index.php/Home/Apialltoken/refresh";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personalinfo);
        getData();
       initView();
    }

    /**
     * 初始化控件
     */
    private void initView(){
        //实例化
        button_forward = (Button) findViewById(R.id.button_forward);
        button_backward = (Button) findViewById(R.id.button_backward);
        text_title = (TextView) findViewById(R.id.text_title);
        button_backward.setOnClickListener(this);
        button_forward.setOnClickListener(this);
        //修改密码
        changePassword = (TextView) findViewById(R.id.changePassword);
        changePassword.setOnClickListener(this);
        //更改标题栏
        text_title.setText("个人信息");
        button_forward.setVisibility(View.VISIBLE);
        button_backward.setVisibility(View.VISIBLE);
        //真是姓名
        realName = (TextView) findViewById(R.id.realName);
//		个人信息
        sex_id_textView = (TextView) findViewById(R.id.sex_id_textView);
        //简介
        profile_textView = (TextView) findViewById(R.id.profile_textView);
        profile_editText = (EditText) findViewById(R.id.profile_editText);
        //读取本地个人信息并设置
        realName.setText(result.getName());
        sex_id_textView.setText(result.getSex());
        profile_textView.setText(result.getIntroduce());
        nameEdit = (EditText) findViewById(R.id.nameEdit);
    }

    private void getData(){
        sp = getSharedPreferences("USER",
                Activity.MODE_PRIVATE);
        String str = sp.getString("userInfo",null);
        Gson gson = new Gson();
        UserInfo userInfo = gson.fromJson(str,UserInfo.class);
        result = userInfo.getResult();

        Intent intent = getIntent();
        token = intent.getStringExtra("token");
    }

    //性别dialog
    public void sexChoose(View view) {
        sex_dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        // 填充对话框的布局
        inflate = LayoutInflater.from(this).inflate(R.layout.sex_dialog,
                null);
        // 初始化控件
        choose_sex_man = (TextView) inflate
                .findViewById(R.id.choose_sex_man);
        choose_sex_woman = (TextView) inflate
                .findViewById(R.id.choose_sex_woman);
        choose_sex_man.setOnClickListener(this);
        choose_sex_woman.setOnClickListener(this);
        // 将布局设置给Dialog
        sex_dialog.setContentView(inflate);
        // 获取当前Activity所在的窗体
        Window dialogWindow = sex_dialog.getWindow();
        // 设置dialog横向充满
        dialogWindow.setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        /// 获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;// 设置Dialog距离底部的距离
        // 将属性设置给窗体
        dialogWindow.setAttributes(lp);
        sex_dialog.show();
    }

    private  void editInfo(String uid, String name, String jianjie, String sex){
        if(sex==null||sex.equals("")){
            sex = "男";
        }
        OkHttpUtils
                .post()
                .url(editInfoUrl)
                .addParams("uid", uid)
                .addParams("name", name)
                .addParams("sex", sex)
                .addParams("token", token)
                .addParams("introduce", jianjie)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        Toast.makeText(PersonalInfoActivity.this, "请检查网络", Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onResponse(String s, int i) {
                        Gson  gson = new Gson();
                        Result  result = gson.fromJson(s,Result.class);
                        if(result.getCode()==200){
                            Toast.makeText(PersonalInfoActivity.this, result.getReason(), Toast.LENGTH_LONG).show();
                            refreshPerInfo();
                        }else{
                            Toast.makeText(PersonalInfoActivity.this, result.getReason(), Toast.LENGTH_LONG).show();
                        }
                    }

                });
    }

    /**
     * 刷新个人中心的数据
     */
    private void refreshPerInfo(){
        OkHttpUtils
                .post()
                .url(refreashUrl)
                .addParams("uid", result.getUid()+"")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        Toast.makeText(PersonalInfoActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        Gson gson = new Gson();
                        UserInfo userInfo = gson.fromJson(s, UserInfo.class);
                        if (userInfo.getCode() == 200) {
                            /**
                             * 保存登录状态
                             */
                            SharedPreferences sp = getSharedPreferences("USER", Activity.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putInt("isfirst", 1);
                            editor.putString("userInfo", s);
                            editor.commit();
                            getData();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.changePassword:
                intent.setClass(PersonalInfoActivity.this,CallbackActivity.class);
                startActivity(intent);
                break;
            case R.id.button_backward:
                finish();
                break;
            case R.id.button_forward:
                String text = button_forward.getText().toString();
                if (text.equals("编辑")) {
                    profile_textView.setVisibility(View.GONE);
                    profile_editText.setVisibility(View.VISIBLE);
                    realName.setVisibility(View.GONE);
                    nameEdit.setVisibility(View.VISIBLE);
                    profile_editText.setText(profile_textView.getText().toString().trim());
                    nameEdit.setText(realName.getText().toString().trim());

                    button_forward.setText("提交");
                } else if (text.equals("提交")) {
                    profile_textView.setVisibility(View.VISIBLE);
                    profile_editText.setVisibility(View.GONE);
                    realName.setVisibility(View.VISIBLE);
                    nameEdit.setVisibility(View.GONE);

                    //修改框值
                    String name = nameEdit.getText().toString().trim();
                    String jianjie = profile_editText.getText().toString().trim();
                    String sex = sex_id_textView.getText().toString().trim();

                    profile_textView.setText(profile_editText.getText().toString().trim());
                    realName.setText(nameEdit.getText().toString().trim());
                    editInfo(result.getUid()+"",name,jianjie,sex);
                    button_forward.setText("编辑");
                }
                break;
            case R.id.choose_sex_man:
                editInfo(result.getUid()+"",realName.getText().toString().trim(),
                        profile_textView.getText().toString().trim(),"男");
                sex_id_textView.setText(choose_sex_man.getText().toString());
                sex_dialog.dismiss();
                break;
            case R.id.choose_sex_woman:
                editInfo(result.getUid()+"",realName.getText().toString().trim(),
                        profile_textView.getText().toString().trim(),"女");
                sex_id_textView.setText(choose_sex_woman.getText().toString());
                sex_dialog.dismiss();
                break;
        }
    }

}
