package com.zhang.demo;

import android.app.Application;
import android.content.Context;

import com.zhang.mypatchlib.FixDexUtils;
import com.zhang.sophixlib.SophixHelper;

/**
 * Created by admin on 2017/7/25.
 */

public class App extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        SophixHelper.getInstance().initSophix(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        FixDexUtils.loadFixedDex(this);
    }

}
