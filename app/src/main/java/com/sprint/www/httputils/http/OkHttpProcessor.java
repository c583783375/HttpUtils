package com.sprint.www.httputils.http;

import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

import static android.net.Uri.encode;
import static java.lang.String.valueOf;


public class OkHttpProcessor implements IHttpProcessor, IConstants {

    private static final String TAG = "OkHttpProcessor";
    private static final long TIME_OUT = 5000; //请求超时时间

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
        //回调方法的使用与get异步请求相同，此时略。
        executeAsyn_Get_Post(request,callBack);
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
        executeAsyn_Get_Post(request,callBack);

    }
    /**文件上传*/
    @Override
    public void uploadFile(String url,  File[] files, String[] filekeys, String[] fileTypes,
                           Map<String, Object> paramsMap, ProgressListener listener ,ICallBack callBack) {
        //封装请求数据
        // 1 form 表单形式上传
        MultipartBody.Builder requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        //2.按顺序添加文件
        if(files != null && files.length > 0){
            for (int i = 0;i< files.length;i++){
                // MediaType.parse() 里面是上传的文件类型。 "file/*" "image/*" "audio/*" "video/*"
                // "image/jpeg; charset=utf-8" "text/*" "application/octet-stream"
                String filetype = FILE_TYOE_DEF;
                if(fileTypes != null && filekeys.length > i){
                    filetype = TextUtils.isEmpty(filekeys[i])?  FILE_TYOE_DEF :filekeys[i];
                }
                //没有上传进度
             //   RequestBody body = RequestBody.create(MediaType.parse(filetype), files[i]);
                ProgressRequestBody body = new ProgressRequestBody(i,MediaType.parse(filetype),files[i],listener);

                // 参数分别为， 请求key ，文件名称 ， RequestBody
                requestBody.addFormDataPart(filekeys[i], files[i].getName(), body);
            }
        }
        //3.添加参数
        if (paramsMap != null) {
            // map 里面是请求中所需要的 key 和 value
            for (Map.Entry entry : paramsMap.entrySet()) {
                requestBody.addFormDataPart(valueOf(entry.getKey()), valueOf(entry.getValue().toString()));
            }
        }
        //4.将总的RequestBody加入到Request中去
        Request request = new Request.Builder()
                .url(url)//"请求地址"
                .post(requestBody.build())
                //.tag(context) //用来取消请求
                .build();
        //5.执行
        executeAsyn_String(request, callBack);

    }
    /**文件下传*/
    @Override
    public void downLoadFile(String url, String fileDir, ProgressListener listener, ICallBack callback) {

        final Request request = new Request.Builder()
                .url(url)
                .build();
        String filename = getNameFromUrl(url);
        executeAsyn_DownloadWithListener(request,fileDir,filename,listener,callback);
    }


    /**
     * 开始执行异步请求，获得少量的String类型返回值。
     * 普通的异步post请求、上传文件时调用
     *
     * @param request
     * @param callBack 通过定义回调函数来接收服务器的返回数据
     */
    private void executeAsyn_String(Request request,  final ICallBack callBack){
        mClient.newBuilder()
                .readTimeout(TIME_OUT, TimeUnit.MILLISECONDS)// readTimeout("请求超时时间" , 时间单位);
                .build()
                .newCall(request)
                .enqueue(new Callback(){
                    @Override
                    public void onFailure(Call call, final IOException e) {
                        /**推送到主线程刷新UI*/
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onFailure(State.NETWORK_FAILURE,Network_Failure_Message);
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
                            final int code = response.code();
                            /**推送到主线程刷新UI*/
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    callBack.onFailure(State.FAILURE,"Unexpected code: " +" error:"+err);
                                    if (debug) Log.e(TAG, "Unexpected code " +err);

                                }
                            });
                        }
                    }
                });
    }
    /**
     * 开始执行异步请求，获得少量的String类型返回值。
     * 普通的异步post请求、get请求时调用
     *
     * @param request
     * @param callBack 通过定义回调函数来接收服务器的返回数据
     */
    private void executeAsyn_Get_Post(Request request,  final ICallBack callBack){
        mClient.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, final IOException e) {
                        /**推送到主线程刷新UI*/
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onFailure(State.NETWORK_FAILURE,Network_Failure_Message);
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
                                    callBack.onFailure(State.FAILURE,"Unexpected code " + err);
                                    if (debug) Log.e(TAG, "Unexpected code " + err);
                                }
                            });
                        }
                    }
                });
    }


    /*** 拼接posturl*/
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

    /*** 拼接geturl*/
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


    /**
     * 继承RequestBody,实现上传的进度监听
     */
    private class ProgressRequestBody extends RequestBody {
        MediaType contentType;
        File file;
        ProgressListener listener;
        int id;

        /**
         * 构造函数
         *
         * @param id          一次可以上传多个文件,id表示本文件在这一批文件当中的编号
         * @param contentType MIME类型
         * @param file        要上传的文件
         * @param listener    传输进度监听器
         */
        public ProgressRequestBody(int id, MediaType contentType, File file, ProgressListener listener) {
            this.id = id;
            this.contentType = contentType;
            this.file = file;
            this.listener = listener;
        }


        @Override
        public MediaType contentType() {
            return contentType;
        }

        @Override
        public long contentLength() throws IOException {
            return file.length();
        }

        @Override
        public void writeTo(BufferedSink sink) throws IOException {
            Source source;
            long len;//记录本次读了多少数据
            long currSize = 0;//记录目前一共读了多少数据
            long totalSize = contentLength();//一共有多少数据

            try {
                source = Okio.source(file);
                Buffer buffer = new Buffer();

                while ((len = source.read(buffer, 2048)) != -1) {
                    sink.write(buffer, len);
                    sink.flush();
                    currSize += len;
                    //回调,进度监听
                    listener.onProgress(totalSize, currSize, totalSize == currSize, id);
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 开始执行带进度监听的异步请求,或得返回的数据流。适用于接收大量的返回数据
     * 下载时调用
     *
     * @param request
     * @param fileDir
     * @param filename
     * @param listener
     */
    private void executeAsyn_DownloadWithListener(Request request, final String fileDir
            , final String filename
            , final ProgressListener listener
            , final ICallBack callback) {
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                /**推送到主线程刷新UI*/
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFailure(State.NETWORK_FAILURE, Network_Failure_Message);
                    }
                });
                if (debug) Log.e(TAG,  "OkHttp is on Failure:操作取消,文件读取失败或者连接超时");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call,final Response response) throws IOException {
                if (response.isSuccessful()) {

                    if (debug) Log.e(TAG,  "the request was successfully received, understood, and accepted.");
                    InputStream is = null;
                    byte[] buf = new byte[2048];
                    int len = 0;//本次读取的字节数
                    long currSize = 0;//当前已经读取的字节数
                    //总大小
                    final long totalSize = Integer.valueOf(response.header("Content-Length", "-1"));
                    String savePath = isExistDir(fileDir);
                    FileOutputStream fos = null;

                    try {
                        is = response.body().byteStream(); //获取返回的Stream
                        File file = new File(savePath, filename);

                        fos = new FileOutputStream(file);
                        while ((len = is.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                            currSize += len;
                            //文件总长度从外部获得；
                            //因为是单文件下载，所以id永远为0，表示不起作用
                            final long finalCurrSize = currSize;
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    listener.onProgress(totalSize, finalCurrSize, totalSize == finalCurrSize, 0);
                                }
                            });
                        }
                        fos.flush();
                        if (debug) Log.e(TAG,  "file has finished downloading.");
                        /**推送到主线程刷新UI*/
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onSuccess( "file has finished downloading.filename=" +
                                        filename + "\tfileDir:" + fileDir);
                            }
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (is != null)
                                is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            if (fos != null)
                                fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                } else {
                    /**推送到主线程刷新UI*/
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onFailure(State.FAILURE, Integer.toString(response.code()));
                        }
                    });
                    if (debug) Log.e(TAG,  "OkHttp response is not successful. Code is: " + response.code());

                }
            }
        });
    }

    /**
     * @param saveDir
     * @return
     * @throws IOException
     * 判断下载目录是否存在
     */
    private String isExistDir(String saveDir) throws IOException {
        // 下载位置
        File downloadFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), saveDir);
        if (!downloadFile.mkdirs()) {
            downloadFile.createNewFile();
        }
        String savePath = downloadFile.getAbsolutePath();
        return savePath;
    }
    /**
     * @param url
     * @return
     * 从下载连接中解析出文件名
     */
    @NonNull
    private String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }
    /** 取消所有请求 */
    @Override
    public void cancelAll(){
        mClient.dispatcher().cancelAll();//取消所有请求
    }

}
