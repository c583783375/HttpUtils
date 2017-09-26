package com.sprint.www.httputils.http;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;


import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.net.Uri.encode;


public class OkHttpProcessor implements IHttpProcessor, IConstants {
    private static final String TAG = "OkHttpProcessor";


    private boolean debug = true;
    /**默认有使用缓存*/
    private  boolean is_use_cache = true;
    /**默认没有添加请求头*/
    private Map<String, String> mHeaders;
    private OkHttpClient mClient;
    private Handler mHandler = new Handler();

    public OkHttpProcessor() {
        //创建OkHttpClient对象。
        mClient = new OkHttpClient();

    }

    @Override
    public IHttpProcessor setCache(boolean isUseCache) {
        is_use_cache = isUseCache;
        return this;
    }

    @Override
    public IHttpProcessor setHeader(Map<String, String> headers) {
        this.mHeaders = headers;
        return this;
    }

    /**
     * 异步请求
     */
    @Override
    public void post(String url, Map<String, Object> params, final ICallBack callBack) {
        RequestBody requestBody = getRequestBody(params);
        Headers headers = getHeaders(mHeaders);
        Log.e(TAG, "post:" + appendParams(url, params).toString());
        Request request = new Request.Builder()//创建Request 对象。
                .url(url)
                .headers(headers)
                .post(requestBody)//传递请求体
                .build();
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                /**推送到主线程刷新UI*/
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onFailure(e.toString());
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    //当执行这行代码得到结果后，再跳转到UI线程修改UI。
                    final String result = new String(response.body().bytes(), "utf-8");
                    /**推送到主线程刷新UI*/
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onSuccess(result);
                            if (debug) Log.e(TAG, result);
                        }
                    });
                } else {
                    final String err = response.toString();
                    /**推送到主线程刷新UI*/
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onFailure("Unexpected code " + err);
                            if (debug) Log.e(TAG, "Unexpected code " + err);
                        }
                    });
                }
            }
        });//回调方法的使用与get异步请求相同，此时略。
    }

    /**
     * 异步请求
     */
    @Override
    public void get(String url, Map<String, Object> params, final ICallBack callBack) {
        String strUrl = appendParams(url, params);
        Log.e(TAG, "get:" + strUrl.toString());
        final Request request = new Request.Builder()
                .url(strUrl)
                .build();

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                /**推送到主线程刷新UI*/
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onFailure(e.toString());
                        if (debug) Log.e(TAG, e.toString());
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    //当执行这行代码得到结果后，再跳转到UI线程修改UI。
                    final String result = new String(response.body().bytes(), "utf-8");
                    /**推送到主线程刷新UI*/
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onSuccess(result);
                            if (debug) Log.e(TAG, result);
                        }
                    });
                } else {
                    final String err = response.toString();
                    /**推送到主线程刷新UI*/
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onFailure("Unexpected code " + err);
                            if (debug) Log.e(TAG, "Unexpected code " + err);
                        }
                    });
                }
            }
        });
    }
    /**文件上传*/
    @Override
    public void uploadFile(String url, File file, String fileKey, String fileType, Map<String, String> paramsMap, ICallBack callBack) {

    }


    /**
     * 拼接posturl
     */
    @NonNull
    private RequestBody getRequestBody(Map<String, Object> params) {
        //把请求参数封装到RequestBody里面
        FormBody.Builder formBuilder = new FormBody.Builder();
        if (params == null || params.isEmpty()) {
            return formBuilder.build();
        }
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            formBuilder.add(entry.getKey(), encode(entry.getValue().toString()));
        }
        return formBuilder.build();
    }

    /**
     * 拼接geturl
     */
    private String appendParams(String url, Map<String, Object> params) {
        if (TextUtils.isEmpty(url) || params == null || params.isEmpty()) {
            return url;
        }
        StringBuffer urlBuilder = new StringBuffer(url);
        if (urlBuilder.indexOf("?") <= 0) {
            urlBuilder.append("?");
        } else {
            if (!urlBuilder.toString().endsWith("?")) {
                urlBuilder.append("&");
            }
        }
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            urlBuilder.append(entry.getKey()).append("=").append(encode(entry.getValue().toString())).append("&");
        }

        return urlBuilder.deleteCharAt(urlBuilder.length() - 1).toString();
    }

    @NonNull
    private Headers getHeaders(Map<String, String> headers) {
        Headers.Builder builder = new Headers.Builder();
        if (headers == null || headers.isEmpty()) {
            return builder.build();
        }
//        for (String key:headers.keySet()){
//            builder.add(key,headers.get(key));
//        }
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            builder.add(entry.getKey(), encode(entry.getValue()));
        }
        return builder.build();
    }

}
