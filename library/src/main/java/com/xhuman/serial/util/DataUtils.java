package com.xhuman.serial.util;

import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 串口数据转换工具类
 * author：created by 闹闹 on 2025/10/27
 * version：v1.0.0
 */
public class DataUtils {

    // 判断奇数或偶数，位运算，最后一位是1则为奇数，为0是偶数
    public static int isOdd(int num) {
        return num & 1;
    }

    //Hex字符串转int
    public static int hexToInt(String inHex) {
        return Integer.parseInt(inHex, 16);
    }

    public static String intToHex(int intHex) {
        return Integer.toHexString(intHex);
    }

    //Hex字符串转byte
    public static byte hexToByte(String inHex) {
        return (byte) Integer.parseInt(inHex, 16);
    }

    //1字节转2个Hex字符
    public static String byte2Hex(Byte inByte) {
        return String.format("%02x", new Object[]{inByte}).toUpperCase();
    }

    //hex字符串 转 字符串
    public static String hexStrToString(String hexString) {
        // 将十六进制字符串转换为字节数组
        byte[] bytes = hexStrToByteArray(hexString);

        // 将字节数组转换为字符串
        String result = new String(bytes, StandardCharsets.UTF_8); // 使用UTF-8编码
        Log.d("dddd", hexString + " - To - " + result);
        return result;
    }


    //字节数组转转hex字符串
    public static String byteArrToHex(byte[] inBytArr) {
        StringBuilder strBuilder = new StringBuilder();
        for (byte valueOf : inBytArr) {
            strBuilder.append(byte2Hex(Byte.valueOf(valueOf)));
            strBuilder.append(" ");
        }
        return strBuilder.toString();
    }

    //字节数组转转hex字符串，可选长度
    public static String byteArrToHex(byte[] inBytArr, int offset, int byteCount) {
        StringBuilder strBuilder = new StringBuilder();
        int j = byteCount;
        for (int i = offset; i < j; i++) {
            strBuilder.append(byte2Hex(Byte.valueOf(inBytArr[i])));
        }
        return strBuilder.toString();
    }

    //转hex字符串转字节数组
    public static byte[] hexToByteArray(String inHex) {
        byte[] result;
        int hexlen = inHex.length();
        if (isOdd(hexlen) == 1) {
            hexlen++;
            result = new byte[(hexlen / 2)];
            inHex = "0" + inHex;
        } else {
            result = new byte[(hexlen / 2)];
        }
        int j = 0;
        for (int i = 0; i < hexlen; i += 2) {
            result[j] = hexToByte(inHex.substring(i, i + 2));
            j++;
        }
        return result;
    }

    public static byte[] hexStrToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    /**
     * 按照指定长度切割字符串
     *
     * @param inputString 需要切割的源字符串
     * @param length      指定的长度
     * @return
     */
    public static List<String> getDivLines(String inputString, int length) {
        List<String> divList = new ArrayList<>();
        int remainder = (inputString.length()) % length;
        // 一共要分割成几段
        int number = (int) Math.floor((inputString.length()) / length);
        for (int index = 0; index < number; index++) {
            String childStr = inputString.substring(index * length, (index + 1) * length);
            divList.add(childStr);
        }
        if (remainder > 0) {
            String cStr = inputString.substring(number * length, inputString.length());
            divList.add(cStr);
        }
        return divList;
    }

    /**
     * 计算长度，两个字节长度
     *
     * @param val value
     * @return 结果
     */
    public static String twoByte(String val) {
        if (val.length() > 4) {
            val = val.substring(0, 4);
        } else {
            int l = 4 - val.length();
            for (int i = 0; i < l; i++) {
                val = "0" + val;
            }
        }
        return val;
    }

    /**
     * 校验和
     *
     * @param cmd 指令
     * @return 结果
     */
    public static String sum(String cmd) {
        List<String> cmdList = DataUtils.getDivLines(cmd, 2);
        int sumInt = 0;
        for (String c : cmdList) {
            sumInt += DataUtils.hexToInt(c);
        }
        String sum = DataUtils.intToHex(sumInt);
        sum = DataUtils.twoByte(sum);
        cmd += sum;
        return cmd.toUpperCase();
    }
}
