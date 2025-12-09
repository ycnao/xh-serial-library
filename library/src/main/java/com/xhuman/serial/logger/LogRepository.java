package com.xhuman.serial.logger;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LogRepository {

    public SharedPreferences preferences;

    public Gson gson = new Gson();

    public LogRepository(Context paramContext) {
        preferences = paramContext.getSharedPreferences("DeviceLogPrefs", 0);
    }

    public void saveLogs(List<DevLogManager.LogEntry> paramList) {
        // 对logCollection加锁，保证线程安全（避免多线程同时操作同一集合）
        try {
            // 2. 通过Gson将日志列表序列化为JSON字符串
            String logJson =gson.toJson(paramList);

            // 3. 存入SharedPreferences
            // 获取Editor对象
            SharedPreferences.Editor editor = preferences.edit();
            // 以"DeviceLogEntries"为key，存储JSON字符串
            editor.putString("DeviceLogEntries", logJson);
            // 异步提交（apply()无返回值，异步写入；commit()有返回值，同步写入）
            editor.apply();
        } finally {
            // 确保锁最终释放（字节码中finally块的monitorexit指令）
            // 注：Java中synchronized会自动释放锁，此处为字节码层面的显式保证，Java代码中无需额外编写
        }
    }

    public List<DevLogManager.LogEntry> loadLogs() {
        String string = preferences.getString("DeviceLogEntries", null);
        if (string == null) {
            return new ArrayList();
        }
        return (List) gson.fromJson(string, new LogToken(this).getType());
    }



    class LogToken extends TypeToken<ArrayList<DevLogManager.LogEntry>> {
        public LogToken(LogRepository logRepository) {

        }
    }
}

