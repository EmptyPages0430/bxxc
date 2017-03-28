package com.jgkj.bxxc.tools;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class PictureOptimization {
	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
														 int reqWidth, int reqHeight) {
		// 第一解码injustdecodebounds =真正的检查尺寸
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);
		//计算insamplesize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		// 解码insamplesize设置位图
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}
    public static Drawable bitmapToDrawble(Bitmap bitmap, Context mcontext){
        Drawable drawable = new BitmapDrawable(mcontext.getResources(), bitmap);
        return drawable;  
  } 
public static int calculateInSampleSize(
		BitmapFactory.Options options, int reqWidth, int reqHeight) {
	// 图像原始高度和宽度
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			// 计算高度和宽度的比例要求的高度和宽度
		final int heightRatio = Math.round((float) height / (float) reqHeight);
		final int widthRatio = Math.round((float) width / (float) reqWidth);
		
		/* 
		 *  选择比最小为insamplesize值，这将保证
			一个最终的图像，这两个维度大于或等于
			请求的高度和宽度
		*/
		inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

}
