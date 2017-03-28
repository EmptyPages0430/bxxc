package com.jgkj.bxxc.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jgkj.bxxc.R;
import com.jgkj.bxxc.bean.MyReservationAction;

public class MyReservationAdapter extends BaseAdapter {
	private Context context;
	private Bundle bundle;
	private LayoutInflater inflater;
	private MyReservationAction myReservation;

    public MyReservationAdapter(Context context, Bundle bundle) {
		this.context = context;
		this.bundle = bundle;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {

		return 6;
	}

	@Override
	public Object getItem(int position) {

		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;

		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(
					R.layout.myreservation_listview_item, parent, false);
			viewHolder.textView1 = (TextView) convertView
					.findViewById(R.id.myReservation_listView_text1);
			viewHolder.textView2 = (TextView) convertView
					.findViewById(R.id.myReservation_listView_text2);
			viewHolder.textView3 = (TextView) convertView
					.findViewById(R.id.myReservation_listView_text3);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		myReservation = (MyReservationAction) bundle.getSerializable("myReservation");
		viewHolder.textView1.setText(myReservation.getName());
		viewHolder.textView2.setText(myReservation.getPlace());
		viewHolder.textView3.setText(myReservation.getDate());
		return convertView;
	}

	static class ViewHolder {
		public ImageView imageView;
		public TextView textView1, textView2, textView3;
	}

}
