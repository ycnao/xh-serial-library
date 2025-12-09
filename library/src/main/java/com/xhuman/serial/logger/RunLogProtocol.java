package com.xhuman.serial.logger;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.xhuman.serial.model.DevLogBean;
import com.xhuman.serial.data.ErrorCodeMap;
import com.xhuman.serial.R;
import com.xhuman.serial.util.XUtils;

public class RunLogProtocol {

    public static Context mContext;
    public static List<RunLogManager.LogEntry> list;


    public static void reset_run_log() {
        RunLogManager.instance().clearLog();
    }

    public static void add_run_log(int eventType, int eventId, int eventValue, long timestamp) {
        RunLogManager.instance().addLog(eventType, eventId, eventValue, timestamp);
    }

    public static List<DevLogBean> get_string_info(Context context) {
        mContext = context;
        List<DevLogBean> arrayList = new ArrayList<>();
        Collections.sort(list = RunLogManager.instance().getAllLogs(), new RunLogA());
        for (int i = 0; i < list.size(); i++) {
            DevLogBean deviceLogBean = new DevLogBean();
            String str1 = "";
            String[] arrayOfString = XUtils.getStringArray(context, R.array.dev_log_type);
            String str2 = "[" + (i + 1) + "] ";
            StringBuilder stringBuilder = (new StringBuilder()).append(str2);
            RunLogManager.LogEntry logEntry;
            if ((logEntry = list.get(i)).getEventType() < arrayOfString.length) {
                str1 = arrayOfString[logEntry.getEventType()];
            } else {
                str1 = XUtils.getResStr(context, R.string.dev_log_state_unknown) + "[" + logEntry.getEventType() + "]";
            }
            deviceLogBean.setDeviceLogType(stringBuilder.append(str1).toString());
            if (logEntry.getEventType() == 0) {
                String str3;
                String str4;
                String[] arrayOfString1 = XUtils.getStringArray(context, R.array.dev_log_control_id);
                if (logEntry.getEventId() < arrayOfString1.length) {
                    str3 = arrayOfString1[logEntry.getEventId()];
                } else {
                    str3 = XUtils.getResStr(context, R.string.dev_log_state_unknown) + "[" + logEntry.getEventId() + "]";
                }
                String[] arrayOfString2 = XUtils.getStringArray(context, R.array.dev_log_control_info);
                if (logEntry.getEventValue() == -1) {
                    str4 = XUtils.getResStr(context, R.string.dev_log_state_unknown);
                } else if (logEntry.getEventValue() < arrayOfString2.length) {
                    str4 = arrayOfString2[logEntry.getEventValue()];
                } else {
                    str4 = XUtils.getResStr(context, R.string.dev_log_state_unknown) + "[" + logEntry.getEventValue() + "]";
                }
                deviceLogBean.setDeviceLogInfo(str3 + str4);
                continue;
            }
            if (logEntry.getEventType() == 1) {
                String str3;
                String str4;
                String[] arrayOfString1 = XUtils.getStringArray(context, R.array.dev_log_sensor_id);
                if (logEntry.getEventId() < arrayOfString1.length) {
                    str3 = arrayOfString1[logEntry.getEventId()];
                } else {
                    str3 = XUtils.getResStr(context, R.string.dev_log_state_unknown) + "[" + logEntry.getEventId() + "]";
                }
                String[] arrayOfString2 = XUtils.getStringArray(context, R.array.dev_log_sensor_info);
                if (logEntry.getEventValue() == -1) {
                    str4 = XUtils.getResStr(context, R.string.dev_log_state_unknown);
                } else if (logEntry.getEventValue() < arrayOfString2.length) {
                    str4 = arrayOfString2[logEntry.getEventValue()];
                } else {
                    str4 = XUtils.getResStr(context, R.string.dev_log_state_unknown) + "[" + logEntry.getEventValue() + "]";
                }
                deviceLogBean.setDeviceLogInfo(str3 + str4);
                continue;
            }
            if (logEntry.getEventType() == 2) {
                String str3;
                String str4;
                String[] arrayOfString1 = XUtils.getStringArray(context, R.array.dev_log_out_id);
                if (logEntry.getEventId() < arrayOfString1.length) {
                    str3 = arrayOfString1[logEntry.getEventId()];
                } else {
                    str3 = XUtils.getResStr(context, R.string.dev_log_state_unknown) + "[" + logEntry.getEventId() + "]";
                }
                String[] arrayOfString2 = XUtils.getStringArray(context, R.array.dev_log_out_info);
                if (logEntry.getEventValue() == -1) {
                    str4 = XUtils.getResStr(context, R.string.dev_log_state_unknown);
                } else if (logEntry.getEventValue() < arrayOfString2.length) {
                    str4 = arrayOfString2[logEntry.getEventValue()];
                } else {
                    str4 = XUtils.getResStr(context, R.string.dev_log_state_unknown) + "[" + logEntry.getEventValue() + "]";
                }
                deviceLogBean.setDeviceLogInfo(str3 + str4);
                continue;
            }
            if (logEntry.getEventType() == 3 || logEntry.getEventType() == 4) {
                int j = logEntry.getEventId();
                String str;
                if ((str = ErrorCodeMap.getErrCodeString(mContext, j, logEntry.getEventValue())) == null) {
                    deviceLogBean.setDeviceLogInfo(XUtils.getResStr(context, R.string.dev_log_state_unknown) + "[" + logEntry.getEventId() + "][" + logEntry.getEventValue() + "]");
                    continue;
                }
            } else {
                if (logEntry.getEventType() == 5) {
                    String str3;
                    String str4;
                    String[] arrayOfString1 = XUtils.getStringArray(context, R.array.dev_log_line_from);
                    if (logEntry.getEventId() < arrayOfString1.length) {
                        str3 = arrayOfString1[logEntry.getEventId()];
                    } else {
                        str3 = XUtils.getResStr(context, R.string.dev_log_state_unknown) + "[" + logEntry.getEventId() + "]";
                    }
                    String[] arrayOfString2 = XUtils.getStringArray(context, R.array.dev_log_line_state);
                    if (logEntry.getEventValue() == -1) {
                        str4 = XUtils.getResStr(context, R.string.dev_log_state_unknown);
                    } else if (logEntry.getEventValue() < arrayOfString2.length) {
                        str4 = arrayOfString2[logEntry.getEventValue()];
                    } else {
                        str4 = XUtils.getResStr(context, R.string.dev_log_state_unknown) + "[" + logEntry.getEventValue() + "]";
                    }
                    deviceLogBean.setDeviceLogInfo(str3 + str4);
                    continue;
                }
                if (logEntry.getEventType() == 7) {
                    String[] arrayOfString1 = XUtils.getStringArray(context, R.array.dev_log_ota);
                    if (logEntry.getEventId() < arrayOfString1.length) {
                        String str = arrayOfString1[logEntry.getEventId()];
                    } else {
                        str1 = XUtils.getResStr(context, R.string.dev_log_state_unknown) + "[" + logEntry.getEventId() + "]";
                    }
                } else {
                    continue;
                }
            }
            deviceLogBean.setDeviceLogInfo(str1);
            deviceLogBean.setDeviceLogTime(logEntry.getFormattedTimestamp());
            arrayList.add(deviceLogBean);
        }
        return arrayList;
    }

    static class RunLogA implements Comparator<RunLogManager.LogEntry> {

        @Override
        public int compare(RunLogManager.LogEntry param1, RunLogManager.LogEntry param2) {
            RunLogManager.LogEntry logEntry = (RunLogManager.LogEntry) param1;
            return logEntry.getTimestamp().compareTo(((RunLogManager.LogEntry) param2).getTimestamp());
        }
    }
}

