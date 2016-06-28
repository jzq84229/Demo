package com.zhang.mydemo.util;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Process;

/**
 * Created by Administrator on 2016/6/23.
 */
public class HandlerHelper {
    private static final String a = handler.a(HandlerHelper.class);
    private static Handler mHandler;
    private Handler handler;
    private HandlerThread mHandlerThread;

    public HandlerHelper() {
        handler.d(a, "init stack:" + a.b());
        mHandler = null;
        this.handler = null;
        this.mHandlerThread = new HandlerThread(HandlerHelper.class.getSimpleName(), 0);
        this.mHandlerThread.start();
    }

    private static Handler getInstance() {
        if(mHandler == null) {
            mHandler = new Handler(Looper.getMainLooper());
        }

        return mHandler;
    }

    public static void postDelayedRunnOnUI(Runnable var0, long delayTime) {
        if(var0 != null) {
            getInstance().postDelayed(var0, delayTime);
        }
    }

    public static void postRunnOnUI(Runnable var0) {
        if(var0 != null) {
            getInstance().post(var0);
        }
    }

    public static void removeCallbacksRunnOnUI(Runnable var0) {
        if(var0 != null) {
            getInstance().removeCallbacks(var0);
        }
    }

    public void postDelayedRunnOnThead(Runnable var1, long delaytime) {
        if(var1 != null) {
            this.getTheadHandler().postDelayed(var1, delaytime);
        }
    }

    public void setHighPriority() {
        if(this.mHandlerThread != null && this.mHandlerThread.isAlive()) {
            int var1 = this.mHandlerThread.getThreadId();

            try {
                if(Process.getThreadPriority(var1) == -8) {
                    handler.d(a, "setHighPriority No Need.");
                    return;
                }

                Process.setThreadPriority(var1, -8);
            } catch (Exception var2) {
                handler.a(a, "thread: " + var1 + " setHighPriority failed");
                return;
            }

            handler.d(a, "thread:" + var1 + " setHighPriority to" + Process.getThreadPriority(var1));
        } else {
            handler.a(a, "setHighPriority failed thread is dead");
        }
    }

    public boolean checkInHighPriority() {
        boolean var1 = true;
        if(this.mHandlerThread != null && this.mHandlerThread.isAlive()) {
            int var2 = this.mHandlerThread.getThreadId();

            try {
                var1 = Process.getThreadPriority(var2) == -8;
            } catch (Exception var3) {
                handler.a(a, "thread:" + var2 + "  check inHighPriority failed");
            }

            return var1;
        } else {
            handler.a(a, "check inHighPriority failed thread is dead");
            return false;
        }
    }

    public void setLowPriority() {
        if(this.mHandlerThread != null && this.mHandlerThread.isAlive()) {
            int var1 = this.mHandlerThread.getThreadId();

            try {
                if(Process.getThreadPriority(var1) == 0) {
                    handler.d(a, "setLowPriority No Need.");
                    return;
                }

                Process.setThreadPriority(var1, 0);
            } catch (Exception var2) {
                handler.a(a, "thread: " + var1 + " setLowPriority failed");
                return;
            }

            handler.d(a, "thread:" + var1 + " setLowPriority to" + Process.getThreadPriority(var1));
        } else {
            handler.a(a, "setLowPriority failed thread is dead");
        }
    }

    public static boolean isMainThread() {
        return Thread.currentThread().getId() != Looper.getMainLooper().getThread().getId();
    }

    public Handler getTheadHandler() {
        if(this.handler == null) {
            this.handler = new Handler(this.mHandlerThread.getLooper());
        }

        return this.handler;
    }

    public final Looper getLooper() {
        return this.mHandlerThread.getLooper();
    }

    public boolean isRunnOnThead() {
        return Thread.currentThread().getId() != this.mHandlerThread.getId();
    }

    public int postRunnOnThead(Runnable var1) {
        if(var1 == null) {
            return -1;
        } else {
            this.getTheadHandler().post(var1);
            return 0;
        }
    }
}
