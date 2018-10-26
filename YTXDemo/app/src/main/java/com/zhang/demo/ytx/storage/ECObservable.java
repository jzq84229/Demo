package com.zhang.demo.ytx.storage;

import java.util.ArrayList;

/**
 * 观察者管理容器
 * Created by Administrator on 2016/7/21.
 */
public abstract class ECObservable<T> {
    protected final ArrayList<T> mObservers = new ArrayList<>();

    /**
     * 注册观察者
     * @param observer
     */
    public void registerObserver(T observer) {
        if (observer == null) {
            throw new IllegalArgumentException("The observer is null");
        }
        synchronized (mObservers) {
            if (mObservers.contains(observer)) {
                throw new IllegalStateException("ECObservable " + observer + " is already registered.");
            }
            mObservers.add(observer);
        }
    }

    /**
     * 移除观察着
     * @param observer
     */
    public void unregisterObserver(T observer) {
        if (observer == null) {
            throw new IllegalArgumentException("The observer is null");
        }
        synchronized (mObservers) {
            int index = mObservers.indexOf(observer);
            if (index == -1) {
                return;
            }
            mObservers.remove(index);
        }
    }

    /**
     * 移除所有观察者
     */
    public void unregisterAll() {
        synchronized (mObservers) {
            mObservers.clear();
        }
    }
}
