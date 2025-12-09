package com.xhuman.serial.listener;

public interface DownloadListener {

    void onDownloadComplete(byte[] arrayOfByte, int length);

    void onDownloadFailed(String error);

}
