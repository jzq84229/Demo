package com.zhang.mypatchlib;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by admin on 2017/8/23.
 */

public class TestClass {

    public void testFix(Context context) {
        int a = 0;
        int b = 10;
        Toast.makeText(context, "热补丁修复：" + b / a, Toast.LENGTH_SHORT).show();
    }
}
