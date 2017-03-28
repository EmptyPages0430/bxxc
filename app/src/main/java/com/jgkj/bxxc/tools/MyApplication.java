package com.jgkj.bxxc.tools;
import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.baidu.mapapi.SDKInitializer;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import cn.jpush.android.api.JPushInterface;

public class MyApplication extends Application {
    public static RequestQueue queue;
    @Override
    public void onCreate() {
        super.onCreate();
        UMShareAPI.get(getApplicationContext());
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(getApplicationContext());
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
        //创建队列
        queue = Volley.newRequestQueue(getApplicationContext());
    }
    {
//    	微信
        PlatformConfig.setWeixin("wx75b78ead0e64a547","16a2704c6eab845d6170899f201d2321");
        //新浪
        PlatformConfig.setSinaWeibo("6681699", "c2318d8d8d8bd0906297aad3394abeec");
    }
    //暴漏一个方法返回请求队列
    public static RequestQueue getQueue() {
        return queue;
    }

}