package com.xhuman.serial.helper;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.xhuman.serial.common.cbus.DevStateValue;
import com.xhuman.serial.data.ErrorCodeMap;
import com.xhuman.serial.R;

import java.util.Locale;

/**
 * 播放语音
 * author：created by 闹闹 on 2025/11/10
 * version：v1.0.0
 */
public class VoicePlayHelper {

    public static Context context;
    public static int voiceTimeCount = 0;
    public static TextToSpeech textToSpeech;

    public static void Init(Context paramContext) {
        context = paramContext;
        textToSpeech = new TextToSpeech(paramContext, new TextListener());
    }

    public static void startPlay(String paramString) {
        textToSpeech.setPitch(1.0F);
        textToSpeech.setSpeechRate(1.0F);
        textToSpeech.setLanguage(Locale.getDefault());
        if (DevStateValue.voiceConfigState) {
            textToSpeech.speak(paramString, 0, null);
        }
    }

    public static void OnDestroy() {
        // 若TextToSpeech实例存在，则停止播放并销毁资源
        if (textToSpeech != null) {
            textToSpeech.stop(); // 停止当前正在播放的语音
            textToSpeech.shutdown(); // 释放TTS引擎资源（一旦调用，实例不可再使用）
        }
    }

    public static void OnStop() {
        textToSpeech.stop();
    }

    public VoicePlayHelper(Context paramContext) {
        context = paramContext;
    }

    public static String getResStr(int paramInt) {
        return context == null ? "" : context.getResources().getString(paramInt);
    }

    public static void TimeRunLoop() {
        String str = getResStr(R.string.home_dev_sos_info_err);
        boolean bool = false;
        if (voiceTimeCount++ > 15) {
            if (DevStateValue.deviceLineState == 0) {
                startPlay(getResStr(R.string.home_dev_sos_inf_offline));
            } else {
                StringBuilder stringBuilder = new StringBuilder("");
                if (DevStateValue.deviceRSSI < 40) {
                    str = str + getResStr(R.string.home_dev_sos_inf_rssi);
                    bool = true;
                }
                if (DevStateValue.run_auto_recycleWater == 1) {
                    if (!bool) {
                        str = "";
                    }
                    str = str + getResStr(R.string.home_dev_sos_inf_recycleWater);
                    bool = true;
                }
                DevStateValue.sos = false;
                if (DevStateValue.err_code_arry != null) {
                    int[][] arrayOfInt;
                    for (byte b = 0; b < (arrayOfInt = DevStateValue.err_code_arry).length; b++) {
                        int i = arrayOfInt[b][0];
                        String str1;
                        if ((str1 = ErrorCodeMap.getErrCodeString(context, i, arrayOfInt[b][1])) != null) {
                            if (DevStateValue.sos == true) {
                                stringBuilder.setLength(0);
                                stringBuilder.append(str1 + "  ");
                                bool = true;
                                break;
                            }
                            stringBuilder.append(str1 + "  ");
                            bool = true;
                        }
                    }
                }
                if (DevStateValue.repair == 1) {
                    stringBuilder.setLength(0);
                    str = getResStr(R.string.dev_other_repair_config_str);
                } else {
                    str = str + stringBuilder.toString();
                }
                if (bool) startPlay(str);
            }
            voiceTimeCount = 0;
        }
    }

    static class TextListener implements TextToSpeech.OnInitListener {

        public void onInit(int param1Int) {
            // 获取TextToSpeech实例（假设VoicePaly.textToSpeech已初始化）
            TextToSpeech textToSpeech = VoicePlayHelper.textToSpeech;

            // 当param1Int为0时，配置TTS参数
            if (param1Int == 0) {
                // 设置音调（需指定实例，修正原代码的setPitch调用）
                textToSpeech.setPitch(0.9F);
                // 设置语速（1.0为正常语速）
                textToSpeech.setSpeechRate(1.0F);
                // 尝试设置中文（简体）
                int chineseResult = textToSpeech.setLanguage(Locale.SIMPLIFIED_CHINESE);
                // 若中文设置失败（返回值为-1表示失败），尝试设置英文（美国）
                if (chineseResult == TextToSpeech.LANG_MISSING_DATA
                        || chineseResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                    int usResult = textToSpeech.setLanguage(Locale.US);
                    // 可根据需要添加英文设置失败的处理（如日志提示）
                    if (usResult != TextToSpeech.SUCCESS) {
                        Log.e("TTSConfig", "不支持中文和英文");
                    }
                }
            }
        }
    }
}


