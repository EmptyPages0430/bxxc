package com.jgkj.bxxc;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
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
import com.jgkj.bxxc.bean.HistoryView;
import com.jgkj.bxxc.bean.SubTest;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.Call;

/**
 * Created by fangzhou on 2016/12/23.
 * 科目一模拟考试
 */

public class SubExamTestActivity extends Activity implements View.OnClickListener {
    private TextView title, skipToPage;
    private Button back_forward;
    private ViewPager viewPager;
    private ProgressDialog proDialog;
    private SubTest subTest;
    private OrderAdapter orderAdapter, adapter;
    private String subUrl = "http://www.baixinxueche.com/index.php/Home/Apiupdata/sendsubject";

    private int count = 0;

    //分数统计
    private int score = 0;
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
    //错题
    private List<HistoryView> errorSub, errorNew;

    private SubTest.Result results;
    //历史视图
    private List<HistoryView> hisView;
    //skip控件
    private TextView cancel, ok;
    private EditText editText;
    private int anw;
    //用户选择的答案
    private int user_Answer = 0, right_Answer;
    private Drawable drawable_right, drawable_error;

    private Dialog dialog, saveDialog;

    private TextView dialog_cancel, dialog_sure, dialog_textView;

    private List<SubTest.Result> listTest;

    private SubTest.Result result;
    //上一题下一题
    private TextView above_Question, next_Question;

    private List<String> str = new ArrayList<>();
    private View line;

    private int num;
    private int subCount = 0;

    private int randomSub;
    private List<Integer> randList = new ArrayList<>();

    private class Result {
        private int count;

        public int getCount() {
            return count;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_test);
        initView();
        getCount();

    }

    private void getTotalCount() {
        Gson gson = new Gson();
        String jsonStr = title.getTag().toString();
        Result result = gson.fromJson(jsonStr, Result.class);
        num = result.getCount();
        randList = getRandom();
        getSub(randList.get(subCount) + "");
    }

    //随机生成100道题目
    private List<Integer> getRandom() {
        List<Integer> list = new ArrayList<>();
        int sum = 0;
        for (int i = 0; i < 100; i++) {
            randomSub = new Random().nextInt(num);
            if (list.contains(randomSub)) {
                i -= 1;
                continue;
            } else {
                list.add(randomSub);
            }
        }
        return list;
    }

    //网络请求
    private void getCount() {
        OkHttpUtils
                .post()
                .url("http://www.baixinxueche.com/index.php/Home/Apiupdata/countsubject")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        Toast.makeText(SubExamTestActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        title.setTag(s);
                        if (title.getTag().toString() != null) {
                            getTotalCount();
                        } else {
                            Toast.makeText(SubExamTestActivity.this, "网络不佳请检查网络设置", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void initView() {
        title = (TextView) findViewById(R.id.text_title);
        title.setText("第1题");

        above_Question = (TextView) findViewById(R.id.above_Question);
        next_Question = (TextView) findViewById(R.id.next_Question);
        next_Question.setOnClickListener(this);
        above_Question.setVisibility(View.GONE);
        line = findViewById(R.id.line);
        line.setVisibility(View.GONE);

        back_forward = (Button) findViewById(R.id.button_backward);
        back_forward.setVisibility(View.VISIBLE);
        back_forward.setOnClickListener(this);
        skipToPage = (TextView) findViewById(R.id.skipToPage);
        skipToPage.setVisibility(View.GONE);
        viewPager = (ViewPager) findViewById(R.id.order_test_viewPager);
        //转化right和error的图标为drawable类型
        drawable_right = getResources().getDrawable(R.drawable.right);
        drawable_right.setBounds(0, 0, drawable_right.getMinimumWidth(), drawable_right.getMinimumHeight());
        drawable_error = getResources().getDrawable(R.drawable.error);
        drawable_error.setBounds(0, 0, drawable_error.getMinimumWidth(), drawable_error.getMinimumHeight());
    }

    //网络请求
    private void getSub(String id) {
        proDialog = ProgressDialog.show(SubExamTestActivity.this, null, "加载中...");
        OkHttpUtils
                .post()
                .url(subUrl)
                .addParams("id", id)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        Toast.makeText(SubExamTestActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        viewPager.setTag(s);
                        if (viewPager.getTag().toString() != null) {
                            getViewTag();
                        } else {
                            Toast.makeText(SubExamTestActivity.this, "网络不佳请稍后再试", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(SubExamTestActivity.this, subTest.getReason(), Toast.LENGTH_SHORT).show();
        }

    }

    private void addView(SubTest.Result results) {
        list = new ArrayList<View>();
        title.setText("第" + (subCount + 1) + "题");

        view = LayoutInflater.from(SubExamTestActivity.this).inflate(R.layout.first_test_order, null);
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
        Log.i("TAG","path:"+path+"----题号为："+results.getId());
        if (!path.endsWith(".jpg") && !path.endsWith(".jpeg") && !path.endsWith(".png") &&
                !path.endsWith(".GIF") && !path.endsWith(".PNG") && !path.endsWith(".JPG") && !path.endsWith(".gif")) {
            image.setVisibility(View.GONE);
        } else {
            Glide.with(SubExamTestActivity.this).load(results.getUrl()).placeholder(R.drawable.defaultimg).into(image);
        }
        list.add(view);
        orderAdapter = new OrderAdapter(SubExamTestActivity.this, list);
        viewPager.setAdapter(orderAdapter);
    }

    public void userClick(View view) {
        int user_Answer = 0;
        switch (view.getId()) {
            case R.id.answer_item1:
                if (anw == 1) {
                    answer_item1.setCompoundDrawables(null, null, drawable_right, null);
                    score += 1;
                } else {
                    answer_item1.setCompoundDrawables(null, null, drawable_error, null);
                }
                user_Answer = 1;
                break;
            case R.id.answer_item2:
                if (anw == 2) {
                    answer_item2.setCompoundDrawables(null, null, drawable_right, null);
                    score += 1;
                } else {
                    answer_item2.setCompoundDrawables(null, null, drawable_error, null);
                }
                user_Answer = 2;
                break;
            case R.id.answer_item3:
                if (anw == 3) {
                    answer_item3.setCompoundDrawables(null, null, drawable_right, null);
                    score += 1;
                } else {
                    answer_item3.setCompoundDrawables(null, null, drawable_error, null);
                }
                user_Answer = 3;
                break;
            case R.id.answer_item4:
                if (anw == 4) {
                    answer_item4.setCompoundDrawables(null, null, drawable_right, null);
                    score += 1;
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
    }

    private void showScoreDialog() {
        dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        // 填充对话框的布局
        TextView textView = new TextView(this);
        textView.setBackgroundResource(R.drawable.dialog_round);
        textView.setTextColor(getResources().getColor(R.color.themeColor));
        textView.setTextSize(25);
        textView.setGravity(Gravity.CENTER);
        if (score >= 90) {
            textView.setText(score+"分！\n恭喜您，通过了测试！");
        } else if (score < 90) {
            textView.setText(score+"分！\n很遗憾，您还需要继续努力哦！");
        }
        // 将布局设置给Dialog
        dialog.setContentView(textView);
        // 获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        // 设置dialog
        dialogWindow.setLayout(500, 300);
        // 设置Dialog从窗体中间弹出
        dialogWindow.setGravity(Gravity.CENTER);
        dialog.show();/// 显示对话框
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_backward:
                finish();
                break;
            case R.id.next_Question:
                if (next_Question.getText().toString().equals("下一题")) {
                    if ((subCount + 1) < 100) {
                        subCount++;
                        getSub(randList.get(subCount) + "");
                    } else {
                        next_Question.setText("完成");
                    }
                }else{
                    showScoreDialog();
                }
                break;
        }
    }
}