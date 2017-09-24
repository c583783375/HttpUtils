package com.sprint.www.httputils.http;

import java.util.Map;

/**
 * 作者：Sprint  on 2017-09-24 23:06
 * 邮箱：xmll17@163.com
 *    //网络请求 post get
 */
public interface IHttpProcessor {

    void post(String url, Map<String,Object> params,ICallBack callBack);

    void get(String url, Map<String,Object> params,ICallBack callBack);

}
