package com.jgkj.bxxc.tools;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by fangzhou on 2017/3/13.
 */

public class HwgtListView extends ListView implements AbsListView.OnScrollListener {

    private static boolean flag = true;
    public HwgtListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnScrollListener(this);
    }
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case OnScrollListener.SCROLL_STATE_IDLE:
                Log.d("HWGT", "SCROLL_STATE_IDLE........");
                break;
            case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                Log.d("HWGT", "SCROLL_STATE_TOUCH_SCROLL........");
                break;
            case OnScrollListener.SCROLL_STATE_FLING:
                break;
        }
    }
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        //在onScroll方法中判断：
        if (firstVisibleItem == 0) { // 只要第一个item有一小部分可见都会满足条件
            View firstView = view.getChildAt(firstVisibleItem);
            if (firstView != null) {
                if(firstView.getTop()==0){ // 判断第一个item到顶部的距离
                    flag = false;
                    Log.d("HWGT", "滑动到顶部了，卧槽！");
                }
            }
        }
        //首先在onScroll方法中判断listview到达底部：
        if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
            View lastVisibleItemView = view.getChildAt(view.getChildCount() - 1);
            if (lastVisibleItemView != null) {
                if(getHeight() == lastVisibleItemView.getBottom()){
                    flag = false;
                    Log.d("HWGT", getHeight()+"...滚到底部了...=.."+lastVisibleItemView.getBottom());
                }
            }
        }
    }
}
