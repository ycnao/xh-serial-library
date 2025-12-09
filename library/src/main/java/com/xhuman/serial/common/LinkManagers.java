package com.xhuman.serial.common;

import android.util.Log;

import com.skydroid.fpvlibrary.serial.SerialPortConnection;
import com.xhuman.serial.R;
import com.xhuman.serial.common.cbus.Config;
import com.xhuman.serial.common.cbus.DevStateValue;
import com.xhuman.serial.common.cbus.XcBUS;
import com.xhuman.serial.common.xh.Xha;
import com.xhuman.serial.common.xh.Xhb;
import com.xhuman.serial.common.xh.Xhc;
import com.xhuman.serial.helper.VoicePlayHelper;
import com.xhuman.serial.listener.ReceiverListener;
import com.xhuman.serial.logger.DevLogProtocol;
import com.xhuman.serial.logger.RunLogProtocol;
import com.xhuman.serial.util.DataUtil;
import com.xhuman.serial.util.DataUtils;
import com.xhuman.serial.util.Tools;

import java.lang.reflect.Array;

/**
 * author：created by 闹闹 on 2025/11/7
 * version：v1.0.0
 */
public class LinkManagers {

    private static final String TAG = "Cbus_LinkManagers";
    private static LinkManagers instance; // 单例实例（volatile保证可见性）
    // 串口连接实例
    private SerialPortConnection serialConnection;
    // 设备线路计时器计数
    private int deviceLineTimerCount = 0;
//    // 接收数据监听器
    private ReceiverListener receiverListener;
//
//    // 私有构造方法，防止外部实例化
//    private LinkManagers() {
//    }
//
//    /**
//     * 获取单例实例（双重校验锁，线程安全）
//     */
//    public static LinkManagers instance() {
//        if (instance == null) {
//            synchronized (LinkManagers.class) {
//                if (instance == null) {
//                    instance = new LinkManagers();
//                }
//            }
//        }
//        return instance;
//    }
//
//    /**
//     * 初始化串口连接
//     *
//     * @param listener 接收数据的监听器
//     * @return 初始化后的串口连接实例
//     */
//    public SerialPortConnection init(ReceiverListener listener) {
//        if (listener == null) {
//            throw new IllegalArgumentException("ReceiverListener cannot be null");
//        }
//        this.receiverListener = listener;
//        // 若已存在连接，先关闭
//        if (serialConnection != null && serialConnection.isConnection()) {
//            try {
//                serialConnection.closeConnection();
//            } catch (Exception e) {
//                Log.e(TAG, "Failed to close existing connection", e);
//            }
//        }
//        // 创建新的串口连接（设备路径、波特率可配置）
//        serialConnection = SerialPortConnection.newBuilder("/dev/ttyHS1", 921600)
//                .flags(8192)
//                .build();
//
//        createSocket(); // 打开连接
//        return serialConnection;
//    }
//
//    /**
//     * 打开串口连接
//     */
//    public void createSocket() {
//        if (serialConnection == null) {
//            Log.e(TAG, "SerialPortConnection is not initialized");
//            return;
//        }
//
//        if (!serialConnection.isConnection()) {
//            Log.d(TAG, "Opening serial port connection");
//            // 设置回调代理，处理接收到的数据
//            serialConnection.setDelegate(new SerialPortDelegate(this));
//            try {
//                serialConnection.openConnection();
//                Log.d(TAG, "Serial port opened successfully");
//            } catch (Exception e) {
//                Log.e(TAG, "Failed to open serial port", e); // 修复原代码null.printStackTrace()错误
//            }
//        } else {
//            Log.d(TAG, "Serial port is already connected");
//        }
//    }
//
//    /**
//     * 获取当前串口连接实例
//     */
//    public SerialPortConnection getDataConnection() {
//        return serialConnection;
//    }
//
//    /**
//     * 获取设备线路计时器计数
//     */
//    public int getDeviceLineTimerCount() {
//        return deviceLineTimerCount;
//    }
//
//    /**
//     * 减少设备线路计时器计数（最低为0）
//     */
//    public void subtractDeviceLineTimerCount() {
//        if (deviceLineTimerCount > 0) {
//            deviceLineTimerCount--;
//        }
//    }
//
//    /**
//     * 增加设备线路计时器计数（防止溢出）
//     */
//    public void addDeviceLineTimerCount() {
//        if (deviceLineTimerCount < Integer.MAX_VALUE - 1) {
//            deviceLineTimerCount++;
//        }
//    }
//
//    /**
//     * 关闭串口连接（释放资源）
//     */
//    public void closeConnection() {
//        if (serialConnection != null && serialConnection.isConnection()) {
//            try {
//                serialConnection.closeConnection();
//                Log.d(TAG, "Serial port closed successfully");
//            } catch (Exception e) {
//                Log.e(TAG, "Failed to close serial port", e);
//            }
//        }
//    }
//
    class SerialPortDelegate implements SerialPortConnection.Delegate {

        private LinkManagers linkManager;

        public SerialPortDelegate(LinkManagers linkManager) {
            this.linkManager = linkManager;
        }

        public void received(byte[] data, int i) {
            String s = DataUtils.byteArrToHex(data);
            Log.e(TAG, "Cbus-Link resp data:" + s);
            Log.e(TAG, "Cbus-Link resp data:" + new String(data));

            int i2;
            Boolean bool = Xhc.f2a;
            if (Xha.state != 2) {
                int i3 = Xha.retry + 1;
                Xha.retry = i3;
                if (i3 > 15) {
                    Xha.retry = 0;
                    Xha.state = 0;
                    Xha.b = 0;
                    Xha.c = 0;
                }
            }
            int i4 = Xha.state;
            if (i4 != 0) {
                if (i4 == 1) {
                    if (Xha.b == 200) {
                        int i5 = Xha.c;
                        if (i5 > 3) {
                            Xha.b = Xha.f0a[3] & 255;
                            Xha.b = Math.min(Xha.f0a[3] & 255, 200);
                        } else if (i5 + i >= 4) {
                            Xha.b = data[3 - i5] & 255;
                            if ((Xha.f0a[1] & 255) == 241 && Xha.b == 170) {
                                Xha.retry = 0;
                                Xha.state = 0;
                                Xha.b = 0;
                                Xha.c = 0;
                            } else {
                                Xha.b = Math.min(Xha.b, 200);
                            }
                        }
                        if (Xha.c + i > Xha.b) {
                        }
                    } else if (Xha.c + i > Xha.b) {
                        Tools.arraycopy(data, 0, Xha.f0a, Xha.c, i);
                        Xha.c += i;
                    } else {
                        Tools.arraycopy(data, 0, Xha.f0a, Xha.c, Xha.b > Xha.c ? Xha.b - Xha.c : 0);
                        Xha.c += Xha.b - Xha.c;
                    }
                }
            } else if (-86 == data[0]) {
                Tools.arraycopy(data, 0, Xha.f0a, 0, i);
                if (i > 3) {
                    Xha.b = Math.min(data[3] & 255, 200);
                } else {
                    Xha.b = 200;
                }
                Xha.c = i;
                Xha.state = 1;
            } else {
                Xha.c = 0;
                Xha.retry = 0;
                Xha.b = 0;
                Log.e(TAG, "cbus data recv flag failed");
            }

            if (Xha.b > 2 && Xha.c >= Xha.b) {
                Xha.retry = 0;
//                byte b1 = Tools.BCC8(Xha.f0a, Xha.b - 1);
                byte b2 = DataUtil.calculateBCC8(Xha.f0a, Xha.b - 1);
                if (Xha.f0a[Xha.b - 1] == b2) {
                    Xha.state = 2;
                } else {
                    Xha.state = 0;
                    Xha.c = 0;
                    Xha.retry = 0;
                    Xha.b = 0;
                    Log.e(TAG, "cbus data recv crc8 failed");
                }
            }
            if (Xha.state == 2) {
                deviceLineTimerCount = 10;
                byte[] bArr2 = Xha.f0a;
                int i6 = Xha.b;
                if (i6 >= 5) {
                    int i7 = i6 - 5;
                    byte[] bArr3 = new byte[i7];
                    switch (bArr2[1] & 255) {
                        case 163:
                            Log.v(TAG, "CbusDataOTA  CBUS_CMD_OTA_NOTIFY");
                            if (i6 > 3) {
                                for (int i8 = 0; i8 < i7 / 3; i8++) {
                                    int i9 = i8 * 3;
                                    Xhb.a(bArr2[i9 + 4] & 255, Xhc.getState(bArr2[i9 + 5] & 255, bArr2[i9 + 6] & 255));
                                }
                                break;
                            } else {
                                Log.e(TAG, "CbusDataOTA  CBUS_CMD_OTA_NOTIFY data len error:" + i6);
                                break;
                            }
                        case XcBUS.CBUS_CMD_HANDSHAKE_RESP /* 239 */:
                            Xhc.setState(0);
                            Log.v(TAG, "CbusData CBUS_CMD_HANDSHAKE_RESP1");
                            Xhc.f2a = Boolean.FALSE;
                            Tools.arraycopy(bArr2, 4, bArr3, 0, i7);
                            String str = new String(bArr3);
                            Log.d(TAG, "CbusData handshake resp info1:  " + str);
                            String[] strArrSplit = str.split("#");
                            for (i2 = 0; i2 < strArrSplit.length; i2++) {
                                String[] strArrSplit2 = strArrSplit[i2].split(XcBUS.HANDSHAKE_INFO_MASK);
                                if (strArrSplit2.length != 2) {
                                    Log.e(TAG, "CbusData handshake info error:" + strArrSplit[i2]);
                                } else if (strArrSplit2[0].equals(XcBUS.HANDSHAKE_HANDSHAKE_VERSION_MASK)) {
                                    Log.d(TAG, "CbusData 0:" + strArrSplit2[1]);
                                } else if (strArrSplit2[0].equals(XcBUS.HANDSHAKE_PROTOCOL_VERSION_MASK)) {
                                    Log.d(TAG, "CbusData 1:" + strArrSplit2[1]);
                                } else if (strArrSplit2[0].equals(XcBUS.HANDSHAKE_DEVICE_SN_MASK)) {
                                    DevStateValue.deviceSN = strArrSplit2[1];
                                    Log.d(TAG, "CbusData 2:" + DevStateValue.deviceSN);
                                } else if (strArrSplit2[0].equals(XcBUS.HANDSHAKE_DEVICE_TYPE_MASK)) {
                                    String str2 = strArrSplit2[1];
                                    DevStateValue.deviceType = str2;
                                    if (str2.equals(Config.PRODUCT_TYPE_LK) || DevStateValue.deviceType.equals(Config.PRODUCT_TYPE_LK_A1) || DevStateValue.deviceType.equals("ling-kong 3.0")) {
                                        DevStateValue.deviceTypeNo = 1;
                                    } else if (DevStateValue.deviceType.equals(Config.PRODUCT_TYPE_LK_B1)) {
                                        DevStateValue.deviceTypeNo = 5;
                                    } else if (DevStateValue.deviceType.equals(Config.PRODUCT_TYPE_LJ_A) || DevStateValue.deviceType.equals(Config.PRODUCT_TYPE_LJ_A1) || DevStateValue.deviceType.equals(Config.PRODUCT_TYPE_LJ_A2)) {
                                        DevStateValue.deviceTypeNo = 2;
                                    } else if (DevStateValue.deviceType.equals(Config.PRODUCT_TYPE_LJ_B) || DevStateValue.deviceType.equals(Config.PRODUCT_TYPE_LJ_B1) || DevStateValue.deviceType.equals(Config.PRODUCT_TYPE_LJ_B2)) {
                                        DevStateValue.deviceTypeNo = 3;
                                    } else if (DevStateValue.deviceType.equals(Config.PRODUCT_TYPE_LG_A) || DevStateValue.deviceType.equals(Config.PRODUCT_TYPE_LG_A1) || DevStateValue.deviceType.equals(Config.PRODUCT_TYPE_LG_A2) || DevStateValue.deviceType.equals(Config.PRODUCT_TYPE_LG_A3)) {
                                        DevStateValue.deviceTypeNo = 4;
                                        if (DevStateValue.deviceType.equals(Config.PRODUCT_TYPE_LG_A1)) {
                                            Config.touch_switch_enable = true;
                                        }
                                    } else {
                                        DevStateValue.deviceTypeNo = 0;
                                    }
                                    Log.d(TAG, "CbusData 3:" + DevStateValue.deviceType);
                                } else if (strArrSplit2[0].equals(XcBUS.HANDSHAKE_DEVICE_MCU_MASK)) {
                                    DevStateValue.deviceMCU = strArrSplit2[1];
                                    Log.d(TAG, "CbusData 4:" + DevStateValue.deviceMCU);
                                } else if (strArrSplit2[0].equals(XcBUS.HANDSHAKE_SOFT_VERSION_MASK)) {
                                    DevStateValue.deviceSoftVersion = strArrSplit2[1];
                                    Log.d(TAG, "CbusData 5:" + DevStateValue.deviceSoftVersion);
                                } else if (strArrSplit2[0].equals(XcBUS.HANDSHAKE_AFS_INFO_MASK)) {
                                    DevStateValue.deviceAFSInfo = strArrSplit2[1];
                                    Log.d(TAG, "CbusData 6:" + DevStateValue.deviceAFSInfo);
                                    try {
                                        int i10 = Integer.parseInt(DevStateValue.deviceAFSInfo);
                                        if (i10 % 10 == 1) {
                                            DevStateValue.deviceAFS_camera = 1;
                                        } else {
                                            DevStateValue.deviceAFS_camera = 0;
                                        }
                                        if ((i10 / 10) % 10 == 1) {
                                            DevStateValue.deviceAFS_distanceLR = 1;
                                        } else {
                                            DevStateValue.deviceAFS_distanceLR = 0;
                                        }
                                        if ((i10 / 100) % 10 == 1) {
                                            DevStateValue.deviceAFS_distanceUD = 1;
                                        } else {
                                            DevStateValue.deviceAFS_distanceUD = 0;
                                        }
                                        if ((i10 / 100000) % 10 == 1) {
                                            DevStateValue.deviceAFS_autoMap = 1;
                                        } else {
                                            DevStateValue.deviceAFS_autoMap = 0;
                                        }
                                    } catch (Exception e) {
                                        Log.e(TAG, "CbusData AFS info error");
                                    }
                                } else if (strArrSplit2[0].equals(XcBUS.HANDSHAKE_KEY_INFO_MASK)) {
                                    DevStateValue.deviceKeyInfo = strArrSplit2[1];
                                    Log.d(TAG, "CbusData 8:" + DevStateValue.deviceKeyInfo);
                                    byte[] bArr4 = new byte[12];
                                    byte[] bArr5 = new byte[5];
                                    byte[] bArrHexStringToByteArray = Tools.hexStringToByteArray(DevStateValue.deviceKeyInfo);
                                    if (bArrHexStringToByteArray.length == 20) {
                                        int i11 = 0;
                                        int i12 = 0;
                                        byte[] bArr6 = new byte[15];
                                        byte[] bArr7 = new byte[15];
                                        byte[] bArr8 = new byte[5];
                                        int i13 = ((bArrHexStringToByteArray[0] & 255) << 8) | (bArrHexStringToByteArray[1] & 255);
                                        int i14 = 0;
                                        while (true) {
                                            int i15 = i14;
                                            if (i15 < 15) {
                                                if (((1 << (15 - i15)) & i13) != 0) {
                                                    bArr6[i11] = (byte) (i15 + 2);
                                                    i11++;
                                                } else {
                                                    bArr7[i12] = (byte) (i15 + 2);
                                                    i12++;
                                                }
                                                i14 = i15 + 1;
                                            } else {
                                                if (i11 >= 5) {
                                                    Tools.arraycopy(bArr6, 0, bArr8, 0, 5);
                                                } else {
                                                    Tools.arraycopy(bArr7, 0, bArr8, 0, 5);
                                                }
                                                for (int i16 = 0; i16 < 5; i16++) {
                                                    bArr5[i16] = bArrHexStringToByteArray[bArr8[i16]];
                                                }
                                                for (int i17 = 0; i17 < 5; i17++) {
                                                    Tools.arraycopy(bArrHexStringToByteArray, (bArr8[i17] + 1) - i17, bArrHexStringToByteArray, bArr8[i17] - i17, (16 - i17) - (bArr8[i17] - i17));
                                                }
                                                bArr5[0] = (byte) (bArr5[0] ^ bArrHexStringToByteArray[17]);
                                                bArr5[1] = (byte) (bArr5[1] ^ bArrHexStringToByteArray[18]);
                                                bArr5[2] = (byte) (bArr5[2] ^ bArrHexStringToByteArray[19]);
                                                bArr5[3] = (byte) (bArr5[3] ^ bArrHexStringToByteArray[0]);
                                                bArr5[4] = (byte) (bArr5[4] ^ bArrHexStringToByteArray[1]);
                                                Tools.arraycopy(bArr5, 0, bArrHexStringToByteArray, 12, 5);
                                                Tools.arraycopy(bArrHexStringToByteArray, 0, bArr4, 0, 12);
                                            }
                                        }
                                    } else {
                                        Log.e(TAG, "CbusData secretkey length:" + bArrHexStringToByteArray.length);
                                    }
                                    Log.d(TAG, "secretkey  key:" + Tools.bytesToAscii(bArr5));
                                    Log.d(TAG, "secretkey   uid:" + Tools.bytes2HexString(bArr4));
                                    if (Tools.bytesToAscii(bArr5).equals(Config.PRODUCT_SECRETKEY) && Tools.bytes2HexString(bArr4).equals(DevStateValue.deviceSN)) {
                                        DevStateValue.deviceHandshakeSuccessState = true;
                                        DevStateValue.deviceLineState = 1;
                                    } else {
                                        Log.e(TAG, "secret key  secret key error");
                                    }
                                } else if (strArrSplit2[0].equals(XcBUS.HANDSHAKE_SUB_VERSION_MASK)) {
                                    DevStateValue.deviceSubSoftVersion = strArrSplit2[1];
                                    Log.e(TAG, "CbusData 11:" + DevStateValue.deviceSubSoftVersion);
                                } else if (strArrSplit2[0].equals(XcBUS.HANDSHAKE_TYPE_OTA_URL)) {
                                    Log.d(TAG, "CbusData 13:" + strArrSplit2[1]);
                                } else if (strArrSplit2[0].equals(XcBUS.HANDSHAKE_TYPE_OTA_MD5)) {
                                    Log.d(TAG, "CbusData 14:" + strArrSplit2[1]);
                                }
                            }
                            break;
                        case XcBUS.CBUS_CMD_READ_RESP /* 241 */:
                            if (i6 > 3) {
                                for (int i18 = 0; i18 < i7 / 3; i18++) {
                                    int i19 = i18 * 3;
                                    int i20 = bArr2[i19 + 4] & 255;
                                    int iA = Xhc.getState(bArr2[i19 + 5] & 255, bArr2[i19 + 6] & 255);
                                    if (i20 == 57) {
                                        DevStateValue.scraper1State = Xhc.getState(iA);
                                        DevStateValue.scraper1Value = iA & 255;
                                    } else if (i20 == 58) {
                                        DevStateValue.scraper2State = Xhc.getState(iA);
                                        DevStateValue.scraper2Value = iA & 255;
                                    } else if (i20 == 74) {
                                        DevStateValue.run_auto_recycleWater = Xhc.getState(iA);
                                    } else if (i20 == 75) {
                                        DevStateValue.run_waterHeatState = Xhc.getState(iA);
                                        DevStateValue.run_waterHeatValue = iA & 255;
                                    } else if (i20 != 102) {
                                        if (i20 != 103) {
                                            if (i20 == 106) {
                                                DevStateValue.scraper_center_time_u = iA;
                                            } else if (i20 == 107) {
                                                DevStateValue.scraper_center_time_d = iA;
                                            } else if (i20 == 186) {
                                                Xhc.setState(1);
                                                Log.e(TAG, "visual ota  device visual ota cmd resp");
                                            } else if (i20 == 187) {
                                                DevStateValue.ota_visual_state = Xhc.getState(iA);
                                                DevStateValue.ota_visual_value = iA & 255;
                                                Xhc.setState(2);
                                            } else if (i20 == 78) {
                                                DevStateValue.run_LED = Xhc.getState(iA);
                                            } else if (i20 != 100) {
                                                if (i20 == 111) {
                                                    DevStateValue.repair = Xhc.getState(iA);
                                                } else if (i20 != 113) {
                                                    switch (i20) {
                                                        case 32:
                                                            DevStateValue.deviceRSSI = iA & 255;
                                                            break;
                                                        case XcBUS.ATTR_STATE_UPDOWN_LOAD /* 33 */:
                                                            DevStateValue.udMoterLoad = iA;
                                                            break;
                                                        case XcBUS.ATTR_STATE_WATER /* 34 */:
                                                            DevStateValue.water_state = Xhc.getState(iA);
                                                            DevStateValue.water_value = iA & 255;
                                                            break;
                                                        case XcBUS.ATTR_STATE_BATTERY /* 35 */:
                                                            DevStateValue.battery = iA;
                                                            break;
                                                        case XcBUS.ATTR_STATE_DIR_U /* 36 */:
                                                            DevStateValue.dirUState = Xhc.getState(iA);
                                                            DevStateValue.dirUValue = iA & 255;
                                                            break;
                                                        case XcBUS.ATTR_STATE_DIR_D /* 37 */:
                                                            DevStateValue.dirDState = Xhc.getState(iA);
                                                            DevStateValue.dirDValue = iA & 255;
                                                            break;
                                                        case XcBUS.ATTR_STATE_DISTANCE_U /* 38 */:
                                                            DevStateValue.distanceU = iA;
                                                            break;
                                                        case XcBUS.ATTR_STATE_DISTANCE_D /* 39 */:
                                                            DevStateValue.distanceD = iA;
                                                            break;
                                                        case 40:
                                                            DevStateValue.distanceL = iA;
                                                            break;
                                                        case XcBUS.ATTR_STATE_DISTANCE_R /* 41 */:
                                                            DevStateValue.distanceR = iA;
                                                            break;
                                                        case XcBUS.ATTR_STATE_NTC /* 42 */:
                                                            DevStateValue.ntc_temp_state = Xhc.getState(iA);
                                                            DevStateValue.ntc_temp_value = iA & 255;
                                                            break;
                                                        case XcBUS.ATTR_STATE_TOUCH /* 43 */:
                                                            DevStateValue.touch_state = Xhc.getState(iA);
                                                            DevStateValue.touch_value = iA & 255;
                                                            break;
                                                        case XcBUS.ATTR_STATE_TIME_ONCE /* 44 */:
                                                            DevStateValue.workTimeOnce = iA;
                                                            break;
                                                        case XcBUS.ATTR_STATE_TIME_SUM /* 45 */:
                                                            DevStateValue.workTimeSum = iA;
                                                            break;
                                                        case XcBUS.ATTR_STATE_VISUAL_RUN /* 46 */:
                                                            DevStateValue.run_visualState = Xhc.getState(iA);
                                                            DevStateValue.run_visualValue = iA & 255;
                                                            break;
                                                        default:
                                                            switch (i20) {
                                                                case XcBUS.ATTR_AUTOMAP_MAP_TYPE /* 207 */:
                                                                    DevStateValue.auto_view_map_id = iA;
                                                                    break;
                                                                case XcBUS.ATTR_AUTOMAP_INFO_W /* 208 */:
                                                                    DevStateValue.auto_view_info_w = iA;
                                                                    break;
                                                                case XcBUS.ATTR_AUTOMAP_INFO_H /* 209 */:
                                                                    DevStateValue.auto_view_info_h = iA;
                                                                    break;
                                                                case XcBUS.ATTR_AUTOMAP_PLAN_L /* 210 */:
                                                                    DevStateValue.auto_view_planned_delta = iA;
                                                                    break;
                                                                case XcBUS.ATTR_AUTOMAP_PLAN_LINE /* 211 */:
                                                                    DevStateValue.auto_view_planned_line = iA;
                                                                    break;
                                                                case XcBUS.ATTR_AUTOMAP_PLAN_TIME /* 212 */:
                                                                    DevStateValue.auto_view_planned_time = iA;
                                                                    break;
                                                                case XcBUS.ATTR_AUTOMAP_CLEAN_STATE /* 213 */:
                                                                    DevStateValue.auto_view_cleaned_state = iA;
                                                                    break;
                                                                case XcBUS.ATTR_AUTOMAP_CLEAN_LINE /* 214 */:
                                                                    DevStateValue.auto_view_cleaned_line = iA;
                                                                    break;
                                                                case XcBUS.ATTR_AUTOMAP_CLEAN_PERSONT /* 215 */:
                                                                    DevStateValue.auto_view_cleaned_persont = iA;
                                                                    break;
                                                                case XcBUS.ATTR_AUTOMAP_CLEAN_PRELINE /* 216 */:
                                                                    DevStateValue.auto_view_cleaned_prelength = iA;
                                                                    break;
                                                                case XcBUS.ATTR_AUTOMAP_CLEAN_TIME /* 217 */:
                                                                    DevStateValue.auto_view_cleaned_time = iA;
                                                                    break;
                                                                case XcBUS.ATTR_AUTOMAP_CLEAN_AREA /* 218 */:
                                                                    DevStateValue.auto_view_cleaned_area = iA;
                                                                    break;
                                                                default:
                                                                    switch (i20) {
                                                                        case XcBUS.ATTR_STATE_SCRAPER3 /* 60 */:
                                                                            DevStateValue.scraper3State = Xhc.getState(iA);
                                                                            DevStateValue.scraper3Value = iA & 255;
                                                                            continue;
                                                                        case XcBUS.ATTR_STATE_SCRAPER4 /* 61 */:
                                                                            DevStateValue.scraper4State = Xhc.getState(iA);
                                                                            DevStateValue.scraper4Value = iA & 255;
                                                                            continue;
                                                                        case XcBUS.ATTR_STATE_FEEDBACK /* 62 */:
                                                                            DevStateValue.feedbackValue = iA;
                                                                            DevStateValue.subDeviceLineState = (iA >> 2) & 3;
                                                                            continue;
                                                                        case XcBUS.ATTR_STATE_DISTANCE /* 63 */:
                                                                            DevStateValue.deviceDistance = iA;
                                                                            break;
                                                                        case 64:
                                                                            break;
                                                                        default:
                                                                            switch (i20) {
                                                                                case XcBUS.ATTR_CONTROL_BOOST_VALUE /* 66 */:
                                                                                    DevStateValue.run_propulsionLeft = Xhc.getState(iA);
                                                                                    DevStateValue.run_propulsionRight = iA & 255;
                                                                                    break;
                                                                                case XcBUS.ATTR_CONTROL_ADHESION /* 67 */:
                                                                                    DevStateValue.run_Adhesion = Xhc.getState(iA);
                                                                                    break;
                                                                                case XcBUS.ATTR_CONTROL_UPDOWN /* 68 */:
                                                                                    DevStateValue.run_updownState = Xhc.getState(iA);
                                                                                    DevStateValue.run_updownValue = iA & 255;
                                                                                    break;
                                                                                case XcBUS.ATTR_CONTROL_DIR /* 69 */:
                                                                                    DevStateValue.run_dirState = Xhc.getState(iA);
                                                                                    DevStateValue.run_DirValue = iA & 255;
                                                                                    break;
                                                                                case XcBUS.ATTR_CONTROL_CLEAN /* 70 */:
                                                                                    DevStateValue.run_Clean = Xhc.getState(iA);
                                                                                    DevStateValue.run_OtherSide = Xhc.getState(iA);
                                                                                    break;
                                                                                case XcBUS.ATTR_CONTROL_WATER /* 71 */:
                                                                                    DevStateValue.run_WiterSpray = Xhc.getState(iA);
                                                                                    DevStateValue.run_Steam = Xhc.getState(iA);
                                                                                    break;
                                                                                case XcBUS.ATTR_CONTROL_AUTO_TYPE /* 72 */:
                                                                                    DevStateValue.run_auto_Type = iA & 255;
                                                                                    break;
                                                                                default:
                                                                                    switch (i20) {
                                                                                        case 96:
                                                                                            DevStateValue.deviceNum = iA;
                                                                                            break;
                                                                                        case XcBUS.ATTR_CONFIG_UD_RESTART /* 97 */:
                                                                                            break;
                                                                                        case XcBUS.ATTR_CONFIG_OBSTACLE /* 98 */:
                                                                                            DevStateValue.runlength = iA;
                                                                                            break;
                                                                                        default:
                                                                                            switch (i20) {
                                                                                                case 128:
                                                                                                    DevStateValue.speed_up = iA;
                                                                                                    break;
                                                                                                case XcBUS.ATTR_SPEED_DOWN /* 129 */:
                                                                                                    DevStateValue.speed_down = iA;
                                                                                                    break;
                                                                                                case XcBUS.ATTR_SPEED_ADHESION /* 130 */:
                                                                                                    DevStateValue.speed_adhesion = iA;
                                                                                                    break;
                                                                                                case XcBUS.ATTR_SPEED_STEAM /* 131 */:
                                                                                                    DevStateValue.speed_steam = iA;
                                                                                                    break;
                                                                                                case XcBUS.ATTR_SPEED_CLEAN /* 132 */:
                                                                                                    DevStateValue.speed_clean = iA;
                                                                                                    break;
                                                                                                case XcBUS.ATTR_SPEED_U_SPRAY1 /* 133 */:
                                                                                                    DevStateValue.speed_waterU1 = iA;
                                                                                                    break;
                                                                                                case XcBUS.ATTR_SPEED_U_SPRAY2 /* 134 */:
                                                                                                    DevStateValue.speed_waterU2 = iA;
                                                                                                    break;
                                                                                                case XcBUS.ATTR_SPEED_D_SPRAY1 /* 135 */:
                                                                                                    DevStateValue.speed_waterD1 = iA;
                                                                                                    break;
                                                                                                case XcBUS.ATTR_SPEED_D_SPRAY2 /* 136 */:
                                                                                                    DevStateValue.speed_waterD2 = iA;
                                                                                                    break;
                                                                                                default:
                                                                                                    Log.e(TAG, "CbusData CbusAttrValue2UIValue not supported attr id:" + i20);
                                                                                                    continue;
                                                                                            }
                                                                                    }
                                                                            }
                                                                    }
                                                                    DevStateValue.run_mode = Xhc.getState(iA);
                                                                    break;
                                                            }
                                                    }
                                                } else {
                                                    DevStateValue.rebootNum = iA;
                                                }
                                            } else if (iA == 0) {
                                                DevStateValue.hand_touch_disable = 0;
                                            } else {
                                                DevStateValue.hand_touch_disable = 1;
                                            }
                                        } else if (Xhc.getState(iA) < 50) {
                                            DevStateValue.visual_config_arry[Xhc.getState(iA)] = iA & 255;
                                        } else {
                                            Log.e(TAG, "ATTR_CONFIG_VISUAL  visual_arry[" + Xhc.getState(iA) + "]::" + (iA & 255));
                                        }
                                    } else if (iA == 0) {
                                        DevStateValue.adhesionControlled = 0;
                                    } else {
                                        DevStateValue.adhesionControlled = 1;
                                    }
                                }
                                if ((bArr2[2] & 255) == 2) {
                                    DevStateValue.waitCommunicationState = Config.WAIT_COM_STATE_SUCCESS_GetSpeed;
                                } else if ((bArr2[2] & 255) == 4) {
                                    DevStateValue.waitCommunicationState = Config.WAIT_COM_STATE_SUCCESS_GetConfig;
                                } else if ((bArr2[2] & 255) == 6) {
                                    DevStateValue.waitCommunicationState = Config.WAIT_COM_STATE_SUCCESS_GetAutodata;
                                }
                                if (!Xhc.f2a.booleanValue()) {
                                    VoicePlayHelper.startPlay(VoicePlayHelper.getResStr(R.string.standby_info_no_str) + DevStateValue.deviceNum);
                                    Xhc.f2a = Boolean.TRUE;
                                }
                                DevLogProtocol.update_device_log();
                                break;
                            } else {
                                Log.e(TAG, "CbusData CBUS_CMD_READ_RESP data len error:" + i6);
                                break;
                            }
                        case XcBUS.CBUS_CMD_SET_RESP /* 242 */:
                            Log.v(TAG, "CbusDataProtocol_msg  CBUS_CMD_SET_RESP");
                            for (int i21 = 0; i21 < i7 / 3; i21++) {
                                int i22 = i21 * 3;
                                int i23 = bArr2[i22 + 4] & 255;
                                byte b = bArr2[i22 + 5];
                                byte b2 = bArr2[i22 + 6];
                                if (i23 == 186) {
                                    if (DevStateValue.ota_state >= 1) {
                                        Xhc.setState(1);
                                    }
                                    DevStateValue.waitCommunicationState = Config.WAIT_COM_STATE_SUCCESS_SetVisualOTA;
                                }
                            }
                            if ((bArr2[2] & 255) == 3) {
                                DevStateValue.waitCommunicationState = Config.WAIT_COM_STATE_SUCCESS_SetSpeed;
                                break;
                            } else if ((bArr2[2] & 255) == 5) {
                                DevStateValue.waitCommunicationState = Config.WAIT_COM_STATE_SUCCESS_SetConfig;
                                break;
                            } else if ((bArr2[2] & 255) == 7) {
                                DevStateValue.waitCommunicationState = Config.WAIT_COM_STATE_SUCCESS_SetAutodata;
                                break;
                            }
                            break;
                        case XcBUS.CBUS_CMD_OTA_RESP /* 243 */:
                            Log.v(TAG, "CbusDataOTA  CBUS ota data recv");
                            if (i6 > 3) {
                                for (int i24 = 0; i24 < i7 / 3; i24++) {
                                    int i25 = i24 * 3;
                                    Xhb.a(bArr2[i25 + 4] & 255, Xhc.getState(bArr2[i25 + 5] & 255, bArr2[i25 + 6] & 255));
                                }
                                break;
                            } else {
                                Log.e(TAG, "CbusDataOTA  CBUS_CMD_OTA_RESP data len error:" + i6);
                                break;
                            }
                        case XcBUS.CBUS_CMD_ERRCODE_RESP /* 245 */:
                            if (i6 >= 8) {
                                int i26 = i7 / 3;
                                int[][] iArr = (int[][]) Array.newInstance((Class<?>) Integer.TYPE, i26, 2);
                                for (int i27 = 0; i27 < i26; i27++) {
                                    int i28 = i27 * 3;
                                    iArr[i27][0] = Xhc.getState(bArr2[i28 + 4] & 255, bArr2[i28 + 5] & 255);
                                    iArr[i27][1] = bArr2[i28 + 6] & 255;
                                }
                                DevStateValue.err_code_arry = iArr;
                            } else {
                                DevStateValue.err_code_arry = null;
                            }
                            DevLogProtocol.device_error_log();
                            break;
                        case XcBUS.CBUS_CMD_RUNLOG_RESP /* 246 */:
                            DevStateValue.waitCommunicationState = Config.WAIT_COM_STATE_SUCCESS_GetRunLog;
                            RunLogProtocol.reset_run_log();
                            Log.e(TAG, "CBUS_CMD_RUNLOG_RESP  data len:" + i6 + "data:" + Tools.bytes2HexString(bArr2, 0, i6));
                            for (int i29 = 0; i29 < i7 / 8; i29++) {
                                int i30 = i29 * 8;
                                RunLogProtocol.add_run_log(bArr2[i30 + 4] & 255, bArr2[i30 + 5] & 255, bArr2[i30 + 6] & 255, ((bArr2[i30 + 7] & 255) << 24) | ((bArr2[i30 + 8] & 255) << 16) | ((bArr2[i30 + 9] & 255) << 8) | (bArr2[i30 + 10] & 255));
                            }
                            break;
//                        case 255:
//                            Log.v(TAG, "CbusData CBUS_CMD_HANDSHAKE_RESP2");
//                            Xhc.f2a = Boolean.FALSE;
//                            Tools.arraycopy(bArr2, 4, bArr3, 0, i7);
//                            String str3 = new String(bArr3);
//                            Log.d(TAG, "CbusData handshake resp info2:  " + str3);
//                            String[] strArrSplit3 = str3.split("#");
//                            int length = strArrSplit3.length;
//                            while (i2 < length) {
//
//                            }
//                            break;
                        default:
                            Log.e(TAG, "CbusData not supprot cmd resp:" + (bArr2[1] & 255));
                            break;
                    }
                } else {
                    Log.e(TAG, "CbusData data len error:" + i6);
                }
                receiverListener.updateRemoteUi();
                receiverListener.handelUpdateCbus();
            }
        }

        public void connect() {
            Log.v("lisha", "connect");
        }
    }
}

