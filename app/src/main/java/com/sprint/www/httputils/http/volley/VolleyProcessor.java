package com.sprint.www.httputils.http.volley;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sprint.www.httputils.http.utils.FileUtils;
import com.sprint.www.httputils.http.utils.ICallBack;
import com.sprint.www.httputils.http.utils.IConstants;
import com.sprint.www.httputils.http.utils.IHttpProcessor;
import com.sprint.www.httputils.http.utils.ProgressListener;
import com.sprint.www.httputils.http.utils.State;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.net.Uri.encode;

/**
 * 作者：Sprint  on 2017-09-24 23:12
 * 邮箱：xmll17@163.com
 * volley 工具类
 */
public class VolleyProcessor implements IHttpProcessor, IConstants {
    private static final String TAG = "VolleyProcessor";
    private  boolean debug = true;
    private static RequestQueue mQueue = null;
    /**默认有使用缓存*/
    private  boolean is_use_cache = true;
    /**默认没有添加请求头*/
    private  Map<String, String> headers;
    public VolleyProcessor(Context context) {
        mQueue = Volley.newRequestQueue(context);
    }

    @Override
    public  IHttpProcessor setCache(boolean isUseCache) {
        is_use_cache = isUseCache;
        return this;
    }

    @Override
    public  IHttpProcessor setHeader(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }
    /**使用POST*/
    @Override
    public void post(String url, Map<String, String> params, final ICallBack callBack) {
        String strUrl = appendParams(url, params);
        StringRequest stringRequest = getStringRequest(strUrl,Request.Method.POST,callBack);
        // 请用缓存
        stringRequest.setShouldCache(is_use_cache);
        mQueue.add(stringRequest);
    }
    /**使用GET*/
    @Override
    public void get(String url, Map<String, String> params,  ICallBack callBack) {
        String strUrl = appendParams(url, params);
        StringRequest stringRequest = getStringRequest(strUrl,Request.Method.GET,callBack);

        // 请用缓存
        stringRequest.setShouldCache(is_use_cache);
        mQueue.add(stringRequest);
    }
    /**
     * @param type 网络请求的方式  int GET = 0;int POST = 1;int PUT = 2;int DELETE = 3;
     *               int HEAD = 4;int OPTIONS = 5;int TRACE = 6;int PATCH = 7;
     *@param strUrl 网络请求的地址
     * @param callBack 结果回调
     *@return StringRequest
     * */
    private StringRequest getStringRequest(String strUrl, int type, final ICallBack callBack){
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                strUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(callBack != null){
                    callBack.onSuccess(response);
                    if (debug) Log.e(TAG,response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(callBack != null){
                    callBack.onFailure(State.FAILURE,error.toString());
                    if (debug) Log.e(TAG,error.toString());
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                if(null != headers && !headers.isEmpty()){
                    params = headers;
                }
//                    headers.put("Charset", "UTF-8");
//                    headers.put("Content-Type", "application/x-javascript");
//                    headers.put("Accept-Encoding", "gzip,deflate");
                if (debug) Log.e(TAG,params.toString());
                return params;
            }
        };
        return stringRequest;
    }


    /**文件上传*/
    @Override
    public void uploadFile(String url, final Map<String, File> filesMap, List<String> fileTypes,
                           final Map<String, String> paramsMap, ProgressListener listener ,
                           final ICallBack callBack) {
        PostUploadRequest mRequest = new PostUploadRequest(Request.Method.POST, url,listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (callBack != null) {
                    callBack.onFailure(State.FAILURE,error.toString());
                    if (debug) Log.e(TAG,error.toString());
                }
            }
        }, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (callBack != null) {
                    callBack.onSuccess(response);
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> superHeader = super.getHeaders();
                if (headers != null && headers.size() > 0) {
                    superHeader = headers;
                }
                if (debug) Log.e(TAG,superHeader.toString());
                return superHeader;
            }

            // 设置Body参数
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> tParams = super.getParams();
                if (paramsMap != null && paramsMap.size() > 0) {
                    tParams = paramsMap;
                }
                return tParams;
            }

            @Override
            public Map<String, File> getUploadFiles() {
                Map<String, File> mUploadFiles  = new HashMap<>();
                if(filesMap != null){
                    mUploadFiles = filesMap;
                }
                return mUploadFiles;
            }
        };

       // mRequest.setTag(TAG);
        mQueue.add(mRequest);

    }
    /**文件下载*/
    @Override
    public void downLoadFile(String url, final Map<String, String> params, final String fileDir,
                             final ProgressListener listener, final ICallBack callback) {

       // Request request =  downLoadByteFile(url,params,fileDir,listener,callback);
        Request request =  downLoadInputStreamFile(url,params,fileDir,listener,callback);


     //   request.setTag(mTag);
        mQueue.add(request);
    }
    private Request downLoadInputStreamFile(String url, final Map<String, String> params, final String fileDir,
                                     final ProgressListener listener, final ICallBack callback){
        final String filename =  getNameFromUrl(url);
        DownInputStreamRequest disr = new DownInputStreamRequest(Request.Method.GET,url,new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                if (debug) Log.e(TAG,  "Volley is on Failure:操作取消,文件读取失败或者连接超时");
                if (callback != null) {
                    callback.onFailure(State.NETWORK_FAILURE,error.toString());
                }
            }
        }, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                if (debug) Log.e(TAG,  "the request was successfully received, understood, and accepted.");
                try {
                    String savePath = isExistDir(fileDir);
                    File file = new File(savePath, filename);
                    FileUtils.saveFile(response, file,listener);
                    if (callback != null) {
                        callback.onSuccess(response.toString());
                    }
                } catch (IOException e){
                    if (callback != null) {
                        callback.onFailure(State.FAILURE,e.toString());
                    }
                    if (debug) Log.e(TAG,  "OkHttp response is not successful. Code is: " + e.toString());
                    e.printStackTrace();
                }catch (Exception e) {
                    if (callback != null) {
                        callback.onFailure(State.FAILURE,e.toString());
                    }
                    if (debug) Log.e(TAG,  "OkHttp response is not successful. Code is: " + e.toString());
                    e.printStackTrace();
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> superHeader = super.getHeaders();
                if (headers != null && headers.size() > 0) {
                    superHeader = headers;
                }
                return superHeader;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> tParams = super.getParams();
                if (params != null && params.size() > 0) {
                    tParams = params;
                }
                return tParams;
            }
        };
        return disr;
    }

    /**下载文件*/
    private Request downLoadByteFile(String url, final Map<String, String> params, final String fileDir,
                                  final ProgressListener listener, final ICallBack callback) {
        final String filename =  getNameFromUrl(url);
        DownRequest dRequest = new DownRequest(Request.Method.GET, url, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (debug) Log.e(TAG,  "Volley is on Failure:操作取消,文件读取失败或者连接超时");
                if (callback != null) {
                    callback.onFailure(State.NETWORK_FAILURE,error.toString());
                }
            }
        }, new Response.Listener<byte[]>() {// byte[]
            @Override
            public void onResponse(byte[] response) {
                if (debug) Log.e(TAG,  "the request was successfully received, understood, and accepted.");
                try {
                    String savePath  = isExistDir(fileDir);
                    File file = new File(savePath, filename);
                    FileUtils.saveFile(response, file,listener);
                    if (callback != null) {
                        callback.onSuccess(response.toString());
                    }
                } catch (IOException e){
                    if (callback != null) {
                        callback.onFailure(State.FAILURE,e.toString());
                    }
                    if (debug) Log.e(TAG,  "OkHttp response is not successful. Code is: " + e.toString());
                    e.printStackTrace();
                }catch (Exception e) {
                    if (callback != null) {
                        callback.onFailure(State.FAILURE,e.toString());
                    }
                    if (debug) Log.e(TAG,  "OkHttp response is not successful. Code is: " + e.toString());
                    e.printStackTrace();
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> superHeader = super.getHeaders();
                if (headers != null && headers.size() > 0) {
                    superHeader = headers;
                }
                return superHeader;
            }

            // 设置Body参数
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> tParams = super.getParams();
                if (params != null && params.size() > 0) {
                    tParams = params;
                }
                return tParams;
            }
        };
        return dRequest;
    }


    /**取消所有网络请求 */
    @Override
    public void cancelAll() {
        mQueue.cancelAll(getClass());
    }


    /**
     * 拼接url
     */
    private String appendParams(String url, Map<String, String> params) {
        if (TextUtils.isEmpty(url) || params == null || params.isEmpty()) {
            return url;
        }
        StringBuffer urlBuilder = new StringBuffer(url);
        if (urlBuilder.indexOf("?") <= 0) {
            urlBuilder.append("?");
        } else {
            if (!urlBuilder.toString().endsWith("?")) {
                urlBuilder.append("&");
            }
        }
        for (Map.Entry<String, String> entry : params.entrySet()) {
            urlBuilder.append(entry.getKey()).append("=").append(encode(entry.getValue().toString())).append("&");
        }

        return urlBuilder.deleteCharAt(urlBuilder.length() - 1).toString();
    }

    /**
     * @param saveDir
     * @return
     * @throws IOException
     * 判断下载目录是否存在
     */
    private String isExistDir(String saveDir) throws IOException {
        // 下载位置
        File downloadFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), saveDir);
        if (!downloadFile.mkdirs()) {
            downloadFile.createNewFile();
        }
        String savePath = downloadFile.getAbsolutePath();
        return savePath;
    }
    /**
     * @param url
     * @return
     * 从下载连接中解析出文件名
     */
    @NonNull
    private String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }


}
