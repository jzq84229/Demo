package com.zhang.demo.mvp;

/**
 * data 层接口定义
 * Created by Administrator on 2016/8/1.
 */
public interface TaskDataSource {
    String getStringFromRemote();

    String getStringFromCache();
}
