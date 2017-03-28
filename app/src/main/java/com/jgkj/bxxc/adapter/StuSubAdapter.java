package com.jgkj.bxxc.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jgkj.bxxc.R;
import com.jgkj.bxxc.bean.CreateDay_Time;
import com.jgkj.bxxc.bean.NoResultAction;
import com.jgkj.bxxc.tools.SetSubDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;

/**
 * Created by fangzhou on 2017/1/4.
 * 教练排课
 */

public class StuSubAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private CreateDay_Time createday;
    private List<CreateDay_Time> list;
    private ProgressDialog dialog;
    private String cid;
    private String uid;
    private Drawable draw1, draw2;
    private String appUrl = "http://www.baixinxueche.com/index.php/Home/Apiupdata/stuAppointment";
    private String cancelUrl = "http://www.baixinxueche.com/index.php/Home/Apiupdata/deleteAppTime";

    public StuSubAdapter(Context context, List<CreateDay_Time> list, String cid, String uid) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
        this.cid = cid;
        this.uid = uid;
        draw1 = context.getResources().getDrawable(R.drawable.checkboximg);
        draw2 = context.getResources().getDrawable(R.drawable.checkboximgempty);
        draw1.setBounds(0, 0, 30, 30);
        draw2.setBounds(0, 0, 30, 30);
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {

            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.item_group_listview,
                    viewGroup, false);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.imageView);
            viewHolder.imageView.setTag("down");
            viewHolder.btn1 = (RadioButton) view.findViewById(R.id.radio_button_01);
            viewHolder.btn2 = (RadioButton) view.findViewById(R.id.radio_button_02);
            viewHolder.btn3 = (RadioButton) view.findViewById(R.id.radio_button_03);

            viewHolder.stuNum1 = (TextView) view.findViewById(R.id.stuNum1);
            viewHolder.stuNum2 = (TextView) view.findViewById(R.id.stuNum2);
            viewHolder.stuNum3 = (TextView) view.findViewById(R.id.stuNum3);
            viewHolder.line = view.findViewById(R.id.line);
            viewHolder.save = (Button) view.findViewById(R.id.save);
            viewHolder.day = (TextView) view.findViewById(R.id.day);
            viewHolder.isApp = (TextView) view.findViewById(R.id.isApp);

            viewHolder.linear = (LinearLayout) view.findViewById(R.id.layoutChild);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        createday = list.get(i);
        viewHolder.imageView.setTag("down");
        viewHolder.imageView.setImageResource(R.drawable.down);
        viewHolder.imageView.setOnClickListener(new OnClick(viewHolder.linear, viewHolder.line));

        viewHolder.btn1.setSelected(false);
        viewHolder.btn2.setSelected(false);
        viewHolder.btn3.setSelected(false);
        viewHolder.day.setText(createday.getDay());
        viewHolder.save.setTag(createday.getDay());
        switch (createday.getIsApp().size()) {
            case 1:
                viewHolder.btn1.setText(createday.getTime().get(0));
                if (createday.getIsApp().get(0)) {
                    viewHolder.btn1.setSelected(true);
                    viewHolder.btn1.setEnabled(false);
                    viewHolder.btn2.setEnabled(false);
                    viewHolder.btn3.setEnabled(false);
                    viewHolder.save.setText("取消预约");
                    viewHolder.isApp.setText("已选课");
                    viewHolder.isApp.setTextColor(context.getResources().getColor(R.color.green));

                }
                viewHolder.btn2.setVisibility(View.INVISIBLE);
                viewHolder.btn3.setVisibility(View.INVISIBLE);
                viewHolder.stuNum1.setText("当前已预约" + createday.getCount().get(0) + "人");
                viewHolder.stuNum2.setVisibility(View.INVISIBLE);
                viewHolder.stuNum3.setVisibility(View.INVISIBLE);

                break;
            case 2:
                viewHolder.btn1.setText(createday.getTime().get(0));
                viewHolder.btn2.setText(createday.getTime().get(1));
                if (createday.getIsApp().get(0)) {
                    viewHolder.btn1.setSelected(true);
                } else if (createday.getIsApp().get(1)) {
                    viewHolder.btn2.setSelected(true);
                }
                if (createday.getIsApp().get(0) || createday.getIsApp().get(1)) {
                    viewHolder.btn1.setEnabled(false);
                    viewHolder.btn2.setEnabled(false);
                    viewHolder.btn3.setEnabled(false);
                    viewHolder.isApp.setText("已选课");
                    viewHolder.save.setText("取消预约");
                    viewHolder.isApp.setTextColor(context.getResources().getColor(R.color.green));

                }
                viewHolder.btn3.setVisibility(View.INVISIBLE);
                viewHolder.stuNum1.setText("当前已预约" + createday.getCount().get(0) + "人");
                viewHolder.stuNum2.setText("当前已预约" + createday.getCount().get(1) + "人");
                viewHolder.stuNum3.setVisibility(View.INVISIBLE);


                break;
            case 3:
                viewHolder.btn1.setText(createday.getTime().get(0));
                viewHolder.btn2.setText(createday.getTime().get(1));
                viewHolder.btn3.setText(createday.getTime().get(2));
                if (createday.getIsApp().get(0)) {
                    viewHolder.btn1.setSelected(true);
                } else if (createday.getIsApp().get(1)) {
                    viewHolder.btn2.setSelected(true);
                } else if (createday.getIsApp().get(2)) {
                    viewHolder.btn3.setSelected(true);
                }
                if (createday.getIsApp().get(0) || createday.getIsApp().get(1) || createday.getIsApp().get(2)) {
                    viewHolder.btn1.setEnabled(false);
                    viewHolder.btn2.setEnabled(false);
                    viewHolder.btn3.setEnabled(false);
                    viewHolder.isApp.setText("已选课");
                    viewHolder.save.setText("取消预约");
                    viewHolder.isApp.setTextColor(context.getResources().getColor(R.color.green));

                }
                viewHolder.stuNum1.setText("当前已预约" + createday.getCount().get(0) + "人");
                viewHolder.stuNum2.setText("当前已预约" + createday.getCount().get(1) + "人");
                viewHolder.stuNum3.setText("当前已预约" + createday.getCount().get(2) + "人");

                break;
        }
        viewHolder.btn1.setOnClickListener(new rtbClick(viewHolder.btn1, viewHolder.btn2, viewHolder.btn3));
        viewHolder.btn2.setOnClickListener(new rtbClick(viewHolder.btn2, viewHolder.btn1, viewHolder.btn3));
        viewHolder.btn3.setOnClickListener(new rtbClick(viewHolder.btn3, viewHolder.btn2, viewHolder.btn1));
        viewHolder.save.setOnClickListener(new save(viewHolder.btn1, viewHolder.btn2,
                viewHolder.btn3, viewHolder.isApp));

        return view;
    }

    private class rtbClick implements View.OnClickListener {
        private RadioButton rtb1;
        private RadioButton rtb2;
        private RadioButton rtb3;

        public rtbClick(RadioButton rtb1, RadioButton rtb2, RadioButton rtb3) {
            this.rtb1 = rtb1;
            this.rtb2 = rtb2;
            this.rtb3 = rtb3;
        }

        @Override
        public void onClick(View view) {
            rtb1.setSelected(true);
            rtb2.setSelected(false);
            rtb3.setSelected(false);
        }
    }

    /**
     * 保存提交
     */
    public class save implements View.OnClickListener {
        private Button btn1;
        private Button btn2;
        private Button btn3;
        private TextView isApp;

        public save(Button btn1, Button btn2, Button btn3, TextView isApp) {
            this.btn1 = btn1;
            this.btn2 = btn2;
            this.btn3 = btn3;
            this.isApp = isApp;
        }

        @Override
        public void onClick(View view) {
            dialog = ProgressDialog.show(context, null, "提交中,请耐心等待...");
            String time = "";
            Button btn = (Button) view;
            SetSubDialog subDialog = null;
            if (btn.getText().toString().equals("取消预约")) {
//                app(btn.getTag().toString(), "", cancelUrl);
            } else {
                if (btn1.isSelected()) {
                    time = btn1.getText().toString();
                } else if (btn2.isSelected()) {
                    time = btn2.getText().toString();
                } else if (btn3.isSelected()) {
                    time = btn3.getText().toString();
                }
                if (!time.equals("")) {
//                    app(btn.getTag().toString(), time, appUrl);
                } else {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Toast.makeText(context, "请至少选择一个时间段在保存", Toast.LENGTH_SHORT).show();
                }
            }
//            SharedPreferences sp = context.getSharedPreferences("getSubjectSet", Activity.MODE_PRIVATE);
//            SharedPreferences.Editor editor = sp.edit();
//            editor.putBoolean("isChange", true);
//            editor.commit();
//            Activity activity = (Activity) context;
//            activity.finish();

        }
    }

    private class OnClick implements View.OnClickListener {
        private LinearLayout linear;
        private View line;

        public OnClick(LinearLayout linear, View line) {
            this.linear = linear;
            this.line = line;
        }

        @Override
        public void onClick(View view) {
            ImageView img = (ImageView) view;
            if (img.getTag().toString().equals("down")) {
                img.setImageResource(R.drawable.up);
                img.setTag("up");
                line.setVisibility(View.VISIBLE);
                linear.setVisibility(View.VISIBLE);
            } else if (img.getTag().toString().equals("up")) {
                img.setImageResource(R.drawable.down);
                img.setTag("down");
                line.setVisibility(View.GONE);
                linear.setVisibility(View.GONE);
            }
        }
    }


    //listView优化
    static class ViewHolder {
        public ImageView imageView;
        private LinearLayout linear;
        private RadioButton btn1, btn2, btn3;
        public Button save;
        private TextView day, isApp;
        private View line;
        public TextView stuNum1, stuNum2, stuNum3;
    }

    /**
     * 预约
     *
     * @param day
     * @param time_slot
     */
    private void app(String day, String time_slot, String url) {
        OkHttpUtils
                .post()
                .url(url)
                .addParams("cid", cid)
                .addParams("uid", uid)
                .addParams("day", day)
                .addParams("time_slot", time_slot)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        Toast.makeText(context, "网络状态不佳，请稍后再试", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        Gson gson = new Gson();
                        NoResultAction action = gson.fromJson(s, NoResultAction.class);
                        Toast.makeText(context, action.getReason(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
