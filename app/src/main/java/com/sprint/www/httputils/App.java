package com.sprint.www.httputils;

import android.app.Application;

import com.sprint.www.httputils.http.HttpProxy;
import com.sprint.www.httputils.http.okhttp.OkHttpProcessor;
import com.sprint.www.httputils.http.volley.VolleyProcessor;

import java.util.HashMap;
import java.util.Map;


public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Map<String ,String> headers = new HashMap<>();
            headers.put("Charset", "UTF-8");
            headers.put("Content-Type", "application/x-javascript");
            headers.put("Accept-Encoding", "gzip,deflate");
    //    HttpProxy.init(new OkHttpProcessor());
        HttpProxy.init(new VolleyProcessor(this));
//        HttpProxy.obtain()
//                .setHeader(headers)
//                .setCache(false);


    }
}
