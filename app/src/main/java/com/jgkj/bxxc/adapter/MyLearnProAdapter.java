package com.jgkj.bxxc.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jgkj.bxxc.R;
import com.jgkj.bxxc.bean.LearnProAction;

import java.util.List;

public class MyLearnProAdapter extends BaseAdapter {

    private List<LearnProAction> list;
    private Context context;
    private LayoutInflater inflater;
    private LearnProAction action;

    public MyLearnProAdapter(Context context, List<LearnProAction> list) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
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
            convertView = inflater.inflate(R.layout.learnpro_listview_item,
                    parent, false);
            viewHolder.proName = (TextView) convertView
                    .findViewById(R.id.proName);
            viewHolder.isComplete = (TextView) convertView
                    .findViewById(R.id.isComplete);
            viewHolder.context = (TextView) convertView
                    .findViewById(R.id.context);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        action = list.get(position);
        viewHolder.proName.setText(action.getProName());
        Drawable finish = context.getResources().getDrawable(R.drawable.finish);
        Drawable gantan = context.getResources().getDrawable(R.drawable.gantan);
        Drawable waiting = context.getResources().getDrawable(R.drawable.waiting);
        Drawable shibai = context.getResources().getDrawable(R.drawable.shibai);
        shibai.setBounds(0, 0, 30, 30);
        finish.setBounds(0, 0, 30, 30);
        gantan.setBounds(0, 0, 30, 30);
        waiting.setBounds(0, 0, 30, 30);

        if(action.getProisComplete().equals("未完成")||action.getProisComplete().equals("未报名")){
            viewHolder.isComplete.setCompoundDrawables(gantan,null,null,null);
        }else if(action.getProisComplete().equals("审核中")||action.getProisComplete().equals("进行中")){
            viewHolder.isComplete.setCompoundDrawables(waiting,null,null,null);
        }else if(action.getProisComplete().equals("已完成")||action.getProisComplete().equals("通过")){
            viewHolder.isComplete.setCompoundDrawables(finish,null,null,null);
        }else if(action.getProisComplete().contains("失败")){
            viewHolder.isComplete.setCompoundDrawables(shibai,null,null,null);
        }
        viewHolder.isComplete.setText(action.getProisComplete());
        viewHolder.context.setHint(action.getContext());
        switch (position){
            case 0:
                viewHolder.image.setBackgroundResource(R.drawable.step_1);
                break;
            case 1:
                viewHolder.image.setBackgroundResource(R.drawable.step_2);
                break;
            case 2:
                viewHolder.image.setBackgroundResource(R.drawable.step_3);
                break;
            case 3:
                viewHolder.image.setBackgroundResource(R.drawable.step_4);
                break;
            case 4:
                viewHolder.image.setBackgroundResource(R.drawable.step_5);
                break;
            case 5:
                viewHolder.image.setBackgroundResource(R.drawable.step_6);
                break;
            case 6:
                viewHolder.image.setBackgroundResource(R.drawable.step_7);
                break;
            case 7:
                viewHolder.image.setBackgroundResource(R.drawable.step_8);
                break;
        }
        return convertView;
    }
    static class ViewHolder {
        public TextView context,isComplete,proName;
        private ImageView image;
    }
}
