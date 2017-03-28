package com.jgkj.bxxc.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jgkj.bxxc.R;
import com.jgkj.bxxc.bean.AppAction;

import java.util.List;

public class AppTotalAdapter extends BaseAdapter {

    private List<AppAction> list;
    private Context context;
    private LayoutInflater inflater;
    private Dialog dialog;
    private View inflate;
    private TextView app_school, app_coach, app_sub, app_time,cancel_app;

    public AppTotalAdapter(Context context, List<AppAction> list) {
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
            convertView = inflater.inflate(R.layout.my_app_listview_item,
                    parent, false);

            viewHolder.textView = (TextView) convertView
                    .findViewById(R.id.time);
            viewHolder.clickMore = (TextView) convertView.findViewById(R.id.clickMore);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final AppAction action = list.get(position);
        viewHolder.textView.setText(action.getTime());
        viewHolder.clickMore.setText("查看更多");
        viewHolder.clickMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                creatDialog(position,action);
            }
        });
        return convertView;
    }

    public void creatDialog(final int position, AppAction action) {
        dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        // 填充对话框的布局
        inflate = LayoutInflater.from(context).inflate(R.layout.myapp_dialog, null);
        // 初始化控件
        app_school = (TextView) inflate.findViewById(R.id.app_school);
        app_coach = (TextView) inflate.findViewById(R.id.app_coach);
        app_sub = (TextView) inflate.findViewById(R.id.app_sub);
        app_time = (TextView) inflate.findViewById(R.id.app_time);
        cancel_app = (TextView) inflate.findViewById(R.id.cancel_app);
        cancel_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.remove(position);
                notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        app_coach.setText(action.getCoachName());
        app_school.setText(action.getSchoolName());
        app_sub.setText(action.getSub());
        app_time.setText(action.getTime());
        // 将布局设置给Dialog
        dialog.setContentView(inflate);
        // 获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        // 设置dialog横向充满
        dialogWindow.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.CENTER);
        dialog.show();// 显示对话框
    }

    static class ViewHolder {
        public TextView textView;
        public TextView clickMore;
    }
}
