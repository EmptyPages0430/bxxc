package com.jgkj.bxxc.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by fangzhou on 2016/11/15.
 * 异步读取图片
 */
public class BitmapAsncyTask extends AsyncTask<String,Void,byte[]> {
    private Context context;
    private ImageView imageView;
    private String imageName;
    public BitmapAsncyTask(ImageView imageView, String imageName, Context context){
        this.context = context;
        this.imageView = imageView;
        this.imageName = imageName;
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
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageView.setImageBitmap(bitmap);
                ReadSaveImage.saveImage(bitmap, //文件的保存位置
                        "/baixinxueche/image/",imageName);
            } catch (Exception e) {
                e.printStackTrace();
            }
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