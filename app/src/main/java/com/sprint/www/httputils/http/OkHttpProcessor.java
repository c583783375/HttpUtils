package com.sprint.www.httputils.http;

import java.util.Map;


public class OkHttpProcessor implements IHttpProcessor {
    private static final String TAG = "OkHttpProcessor";
    /**默认有使用缓存*/
    private  boolean is_use_cache = true;
    /**默认没有添加请求头*/
    private  Map<String, String> headers;
    @Override
    public IHttpProcessor setCache(boolean isUseCache) {
        is_use_cache = isUseCache;
        return this;
    }

    @Override
    public IHttpProcessor setHeader(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    @Override
    public void post(String url, Map<String, Object> params, ICallBack callBack) {

    }

    @Override
    public void get(String url, Map<String, Object> params, ICallBack callBack) {

    }
}
