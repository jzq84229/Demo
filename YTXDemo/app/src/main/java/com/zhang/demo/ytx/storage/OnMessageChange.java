package com.zhang.demo.ytx.storage;

/**
 * Created by Administrator on 2016/7/12.
 */
public interface OnMessageChange {
    /** 数据库改变 */
    void onChanged(String sessionId);
}
