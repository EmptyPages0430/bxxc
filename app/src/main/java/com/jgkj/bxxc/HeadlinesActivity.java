package com.jgkj.bxxc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jgkj.bxxc.adapter.HeadlinesAdapter;
import com.jgkj.bxxc.bean.HeadlinesAction;
import com.jgkj.bxxc.tools.RefreshLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by fangzhou on 2017/3/9.
 * 百信头条
 */

public class HeadlinesActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener
        , RefreshLayout.OnLoadListener{
    private Button back;
    private TextView title,textView;
    private ListView listView;
    private RefreshLayout refreshLayout;
    private HeadlinesAdapter adapter;
    private List<HeadlinesAction.Result> headlinesList;
    private int currentPage = 1;
    private boolean isfresh = false;
    private String url = "http://www.baixinxueche.com/index.php/Home/Apitoken/nowLines";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.headlines);
        initView();
        initData();
        getheadlines();
    }


    //初始化集合，和解决listView和refreshLayout控件的滑动冲突问题confect
    private void initData() {
        headlinesList = new ArrayList<>();
        //解决listView和refreshLayout刷新控件滑动冲突，当滑动屏幕时refreshLayout会优先获取焦点，
        //因此往往listView滑下来了并不在顶部和底部但是却走了refreshLayout，因此我们需要判断下
        //当你滑动时，listView是否处于顶部一个item项，如果是第一个则走refreshLayout，如果不是则继续走listView
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                boolean enable = false;
                if(listView != null && listView.getChildCount() > 0){
                    // check if the first item of the list is visible
                    boolean firstItemVisible = listView.getFirstVisiblePosition() == 0;
                    // check if the top of the first item is visible
                    boolean topOfFirstItemVisible = listView.getChildAt(0).getTop() == 0;
                    // enabling or disabling the refresh layout
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                refreshLayout.setEnabled(enable);
            }});
    }

    /**
     * 初始化布局
     */
    private void initView() {

        title = (TextView) findViewById(R.id.text_title);
        textView = (TextView) findViewById(R.id.textView);

        back = (Button) findViewById(R.id.button_backward);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(this);
        title.setText("百信头条");

        refreshLayout = (RefreshLayout) findViewById(R.id.refresh);
        refreshLayout.setColorSchemeResources(R.color.color_bule2, R.color.color_bule, R.color.color_bule2, R.color.color_bule3);
        refreshLayout.setOnLoadListener(this);
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);

    }

    /**
     * 网络请求百信头条
     */
    private void getheadlines() {

        OkHttpUtils
                .post()
                .url(url)
                .addParams("page", currentPage+"")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        Toast.makeText(HeadlinesActivity.this, "网络异常，请检查网络！", Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onResponse(String s, int i) {
                        listView.setTag(s);
                        if(listView.getTag()!=null){
                            setHeadLines();
                        }
                    }
                });
    }

    private void setHeadLines(){
        String tag = listView.getTag().toString();
        Gson gson = new Gson();
        HeadlinesAction action = gson.fromJson(tag, HeadlinesAction.class);
        if(action.getCode()==200){
            headlinesList.addAll(action.getResult());
            adapter = new HeadlinesAdapter(HeadlinesActivity.this,headlinesList);
            listView.setAdapter(adapter);
        }else{
            if(!isfresh){
                refreshLayout.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);
            }
        }
        Toast.makeText(HeadlinesActivity.this,action.getReason(), Toast.LENGTH_SHORT).show();
    }

    /**
     * 点击事件
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_backward:
                finish();
                break;
        }
    }


    /**
     * 初listView的item点击事件
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.readAll);
        Intent intent = new Intent();
        intent.setClass(HeadlinesActivity.this,WebViewActivity.class);
        intent.putExtra("url",layout.getTag().toString());
        intent.putExtra("title","百信头条");
        startActivity(intent);
    }

    @Override
    public void onLoad() {
        isfresh = true;
        refreshLayout.postDelayed(new Runnable() {

            @Override
            public void run() {
                currentPage ++ ;
                getheadlines();
                refreshLayout.setLoading(false);
            }
        }, 2000);
    }
}