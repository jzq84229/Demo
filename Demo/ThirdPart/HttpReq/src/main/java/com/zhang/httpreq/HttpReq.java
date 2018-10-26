package com.zhang.httpreq;

import android.app.Application;

/**
 * Created by admin on 2017/8/1.
 */

public class HttpReq {
    private Application context;

    private HttpReq() {

    }

    public static HttpReq getInstance() {
        return HttpReqHolder.hoder;
    }

    private static class HttpReqHolder{
        private static HttpReq hoder = new HttpReq();
    }

    /** 必须在全局Application先调用，获取context上下文，否则缓存无法使用 */
    public HttpReq init(Application app) {
        this.context = app;
        return this;
    }

    /** get请求 */
    public void get(String url){

    }

    /** post请求 */
    public void post(String url){

    }

}
