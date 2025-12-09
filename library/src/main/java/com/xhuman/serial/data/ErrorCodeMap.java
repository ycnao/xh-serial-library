package com.xhuman.serial.data;

import android.content.Context;

import com.xhuman.serial.R;
import com.xhuman.serial.common.cbus.DevStateValue;

public class ErrorCodeMap {

    public static Context mContext;

    /**
     * 根据错误码和参数获取错误描述字符串
     *
     * @param errCode 错误码（核心匹配依据）
     * @param param   错误参数（用于细化错误描述）
     * @return 错误描述字符串，无匹配时返回null
     */
    public static String getErrCodeString(Context context, int errCode, int param) {
        mContext = context;
        String errorDesc = null;

        // 1. 错误码前缀校验：右移8位后与0xE0（224）按位与，需等于0xC0（192），否则返回null
        int codePrefix = (errCode >> 8) & 0xE0;
        if (codePrefix != 0xC0) {
            return errorDesc;
        }

        // 2. 截取错误码有效部分（低13位，与0x1FFF（8191）按位与）
        int realErrCode = errCode & 0x1FFF;

        // 3. 错误码为0时，返回UTC相关错误
        if (realErrCode == 0) {
            errorDesc = getResStr(R.string.err_sys_utc);
            return errorDesc;
        }

        // 4. 按错误码匹配对应的错误描述
        switch (realErrCode) {
            // 水加热相关错误（1024）
            case 1024:
                if (param == 0xFF) { // 0xFF即255
                    errorDesc = getResStr(R.string.err_water_heat_1);
                } else if (param == 1) {
                    errorDesc = getResStr(R.string.err_water_heat_2);
                } else if (param == 2) {
                    errorDesc = getResStr(R.string.err_water_heat_3);
                } else if (param == 3) {
                    errorDesc = getResStr(R.string.err_water_heat_4);
                }
                break;

            // 子控相关错误（1025）
            case 1025:
                errorDesc = getResStr(R.string.err_sub_control);
                break;

            // 电池相关错误（256）
            case 256:
                if (param == 0xFF) {
                    errorDesc = getResStr(R.string.err_run_battery);
                } else if (param == 0xFE) { // 254
                    errorDesc = getResStr(R.string.err_run_battery_off);
                } else if (param == 1) {
                    errorDesc = getResStr(R.string.err_run_battery_temp1);
                } else if (param == 2) {
                    errorDesc = getResStr(R.string.err_run_battery_temp2);
                } else {
                    // 其他电池错误，拼接参数提示
                    errorDesc = new StringBuilder()
                            .append(getResStr(R.string.err_run_battery_1))
                            .append("[")
                            .append(param)
                            .append("]")
                            .toString();
                }
                break;

            // 水位相关错误（257）
            case 257:
                errorDesc = getResStr(R.string.err_run_water);
                break;

            // 负载相关错误（258）
            case 258:
                errorDesc = getResStr(R.string.err_run_load);
                break;

            // 箱体温度相关错误（259）
            case 259:
                if (param == 0xFF) {
                    errorDesc = getResStr(R.string.err_run_box_temp_h);
                } else {
                    errorDesc = getResStr(R.string.err_run_box_temp_l);
                }
                break;

            // 距离传感器错误（260-263）
            case 260:
                errorDesc = getResStr(R.string.err_run_distance1);
                break;
            case 261:
                errorDesc = getResStr(R.string.err_run_distance2);
                break;
            case 262:
                errorDesc = getResStr(R.string.err_run_distance3);
                break;
            case 263:
                errorDesc = getResStr(R.string.err_run_distance4);
                break;

            // 触摸传感器错误（264-267）
            case 264:
                errorDesc = getResStr(R.string.err_run_touch1);
                break;
            case 265:
                errorDesc = getResStr(R.string.err_run_touch2);
                break;
            case 266:
                errorDesc = getResStr(R.string.err_run_touch3);
                break;
            case 267:
                errorDesc = getResStr(R.string.err_run_touch4);
                break;

            // 碰撞相关错误（268-271）
            case 268:
                errorDesc = getResStr(R.string.err_run_collision1);
                break;
            case 269:
                errorDesc = getResStr(R.string.err_run_collision2);
                break;
            case 270:
                errorDesc = getResStr(R.string.err_run_collision3);
                break;
            case 271:
                errorDesc = getResStr(R.string.err_run_collision4);
                break;

            // 紧急按钮错误（272）：同时设置SOS状态
            case 272:
                DevStateValue.sos = true;
                errorDesc = getResStr(R.string.err_run_dangerbtn);
                break;

            // 视觉相关错误（273）
            case 273:
                errorDesc = getResStr(R.string.err_run_visual);
                break;

            // 压力相关错误（274-275）
            case 274:
                errorDesc = getResStr(R.string.err_run_pressure1);
                break;
            case 275:
                errorDesc = getResStr(R.string.err_run_pressure2);
                break;

            // 电机相关错误（512-528）：从数组中匹配描述
            case 512:
            case 513:
            case 514:
            case 515:
            case 516:
            case 517:
            case 518:
            case 519:
            case 520:
            case 521:
            case 522:
            case 523:
            case 524:
            case 525:
            case 526:
            case 527:
            case 528:
                errorDesc = getMotorErrorDesc(realErrCode, param);
                break;

            // 传感器相关错误（768-785，除786）：从数组中匹配描述
            case 768:
            case 769:
            case 770:
            case 771:
            case 772:
            case 773:
            case 774:
            case 775:
            case 776:
            case 777:
            case 778:
            case 779:
            case 780:
            case 781:
            case 782:
            case 783:
            case 784:
            case 785:
                errorDesc = getSensorErrorDesc(realErrCode, param);
                break;

            // UWB传感器错误（786）：从数组中匹配描述
            case 786:
                errorDesc = getUwbSensorErrorDesc(param);
                break;

            // 远程相关错误（1536-1539）
            case 1536:
                errorDesc = getResStr(R.string.err_remote_iot);
                break;
            case 1537:
                errorDesc = getResStr(R.string.err_remote_h12);
                break;
            case 1538:
                errorDesc = getResStr(R.string.err_remote_g24);
                break;
            case 1539:
                errorDesc = getResStr(R.string.err_remote_ground);
                break;

            // 默认分支：无匹配错误码，返回null
            default:
                break;
        }

        return errorDesc;
    }

    /**
     * 辅助方法：获取电机相关错误描述
     *
     * @param errCode 电机错误码（512-528）
     * @param param   错误参数
     * @return 电机错误描述字符串
     */
    private static String getMotorErrorDesc(int errCode, int param) {
        // 从资源数组中获取电机错误类型和参数描述
        String[] motorErrTypes = getStrArray(R.array.err_motor_list);
        String[] motorErrValues = getStrArray(R.array.err_motor_value_list);

        // 计算数组索引（错误码低8位）
        int typeIndex = errCode & 0xFF;
        int valueIndex = param & 0xFF;

        // 索引合法时拼接描述，否则返回null
        if (typeIndex < motorErrTypes.length && valueIndex < motorErrValues.length) {
            return new StringBuilder()
                    .append(motorErrTypes[typeIndex])
                    .append(motorErrValues[valueIndex])
                    .toString();
        }
        return null;
    }

    /**
     * 辅助方法：获取普通传感器相关错误描述
     *
     * @param errCode 传感器错误码（768-785）
     * @param param   错误参数
     * @return 传感器错误描述字符串
     */
    private static String getSensorErrorDesc(int errCode, int param) {
        // 从资源数组中获取传感器错误类型和参数描述
        String[] sensorErrTypes = getStrArray(R.array.err_sensor_list);
        String[] sensorErrValues = getStrArray(R.array.err_sensor_value_list);

        // 计算数组索引（错误码低8位）
        int typeIndex = errCode & 0xFF;
        int valueIndex = param & 0xFF;

        // 索引合法时拼接描述，否则返回null
        if (typeIndex < sensorErrTypes.length && valueIndex < sensorErrValues.length) {
            return new StringBuilder()
                    .append(sensorErrTypes[typeIndex])
                    .append(sensorErrValues[valueIndex])
                    .toString();
        }
        return null;
    }

    /**
     * 辅助方法：获取UWB传感器错误描述
     *
     * @param param 错误参数
     * @return UWB传感器错误描述字符串
     */
    private static String getUwbSensorErrorDesc(int param) {
        // 从资源数组中获取UWB传感器错误描述
        String[] uwbErrValues = getStrArray(R.array.err_sensor_uwb_value_list);

        // 计算数组索引（参数低8位）
        int valueIndex = param & 0xFF;

        // 索引合法时返回描述，否则返回null
        if (valueIndex < uwbErrValues.length) {
            return uwbErrValues[valueIndex];
        }
        return null;
    }

    public static String getResStr(int paramInt) {
        return mContext.getString(paramInt);
    }

    public static String[] getStrArray(int paramInt) {
        return mContext.getResources().getStringArray(paramInt);
    }

}

