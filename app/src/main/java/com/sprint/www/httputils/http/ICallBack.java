package com.sprint.www.httputils.http;

/**
 * 作者：Sprint  on 2017-09-24 22:45
 * 邮箱：xmll17@163.com
 */
public interface ICallBack {
    //成功调用
    void onSuccess(String response);
    //失败调用
    void onFailure(String error);
}
