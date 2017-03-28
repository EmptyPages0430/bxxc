package com.jgkj.bxxc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jgkj.bxxc.R;

/**
 * Created by fangzhou on 2017/3/25.
 * 交通标志适配器
 */

public class TrafficSignAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private String[] strSigns;

    public TrafficSignAdapter(Context context,String[] strSigns){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.strSigns = strSigns;
    }
    @Override
    public int getCount() {
        return strSigns.length;
    }

    @Override
    public Object getItem(int i) {
        return strSigns[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(view == null){
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.taffic_signs_listview, viewGroup, false);
            viewHolder.textView = (TextView) view.findViewById(R.id.signs_text);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.textView.setText(strSigns[i]);
        return view;
    }

    class ViewHolder{
        public TextView textView;

    }

}
