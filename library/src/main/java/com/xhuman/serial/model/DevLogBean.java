package com.xhuman.serial.model;

public class DevLogBean {

    public String deviceLogType;

    public String deviceLogInfo;

    public String deviceLogTime;

    public String getDeviceLogType() {
        return deviceLogType;
    }

    public void setDeviceLogType(String paramString) {
        deviceLogType = paramString;
    }

    public String getDeviceLogInfo() {
        return deviceLogInfo;
    }

    public void setDeviceLogInfo(String paramString) {
        deviceLogInfo = paramString;
    }

    public String getDeviceLogTime() {
        return deviceLogTime;
    }

    public void setDeviceLogTime(String paramString) {
        deviceLogTime = paramString;
    }

    public String toString() {
        return "DeviceLogBean{devicelog_type='" + deviceLogType + '\'' + ", devicelog_info=" + deviceLogInfo + '}';
    }
}
