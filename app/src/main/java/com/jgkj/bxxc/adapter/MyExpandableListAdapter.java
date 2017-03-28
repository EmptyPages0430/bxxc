package com.jgkj.bxxc.adapter;

import android.content.Context;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jgkj.bxxc.R;

/**
 * Created by fangzhou on 2016/12/22.
 */

public class MyExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private LayoutInflater inflater;
    private String[] city;
    private String[][] datialPlace;

    public MyExpandableListAdapter(Context context, String[] city, String[][] datialPlace) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.city = city;
        this.datialPlace = datialPlace;
    }

    @Override
    public int getGroupCount() {
        return city.length;
    }

    @Override
    public int getChildrenCount(int i) {
        return datialPlace[i].length;
    }

    @Override
    public Object getGroup(int i) {
        return city[i];
    }

    @Override
    public Object getChild(int i, int i1) {
        return datialPlace[i][i1];
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    //【重要】填充一级列表
    @Override
    public View getGroupView(int groupPosition, final boolean isExpanded, View convertView, ViewGroup viewGroup) {
        ViewHolder3 viewHolder3 = null;
        if (convertView == null) {
            viewHolder3 = new ViewHolder3();
            convertView = inflater.inflate(R.layout.item_group, null);
            viewHolder3.textView = (TextView) convertView.findViewById(R.id.textView);
            viewHolder3.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(viewHolder3);
        } else {
            viewHolder3 = (ViewHolder3) convertView.getTag();
        }
        viewHolder3.textView.setText(city[groupPosition]);
        TextPaint paint = viewHolder3.textView.getPaint();
        paint.setFakeBoldText(true);
        viewHolder3.imageView.setImageResource(R.drawable.down);

        return convertView;
    }

    //【重要】填充二级列表
    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean b, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_child, null);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(datialPlace[groupPosition][childPosition]);
        return convertView;
    }

    static class ViewHolder {
        public TextView textView;
    }

    static class ViewHolder3 {
        public TextView textView;
        public ImageView imageView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

}
