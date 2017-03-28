package com.jgkj.bxxc.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2016/10/29.
 */
public class OrderAdapter extends PagerAdapter {
    private List<View> list;
    //构造方法，参数是我们的页卡，这样比较方便
    public OrderAdapter(Context context, List<View> list){
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.size();//返回页卡的数量

    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;//官方提示这样写
    }
    //删除页卡
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(list.get(position));
    }
    /*
     * 这个方法用来实例化
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(list.get(position));
        return list.get(position);//添加页卡
    }
}
