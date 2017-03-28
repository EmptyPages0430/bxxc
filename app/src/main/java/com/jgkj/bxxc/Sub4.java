package com.jgkj.bxxc;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jgkj.bxxc.tools.AutoTextView;

public class Sub4 extends Fragment implements View.OnClickListener {
    private View view;
    private Button orderTest, error_Sub, randomTest, examTest;
    private int index;
    private LinearLayout inner_function, dashboard;
    private TextView inner_function1, dashboard1;
    private AutoTextView showNum;
    private ImageView boom;
    private Runnable runnable;
    private Handler handler = new Handler();
    private int[] phoneSt = new int[]{130, 131, 132, 133, 134, 135, 136, 137, 138, 139,
            145, 147, 150, 151, 152, 153, 155, 156, 157, 158, 159, 180, 181,
            182, 183, 185, 186, 187, 188, 189};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.subject4, container, false);
        init();
        return view;
    }

    /**
     * 初始化布局
     */
    private void init() {

        Drawable shunxu = getResources().getDrawable(R.drawable.license_text1);
        shunxu.setBounds(0, 0, 100, 100);
        Drawable cuoti = getResources().getDrawable(R.drawable.license_text2);
        cuoti.setBounds(0, 0, 100, 100);
        Drawable suiji = getResources().getDrawable(R.drawable.license_text4);
        suiji.setBounds(0, 0, 100, 100);
        Drawable moni = getResources().getDrawable(R.drawable.license_text3);
        moni.setBounds(0, 0, 100, 100);

        Drawable textimg1 = getResources().getDrawable(R.drawable.textimg1);
        textimg1.setBounds(0, 0, 50, 50);
        Drawable textimg4 = getResources().getDrawable(R.drawable.textimg4);
        textimg4.setBounds(0, 0, 50, 50);

        inner_function1 = (TextView) view.findViewById(R.id.inner_function1);
        dashboard1 = (TextView) view.findViewById(R.id.dashboard1);
        //设置左部图标
        inner_function1.setCompoundDrawables(textimg1, null, null, null);
        dashboard1.setCompoundDrawables(textimg4, null, null, null);

        inner_function = (LinearLayout) view.findViewById(R.id.inner_function);
        dashboard = (LinearLayout) view.findViewById(R.id.dashboard);
        dashboard.setOnClickListener(this);
        inner_function.setOnClickListener(this);

        boom = (ImageView) view.findViewById(R.id.boom);
        Glide.with(getActivity()).load(R.drawable.boom).into(boom);

        showNum = (AutoTextView) view.findViewById(R.id.showNum);
        showNum.setText("恭喜 " + getNum() + " 学员 顺利拿证!");
        setShowNum();

        //顺序练题
        orderTest = (Button) view.findViewById(R.id.orderTest);
        orderTest.setOnClickListener(this);
        //错题
        error_Sub = (Button) view.findViewById(R.id.error_Sub);
        error_Sub.setOnClickListener(this);
        //随机练题
        randomTest = (Button) view.findViewById(R.id.randomTest);
        randomTest.setOnClickListener(this);
        //模拟考试
        examTest = (Button) view.findViewById(R.id.examTest);
        examTest.setOnClickListener(this);
        index = 4;
        //设置按钮顶部图标
        orderTest.setCompoundDrawables(null, shunxu, null, null);
        error_Sub.setCompoundDrawables(null, cuoti, null, null);
        examTest.setCompoundDrawables(null, suiji, null, null);
        randomTest.setCompoundDrawables(null, moni, null, null);

    }

    private void setShowNum() {
        runnable = new Runnable() {
            public void run() {
                showNum.next();
                showNum.setText("恭喜 " + getNum() + " 学员 顺利拿证!");
                handler.postDelayed(this, 2000);
            }
        };
        handler.postDelayed(runnable, 5000);
    }

    private String getNum() {

        int num1 = (int) (Math.random() * 29);
        int num2 = (int) (Math.random() * 9999);
        return phoneSt[num1] + "****" + num2;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.orderTest:
                intent.setClass(getActivity(), SubFourTestActivity.class);
                startActivity(intent);
                break;
            case R.id.error_Sub:
                intent.setClass(getActivity(), SubfourErrorTestActivity.class);
                startActivity(intent);
                break;
            case R.id.randomTest:
                intent.setClass(getActivity(), SubFourRandTestActivity.class);
                startActivity(intent);
                break;
            case R.id.examTest:
                intent.setClass(getActivity(), SubFourExamTestActivity.class);
                startActivity(intent);
                break;
            case R.id.inner_function:
                intent.setClass(getActivity(), WebViewActivity.class);
                intent.putExtra("url", "http://www.baixinxueche.com/webshow/kesi/gnj.html");
                intent.putExtra("title", "车内功能键");
                startActivity(intent);
                break;
            case R.id.dashboard:
                intent.setClass(getActivity(), WebViewActivity.class);
                intent.putExtra("url", "http://www.baixinxueche.com/webshow/kesi/ybp.html");
                intent.putExtra("title", "汽车仪表盘");
                startActivity(intent);
                break;
        }
    }
}
