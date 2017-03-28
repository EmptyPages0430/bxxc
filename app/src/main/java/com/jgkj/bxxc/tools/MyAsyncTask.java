package com.jgkj.bxxc.tools;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Message;

import com.jgkj.bxxc.bean.ItemEntity;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by Administrator on 2016/10/29.
 * AsyncTask异步解析
 */
public class MyAsyncTask extends AsyncTask<String,Void,byte[]> {
    private Context context;
    private ProgressDialog dialog;
    private android.os.Handler handler;
    public MyAsyncTask(android.os.Handler handler, ProgressDialog dialog, Context context){
        this.handler = handler;
        this.context = context;
        this.dialog = dialog;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(byte[] bytes) {
        //当doInBackground任务结束时候，立即被调用
        if(bytes!=null){
            try {
                String date = new String(bytes,"UTF-8");
                SharedPreferences sp2 = context.getSharedPreferences("subject1", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor2 = sp2.edit();
                editor2.putString("sub1", date);
                editor2.commit();
                List<ItemEntity> list = JsonTool.jsonStringList(date);
                Message message = handler.obtainMessage();
                message.obj = list;
                message.sendToTarget();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(dialog!=null){
            dialog.dismiss();
        }

    }
    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected byte[] doInBackground(String... params) {
        //子线程请求数据
        BufferedInputStream bis = null;
        //写数据
        ByteArrayOutputStream bos = null;
        try {
            URL url = new URL(params[0]);
            //根据url创建HttpURLConnection创建对象
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            bos = new ByteArrayOutputStream();
            if(connection.getResponseCode()==200){
                bis = new BufferedInputStream(connection.getInputStream());
                byte [] buffer = new byte[1024*10];
                int a = 0;
                while ((a = bis.read(buffer))!=-1){
                    bos.write(buffer,0,a);
                    bos.flush();
                }
                return bos.toByteArray();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
