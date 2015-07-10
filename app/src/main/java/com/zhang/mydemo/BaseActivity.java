package com.zhang.mydemo;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by zjun on 2015/7/8 0008.
 */
public abstract class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public void init(){
        setContent();
        findViews();
        setData();
        showContent();
    }

    public abstract void setContent();
    public abstract void findViews();
    public abstract void setData();
    public abstract void showContent();
}
