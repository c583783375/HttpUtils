package com.sprint.www.httputils.http.utils;

/**
 * Created by admin on 2017/10/20 0020.
 *
 * 传输进度监听器
 *
 */

public interface ProgressListener {

    void onProgress(long totalSize, long currSize, boolean done, int id);
}
