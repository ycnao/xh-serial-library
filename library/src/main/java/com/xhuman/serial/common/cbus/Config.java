package com.xhuman.serial.common.cbus;

public class Config {

    public static final String TAG = "LDKJ";
    public static final String FACTORY_PASSWORD = "123123";
    public static final String PRODUCT_SECRETKEY = "LINGD";
    public static final String REMOTE_URL = "https://www.pgyer.com/lingdu";
    public static final String VERSION_APP = "2.2_20240620";
    public static boolean touch_switch_enable = false;
    public static final String HANDSHAKE_VERSION = "2.0";
    public static final String PROTOCOL_VERSION = "2.0";
    public static final String HANDSHAKE_SEPARATOR_MASK = "#";
    public static final String REMOTE_JSON_URL = "https://remote-token.oss-cn-beijing.aliyuncs.com/";
    public static final String REMOTE_JSON_NAME = "remote_token.json";
    public static final String PRODUCT_TYPE_LK = "lingkong";
    public static final String PRODUCT_TYPE_LK_A1 = "lingkong-a1";
    public static final String PRODUCT_TYPE_LK_B1 = "lingkong-b1";
    public static final String PRODUCT_TYPE_LG_A = "lingguang-a";
    public static final String PRODUCT_TYPE_LG_A1 = "lingguang-a1";
    public static final String PRODUCT_TYPE_LG_A2 = "lingguang-a2";
    public static final String PRODUCT_TYPE_LG_A3 = "lingguang-a3";
    public static final String PRODUCT_TYPE_LG_B = "lingguang-b";
    public static final String PRODUCT_TYPE_LG_B1 = "lingguang-b1";
    public static final String PRODUCT_TYPE_LG_B2 = "lingguang-b2";
    public static final String PRODUCT_TYPE_LG_B3 = "lingguang-b3";
    public static final String PRODUCT_TYPE_LG_B4 = "lingguang-b4";
    public static final String PRODUCT_TYPE_LJ_A = "lingjing-a";
    public static final String PRODUCT_TYPE_LJ_A1 = "lingjing-a1";
    public static final String PRODUCT_TYPE_LJ_A2 = "lingjing-a2";
    public static final String PRODUCT_TYPE_LJ_B = "lingjing-b";
    public static final String PRODUCT_TYPE_LJ_B1 = "lingjing-b1";
    public static final String PRODUCT_TYPE_LJ_B2 = "lingjing-b2";

    public static final int DEVICE_TYPE_UNKNOW = 0;

    public static final int DEVICE_TYPE_LINGKONG = 1;

    public static final int DEVICE_TYPE_LINGJING_A = 2;

    public static final int DEVICE_TYPE_LINGJING_B = 3;

    public static final int DEVICE_TYPE_LINGGUANG = 4;

    public static final int DEVICE_TYPE_LINGKONG_B = 5;

    public static final int HANDEL_UPDAREMOTEUI = 6;

    public static final int HANDEL_UPDATA_CBUS = 18;

    public static final int OTA_TYPE_HOST = 0;

    public static final int OTA_TYPE_SUB = 1;

    public static final String INSTALL_PATH = "H12/";

    public static final String OTA_PATH = "H12/OTA/";

    public static final int WAIT_COM_STATE_NONE = 0;

    public static final int WAIT_COM_STATE_FAIL = 5;

    public static final int WAIT_COM_STATE_SUCCESS_GetSpeed = 1006;

    public static final int WAIT_COM_STATE_SUCCESS_SetSpeed = 1007;

    public static final int WAIT_COM_STATE_SUCCESS_GetConfig = 1008;

    public static final int WAIT_COM_STATE_SUCCESS_SetConfig = 1009;

    public static final int WAIT_COM_STATE_SUCCESS_GetAutodata = 1010;

    public static final int WAIT_COM_STATE_SUCCESS_SetAutodata = 1011;

    public static final int WAIT_COM_STATE_SUCCESS_GetRunLog = 1012;

    public static final int WAIT_COM_STATE_SUCCESS_SetRunLog = 1013;

    public static final int WAIT_COM_STATE_SUCCESS_SetVisualOTA = 1014;

    public static final int ACTIVITY_MAIN = 100;

    public static final int ACTIVITY_SETSPEED = 101;

    public static final int ACTIVITY_SETCONFIG = 102;

    public static final int ACTIVITY_SETAPP = 103;

    public static final int GET_DEVICE_TIMER = 300;

    public static final int LINE_TIMER_NUM = 10;

    public static final int LINE_STATE_ON = 1;

    public static final int LINE_STATE_OFF = 0;

    public static final int VOIVE_TIMER_NUM = 15;

    public static final int ERROR_VALUE_RSSI = 40;

    public static final int ERROR_VALUE_UPDOWNLOAD = 2800;

    public static final int ERROR_VALUE_BATTERY = 20;

    public static final int WATER_HEAT_E_SENSOR = 101;

    public static final int WATER_HEAT_E_TIMELONG = 102;

    public static final int WATER_HEAt_E_OTHER = 103;

    public static final int WATER_HEAT_LEVEL_OFF_VALUE = 0;

    public static final int WATER_HEAT_LEVEL_1_VALUE = 10;

    public static final int WATER_HEAT_LEVEL_2_VALUE = 20;

    public static final int WATER_HEAT_LEVEL_3_VALUE = 30;

    public static final int WATER_HEAT_LEVEL_4_VALUE = 40;

    public static final int DIR_SENSOR_E_INSTALL = 101;

    public static final int DIR_SENSOR_E_NONE = 102;

    public static final int STATE_OFF = 0;

    public static final int STATE_ON = 1;

    public static final int STATE_UPLEFT = 2;

    public static final int STATE_DOWNRIGHT = 3;

    public static final int STATE_CENTER = 4;

    public static final int STATE_LEFT_SPIN = 5;

    public static final int STATE_RIGHT_SPIN = 6;

    public static final int STATE_E_OTHER = 100;

    public static final int STATE_E_INSTALL = 101;

    public static final int STATE_E_NONE = 102;

    public static final int STATE_UNKNOW = 200;

    public static final int MODE_STANDBY = 0;

    public static final int MODE_HAND = 1;

    public static final int MODE_AUTO = 2;

    public static final int MODE_VISUAL = 3;

    public static final int MODE_CONFIG_REMOTE = 101;

    public static final int MODE_PROPULSION = 102;

    public static final int MODE_FACTORY = 201;

    public static final int MODE_REPAIR = 202;

    public static final int MODE_UNKNOWN = 250;

    public static final int AUTO_TYPE_HAND = 0;

    public static final int AUTO_TYPE_LEFT = 1;

    public static final int AUTO_TYPE_CENTER = 2;

    public static final int AUTO_TYPE_RIGHT = 3;

    public static final int AUTO_TYPE_IMU = 4;

    public static final int AUTO_TYPE_VISUAL_Q = 5;

    public static final int AUTO_TYPE_VISUAL_F = 6;

    public static final int AUTO_TYPE_AUTOSTART = 10;

    public static final int CTL_ATTR_E_UNSPORT = 65534;

    public static final int CTL_ATTR_E_READ = 65533;

    public static final int CTL_ATTR_E_SET = 65532;

    public static final int CTL_ATTR_E_DISBALE = 65527;
}

