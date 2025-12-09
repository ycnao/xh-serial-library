package com.xhuman.serial.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

public class Tools {

    public static Toast toast;


    public static byte[] bytesCut(byte[] arrayOfByte, int param1, int param2) {
        int i;
        byte[] array = new byte[i = param2 - param1 + 1];
        if (arrayOfByte != null && arrayOfByte.length <= param2) {
            return array;
        }
        for (param2 = 0; param2 < i; param2++) {
            array[param2] = arrayOfByte[param1 + param2];
        }
        return array;
    }

    public static String bytes2HexString(byte[] arrayOfByte) {
        String str = "";
        if (arrayOfByte == null) {
            return "";
        }
        for (byte b = 0; b < arrayOfByte.length; b++) {
            String str1;
            if ((str1 = Integer.toHexString(arrayOfByte[b] & 0xFF)).length() == 1) {
                str = str + "0" + str1;
            } else {
                str = str + str1;
            }
        }
        return str.toUpperCase();
    }

    public static String bytes2HexString(byte[] arrayOfByte, int paramInt1, int paramInt2) {
        String str = "";
        if (arrayOfByte == null) {
            return "";
        }
        if (arrayOfByte.length < paramInt2 || arrayOfByte.length < paramInt1) {
            return "";
        }
        while (paramInt1 < paramInt2) {
            String str1;
            if ((str1 = Integer.toHexString(arrayOfByte[paramInt1] & 0xFF)).length() == 1) {
                str = str + "0" + str1;
            } else {
                str = str + str1;
            }
            paramInt1++;
        }
        return str.toUpperCase();
    }

    public static byte[] hexStringToByteArray(String param) {
        int i;
        if ((i = param.length()) % 2 == 0) {
            byte[] arrayOfByte = new byte[i / 2];
            for (byte b = 0; b < i; b += 2) {
                int j = b / 2;
                arrayOfByte[j] = (byte) ((Character.digit(param.charAt(b), 16) << 4) + Character.digit(param.charAt(b + 1), 16));
            }
            return arrayOfByte;
        }
        throw new IllegalArgumentException("Hex string must have even length");
    }

    public static String bytesToAscii(byte[] arrayOfByte) {
        StringBuilder stringBuilder = new StringBuilder();
        int i = arrayOfByte.length;
        for (byte b = 0; b < i; b++) {
            int j;
            if ((j = arrayOfByte[b] & 0xFF) > 31 && j < 127) {
                stringBuilder.append((char) j);
            } else {
                stringBuilder.append(".");
            }
        }
        return stringBuilder.toString();
    }

    public static int CRC16(byte[] arrayOfByte, int paramInt) {
        int i = 0;
        for (byte b = 0; b < paramInt; b++)
            i ^= arrayOfByte[b] & 0xFF;
        return i;
    }

    public static byte BCC8(byte[] arrayOfByte, int paramInt) {
        byte b = 0;
        for (byte b1 = 0; b1 < paramInt; b1++) {
            b = (byte) (b ^ arrayOfByte[b1]);
        }
        return b;

    }

    public static void showToast(Context context, String param) {
        if (toast == null) {
            toast = Toast.makeText(context, param, Toast.LENGTH_SHORT);
            toast.setGravity(17, 0, 0);
        } else {
            toast.setText(param);
            toast.setGravity(17, 0, 0);
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.show();
    }

    public static String getVersionName(Context context) {
        try {
            return (context.getPackageManager().getPackageInfo(context.getPackageName(), 0)).versionName;
        } catch (PackageManager.NameNotFoundException nameNotFoundException) {
            return "1.0.0";
        }
    }

    public static boolean isForeground(Activity activity) {
        return isForeground(activity, activity.getClass().getName());
    }

    public static boolean isForegroundString(Activity activity, String param) {
        return isForeground(activity, param);
    }

    public static boolean isForeground(Activity activity, String param) {
        List list;
        return (activity == null || TextUtils.isEmpty(param)) ? false : (((list = ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE)).getRunningTasks(1)) != null && list.size() > 0 && param.equals(((ActivityManager.RunningTaskInfo) list.get(0)).topActivity.getClassName())));
    }

    public static boolean getVisblehInfo(int paramInt) {
        return !(paramInt == 65533 || paramInt == 65531);
    }

    public static String getTopActivity(Context context) {
        return ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningTasks(1).get(0).topActivity.getClassName();
    }

    public static String getMacAddress(Context context) {
//        String str;
//        paramContext = null;
//        WifiManager wifiManager;
//        WifiInfo wifiInfo;
//        if ((wifiManager = (WifiManager) paramContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE)) != null && (wifiInfo = wifiManager.getConnectionInfo()) != null)
//            str = wifiInfo.getMacAddress();
//        return str;
        return "";
    }


    public static void arraycopy(byte[] src, int srcPos, byte[] dest, int destPos, int length) {
        try {
            if (srcPos + length <= src.length && destPos + length <= dest.length) {
                System.arraycopy(src, srcPos, dest, destPos, length);
            } else {
                Log.e("数组越界", "src.length:" + src.length + "  srcPos:" + srcPos + "  dst.length:" + dest.length + "  dstPos:" + destPos + "  length:" + length);
            }
        } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
            Log.e("数组越界", "数组越界异常:" + arrayIndexOutOfBoundsException.getMessage());
        }
    }

    public static String processString(String param1, String param2, int length) {
        int j;
        if ((j = param1.length()) > length) {
            return param1.substring(0, length);
        }
        StringBuilder stringBuilder = new StringBuilder(param1);
        int i = length - j;
        for (j = 0; j < param2.length() && i > 0; j++) {
            stringBuilder.append(param2.charAt(j));
            i--;
        }
        return stringBuilder.toString();
    }
}
