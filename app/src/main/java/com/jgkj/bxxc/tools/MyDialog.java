package com.jgkj.bxxc.tools;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jgkj.bxxc.R;

public class MyDialog extends Dialog {
	private String text;
	private Context context;
	private TextView bt;
	//类似于自定义View，必须实现一个非默认的构造方法
    public MyDialog(Context context) {
            super(context);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //设置对话框显示哪个布局文件
            setContentView(R.layout.training_field_detail);
            //对话框也可以通过资源id找到布局文件中的组件，从而设置点击侦听
            bt = (TextView) findViewById(R.id.training_filed_textView_id);
            bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            dismiss();
                    }
            });
    }
}
