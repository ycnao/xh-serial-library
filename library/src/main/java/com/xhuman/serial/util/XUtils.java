package com.xhuman.serial.util;

import android.content.Context;
import android.provider.Settings;

public class XUtils {

    public static String androidId(Context context) {
        String uid = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return Tools.processString(uid, "112233445566778899AABBCC", 24);
    }

    public static String getResStr(Context mContext, int paramInt) {
        return mContext.getString(paramInt);
    }

    public static String[] getStringArray(Context mContext, int paramInt) {
        return mContext.getResources().getStringArray(paramInt);
    }
}
