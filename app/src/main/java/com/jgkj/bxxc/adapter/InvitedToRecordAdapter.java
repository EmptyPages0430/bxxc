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
 * 邀请学车记录
 */

public class InvitedToRecordAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;

    public InvitedToRecordAdapter(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int i) {
        return null;
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
            view = inflater.inflate(R.layout.invited_to_record_listview, viewGroup, false);
//            viewHolder.textView = (TextView) view.findViewById(R.id.signs_text);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }

        return view;
    }

    class ViewHolder{
        public TextView textView;

    }

}
