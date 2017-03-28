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
import com.jgkj.bxxc.bean.SchoolShow;

import java.util.List;

public class SchoolPlaceAdapter extends BaseAdapter {

    private List<SchoolShow> list;
    private Context context;
    private LayoutInflater inflater;
    private SchoolShow result;

    public SchoolPlaceAdapter(Context context, List<SchoolShow> list) {
        this.list = list;
        this.context = context;
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
            convertView = inflater.inflate(R.layout.schoolplace_listview_item,
                    parent, false);
            viewHolder.CityName = (TextView) convertView
                    .findViewById(R.id.CityName);
            viewHolder.place = (TextView) convertView
                    .findViewById(R.id.place);
            viewHolder.distance = (TextView) convertView
                    .findViewById(R.id.distance);
            viewHolder.schId = (TextView) convertView
                    .findViewById(R.id.schId);
            viewHolder.CityPic = (ImageView) convertView.findViewById(R.id.CityPic);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        result = list.get(position);

        if(!result.getMarker().equals("")&&result.getMarker()!=null){
            viewHolder.CityName.setText(result.getSname()+"(标记"+result.getMarker()+")");
        }else{
            viewHolder.CityName.setText(result.getSname()+"(默认标记)");
        }

        viewHolder.schId.setText(result.getId() + "");
        viewHolder.place.setText(result.getFaddress());
        viewHolder.distance.setText(result.getDistance());
        String path = result.getSfile();
        if (!path.endsWith(".jpg") && !path.endsWith(".jpeg") && !path.endsWith(".png") &&
                !path.endsWith(".GIF") && !path.endsWith(".PNG") && !path.endsWith(".JPG") && !path.endsWith(".gif")) {
            Glide.with(context).load("http://www.baixinxueche.com/Public/Home/img/default.png").into(viewHolder.CityPic);
        } else {
            Glide.with(context).load(result.getSfile()).placeholder(R.drawable.coach_pic).error(R.drawable.coach_pic).into(viewHolder.CityPic);
        }
        return convertView;
    }

    static class ViewHolder {
        public TextView CityName, place, distance, schId;
        private ImageView CityPic;
    }
}
