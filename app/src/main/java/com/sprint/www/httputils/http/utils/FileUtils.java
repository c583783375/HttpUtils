package com.sprint.www.httputils.http.utils;

import android.os.Handler;
import android.util.Log;

import com.android.volley.NetworkResponse;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public final class FileUtils {
    public static final void saveFile(NetworkResponse response, File file , ProgressListener listener) throws IOException {

		InputStream inputStream = new ByteArrayInputStream(response.data);
		int len = 0;
		long currSize = 0;
		//总大小
		final long totalSize = response.data.length;
		if (null != inputStream) {
			FileOutputStream fos = null;
			try {
				byte[] bys = new byte[ 1024];
				fos = new FileOutputStream(file);
				while ((len = inputStream.read(bys)) != -1) {
					fos.write(bys, 0, len);
					currSize += len;
					//由于下载的是单文件所以id 一直设置为0
					listener.onProgress(totalSize,currSize,currSize == totalSize,0);
				}
				fos.flush();
				 Log.e("volley file_utils",  "file has finished downloading.");
			} finally {
				if(inputStream != null){
					inputStream.close();
				}
				if(fos != null) {
					fos.close();
				}

			}
		}
	}

	public static final void saveFile(byte[] bytes, File file,ProgressListener listener) throws IOException {
		long currSize = 0;//当前已经读取的字节数
		long totalSize = bytes.length;
		Log.e("======",String.valueOf(totalSize));
		if(bytes != null) {
			FileOutputStream fos = new FileOutputStream(file);
			//byte[] bys = new byte[4 * 1024];
			fos.write(bytes);
			fos.flush();
			fos.close();
		}
	}
}