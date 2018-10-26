package com.zhang.demo.web;

import android.app.Application;

import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsDownloader;

/**
 * Created by Administrator on 2016/7/28.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        initQbSdk();
    }

    /**
     * 初始化腾讯X5浏览器
     */
    private void initQbSdk() {
        QbSdk.allowThirdPartyAppDownload(true);
        TbsDownloader.needDownload(getApplicationContext(), false);
    }
}
