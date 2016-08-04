package com.zhang.demo.mvp;

/**
 * 从数据层获取的数据，在这里进行拼装和组合
 * Created by Administrator on 2016/8/1.
 */
public class TaskManager {
    TaskDataSource dataSource;

    public TaskManager(TaskDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String getShowContent() {
        return dataSource.getStringFromRemote() + dataSource.getStringFromCache();
    }
}
