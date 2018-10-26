package com.zhang.httpreq.excute;

import android.content.Context;

import com.zhang.httpreq.callback.Callback;

import java.util.Map;

/**
 * Created by admin on 2017/8/1.
 */

public interface BaseHttpExcute {

    public void get(Context context, String url, Map<String, String> params, Callback callback);

    public void post(Context context, String url, Map<String, String> params, Callback callback);
}
