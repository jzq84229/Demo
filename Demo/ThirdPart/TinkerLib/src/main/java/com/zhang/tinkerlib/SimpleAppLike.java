package com.zhang.tinkerlib;

import android.app.Application;
import android.content.Intent;

import com.tencent.tinker.loader.app.DefaultApplicationLike;

/**
 * Created by admin on 2017/8/18.
 */

public class SimpleAppLike extends DefaultApplicationLike {
    public SimpleAppLike(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag,
                         long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag,
                applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
    }
}
