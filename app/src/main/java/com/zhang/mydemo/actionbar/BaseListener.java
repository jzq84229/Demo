package com.zhang.mydemo.actionbar;

import android.content.Context;

/**
 * Created by zjun on 2015/6/16.
 */
public class BaseListener {
    protected IReportBack mReportTo;
    protected Context mContext;
    public BaseListener(Context ctx, IReportBack target){
        mReportTo = target;
        mContext = ctx;
    }
}
