package com.jgkj.bxxc;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jgkj.bxxc.adapter.MyAdapter;
import com.jgkj.bxxc.bean.HeadlinesAction;
import com.jgkj.bxxc.bean.Picture;
import com.jgkj.bxxc.tools.AutoTextView;
import com.jgkj.bxxc.tools.SecondToDate;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;

/**
 * 首页布局
 */
public class IndexFragment2 extends Fragment implements OnClickListener {
    // LinearLayout
    private LinearLayout linearlayout;
    private View view, view1;
    // viewpager
    private ViewPager viewpager;
    // 集合list
    private List<View> list;
    // 适配器
    private MyAdapter adapter;

    // 实例化4个主button
    private TextView first_btn, fourth_btn, coach_center_btn, carsend,
            space_choose_btn, question;
    // 实例化Fragment
    private Fragment mCurrentFragment, license_Text_Fragment1,
            license_Text_Fragment2, coach;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    // 标题栏
    private TextView text_title;
    //毫秒数转化为天数
    private SecondToDate std;
    private TextView lookMore;
    private ImageView imageView, search;

    private LinearLayout aboutbaixinLayout, jiesongLayout, chengnuoLayout, liuchengLayout;

    private AutoTextView headlines;
    //图片地址
    private String url = "http://www.baixinxueche.com/index.php/Home/Apitoken/bannerpic";
    private List<String> imagePath = new ArrayList<>();
    private LinearLayout.LayoutParams wrapParams;
    private Timer timer = new Timer();
    private int currentItem = 0;
    private Runnable runnable;
    private Handler handler = new Handler();
    private String headlinesUrl = "http://www.baixinxueche.com/index.php/Home/Apitoken/nowLinesTitleAndroid";
    private List<HeadlinesAction.Result> headlinesList;
    private int headlinesCount = 0;
    private TextView bxhead;
    private HeadlinesAction action;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.index_fragment2, container, false);
        view.scrollBy(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        view1 = inflater.inflate(R.layout.title, container, false);
        init();
        getImage();
        scrollView();
        getheadlines();
        headlinesList = new ArrayList<HeadlinesAction.Result>();
        return view;
    }

    //初始化布局
    private void init() {
        //百信头条
        headlines = (AutoTextView) view.findViewById(R.id.headlines);
        headlines.setOnClickListener(this);
        headlines.setTag("nourl");
        bxhead = (TextView) view.findViewById(R.id.bxhead);
        bxhead.setOnClickListener(this);

        liuchengLayout = (LinearLayout) view.findViewById(R.id.liuchengLayout);
        aboutbaixinLayout = (LinearLayout) view.findViewById(R.id.aboutbaixinLayout);
        jiesongLayout = (LinearLayout) view.findViewById(R.id.jiesongLayout);
        chengnuoLayout = (LinearLayout) view.findViewById(R.id.chengnuoLayout);
        chengnuoLayout.setOnClickListener(this);
        liuchengLayout.setOnClickListener(this);
        aboutbaixinLayout.setOnClickListener(this);
        jiesongLayout.setOnClickListener(this);
        search = (ImageView) view1.findViewById(R.id.search);
        headlines.setText("科技改变生活，百信引领学车!");
        // 实例化控件
        linearlayout = (LinearLayout) view.findViewById(R.id.linearlayout);
        first_btn = (Button) view.findViewById(R.id.first_Text_btn);
        fourth_btn = (Button) view.findViewById(R.id.fourth_Text_btn);
        first_btn.setOnClickListener(this);
        fourth_btn.setOnClickListener(this);
        first_btn.setBackgroundResource(R.drawable.btn_background);
        first_btn.setTextColor(getResources().getColor(R.color.red));

        // 标题栏
        text_title = (TextView) view1.findViewById(R.id.text_title);
        /// 初始化一个Fragment
        license_Text_Fragment1 = new License_Text_Fragment();
        license_Text_Fragment2 = new License_Text_Fragment();
        coach = new CoachFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("index", 1);
        license_Text_Fragment1.setArguments(bundle);
        mCurrentFragment = license_Text_Fragment1;
        fragmentManager = getFragmentManager();
        viewpager = (ViewPager) view.findViewById(R.id.viewPage);
        transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.license_text_learnning, license_Text_Fragment1);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    /**
     * 百信头条轮播文字
     */
    private void getheadlines() {

        OkHttpUtils
                .post()
                .url(headlinesUrl)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        Toast.makeText(getActivity(), "网络异常，请检查网络！", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        headlines.setTag(s);
                        if (headlines.getTag() != null) {
                            setHeadlines();
                        }
                    }
                });
    }

    private void setHeadlines() {
        String headlinesTag = headlines.getTag().toString();
        Gson gson = new Gson();
        action = gson.fromJson(headlinesTag, HeadlinesAction.class);
        if (action.getCode() == 200) {
            headlinesList.addAll(action.getResult());
            runnable = new Runnable() {
                public void run() {
                    headlines.next();
                    headlines.setText(headlinesList.get(headlinesCount).getTitle());
                    headlines.setTag(headlinesList.get(headlinesCount).getUrl());
                    if (headlinesCount < (headlinesList.size()-1)) {
                        headlinesCount++;
                    } else {
                        headlinesCount = 0;
                    }
                    handler.postDelayed(this, 2000);
                }
            };
            handler.postDelayed(runnable, 2000);
        }
    }

    /**
     * 图片请求，几张图片创建相对应的viewPager+ImageView
     * 来显示图片
     */
    private void getImage() {
        OkHttpUtils
                .post()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        Toast.makeText(getActivity(), "网络状态不佳,请稍后再试！", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        Gson gson = new Gson();
                        Picture pic = gson.fromJson(s, Picture.class);
                        if (pic.getCode() == 200) {
                            final List<String> list = pic.getResult();
                            if (list != null) {
                                // 实例化listView
                                List<View> listView = new ArrayList<View>();
                                for (int k = 0; k < list.size(); k++) {
                                    imageView = new ImageView(getActivity());
                                    Glide.with(getActivity()).load(list.get(k)).placeholder(R.drawable.coach_pic).error(R.drawable.coach_pic).into(imageView);
                                    imageView.setTag(list.get(k));
                                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                                    listView.add(imageView);
                                }
                                adapter = new MyAdapter(getActivity(), listView);
                                SharedPreferences sp = getActivity().getSharedPreferences("PicCount", Activity.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putInt("Count", list.size());
                                editor.commit();
                                viewpager.setAdapter(adapter);
                            }
                        }
                    }
                });
    }

    //自动轮播
    private void scrollView() {
        SharedPreferences sp = getActivity().getSharedPreferences("PicCount", Activity.MODE_PRIVATE);
        final int count = sp.getInt("Count", -1);
        if (count != -1) {
            final ImageView[] dots = new ImageView[count];
            for (int k = 0; k < count; k++) {
                ImageView image = new ImageView(getActivity());
                image.setImageDrawable(getResources().getDrawable(R.drawable.selector));
                image.setId(k);
                wrapParams = new LinearLayout.LayoutParams(ViewPager.LayoutParams.WRAP_CONTENT, ViewPager.LayoutParams.WRAP_CONTENT);
                wrapParams.leftMargin = 5;
                image.setLayoutParams(wrapParams);
                linearlayout.addView(image);
                dots[k] = (ImageView) linearlayout.getChildAt(k);
                dots[k].setEnabled(true);
            }
            final Handler mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (currentItem < (count - 1)) {
                        currentItem++;
                        viewpager.setCurrentItem(currentItem);
                    } else if (currentItem == (count - 1)) {
                        currentItem = 0;
                        viewpager.setCurrentItem(currentItem);
                    }
                    for (int j = 0; j < count; j++) {
                        dots[j].setEnabled(false);
                    }
                    dots[currentItem].setEnabled(true);
                }
            };
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    mHandler.sendEmptyMessage(0);
                }
            };
            timer.schedule(timerTask, 1000, 3000);
        }
    }

    // fragment切换
    public void switchFragment(Fragment from, Fragment to) {
        transaction.setCustomAnimations(R.anim.switch_fragment_anim_in,
                R.anim.switch_fragment_anim_out);
        if (!to.isAdded()) {
            transaction.hide(from).add(R.id.license_text_learnning, to)
                    .addToBackStack(null).commit();
        } else {
            transaction.hide(from).show(to).commit();
        }
    }

    @Override
    public void onClick(View v) {
        fragmentManager = getFragmentManager();
        transaction = fragmentManager.beginTransaction();
        license_Text_Fragment1 = new License_Text_Fragment();
        license_Text_Fragment2 = new License_Text_Fragment();
        switch (v.getId()) {
            case R.id.first_Text_btn:
                if (mCurrentFragment != license_Text_Fragment1) {
                    first_btn.setBackgroundResource(R.drawable.btn_background);
                    first_btn.setTextColor(getResources().getColor(R.color.red));
                    fourth_btn.setBackgroundResource(R.color.white);
                    fourth_btn.setTextColor(getResources().getColor(R.color.black));
                    Bundle bundle1 = new Bundle();
                    bundle1.putInt("index", 1);
                    license_Text_Fragment1.setArguments(bundle1);
                    switchFragment(mCurrentFragment, license_Text_Fragment1);
                    mCurrentFragment = license_Text_Fragment1;
                } else {
                    Toast.makeText(getActivity(), "您已处于当前状态，请勿请勿点击", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.fourth_Text_btn:
                if (mCurrentFragment != license_Text_Fragment2) {
                    fourth_btn.setBackgroundResource(R.drawable.btn_background);
                    fourth_btn.setTextColor(getResources().getColor(R.color.red));
                    first_btn.setBackgroundResource(R.color.white);
                    first_btn.setTextColor(getResources().getColor(R.color.black));
                    Bundle bundle4 = new Bundle();
                    bundle4.putInt("index", 4);
                    license_Text_Fragment2.setArguments(bundle4);
                    switchFragment(mCurrentFragment, license_Text_Fragment2);
                    mCurrentFragment = license_Text_Fragment2;
                } else {
                    Toast.makeText(getActivity(), "您已处于当前状态，请勿请勿点击", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.aboutbaixinLayout:
                Intent img = new Intent();
                img.setClass(getActivity(), GuideActivity.class);
                startActivity(img);
                break;
            case R.id.jiesongLayout:
                Intent intent = new Intent(getActivity(), CarSendActivity.class);
                startActivity(intent);
                break;
            case R.id.chengnuoLayout:
                text_title.setText("教练");
                Intent intent3 = new Intent();
                intent3.setClass(getActivity(), HomeActivity.class);
                intent3.putExtra("FromActivity", "IndexFragment");
                startActivity(intent3);
                getActivity().finish();
                mCurrentFragment = coach;
                break;
            case R.id.liuchengLayout:
                Intent space_intent = new Intent();
                space_intent.setClass(getActivity(), PlaceChooseActivity.class);
                startActivity(space_intent);
                break;
            case R.id.headlines:
                if (headlines.getTag().toString().equals("nourl")) {
                    Toast.makeText(getActivity(), "加载中,请稍后再试!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent headlinesIntent = new Intent();
                    headlinesIntent.setClass(getActivity(),WebViewActivity.class);
                    headlinesIntent.putExtra("url",headlines.getTag().toString());
                    headlinesIntent.putExtra("title","百信头条");
                    startActivity(headlinesIntent);
                }
                break;
            case R.id.bxhead:
                Intent bxheadIntent = new Intent();
                bxheadIntent.setClass(getActivity(), HeadlinesActivity.class);
                startActivity(bxheadIntent);
                break;
        }
    }
}
