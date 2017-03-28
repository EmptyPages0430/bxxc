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
import com.jgkj.bxxc.bean.HeadlinesAction;

import java.util.List;

/**
 * Created by fangzhou on 2016/12/29.
 */

public class HeadlinesAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<HeadlinesAction.Result> list;
    private HeadlinesAction.Result res;

    public HeadlinesAdapter(Context context, List<HeadlinesAction.Result> list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.headlines_listview,
                    parent, false);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);
            viewHolder.headlinestitle = (TextView) convertView.findViewById(R.id.headlinestitle);
            viewHolder.headlinesdetails = (TextView) convertView.findViewById(R.id.headlinesdetails);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            viewHolder.readAll = (LinearLayout) convertView.findViewById(R.id.readAll);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        res = list.get(position);
        viewHolder.time.setText(res.getTime());
        viewHolder.headlinesdetails.setText(res.getSubtitle());
        viewHolder.headlinestitle.setText(res.getTitle());
        String path = res.getPicture();
        if (!path.endsWith(".jpg") && !path.endsWith(".jpeg") && !path.endsWith(".png") &&
                !path.endsWith(".GIF") && !path.endsWith(".PNG") && !path.endsWith(".JPG") && !path.endsWith(".gif")) {
            Glide.with(context).load(R.drawable.coach_pic).into(viewHolder.imageView);
        } else {
            Glide.with(context).load(path).placeholder(R.drawable.coach_pic).into(viewHolder.imageView);
        }
        viewHolder.readAll.setTag(res.getUrl());
        return convertView;
    }

    //静态初始化
    static class ViewHolder {
        public TextView time, headlinestitle, headlinesdetails;
        private ImageView imageView;
        private LinearLayout readAll;

    }
}
