package com.jgkj.bxxc;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jgkj.bxxc.tools.StarBar;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by fangzhou on 2016/12/29.
 * 学员评价
 */

public class AppraiseActivity extends Activity implements View.OnClickListener, TextWatcher {
    private TextView title;
    private Button back, submit;
    private EditText editContext;
    private String timeid = "";
    private String token = "",uid = "";

    private StarBar starBar1, starBar2, starBar3;
    private TextView textLength;

    private ProgressDialog dialog;
    private String commentUrl = "http://www.baixinxueche.com/index.php/Home/Apitokenupdata/commentSave";

    private class PingJia {
        private int code;
        private String reason;

        public int getCode() {
            return code;
        }

        public String getReason() {
            return reason;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appraise);
        initView();
        getData();
    }

    private void initView() {
        title = (TextView) findViewById(R.id.text_title);
        title.setText("我的评价");
        back = (Button) findViewById(R.id.button_backward);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(this);

        starBar1 = (StarBar) findViewById(R.id.starBar1);
        starBar2 = (StarBar) findViewById(R.id.starBar2);
        starBar3 = (StarBar) findViewById(R.id.starBar3);
        starBar1.setIntegerMark(true);
        starBar2.setIntegerMark(true);
        starBar3.setIntegerMark(true);
        textLength = (TextView) findViewById(R.id.textLength);

        editContext = (EditText) findViewById(R.id.editContext);
        editContext.addTextChangedListener(this);
        submit = (Button) findViewById(R.id.button_forward);
        submit.setVisibility(View.VISIBLE);
        submit.setText("提交");
        submit.setOnClickListener(this);
    }

    //获取time_id
    private void getData() {
        Intent intent = getIntent();
        if (intent != null) {
            timeid = intent.getStringExtra("timeid");
            token = intent.getStringExtra("token");
            uid = intent.getIntExtra("uid",-1)+"";
        }
    }

    /**
     * 评价
     *
     * @param comment 评价内容
     */
    private void commentSave(String comment) {
        OkHttpUtils
                .post()
                .url(commentUrl)
                .addParams("timeid", timeid)
                .addParams("comment", comment)
                .addParams("token", token)
                .addParams("uid", uid)
                .addParams("zonghe", (int) starBar1.getStarMark() + "")
                .addParams("teach", (int) starBar2.getStarMark() + "")
                .addParams("wait", (int) starBar3.getStarMark() + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        dialog.dismiss();
                        Toast.makeText(AppraiseActivity.this, "发送失败", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        Gson gson = new Gson();
                        PingJia pj = gson.fromJson(s, PingJia.class);
                        if (pj.getCode() == 200) {
                            dialog.dismiss();
                            Toast.makeText(AppraiseActivity.this, pj.getReason(), Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            dialog.dismiss();
                            Toast.makeText(AppraiseActivity.this, pj.getReason(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_forward:
                int star1 = (int) starBar1.getStarMark();
                int star2 = (int) starBar2.getStarMark();
                int star3 = (int) starBar3.getStarMark();
                if (editContext.getText().toString().trim() == null || editContext.getText().toString().trim().equals("")) {
                    Toast.makeText(AppraiseActivity.this, "亲，您还没有输入任何内容哦", Toast.LENGTH_SHORT).show();
                } else if (star1 == 0) {
                    Toast.makeText(AppraiseActivity.this, "综合评价还未打分", Toast.LENGTH_SHORT).show();
                } else if (star2 == 0) {
                    Toast.makeText(AppraiseActivity.this, "服务态度还未打分", Toast.LENGTH_SHORT).show();
                } else if (star3 == 0) {
                    Toast.makeText(AppraiseActivity.this, "教学质量还未打分", Toast.LENGTH_SHORT).show();
                } else if (editContext.getText().toString().length() > 120) {
                    Toast.makeText(AppraiseActivity.this, "您输入的字数超出限制", Toast.LENGTH_SHORT).show();
                } else {
                    commentSave(editContext.getText().toString().trim());
                    dialog = ProgressDialog.show(AppraiseActivity.this, null, "评价提交中...");
                }
                break;
            case R.id.button_backward:
                finish();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.length() > 120) {
            Toast.makeText(AppraiseActivity.this, "您输入的字数超出限制", Toast.LENGTH_SHORT).show();
        }
        textLength.setText(charSequence.length() + "/120");
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
