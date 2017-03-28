package com.jgkj.bxxc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jgkj.bxxc.R;

import java.util.List;

public class AppTimeAdapter extends BaseAdapter {

    private List<String> list;
    private Context context;
    private LayoutInflater inflater;
    private int mRightWidth = 0;

    public AppTimeAdapter(Context context, List<String> list, int rightWidth) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        mRightWidth = rightWidth;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.total_time_show_listview_item,
                    parent, false);

            viewHolder.textView = (TextView) convertView
                    .findViewById(R.id.app_timeShow);
            viewHolder.item_right = (RelativeLayout) convertView.findViewById(R.id.item_right);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(list.get(position));
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        viewHolder.textView.setLayoutParams(lp1);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(mRightWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        viewHolder.item_right.setLayoutParams(lp2);
        viewHolder.item_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onRightItemClick(v,position);
                }
            }
        });
        return convertView;
    }

    static class ViewHolder {
        public TextView textView;
        public RelativeLayout item_right;
    }
    /**
     * 单击事件监听器
     */
    private onRightItemClickListener mListener = null;

    public void setOnRightItemClickListener(onRightItemClickListener listener) {
        mListener = listener;
    }

    public interface onRightItemClickListener {
        void onRightItemClick(View v, int position);
    }

}
