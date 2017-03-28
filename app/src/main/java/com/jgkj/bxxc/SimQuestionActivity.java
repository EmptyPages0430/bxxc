package com.jgkj.bxxc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jgkj.bxxc.tools.PictureOptimization;

/**
 * Created by fangzhou on 2016/11/5.
 */
public class SimQuestionActivity extends Activity implements View.OnClickListener {
    private Button back_forward;
    private TextView text_title;
    private ImageView sim_ImageView;
    private String str;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sim_question);
        Intent intent = getIntent();
        str = intent.getStringExtra("image");
        init();
    }
    private void init() {
        back_forward = (Button) findViewById(R.id.button_backward);
        back_forward.setVisibility(View.VISIBLE);
        text_title = (TextView) findViewById(R.id.text_title);

        back_forward.setOnClickListener(this);
        sim_ImageView = (ImageView) findViewById(R.id.sim_ImageView);
        if(str.equals("question")){
            sim_ImageView.setBackgroundDrawable(PictureOptimization.bitmapToDrawble(PictureOptimization.decodeSampledBitmapFromResource(getResources(),
                    R.drawable.sim_question,720, 1280), SimQuestionActivity.this));
            text_title.setText("常见问题");
        }else if(str.equals("aboutbaixinLayout")){
            sim_ImageView.setBackgroundDrawable(PictureOptimization.bitmapToDrawble(PictureOptimization.decodeSampledBitmapFromResource(getResources(),
                    R.drawable.aboutbaixin, 480, 800), SimQuestionActivity.this));
            text_title.setText("关于百信");
        }else if(str.equals("jiesongLayout")){
            sim_ImageView.setBackgroundDrawable(PictureOptimization.bitmapToDrawble(PictureOptimization.decodeSampledBitmapFromResource(getResources(),
                    R.drawable.aboutcoach, 480, 800), SimQuestionActivity.this));
            text_title.setText("关于教练");
        }else if(str.equals("chengnuoLayout")){
            sim_ImageView.setBackgroundDrawable(PictureOptimization.bitmapToDrawble(PictureOptimization.decodeSampledBitmapFromResource(getResources(),
                    R.drawable.chengnuo, 480, 800), SimQuestionActivity.this));
            text_title.setText("服务承诺");
        }else if(str.equals("liuchengLayout")){
            sim_ImageView.setBackgroundDrawable(PictureOptimization.bitmapToDrawble(PictureOptimization.decodeSampledBitmapFromResource(getResources(),
                    R.drawable.xuecheliucheng, 480, 800), SimQuestionActivity.this));
            text_title.setText("学车流程");
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_backward:
                finish();
                break;
        }
    }
}
