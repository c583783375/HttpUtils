package com.sprint.www.httputils.http;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者：Sprint  on 2017-09-24 23:12
 * 邮箱：xmll17@163.com
 * volley 工具类
 */
public class VolleyProcessor  implements IHttpProcessor{
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callBack.onSuccess(response);
                if (debug) Log.e(TAG,response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.onFailure(error.toString());
                if (debug) Log.e(TAG,error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                if( null != headers && !headers.isEmpty()){
                    params = headers;
                }
                if (debug) Log.e(TAG,params.toString());
                return params;
            }
        };
        // 请用缓存
        stringRequest.setShouldCache(is_use_cache);

        mQueue.add(stringRequest);
    }
    /**使用GET*/
    @Override
    public void get(String url, Map<String, Object> params, final ICallBack callBack) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callBack.onSuccess(response);
                if (debug) Log.e(TAG,response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.onFailure(error.toString());
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
        // 请用缓存
        stringRequest.setShouldCache(is_use_cache);
        mQueue.add(stringRequest);
    }

}
