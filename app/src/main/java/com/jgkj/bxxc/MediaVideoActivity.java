package com.jgkj.bxxc;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.gson.Gson;
import com.jgkj.bxxc.bean.VideoShow;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by fangzhou on 2017/3/13.
 * 视屏播放窗口
 */

public class MediaVideoActivity extends Activity {
    private VideoView videoView;
    //创建一个MediaController的对象用于控制视频的播放
    private MediaController mediaController;
    private String videoid;
    private VideoShow videoShow;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.videomedia);
        initView();
        getVideo();
    }

    //初始化布局控件并设置videoView的控制器
    private void initView() {

        videoid = getIntent().getStringExtra("videoid");

        videoView = (VideoView) findViewById(R.id.video);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        //获取界面上的VideoView组件
        //初始化mediaController
        mediaController = new MediaController(this);
        //将videoView与mediaController建立关联
        videoView.setMediaController(mediaController);
        //将mediaController与videoView建立关联
        mediaController.setMediaPlayer(videoView);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        dialog = ProgressDialog.show(MediaVideoActivity.this, null, "视频加载中...");

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                //Called when the video is ready to play
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });


    }

    //解析json填充数据
    private void setData() {
        Gson gson = new Gson();
        videoShow = gson.fromJson(videoView.getTag().toString(), VideoShow.class);
        if (videoShow.getCode() == 200) {
            //设置播放加载路径
            videoView.setVideoURI(Uri.parse(videoShow.getResult().getVideo()));
            videoView.requestFocus();
            //播放
            videoView.start();
        } else {
            Toast.makeText(MediaVideoActivity.this, videoShow.getReason(), Toast.LENGTH_LONG).show();
        }

    }

    /**
     * 获取视频展示页面
     */
    private void getVideo() {

        OkHttpUtils
                .post()
                .url("http://www.baixinxueche.com/index.php/Home/Apitoken/video")
                .addParams("videoid", videoid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        if (dialog.isShowing()) {
                            dialog.isShowing();
                        }
                        Toast.makeText(MediaVideoActivity.this, "网络异常，请检查网络！", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        videoView.setTag(s);
                        if (videoView.getTag() != null) {
                            setData();
                        }
                    }
                });
    }

}
