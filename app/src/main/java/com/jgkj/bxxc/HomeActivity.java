package com.jgkj.bxxc;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jgkj.bxxc.bean.CoachDetailAction;
import com.jgkj.bxxc.bean.SchoolAction;
import com.jgkj.bxxc.bean.Version;
import com.jgkj.bxxc.tools.GetVersion;
import com.jgkj.bxxc.tools.JPushDataUitl;
import com.jgkj.bxxc.tools.SelectPopupWindow;
import com.jgkj.bxxc.tools.UpdateManger;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Date;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;

public class HomeActivity extends FragmentActivity implements OnClickListener {
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private RadioButton radioButton1, radioButton2, radioButton3,radioButton4;
    private Fragment mCurrentFragment, per, index, my_set, coach, map,study;
    private TextView text_title;
    private ScrollView scroll_bar;
    private FrameLayout frame, car_frameLayout;
    private static String[] school = {"越达驾校(新周谷堆校区)", "越达驾校(包河区第一校区)",
            "越达驾校(大学城中心校区)", "越达驾校(蜀山区新华校区)", "越达驾校(庐阳区总校区)"};
    private static String[] cityInfo = {"合肥"};
    //popupWindow
    private SelectPopupWindow mPopupWindow = null;
    private String[] city = {"合肥"};
    private String[][] datialPlace = {
            {"新周谷堆校区", "包河区第一校区", "大学城中心校区", "蜀山区新华校区", "庐阳区总校区"}
    };
    private Bundle bundle;
    private SchoolAction schoolAction;

    private Dialog dialog;
    //搜索按钮
    private ImageView search;
    private View inflate;
    private CarSendActivity carSendMap;
    // 定义一个变量，来标识是否退出
    private static boolean isExit = false;
    //判断调过来的Activity
    private String fromActivity = null;
    private EditText mEtSearch = null;// 输入搜索内容
    private Button mBtnClearSearchText = null;// 清空搜索信息的按钮
    private LinearLayout mLayoutClearSearchText = null;

    //jPush推送
    private EditText msgText;
    public final static int CLOSE_ACTIVITY = 1001;
    public final static int TOUCH_DOWN = 1002;
    public static boolean isForeground = false;

    private String searchUrl = "http://www.baixinxueche.com/index.php/Home/Apitoken/like";
    private String versionUrl = "http://www.baixinxueche.com/index.php/Home/Apitoken/versionandroid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        JPushInterface.init(getApplicationContext());
        registerMessageReceiver();
        isClearLoginSession();
        checkSoftInfo();
    }

    /**
     * 做本地判断，如果上次运行app是三天前，将会清空登录状态
     * 如果上次运行app是三天内，那么覆盖userSessionTime
     */
    private void isClearLoginSession() {
        SharedPreferences sp2 = getSharedPreferences("UserLoginSession", Activity.MODE_PRIVATE);
        SharedPreferences sp = getSharedPreferences("USER", Activity.MODE_PRIVATE);
        Long currentTime = new Date().getTime();
        Long loginTime = sp2.getLong("userSessionTime",currentTime);
        if((currentTime-loginTime)>259200000){
            SharedPreferences.Editor editor = sp.edit();
            editor.clear();
            editor.commit();
        }else{
            SharedPreferences.Editor editor2 = sp2.edit();
            editor2.putLong("userSessionTime",currentTime);
            editor2.commit();
        }
    }

    /**
     * 检查版本更新
     */
    private void checkSoftInfo() {
        OkHttpUtils
                .get()
                .url(versionUrl)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        Toast.makeText(HomeActivity.this, "请检查网络", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        Gson gson = new Gson();
                        Version version = gson.fromJson(s, Version.class);
                        if (version.getCode() == 200) {
                            if (version.getResult().get(0).getVersionCode() > GetVersion.getVersionCode(HomeActivity.this)) {
                                UpdateManger updateManger = new UpdateManger(HomeActivity.this,
                                        version.getResult().get(0).getPath(), version.getResult().get(0).getVersionName());
                                updateManger.checkUpdateInfo();
                            }
                        }
                    }
                });
    }

    // 初始化控件及部分方法
    private void init() {
        Drawable rbImg1 = getResources().getDrawable(R.drawable.selector_home_bottom);
        rbImg1.setBounds(0, 0, 40, 40);
        Drawable rbImg2 = getResources().getDrawable(R.drawable.selector_coach_bottom);
        rbImg2.setBounds(0, 0, 40, 40);
        Drawable rbImg3 = getResources().getDrawable(R.drawable.selector_study_bottom);
        rbImg3.setBounds(0, 0, 40, 40);
        Drawable rbImg4 = getResources().getDrawable(R.drawable.selector_me_bottom);
        rbImg4.setBounds(0, 0, 40, 40);


        search = (ImageView) findViewById(R.id.search);
        search.setOnClickListener(this);
        car_frameLayout = (FrameLayout) findViewById(R.id.car_send_map);
        mCurrentFragment = index;
        frame = (FrameLayout) findViewById(R.id.index_fragment_layout);
        // 实例化按钮
        radioButton1 = (RadioButton) findViewById(R.id.radio_button_01);
        radioButton2 = (RadioButton) findViewById(R.id.radio_button_02);//radio_button_03
        radioButton4 = (RadioButton) findViewById(R.id.radio_button_04);
        radioButton3 = (RadioButton) findViewById(R.id.radio_button_03);

        text_title = (TextView) findViewById(R.id.text_title);
        scroll_bar = (ScrollView) findViewById(R.id.scroll_bar);
        scroll_bar.smoothScrollTo(0, 0);
        // 设置监听
        radioButton1.setOnClickListener(this);
        radioButton2.setOnClickListener(this);
        radioButton4.setOnClickListener(this);
        radioButton3.setOnClickListener(this);

        //设置按钮顶部图标
        radioButton1.setCompoundDrawables(null,rbImg1,null,null);
        radioButton2.setCompoundDrawables(null,rbImg2,null,null);
        radioButton3.setCompoundDrawables(null,rbImg3,null,null);
        radioButton4.setCompoundDrawables(null,rbImg4,null,null);

        // 初始化一个fragment填充首页
        IndexFragment indexFragment = new IndexFragment();
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        CoachFragment coach = new CoachFragment();
        my_set = new My_Setting_Fragment();
        study = new StudyFragment();
        Intent intent = getIntent();
        fromActivity = intent.getStringExtra("FromActivity");
        if (fromActivity.equals("WelcomeActivity") || fromActivity.equals("LoginActivity")) {
            transaction.add(R.id.index_fragment_layout, indexFragment);
        } else if (fromActivity.equals("SimpleCoachActivity") || fromActivity.equals("IndexFragment")) {
            text_title.setText("教练中心");
            search.setVisibility(View.VISIBLE);
            radioButton2.setChecked(true);
            scroll_bar.setVisibility(View.GONE);
            car_frameLayout.setVisibility(View.VISIBLE);
            transaction.add(R.id.car_send_map, coach);
        } else if (fromActivity.equals("MySetting")) {
            text_title.setText("我的资料");
            radioButton4.setChecked(true);
            scroll_bar.setVisibility(View.GONE);
            car_frameLayout.setVisibility(View.VISIBLE);
            transaction.add(R.id.car_send_map, my_set);
        }
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void creatDialog() {
        dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        // 填充对话框的布局
        View inflate = LayoutInflater.from(this).inflate(R.layout.search_dialog,
                null);
        // 初始化控件
        mEtSearch = (EditText) inflate.findViewById(R.id.et_search);
        mBtnClearSearchText = (Button) inflate.findViewById(R.id.btn_clear_search_text);
        mLayoutClearSearchText = (LinearLayout) inflate.findViewById(R.id.layout_clear_search_text);
        mEtSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int textLength = mEtSearch.getText().length();
                if (textLength > 0) {
                    mLayoutClearSearchText.setVisibility(View.VISIBLE);
                } else {
                    mLayoutClearSearchText.setVisibility(View.GONE);
                }
            }
        });

        mBtnClearSearchText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mEtSearch.setText("");
                mLayoutClearSearchText.setVisibility(View.GONE);
            }
        });
        mEtSearch.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View arg0, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    search(mEtSearch.getText().toString().trim(), "1");
                }
                return false;
            }
        });
        // 将布局设置给Dialog
        dialog.setContentView(inflate);
        // 获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        // 设置dialog横向充满
        dialogWindow.setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置Dialog从窗体中间弹出
        dialogWindow.setGravity(Gravity.TOP);
        dialog.show();/// 显示对话框
    }

    /**
     * 教练中心页面模糊查找
     *
     * @param str 编辑框输出的文字
     * @param searchPage 页数
     */
    private void search(String str, String searchPage) {
        OkHttpUtils
                .post()
                .url(searchUrl)
                .addParams("input", str)
                .addParams("page", searchPage)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        dialog.dismiss();
                        Toast.makeText(HomeActivity.this, "网络异常，请检查网络！", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        dialog.dismiss();
                        Gson gson = new Gson();
                        CoachDetailAction coachDetailAction = gson.fromJson(s, CoachDetailAction.class);
                        if (coachDetailAction.getCode() == 200) {
                            coach = new CoachFragment();
                            fragmentManager = getSupportFragmentManager();
                            Bundle bundle = new Bundle();
                            bundle.putString("SEARCH", s);
                            coach.setArguments(bundle);
                            transaction = fragmentManager.beginTransaction();
                            transaction.replace(R.id.car_send_map, coach).commit();
                        } else {
                            Toast.makeText(HomeActivity.this, coachDetailAction.getReason(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    /**
     * 点击监听事件
     * @param v 当前视图
     */
    @Override
    public void onClick(View v) {
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        switch (v.getId()) {
            // 底部导航栏监听
            case R.id.radio_button_01:
                search.setVisibility(View.GONE);
                index = new IndexFragment();
                if (mCurrentFragment != index) {
                    scroll_bar.setVisibility(View.VISIBLE);
                    car_frameLayout.setVisibility(View.GONE);
                    text_title.setText("首页");
                    transaction.replace(R.id.index_fragment_layout, index)
                            .addToBackStack(null).commit();
                    mCurrentFragment = index;
                }
                break;
            case R.id.radio_button_02:
                search.setVisibility(View.VISIBLE);
                coach = new CoachFragment();
                if (mCurrentFragment != coach) {
                    scroll_bar.setVisibility(View.GONE);
                    car_frameLayout.setVisibility(View.VISIBLE);
                    text_title.setText("教练");
                    transaction.replace(R.id.car_send_map, coach)
                            .addToBackStack(null).commit();
                    mCurrentFragment = coach;
                }
                break;
            case R.id.radio_button_03:
                search.setVisibility(View.GONE);
                study = new StudyFragment();
                if (mCurrentFragment != study) {
                    scroll_bar.setVisibility(View.GONE);
                    car_frameLayout.setVisibility(View.VISIBLE);
                    text_title.setText("学习");
                    transaction.replace(R.id.car_send_map, study)
                            .addToBackStack(null).commit();
                    mCurrentFragment = study;
                }
                break;
            case R.id.radio_button_04:
                search.setVisibility(View.GONE);
                my_set = new My_Setting_Fragment();
                if (mCurrentFragment != my_set) {
                    scroll_bar.setVisibility(View.GONE);
                    car_frameLayout.setVisibility(View.VISIBLE);
                    scroll_bar.setEnabled(false);
                    scroll_bar.setOnTouchListener(new OnTouchListener() {
                        @Override
                        public boolean onTouch(View arg0, MotionEvent arg1) {
                            return true;
                        }
                    });
                    text_title.setText("我的资料");
                    transaction.replace(R.id.car_send_map, my_set)
                            .addToBackStack(null).commit();
                    mCurrentFragment = my_set;
                }
                break;
            case R.id.search:
                creatDialog();
                break;
        }
    }

    //主线程处理视图，isExit默认为false，就是点击第一次时，弹出"再按一次退出程序"
    //点击第二次时关闭应用
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 点击两次退出程序
     */
    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            //参数用作状态码；根据惯例，非 0 的状态码表示异常终止。
            System.exit(0);
        }
    }

    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
    }

    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.jgkj.bxxc.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                if (!JPushDataUitl.isEmpty(extras)) {
                    showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                }
                setCostomMsg(showMsg.toString());
            }
        }
    }

    private void setCostomMsg(String msg) {
        if (null != msgText) {
            msgText.setText(msg);
            msgText.setVisibility(android.view.View.VISIBLE);
        }
    }

}
