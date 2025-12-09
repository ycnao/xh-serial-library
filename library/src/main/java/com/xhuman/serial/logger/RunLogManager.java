package com.xhuman.serial.logger;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RunLogManager {

    public static RunLogManager runLogManager;
    public static List<LogEntry> logEntryList;
    public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public RunLogManager() {
        logEntryList = new ArrayList<>();
    }

    public static synchronized RunLogManager instance() {
        if (runLogManager == null) {
            runLogManager = new RunLogManager();
        }
        return runLogManager;
    }

    public void addLog(int eventType, int eventId, int eventValue, long timestamp) {
        LogEntry logEntry = new LogEntry(eventType, eventId, eventValue, timestamp);
        if (logEntryList.size() >= 500) {
            logEntryList.remove(0);
        }
        logEntryList.add(logEntry);
    }

    public void clearLog() {
        logEntryList.clear();
    }

    public List<LogEntry> getAllLogs() {
        return new ArrayList<>(logEntryList);
    }

    public static class LogEntry implements Serializable {

        public final int eventType;
        public final int eventId;
        public final int eventValue;
        public final Date timestamp;

        public LogEntry(int eventType, int eventId, int eventValue, long timestamp) {
            this.eventType = eventType;
            this.eventId = eventId;
            this.eventValue = eventValue;
            this.timestamp = new Date(timestamp * 1000L);
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

        public String getFormattedTimestamp() {
            return RunLogManager.simpleDateFormat.format(timestamp);
        }
    }
}


