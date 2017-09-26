package com.sprint.www.httputils;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sprint.www.httputils.bean.Content;
import com.sprint.www.httputils.http.HttpCallback;
import com.sprint.www.httputils.http.HttpProxy;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private TextView tv;
    StringBuffer sb = new StringBuffer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.tv);
    }

    public void onBtn(View v){
        aa();
    }

    private void aa() {
//        String url = "http://lf.snssdk.com/article/category/get_subscribed/v2/?";
        String url = "http://lf.snssdk.com/api/news/feed/v64/?";
        Map<String, Object> params = new HashMap<>();
        params.put("iid", "134852330716");
        params.put("loc_mode", "7");
        params.put("category", "news_local");
        params.put("refer", "1");
        params.put("count", "20");
        params.put("min_behot_time", "1502237751");
        params.put("loc_time", "1506404359");
        HttpProxy.obtain().get(url, params, new HttpCallback<Content>() {
            @Override
            public void onSuccess(Content response) {
//                sb.append(response.toString());
                tv.setText(response.getData().get(4).toString());
            }

            @Override
            public void onFailure(String error) {
                tv.setText(error);
            }
        });
    }

}
