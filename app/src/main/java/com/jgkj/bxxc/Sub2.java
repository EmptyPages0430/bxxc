package com.jgkj.bxxc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jgkj.bxxc.bean.VideoMsg;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by fangzhou on 2017/3/13.
 * 科目二和科目三
 */

public class Sub2 extends Fragment implements View.OnClickListener {
    private ImageView videoview1, videoview2, videoview3, videoview4;
    private View view;
    private TextView zhuchetaban, zhidongtaban, houshijing, lihe, fangxiangpan, anquandai, zuoyi;
    private VideoMsg videoMsg;
    private TextView textView1, textView2, time1, time2;
    private TextView moreMedia;
    private ProgressDialog dialog;
    private LinearLayout learnSkills;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.subject2, container, false);
        initView();
        getVideoMedia();
        return view;
    }

    /**
     * 初始化控件
     */
    private void initView() {
        dialog = ProgressDialog.show(getActivity(), null, "数据加载中...");

        learnSkills = (LinearLayout) view.findViewById(R.id.learnSkills);
        learnSkills.setOnClickListener(this);

        videoview1 = (ImageView) view.findViewById(R.id.videoview1);
        videoview1.setOnClickListener(this);
        textView1 = (TextView) view.findViewById(R.id.textView1);
        time1 = (TextView) view.findViewById(R.id.time1);
        videoview2 = (ImageView) view.findViewById(R.id.videoview2);
        videoview2.setOnClickListener(this);
        textView2 = (TextView) view.findViewById(R.id.textView2);
        time2 = (TextView) view.findViewById(R.id.time2);

        moreMedia = (TextView) view.findViewById(R.id.moreMedia);
        moreMedia.setOnClickListener(this);

        zhuchetaban = (TextView) view.findViewById(R.id.zhuchetaban);
        zhidongtaban = (TextView) view.findViewById(R.id.zhidongtaban);
        houshijing = (TextView) view.findViewById(R.id.houshijing);
        lihe = (TextView) view.findViewById(R.id.lihe);
        fangxiangpan = (TextView) view.findViewById(R.id.fangxiangpan);
        anquandai = (TextView) view.findViewById(R.id.anquandai);
        zuoyi = (TextView) view.findViewById(R.id.zuoyi);
        zuoyi.setOnClickListener(this);
        anquandai.setOnClickListener(this);
        fangxiangpan.setOnClickListener(this);
        lihe.setOnClickListener(this);
        houshijing.setOnClickListener(this);
        zhidongtaban.setOnClickListener(this);
        zhuchetaban.setOnClickListener(this);
    }


    /**
     * 获取视频展示页面
     */
    private void getVideoMedia() {

        OkHttpUtils
                .post()
                .url("http://www.baixinxueche.com/index.php/Home/Apitoken/videoSub")
                .addParams("subject", "2")
                .addParams("limit", "2")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        Toast.makeText(getActivity(), "网络异常，请检查网络！", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        view.setTag(s);
                        if (view.getTag() != null) {
                            setData();
                        }
                    }
                });
    }

    private void setData() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        String tag = view.getTag().toString();
        Gson gson = new Gson();
        videoMsg = gson.fromJson(tag, VideoMsg.class);
        if (videoMsg.getCode() == 200) {
            Glide.with(getActivity())
                    .load(videoMsg.getResult().get(0).getVideopic())
                    .placeholder(R.drawable.coach_pic)
                    .error(R.drawable.coach_pic)
                    .into(videoview1);
            textView1.setText(videoMsg.getResult().get(0).getTitle());
            textView1.setTag(videoMsg.getResult().get(0).getVideoid());
            time1.setText(videoMsg.getResult().get(0).getTimes());

            Glide.with(getActivity())
                    .load(videoMsg.getResult().get(1).getVideopic())
                    .placeholder(R.drawable.coach_pic)
                    .error(R.drawable.coach_pic)
                    .into(videoview2);
            textView2.setText(videoMsg.getResult().get(1).getTitle());
            textView2.setTag(videoMsg.getResult().get(1).getVideoid());
            time2.setText(videoMsg.getResult().get(1).getTimes());

        } else {
            Toast.makeText(getActivity(), videoMsg.getReason(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.videoview1:
                intent.setClass(getActivity(), MediaVideoActivity.class);
                intent.putExtra("videoid", textView1.getTag().toString());
                startActivity(intent);
                break;
            case R.id.videoview2:
                intent.setClass(getActivity(), MediaVideoActivity.class);
                intent.putExtra("videoid", textView2.getTag().toString());
                startActivity(intent);
                break;
            case R.id.moreMedia:
                intent.setClass(getActivity(), TotalMediaActivity.class);
                startActivity(intent);
                break;
            case R.id.zhuchetaban:
                intent.setClass(getActivity(), WebViewActivity.class);
                intent.putExtra("url", "http://www.baixinxueche.com/webshow/keer/stop.html");
                intent.putExtra("title", "学车技巧");
                startActivity(intent);
                break;
            case R.id.zhidongtaban:
                intent.setClass(getActivity(), WebViewActivity.class);
                intent.putExtra("url", "http://www.baixinxueche.com/webshow/keer/brake.html");
                intent.putExtra("title", "学车技巧");
                startActivity(intent);
                break;
            case R.id.houshijing:
                intent.setClass(getActivity(), WebViewActivity.class);
                intent.putExtra("url", "http://www.baixinxueche.com/webshow/keer/rearview-mirror.html");
                intent.putExtra("title", "学车技巧");
                startActivity(intent);
                break;
            case R.id.lihe:
                intent.setClass(getActivity(), WebViewActivity.class);
                intent.putExtra("url", "http://www.baixinxueche.com/webshow/keer/clutch.html");
                intent.putExtra("title", "学车技巧");
                startActivity(intent);
                break;
            case R.id.fangxiangpan:
                intent.setClass(getActivity(), WebViewActivity.class);
                intent.putExtra("url", "http://www.baixinxueche.com/webshow/keer/wheel.html");
                intent.putExtra("title", "学车技巧");
                startActivity(intent);
                break;
            case R.id.anquandai:
                intent.setClass(getActivity(), WebViewActivity.class);
                intent.putExtra("url", "http://www.baixinxueche.com/webshow/keer/safe.html");
                intent.putExtra("title", "学车技巧");
                startActivity(intent);
                break;
            case R.id.zuoyi:
                intent.setClass(getActivity(), WebViewActivity.class);
                intent.putExtra("url", "http://www.baixinxueche.com/webshow/keer/zuoyi.html");
                intent.putExtra("title", "学车技巧");
                startActivity(intent);
                break;
            case R.id.learnSkills:
                intent.setClass(getActivity(), WebViewActivity.class);
                intent.putExtra("url", "http://www.baixinxueche.com/webshow/keer/keer.html");
                intent.putExtra("title", "考试技巧");
                startActivity(intent);
                break;

        }
    }
}
