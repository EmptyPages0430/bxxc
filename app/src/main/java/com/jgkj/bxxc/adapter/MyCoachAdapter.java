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
public class MyCoachAdapter extends BaseAdapter {
    private Context context;
    private List<CoachDetailAction.Result> list;
    private LayoutInflater inflater;
    private CoachDetailAction.Result coachDetailAction;
    private LinearLayout.LayoutParams wrapParams;

    public MyCoachAdapter(Context context, List<CoachDetailAction.Result> list) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.coach_listview_item, parent, false);
            viewHolder.coachName = (TextView) convertView.findViewById(R.id.coachName);
            viewHolder.kemu = (TextView) convertView.findViewById(R.id.kemu);
            viewHolder.place = (TextView) convertView.findViewById(R.id.place);
            viewHolder.car = (TextView) convertView.findViewById(R.id.car);
            viewHolder.coachId = (TextView) convertView.findViewById(R.id.coachId);
            viewHolder.classType = (TextView) convertView.findViewById(R.id.classType);
            viewHolder.credit = (LinearLayout) convertView.findViewById(R.id.credit);
            viewHolder.totalPriseText2 = (TextView) convertView.findViewById(R.id.totalPriseText2);
            viewHolder.goodPrise = (TextView) convertView.findViewById(R.id.goodPrise);
            viewHolder.adopt = (TextView) convertView.findViewById(R.id.adopt);
            viewHolder.coachPic = (ImageView) convertView.findViewById(R.id.coachPic);
            viewHolder.stuNo = (TextView) convertView.findViewById(R.id.stuNo);
            viewHolder.totalPriseText1 = (LinearLayout) convertView.findViewById(R.id.totalPriseText1);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        coachDetailAction = list.get(position);
        int credit = Integer.parseInt(coachDetailAction.getCredit());
        int totalPrise = Integer.parseInt(coachDetailAction.getZonghe());
        viewHolder.credit.removeAllViews();
        viewHolder.totalPriseText1.removeAllViews();
        for (int i = 0; i < credit; i++) {
            ImageView image = new ImageView(this.context);
            image.setBackgroundResource(R.drawable.xin_1);
            wrapParams = new LinearLayout.LayoutParams(18,18);
            image.setLayoutParams(wrapParams);
            viewHolder.credit.addView(image);
        }
        for (int i = 0; i < totalPrise; i++) {
            ImageView image = new ImageView(this.context);
            image.setBackgroundResource(R.drawable.star1);
            wrapParams = new LinearLayout.LayoutParams(18,18);
            image.setLayoutParams(wrapParams);
            viewHolder.totalPriseText1.addView(image);
        }
        Glide.with(context).load(coachDetailAction.getFile()).placeholder(R.drawable.head1).error(R.drawable.head1).into(viewHolder.coachPic);
        viewHolder.coachPic.setTag(R.id.imageloader_uri,coachDetailAction.getFile());
        viewHolder.coachName.setText(coachDetailAction.getCoachname());
        viewHolder.place.setText(coachDetailAction.getFaddress());
        viewHolder.car.setText("车型："+coachDetailAction.getChexing());
        viewHolder.classType.setText("班级:"+coachDetailAction.getClass_type());
        viewHolder.totalPriseText2.setHint(coachDetailAction.getZonghe()+".0分");
        viewHolder.goodPrise.setHint("好评率:"+coachDetailAction.getPraise()+"%");
        viewHolder.adopt.setHint("通过率:"+coachDetailAction.getPass()+"%");
        viewHolder.stuNo.setHint("所带学员数为"+coachDetailAction.getStunum()+"个");
        viewHolder.coachId.setText(coachDetailAction.getCid()+"");
        if(coachDetailAction.getClass_class()!=null){
            viewHolder.kemu.setText(coachDetailAction.getClass_type());
            viewHolder.classType.setText("班级:"+coachDetailAction.getClass_class());
        }else{
            viewHolder.kemu.setVisibility(View.GONE);
        }
        return convertView;
    }
    //listView优化
    static class ViewHolder {
        public ImageView coachPic;
        public TextView coachName, place,car,classType,totalPriseText2,goodPrise,adopt,stuNo;
        private TextView coachId,kemu;
        public LinearLayout credit,totalPriseText1;
    }

}
