package com.jgkj.bxxc;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jgkj.bxxc.adapter.MyAdapter;
import com.jgkj.bxxc.adapter.MyCoachAdapter;
import com.jgkj.bxxc.bean.CoachDetailAction;
import com.jgkj.bxxc.tools.PictureOptimization;

import java.util.ArrayList;
import java.util.List;

public class SimpleCoachActivity extends Activity implements OnClickListener {

    private TextView training_filed;
    private TextView bt;

    // LinearLayout
    private LinearLayout linearlayout, dialog_layout;
    // viewpager
    private ViewPager viewpager;
    // 集合list
    private List<View> list;
    // 数组图片
    private int[] imagerIDS = {};
    // imageview代表的是选择器中图片数组
    private ImageView[] dots;

    // 适配器
    private MyAdapter adapter;

    private TextView Training_coach_textView_id, title;
    //实例化ListView,这个场地里面所有的教练
    private ListView listView;
    private MyCoachAdapter coachAdapter;
    private Bitmap bitmap;
    private CoachDetailAction coachDetailAction;
    private Bundle bundle;
    private PictureOptimization po;
    private Button button_backward;
    private View headView;
    private List<CoachDetailAction> listCoach;
    //查看更多教练
    private TextView moreCoach;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simplecoach);
        init();
    }

    private void init() {
        headView = getLayoutInflater().inflate(R.layout.simplecoach_head, null);
        //查看更多教练
        moreCoach = (TextView) headView.findViewById(R.id.moreCoach);
        moreCoach.setOnClickListener(this);
        //实例化控件
        Training_coach_textView_id = (TextView) headView.findViewById(R.id.Training_coach_textView_id);
        Training_coach_textView_id.setOnClickListener(this);
        title = (TextView) findViewById(R.id.text_title);
        title.setText("场地选择");
        button_backward = (Button) findViewById(R.id.button_backward);
        button_backward.setVisibility(View.VISIBLE);
        button_backward.setOnClickListener(this);
        //接受传值
        Intent intent = getIntent();
        String str = intent.getStringExtra("school");
        //实例化listView
        list = new ArrayList<View>();
        for (int i = 0; i < imagerIDS.length; i++) {
            //实例化listview
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(imagerIDS[i]);
            list.add(imageView);
        }
        viewpager = (ViewPager) headView.findViewById(R.id.viewPage);
        adapter = new MyAdapter(this, list);
        viewpager.setAdapter(adapter);
        //设置选择器电机时的触发事件
        initSelector();

        //设置viewpager的滑动监听
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //当图片滑动是改变selector的图片颜色
                for (int j = 0; j < dots.length; j++) {
                    dots[j].setEnabled(true);
                }
                dots[position].setEnabled(false);
            }
            @Override
            public void onPageSelected(int position) {

            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bitmap = PictureOptimization.decodeSampledBitmapFromResource(getResources(),
                R.drawable.head, 60, 60);
        listCoach = new ArrayList<>();
        listCoach.add(coachDetailAction);

        listView = (ListView) findViewById(R.id.thisSpace_coach_listView);
        listView.addHeaderView(headView);
        listView.setAdapter(coachAdapter);
    }

    //选择器点击事件
    private void initSelector() {
        linearlayout = (LinearLayout) headView.findViewById(R.id.linearlayout);
        dots = new ImageView[imagerIDS.length];
        for (int i = 0; i < dots.length; i++) {
            dots[i] = (ImageView) linearlayout.getChildAt(i);
            dots[i].setEnabled(true);
            dots[i].setTag(i);
            dots[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewpager.setCurrentItem((Integer) v.getTag());
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //教练场地一些详细信息
            case R.id.Training_coach_textView_id:
                String text = Training_coach_textView_id.getText().toString();
                final Dialog dialog = new Dialog(SimpleCoachActivity.this, R.style.ActionSheetDialogStyle);
                dialog.setContentView(R.layout.training_field_detail);

                training_filed = (TextView) dialog.findViewById(R.id.training_filed);
                training_filed.setTextColor(getResources().getColor(R.color.black));
                training_filed.setTextSize(20);
                bt = (TextView) dialog.findViewById(R.id.training_filed_textView_id);
                dialog_layout = (LinearLayout) dialog.findViewById(R.id.dialog_layout);

                bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                Window dialogWindow = dialog.getWindow();
                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                lp.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
                lp.horizontalMargin = 80;
                dialog_layout.setBackgroundResource(R.drawable.dialog_round);
                // 当Window的Attributes改变时系统会调用此函数,可以直接调用以应用上面对窗口参数的更改,
                // 也可以用setAttributes
                // dialog.onWindowAttributesChanged(lp);
                dialogWindow.setAttributes(lp);
                training_filed.setText(text.replaceAll(" ", ""));
                dialog.show();
                break;
            case R.id.button_backward:
                finish();
                break;
            case R.id.moreCoach:
                Intent intent = new Intent();
                intent.setClass(SimpleCoachActivity.this,HomeActivity.class);
                intent.putExtra("FromActivity","SimpleCoachActivity");
                startActivity(intent);
                break;
        }
    }
}
