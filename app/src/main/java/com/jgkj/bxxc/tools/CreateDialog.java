package com.jgkj.bxxc.tools;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jgkj.bxxc.ChangeCoachActivity;
import com.jgkj.bxxc.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by fangzhou on 2016/12/26.
 */

public class CreateDialog implements View.OnClickListener{
    private Dialog sureDialog;
    private Context context;
    private View sureView;
    private TextView dialog_textView, dialog_sure, dialog_cancel;
    private String str;
    private String sureBtn,cancelBtn;
    private int uid;
    private ProgressDialog progressDialog;
    private int zhuangtai;
    private String token;
    private class Result {
        private int code;
        private String reason;

        public String getReason() {
            return reason;
        }

        public int getCode() {
            return code;
        }
    }
    private String url = "http://www.baixinxueche.com/index.php/Home/Apitokenupdata/confirmCoach";

    public CreateDialog(Context context, String str, String sureBtn, String cancelBtn,
                        int uid, int zhuangtai, String token) {
        this.context = context;
        this.str = str;
        this.sureBtn = sureBtn;
        this.cancelBtn = cancelBtn;
        this.uid = uid;
        this.zhuangtai = zhuangtai;
        this.token = token;
    }

    /**
     * 是否修改教练
     */
    public void createSureDialog() {
        sureDialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        // 填充对话框的布局
        sureView = LayoutInflater.from(context).inflate(
                R.layout.sure_cancel_dialog, null);
        // 初始化控件
        dialog_textView = (TextView) sureView.findViewById(R.id.dialog_textView);
        dialog_textView.setText(str);
        dialog_sure = (TextView) sureView.findViewById(R.id.dialog_sure);
        dialog_cancel = (TextView) sureView.findViewById(R.id.dialog_cancel);

        dialog_sure.setText(sureBtn);
        dialog_cancel.setText(cancelBtn);

        dialog_sure.setOnClickListener(this);
        dialog_cancel.setOnClickListener(this);
        // 将布局设置给Dialog
        sureDialog.setContentView(sureView);
        // 获取当前Activity所在的窗体
        Window dialogWindow = sureDialog.getWindow();
        // 设置dialog宽度
        dialogWindow.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置Dialog从窗体中间弹出
        dialogWindow.setGravity(Gravity.CENTER);
        sureDialog.show();
    }

    /**
     * 确定选择教练
     */
    private void sureChoose() {
        OkHttpUtils
                .post()
                .url(url)
                .addParams("uid", uid+"")//token
                .addParams("token", token)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        sureDialog.dismiss();
                        progressDialog.dismiss();
                        Toast.makeText(context, "网络状态不佳，请稍后再试", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onResponse(String s, int i) {
                        Gson gson = new Gson();
                        Result result = gson.fromJson(s,Result.class);
                        if(result.getCode()==200){
                            Toast.makeText(context, "修改成功，请刷新页面", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(context, result.getReason(), Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                        sureDialog.dismiss();
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dialog_cancel:
                Intent intent = new Intent();
                intent.setClass(context, ChangeCoachActivity.class);
                intent.putExtra("zhuangtai",zhuangtai);
                intent.putExtra("uid",uid);
                intent.putExtra("token",token);
                context.startActivity(intent);
                sureDialog.dismiss();
                break;
            case R.id.dialog_sure:
                progressDialog = ProgressDialog.show(context, null, "修改中...");
                sureChoose();
                break;
        }
    }
}
