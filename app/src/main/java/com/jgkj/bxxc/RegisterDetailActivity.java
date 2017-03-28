package com.jgkj.bxxc;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jgkj.bxxc.bean.UserInfo;
import com.lidroid.xutils.http.client.multipart.MultipartEntity;
import com.lidroid.xutils.http.client.multipart.content.FileBody;
import com.lidroid.xutils.http.client.multipart.content.StringBody;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * 提交个人信息照片
 * 上传身份证正反面，上半身，和五指健全照片
 */
public class RegisterDetailActivity extends Activity implements View.OnClickListener {
    private TextView title;
    private Button back_forward;
    //上传imageView
    private ImageView pic1, pic2, pic3, pic4;
    //样式imageView
    private ImageView style1, style2, style3, style4;
    private Dialog dialog, look_dialog;
    private View inflate, look_inflate;
    private ImageView look_image;
    private Button choosePhoto;
    private Button takePhoto;
    private Button she_cancel;
    private boolean flag = false;

    private UserInfo.Result result;
    private Uri corpUri;

    // 拍照
    private static int CAMERA_REQUEST_CODE = 1001;
    // 相册选图
    private static int CHOOSE_PICTIRE = 1002;
    // 裁剪
    private static int CROP_REQUEST_CODE = 1003;
    //4张照片的本地url
    private String url1, url2, url3, url4;
    private Intent intent;
    private int uid;
    private long random, random1, random2, random3, random4;
    //上传照片
    private Button submit;
    private Handler mHandler;

    private ProgressDialog proDialog;
    private String imageName = null;
    private Uri nowUri;
    private String oldupLoadUrls = "http://www.baixinxueche.com/index.php/Home/Api/get_files";
    private String upLoadUrls = "http://www.baixinxueche.com/index.php/Home/Api/get_filesAgain";

    private class Result {
        private String code;
        private String reason;

        public String getCode() {
            return code;
        }

        public String getReason() {
            return reason;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registerdetail);
        init();
        initData();
    }

    /**
     * 初始化数据，读取本地个人信息，和生成图片名
     */
    private void initData() {
        SharedPreferences sp = getSharedPreferences("USER",
                Activity.MODE_PRIVATE);
        String str = sp.getString("userInfo", null);
        Gson gson = new Gson();
        UserInfo userInfo = gson.fromJson(str, UserInfo.class);
        result = userInfo.getResult();
        uid = result.getUid();
        random1 = System.currentTimeMillis();
        random2 = System.currentTimeMillis() + 1;
        random3 = System.currentTimeMillis() + 2;
        random4 = System.currentTimeMillis() + 3;
    }

    /**
     * 初始化控件
     */
    private void init() {
        title = (TextView) findViewById(R.id.text_title);
        title.setText("完善信息");
        back_forward = (Button) findViewById(R.id.button_backward);
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(this);
        //上传imageView实例化
        pic1 = (ImageView) findViewById(R.id.pic1);
        pic2 = (ImageView) findViewById(R.id.pic2);
        pic3 = (ImageView) findViewById(R.id.pic3);
        pic4 = (ImageView) findViewById(R.id.pic4);
        //样式imageView实例化
        style1 = (ImageView) findViewById(R.id.style1);
        style2 = (ImageView) findViewById(R.id.style2);
        style3 = (ImageView) findViewById(R.id.style3);
        style4 = (ImageView) findViewById(R.id.style4);
    }

    /**
     * 图片点击监听方法
     *
     * @param v 视图
     */
    public void showPic(View v) {
        switch (v.getId()) {
            case R.id.pic1:
                creatDialog();
                setTag(pic1, pic2, pic3, pic4);
                break;
            case R.id.pic2:
                creatDialog();
                setTag(pic2, pic1, pic3, pic4);
                break;
            case R.id.pic3:
                creatDialog();
                setTag(pic3, pic2, pic1, pic4);
                break;
            case R.id.pic4:
                creatDialog();
                setTag(pic4, pic3, pic2, pic1);
                break;
        }
    }

    /**
     * 上传照片
     * @param http_url 请求地址
     * @param filepath1 图片地址
     * @param filepath2
     * @param filepath3
     * @param filepath4
     * @param uid 用户id
     */
    public void uploadImage(final String http_url, final String filepath1, final String filepath2
            , final String filepath3, final String filepath4, final String uid) {
        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                Gson gson = new Gson();
                final Result res = gson.fromJson(msg.obj.toString(), Result.class);
                if (res.getCode().equals("200")) {
                    Toast.makeText(RegisterDetailActivity.this, res.getReason(), Toast.LENGTH_SHORT).show();
                    Intent login = new Intent();
                    login.setClass(RegisterDetailActivity.this, HomeActivity.class);
                    login.putExtra("FromActivity", "MySetting");
                    startActivity(login);
                    finish();
                } else {
                    Toast.makeText(RegisterDetailActivity.this, res.getReason(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File file1 = new File(filepath1);
                    File file2 = new File(filepath2);
                    File file3 = new File(filepath3);
                    File file4 = new File(filepath4);

                    if (!file1.exists() || !file2.exists() || !file3.exists() || !file4.exists()) {
                        Log.i("错误", "文件不存在");
                    }

                    HttpClient client = new DefaultHttpClient();

                    HttpPost post = new HttpPost(http_url);

                    FileBody fileBody1 = new FileBody(file1, "image/jpg");
                    FileBody fileBody2 = new FileBody(file2, "image/jpg");
                    FileBody fileBody3 = new FileBody(file3, "image/jpg");
                    FileBody fileBody4 = new FileBody(file4, "image/jpg");
                    MultipartEntity entity = new MultipartEntity();

                    entity.addPart("idCard1", fileBody1);//uploadedfile是图片上传的键值名
                    entity.addPart("idCard2", fileBody2);
                    entity.addPart("upbody", fileBody3);
                    entity.addPart("finger", fileBody4);
                    entity.addPart("uid", new StringBody(uid, Charset.forName("UTF-8")));//设置要传入的参数，key_app是键值名,此外传参时候需要指定编码格式
                    post.setEntity(entity);
                    HttpResponse response = client.execute(post);

                    if (response.getStatusLine().getStatusCode() == 200) {
                        HttpEntity httpEntity = response.getEntity();
                        final String result = EntityUtils.toString(httpEntity, "utf-8");
                        proDialog.dismiss();
                        Message msg = new Message();
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                    } else {
                        Toast.makeText(RegisterDetailActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    proDialog.dismiss();
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void setTag(ImageView current, ImageView img2, ImageView img3, ImageView img4) {
        current.setTag("CHICK");
        img2.setTag("UNCHICK");
        img3.setTag("UNCHICK");
        img4.setTag("UNCHICK");
    }

    private void getTag() {
        if (pic1.getTag().toString().equals("CHICK")) {
            imageName = "idcard1" + uid + random1;
        } else if (pic2.getTag().toString().equals("CHICK")) {
            imageName = "idcard2" + uid + random2;
        } else if (pic3.getTag().toString().equals("CHICK")) {
            imageName = "upbody" + uid + random3;
        } else if (pic4.getTag().toString().equals("CHICK")) {
            imageName = "finger" + uid + random4;
        }
    }

    /**
     * 摄像头拍照或者调用图片库存
     */
    private void creatDialog() {
        dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        inflate = LayoutInflater.from(this).inflate(R.layout.she_dialog, null);
        choosePhoto = (Button) inflate.findViewById(R.id.choosePhoto);
        takePhoto = (Button) inflate.findViewById(R.id.takePhoto);
        she_cancel = (Button) inflate.findViewById(R.id.she_cancel);
        she_cancel.setOnClickListener(this);
        choosePhoto.setOnClickListener(this);
        takePhoto.setOnClickListener(this);
        // 将布局设置给Dialog
        dialog.setContentView(inflate);
        // 获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        // 设置dialog横向充满
        dialogWindow.setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        // 获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;// 设置Dialog距离底部的距离
        // 将属性设置给窗体
        dialogWindow.setAttributes(lp);
        dialog.show();// 显示对话框
    }

    /**
     * 回调函数，处理头像设置
     * @param requestCode 请求码
     * @param resultCode 返回码
     * @param data 返回数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap = null;
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (nowUri != null){
                startImgZoom(nowUri);
            }
        } else if (requestCode == CHOOSE_PICTIRE) {
            if (data == null) {
                return;
            } else {
                Uri uri;
                uri = data.getData();
                Uri fileUri = convertUri(uri);
                startImgZoom(fileUri);
            }
        } else if (requestCode == CROP_REQUEST_CODE) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver()
                        , corpUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
                if (pic1.getTag() == "CHICK") {
                    pic1.setImageBitmap(bitmap);
                } else if (pic2.getTag() == "CHICK") {
                    pic2.setImageBitmap(bitmap);
                } else if (pic3.getTag() == "CHICK") {
                    pic3.setImageBitmap(bitmap);
                } else if (pic4.getTag() == "CHICK") {
                    pic4.setImageBitmap(bitmap);
                }
        }
    }

    /**
     *
     * 图像裁剪
     * @param uri 图片uri
     */
    private void startImgZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("return-data", false);//设置为不返回数据
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, corpUri);
        intent.putExtra("scale", true);
        startActivityForResult(intent, CROP_REQUEST_CODE);
    }

    //吧bitmap转化为Uri
    private Uri saveBitmap(Bitmap bitmap) {
        File tmpDir = new File(Environment.getExternalStorageDirectory()
                + "/baixinxueche/image/");
        if (!tmpDir.exists()) {
            tmpDir.mkdirs();
        }
        getTag();
        File img = new File(tmpDir.getAbsoluteFile() + "/" + imageName + ".png");
        try {
            FileOutputStream fos = new FileOutputStream(img);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            corpUri = Uri.fromFile(img);
            return corpUri;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 转换Uri
    private Uri convertUri(Uri uri) {
        InputStream is = null;
        try {
            is = getContentResolver().openInputStream(uri);
            Bitmap bm = BitmapFactory.decodeStream(is);
            is.close();
            return saveBitmap(bm);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 设置图片保存路径
     * @return
     */
    private File getImageStoragePath(Context context){
        File tmpDir = new File(Environment.getExternalStorageDirectory()
                + "/baixinxueche/image/");
        if (!tmpDir.exists()) {
            tmpDir.mkdirs();
        }
        getTag();
        File img = new File(tmpDir.getAbsoluteFile() + "/" + imageName + ".png");
        try {
            FileOutputStream fos = new FileOutputStream(img);
            fos.flush();
            fos.close();
            corpUri = Uri.fromFile(img);
            return img;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_backward:
                finish();
                break;
            case R.id.takePhoto:
                nowUri = Uri.fromFile(getImageStoragePath(this));
                Intent intent_takePhoto = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                intent_takePhoto.putExtra(MediaStore.EXTRA_OUTPUT, nowUri);
                startActivityForResult(intent_takePhoto, CAMERA_REQUEST_CODE);
                dialog.dismiss();
                break;
            case R.id.choosePhoto:
                Intent action_getPhoto = new Intent(Intent.ACTION_GET_CONTENT);
                action_getPhoto.setType("image/*");
                startActivityForResult(action_getPhoto, CHOOSE_PICTIRE);
                dialog.dismiss();
                break;
            case R.id.she_cancel:
                dialog.dismiss();
                break;
            case R.id.submit:
                proDialog = ProgressDialog.show(RegisterDetailActivity.this, null, "上传中...");
                uploadImage(upLoadUrls,
                        Environment.getExternalStorageDirectory() + "/baixinxueche/image/" + "idcard1" + uid + random1 + ".png",
                        Environment.getExternalStorageDirectory() + "/baixinxueche/image/" + "idcard2" + uid + random2 + ".png",
                        Environment.getExternalStorageDirectory() + "/baixinxueche/image/" + "upbody" + uid + random3 + ".png",
                        Environment.getExternalStorageDirectory() + "/baixinxueche/image/" + "finger" + uid + random4 + ".png",
                        uid + "");

                break;

        }
    }
}
