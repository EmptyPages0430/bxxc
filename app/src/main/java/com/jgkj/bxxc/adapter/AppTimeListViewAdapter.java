package com.jgkj.bxxc.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jgkj.bxxc.R;
import com.jgkj.bxxc.bean.MyAppTime;
import com.jgkj.bxxc.bean.MyCoachAction;
import com.jgkj.bxxc.bean.UserInfo;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/12/29.
 */

public class AppTimeListViewAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private List<MyAppTime> list;
    private ProgressDialog dialog;

    private MyAppTime sub;
    private View headView;
    private LayoutInflater inflater;
    private String day_time = "";
    private String cid;
    private String uid;
    private String appUrl = "http://www.baixinxueche.com/index.php/Home/Apiupdata/stuAppointment";
    private String cancelUrl = "http://www.baixinxueche.com/index.php/Home/Apiupdata/deleteAppTime";

    public AppTimeListViewAdapter(Context context, List<MyAppTime> list, String cid) {
        this.list = list;
        this.context = context;
        headView = new View(context);
        this.cid = cid;
        inflater = LayoutInflater.from(context);
        SharedPreferences sp = context.getSharedPreferences("USER", Context.MODE_PRIVATE);
        String str = sp.getString("userInfo", null);
        Gson gson = new Gson();
        UserInfo userInfo = gson.fromJson(str, UserInfo.class);
        uid = userInfo.getResult().getUid() + "";
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
            convertView = inflater.inflate(R.layout.item_group_listview, null);
            viewHolder.day = (TextView) convertView.findViewById(R.id.day);
            viewHolder.isApp = (TextView) convertView.findViewById(R.id.isApp);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);

            viewHolder.layoutChild = (LinearLayout) convertView.findViewById(R.id.layoutChild);
            viewHolder.layout = (LinearLayout) convertView.findViewById(R.id.layout);
            viewHolder.radioGroup = (RadioGroup) convertView.findViewById(R.id.radioGroup);
            viewHolder.nothing = (TextView) convertView.findViewById(R.id.nothing);
            viewHolder.line = convertView.findViewById(R.id.line);

            viewHolder.radio_button_01 = (RadioButton) convertView.findViewById(R.id.radio_button_01);
            viewHolder.radio_button_02 = (RadioButton) convertView.findViewById(R.id.radio_button_02);
            viewHolder.radio_button_03 = (RadioButton) convertView.findViewById(R.id.radio_button_03);
            viewHolder.stuNum1 = (TextView) convertView.findViewById(R.id.stuNum1);
            viewHolder.stuNum2 = (TextView) convertView.findViewById(R.id.stuNum2);
            viewHolder.stuNum3 = (TextView) convertView.findViewById(R.id.stuNum3);
            viewHolder.save = (Button) convertView.findViewById(R.id.save);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        sub = list.get(position);
        viewHolder.save.setTag(position);
        viewHolder.day.setText(sub.getDay());
        viewHolder.imageView.setTag("up");
        viewHolder.imageView.setImageResource(R.drawable.up);
        viewHolder.imageView.setOnClickListener(new OnClick(viewHolder.layoutChild, viewHolder.line));
        switch (list.get(Integer.parseInt(viewHolder.save.getTag().toString())).getTime().size()) {
            case 0:
                viewHolder.radio_button_01.setVisibility(View.GONE);
                viewHolder.radio_button_02.setVisibility(View.GONE);
                viewHolder.radio_button_03.setVisibility(View.GONE);
                viewHolder.stuNum1.setVisibility(View.GONE);
                viewHolder.stuNum2.setVisibility(View.GONE);
                viewHolder.stuNum3.setVisibility(View.GONE);

                break;
            case 1:
                viewHolder.radio_button_01.setText(sub.getTime().get(0));
                viewHolder.stuNum1.setText("当前预约：" + sub.getNum().get(0) + "人");
                viewHolder.radio_button_02.setVisibility(View.GONE);
                viewHolder.radio_button_03.setVisibility(View.GONE);
                viewHolder.stuNum2.setVisibility(View.GONE);
                viewHolder.stuNum3.setVisibility(View.GONE);

                break;
            case 2:
                viewHolder.radio_button_01.setText(sub.getTime().get(0));
                viewHolder.radio_button_02.setText(sub.getTime().get(1));
                viewHolder.stuNum1.setText("当前预约：" + sub.getNum().get(0) + "人");
                viewHolder.stuNum2.setText("当前预约：" + sub.getNum().get(1) + "人");
                viewHolder.radio_button_03.setVisibility(View.GONE);
                viewHolder.stuNum3.setVisibility(View.GONE);

                break;
            case 3:
                viewHolder.radio_button_01.setText(sub.getTime().get(0));
                viewHolder.radio_button_02.setText(sub.getTime().get(1));
                viewHolder.radio_button_03.setText(sub.getTime().get(2));
                viewHolder.stuNum1.setText("当前预约：" + sub.getNum().get(0) + "人");
                viewHolder.stuNum2.setText("当前预约：" + sub.getNum().get(1) + "人");
                viewHolder.stuNum3.setText("当前预约：" + sub.getNum().get(2) + "人");
                break;
        }
        if (viewHolder.radio_button_01.getText().toString().equals(sub.getAppTime())) {
            viewHolder.radio_button_01.setChecked(true);
            viewHolder.radio_button_01.setEnabled(false);
            viewHolder.radio_button_02.setEnabled(false);
            viewHolder.radio_button_03.setEnabled(false);
            viewHolder.isApp.setText("已选课");
            viewHolder.isApp.setTextColor(context.getResources().getColor(R.color.themeColor));
            viewHolder.save.setText("取消预约");
        } else if (viewHolder.radio_button_02.getText().toString().equals(sub.getAppTime())) {
            viewHolder.radio_button_02.setChecked(true);
            viewHolder.radio_button_01.setEnabled(false);
            viewHolder.radio_button_02.setEnabled(false);
            viewHolder.radio_button_03.setEnabled(false);
            viewHolder.isApp.setText("已选课");
            viewHolder.isApp.setTextColor(context.getResources().getColor(R.color.themeColor));
            viewHolder.save.setText("取消预约");
        } else if (viewHolder.radio_button_03.getText().toString().equals(sub.getAppTime())) {
            viewHolder.radio_button_03.setChecked(true);
            viewHolder.radio_button_01.setEnabled(false);
            viewHolder.radio_button_02.setEnabled(false);
            viewHolder.radio_button_03.setEnabled(false);
            viewHolder.isApp.setText("已选课");
            viewHolder.isApp.setTextColor(context.getResources().getColor(R.color.themeColor));

            viewHolder.save.setText("取消预约");
        } else if (sub.getAppTime().equals("") || sub.getAppTime() == null) {
            viewHolder.radio_button_01.setChecked(false);
            viewHolder.radio_button_02.setChecked(false);
            viewHolder.radio_button_03.setChecked(false);
            viewHolder.radio_button_01.setEnabled(true);
            viewHolder.radio_button_02.setEnabled(true);
            viewHolder.radio_button_03.setEnabled(true);
            viewHolder.isApp.setText("未选课");
            viewHolder.isApp.setTextColor(context.getResources().getColor(R.color.right_bg));
            viewHolder.save.setText("立即预约");
        }
        viewHolder.save.setOnClickListener(new MyApp(viewHolder.radio_button_01, viewHolder.radio_button_02,
                viewHolder.radio_button_03, position,viewHolder.isApp));
        return convertView;
    }

    /**
     * //     * 点击事件
     * //
     */
    private class MyApp implements View.OnClickListener {
        private RadioButton btn1;
        private RadioButton btn2;
        private RadioButton btn3;
        private int position;
        private TextView isApp;

        public MyApp(RadioButton btn1, RadioButton btn2, RadioButton btn3, int position, TextView isApp) {
            this.btn1 = btn1;
            this.btn2 = btn2;
            this.btn3 = btn3;
            this.isApp = isApp;
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.save:
                    Button btn = (Button) view;
                    if (btn.getText().toString().equals("立即预约")) {
                        if (!btn1.isChecked() && !btn2.isChecked() && !btn3.isChecked()) {
                            Toast.makeText(context, "请至少选择一个时间段", Toast.LENGTH_SHORT).show();
                        } else {
                            dialog = ProgressDialog.show(context, null, "请求中...");
                            if (btn1.isChecked()) {
                                day_time = btn1.getText().toString();
                            } else if (btn2.isChecked()) {
                                day_time = btn2.getText().toString();
                            } else if (btn3.isChecked()) {
                                day_time = btn3.getText().toString();
                            }
                            btn1.setEnabled(false);
                            btn2.setEnabled(false);
                            btn3.setEnabled(false);
                            isApp.setText("已选课");
                            isApp.setTextColor(context.getResources().getColor(R.color.themeColor));

                            app(list.get(position).getDay(), day_time);
                            btn.setText("取消预约");
                        }
                    } else if (btn.getText().toString().equals("取消预约")) {
                        dialog = ProgressDialog.show(context, null, "请求中...");
                        cancelApp(list.get(position).getDay());
                        btn.setText("立即预约");
                        isApp.setText("未选课");
                        isApp.setTextColor(context.getResources().getColor(R.color.right_bg));
                        btn1.setChecked(false);
                        btn2.setChecked(false);
                        btn3.setChecked(false);
                        btn1.setEnabled(true);
                        btn2.setEnabled(true);
                        btn3.setEnabled(true);
                    }
                    break;
            }
        }
    }

    @Override
    public void onClick(View view) {

    }

    private class OnClick implements View.OnClickListener {
        private LinearLayout layoutChild;
        private View line;

        public OnClick(LinearLayout layoutChild, View line) {
            this.layoutChild = layoutChild;
            this.line = line;
        }
        @Override
        public void onClick(View view) {
            ImageView img = (ImageView) view;
            if (img.getTag().toString().equals("down")) {
                img.setImageResource(R.drawable.up);
                img.setTag("up");
                layoutChild.setVisibility(View.VISIBLE);
                line.setVisibility(View.VISIBLE);
            } else if (img.getTag().toString().equals("up")) {
                img.setImageResource(R.drawable.down);
                img.setTag("down");
                layoutChild.setVisibility(View.GONE);
                line.setVisibility(View.GONE);
            }
        }
    }

    static class ViewHolder {
        public LinearLayout layout, layoutChild;
        public RadioButton radio_button_01, radio_button_02, radio_button_03;
        public TextView stuNum1, stuNum2, stuNum3, nothing;
        public RadioGroup radioGroup;
        private Button save, cancel;
        public TextView day, isApp;
        public ImageView imageView;
        public View line;
    }

    /**
     * //     * 取消预约
     * //     *
     * //     * @param day
     * //
     */
    private void cancelApp(String day) {

        OkHttpUtils
                .post()
                .url(cancelUrl)
                .addParams("cid", cid)
                .addParams("uid", uid)
                .addParams("day", day)
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
                        headView.setTag(s);
                        if (headView.getTag() != null) {
                            getShare();
                        }
                    }
                });
    }

    private void getShare() {
        String str = headView.getTag().toString();
        if (str != null || !str.equals("")) {
            Gson gson1 = new Gson();
            MyCoachAction.Result appResultAction = gson1.fromJson(str, MyCoachAction.Result.class);
            Toast.makeText(context, appResultAction.getReason(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "操作失效,请刷新当前页面重试", Toast.LENGTH_SHORT).show();
        }
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }


    /**
     * 预约
     *
     * @param day
     * @param time_slot
     */
    private void app(String day, String time_slot) {
        OkHttpUtils
                .post()
                .url(appUrl)
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
                        headView.setTag(s);
                        if (headView.getTag() != null) {
                            getShare();
                        }
                    }
                });
    }


}
