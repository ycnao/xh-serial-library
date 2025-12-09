package com.xhuman.serial.util;


//数据校验
public class DataCheck {

    public static byte GetCRC8(byte[] str, int num) {
        byte crc = 0x00;
        byte poly = 0x07;
        int bit;
        for (int i = 0; i < num; i++) {
            crc = (byte) (crc ^ str[i]);
            for (bit = 0; bit < 8; bit++) {
                if ((crc & 0x80) != 0) {
                    crc = (byte) ((crc << 1) ^ poly);
                } else {
                    crc <<= 1;
                }
            }
        }
        return crc;
    }

    public static short GetCRC16(byte[] str, int num) {
        short CRCin = (short) 0xffff;
        for (int i = 0; i < num; i++) {
            CRCin = (short) (str[i] ^ CRCin);
            for (int j = 0; j < 8; j++) {
                if ((CRCin & 0x01) != 0) {
                    CRCin = (short) (CRCin >> 1);
                    CRCin = (short) (CRCin ^ 0xa001); // 0xa001是由0x8005高低位转换所得
                } else {
                    CRCin = (short) (CRCin >> 1);
                }
            }
        }
        short CRCret = (short) (CRCin >> 8);
        CRCret = (short) (CRCret | (CRCin << 8)); // CRC16必须进行高低位交换
        return CRCret;
    }

    public static byte GetXOR(byte[] str, int num) {
        byte xor = 0;
        for (int i = 0; i < num; i++) {
            xor ^= str[i];
        }
        return xor;
    }

    //低位符转义
    public static short getShortDataByByte(byte hsb, byte lsb) {
        short ret;
        short uByte;
        if (hsb >= 0) {
            uByte = lsb < 0 ? (short) (255 + lsb) : lsb;
        } else {
            uByte = lsb > 0 ? (short) (lsb - 255) : lsb;
        }
        ret = (short) (hsb << 8 | uByte);
        return ret;
    }
}