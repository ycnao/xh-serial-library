package com.xhuman.serial.common.xh;

import android.util.Log;

import com.xhuman.serial.common.cbus.DevStateValue;
import com.xhuman.serial.data.CbusDataOTA;

public class Xhb {

    public static int f1a;
    public static int b;
    public static int c;
    public static int d;
    public static int e;

    public static void a(int param1, int param2) {
        DevStateValue.otaComTimeCount = 0;
        if (param1 == 160) {
            if (Xhc.getState(param2) != 1) {
                DevStateValue.ota_state = 15;
                Log.e("CbusDataOTA", "OTAAttrValueHandle notify state is error");
                return;
            }
            int i3 = DevStateValue.ota_state;
            if (i3 == 2) {
                d = 0;
                c = 0;
                DevStateValue.ota_state = 4;
                Log.d("CbusDataOTA", "recv OTA_CMD_WAIT_EERASE notify");
                return;
            }
            if (i3 == 6) {
                if (CbusDataOTA.ota_dev_type == 1) {
                    DevStateValue.ota_state = 7;
                } else {
                    DevStateValue.ota_state = 8;
                    DevStateValue.ota_progress = 0;
                }
                Log.d("CbusDataOTA", "recv OTA_CMD_WAIT_REBOOT notify");
                return;
            }
            return;
        }
        if (param1 != 161) {
            if (param1 != 168) {
                Log.e("CbusDataOTA", "OTAAttrValue2UIValue not supported attr id:" + param1);
            }
            DevStateValue.ota_progress = param2;
            Log.e("CbusDataOTA", "download:" + Xhc.getState(DevStateValue.ota_progress) + "  update" + (DevStateValue.ota_progress & 255));
            if (CbusDataOTA.ota_dev_type != 1 || (DevStateValue.ota_progress & 255) < 100) {
                return;
            }
            DevStateValue.ota_state = 8;
            DevStateValue.ota_progress = 0;
            DevStateValue.ota_flag = true;
            DevStateValue.deviceSubSoftVersion = DevStateValue.deviceSubNewSoftVersion;
            return;
        }
        if (Xhc.getState(param2) != 1) {
            DevStateValue.ota_state = 15;
            Log.e("CbusDataOTA", "OTAAttrValueHandle resp state is error");
            return;
        }
        switch (DevStateValue.ota_state) {
            case 3:
                DevStateValue.ota_state = 2;
                Log.d("CbusDataOTA", "recv OTA_CMD_START resp");
                break;
            case 4:
                d = 0;
                int i4 = c + 1;
                c = i4;
                if (i4 >= f1a) {
                    DevStateValue.ota_state = 5;
                }
                Log.d("CbusDataOTA", "recv OTA_CMD_START resp index:" + c + "/" + f1a);
                break;
            case 5:
                DevStateValue.ota_state = 6;
                Log.d("CbusDataOTA", "recv OTA_CMD_END resp");
                break;
            case 7:
                d = 0;
                break;
        }
    }
}
