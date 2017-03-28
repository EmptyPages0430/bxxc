package com.jgkj.bxxc;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Sub1 extends Fragment implements View.OnClickListener {
    private View view;
    private Button orderTest, error_Sub, randomTest, examTest;
    private int index;
    private LinearLayout visual, traffic, gestures;
    private TextView visual1, traffic1, gestures1;
    private Button baoming;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.subject1, container, false);
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

        Drawable textimg3 = getResources().getDrawable(R.drawable.textimg3);
        textimg3.setBounds(0, 0, 50, 50);
        Drawable textimg2 = getResources().getDrawable(R.drawable.textimg2);
        textimg2.setBounds(0, 0, 50, 50);
        Drawable textimg5 = getResources().getDrawable(R.drawable.textimg5);
        textimg5.setBounds(0, 0, 50, 50);

        visual = (LinearLayout) view.findViewById(R.id.visual);
        traffic = (LinearLayout) view.findViewById(R.id.traffic);
        gestures = (LinearLayout) view.findViewById(R.id.gestures);
        gestures.setOnClickListener(this);
        traffic.setOnClickListener(this);
        visual.setOnClickListener(this);
        baoming = (Button) view.findViewById(R.id.baoming);
        baoming.setOnClickListener(this);

        visual1 = (TextView) view.findViewById(R.id.visual1);
        traffic1 = (TextView) view.findViewById(R.id.traffic1);
        gestures1 = (TextView) view.findViewById(R.id.gestures1);
        //设置左部图标
        visual1.setCompoundDrawables(textimg3, null, null, null);
        traffic1.setCompoundDrawables(textimg2, null, null, null);
        gestures1.setCompoundDrawables(textimg5, null, null, null);

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
        index = 1;

        //设置按钮顶部图标
        orderTest.setCompoundDrawables(null, shunxu, null, null);
        error_Sub.setCompoundDrawables(null, cuoti, null, null);
        examTest.setCompoundDrawables(null, suiji, null, null);
        randomTest.setCompoundDrawables(null, moni, null, null);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.orderTest:
                intent.setClass(getActivity(), SubTestActivity.class);
                startActivity(intent);
                break;
            case R.id.error_Sub:
                intent.setClass(getActivity(), SubErrorTestActivity.class);
                startActivity(intent);
                break;
            case R.id.randomTest:
                intent.setClass(getActivity(), SubRandTestActivity.class);
                startActivity(intent);
                break;
            case R.id.examTest:
                intent.setClass(getActivity(), SubExamTestActivity.class);
                startActivity(intent);
                break;
            case R.id.visual:
                intent.setClass(getActivity(), WebViewActivity.class);
                intent.putExtra("url", "http://www.baixinxueche.com/webshow/keyi/sjcs.html");
                intent.putExtra("title", "视觉测试");
                startActivity(intent);
                break;
            case R.id.traffic:
                intent.setClass(getActivity(), TrafficSignsActivity.class);
                startActivity(intent);
                break;
            case R.id.gestures:
                intent.setClass(getActivity(), WebViewActivity.class);
                intent.putExtra("url", "http://www.baixinxueche.com/webshow/keyi/jjss.html");
                intent.putExtra("title", "交警手势");
                startActivity(intent);
                break;
            case R.id.baoming:
                intent.setClass(getActivity(), HomeActivity.class);
                intent.putExtra("FromActivity", "IndexFragment");
                startActivity(intent);
                getActivity().finish();
                break;
        }

    }
}
