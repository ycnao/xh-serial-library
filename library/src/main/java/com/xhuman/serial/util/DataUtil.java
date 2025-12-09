package com.xhuman.serial.util;


import java.util.ArrayList;
import java.util.List;

/**
 * 串口数据转换工具类
 * author：created by 闹闹 on 2025/10/27
 * version：v1.0.0
 */
public class DataUtil {

    public static byte[] toUid(byte[] array) {
        String read = DataUtils.byteArrToHex(array);
        String[] split = read.split(" ");
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < split.length; i++) {
            if (i != 7 && i != 9 && i != 10 && i != 11 && i != 13) {
                list.add(split[i]);
            }
        }

        // 将ArrayList转化为数组
        byte[] newArray = new byte[list.size()];
        for (int i = 0; i < list.size(); i++) {
            newArray[i] = HexUtil.hexToByte(list.get(i));
        }
        return newArray;
    }

    public static byte calculateBCC8(byte[] data, int length) {
        // 简单的异或CRC计算
        byte crc = 0;
        for (int i = 0; i < length; i++) {
            crc ^= data[i];
        }
        return crc;
    }

}
