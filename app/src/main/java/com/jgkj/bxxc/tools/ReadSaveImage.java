package com.jgkj.bxxc.tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by fangzhou on 2016/12/1.
 * 本地照片的存取
 */

public class ReadSaveImage {
    public static void saveImage(Bitmap bitmap, String path, String imageName) {
        File f = new File(Environment.getExternalStorageDirectory() + path);
        File f1 = new File(Environment.getExternalStorageDirectory() + path+imageName);
        FileOutputStream out = null;
        if (f1.exists()) {
            f1.delete();
        }
        if (!f.exists()) {
            f.mkdirs();
        }
        try {
            out = new FileOutputStream(f1);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
public static  boolean isExists(String pathString){
    File file = new File(Environment.getExternalStorageDirectory()+pathString);
    if (file.exists()) {
        return true;
    }else{
        return false;
    }
}

    //drawable转bitmap
    public static Bitmap drawableToBitmap(Drawable drawable) {
        //首先获取图片的高度和宽度
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap.Config config =
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        //注意，下面三行代码要用到，否在在View或者surfaceview里的canvas.drawBitmap会看不到图
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }
    /**
     * @param urlpath
     * @return Bitmap
     * 根据url获取布局背景的对象
     */
    public static Bitmap getBitMBitmap(String urlpath) {
        Bitmap map = null;
        try {
            URL url = new URL(urlpath);
            URLConnection conn = url.openConnection();
            conn.connect();
            InputStream in;
            in = conn.getInputStream();
            map = BitmapFactory.decodeStream(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }
    //读取本地图片
    public static Bitmap getDiskBitmap(String pathString) {
        Bitmap bitmap = null;
        try {
            File file = new File(Environment.getExternalStorageDirectory()+pathString);
            if (file.exists()) {
                bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+pathString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
