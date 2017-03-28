package com.jgkj.bxxc.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by fangzhou on 2016/11/14.
 * 读取url图片并转化为bitmap类型
 */
public class BitmapFromURL {
    private Context context;
    public BitmapFromURL(Context context){
        this.context = context;
    }
    //imageView加载url
    public Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);

            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context,"图片加载失败！", Toast.LENGTH_SHORT).show();
            return null;
        }
    }
}
