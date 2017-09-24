package com.sprint.www.httputils.http;

import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 作者：Sprint  on 2017-09-24 22:53
 * 邮箱：xmll17@163.com
 *
 * 定义一个泛型
 */
public abstract class HttpCallback<Result> implements ICallBack {

    @Override
    public void onSuccess(String result) {
        Gson gson = new Gson();
        Class<?> cls = analysisClazInfo(this);
        Result objectResult = (Result) gson.fromJson(result, cls);
        onSuccess(objectResult);
    }

    public abstract void onSuccess(Result result);

    public static Class<?> analysisClazInfo(Object object){
        Type genType = object.getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType)genType).getActualTypeArguments();
        return (Class<?>)params[0];
    }
    
    
    
}
