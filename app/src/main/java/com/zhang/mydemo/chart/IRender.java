package com.zhang.mydemo.chart;

import android.graphics.Canvas;

/**
 * 用于绘制的接口
 * Created by zjun on 2015/9/10 0010.
 */
public interface IRender {
    boolean render(Canvas canvas) throws Exception;
}
