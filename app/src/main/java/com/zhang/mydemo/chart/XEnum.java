package com.zhang.mydemo.chart;

/**
 * 枚举定义
 * Created by zjun on 2015/9/9 0009.
 */
public class XEnum {
    /**
     * 线的几种显示风格:Solid、Dot、Dash
     */
    public enum LineStyle {
        SOLID, DOT, DASH
    }

    /**
     * 点的类型，隐藏，三角形,方形,实心圆,空心圆,棱形
     * HIDE	隐藏，不显示点
     * TRIANGLE	三角形
     * RECT	方形
     * DOT	圆点
     * RING	圆环
     * PRISMATIC	棱形
     */
    public enum DotStyle {
        HIDE, TRIANGLE, RECT, DOT, RING, PRISMATIC, X, CROSS
    }
}
