package com.jgkj.bxxc.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jgkj.bxxc.R;

/**
 * @author fangzhou
 * Popwindow下拉框一级菜单
 */
public class ParentCategoryAdapter extends BaseAdapter {
		private Context mContext;
		private String[] str;
		private int pos;
	
	public ParentCategoryAdapter(Context context, String[] str) {
		mContext = context;
		this.str = str;
	}

	@Override
	public int getCount() {
		return str.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_parent_category_item, null);
			holder.tvParentCategoryName = (TextView) convertView.findViewById(R.id.tv_parent_category_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tvParentCategoryName.setText(str[position]);
		
		if(pos==position){
			holder.tvParentCategoryName.setTextColor(mContext.getResources().getColor(R.color.list_text_select_color));
			convertView.setBackgroundColor(mContext.getResources().getColor(R.color.zu_choose_right_item_bg));
		}else{
			holder.tvParentCategoryName.setTextColor(mContext.getResources().getColor(android.R.color.black));
			convertView.setBackgroundColor(mContext.getResources().getColor(R.color.zu_choose_left_item_bg));
		}
		return convertView;
	}
	//优化，防止每次滑动的时候item都会创建
	private class ViewHolder {
		private TextView tvParentCategoryName;
	}
	
	public void setSelectedPosition(int pos) {
		this.pos = pos;
	}
	
	public int getPos() {
		return pos;
	}
}
