package com.xhuman.serial.data;


import android.content.Context;

import com.xhuman.serial.R;

public class VisualStateMap {

    // 静态上下文变量，用于获取字符串资源
    public static Context context;

    /**
     * 根据状态码和参数获取视觉状态描述字符串
     *
     * @param mContext 上下文（实际是状态码，字节码中参数1用途推测为状态码，命名为code更合理）
     * @param param   动态参数（用于拼接字符串）
     * @return 视觉状态描述字符串
     */
    public static String getStateString(Context mContext, int code, int param) {
        context = mContext;
        // 2. 默认返回"未知状态"
        String result = mContext.getString(R.string.vision_unknown);

        // 3. 第一层分支：判断状态码code是否为255
        if (code != 255) {
            // 3.1 第一层tableswitch：匹配code的第一个区间（0-6）
            switch (code) {
                case 0:
                    result = mContext.getString(R.string.vision_00);
                    break;
                case 1:
                    result = mContext.getString(R.string.vision_01);
                    break;
                case 2:
                    // 拼接：资源字符串 + param + "°"
                    result = new StringBuilder()
                            .append(mContext.getString(R.string.vision_02))
                            .append(param)
                            .append("°")
                            .toString();
                    break;
                case 3:
                    result = mContext.getString(R.string.vision_03);
                    break;
                case 4:
                    result = mContext.getString(R.string.vision_04);
                    break;
                case 5:
                    result = mContext.getString(R.string.vision_05);
                    break;
                case 6:
                    // 拼接：资源字符串 + param + "s"
                    result = new StringBuilder()
                            .append(mContext.getString(R.string.vision_06))
                            .append(param)
                            .append("s")
                            .toString();
                    break;
                // 3.2 第二层tableswitch：匹配code的第二个区间（16-20）
                case 16:
                    result = mContext.getString(R.string.vision_10);
                    break;
                case 17:
                    result = mContext.getString(R.string.vision_11);
                    break;
                case 18:
                    // 拼接：资源字符串 + param
                    result = new StringBuilder()
                            .append(mContext.getString(R.string.vision_12))
                            .append(param)
                            .toString();
                    break;
                case 19:
                    result = mContext.getString(R.string.vision_13);
                    break;
                case 20:
                    result = mContext.getString(R.string.vision_14);
                    break;
                // 3.3 第三层tableswitch：匹配code的第三个区间（32-39）
                case 32:
                    result = mContext.getString(R.string.vision_20);
                    break;
                case 33:
                    // 拼接：资源字符串 + param
                    result = new StringBuilder()
                            .append(mContext.getString(R.string.vision_21))
                            .append(param)
                            .toString();
                    break;
                case 34:
                    result = mContext.getString(R.string.vision_22);
                    break;
                case 35:
                    result = mContext.getString(R.string.vision_23);
                    break;
                case 36:
                    result = mContext.getString(R.string.vision_24);
                    break;
                case 37:
                    result = mContext.getString(R.string.vision_25);
                    break;
                case 38:
                    result = mContext.getString(R.string.vision_26);
                    break;
                case 39:
                    result = mContext.getString(R.string.vision_27);
                    break;
                // 3.4 第四层tableswitch：匹配code的第四个区间（48-55）
                case 48:
                    result = mContext.getString(R.string.vision_30);
                    break;
                case 49:
                    // 拼接：资源字符串 + param + "%"
                    result = new StringBuilder()
                            .append(mContext.getString(R.string.vision_31))
                            .append(param)
                            .append("%")
                            .toString();
                    break;
                case 50:
                    // 拼接：资源字符串 + param + "%"
                    result = new StringBuilder()
                            .append(mContext.getString(R.string.vision_32))
                            .append(param)
                            .append("%")
                            .toString();
                    break;
                case 51:
                    // 拼接：资源字符串 + param + "°"
                    result = new StringBuilder()
                            .append(mContext.getString(R.string.vision_33))
                            .append(param)
                            .append("°")
                            .toString();
                    break;
                case 52:
                    // 拼接：资源字符串 + param + "°"
                    result = new StringBuilder()
                            .append(mContext.getString(R.string.vision_34))
                            .append(param)
                            .append("°")
                            .toString();
                    break;
                case 53:
                    // 拼接：资源字符串 + param + "cm"
                    result = new StringBuilder()
                            .append(mContext.getString(R.string.vision_35))
                            .append(param)
                            .append("cm")
                            .toString();
                    break;
                case 54:
                    // 拼接：资源字符串 + param + "cm"
                    result = new StringBuilder()
                            .append(mContext.getString(R.string.vision_36))
                            .append(param)
                            .append("cm")
                            .toString();
                    break;
                case 55:
                    result = mContext.getString(R.string.vision_37);
                    break;
                // 3.5 第五层tableswitch：匹配code的第五个区间（64-67）
                case 64:
                    result = mContext.getString(R.string.vision_40);
                    break;
                case 65:
                    // 拼接：资源字符串 + param + "%"
                    result = new StringBuilder()
                            .append(mContext.getString(R.string.vision_41))
                            .append(param)
                            .append("%")
                            .toString();
                    break;
                case 66:
                    // 拼接：资源字符串 + param + "%"
                    result = new StringBuilder()
                            .append(mContext.getString(R.string.vision_42))
                            .append(param)
                            .append("%")
                            .toString();
                    break;
                case 67:
                    result = mContext.getString(R.string.vision_43);
                    break;
                // 默认分支：保持默认值"未知状态"
                default:
                    break;
            }
        } else {
            // 4. 第二层分支：当code=255时，判断param的值
            if (param == 136) {
                result = mContext.getString(R.string.vision_FF_88);
            } else if (param == 137) {
                result = mContext.getString(R.string.vision_FF_89);
            } else {
                switch (param) {
                    case 1:
                        result = mContext.getString(R.string.vision_FF_01);
                        break;
                    case 2:
                        result = mContext.getString(R.string.vision_FF_02);
                        break;
                    case 3:
                        result = mContext.getString(R.string.vision_FF_03);
                        break;
                    case 4:
                        result = mContext.getString(R.string.vision_FF_04);
                        break;
                    case 5:
                        result = mContext.getString(R.string.vision_FF_05);
                        break;
                    case 6:
                        result = mContext.getString(R.string.vision_FF_06);
                        break;
                    case 7:
                        result = mContext.getString(R.string.vision_FF_07);
                        break;
                    case 8:
                        result = mContext.getString(R.string.vision_FF_08);
                        break;
                    case 144:
                        result = mContext.getString(R.string.vision_FF_90);
                        break;
                    case 145:
                        result = mContext.getString(R.string.vision_FF_91);
                        break;
                    case 146:
                        result = mContext.getString(R.string.vision_FF_92);
                        break;
                    case 147:
                        result = mContext.getString(R.string.vision_FF_93);
                        break;
                    case 148:
                        result = mContext.getString(R.string.vision_FF_94);
                        break;
                    case 149:
                        result = mContext.getString(R.string.vision_FF_95);
                        break;
                    case 150:
                        result = mContext.getString(R.string.vision_FF_96);
                        break;
                    case 151:
                        result = mContext.getString(R.string.vision_FF_97);
                        break;
                    case 152:
                        result = mContext.getString(R.string.vision_FF_98);
                        break;
                    case 153:
                        result = mContext.getString(R.string.vision_FF_99);
                        break;
                    // 默认分支：保持默认值"未知状态"
                    default:
                        break;
                }
            }
        }
        // 返回最终生成的描述字符串
        return result;
    }
}

