package com.xhuman.serial.helper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolBuilder {

    public static ExecutorService buildThreadPoolExecutor(String name) {
        return new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1024),
                new NameThreadFactory(name), new ThreadPoolExecutor.AbortPolicy());
    }

    public static ExecutorService buildThreadPoolExecutor(String name, int corePoolSize, int maximumPoolSize, long keepAliveTime) {
        return new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1024),
                new NameThreadFactory(name), new ThreadPoolExecutor.AbortPolicy());
    }


    public static ScheduledExecutorService buildScheduledExecutor(String name) {
        return new ScheduledThreadPoolExecutor(1, new NameThreadFactory(name));
    }
}
