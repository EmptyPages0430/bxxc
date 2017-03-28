package com.jgkj.bxxc;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jgkj.bxxc.adapter.OrderAdapter;
import com.jgkj.bxxc.bean.ErrorMsg;
import com.jgkj.bxxc.bean.SubTest;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by fangzhou on 2016/12/23.
 * 科目一模拟考试
 */

public class SubFourExamTestActivity extends Activity implements View.OnClickListener {
    private TextView title, skipToPage;
    private Button back_forward;
    private ViewPager viewPager;
    private ProgressDialog proDialog;
    private SubTest subTest;
    private OrderAdapter orderAdapter, adapter;
    private String panUrl = "http://www.baixinxueche.com/index.php/Home/Apiupdata/choosefourPan";
    private String singleUrl = "http://www.baixinxueche.com/index.php/Home/Apiupdata/choosefourDan";
    private String duoUrl = "http://www.baixinxueche.com/index.php/Home/Apiupdata/choosefourDuo";
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
    private Button sureAnswer;
    private List<View> list;
    private View view;
    private LinearLayout detail;

    private String anw;
    private Drawable drawable_right, drawable_error, check_background;
    private boolean anw1Flag = false;
    private boolean anw2Flag = false;
    private boolean anw3Flag = false;
    private boolean anw4Flag = false;
    private List<String> userAnw = new ArrayList<>();
    private ErrorMsg errorSub;
    private Dialog dialog;

    //上一题下一题
    private TextView above_Question, next_Question;

    /**
     * 自动保存错题
     */
    private List<ErrorMsg.Result> str;

    private View line;

    //当前题号
    private int currentPage = 1;

    private List<String> countNum = new ArrayList<>();

    //答案
    private List<String> arr;
    //正确个数
    private int countAns = 0;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

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
        getSub(panUrl);
    }

    private void initView() {
        title = (TextView) findViewById(R.id.text_title);
        title.setText("第"+currentPage+"题");

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
        check_background = getResources().getDrawable(R.drawable.check_background);
        check_background.setBounds(0, 0, check_background.getMinimumWidth(), check_background.getMinimumHeight());

        sp = getSharedPreferences("error_test", Activity.MODE_PRIVATE);
        String jsonStr = sp.getString("error_code", null);
        if (jsonStr == null || jsonStr == "") {
            str = new ArrayList<>();
            errorSub = new ErrorMsg();
        } else {
            Gson gson = new Gson();
            errorSub = gson.fromJson(jsonStr, ErrorMsg.class);
            str = errorSub.getResult();
        }
    }

    /**
     * 网络请求,
     * @param url 请求地址，这里传值是为了区别多选，单选和判断题
     */
    private void getSub(String url) {
        proDialog = ProgressDialog.show(SubFourExamTestActivity.this, null, "加载中...");
        OkHttpUtils
                .post()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        Toast.makeText(SubFourExamTestActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        viewPager.setTag(s);
                        if (viewPager.getTag().toString() != null) {
                            getViewTag();
                        } else {
                            Toast.makeText(SubFourExamTestActivity.this, "网络不佳请稍后再试", Toast.LENGTH_SHORT).show();
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
            if(!countNum.contains(subTest.getResult().getId())){
                addView(subTest.getResult());
                countNum.add(subTest.getResult().getId());
            }else{
                if ((currentPage) < 21) {
                    getSub(panUrl);
                }else if ((currentPage) < 45&&(currentPage) >= 21) {
                    getSub(singleUrl);
                }else if ((currentPage) < 50&&(currentPage) >=45) {
                    getSub(duoUrl);
                }
            }
        } else {
            Toast.makeText(SubFourExamTestActivity.this, subTest.getReason(), Toast.LENGTH_SHORT).show();
        }

    }

    private void addView(SubTest.Result results) {
        list = new ArrayList<View>();
        title.setText("第" + currentPage + "题");
        userAnw.clear();
        String rightAnw = "";
        countAns = 0;
        view = LayoutInflater.from(SubFourExamTestActivity.this).inflate(R.layout.first_test_order, null);
        image = (ImageView) view.findViewById(R.id.test_imageView);
        order_quester = (TextView) view.findViewById(R.id.order_quester);
        explain = (TextView) view.findViewById(R.id.explain);
        answer_item1 = (TextView) view.findViewById(R.id.answer_item1);
        answer_item2 = (TextView) view.findViewById(R.id.answer_item2);
        answer_item3 = (TextView) view.findViewById(R.id.answer_item3);
        answer_item4 = (TextView) view.findViewById(R.id.answer_item4);
        sureAnswer = (Button) view.findViewById(R.id.sureAnswer);
        sureAnswer.setVisibility(View.VISIBLE);
        sureAnswer.setOnClickListener(this);
        answer_item1.setOnClickListener(this);
        answer_item2.setOnClickListener(this);
        answer_item3.setOnClickListener(this);
        answer_item4.setOnClickListener(this);
        detail = (LinearLayout) view.findViewById(R.id.detail);

        anw = results.getAnswer();
        arr = new ArrayList();
        for (int i = 0; i < anw.length(); i++) {
            arr.add(anw.substring(i, i + 1));
        }
        if ((currentPage) < 21) {
            title.setText("第" + currentPage + "题(判断题)");
        }else if ((currentPage) < 45&&(currentPage) >= 21) {
            title.setText("第" + currentPage + "题(单选题)");
        }else if ((currentPage) < 50&&(currentPage) >=45) {
            title.setText("第" + currentPage + "题(多选题)");
        }
        if(arr.contains("A")){
            rightAnw = rightAnw+"A";
        }
        if(arr.contains("B")){
            rightAnw = rightAnw+"B";
        }
        if(arr.contains("C")){
            rightAnw = rightAnw+"C";
        }
        if(arr.contains("D")){
            rightAnw = rightAnw+"D";
        }
        explain.setText("正确答案为：" +rightAnw+"。"+ results.getExplains());
        order_quester.setText(results.getQuestion());
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
        if (!path.endsWith(".jpg") && !path.endsWith(".jpeg") && !path.endsWith(".png") &&
                !path.endsWith(".GIF") && !path.endsWith(".PNG") && !path.endsWith(".JPG") && !path.endsWith(".gif")) {
            image.setVisibility(View.GONE);
        } else {
            Glide.with(SubFourExamTestActivity.this).load(results.getUrl()).into(image);
        }
        list.add(view);
        orderAdapter = new OrderAdapter(SubFourExamTestActivity.this, list);
        viewPager.setAdapter(orderAdapter);
    }

    /**
     *
     * 检查答案是否正确
     * @return
     */
    private void checkAnw() {
        if (userAnw.contains("A")) {
            if (arr.contains("A")) {
                countAns++;
            } else {
                answer_item1.setCompoundDrawables(null, null, drawable_error, null);
            }
        }
        if (userAnw.contains("B")) {
            if (arr.contains("B")) {
                countAns++;
            } else {
                answer_item2.setCompoundDrawables(null, null, drawable_error, null);
            }
        }
        if (userAnw.contains("C")) {
            if (arr.contains("C")) {
                countAns++;
            } else {
                answer_item3.setCompoundDrawables(null, null, drawable_error, null);
            }
        }
        if (userAnw.contains("D")) {
            if (arr.contains("D")) {
                countAns++;
            } else {
                answer_item4.setCompoundDrawables(null, null, drawable_error, null);
            }
        }
        explain.setVisibility(View.VISIBLE);
        detail.setVisibility(View.VISIBLE);
        //自动保存错题数据
        ErrorMsg result = new ErrorMsg();
        ErrorMsg.Result res = result.new Result();
        res.setSubCount(count + "");
        if (countAns!= arr.size()) {
            str.add(res);
        } else {
            score = score+2;
        }
        answer_item1.setEnabled(false);
        answer_item2.setEnabled(false);
        answer_item3.setEnabled(false);
        answer_item4.setEnabled(false);

        sp = getSharedPreferences("error_fourtest", Activity.MODE_PRIVATE);
        editor = sp.edit();
        errorSub.setResult(str);
        editor.putString("error_fourcode", new Gson().toJson(errorSub));
        editor.commit();
    }


    /**
     * 计算分数
     */
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
            case R.id.sureAnswer:
                checkAnw();
                sureAnswer.setVisibility(View.GONE);
                break;
            case R.id.next_Question:
                if (next_Question.getText().toString().equals("下一题")) {
                    if ((currentPage) < 21) {
                        currentPage++;
                        getSub(panUrl);
                    }else if ((currentPage) < 45&&(currentPage) >= 21) {
                        currentPage++;
                        getSub(singleUrl);
                    }else if ((currentPage) < 50&&(currentPage) >=45) {
                        currentPage++;
                        getSub(duoUrl);
                    } else {
                        next_Question.setText("完成");
                    }
                }else{
                    showScoreDialog();
                }
                break;
            case R.id.answer_item1:
                if (!anw1Flag) {
                    answer_item1.setCompoundDrawables(null, null, drawable_right, null);
                    anw1Flag = true;
                    userAnw.add("A");
                } else {
                    answer_item1.setCompoundDrawables(null, null, check_background, null);
                    anw1Flag = false;
                    userAnw.remove("A");
                }
                break;
            case R.id.answer_item2:
                if (!anw2Flag) {
                    answer_item2.setCompoundDrawables(null, null, drawable_right, null);
                    anw2Flag = true;
                    userAnw.add("B");
                } else {
                    answer_item2.setCompoundDrawables(null, null, check_background, null);
                    anw2Flag = false;
                    userAnw.remove("B");
                }
                break;
            case R.id.answer_item3:
                if (!anw3Flag) {
                    answer_item3.setCompoundDrawables(null, null, drawable_right, null);
                    anw3Flag = true;
                    userAnw.add("C");
                } else {
                    answer_item3.setCompoundDrawables(null, null, check_background, null);
                    anw3Flag = false;
                    userAnw.remove("C");
                }
                break;
            case R.id.answer_item4:
                if (!anw4Flag) {
                    answer_item4.setCompoundDrawables(null, null, drawable_right, null);
                    anw4Flag = true;
                    userAnw.add("D");
                } else {
                    answer_item4.setCompoundDrawables(null, null, check_background, null);
                    anw4Flag = false;
                    userAnw.remove("D");
                }
                break;
        }
    }
}