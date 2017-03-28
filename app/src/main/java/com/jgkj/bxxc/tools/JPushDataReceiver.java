package com.jgkj.bxxc.tools;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.jgkj.bxxc.HomeActivity;
import com.jgkj.bxxc.LearnHisActivity;
import com.jgkj.bxxc.LoginActivity;
import com.jgkj.bxxc.MyActivity;
import com.jgkj.bxxc.MyCoachActivity;
import com.jgkj.bxxc.bean.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by fangzhou on 2016/10/22.
 * 自定义接收器
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class JPushDataReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";
    //读取本地信息
    private SharedPreferences sp, sp1;
    private SharedPreferences.Editor editor;
    private UserInfo.Result result;
    private int isFirstRun;
    private String token;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
        /**
         * 读取本地信息
         * 判断是否登录过了，没有登录的状态下，点击推送消息，跳到登录页面
         */
        sp = context.getSharedPreferences("USER",
                Activity.MODE_PRIVATE);
        String userStr = sp.getString("userInfo", null);
        if(userStr!=null){
            Gson gson = new Gson();
            result = gson.fromJson(userStr, UserInfo.class).getResult();
            isFirstRun = sp.getInt("isfirst", 0);
        }
        /**
         * 读取token
         */
        sp1 = context.getSharedPreferences("token",
                Activity.MODE_PRIVATE);
        token = sp1.getString("token", null);
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver]    接收Registration Id : " + regId);

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            processCustomMessage(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
            /**
             * 打开自定义的Activity
             * 根据不同的推送信息，来选择跳转到哪里
             *
             * title ：百信学车
             * content ：推送内容
             * type ：推送数据，自定义的键值对，用来判断跳转到哪里去，需要json解析
             */
            String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
            String content = bundle.getString(JPushInterface.EXTRA_ALERT);
            String type = bundle.getString(JPushInterface.EXTRA_EXTRA);
            try {
                JSONObject jsonObject = new JSONObject(type);
                String str = jsonObject.getString("type");
                Intent i = new Intent();
                if (isFirstRun != 0) {
                    if (str != null) {
                        if(str.equals("comment")){
                            i.setClass(context, LearnHisActivity.class);
                            i.putExtra("state", result.getState());
                            i.putExtra("token", token);
                            i.putExtra("uid", result.getUid());
                        }else if(str.equals("apply")){
                            i.setClass(context, MyCoachActivity.class);
                            i.putExtra("state", result.getState());
                            i.putExtra("token", token);
                            i.putExtra("uid", result.getUid());
                        }else if(str.equals("activity")){
                            i.setClass(context, MyActivity.class);
                        }else{
                            i.setClass(context, HomeActivity.class);
                            i.putExtra("FromActivity", "WelcomeActivity");
                        }
                    }
                } else {
                    i.setClass(context, LoginActivity.class);
                }
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.i(TAG,"跳转信息错误");
                Intent i = new Intent(context, HomeActivity.class);
                intent.putExtra("FromActivity", "WelcomeActivity");
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
            }
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等...
        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }
    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA) == null) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }
                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }
            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    //send msg to HomeActivity
    private void processCustomMessage(Context context, Bundle bundle) {
        if (HomeActivity.isForeground) {
            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            Intent msgIntent = new Intent(HomeActivity.MESSAGE_RECEIVED_ACTION);
            msgIntent.putExtra(HomeActivity.KEY_MESSAGE, message);
            if (!JPushDataUitl.isEmpty(extras)) {
                try {
                    JSONObject extraJson = new JSONObject(extras);
                    if (null != extraJson && extraJson.length() > 0) {
                        msgIntent.putExtra(HomeActivity.KEY_EXTRAS, extras);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            context.sendBroadcast(msgIntent);
        }
    }
}
