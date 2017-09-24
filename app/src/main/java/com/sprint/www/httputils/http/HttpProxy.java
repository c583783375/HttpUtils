package com.sprint.www.httputils.http;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

import static android.net.Uri.encode;

/**
 * 作者：Sprint  on 2017-09-24 22:51
 * 邮箱：xmll17@163.com
 * 单利模式 创建网络请求代理
 */
public class HttpProxy implements IHttpProcessor {

    private static HttpProxy _instance = null;
    private static IHttpProcessor mIHttpProcessor;
    private Map<String ,Object> mParams = null;
    private HttpProxy(){
        mParams = new HashMap<>();
    }


    public static HttpProxy obtain(){
        synchronized (HttpProxy.class){
            if (_instance == null){
                _instance = new HttpProxy();
            }
        }
        return _instance;
    }

    public static void init(IHttpProcessor httpProcessor){
        mIHttpProcessor = httpProcessor;
    }


    @Override
    public void post(String url, Map<String, Object> params, ICallBack callBack) {
        final String finalUrl = appendParams(url, params);
        mIHttpProcessor.post(url,params,callBack);
    }

    @Override
    public void get(String url, Map<String, Object> params, ICallBack callBack) {
        mIHttpProcessor.get(url,params,callBack);
    }

    /**拼接url*/
    public static String appendParams(String url, Map<String, Object> params) {
        if (TextUtils.isEmpty(url) || params.isEmpty() ) {
            return url;
        }
        StringBuffer urlBuilder = new StringBuffer(url);
        if(urlBuilder.indexOf("?") <=0 ){
            urlBuilder.append("?");
        }else{
            if(!urlBuilder.toString().endsWith("?")){
                urlBuilder.append("&");
            }
        }
        for (Map.Entry<String,Object> entry : params.entrySet()){
            urlBuilder.append(entry.getKey()).append("=").append(encode(entry.getValue().toString()));
        }

        return urlBuilder.toString();
    }


}
