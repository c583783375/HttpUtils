package com.sprint.www.httputils.http;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.File;
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
    public void downLoadFile(String url, String fileDir, String filename, ProgressListener listener, ICallBack callback) {

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
}
