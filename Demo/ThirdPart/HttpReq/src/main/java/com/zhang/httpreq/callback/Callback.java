package com.zhang.httpreq.callback;

/**
 * Created by admin on 2017/8/1.
 * 抽象的回调接口
 */

public interface Callback {

    void onSuccess(int statusCode, String response);

    void onError(int statusCode, String responseString, Throwable throwable);
}
