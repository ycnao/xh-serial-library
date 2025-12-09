package com.xhuman.serial.data;

import android.util.Log;

import com.xhuman.serial.common.cbus.DevStateValue;
import com.xhuman.serial.common.xh.Xhb;
import com.xhuman.serial.util.Tools;

public class CbusDataOTA {

    public static final int OTA_CMD_IDLE = 0;
    public static final int OTA_CMD_READ = 1;
    public static final int OTA_CMD_START = 3;
    public static final int OTA_CMD_WAIT_EERASE = 2;
    public static final int OTA_CMD_DATA = 4;
    public static final int OTA_CMD_END = 5;
    public static final int OTA_CMD_WAIT_REBOOT = 6;
    public static final int OTA_CMD_WAIT_SUBOTA = 7;
    public static final int OTA_CMD_FINISH = 8;
    public static final int OTA_CMD_STOP = 15;
    public static final int OTA_DATA_BIN_MAX = 153600;
    public static final int OTA_DATA_ONCE_NUM = 100;
    public static final int OTA_DATA_TIME_CYLE = 10;
    public static final int OTA_DATA_TIME_RESEND = 200;
    public static final int OTA_DATA_TIME_OUT = 10000;
    public static final int OTA_STATE_OK = 1;
    public static final int OTA_STATE_ERROR = 0;
    public static int ota_dev_type = 0;
    public static byte[] ota_bin = new byte[153600];
    public static int ota_bin_len = 0;


}

