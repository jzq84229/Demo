package com.zhang.mydemo.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 含手势操作的XCL-Charts图表View基类
 * Created by zjun on 2015/9/10 0010.
 */
public class ChartView extends GraphicalView {
//    private List<ChartTouch> mTouch = new ArrayList<>();
    public ChartView(Context context) {
        super(context);
    }

    public ChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ChartView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void render(Canvas canvas) {

    }


//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        touchEvent(event);
//        return true;
//    }
//
//    /**
//     * 用于绑定需要手势滑动的图表
//     * @param view  视图
//     * @param chart 图表类
//     */
//    public void bindTouch(View view, XChart chart) {
//        mTouch.add(new ChartTouch(this, chart));
//    }
//
//    /**
//     * 用于绑定需要手势滑动的图表，及指定可滑动范围
//     * @param view  视图
//     * @param chart	图表类
//     * @param panRatio 需大于0
//     */
//    public void bindTouch(View view, XChart chart, float panRatio) {
//        mTouch.add(new ChartTouch(this,chart,panRatio));
//    }
//
//    /**
//     * 清空绑定类
//     */
//    public void resetTouchBind() {
//        mTouch.clear();
//    }
//
//    /**
//     * 触发手势操作
//     * @param event
//     * @return
//     */
//    private boolean touchEvent(MotionEvent event) {
//        for (ChartTouch c : mTouch) {
//            c.handleTouch(event);
//        }
//        return true;
//    }
}
