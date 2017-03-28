package com.jgkj.bxxc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jgkj.bxxc.R;
import com.jgkj.bxxc.bean.CoachDetailAction;

import java.util.List;

/**
 * 我的教练适配器
 */
public class QuesAnsAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private String[] listItem;

    public QuesAnsAdapter(Context context, String[] listItem) {
        this.context = context;
        this.listItem = listItem;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return listItem.length;
    }

    @Override
    public Object getItem(int position) {

        return listItem[position];
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.ques_ans_listview, parent, false);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(listItem[position]);
        return convertView;
    }
    //listView优化
    static class ViewHolder {
        public TextView textView;
    }

}
