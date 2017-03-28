package com.jgkj.bxxc.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class MyAdapter extends PagerAdapter {
	
	    private List<View> list;

	    public MyAdapter(Context context, List<View> list){
	        this.list = list;
	    }
	    @Override
	    public int getCount() {
	        return list.size();
	    }

	    @Override
	    public boolean isViewFromObject(View view, Object object) {
	        return view == object;
	    }
	    @Override
	    public void destroyItem(ViewGroup container, int position, Object object) {
	        container.removeView(list.get(position));
	    }
	   		 /*
			 * container:viewpager position图片的位置，在list中的位置
			 */
	    @Override
	    public Object instantiateItem(ViewGroup container, int position) {

	        container.addView(list.get(position));

			return list.get(position);
	    }

}
