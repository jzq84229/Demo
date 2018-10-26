package com.zhang.imagepicker.util;

import android.content.Context;

/**
 * 用于解决provider冲突的util
 * Created by admin on 2017/7/27.
 */

public class ProviderUtil {

    public static String getFileProviderName(Context context) {
        return context.getPackageName() + ".provider";
    }
}
