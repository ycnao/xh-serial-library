package com.xhuman.serial.common.xh;

import com.xhuman.serial.common.cbus.DevStateValue;

public class Xhc {

    public static Boolean f2a = Boolean.FALSE;

    public static int getState(int paramInt) {
        return (paramInt >> 8)& 255;
    }

    public static int getState(int param1, int param2) {
        return (param1 << 8) | param2;
    }

    public static void setState(int paramInt) {
        DevStateValue.visual_ota_data_wait_resp = false;
        DevStateValue.otaComTimeCount = 0;
        if (paramInt != 0) {
            if (paramInt != 1) {
                if (paramInt == 2 && DevStateValue.ota_visual_state == 32)
                    DevStateValue.ota_state = 32;
            } else {
                DevStateValue.ota_state = 2;
            }
        } else {
            DevStateValue.ota_state = 5;
        }
    }
}
