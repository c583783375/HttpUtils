package com.sprint.www.httputils.http.utils;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public final class FileUtils {
    public static final void saveFile(InputStream is, File file ) throws IOException {
		InputStream inputStream = is;
		if (null != inputStream) {
			FileOutputStream fos = null;
			int len = 0;
			try {
				byte[] bys = new byte[4 * 1024];
				fos = new FileOutputStream(file);
				while ((len = inputStream.read(bys)) != -1) {
					fos.write(bys, 0, len);
				}
				fos.flush();
			} finally {
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