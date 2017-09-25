package com.sprint.www.httputils;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sprint.www.httputils.bean.Title;
import com.sprint.www.httputils.http.HttpCallback;
import com.sprint.www.httputils.http.HttpProxy;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.tv);
    }


    public void onBtn(View v){
        String url = "http://lf.snssdk.com/article/category/get_subscribed/v2/?";
        Map<String, Object> params = new HashMap<>();
        HttpProxy.obtain().post(url, params, new HttpCallback<Title>() {
            @Override
            public void onSuccess(Title response) {
            tv.setText(response.toString());
            }

            @Override
            public void onFailure(String error) {
                tv.setText(error);
            }
        });
    }

}
