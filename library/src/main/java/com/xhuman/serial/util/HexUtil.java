package com.xhuman.serial.util;

import android.util.Log;

import java.math.BigInteger;

public class HexUtil {
    private static String TAG = "HEXNum";

    public static String encodeHEX(int num, boolean isHeight) {
        String toHex = "";
        if (num == 0) {
            if (isHeight)
                toHex = "0000";
            else
                toHex = "00";
        } else {
            String hex = Long.toHexString(num);
            if (isHeight) {
                if (num > 255) {
                    if (hex.length() == 3) {
                        toHex = "0" + hex;
                    } else if (hex.length() == 4) {
                        toHex = hex;
                    } else {
                        toHex = "FFFF";
                    }
                } else {
                    toHex = "00" + hex;
                }
                Log.d(TAG, "十进制数：" + num + "转换为十六进制数：" + toHex);
            } else {
                if (hex.length() == 1) {
                    toHex = "0" + hex;
                } else {
                    toHex = hex;
                }
            }
        }
        return toHex;
    }

    //將16進制字符串轉換為10進制數字
    public static int decodeHEX(String hex) {
        if (hex != null && !hex.equals("")) {
            BigInteger bigint = new BigInteger(hex, 16);
            int num = bigint.intValue();
            System.out.println("十六进制数：" + hex + "转换为十进制数：" + num);
            return num;
        } else {
            return 0;
        }
    }

    /**
     * 十六进制字符串转换成字节数组
     *
     * @param hexstr 十六进制字符串
     * @return 字节数组
     */
    public static byte[] hexStrToByte(String hexstr) {
        int len = (hexstr.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hexstr.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (((byte) "0123456789ABCDEF".indexOf(achar[pos])) << 4 | ((byte) "0123456789ABCDEF".indexOf(achar[pos + 1])));
        }
        return result;
    }

    //    XOR校验值生成代码
    public static byte getXor(byte[] datas) {
        byte temp = datas[0];
        for (int i = 1; i < datas.length; i++) {

            temp ^= datas[i];
        }
        return temp;
    }

    //1字节转2个Hex字符
    public static String Byte2Hex(Byte inByte) {
        return String.format("%01x", new Object[]{inByte}).toUpperCase();
    }

    /**
     * @param data1
     * @param data2
     * @return data1 与 data2拼接的结果
     */
    public static byte[] addBytes(byte[] data1, byte[] data2) {
        byte[] data3 = new byte[data1.length + data2.length];
        System.arraycopy(data1, 0, data3, 0, data1.length);
        System.arraycopy(data2, 0, data3, data1.length, data2.length);
        return data3;

    }

    public static String byteToString(byte src) {
        String desc = null;
        int i = 0;    //取1个字节
        i = src & 0xFF;
        desc = Integer.toHexString(i);
        if (desc.length() == 1) {         //当只有1位时前面补0
            desc = "0" + desc;
        }
        return desc;
    }

    /**
     * 字节转十六进制
     *
     * @param b 需要进行转换的byte字节
     * @return 转换后的Hex字符串
     */
    public static String byteToHex(byte b) {
        String hex = Integer.toHexString(b & 0xFF);
        if (hex.length() < 2) {
            hex = "0" + hex;
        }
        return hex;
    }

    /**
     * 字节数组转16进制
     *
     * @param bytes 需要转换的byte数组
     * @return 转换后的Hex字符串
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(aByte & 0xFF);
            if (hex.length() < 2) {
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * Hex字符串转byte
     *
     * @param inHex 待转换的Hex字符串
     * @return 转换后的byte
     */
    public static byte hexToByte(String inHex) {
        return (byte) Integer.parseInt(inHex, 16);
    }

    /**
     * hex字符串转byte数组
     *
     * @param inHex 待转换的Hex字符串
     * @return 转换后的byte数组结果
     */
    public static byte[] hexToByteArray(String inHex) {
        int hexlen = inHex.length();
        byte[] result;
        if (hexlen % 2 == 1) {
            //奇数
            hexlen++;
            result = new byte[(hexlen / 2)];
            inHex = "0" + inHex;
        } else {
            //偶数
            result = new byte[(hexlen / 2)];
        }
        int j = 0;
        for (int i = 0; i < hexlen; i += 2) {
            result[j] = hexToByte(inHex.substring(i, i + 2));
            j++;
        }
        return result;
    }

}
