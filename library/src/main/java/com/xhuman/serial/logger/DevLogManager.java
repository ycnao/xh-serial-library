package com.xhuman.serial.logger;

import android.content.Context;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DevLogManager {

    // 应用上下文（避免内存泄漏）
    private static Context appContext;
    // 日志仓库
    private LogRepository logRepository;
    // 单例实例（volatile保证多线程可见性）
    private static volatile DevLogManager instance;
    // 日志列表（需同步操作）
    private final List<LogEntry> logEntries;
    // 日期格式化（线程不安全，使用ThreadLocal包装）
    public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // 私有构造方法（防止外部实例化）
    private DevLogManager() {
        this.logEntries = new ArrayList<>();
        initLogRepository(); // 初始化日志仓库
    }

    // 单例获取（双重校验锁保证线程安全）
    public static DevLogManager instance() {
        if (instance == null) {
            synchronized (DevLogManager.class) {
                if (instance == null) {
                    instance = new DevLogManager();
                }
            }
        }
        return instance;
    }

    // 设置上下文（建议在Application初始化时调用）
    public static void setContext(Context context) {
        if (context != null) {
            appContext = context.getApplicationContext(); // 存储应用上下文
        }
    }

    // 添加日志
    public void addLog(int eventType, int eventId, int eventValue) {
        if (appContext == null) {
            throw new IllegalStateException("Context not initialized. Call setContext() first.");
        }

        LogEntry newEntry = new LogEntry(eventType, eventId, eventValue);
        synchronized (logEntries) { // 同步操作避免并发问题
            // 超过500条时移除最早的日志
            if (logEntries.size() >= 500) {
                logEntries.remove(0);
            }
            logEntries.add(newEntry);
        }

        // 保存日志到仓库
        if (logRepository == null) {
            initLogRepository();
        }
        logRepository.saveLogs(getAllLogs()); // 传递副本避免并发修改异常
    }

    // 获取所有日志（返回副本保证数据安全）
    public List<LogEntry> getAllLogs() {
        synchronized (logEntries) {
            return new ArrayList<>(logEntries); // 返回副本，防止外部修改内部列表
        }
    }

    // 清空日志（包括内存和仓库）
    public void clear() {
        synchronized (logEntries) {
            logEntries.clear();
        }
        if (logRepository != null) {
            logRepository.saveLogs(new ArrayList<>()); // 保存空列表到仓库
        }
    }

    // 初始化日志仓库
    private void initLogRepository() {
        if (appContext == null) {
            throw new IllegalStateException("Context not initialized. Call setContext() first.");
        }
        this.logRepository = new LogRepository(appContext);
        // 从仓库加载日志（首次初始化时）
        synchronized (logEntries) {
            logEntries.addAll(logRepository.loadLogs());
        }
    }

    public static class LogEntry implements Serializable {

        public final Date timestamp;
        public final int eventType;
        public final int eventId;
        public final int eventValue;

        public String getFormattedTimestamp() {
            return DevLogManager.simpleDateFormat.format(timestamp);
        }

        public LogEntry(int param1Int1, int param1Int2, int param1Int3) {
            timestamp = new Date();
            eventType = param1Int1;
            eventId = param1Int2;
            eventValue = param1Int3;
        }

        public Date getTimestamp() {
            return timestamp;
        }

        public int getEventType() {
            return eventType;
        }

        public int getEventId() {
            return eventId;
        }

        public int getEventValue() {
            return eventValue;
        }
    }
}
