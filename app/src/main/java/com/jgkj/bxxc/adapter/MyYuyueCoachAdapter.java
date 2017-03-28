package com.jgkj.bxxc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jgkj.bxxc.R;
import com.jgkj.bxxc.bean.MyCoachBean;

import java.util.List;

public class MyYuyueCoachAdapter extends BaseAdapter {
	private Context context;
	private List<MyCoachBean.Result> list;
	private LayoutInflater inflater;
	private MyCoachBean.Result result;

	public MyYuyueCoachAdapter(Context context, List<MyCoachBean.Result> list){
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {

		return position;
	}
	@Override
	public long getItemId(int position) {

		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.mycoach_listview, parent, false);
			viewHolder.mySub = (TextView) convertView
					.findViewById(R.id.mySub);
			viewHolder.num1 = (TextView) convertView
					.findViewById(R.id.num1);
			viewHolder.coachName = (TextView) convertView
					.findViewById(R.id.coachName);
			viewHolder.place = (TextView) convertView
					.findViewById(R.id.place);
			viewHolder.mycar = (TextView) convertView
					.findViewById(R.id.mycar);
			viewHolder.myclass = (TextView) convertView
					.findViewById(R.id.myclass);
			viewHolder.num2 = (TextView) convertView
					.findViewById(R.id.num2);
			viewHolder.myContext = (TextView) convertView
					.findViewById(R.id.myContext);
			viewHolder.coachHead = (ImageView) convertView
					.findViewById(R.id.coachHead);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		result = list.get(position);
		viewHolder.coachName.setText(result.getCoachname());
		viewHolder.num1.setText("当前所带学员数："+result.getStunum()+"人");
		viewHolder.num2.setText("最高可带学员数："+result.getMaxnum()+"人");
		viewHolder.coachName.setText(result.getCoachname());
		viewHolder.mySub.setText("我的科目"+result.getSubject()+"教练");
		viewHolder.myContext.setText(result.getPrompt());
		viewHolder.place.setText(result.getFaddress());
		viewHolder.mycar.setText(result.getChexing());
		viewHolder.myclass.setText(result.getClass_type());
		Glide.with(context).load(result.getFile()) .into(viewHolder.coachHead);
		return convertView;
	}
	static class ViewHolder {
		public TextView coachName,mycar,myclass;
		public TextView mySub,place,num1,num2,myContext;
		public ImageView coachHead;

	}

}
