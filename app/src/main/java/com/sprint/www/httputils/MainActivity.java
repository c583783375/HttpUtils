package com.sprint.www.httputils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sprint.www.httputils.bean.Content;
import com.sprint.www.httputils.http.utils.HttpCallback;
import com.sprint.www.httputils.http.HttpProxy;
import com.sprint.www.httputils.http.utils.ICallBack;
import com.sprint.www.httputils.http.utils.ProgressListener;
import com.sprint.www.httputils.http.utils.State;
import com.sprint.www.httputils.loader.GlideImageLoader;
import com.yancy.gallerypick.config.GalleryConfig;
import com.yancy.gallerypick.config.GalleryPick;
import com.yancy.gallerypick.inter.IHandlerCallBack;

import java.io.File;
import java.text.NumberFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private String TAG = "---http_image_select---";
    private Context mContext;
    private Activity mActivity;
    private final int PERMISSIONS_REQUEST_READ_CONTACTS = 8;
    private IHandlerCallBack iHandlerCallBack;
    private GalleryConfig galleryConfig;

    private List<String> path = new ArrayList<>();
    private TextView tv;
    private ImageView img;
    StringBuffer sb = new StringBuffer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        mActivity = this;
        initGallery();
        initView();
        tv = (TextView) findViewById(R.id.tv);
        img = (ImageView) findViewById(R.id.main_img);
//        Log.e(TAG,Environment.getExternalStorageDirectory()+"/down/1510903638457ohy.jpg");
//        Glide.with(mContext)
//                .load(Environment.getExternalStorageDirectory()+"/down/1510903638457ohy.jpg")
//                .centerCrop()
//                .into(img);
    }

    private void initView() {
        galleryConfig = new GalleryConfig.Builder()
                .imageLoader(new GlideImageLoader())    // ImageLoader 加载框架（必填）
                .iHandlerCallBack(iHandlerCallBack)     // 监听接口（必填）
                .pathList(path)                         // 记录已选的图片
                .multiSelect(false)                      // 是否多选   默认：false
                .provider("com.sprint.www.httputils.fileprovider")
                .multiSelect(false, 9)                   // 配置是否多选的同时 配置多选数量   默认：false ， 9
                .maxSize(9)                             // 配置多选时 的多选数量。    默认：9
                .crop(false)                             // 快捷开启裁剪功能，仅当单选 或直接开启相机时有效
                .crop(false, 1, 1, 500, 500)             // 配置裁剪功能的参数，   默认裁剪比例 1:1
                .isShowCamera(true)                     // 是否现实相机按钮  默认：false
                .filePath("/Sprint/Pictures")
                .build();
    }

    public void onBtn(View v){
        //aa();
      //  downFile();
       // galleryConfig.getBuilder().isOpenCamera(false).build();
        initPermissions();
    }

    public void onCommit(View v){
//       uploadImage();
        downFile();
    }

    NumberFormat format = NumberFormat.getPercentInstance();// 获取格式化类实例
    private void uploadImage(){

        format.setMinimumFractionDigits(2);// 设置小数位
        if(path == null || path .size() <=0){return;}
        String url = "http://106.14.217.169:8082/Qt/Test/uploadFile";

        Map<String, File> filesMap = new HashMap<>();
        File file = new File(path.get(0));
        filesMap.put("img",file);
        filesMap.put("img1",file);

        List<String> fileTypes = new ArrayList<>();
        fileTypes.add("image");
        fileTypes.add("image");

        Map<String, String> paramsMap = new HashMap<>();//fileTypes.toArray(new String[fileTypes.size()])
        paramsMap.put("id","11");
        HttpProxy.obtain().uploadFile(url, filesMap,fileTypes , paramsMap, new ProgressListener() {
            @Override
            public void onProgress(final long totalSize,final long currSize,final boolean done,final int id) {
                Log.e(TAG,"上传中"+totalSize+"="+currSize+"="+done+"="+id);

                tv.post(new Runnable() {
                    @Override
                    public void run() {
                        tv.setText("总大小："+totalSize+"当前完成大小："+currSize+"=="+done+"编号："+id+"=="+format.format((float)currSize/totalSize));
                    }
                });


            }
        }, new HttpCallback<String>() {

            @Override
            public void onSuccess(String s) {
                Log.e(TAG,"上传完成"+s);
                tv.setText("上传完成");
            }

            @Override
            public void onFailure(State state, String error) {
                Log.e(TAG,"上传失败"+error);
                tv.setText("上传失败");
            }
        });

    }

    private void initGallery() {
        iHandlerCallBack = new IHandlerCallBack() {
            @Override
            public void onStart() {
                Log.i(TAG, "onStart: 开启");
            }

            @Override
            public void onSuccess(List<String> photoList) {
                Log.i(TAG, "onSuccess: 返回数据");
                path.clear();
                for (String s : photoList) {
                    Log.i(TAG, s);
                    path.add(s);
                }
                Log.e(TAG,path.toString());
                //photoAdapter.notifyDataSetChanged();
                if(path != null && path .size() >0){
                    Glide.with(mContext)
                            .load(path.get(0))
                            .centerCrop()
                            .into(img);
                }

            }

            @Override
            public void onCancel() {
                Log.i(TAG, "onCancel: 取消");
            }

            @Override
            public void onFinish() {
                Log.i(TAG, "onFinish: 结束");
            }

            @Override
            public void onError() {
                Log.i(TAG, "onError: 出错");
            }
        };

    }



    private void aa() {
//        String url = "http://lf.snssdk.com/article/category/get_subscribed/v2/?";
        String url = "http://lf.snssdk.com/api/news/feed/v64/?";
        Map<String, String> params = new HashMap<>();
        params.put("iid", "134852330716");
        params.put("loc_mode", "7");
        params.put("category", "news_local");
        params.put("refer", "1");
        params.put("count", "20");
        params.put("min_behot_time", "1502237751");
        params.put("loc_time", "1506404359");
        HttpProxy.obtain().post(url, params, new HttpCallback<Content>() {
            @Override
            public void onSuccess(Content response) {
//                sb.append(response.toString());
                tv.setText(response.getData().get(4).toString());
            }

            @Override
            public void onFailure(State state,String error) {
                if(State.NETWORK_FAILURE == state){
                    tv.setText(error);
                }else{
                    tv.setText(error);
                }

            }
        });
    }

    private void downFile(){
        format.setMinimumFractionDigits(2);// 设置小数位
        String url = "http://106.14.217.169:8082/upload/2017-11-17/1510903638457ohy.jpg";
       // String url = "http://192.168.1.188:8888/1476329828897.jpg";
        HttpProxy.obtain().downLoadFile(url, null,"down", new ProgressListener() {
            @Override
            public void onProgress(final long totalSize, final long currSize, final boolean done, final int id) {

                tv.post(new Runnable() {
                    @Override
                    public void run() {
                        tv.setText("总大小："+totalSize+"当前完成大小："+currSize+"=="+done+"编号："+id+"=="+format.format((float)currSize/totalSize));
                    }
                });
            }
        }, new ICallBack() {
            @Override
            public void onSuccess(String response) {
                tv.setText("下载完成");
            }

            @Override
            public void onFailure(State state, String error) {
                tv.setText("下载失败");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HttpProxy.obtain().cancelAll();
    }

    // 授权管理
    private void initPermissions() {
        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "需要授权 ");
            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Log.i(TAG, "拒绝过了");
                Toast.makeText(mContext, "请在 设置-应用管理 中开启此应用的储存授权。", Toast.LENGTH_SHORT).show();
            } else {
                Log.i(TAG, "进行授权");
                ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        } else {
            Log.i(TAG, "不需要授权 ");
            GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(MainActivity.this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "同意授权");
                GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(MainActivity.this);
            } else {
                Log.i(TAG, "拒绝授权");
            }
        }
    }
}
