package com.xhuman.serial.helper;

import com.xhuman.serial.listener.DownloadListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class XchDownloader {

    public static void downloadBinaryFile(String url, DownloadListener listener) {
        try {
            HttpURLConnection httpURLConnection;
            (httpURLConnection = (HttpURLConnection) (new URL(url)).openConnection()).connect();
            if (((HttpURLConnection) (new URL(url)).openConnection()).getResponseCode() == 200) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                InputStream inputStream = httpURLConnection.getInputStream();

                byte[] arrayOfByte = new byte[8192];
                int i;
                int j;
                for (i = 0; (j = inputStream.read(arrayOfByte)) != -1; i += j) {
                    byteArrayOutputStream.write(arrayOfByte, 0, j);
                }
                listener.onDownloadComplete(byteArrayOutputStream.toByteArray(), i);
                inputStream.close();
            } else {
                int code = httpURLConnection.getResponseCode();
                listener.onDownloadFailed("Download failed. Response code: " + code);
            }
            httpURLConnection.disconnect();
        } catch (IOException iOException) {
            iOException.printStackTrace();
            listener.onDownloadFailed("Exception occurred: " + iOException.getMessage());
        }
    }


}

