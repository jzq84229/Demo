package com.zhang.demo.mvp;

/**
 * Created by Administrator on 2016/8/1.
 */
public class TaskDataSourceTestImpl implements TaskDataSource {
    @Override
    public String getStringFromRemote() {
        return "Hello ";
    }

    @Override
    public String getStringFromCache() {
        return "World Test";
    }
}
