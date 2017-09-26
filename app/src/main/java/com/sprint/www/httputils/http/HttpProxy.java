package com.sprint.www.httputils.http;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 作者：Sprint  on 2017-09-24 22:51
 * 邮箱：xmll17@163.com
 * 单利模式 创建网络请求代理
 */
public class HttpProxy implements IHttpProcessor, IConstants {

    private static HttpProxy _instance = null;
    private static IHttpProcessor mIHttpProcessor;
    private Map<String ,Object> mParams = null;
    private HttpProxy(){
        mParams = new HashMap<>();
    }


    /**
     * Obtain http proxy.
     *
     * @return the http proxy
     */
    public static HttpProxy obtain(){
        synchronized (HttpProxy.class){
            if (_instance == null){
                _instance = new HttpProxy();
            }
        }
        return _instance;
    }

    /**
     * Init http proxy.
     *
     * @param httpProcessor the http processor
     * @return the http proxy
     */
    public static HttpProxy init(IHttpProcessor httpProcessor){
        mIHttpProcessor = httpProcessor;
        return _instance;
    }

    /**
     * 设置是否使用缓存
     *
     * @param isUseCache：map集合，封装请求头键值对
     */
    @Override
    public  HttpProxy setCache(boolean isUseCache) {
        mIHttpProcessor.setCache(isUseCache);
        return _instance;
    }

    /**
     * 设置请求，获取数据
     *
     * @param headers：map集合，封装请求头键值对
     */
    @Override
    public HttpProxy setHeader(Map<String, String> headers) {
        mIHttpProcessor.setHeader(headers);
        return _instance;
    }

    /**
     * post请求，获取数据
     * @param url：url
     * @param params：map集合，封装键值对参数
     * @param callBack：回调接口，onFailure方法在请求失败时调用，onSuccess方法在请求成功后调用
     */
    @Override
    public void post(String url, Map<String, Object> params, ICallBack callBack) {
        mIHttpProcessor.post(url,params,callBack);
    }

    /**
     * get请求，获取数据
     * @param url：url
     * @param params：map集合，封装键值对参数
     * @param callBack：回调接口，onFailure方法在请求失败时调用，onSuccess方法在请求成功后调用
     */
    @Override
    public void get(String url, Map<String, Object> params, ICallBack callBack) {
        mIHttpProcessor.get(url,params,callBack);
    }

    /**
     * post请求，上传单个文件
     *
     * @param url：url
     * @param file：File对象
     * @param fileKey：上传参数时file对应的键
     * @param fileType：File类型，是image，video，audio，file
     * @param paramsMap：map集合，封装键值对参数
     * @param callBack：回调接口，onFailure方法在请求失败时调用，onResponse方法在请求成功后调用，这两个方法都执行在UI线程。还可以重写onProgress方法，得到上传进度
     */
    @Override
    public void uploadFile(String url, File file, String fileKey, String fileType, Map<String, String> paramsMap, ICallBack callBack) {
        //mIHttpProcessor.okHttpUploadFile();
    }

}
