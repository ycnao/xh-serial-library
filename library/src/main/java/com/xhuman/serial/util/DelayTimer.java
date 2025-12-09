package com.xhuman.serial.util;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

import java.util.Timer;
import java.util.TimerTask;

public class DelayTimer {

    boolean timerStart = false;
    long startTime = 0, endTime = 0;
    Timer timer;

    //普通延时，需要用while
    public boolean delayTimerSet(int ms) {
        if (!timerStart) {
            startTime = SystemClock.elapsedRealtime();//启动到现在的ms
            timerStart = true;
        }
        endTime = SystemClock.elapsedRealtime() - startTime;
        if (endTime >= ms) {
            timerStart = false;
            return true;
        }
        return false;
    }

    public void delayTimerReset() {
        timerStart = false;
    }


    //在主进程进行延时，通常用于和UI有关联的地方
    public void threadSleep(int ms, Runnable runnable) {
        new Handler(Looper.getMainLooper()).postDelayed(runnable, ms);
    }

    //生成一个常规定时器
    public void timerStart(int ms, TimerTask task) {
        timer = new Timer();
        timer.schedule(task, 0, ms);
    }

    public void timerStop() {
        if (timer != null) {
            timer.cancel();
        }
    }

}
