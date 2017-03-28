package com.jgkj.bxxc;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jgkj.bxxc.bean.VideoMsg;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

import static android.view.View.VISIBLE;

/**
 * Created by fangzhou  on 2017/3/15.
 *
 * 展示所有的视频
 */

public class TotalMediaActivity extends Activity implements View.OnClickListener {

    private Button back;
    private TextView title;
    private TextView textView1,textView2,textView3,textView4,textView5,time1,time2
            ,time3,time4,time5;
    private ImageView videoview1,videoview2,videoview3,videoview4,videoview5;
    private VideoMsg videoMsg;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.total_media);
        initView();
        getVideoMedia();
    }
    private void initView(){
        dialog = ProgressDialog.show(TotalMediaActivity.this, null, "加载中...");
        back = (Button) findViewById(R.id.button_backward);
        back.setVisibility(VISIBLE);
        back.setOnClickListener(this);
        title = (TextView) findViewById(R.id.text_title);
        title.setText("科目二视频");

        videoview1 = (ImageView) findViewById(R.id.videoview1);
        videoview1.setOnClickListener(this);
        textView1 = (TextView) findViewById(R.id.textView1);
        time1 = (TextView) findViewById(R.id.time1);
        videoview2 = (ImageView) findViewById(R.id.videoview2);
        videoview2.setOnClickListener(this);
        textView2 = (TextView) findViewById(R.id.textView2);
        time2 = (TextView) findViewById(R.id.time2);

        videoview3 = (ImageView) findViewById(R.id.videoview3);
        videoview3.setOnClickListener(this);
        textView3 = (TextView) findViewById(R.id.textView3);
        time3 = (TextView) findViewById(R.id.time3);

        videoview4 = (ImageView) findViewById(R.id.videoview4);
        videoview4.setOnClickListener(this);
        textView4 = (TextView) findViewById(R.id.textView4);
        time4 = (TextView) findViewById(R.id.time4);

        videoview5 = (ImageView) findViewById(R.id.videoview1);
        videoview5.setOnClickListener(this);
        textView5 = (TextView) findViewById(R.id.textView5);
        time5 = (TextView) findViewById(R.id.time5);

    }
    /**
     * 获取视频展示页面
     */
    private void getVideoMedia() {

        OkHttpUtils
                .post()
                .url("http://www.baixinxueche.com/index.php/Home/Apitoken/videoSub")
                .addParams("subject", "2")
                .addParams("limit", "0")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        Toast.makeText(TotalMediaActivity.this, "网络异常，请检查网络！", Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onResponse(String s, int i) {
                        title.setTag(s);
                        if(title.getTag()!=null){
                            setData();
                        }
                    }
                });
    }
    private void setData(){
        if(dialog.isShowing()){
            dialog.dismiss();
        }
        Gson gson = new Gson();
        videoMsg = gson.fromJson(title.getTag().toString(),VideoMsg.class);
        if(videoMsg.getCode()==200){
            Glide.with(TotalMediaActivity.this)
                    .load(videoMsg.getResult().get(0).getVideopic())
                    .placeholder(R.drawable.coach_pic)
                    .error(R.drawable.coach_pic)
                    .into(videoview1);
            textView1.setText(videoMsg.getResult().get(0).getTitle());
            textView1.setTag(videoMsg.getResult().get(0).getVideoid());
            time1.setText(videoMsg.getResult().get(0).getTimes());

            Glide.with(TotalMediaActivity.this)
                    .load(videoMsg.getResult().get(1).getVideopic())
                    .placeholder(R.drawable.coach_pic)
                    .error(R.drawable.coach_pic)
                    .into(videoview2);
            textView2.setText(videoMsg.getResult().get(1).getTitle());
            textView2.setTag(videoMsg.getResult().get(1).getVideoid());
            time2.setText(videoMsg.getResult().get(1).getTimes());

            Glide.with(TotalMediaActivity.this)
                    .load(videoMsg.getResult().get(2).getVideopic())
                    .placeholder(R.drawable.coach_pic)
                    .error(R.drawable.coach_pic)
                    .into(videoview3);
            textView3.setText(videoMsg.getResult().get(2).getTitle());
            textView3.setTag(videoMsg.getResult().get(2).getVideoid());
            time3.setText(videoMsg.getResult().get(2).getTimes());

//            Glide.with(TotalMediaActivity.this)
//                    .load(videoMsg.getResult().get(3).getVideopic())
//                    .placeholder(R.drawable.coach_pic)
//                    .error(R.drawable.coach_pic)
//                    .into(videoview4);
//            textView4.setText(videoMsg.getResult().get(3).getTitle());
//            textView4.setTag(videoMsg.getResult().get(3).getVideoid());
//            time4.setText(videoMsg.getResult().get(3).getTimes());
//
//            Glide.with(TotalMediaActivity.this)
//                    .load(videoMsg.getResult().get(4).getVideopic())
//                    .placeholder(R.drawable.coach_pic)
//                    .error(R.drawable.coach_pic)
//                    .into(videoview2);
//            textView5.setText(videoMsg.getResult().get(4).getTitle());
//            textView5.setTag(videoMsg.getResult().get(4).getVideoid());
//            time5.setText(videoMsg.getResult().get(4).getTimes());

        }else{
            Toast.makeText(TotalMediaActivity.this, videoMsg.getReason(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()){
            case R.id.button_backward:
                finish();
                break;
            case R.id.videoview1:
                intent.setClass(TotalMediaActivity.this, MediaVideoActivity.class);
                intent.putExtra("videoid", textView1.getTag().toString());
                startActivity(intent);
                break;
            case R.id.videoview2:
                intent.setClass(TotalMediaActivity.this, MediaVideoActivity.class);
                intent.putExtra("videoid", textView2.getTag().toString());
                startActivity(intent);
                break;
            case R.id.videoview3:
                intent.setClass(TotalMediaActivity.this, MediaVideoActivity.class);
                intent.putExtra("videoid", textView3.getTag().toString());
                startActivity(intent);
                break;

        }
    }
}
