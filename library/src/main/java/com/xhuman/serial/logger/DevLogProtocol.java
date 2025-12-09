package com.xhuman.serial.logger;

import android.content.Context;

import com.xhuman.serial.model.DevLogBean;
import com.xhuman.serial.data.ErrorCodeMap;
import com.xhuman.serial.R;
import com.xhuman.serial.common.cbus.DevStateValue;
import com.xhuman.serial.util.XUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DevLogProtocol {

    public static List<DevLogManager.LogEntry> logEntryList;

    public static Context mContext;

    public static int b = -1;

    public static int c = -1;

    public static int d = -1;

    public static float e = -1.0F;

    public static float f = -1.0F;

    public static int g = -1;

    public static int h = -1;

    public static int i = -1;

    public static int j = -1;

    public static int k = -1;

    public static int l = -1;

    public static int m = -1;

    public static int n = -1;

    public static int o = -1;

    public static int[][] p;

    public static int q = -1;


    public static void device_control_log() {
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11 = b;
        if (i11 != -1 && i11 != (i10 = DevStateValue.run_mode)) {
            if (i10 >= 5) {
                i10 = i10 == 101 ? 5 : i10 == 102 ? 6 : i10 == 201 ? 7 : i10 == 202 ? 8 : 0;
            }
            DevLogManager.instance().addLog(0, 0, i10);
        }
        b = DevStateValue.run_mode;
        float f2 = e;
        if (f2 != -1.0f) {
            float f3 = DevStateValue.run_propulsionLeft;
            if (f2 != f3) {
                DevLogManager.instance().addLog(0, 1, f3 > 0.0f ? 10 : 9);
            }
        }
        e = DevStateValue.run_propulsionLeft;
        float f4 = f;
        if (f4 != -1.0f) {
            float f5 = DevStateValue.run_propulsionRight;
            if (f4 != f5) {
                DevLogManager.instance().addLog(0, 2, f5 > 0.0f ? 10 : 9);
            }
        }
        f = DevStateValue.run_propulsionRight;
        int i12 = d;
        if (i12 != -1 && i12 != (i9 = DevStateValue.run_Adhesion)) {
            DevLogManager.instance().addLog(0, 3, i9 > 6 ? -1 : i9 + 9);
        }
        d = DevStateValue.run_Adhesion;
        int i13 = c;
        if (i13 != -1 && i13 != (i8 = DevStateValue.run_updownState)) {
            DevLogManager.instance().addLog(0, 4, i8 > 6 ? -1 : i8 + 9);
        }
        c = DevStateValue.run_updownState;
        int i14 = g;
        if (i14 != -1 && i14 != (i7 = DevStateValue.run_dirState)) {
            DevLogManager.instance().addLog(0, 5, i7 > 6 ? -1 : i7 + 9);
        }
        g = DevStateValue.run_dirState;
        int i15 = h;
        if (i15 != -1 && i15 != (i6 = DevStateValue.run_Clean)) {
            DevLogManager.instance().addLog(0, 6, i6 > 6 ? -1 : i6 + 9);
        }
        h = DevStateValue.run_Clean;
        int i16 = i;
        if (i16 != -1 && i16 != (i5 = DevStateValue.water_state)) {
            DevLogManager.instance().addLog(0, 7, i5 > 6 ? -1 : i5 + 9);
        }
        i = DevStateValue.water_state;
        int i17 = j;
        if (i17 != -1 && k != -1 && (i17 != DevStateValue.run_waterHeatState || k != DevStateValue.run_waterHeatValue)) {
            if (DevStateValue.run_waterHeatState == 1) {
                int i18 = DevStateValue.run_waterHeatValue;
                i4 = i18 == 10 ? 17 : i18 == 20 ? 18 : i18 == 30 ? 19 : i18 == 40 ? 20 : -1;
            } else {
                i4 = 16;
            }
            DevLogManager.instance().addLog(0, 8, i4);
        }
        j = DevStateValue.run_waterHeatState;
        k = DevStateValue.run_waterHeatValue;
        int i19 = l;
        if (i19 != -1 && i19 != (i3 = DevStateValue.run_auto_Type)) {
            DevLogManager.instance().addLog(0, 9, i3 == 0 ? 21 : i3 == 5 ? 22 : i3 == 6 ? 23 : i3 == 10 ? 24 : -1);
        }
        l = DevStateValue.run_auto_Type;
        int i20 = m;
        if (i20 != -1 && i20 != (i2 = DevStateValue.run_auto_recycleWater)) {
            DevLogManager.instance().addLog(0, 10, i2 <= 6 ? i2 + 9 : -1);
        }
        m = DevStateValue.run_auto_recycleWater;
    }

    public static void device_sensor_log() {
        int i2;
        int i3;
        if (DevStateValue.deviceLineState == 1 && (i3 = DevStateValue.deviceRSSI) != 0) {
            int i4 = i3 > 70 ? 1 : 0;
            if (i4 == 0 && n != i4) {
                DevLogManager.instance().addLog(1, 0, i4);
            }
            n = i4;
        }
        int i5 = o;
        if (i5 != -1 && i5 != (i2 = DevStateValue.touch_state)) {
            DevLogManager.instance().addLog(1, 2, i2 == 1 ? 2 : 3);
        }
        o = DevStateValue.touch_state;
    }

    public static void device_error_log() {
        boolean z;
        boolean z2;
        if (p == null && DevStateValue.err_code_arry == null) {
            return;
        }
        if (p == null) {
            for (int[] iArr : DevStateValue.err_code_arry) {
                DevLogManager.instance().addLog(3, iArr[0], iArr[1]);
            }
        } else {
            int[][] iArr2 = DevStateValue.err_code_arry;
            if (iArr2 == null) {
                for (int[] iArr3 : p) {
                    DevLogManager.instance().addLog(4, iArr3[0], iArr3[1]);
                }
            } else {
                for (int[] iArr4 : iArr2) {
                    int[][] iArr5 = p;
                    int length = iArr5.length;
                    int i2 = 0;
                    while (true) {
                        if (i2 >= length) {
                            z2 = false;
                            break;
                        } else {
                            if (Arrays.equals(iArr4, iArr5[i2])) {
                                z2 = true;
                                break;
                            }
                            i2++;
                        }
                    }
                    if (!z2) {
                        DevLogManager.instance().addLog(3, iArr4[0], iArr4[1]);
                    }
                }
                for (int[] iArr6 : p) {
                    int[][] iArr7 = DevStateValue.err_code_arry;
                    int length2 = iArr7.length;
                    int i3 = 0;
                    while (true) {
                        if (i3 >= length2) {
                            z = false;
                            break;
                        } else {
                            if (Arrays.equals(iArr6, iArr7[i3])) {
                                z = true;
                                break;
                            }
                            i3++;
                        }
                    }
                    if (!z) {
                        DevLogManager.instance().addLog(4, iArr6[0], iArr6[1]);
                    }
                }
            }
        }
        p = DevStateValue.err_code_arry;
    }

    public static void device_line_log(int paramInt1, int paramInt2) {
        if (paramInt1 == 0) {
            DevLogManager.instance().addLog(5, paramInt1, paramInt2);
            return;
        }
        if (paramInt1 == 1) {
            int i4 = q;
            if (i4 != -1 && i4 != paramInt2) {
                if (paramInt2 == 0) {
                    b = -1;
                    c = -1;
                    d = -1;
                    e = -1.0f;
                    f = -1.0f;
                    g = -1;
                    h = -1;
                    i = -1;
                    j = -1;
                    k = -1;
                    l = -1;
                    m = -1;
                    n = -1;
                    p = null;
                    DevStateValue.err_code_arry = null;
                }
                DevLogManager.instance().addLog(5, paramInt1, paramInt2);
            }
            q = paramInt2;
        }
    }

    public static void device_ota_log(int paramInt, String paramString) {
        DevLogManager.instance().addLog(7, paramInt, 0);
    }

    public static void update_device_log() {
        device_control_log();
        device_sensor_log();
    }

    public static List<DevLogBean> get_string_info(Context context) {
        mContext = context;
        ArrayList<DevLogBean> arrayList = new ArrayList<>();
        logEntryList = DevLogManager.instance().getAllLogs();
        for (int i = 0; i < logEntryList.size(); i++) {
            DevLogBean deviceLogBean = new DevLogBean();
            String str1;
            String[] arrayOfString = XUtils.getStringArray(context,R.array.dev_log_type);
            String str2 = "[" + (i + 1) + "] ";
            StringBuilder stringBuilder = (new StringBuilder()).append(str2);
            DevLogManager.LogEntry logEntry;
            if ((logEntry = logEntryList.get(i)).getEventType() < arrayOfString.length) {
                str1 = arrayOfString[logEntry.getEventType()];
            } else {
                str1 = XUtils.getResStr(context,R.string.dev_log_state_unknown) + "[" + logEntry.getEventType() + "]";
            }
            deviceLogBean.setDeviceLogType(stringBuilder.append(str1).toString());
            if (logEntry.getEventType() == 0) {
                String str3;
                String str4;
                String[] arrayOfString1 = XUtils.getStringArray(context,R.array.dev_log_control_id);
                if (logEntry.getEventId() < arrayOfString1.length) {
                    str3 = arrayOfString1[logEntry.getEventId()];
                } else {
                    str3 = XUtils.getResStr(context,R.string.dev_log_state_unknown) + "[" + logEntry.getEventId() + "]";
                }
                String[] arrayOfString2 = XUtils.getStringArray(context,R.array.dev_log_control_info);
                if (logEntry.getEventValue() == -1) {
                    str4 = XUtils.getResStr(context,R.string.dev_log_state_unknown);
                } else if (logEntry.getEventValue() < arrayOfString2.length) {
                    str4 = arrayOfString2[logEntry.getEventValue()];
                } else {
                    str4 = XUtils.getResStr(context,R.string.dev_log_state_unknown) + "[" + logEntry.getEventValue() + "]";
                }
                deviceLogBean.setDeviceLogInfo(str3 + str4);
                continue;
            }
            if (logEntry.getEventType() == 1) {
                String str3;
                String str4;
                String[] arrayOfString1 = XUtils.getStringArray(context,R.array.dev_log_sensor_id);
                if (logEntry.getEventId() < arrayOfString1.length) {
                    str3 = arrayOfString1[logEntry.getEventId()];
                } else {
                    str3 = XUtils.getResStr(context,R.string.dev_log_state_unknown) + "[" + logEntry.getEventId() + "]";
                }
                String[] arrayOfString2 = XUtils.getStringArray(context,R.array.dev_log_sensor_info);
                if (logEntry.getEventValue() == -1) {
                    str4 = XUtils.getResStr(context,R.string.dev_log_state_unknown);
                } else if (logEntry.getEventValue() < arrayOfString2.length) {
                    str4 = arrayOfString2[logEntry.getEventValue()];
                } else {
                    str4 = XUtils.getResStr(context,R.string.dev_log_state_unknown) + "[" + logEntry.getEventValue() + "]";
                }
                deviceLogBean.setDeviceLogInfo(str3 + str4);
                continue;
            }
            if (logEntry.getEventType() == 2) {
                String str3;
                String str4;
                String[] arrayOfString1 = XUtils.getStringArray(context,R.array.dev_log_out_id);
                if (logEntry.getEventId() < arrayOfString1.length) {
                    str3 = arrayOfString1[logEntry.getEventId()];
                } else {
                    str3 = XUtils.getResStr(context,R.string.dev_log_state_unknown) + "[" + logEntry.getEventId() + "]";
                }
                String[] arrayOfString2 = XUtils.getStringArray(context,R.array.dev_log_out_info);
                if (logEntry.getEventValue() == -1) {
                    str4 = XUtils.getResStr(context,R.string.dev_log_state_unknown);
                } else if (logEntry.getEventValue() < arrayOfString2.length) {
                    str4 = arrayOfString2[logEntry.getEventValue()];
                } else {
                    str4 = XUtils.getResStr(context,R.string.dev_log_state_unknown) + "[" + logEntry.getEventValue() + "]";
                }
                deviceLogBean.setDeviceLogInfo(str3 + str4);
                continue;
            }
            if (logEntry.getEventType() == 3 || logEntry.getEventType() == 4) {
                int j = logEntry.getEventId();
                String str;
                if ((str = ErrorCodeMap.getErrCodeString(mContext, j, logEntry.getEventValue())) == null) {
                    deviceLogBean.setDeviceLogInfo(XUtils.getResStr(context,R.string.dev_log_state_unknown) + "[" + logEntry.getEventId() + "][" + logEntry.getEventValue() + "]");
                    continue;
                }
            } else {
                if (logEntry.getEventType() == 5) {
                    String str3;
                    String str4;
                    String[] arrayOfString1 = XUtils.getStringArray(context,R.array.dev_log_line_from);
                    if (logEntry.getEventId() < arrayOfString1.length) {
                        str3 = arrayOfString1[logEntry.getEventId()];
                    } else {
                        str3 = XUtils.getResStr(context,R.string.dev_log_state_unknown) + "[" + logEntry.getEventId() + "]";
                    }
                    String[] arrayOfString2 = XUtils.getStringArray(context,R.array.dev_log_line_state);
                    if (logEntry.getEventValue() == -1) {
                        str4 = XUtils.getResStr(context,R.string.dev_log_state_unknown);
                    } else if (logEntry.getEventValue() < arrayOfString2.length) {
                        str4 = arrayOfString2[logEntry.getEventValue()];
                    } else {
                        str4 = XUtils.getResStr(context,R.string.dev_log_state_unknown) + "[" + logEntry.getEventValue() + "]";
                    }
                    deviceLogBean.setDeviceLogInfo(str3 + str4);
                    continue;
                }
                if (logEntry.getEventType() == 7) {
                    String[] arrayOfString1 = XUtils.getStringArray(context,R.array.dev_log_ota);
                    if (logEntry.getEventId() < arrayOfString1.length) {
                        str1 = arrayOfString1[logEntry.getEventId()];
                    } else {
                        str1 = XUtils.getResStr(context,R.string.dev_log_state_unknown) + "[" + logEntry.getEventId() + "]";
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
}

