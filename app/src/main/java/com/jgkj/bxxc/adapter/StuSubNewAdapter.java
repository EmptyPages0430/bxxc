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

public class StuSubNewAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private CreateDay_Time createday;
    private List<CreateDay_Time> list;
    private ProgressDialog dialog;
    private String cid;
    private String uid;
    private String token;
    private Drawable draw1, draw2;

    private String appUrl = "http://www.baixinxueche.com/index.php/Home/Apiapplytoken/stuAppointmentFive";
    private String cancelUrl = "http://www.baixinxueche.com/index.php/Home/Apiapplytoken/deleteAppTimeLimitFive";

    public StuSubNewAdapter(Context context, List<CreateDay_Time> list,
                            String cid, String uid, String token) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
        this.cid = cid;
        this.uid = uid;
        this.token = token;
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
            view = inflater.inflate(R.layout.item_newgroup_listview,
                    viewGroup, false);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.imageView);
            viewHolder.imageView.setTag("down");
            viewHolder.btn1 = (Button) view.findViewById(R.id.radio_button_01);
            viewHolder.btn2 = (Button) view.findViewById(R.id.radio_button_02);
            viewHolder.btn3 = (Button) view.findViewById(R.id.radio_button_03);
            viewHolder.btn4 = (Button) view.findViewById(R.id.radio_button_04);
            viewHolder.btn5 = (Button) view.findViewById(R.id.radio_button_05);

            viewHolder.save = (Button) view.findViewById(R.id.save);
            viewHolder.day = (TextView) view.findViewById(R.id.day);
            viewHolder.isApp = (TextView) view.findViewById(R.id.isApp);//timeItem2

            viewHolder.linear = (LinearLayout) view.findViewById(R.id.layoutChild);
            viewHolder.timeItem2 = (LinearLayout) view.findViewById(R.id.timeItem2);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        createday = list.get(i);
        viewHolder.imageView.setTag("down");
        viewHolder.imageView.setImageResource(R.drawable.down);
        viewHolder.imageView.setOnClickListener(new OnClick(viewHolder.linear));

        viewHolder.btn1.setSelected(false);
        viewHolder.btn2.setSelected(false);
        viewHolder.btn3.setSelected(false);
        viewHolder.btn4.setSelected(false);
        viewHolder.btn5.setSelected(false);
        viewHolder.day.setText(createday.getDay());
        viewHolder.save.setTag(createday.getDay());
        switch (createday.getIsApp().size()) {
            case 1:
                viewHolder.btn1.setText(createday.getTime().get(0) +
                        "\n已预约" + createday.getCount().get(0) + "人");
                if (createday.getIsApp().get(0)) {
                    viewHolder.btn1.setSelected(true);
                    viewHolder.btn1.setEnabled(false);
                    viewHolder.btn2.setEnabled(false);
                    viewHolder.btn3.setEnabled(false);
                    viewHolder.btn4.setEnabled(false);
                    viewHolder.btn5.setEnabled(false);
                    viewHolder.save.setText("取消预约");
                    viewHolder.isApp.setText("已选课");
                    viewHolder.isApp.setTextColor(context.getResources().getColor(R.color.green));
                }
                viewHolder.btn2.setVisibility(View.INVISIBLE);
                viewHolder.btn3.setVisibility(View.INVISIBLE);
//                viewHolder.btn4.setVisibility(View.GONE);
//                viewHolder.btn5.setVisibility(View.GONE);
                viewHolder.timeItem2.setVisibility(View.GONE);

                break;
            case 2:
                viewHolder.btn1.setText(createday.getTime().get(0) +
                        "\n已预约" + createday.getCount().get(0) + "人");
                viewHolder.btn2.setText(createday.getTime().get(1) +
                        "\n已预约" + createday.getCount().get(1) + "人");
                if (createday.getIsApp().get(0)) {
                    viewHolder.btn1.setSelected(true);
                } else if (createday.getIsApp().get(1)) {
                    viewHolder.btn2.setSelected(true);
                }
                if (createday.getIsApp().get(0) || createday.getIsApp().get(1)) {
                    viewHolder.btn1.setEnabled(false);
                    viewHolder.btn2.setEnabled(false);
                    viewHolder.btn3.setEnabled(false);
                    viewHolder.btn4.setEnabled(false);
                    viewHolder.btn5.setEnabled(false);
                    viewHolder.isApp.setText("已选课");
                    viewHolder.save.setText("取消预约");
                    viewHolder.isApp.setTextColor(context.getResources().getColor(R.color.green));

                }
                viewHolder.btn3.setVisibility(View.INVISIBLE);
//                viewHolder.btn4.setVisibility(View.GONE);
//                viewHolder.btn5.setVisibility(View.GONE);
                viewHolder.timeItem2.setVisibility(View.GONE);
                break;
            case 3:
                viewHolder.btn1.setText(createday.getTime().get(0) +
                        "\n已预约" + createday.getCount().get(0) + "人");
                viewHolder.btn2.setText(createday.getTime().get(1) +
                        "\n已预约" + createday.getCount().get(1) + "人");
                viewHolder.btn3.setText(createday.getTime().get(2) +
                        "\n已预约" + createday.getCount().get(2) + "人");
                if (createday.getIsApp().get(0)) {
                    viewHolder.btn1.setSelected(true);
                } else if (createday.getIsApp().get(1)) {
                    viewHolder.btn2.setSelected(true);
                } else if (createday.getIsApp().get(2)) {
                    viewHolder.btn3.setSelected(true);
                }
                if (createday.getIsApp().get(0) || createday.getIsApp().get(1) || createday.getIsApp().get(2)
                        ) {
                    viewHolder.btn1.setEnabled(false);
                    viewHolder.btn2.setEnabled(false);
                    viewHolder.btn3.setEnabled(false);
                    viewHolder.btn4.setEnabled(false);
                    viewHolder.btn5.setEnabled(false);
                    viewHolder.isApp.setText("已选课");
                    viewHolder.save.setText("取消预约");
                    viewHolder.isApp.setTextColor(context.getResources().getColor(R.color.green));

                }
//                viewHolder.btn4.setVisibility(View.GONE);
//                viewHolder.btn5.setVisibility(View.GONE);
                viewHolder.timeItem2.setVisibility(View.GONE);
                break;
            case 4:
                viewHolder.btn1.setText(createday.getTime().get(0) +
                        "\n已预约" + createday.getCount().get(0) + "人");
                viewHolder.btn2.setText(createday.getTime().get(1) +
                        "\n已预约" + createday.getCount().get(1) + "人");
                viewHolder.btn3.setText(createday.getTime().get(2) +
                        "\n已预约" + createday.getCount().get(2) + "人");
                viewHolder.btn4.setText(createday.getTime().get(3) +
                        "\n已预约" + createday.getCount().get(3) + "人");
                if (createday.getIsApp().get(0)) {
                    viewHolder.btn1.setSelected(true);
                } else if (createday.getIsApp().get(1)) {
                    viewHolder.btn2.setSelected(true);
                } else if (createday.getIsApp().get(2)) {
                    viewHolder.btn3.setSelected(true);
                } else if (createday.getIsApp().get(3)) {
                    viewHolder.btn4.setSelected(true);
                }
                if (createday.getIsApp().get(0) || createday.getIsApp().get(1) || createday.getIsApp().get(2)
                        || createday.getIsApp().get(3)) {
                    viewHolder.btn1.setEnabled(false);
                    viewHolder.btn2.setEnabled(false);
                    viewHolder.btn3.setEnabled(false);
                    viewHolder.btn4.setEnabled(false);
                    viewHolder.btn5.setEnabled(false);
                    viewHolder.isApp.setText("已选课");
                    viewHolder.save.setText("取消预约");
                    viewHolder.isApp.setTextColor(context.getResources().getColor(R.color.green));
                }
                viewHolder.btn5.setVisibility(View.INVISIBLE);
                break;
            case 5:
                viewHolder.btn1.setText(createday.getTime().get(0) +
                        "\n已预约" + createday.getCount().get(0) + "人");
                viewHolder.btn2.setText(createday.getTime().get(1) +
                        "\n已预约" + createday.getCount().get(1) + "人");
                viewHolder.btn3.setText(createday.getTime().get(2) +
                        "\n已预约" + createday.getCount().get(2) + "人");
                viewHolder.btn4.setText(createday.getTime().get(3) +
                        "\n已预约" + createday.getCount().get(3) + "人");
                viewHolder.btn5.setText(createday.getTime().get(4) +
                        "\n已预约" + createday.getCount().get(4) + "人");
                if (createday.getIsApp().get(0)) {
                    viewHolder.btn1.setSelected(true);
                } else if (createday.getIsApp().get(1)) {
                    viewHolder.btn2.setSelected(true);
                } else if (createday.getIsApp().get(2)) {
                    viewHolder.btn3.setSelected(true);
                } else if (createday.getIsApp().get(3)) {
                    viewHolder.btn4.setSelected(true);
                } else if (createday.getIsApp().get(4)) {
                    viewHolder.btn5.setSelected(true);
                }
                if (createday.getIsApp().get(0) || createday.getIsApp().get(1) || createday.getIsApp().get(2)
                        || createday.getIsApp().get(3) || createday.getIsApp().get(4)) {
                    viewHolder.btn1.setEnabled(false);
                    viewHolder.btn2.setEnabled(false);
                    viewHolder.btn3.setEnabled(false);
                    viewHolder.btn4.setEnabled(false);
                    viewHolder.btn5.setEnabled(false);
                    viewHolder.isApp.setText("已选课");
                    viewHolder.save.setText("取消预约");
                    viewHolder.isApp.setTextColor(context.getResources().getColor(R.color.green));
                }
                break;
        }
        viewHolder.btn1.setOnClickListener(new rtbClick(viewHolder.btn1, viewHolder.btn2,
                viewHolder.btn3, viewHolder.btn4, viewHolder.btn5));
        viewHolder.btn2.setOnClickListener(new rtbClick(viewHolder.btn2, viewHolder.btn1,
                viewHolder.btn3, viewHolder.btn4, viewHolder.btn5));
        viewHolder.btn3.setOnClickListener(new rtbClick(viewHolder.btn3, viewHolder.btn2,
                viewHolder.btn1, viewHolder.btn4, viewHolder.btn5));
        viewHolder.btn4.setOnClickListener(new rtbClick(viewHolder.btn4, viewHolder.btn2,
                viewHolder.btn3, viewHolder.btn1, viewHolder.btn5));
        viewHolder.btn5.setOnClickListener(new rtbClick(viewHolder.btn5, viewHolder.btn2,
                viewHolder.btn1, viewHolder.btn4, viewHolder.btn3));

        viewHolder.save.setOnClickListener(new save(viewHolder.btn1, viewHolder.btn2,
                viewHolder.btn3, viewHolder.btn4, viewHolder.btn5, viewHolder.isApp));

        return view;
    }//

    private class rtbClick implements View.OnClickListener {
        private Button rtb1;
        private Button rtb2;
        private Button rtb3;
        private Button rtb4;
        private Button rtb5;

        public rtbClick(Button rtb1, Button rtb2, Button rtb3, Button rtb4, Button rtb5) {
            this.rtb1 = rtb1;
            this.rtb2 = rtb2;
            this.rtb3 = rtb3;
            this.rtb4 = rtb4;
            this.rtb5 = rtb5;
        }

        @Override
        public void onClick(View view) {
            rtb1.setSelected(true);
            rtb2.setSelected(false);
            rtb3.setSelected(false);
            rtb4.setSelected(false);
            rtb5.setSelected(false);
        }
    }

    /**
     * 保存提交
     */
    public class save implements View.OnClickListener {
        private Button btn1;
        private Button btn2;
        private Button btn3;
        private Button btn4;
        private Button btn5;
        private TextView isApp;

        public save(Button btn1, Button btn2, Button btn3, Button btn4, Button btn5, TextView isApp) {
            this.btn1 = btn1;
            this.btn2 = btn2;
            this.btn3 = btn3;
            this.btn4 = btn4;
            this.btn5 = btn5;
            this.isApp = isApp;
        }

        @Override
        public void onClick(View view) {
            String time = "";
            Button btn = (Button) view;
            if (btn1.isSelected()) {
                time = btn1.getText().toString().substring(0, 11).trim();
            } else if (btn2.isSelected()) {
                time = btn2.getText().toString().substring(0, 11).trim();
            } else if (btn3.isSelected()) {
                time = btn3.getText().toString().substring(0, 11).trim();
            } else if (btn4.isSelected()) {
                time = btn4.getText().toString().substring(0, 11).trim();
            } else if (btn5.isSelected()) {
                time = btn5.getText().toString().substring(0, 11).trim();
            }
            SetSubDialog subDialog = null;
            if (btn.getText().toString().equals("取消预约")) {
                subDialog = new SetSubDialog(context, "确定取消？", token, cid,uid, cancelUrl,
                        btn.getTag().toString(),time,btn, btn1, btn2, btn3,btn4, btn5, isApp);
                subDialog.call();
            } else {
                if (!time.equals("")) {
                    subDialog = new SetSubDialog(context, "确定预约" + time + "时间段?", token,  cid,uid, appUrl,
                            btn.getTag().toString(), time, btn, btn1, btn2, btn3,btn4, btn5, isApp);
                    subDialog.call();
                } else {
                    Toast.makeText(context, "请至少选择一个时间段在保存", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private class OnClick implements View.OnClickListener {
        private LinearLayout linear;

        public OnClick(LinearLayout linear) {
            this.linear = linear;
        }

        @Override
        public void onClick(View view) {
            ImageView img = (ImageView) view;
            if (img.getTag().toString().equals("down")) {
                img.setImageResource(R.drawable.up);
                img.setTag("up");
                linear.setVisibility(View.VISIBLE);
            } else if (img.getTag().toString().equals("up")) {
                img.setImageResource(R.drawable.down);
                img.setTag("down");
                linear.setVisibility(View.GONE);
            }
        }
    }

    /**
     * listviw优化
     */
    static class ViewHolder {
        public ImageView imageView;
        private LinearLayout linear,timeItem2;
        private Button btn1, btn2, btn3, btn4, btn5;
        public Button save;
        private TextView day, isApp;
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
                .addParams("token", token)
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
