package com.sprint.www.httputils.http;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static android.net.Uri.encode;

/**
 * 作者：Sprint  on 2017-09-24 23:12
 * 邮箱：xmll17@163.com
 * volley 工具类
 */
public class VolleyProcessor implements IHttpProcessor, IConstants {
    private static final String TAG = "VolleyProcessor";
    private  boolean debug = true;
    private static RequestQueue mQueue = null;
    /**默认有使用缓存*/
    private  boolean is_use_cache = true;
    /**默认没有添加请求头*/
    private  Map<String, String> headers;
    public VolleyProcessor(Context context) {
        mQueue = Volley.newRequestQueue(context);
    }

    @Override
    public  IHttpProcessor setCache(boolean isUseCache) {
        is_use_cache = isUseCache;
        return this;
    }

    @Override
    public  IHttpProcessor setHeader(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }
    /**使用POST*/
    @Override
    public void post(String url, Map<String, Object> params, final ICallBack callBack) {
        String strUrl = appendParams(url, params);
        StringRequest stringRequest = getStringRequest(strUrl,Request.Method.POST,callBack);
        // 请用缓存
        stringRequest.setShouldCache(is_use_cache);
        mQueue.add(stringRequest);
    }
    /**使用GET*/
    @Override
    public void get(String url, Map<String, Object> params,  ICallBack callBack) {
        String strUrl = appendParams(url, params);
        StringRequest stringRequest = getStringRequest(strUrl,Request.Method.GET,callBack);

        // 请用缓存
        stringRequest.setShouldCache(is_use_cache);
        mQueue.add(stringRequest);
    }
    /**
     * @param type 网络请求的方式  int GET = 0;int POST = 1;int PUT = 2;int DELETE = 3;
     *               int HEAD = 4;int OPTIONS = 5;int TRACE = 6;int PATCH = 7;
     *@param strUrl 网络请求的地址
     * @param callBack 结果回调
     *@return StringRequest
     * */
    private StringRequest getStringRequest(String strUrl, int type, final ICallBack callBack){
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                strUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callBack.onSuccess(response);
                if (debug) Log.e(TAG,response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.onFailure(State.FAILURE,error.toString());
                if (debug) Log.e(TAG,error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                if(null != headers && !headers.isEmpty()){
                    params = headers;
                }
//                    headers.put("Charset", "UTF-8");
//                    headers.put("Content-Type", "application/x-javascript");
//                    headers.put("Accept-Encoding", "gzip,deflate");
                if (debug) Log.e(TAG,params.toString());
                return params;
            }
        };
        return stringRequest;
    }


    /**文件上传*/
    @Override
    public void uploadFile(String url, File[] files, String[] filekeys, String[] fileTypes, Map<String, Object> paramsMap,ProgressListener listener ,ICallBack callBack) {

    }
    /**文件下载*/
    @Override
    public void downLoadFile(String url, String fileDir,  ProgressListener listener, ICallBack callback) {
        String filename =  getNameFromUrl(url);
     /*   write2SDFromInput(url,filename,fileDir,listener,callback);*/

    }

/*
    //将一个InoutStream里面的数据写入到SD卡中
    public File write2SDFromInput(String fileUrl, String fileName,String fileDir, ProgressListener listener, ICallBack callback){

        File file=null;
        OutputStream output=null;
        InputStream input = null;
        try {
            //创建一个URL对象
            URL url=new URL(fileUrl);
            //创建一个HTTP链接
            HttpURLConnection urlConn=(HttpURLConnection)url.openConnection();
            //使用IO流获取数据
            input=urlConn.getInputStream();

            //创建目录
            String savePath = isExistDir(fileDir);
            //创建文件
            file = new File(savePath, fileName);
            //写数据流
            output=new FileOutputStream(file);
            byte buffer[]=new byte[4*1024];//每次存4K
            int temp;
            //写入数据
            while((temp=input.read(buffer))!=-1){
                output.write(buffer,0,temp);
            }
            output.flush();
        } catch (Exception e) {
            System.out.println("写数据异常："+e);
        }
        finally{
            try {
                if(output != null){
                    output.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if(input != null){
                    input.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }
*/



    /**取消所有网络请求 */
    @Override
    public void cancelAll() {
        mQueue.cancelAll(getClass());
    }


    /**
     * 拼接url
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


}
