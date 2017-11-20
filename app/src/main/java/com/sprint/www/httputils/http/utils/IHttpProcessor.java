package com.sprint.www.httputils.http.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 作者：Sprint  on 2017-09-24 23:06
 * 邮箱：xmll17@163.com
 *    //网络请求 post get
 */
public interface IHttpProcessor {

    IHttpProcessor setCache(boolean isUseCache);//是否使用缓存
    IHttpProcessor setHeader(Map<String, String> headers);//是否添加请求头

    void post(String url, Map<String,String> params,ICallBack callBack);

    void get(String url, Map<String,String> params,ICallBack callBack);

    void uploadFile(String url, Map<String, File> filesMap, List<String> fileTypes, Map<String, String> paramsMap, ProgressListener listener , ICallBack callBack);

    void downLoadFile(String url,Map<String, String> params ,String fileDir,  ProgressListener listener, ICallBack callback);

    void cancelAll();
}
