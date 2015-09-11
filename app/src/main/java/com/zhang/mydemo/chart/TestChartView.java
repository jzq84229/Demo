package com.zhang.mydemo.chart;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import com.zhang.mydemo.util.ScreenUtil;

/**
 * Created by zjun on 2015/9/11 0011.
 */
public class TestChartView extends View {
    private Paint axisPaint = null;     // 坐标线画笔
    private Paint dashPaint = null;     // 月中分隔虚线画笔
    private Paint labelPaint = null;    // 坐标线标签画笔
    private Paint linePaint = null;     // 折线画笔
    private Paint mPaintFill = null;    // 坐标点环形填充画笔

    private int screenWidth;
    private int monthWidth;
    //组件高宽(像素)
    private int mWidth, mHeight;

    private int horizontalSpace = 50;
    private int verticalSpace = 50;
    private int xStep = 0;
    private int yStep = 0;
    private float xPoint = 0; // 原点X坐标
    private float yPoint = 0; // 原点y坐标

    private String[] xLabels;

    //x与y坐标轴的长度
    private int xLength = 0;
    private int yLength = 0;

    private int mMinVelocity;
    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;
    public static final Paint LABEL_PAINT = new Paint();

    public TestChartView(Context context) {
        super(context);
        init();
    }

    public TestChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TestChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TestChartView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        mWidth = getWidth();
        mHeight = getHeight();
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 计算X与Y轴长度
        xLength = xLabels.length * (monthWidth - 1);
        yLength = mHeight - verticalSpace * 2;

        // 计算原点位置
        xPoint = horizontalSpace;
        yPoint = mHeight - verticalSpace;

        chartAxisLine(canvas);
    }

    private void init() {
        screenWidth = ScreenUtil.screenWidthPixels(getContext().getApplicationContext());
        //一屏显示三个月，每个月按31天划分，所以x的刻度为：（屏幕宽度-两边空白宽度）/ 3 = X轴月宽度
        monthWidth = (screenWidth - horizontalSpace * 2) / 3;
        // X轴单位刻度 = 月宽度 / 31
        xStep = monthWidth / 31;

        mScroller = new Scroller(getContext());
        mMinVelocity = ViewConfiguration.get(getContext()).getScaledMinimumFlingVelocity();

        xLabels = chartLabels();
        System.out.println("----------width:" + (monthWidth * (xLabels.length - 1) + horizontalSpace * 2));
    }

    private String[] chartLabels() {
        return new String[]{"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"};
    }

    private void chartPointData() {

    }

    /**
     * 画坐标轴
     * @param canvas 画布
     */
    private void chartAxisLine(Canvas canvas) {
        if (xLabels != null && xLabels.length > 0) {
            // 画X轴
            canvas.drawLine(xPoint, yPoint, xPoint + xLength, yPoint, getAxisPaint());
            // 画Y轴及月份分隔线
            for (int i = 0; i < xLabels.length; i++) {
                // x轴分割点
                float xPo = xPoint + i * monthWidth;
                // 画月份分隔线
                canvas.drawLine(xPo, yPoint, xPo, yPoint - yLength, getAxisPaint());
                // 画月份标签
                canvas.drawText(xLabels[i], xPo, yPoint + 30, getLabelPaint());

                // 画月中分割线
                if (i != xLabels.length - 1) {
                    Path path = new Path();
                    path.moveTo(xPo + monthWidth / 2, yPoint);
                    path.lineTo(xPo + monthWidth / 2, yPoint - yLength);
                    canvas.drawPath(path, getDashPaint());
                }
            }
        }
    }

    /**
     * 画折线
     * @param canvas
     */
//    private void chartLineRender(Canvas canvas, Paint paint){
//        paint.setColor(Color.RED);
//        paint.setStrokeWidth(5);
//
//        Paint paint2 = new Paint();
//        paint2.setColor(Color.BLUE);
//        paint2.setStyle(Paint.Style.FILL);
//        if (data.size() > 1) {
//            Path path = new Path();
//            Path path2 = new Path();
//            path.moveTo(xPoint, yPoint - data.get(0) * yScale);
//            path2.moveTo(xPoint, yPoint);
//            for (int i = 0; i < data.size(); i++) {
//                path.lineTo(xPoint + i * xScale, yPoint - data.get(i) * yScale);
//                path2.lineTo(xPoint + i * xScale, yPoint - data.get(i) * yScale);
//            }
//            path2.lineTo(xPoint + (data.size() - 1) * xScale, yPoint);
//            canvas.drawPath(path, paint);
////            canvas.drawPath(path2, paint2);
//        }
//    }

    /**
     * 绘制坐标点
     * @param canvas 画布
     * @param paint 画笔
     * @param radius 半径
     * @param cX x坐标
     * @param cy y坐标
     * @param value 坐标标签
     */
    private void renderRing(Canvas canvas, Paint paint, float radius, float cX ,float cy, String value) {
        float ringRadius = radius * 0.7f; // MathHelper.getInstance().mul(radius, 0.7f);
        canvas.drawCircle(cX, cy, radius, paint);

        canvas.drawCircle(cX, cy, ringRadius, getInnerFillPaint());

        canvas.drawText(value, cX - 10, cy + 5, paint);
    }

    /**
     * 坐标线画笔
     * @return 画笔
     */
    private Paint getAxisPaint() {
        if (axisPaint == null) {
            axisPaint = new Paint();
            axisPaint.setColor(Color.BLUE);
            axisPaint.setAntiAlias(true);
            axisPaint.setStyle(Paint.Style.STROKE);
        }
        return axisPaint;
    }

    /**
     * 坐标线标签画笔
     * @return  画笔
     */
    private Paint getLabelPaint() {
        if (labelPaint == null) {
            labelPaint.setTextSize(30);
            labelPaint.setTextAlign(Paint.Align.CENTER);
            labelPaint.setColor(Color.BLACK);
        }
        return labelPaint;
    }

    /**
     * 月中虚线画笔
     * @return 画笔
     */
    private Paint getDashPaint() {
        if (dashPaint == null) {
            DashPathEffect pathEffect = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
            dashPaint = new Paint();
            dashPaint.setColor(Color.BLUE);
            dashPaint.setAntiAlias(true);
            dashPaint.setStyle(Paint.Style.STROKE);
            dashPaint.setPathEffect(pathEffect);
        }
        return dashPaint;
    }

    /**
     * 折线画笔
     * @return
     */
    private Paint getLinePaint() {
        if (linePaint == null) {
            linePaint = new Paint();
            mPaintFill.setColor(Color.BLUE);
            mPaintFill.setStyle(Paint.Style.STROKE);
            mPaintFill.setAntiAlias(true);
        }
        return linePaint;
    }

    /**
     * 填充内部环形的画笔
     */
    private Paint getInnerFillPaint() {
        if(null == mPaintFill) {
            mPaintFill = new Paint();
            mPaintFill.setColor(Color.WHITE);
            mPaintFill.setStyle(Paint.Style.FILL);
            mPaintFill.setAntiAlias(true);
        }
        return mPaintFill;
    }








    /**
     * 设置X轴标签数据
     * @param labels 标签数据
     */
    public void setXLabels(String[] labels) {
        this.xLabels = labels;
    }


}
