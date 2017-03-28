package com.jgkj.bxxc;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jgkj.bxxc.adapter.OrderAdapter;
import com.jgkj.bxxc.bean.ErrorMsg;
import com.jgkj.bxxc.bean.HistoryView;
import com.jgkj.bxxc.bean.SubTest;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by fangzhou on 2016/12/23.
 * 科目一考题
 */

public class SubTestActivity extends Activity implements View.OnClickListener {
    private TextView title, skipToPage;
    private Button back_forward;
    private ViewPager viewPager;
    private ProgressDialog proDialog;
    private SubTest subTest;
    private OrderAdapter orderAdapter, adapter;
    private String subUrl = "http://www.baixinxueche.com/index.php/Home/Apiupdata/sendsubject";

    private int count = 1;
    private ImageView image;
    private TextView order_quester;
    private TextView explain;
    private TextView answer_item1;
    private TextView answer_item2;
    private TextView answer_item3;
    private TextView answer_item4;
    private List<View> list;
    private View view, his_view;
    private LinearLayout detail;

    private SubTest.Result results;
    //历史视图
    private List<HistoryView> hisView;
    //skip控件
    private TextView cancel, ok;
    private EditText editText;
    private int anw;
    //用户选择的答案
    private Drawable drawable_right, drawable_error;

    private Dialog dialog, saveDialog;

    private TextView dialog_cancel, dialog_sure, dialog_textView;

    private List<SubTest.Result> listTest;

    private SubTest.Result result;
    //上一题下一题
    private TextView above_Question, next_Question;

    private List<ErrorMsg.Result> str;
    private ErrorMsg errorSub;

    private List<View> viewList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_test);
        initView();
        SharedPreferences sp = getSharedPreferences("last_sub", Activity.MODE_PRIVATE);
        count = sp.getInt("LastSub",1);
        getSub(count + "");
    }

    private void initView() {
        title = (TextView) findViewById(R.id.text_title);
        title.setText("第1题");

        above_Question = (TextView) findViewById(R.id.above_Question);
        next_Question = (TextView) findViewById(R.id.next_Question);
        next_Question.setOnClickListener(this);
        above_Question.setOnClickListener(this);

        back_forward = (Button) findViewById(R.id.button_backward);
        back_forward.setVisibility(View.VISIBLE);
        back_forward.setOnClickListener(this);
        skipToPage = (TextView) findViewById(R.id.skipToPage);
        skipToPage.setOnClickListener(this);
        viewPager = (ViewPager) findViewById(R.id.order_test_viewPager);
        //转化right和error的图标为drawable类型
        drawable_right = getResources().getDrawable(R.drawable.right);
        drawable_right.setBounds(0, 0, drawable_right.getMinimumWidth(), drawable_right.getMinimumHeight());
        drawable_error = getResources().getDrawable(R.drawable.error);
        drawable_error.setBounds(0, 0, drawable_error.getMinimumWidth(), drawable_error.getMinimumHeight());
        SharedPreferences sp = getSharedPreferences("error_test", Activity.MODE_PRIVATE);
        String jsonStr = sp.getString("error_code",null);
        if(jsonStr==null||jsonStr==""){
            str = new ArrayList<>() ;
            errorSub = new ErrorMsg();
        }else{
            Gson gson = new Gson();
            errorSub = gson.fromJson(jsonStr,ErrorMsg.class);
            str = errorSub.getResult();
        }
    }

    //网络请求,根据题号加载题目内容
    private void getSub(String id) {
        proDialog = ProgressDialog.show(SubTestActivity.this, null, "加载中...");
        OkHttpUtils
                .post()
                .url(subUrl)
                .addParams("id", id)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        Toast.makeText(SubTestActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onResponse(String s, int i) {
                        viewPager.setTag(s);
                        if (viewPager.getTag().toString() != null) {
                            getViewTag();
                        } else {
                            Toast.makeText(SubTestActivity.this, "网络不佳请稍后再试", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void getViewTag() {
        String str = viewPager.getTag().toString();
        Gson gson = new Gson();
        subTest = gson.fromJson(str, SubTest.class);
        proDialog.dismiss();
        if (subTest.getCode() == 200) {
            count = Integer.parseInt(subTest.getResult().getId());
            addView(subTest.getResult());
        } else {
            Toast.makeText(SubTestActivity.this, subTest.getReason(), Toast.LENGTH_SHORT).show();
        }

    }

    //实例化每个viewPage的页面控件，并且填充数据
    private void addView(SubTest.Result results) {
        list = new ArrayList<View>();
        title.setText("第" + results.getId() + "题");

        view = LayoutInflater.from(SubTestActivity.this).inflate(R.layout.first_test_order, null);
        image = (ImageView) view.findViewById(R.id.test_imageView);
        order_quester = (TextView) view.findViewById(R.id.order_quester);
        explain = (TextView) view.findViewById(R.id.explain);
        answer_item1 = (TextView) view.findViewById(R.id.answer_item1);
        answer_item2 = (TextView) view.findViewById(R.id.answer_item2);
        answer_item3 = (TextView) view.findViewById(R.id.answer_item3);
        answer_item4 = (TextView) view.findViewById(R.id.answer_item4);
        detail = (LinearLayout) view.findViewById(R.id.detail);

        anw = Integer.parseInt(results.getAnswer());

        order_quester.setText(results.getQuestion());
        if(results.getAnswer().equals("1")){
            explain.setText("正确答案为：A。"+results.getExplains());
        }else if(results.getAnswer().equals("2")){
            explain.setText("正确答案为：B。"+results.getExplains());
        }else if(results.getAnswer().equals("3")){
            explain.setText("正确答案为：C。"+results.getExplains());
        }else if(results.getAnswer().equals("4")){
            explain.setText("正确答案为：D。"+results.getExplains());
        }
        answer_item1.setText("A:"+results.getItem1());
        answer_item2.setText("B:"+results.getItem2());

        if(results.getItem3().equals("")||results.getItem3()==null||
                results.getItem4().equals("")||results.getItem4()==null){
            answer_item3.setVisibility(View.GONE);
            answer_item4.setVisibility(View.GONE);
        }

        answer_item3.setText("C:"+results.getItem3());
        answer_item4.setText("D:"+results.getItem4());
        String path = results.getUrl();
        if(!path.endsWith(".jpg")&&!path.endsWith(".jpeg")&&!path.endsWith(".png")&&
                !path.endsWith(".GIF")&&!path.endsWith(".PNG") &&!path.endsWith(".JPG")&&!path.endsWith(".gif")){
            image.setVisibility(View.GONE);
        }else{
            Glide.with(SubTestActivity.this).load(results.getUrl()).into(image);
        }
        list.add(view);
        orderAdapter = new OrderAdapter(SubTestActivity.this, list);
        viewPager.setAdapter(orderAdapter);
    }

    public void userClick(View view) {
        int user_Answer = 0;
        switch (view.getId()) {
            case R.id.answer_item1:
                if (anw == 1) {
                    answer_item1.setCompoundDrawables(null, null, drawable_right, null);
                } else {
                    answer_item1.setCompoundDrawables(null, null, drawable_error, null);
                }
                user_Answer = 1;
                break;
            case R.id.answer_item2:
                if (anw == 2) {
                    answer_item2.setCompoundDrawables(null, null, drawable_right, null);
                } else {
                    answer_item2.setCompoundDrawables(null, null, drawable_error, null);
                }
                user_Answer = 2;
                break;
            case R.id.answer_item3:
                if (anw == 3) {
                    answer_item3.setCompoundDrawables(null, null, drawable_right, null);
                } else {
                    answer_item3.setCompoundDrawables(null, null, drawable_error, null);
                }
                user_Answer = 3;
                break;
            case R.id.answer_item4:
                if (anw == 4) {
                    answer_item4.setCompoundDrawables(null, null, drawable_right, null);
                } else {
                    answer_item4.setCompoundDrawables(null, null, drawable_error, null);
                }
                user_Answer = 4;
                break;
        }
        explain.setVisibility(View.VISIBLE);
        detail.setVisibility(View.VISIBLE);
        answer_item2.setEnabled(false);
        answer_item1.setEnabled(false);
        answer_item3.setEnabled(false);
        answer_item4.setEnabled(false);
        //自动保存错题数据
        SharedPreferences sp = getSharedPreferences("error_test", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        ErrorMsg result = new ErrorMsg();
        ErrorMsg.Result res = result.new Result();
        res.setSubCount(count+"");
        if (user_Answer != anw) {
            str.add(res);
        } else {
            for(int i=0;i<str.size();i++){
                if(str.get(i).getSubCount().equals(count+"")){
                    str.remove(i);
                }
            }
        }
        errorSub.setResult(str);
        editor.putString("error_code", new Gson().toJson(errorSub));
        editor.commit();
    }

    //是否保存做题进度弹窗
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void saveDialog() {
        saveDialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(this).inflate(R.layout.sure_cancel_dialog,
                null);
        dialog_textView = (TextView) inflate.findViewById(R.id.dialog_textView);
        dialog_cancel = (TextView) inflate.findViewById(R.id.dialog_cancel);
        dialog_sure = (TextView) inflate.findViewById(R.id.dialog_sure);
        dialog_textView.setText("是否保存做题进度？");

        dialog_sure.setOnClickListener(this);
        dialog_cancel.setOnClickListener(this);
        saveDialog.setContentView(inflate);
        Window dialogWindow = saveDialog.getWindow();
        dialogWindow.setLayout(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogWindow.setGravity(Gravity.CENTER);
        saveDialog.show();//  显示对话框
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_backward:
                saveDialog();
                break;
            case R.id.above_Question:
                getSub(count -1+ "");
                break;
            case R.id.next_Question:
                getSub(count +1+ "");
                break;
            case R.id.skipToPage:
                dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
                // 填充对话框的布局
                View inflate = LayoutInflater.from(this).inflate(R.layout.skiptopage,
                        null);
                // 初始化控件
                ok = (TextView) inflate.findViewById(R.id.ok);
                cancel = (TextView) inflate.findViewById(R.id.cancel);
                editText = (EditText) inflate.findViewById(R.id.editText);
                ok.setOnClickListener(this);
                cancel.setOnClickListener(this);
                // 将布局设置给Dialog
                dialog.setContentView(inflate);
                // 获取当前Activity所在的窗体
                Window dialogWindow = dialog.getWindow();
                // 设置dialog横向充满
                dialogWindow.setLayout(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                        android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
                // 设置Dialog从窗体中间弹出
                dialogWindow.setGravity(Gravity.CENTER);
                dialog.show();/// 显示对话框
                break;
            case R.id.cancel:
                dialog.dismiss();
            case R.id.ok:
                dialog.dismiss();
                if(editText.getText().toString().trim()!=null||!editText.getText().toString().trim().equals("")){
                    getSub(editText.getText().toString().trim());
                }else{
                    Toast.makeText(SubTestActivity.this,"您输入题号有误", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.dialog_sure:
                SharedPreferences sp = getSharedPreferences("last_sub", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("LastSub", count);
                editor.commit();
                finish();
                break;
            case R.id.dialog_cancel:
                SharedPreferences sp1 = getSharedPreferences("last_sub", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor1 = sp1.edit();
                editor1.clear();
                editor1.commit();
                finish();
                break;
        }
    }
    //返回键监听，当点击手机自带的返回键时，同样会弹出窗口
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            saveDialog();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
