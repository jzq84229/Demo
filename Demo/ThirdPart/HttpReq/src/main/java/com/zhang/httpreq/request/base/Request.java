package com.zhang.httpreq.request.base;

/**
 * 所有请求的基类，其中泛型 R 主要用于属性设置方法后，返回对应的子类型，以便于实现链式调用
 * Created by admin on 2017/8/1.
 */

public class Request {
    protected String url;
    protected String baseUrl;

    public Request(String url) {
        this.url = url;
        this.baseUrl = url;
    }
}
