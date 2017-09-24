package com.sprint.www.httputils.http;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Map;

/**
 * 作者：Sprint  on 2017-09-24 23:12
 * 邮箱：xmll17@163.com
 * volley 工具类
 */
public class VolleyProcessor  implements IHttpProcessor{
    private static RequestQueue mQueue = null;

    public VolleyProcessor(Context context) {
        mQueue = Volley.newRequestQueue(context);
    }
    /**有使用缓存*/
    @Override
    public void post(String url, Map<String, Object> params, final ICallBack callBack) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            callBack.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.onFailure(error.toString());
            }
        });
        // 请用缓存
        stringRequest.setShouldCache(true);
        // 设置缓存时间10分钟
        // myReq.setCacheTime(10*60);
        mQueue.add(stringRequest);
    }
    /**有使用缓存*/
    @Override
    public void get(String url, Map<String, Object> params, final ICallBack callBack) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callBack.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.onFailure(error.toString());
            }
        });
        // 请用缓存
        stringRequest.setShouldCache(true);
        // 设置缓存时间10分钟
        // myReq.setCacheTime(10*60);
        mQueue.add(stringRequest);
    }
}
