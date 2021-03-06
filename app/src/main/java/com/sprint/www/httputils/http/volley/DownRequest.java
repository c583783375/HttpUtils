package com.sprint.www.httputils.http.volley;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;



public class DownRequest extends Request<byte[]> {

    private final Response.Listener<byte[]> mListener;

    public DownRequest(int method, String url, Response.ErrorListener listener, Response.Listener<byte[]> downListener) {
        super(method, url, listener);
        this.mListener = downListener;
        setShouldCache(false);
    }

    @Override
    protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {
        return Response.success(response.data, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(byte[] response) {
        if (null != mListener) {
            mListener.onResponse(response);
        }
    }

}