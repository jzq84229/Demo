package com.zhang.common.lib.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池管理类
 * Created by jun on 2019/2/27.
 */

public class ThreadPoolManage {
    public static final String DEFAULT_SINGLE_POOL_NAME = "DEFAULT_SINGLE_POOL_NAME";
    private static ThreadPoolManage.ThreadPool mThreadPool = null;
    private static Object mThreadLock = new Object();
    private static ThreadPoolManage.ThreadPool mLongPool = null;
    private static Object mLongLock = new Object();
    private static ThreadPoolManage.ThreadPool mShortPool = null;
    private static Object mShortLock = new Object();
    private static ThreadPoolManage.ThreadPool mDownloadPool = null;
    private static Object mDownloadLock = new Object();
    private static Map<String, ThreadPool> mMap = new HashMap();
    private static Object mSingleLock = new Object();

    public ThreadPoolManage() {
    }

    public static ThreadPoolManage.ThreadPool getThreadPoolProxy(int corePoolSize, int maximumPoolSize, long keepAliveTime) {
        if(null == mThreadPool) {
            Object var4 = mThreadLock;
            synchronized(mThreadLock) {
                if(mThreadPool == null) {
                    mThreadPool = new ThreadPoolManage.ThreadPool(corePoolSize, maximumPoolSize, keepAliveTime);
                }
            }
        }

        return mThreadPool;
    }

    public static ThreadPoolManage.ThreadPool getDownloadPool() {
        return ThreadPoolManage.ThreadPoolHolder.mDownloadPool;
    }

    public static ThreadPoolManage.ThreadPool getLongPool() {
        return ThreadPoolManage.ThreadPoolHolder.mLongPool;
    }

    public static ThreadPoolManage.ThreadPool getShortPool() {
        return ThreadPoolManage.ThreadPoolHolder.mShortPool;
    }

    public static ThreadPoolManage.ThreadPool getSinglePool() {
        return getSinglePool("DEFAULT_SINGLE_POOL_NAME");
    }

    public static ThreadPoolManage.ThreadPool getSinglePool(String name) {
        Object var1 = mSingleLock;
        synchronized(mSingleLock) {
            ThreadPoolManage.ThreadPool singlePool = (ThreadPoolManage.ThreadPool)mMap.get(name);
            if(singlePool == null) {
                singlePool = ThreadPoolManage.ThreadPoolHolder.mSinglePool;
                mMap.put(name, singlePool);
            }

            return singlePool;
        }
    }

    public static class ThreadPool {
        private ThreadPoolExecutor mPool;
        private int mCorePoolSize;
        private int mMaximumPoolSize;
        private long mKeepAliveTime;

        private ThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime) {
            this.mCorePoolSize = corePoolSize;
            this.mMaximumPoolSize = maximumPoolSize;
            this.mKeepAliveTime = keepAliveTime;
        }

        public synchronized void execute(Runnable run) {
            if(run != null) {
                if(this.mPool == null || this.mPool.isShutdown()) {
                    this.mPool = new ThreadPoolExecutor(this.mCorePoolSize, this.mMaximumPoolSize, this.mKeepAliveTime, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
                }

                this.mPool.execute(run);
            }
        }

        public synchronized void cancel(Runnable run) {
            if(this.mPool != null && (!this.mPool.isShutdown() || this.mPool.isTerminating())) {
                this.mPool.getQueue().remove(run);
            }

        }

        public synchronized boolean contains(Runnable run) {
            return this.mPool == null || this.mPool.isShutdown() && !this.mPool.isTerminating()?false:this.mPool.getQueue().contains(run);
        }

        public void stop() {
            if(this.mPool != null && (!this.mPool.isShutdown() || this.mPool.isTerminating())) {
                this.mPool.shutdownNow();
            }

        }

        public synchronized void shutdown() {
            if(this.mPool != null && (!this.mPool.isShutdown() || this.mPool.isTerminating())) {
                this.mPool.shutdownNow();
            }

        }
    }

    public static class ThreadPoolHolder {
        private static final ThreadPoolManage.ThreadPool mDownloadPool = new ThreadPoolManage.ThreadPool(3, 3, 5L);
        private static final ThreadPoolManage.ThreadPool mLongPool = new ThreadPoolManage.ThreadPool(5, 5, 5L);
        private static final ThreadPoolManage.ThreadPool mShortPool = new ThreadPoolManage.ThreadPool(2, 5, 5L);
        private static final ThreadPoolManage.ThreadPool mSinglePool = new ThreadPoolManage.ThreadPool(1, 1, 5L);

        public ThreadPoolHolder() {
        }
    }
}