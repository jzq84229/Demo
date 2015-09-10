package com.zhang.mydemo.chart;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.zhang.mydemo.util.DebugUtils;
import com.zhang.mydemo.util.SysinfoHelper;

/**
 * Charts图表的View基类
 * Created by zjun on 2015/9/10 0010.
 */
public abstract class GraphicalView extends View {
    private String TAG = "GraphicalView";

    public GraphicalView(Context context) {
        super(context);
        initChartView();
    }

    public GraphicalView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initChartView();
    }

    public GraphicalView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initChartView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GraphicalView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initChartView();
    }

    protected void initChartView() {
        //禁用硬件加速
        disableHardwareAccelerated();
    }

    /**
     * 刷新图表
     */
    public void refreshChart() {
        this.invalidate();
    }

    /**
     * 绘制图表
     * @param canvas 画布
     */
    public abstract void render(Canvas canvas);

    @Override
    protected void onDraw(Canvas canvas) {
        try {
            render(canvas);
        } catch (Exception e) {
            DebugUtils.printLogE(TAG, e.toString());
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureWidth(widthMeasureSpec),
                measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int measureSpec) {
        int result = 100;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) { //fill_parent
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) { //wrap_content
            result = Math.min(result, specSize);
        }
        return result;
    }

    private int measureHeight(int measureSpec) {
        int result = 100;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) { //fill_parent
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) { //wrap_content
            result = Math.min(result, specSize);
        }
        return result;
    }

    /**
     * 禁用硬件加速.
     * 原因:android自3.0引入了硬件加速，即使用GPU进行绘图,但它并不能完善的支持所有的绘图，
     * 通常表现为内容(如Rect或Path)不可见，异常或渲染错误。所以类了保证图表的正常显示，强制禁用掉.
     */
    private void disableHardwareAccelerated() {
        if (SysinfoHelper.getInstance().supportHardwareAccelerated()) {
            //是否开启了硬件加速,如开启将其禁掉
            if (!isHardwareAccelerated()) {
                setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
        }
    }
}
