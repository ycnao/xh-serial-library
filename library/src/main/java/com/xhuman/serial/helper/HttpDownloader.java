package com.xhuman.serial.helper;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.util.Log;

import com.xhuman.serial.util.FileUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpDownloader {

    public URL url;

    public FileUtils fileUtils;

    public HttpDownloader() {
        FileUtils fileUtils;
        fileUtils = new FileUtils();
    }

    public static boolean isNetSystemUsable(Context paramContext) {
        boolean bool = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) paramContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities networkCapabilities;
        if (Build.VERSION.SDK_INT >= 23 && (networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork())) != null) {
            bool = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
        }
        return bool;
    }

    public int downloadFile(String url, String filePath, String fileName) {
        if (fileUtils.isFileExist(filePath + fileName)) {
            Log.e("LDKJ", "fileUtils.isFileExist");
            return 1;
        }
        InputStream inputStream = getInputStream(url);
        if (fileUtils.write2SDFromInput(filePath, fileName, inputStream) == null) {
            return -1;
        }

        return 0;
    }

    public InputStream getInputStream(String str) {
        try {
            URL url = new URL(str);
            this.url = url;
            return ((HttpURLConnection) url.openConnection()).getInputStream();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String downloadReleaseVersion(String url) {
        String line = null;
        try {
            HttpURLConnection httpConnection = (HttpURLConnection) new URL(url).openConnection();
            InputStreamReader inputStreamReader = new InputStreamReader((httpConnection).getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            while (true) {
                try {
                    line = bufferedReader.readLine();
                    if (line == null) {
                        System.out.println(stringBuffer.toString());
                        return stringBuffer.toString();
                    }
                    stringBuffer.append(line);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    return line;
                } catch (IOException e2) {
                    e2.printStackTrace();
                    return line;
                }
            }
        } catch (MalformedURLException e3) {
            e3.printStackTrace();
            return null;
        } catch (IOException e4) {
            e4.printStackTrace();
            return null;
        }
    }

}

