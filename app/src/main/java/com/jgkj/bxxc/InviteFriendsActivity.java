package com.jgkj.bxxc;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jgkj.bxxc.bean.UserInfo;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

/**
 * Created by fangzhou on 2017/3/25.
 */

public class InviteFriendsActivity extends Activity implements View.OnClickListener {
    private TextView title;
    private Button button_back, button_forward;
    private TextView sina, weChat, wxCircle;
    UMImage image;
    private SharedPreferences sp;
    private UserInfo userInfo;
    private String shareUrl = "http://www.baixinxueche.com/index.php/Home/index/share?uid=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invite_friends);
        initView();
        // 验证是否登录
        sp = getSharedPreferences("USER",
                Activity.MODE_PRIVATE);
        int isFirstRun = sp.getInt("isfirst", 0);
        if (isFirstRun == 0) {
            Intent intent = new Intent();
            intent.setClass(InviteFriendsActivity.this, LoginActivity.class);
            intent.putExtra("message", "InviteFriendsActivity");
            startActivity(intent);
        }
    }

    private void initView() {

        Drawable xinlang = getResources().getDrawable(R.drawable.umeng_socialize_sina_on);
        xinlang.setBounds(0, 0, 100, 100);
        Drawable weixin = getResources().getDrawable(R.drawable.umeng_socialize_wechat);
        weixin.setBounds(0, 0, 100, 100);
        Drawable pengyouquan = getResources().getDrawable(R.drawable.umeng_socialize_wxcircle);
        pengyouquan.setBounds(0, 0, 100, 100);

        title = (TextView) findViewById(R.id.text_title);
        title.setText("邀请好友");
        button_back = (Button) findViewById(R.id.button_backward);
        button_back.setVisibility(View.VISIBLE);
        button_back.setOnClickListener(this);

        button_forward = (Button) findViewById(R.id.button_forward);
        button_forward.setText("邀请记录");
        button_forward.setVisibility(View.VISIBLE);
        button_forward.setOnClickListener(this);
        sina = (TextView) findViewById(R.id.sina);
        weChat = (TextView) findViewById(R.id.weChat);
        wxCircle = (TextView) findViewById(R.id.wxCircle);
        sina.setOnClickListener(this);
        weChat.setOnClickListener(this);
        wxCircle.setOnClickListener(this);

        sina.setCompoundDrawables(null, xinlang, null, null);
        weChat.setCompoundDrawables(null, weixin, null, null);
        wxCircle.setCompoundDrawables(null, pengyouquan, null, null);

    }

    //分享后回调方法
    private UMShareListener shareListener = new UMShareListener() {
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(InviteFriendsActivity.this, platform + " 分享成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(InviteFriendsActivity.this, platform + " 分享失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(InviteFriendsActivity.this, platform + " 分享取消", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onClick(View view) {

        userInfo = new Gson().fromJson(sp.getString("userInfo",null),UserInfo.class);
        image = new UMImage(InviteFriendsActivity.this, "http://www.baixinxueche.com/webshow/img/gift.png");
        switch (view.getId()) {

            case R.id.button_backward:
                finish();
                break;

            case R.id.button_forward:
                Intent intent = new Intent();
                intent.setClass(InviteFriendsActivity.this, InvitedToRecordActivity.class);
                startActivity(intent);
                break;

            case R.id.sina:
                /**
                 * setDisplayList方法是友盟内部封装好的，我们拿来调用就好了，
                 * 当我们需要多个分享时只需，在括号里面填写即可，不要另外的写dialog
                 * withTargetUrl设置分享链接，
                 * withTitle设置分享显示的标题
                 * withMedia设置分享图片等
                 * withText设置分享文本
                 * setCallback，当分享成功会回调setCallback此方法，用于显示分享动态
                 *
                 * tips：温馨提示,此版本只能分享到签名版的，debug版本无法分享，如果需要debug分享的话
                 *       请在各大平台上注册并完善debug和正式版本的信息填写，然后更换此项目的appkey
                 *
                 */

                new ShareAction(InviteFriendsActivity.this).setPlatform(SHARE_MEDIA.SINA)
                    .withText("科技改变生活，百信引领学车。学驾驶就用百信学车，方便快捷，更优惠！")
                    .withMedia(image)
                    .withTitle("和我一起来学车,更优惠!")
                    .withTargetUrl(shareUrl+userInfo.getResult().getUid())
                    .setCallback(shareListener)
                    .share();

                break;

            case R.id.weChat:

                new ShareAction(InviteFriendsActivity.this).setPlatform(SHARE_MEDIA.WEIXIN)
                        .withText("科技改变生活，百信引领学车。学驾驶就用百信学车，方便快捷，更优惠！")
                        .withMedia(image)
                        .withTargetUrl(shareUrl+userInfo.getResult().getUid())
                        .withTitle("和我一起来学车,更优惠!")
                        .setCallback(shareListener)
                        .share();

                break;

            case R.id.wxCircle:

                new ShareAction(InviteFriendsActivity.this).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                        .withText("和我一起来学车,更优惠!")
                        .withMedia(image)
                        .withTitle("科技改变生活，百信引领学车。学驾驶就用百信学车，方便快捷，更优惠！")
                        .withTargetUrl(shareUrl+userInfo.getResult().getUid())
                        .setCallback(shareListener)
                        .share();

                break;

        }
    }
}
