package com.sprint.www.httputils.http.volley;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

/**
 * Created by admin on 2017/11/21 0021.
 */

public class DownInputStreamRequest extends Request<NetworkResponse> {
    private final Response.Listener<NetworkResponse> mListener;
    public DownInputStreamRequest(int method, String url, Response.ErrorListener listener, Response.Listener<NetworkResponse> mListener) {
        super(method, url, listener);
        this.mListener = mListener;
    }

    @Override
    protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {

        return Response.success(response, HttpHeaderParser.parseCacheHeaders(response));

    }

    @Override
    protected void deliverResponse(NetworkResponse response) {
        if (null != mListener) {
            mListener.onResponse(response);
        }
    }
}
