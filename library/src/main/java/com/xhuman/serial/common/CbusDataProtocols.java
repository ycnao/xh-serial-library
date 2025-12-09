package com.xhuman.serial.common;

import android.util.Log;

import com.xhuman.serial.common.cbus.DevStateValue;
import com.xhuman.serial.util.Tools;
import com.xhuman.serial.common.xh.Xhc;
import com.skydroid.fpvlibrary.serial.SerialPortConnection;

import java.util.Random;

public class CbusDataProtocols {

    // 日志标签常量
    private static final boolean IS_LOG = false;
    private static final String TAG_CONTROL = "Cbus_controlAndroidId";
    private static final String TAG_SECRET = "Cbus_secretkey";
    private static final String TAG_HANDSHAKE = "Cbus_DataSendHandshake";
    private static final String TAG_PROTOCOL = "Cbus_DataProtocol";

    /**
     * 握手数据发送
     */
    public static void sendDataHandshake(SerialPortConnection connection, String param) {
        if (connection == null) {
            Log.e(TAG_HANDSHAKE, "串口连接不能为空");
            return;
        }

        // 1. 初始化基础数据
        if (IS_LOG) {
            Log.d(TAG_CONTROL, ":" + DevStateValue.controlAndroidId);
        }
        byte[] androidIdBytes = Tools.hexStringToByteArray(DevStateValue.controlAndroidId);
        byte[] lingdBytes = "LINGD".getBytes();

        // 2. 初始化密钥数组（20字节）
        byte[] secretKey = new byte[20];
        // 填充前12字节（设备ID）
        int copyLen = Math.min(androidIdBytes.length, 12);
        Tools.arraycopy(androidIdBytes, 0, secretKey, 0, copyLen);
        // 填充12-16字节（"LINGD"）
        Tools.arraycopy(lingdBytes, 0, secretKey, 12, 5);

        // 3. 生成随机数并填充17-19字节
        Random random = new Random();
        int randomNum = random.nextInt(10000000);
        secretKey[17] = (byte) (randomNum >> 16);    // 高8位
        secretKey[18] = (byte) (randomNum >> 8);     // 中8位
        secretKey[19] = (byte) (randomNum & 0xFF);   // 低8位

        // 4. 临时数组异或操作（原代码中重复创建的20字节数组）
        byte[] tempXor = new byte[20];
        tempXor[12] ^= secretKey[17];
        tempXor[13] ^= secretKey[18];
        tempXor[14] ^= secretKey[19];
        tempXor[15] ^= secretKey[0];
        tempXor[16] ^= secretKey[1];

        // 5. 拆分16位整数并分类索引
        int n = ((secretKey[0] & 0xFF) << 8) | (secretKey[1] & 0xFF);
        byte[] oneIndices = new byte[15];  // 存储位为1的索引
        byte[] zeroIndices = new byte[15]; // 存储位为0的索引
        int oneCount = 0, zeroCount = 0;

        for (byte b = 0; b < 15; b++) {
            if ((n & (1 << (15 - b))) != 0) {
                if (oneCount < 15) oneIndices[oneCount++] = (byte) (b + 2);
            } else {
                if (zeroCount < 15) zeroIndices[zeroCount++] = (byte) (b + 2);
            }
        }

        // 6. 选择5个索引（优先选位为1的）
        byte[] selectedIndices = new byte[5];
        if (oneCount >= 5) {
            Tools.arraycopy(oneIndices, 0, selectedIndices, 0, 5);
        } else {
            Tools.arraycopy(zeroIndices, 0, selectedIndices, 0, 5);
        }

        // 7. 提取关键片段并处理密钥数组
        byte[] temp3Bytes = new byte[3];
        byte[] temp5Bytes = new byte[5];
        Tools.arraycopy(secretKey, 17, temp3Bytes, 0, 3); // 备份17-19字节
        Tools.arraycopy(secretKey, 12, temp5Bytes, 0, 5); // 提取12-16字节

        // 循环修改密钥数组（移位+替换）
        byte[] tempKey = new byte[20];
        for (int k = 0; k < 5; k++) {
            int index = selectedIndices[k] & 0xFF; // 索引转为无符号整数
            byte replaceByte = temp5Bytes[k];

            // 复制原密钥到临时数组
            Tools.arraycopy(secretKey, 0, tempKey, 0, 20);
            // 计算移位位置（避免负数）
            int shiftPos = Math.max(20 - index - 1, 0);
            // 数据后移
            if (shiftPos + 1 < 20) {
                Tools.arraycopy(secretKey, shiftPos, tempKey, shiftPos + 1, index);
            }
            // 插入替换字节
            tempKey[shiftPos] = replaceByte;
            // 复制回原数组
            Tools.arraycopy(tempKey, 0, secretKey, 0, 20);
        }

        // 恢复17-19字节
        Tools.arraycopy(temp3Bytes, 0, secretKey, 17, 3);

        // 8. 构建请求字符串
        long timestamp = System.currentTimeMillis() / 1000L;
        String str5 = "0:2.0#";
        String str6 = "1:2.0#";
        String str7 = "2:" + DevStateValue.controlAndroidId + "#";
        String str8 = "3:H12#";
        String str9 = "4:#";
        String paramStr = "5:" + (param == null ? "" : param) + "#";
        String str10 = "6:#";
        String str2 = "7:" + timestamp + "#";
        String str3 = "8:" + Tools.bytes2HexString(secretKey) + "#";
        String str4 = DevStateValue.ota_flag ? "11:" + DevStateValue.deviceSubSoftVersion + "#" : "11:#";
        if (DevStateValue.ota_flag) DevStateValue.ota_flag = false; // 重置标志

        String reqStr = str5 + str6 + str7 + str8 + str9 + paramStr + str10 + str2 + str3 + str4;
        if (IS_LOG) {
            Log.d(TAG_SECRET, ":" + Tools.bytes2HexString(secretKey));
            Log.e(TAG_HANDSHAKE, "req:" + reqStr);
        }

        // 9. 构建发送数据（帧结构：帧头+长度+数据+BCC）
        byte[] reqBytes = reqStr.getBytes();
        int dataLen = reqBytes.length + 5; // 总长度=数据长+5（帧头3+长度1+BCC1）
        byte[] sendData = new byte[dataLen];

        // 帧头（0xAA, 0x0F, 0xFF）
        sendData[0] = -86;   // 0xAA
        sendData[1] = 15;    // 0x0F
        sendData[2] = -1;    // 0xFF
        // 长度字段
        sendData[3] = (byte) dataLen;
        // 数据字段
        Tools.arraycopy(reqBytes, 0, sendData, 4, reqBytes.length);
        // BCC校验
        sendData[dataLen - 1] = Tools.BCC8(sendData, dataLen - 1);

        // 发送数据
        sendData(connection, sendData, "Cbus_DataSendHandshake");
    }


    /**
     * 设备信息获取
     */
    public static void sendDataInfoGet(SerialPortConnection connection) {
        if (connection == null) {
            Log.e(TAG_PROTOCOL, "串口连接不能为空");
            return;
        }

        int baseCount;          // 基础参数数量
        int extraCount;         // 附加参数数量
        int afsCount;           // AFS参数数量
        int[] baseParams;       // 基础参数ID数组
        int[] extraParams;      // 附加参数ID数组
        int[] afsParams;        // AFS参数ID数组
        byte[] sendData;        // 发送数据缓冲区

        // 根据设备类型初始化参数
        switch (DevStateValue.deviceTypeNo) {
            case 5:
                baseCount = 27;
                baseParams = new int[]{33, 34, 35, 32, 62, 45, 44, 42, 38, 39, 46, 36, 37, 57, 58, 60, 61, 64, 72, 68, 69, 70, 67, 66, 71, 74, 75};
                extraCount = 1;
                extraParams = new int[]{96};
                afsCount = 0;
                afsParams = new int[0];
                break;

            case 4:
                baseCount = 18;
                baseParams = new int[]{33, 34, 35, 32, 62, 45, 44, 42, 38, 39, 43, 46, 78, 64, 72, 68, 69, 70};
                extraCount = 1;
                extraParams = new int[]{96};
                afsCount = 12;
                afsParams = new int[]{207, 208, 209, 210, 211, 212, 213, 214, 215, 216, 217, 218};
                break;

            case 2:
            case 3:
                baseCount = 19;
                baseParams = new int[]{33, 34, 35, 32, 62, 45, 44, 42, 38, 39, 43, 63, 64, 72, 68, 69, 70, 67, 71};
                extraCount = 1;
                extraParams = new int[]{96};
                afsCount = 12;
                afsParams = new int[]{207, 208, 209, 210, 211, 212, 213, 214, 215, 216, 217, 218};
                break;

            default:
                baseCount = 23;
                baseParams = new int[]{33, 34, 35, 32, 62, 45, 44, 42, 38, 39, 46, 36, 37, 64, 72, 68, 69, 70, 67, 66, 71, 74, 75};
                extraCount = 1;
                extraParams = new int[]{96};
                afsCount = 0;
                afsParams = new int[0];
                break;
        }

        // 计算总参数数量
        int totalParamCount = Xhc.f2a ? baseCount :
                (DevStateValue.deviceAFS_autoMap == 1 ? baseCount + afsCount : baseCount + extraCount);
        int dataLen = totalParamCount * 3 + 5; // 总长度=参数区+5（帧头3+长度1+BCC1）
        sendData = new byte[dataLen];

        // 初始化帧头
        sendData[0] = -86;  // 0xAA
        sendData[1] = 1;    // 功能码：获取
        sendData[2] = 1;    // 子功能码
        sendData[3] = (byte) dataLen; // 长度字段

        // 填充基础参数
        for (int i = 0; i < baseCount; i++) {
            int pos = i * 3 + 4;
            sendData[pos] = (byte) baseParams[i];
        }

        // 填充附加参数
        if (!Xhc.f2a.booleanValue()) {
            for (int i = 0; i < extraCount; i++) {
                int pos = (baseCount + i) * 3 + 4;
                sendData[pos] = (byte) extraParams[i];
            }
        }

        // 填充AFS参数（如果开启）
        if (DevStateValue.deviceAFS_autoMap == 1) {
            int startIdx = Xhc.f2a ? baseCount : baseCount + extraCount;
            for (int i = 0; i < afsCount; i++) {
                int pos = (startIdx + i) * 3 + 4;
                sendData[pos] = (byte) afsParams[i];
            }
        }

        // 计算BCC校验
        int checkPos = totalParamCount * 3 + 4;
        sendData[checkPos] = Tools.BCC8(sendData, checkPos);

        // 发送数据
        sendData(connection, sendData, "getDataSendInfo");
    }

    /**
     * 速度参数获取
     */
    public static void sendDataSpeedGet(SerialPortConnection connection) {
        if (connection == null) return;

        byte[] data = new byte[32];
        // 帧头
        data[0] = -86;  // 0xAA
        data[1] = 1;    // 功能码：获取
        data[2] = 2;    // 子功能码：速度
        // 填充固定参数（原代码逻辑）
        byte[] fixed = new byte[]{-86, 1, 2, Byte.MIN_VALUE, -127, -126, -125, -124, -123, -122, -121, -120, 32};
        System.arraycopy(fixed, 0, data, 0, fixed.length);
        // BCC校验
        data[31] = Tools.BCC8(fixed, 31);

        sendData(connection, data, "CbusDataSendSpeedReq");
    }

    /**
     * 速度参数设置
     */
    public static void sendDataSpeedSet(SerialPortConnection connection) {
        if (connection == null) return;

        byte[] data = new byte[32];
        // 帧头
        data[0] = -86;  // 0xAA
        data[1] = 2;    // 功能码：设置
        data[2] = 3;    // 子功能码：速度
        // 填充速度参数（高8位+低8位）
        data[4] = (byte) Xhc.getState(DevStateValue.speed_up);
        data[5] = (byte) (DevStateValue.speed_up & 0xFF);
        data[7] = (byte) Xhc.getState(DevStateValue.speed_down);
        data[8] = (byte) (DevStateValue.speed_down & 0xFF);
        data[10] = (byte) Xhc.getState(DevStateValue.speed_adhesion);
        data[11] = (byte) (DevStateValue.speed_adhesion & 0xFF);
        data[13] = (byte) Xhc.getState(DevStateValue.speed_steam);
        data[14] = (byte) (DevStateValue.speed_steam & 0xFF);
        data[16] = (byte) Xhc.getState(DevStateValue.speed_clean);
        data[17] = (byte) (DevStateValue.speed_clean & 0xFF);
        data[19] = (byte) Xhc.getState(DevStateValue.speed_waterU1);
        data[20] = (byte) (DevStateValue.speed_waterU1 & 0xFF);
        data[22] = (byte) Xhc.getState(DevStateValue.speed_waterU2);
        data[23] = (byte) (DevStateValue.speed_waterU2 & 0xFF);
        data[25] = (byte) Xhc.getState(DevStateValue.speed_waterD1);
        data[26] = (byte) (DevStateValue.speed_waterD1 & 0xFF);
        data[28] = (byte) Xhc.getState(DevStateValue.speed_waterD2);
        data[29] = (byte) (DevStateValue.speed_waterD2 & 0xFF);
        // 固定填充
        data[3] = Byte.MIN_VALUE;
        data[6] = -127;
        data[9] = -126;
        data[12] = -125;
        data[15] = -124;
        data[18] = -123;
        data[21] = -122;
        data[24] = -121;
        data[27] = -120;
        data[30] = 32;
        // BCC校验
        data[31] = Tools.BCC8(data, 31);

        sendData(connection, data, "CbusDataSendSpeedSet");
    }

    /**
     * 配置参数获取
     */
    public static void sendDataConfigGet(SerialPortConnection connection) {
        if (connection == null) return;
        // 1. 定义核心数据（命令码0x01，子命令0x04，数据域为配置项标识）
        byte[] coreData = new byte[]{
                (byte) 0xAA,  // 帧头：固定0xAA
                (byte) 0x01,  // 命令码：配置获取
                (byte) 0x04,  // 子命令：获取指定配置
                (byte) 0x0D,  // 数据长度：13字节（后续数据的有效长度）
                (byte) 0x60,  // 配置项1标识
                (byte) 0x62,  // 配置项2标识
                (byte) 0x4B,  // 配置项3标识
                (byte) 0x61,  // 配置项4标识
                (byte) 0x64,  // 配置项5标识
                (byte) 0x66,  // 配置项6标识
                (byte) 0x6A,  // 配置项7标识
                (byte) 0x6B,  // 配置项8标识
                (byte) 0x6F,  // 配置项9标识
                (byte) 0x71,  // 配置项10标识
                (byte) 0x23   // 配置项11标识
        };

        // 2. 计算BCC8校验（从帧头到数据域末尾）
        byte checksum = Tools.BCC8(coreData, coreData.length);

        // 3. 拼接完整帧（核心数据 + 校验位）
        byte[] fullFrame = new byte[coreData.length + 1];
        System.arraycopy(coreData, 0, fullFrame, 0, coreData.length);
        fullFrame[coreData.length] = checksum;
        sendData(connection, fullFrame, "CbusDataSendConfigReq");   // BCC校验
    }

    /**
     * 配置参数设置
     */
    public static void sendDataConfigSet(SerialPortConnection connection) {
        if (connection == null) return;

        byte[] arrayOfByte = new byte[38];
        arrayOfByte[0] = -86;
        arrayOfByte[1] = 2;
        arrayOfByte[2] = 5;
        arrayOfByte[3] = 38;
        arrayOfByte[4] = 96;
        arrayOfByte[5] = (byte) Xhc.getState(DevStateValue.deviceNum);
        arrayOfByte[6] = (byte) (DevStateValue.deviceNum & 0xFF);
        arrayOfByte[7] = 98;
        arrayOfByte[8] = (byte) Xhc.getState(DevStateValue.runlength);
        arrayOfByte[9] = (byte) (DevStateValue.runlength & 0xFF);
        arrayOfByte[10] = 75;
        arrayOfByte[11] = (byte) DevStateValue.set_heat_state;
        arrayOfByte[12] = (byte) DevStateValue.set_heat_value;
        arrayOfByte[13] = 97;
        arrayOfByte[14] = (byte) DevStateValue.upDownReset;
        arrayOfByte[15] = 0;
        arrayOfByte[16] = 100;
        arrayOfByte[17] = (byte) DevStateValue.hand_touch_disable;

        byte b1 = (byte) (DevStateValue.hand_touch_disable_flash ? 255 : 0);
        arrayOfByte[18] = b1;
        arrayOfByte[19] = 102;
        arrayOfByte[20] = (byte) DevStateValue.adhesionControlled;
        arrayOfByte[21] = 0;
        arrayOfByte[22] = 106;
        arrayOfByte[23] = (byte) Xhc.getState(DevStateValue.scraper_center_time_u);
        arrayOfByte[24] = (byte) (DevStateValue.scraper_center_time_u & 0xFF);
        arrayOfByte[25] = 107;
        arrayOfByte[26] = (byte) Xhc.getState(DevStateValue.scraper_center_time_d);
        arrayOfByte[27] = (byte) (DevStateValue.scraper_center_time_d & 0xFF);
        arrayOfByte[28] = 111;
        arrayOfByte[29] = (byte) DevStateValue.repair;
        arrayOfByte[30] = 0;
        arrayOfByte[31] = 113;
        arrayOfByte[32] = (byte) DevStateValue.config_reboot;
        arrayOfByte[33] = 0;
        arrayOfByte[34] = 114;
        arrayOfByte[35] = (byte) DevStateValue.config_reset;
        arrayOfByte[36] = 0;
        arrayOfByte[37] = Tools.BCC8(arrayOfByte, 37);

        sendData(connection, arrayOfByte, "CbusDataSendConfigSet");

    }

    /**
     * 自动数据调试获取
     */
    public static void sendDataAutoDataDebugGet(SerialPortConnection connection) {
        if (connection == null) return;

        byte[] data = new byte[155];
        // 帧头
        data[0] = -86;  // 0xAA
        data[1] = 1;    // 功能码：获取
        data[2] = 6;    // 子功能码：自动数据
        data[3] = -101; // 长度（0x9B的补码）
        // 填充50组参数
        for (int i = 0; i < 50; i++) {
            int pos = (i + 1) * 3;
            data[pos + 1] = 103;  // 参数标识
            data[pos + 3] = (byte) i; // 参数索引
        }
        // BCC校验
        data[154] = Tools.BCC8(data, 154);

        sendData(connection, data, "CbusDataSendAutodataReq");
    }

    /**
     * 自动数据调试设置
     */
    public static void sendDataAutoDataDebugSet(SerialPortConnection connection) {
        if (connection == null) return;

        byte[] data = new byte[155];
        // 帧头
        data[0] = -86;  // 0xAA
        data[1] = 2;    // 功能码：设置
        data[2] = 7;    // 子功能码：自动数据
        data[3] = -101; // 长度（0x9B的补码）
        // 填充50组参数
        for (int i = 0; i < 50; i++) {
            int pos = (i + 1) * 3;
            data[pos + 1] = 103;  // 参数标识
            data[pos + 3] = (byte) i; // 参数索引
            data[pos + 4] = (byte) DevStateValue.visual_config_arry[i]; // 参数值
        }
        // BCC校验
        data[154] = Tools.BCC8(data, 154);

        sendData(connection, data, "CbusDataSendAutodataDebugSet");
    }

    /**
     * 自动数据配置获取
     */
    public static void sendDataAutoDataConfigGet(SerialPortConnection connection, int paramInt) {
        if (connection == null) return;
        int count = Math.min(paramInt, 50); // 限制最大50组

        int dataLen = count * 3 + 5;
        byte[] data = new byte[dataLen];
        // 帧头
        data[0] = -86;  // 0xAA
        data[1] = 1;    // 功能码：获取
        data[2] = 6;    // 子功能码：自动数据
        data[3] = (byte) dataLen; // 长度
        // 填充参数
        for (int i = 0; i < count; i++) {
            int pos = (i + 1) * 3;
            data[pos + 1] = 103;  // 参数标识
            data[pos + 3] = (byte) i; // 参数索引
        }
        // BCC校验
        data[dataLen - 1] = Tools.BCC8(data, dataLen - 1);

        sendData(connection, data, "CbusDataSendAutodataReq");
    }

    /**
     * 自动数据配置设置
     */
    public static void sendDataAutoDataConfigSet(SerialPortConnection connection, int paramInt) {
        if (connection == null) return;
        int count = Math.min(paramInt, 50); // 限制最大50组

        int dataLen = count * 3 + 5;
        byte[] data = new byte[dataLen];
        // 帧头
        data[0] = -86;  // 0xAA
        data[1] = 2;    // 功能码：设置
        data[2] = 7;    // 子功能码：自动数据
        data[3] = (byte) dataLen; // 长度
        // 填充参数
        for (int i = 0; i < count; i++) {
            int pos = (i + 1) * 3;
            data[pos + 1] = 103;  // 参数标识
            data[pos + 3] = (byte) i; // 参数索引
            data[pos + 4] = (byte) DevStateValue.visual_config_arry[i]; // 参数值
        }
        // BCC校验
        data[dataLen - 1] = Tools.BCC8(data, dataLen - 1);

        sendData(connection, data, "CbusDataSendAutodataDebugSet");
    }

    /**
     * 错误码设置
     */
    public static void sendDataErrCodeSet(SerialPortConnection connection) {
        if (connection == null) return;

        byte[] data = new byte[5];
        byte[] fixed = new byte[]{-86, 5, -1, 5};
        System.arraycopy(fixed, 0, data, 0, fixed.length);
        data[4] = Tools.BCC8(fixed, 4); // BCC校验

        sendData(connection, data, "CbusDataSendErrCodeSet");
    }

    /**
     * 运行日志获取
     */
    public static void sendDataRunLogGet(SerialPortConnection connection) {
        if (connection == null) return;

        byte[] data = new byte[5];
        byte[] fixed = new byte[]{-86, 6, -1, 5};
        System.arraycopy(fixed, 0, data, 0, fixed.length);
        data[4] = Tools.BCC8(fixed, 4); // BCC校验

        sendData(connection, data, "CbusDataSendRunLogGet");
    }

    public static void sendData(SerialPortConnection connection, byte[] sendData, String fun) {
        // 判断串口是否已打开，执行发送操作
        if (connection.isConnection()) {
            // 发送数据
            connection.sendData(sendData);
            String s = new String(sendData);
//            Log.v(TAG_PROTOCOL, fun + "发送数据：" + s);
            Log.v(TAG_PROTOCOL, fun + " 数据发送：" + Tools.bytes2HexString(sendData));
        } else {
            Log.e(TAG_PROTOCOL, "发送失败：串口未打开");
        }
    }
}

