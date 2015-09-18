package com.zhang.mydemo.chart.mychart;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.zhang.mydemo.R;
import com.zhang.mydemo.util.ScreenUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by zjun on 2015/9/11 0011.
 */
public class MyChartView extends View {
    private Paint axisPaint = null;         // 坐标线画笔
    private Paint dashPaint = null;         // 月中分隔虚线画笔
    private Paint labelPaint = null;        // 坐标线标签画笔
    private Paint linePaint = null;         // 折线画笔
    private Paint mPaintLineFill = null;    // 折线填充画笔
    private Paint mPaintDotFill = null;     // 坐标点环形填充画笔
    private Paint dotLabelPaint = null;     // 坐标点标签画笔
    private DecimalFormat df = new DecimalFormat("#0.#");
    private static final int DOT_RADIUS = 30;   // 坐标点半径

    private int screenWidth;
    private static final int screenMonthLineNum = 3;    //一屏显示月份数

    //组件高宽(像素)
    private int mWidth, mHeight;

    private int horizontalSpace = 50;
    private int verticalSpace = 50;
    private float xStep = 0;
    private float xPoint = 0;       // 原点X坐标
    private float yWeightPoint = 0; // 体重原点y坐标
    private float yHeightPoint = 0; // 身高原点y坐标

    private List<String> labelList = new ArrayList<>();
    private List<MyPoint> weightList = new ArrayList<>();
    private List<MyPoint> heigthList = new ArrayList<>();
    private List<GrowData> dataList = Collections.emptyList();

    //x与y坐标轴的长度
    private int xLength = 0;
    private int yLength = 0;

    public MyChartView(Context context) {
        super(context);
        init();
    }

    public MyChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyChartView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        screenWidth = ScreenUtil.screenWidthPixels(getContext().getApplicationContext());
        //一屏显示三个月，每个月按31天划分，所以x的刻度为：（屏幕宽度-两边空白宽度）/ 一屏显示月份 = X轴月宽度
        xStep = (screenWidth - horizontalSpace * 2.f) / screenMonthLineNum;
//        // X轴单位刻度 = 月宽度 / 31
//        xDayStep = xStep / 31;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        System.out.println("-----------onMeasure----------");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        System.out.println("-----------onLayout----------");
        mWidth = getWidth();
        mHeight = getHeight();

        // 计算y轴高度
        yLength = (mHeight - verticalSpace * 2) / 2;

        // 计算原点位置
        xPoint = horizontalSpace;
        yWeightPoint = mHeight - verticalSpace;
        yHeightPoint = yWeightPoint - yLength;

        // 初始化：X轴标签、身高、体重数据
        initData();

        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        System.out.println("-----------onDraw----------");

        // 绘制坐标轴
        drawAxisLine(canvas);

        // 绘制身高、体重折线
        drawWeightLineRender(canvas, xPoint, yWeightPoint, weightList);
        drawHeightLineRender(canvas, xPoint, yHeightPoint, heigthList);
    }

    /**
     * 设置成长数据
     * @param list  数据
     */
    public void setData(List<GrowData> list) {
        System.out.println("-----------setData----------");
        this.dataList = list;
    }

    /**
     * 初始化：X轴标签、身高、体重数据
     */
    private void initData() {
        if (dataList.size() > 0) {
            GrowData firstData = dataList.get(0);
            GrowData lastData = dataList.get(dataList.size() - 1);

            labelList = chartLabels(firstData.getCreateAt(), lastData.getCreateAt());

            // 计算X与Y轴长度
            if (labelList.size() > 0) {
                xLength = (int) ((labelList.size() - 1) * xStep);
                // 根据x轴长度重设控件宽度
                setLayoutParams(new LinearLayout.LayoutParams(Math.round(xLength + horizontalSpace * 2), mHeight));
            }

            // 计算身高与体重的Y轴步长
            float yWeightStep = (float) yLength / GrowData.MAX_WEIGHT;  // 体重Y轴步长
            float yHeightStep = (float) yLength / GrowData.MAX_HEIGHT;  // 身高Y轴步长

            weightList.clear();
            heigthList.clear();

            for (GrowData growData : dataList) {
                if (growData.getType() == GrowData.TYPE_WEIGHT) {
                    weightList.add(getMyPoint(growData, xPoint, yWeightPoint, xStep, yWeightStep, firstData.getCreateAt()));
                } else {
                    heigthList.add(getMyPoint(growData, xPoint, yHeightPoint, xStep, yHeightStep, firstData.getCreateAt()));
                }
            }
        }
    }

    /**
     * 计算记录坐标位置
     * @param growData      当前记录
     * @param yStep         Y轴步长
     * @param firstDate     坐标X轴起始日期
     * @return
     */
    private MyPoint getMyPoint(GrowData growData, float xPoint, float yPoint, float monthStep, float yStep, Date firstDate) {
        Calendar c = Calendar.getInstance();
        c.setTime(growData.getCreateAt());
        int month = (growData.getCreateAt().getYear() - firstDate.getYear()) * 12 + (growData.getCreateAt().getMonth() - firstDate.getMonth());
        return new MyPoint(xPoint + (month + (float)c.get(Calendar.DAY_OF_MONTH) / c.getActualMaximum(Calendar.DATE)) * monthStep ,
                yPoint - growData.getValue().floatValue() * yStep,
                growData.getValue().floatValue(),
                DOT_RADIUS);
    }



//    /**
//     * 计算X轴坐标
//     * @param createAt      当前记录日期
//     * @param firstDate     坐标轴起始日期
//     * @return
//     */
//    private float getXPosition(Date createAt, Date firstDate) {
//        int month = (createAt.getYear() - firstDate.getYear()) * 12 + (createAt.getMonth() - firstDate.getMonth());
//        int day = (createAt.getDay() - firstDate.getDay());
//        return month * xStep + day * xStep;
//    }
//
//    /**
//     * 计算Y轴坐标坐标
//     * @param growData      当前记录
//     * @param yStep         Y轴步长
//     * @return
//     */
//    private float getYPosition(GrowData growData, float yStep) {
//        return growData.getWeight() * yStep;
//    }

    /**
     * 组装分隔线标签
     * @param startAt   第一条数据日期
     * @param endAt     最后一条数据日期
     * @return
     */
    private List<String> chartLabels(Date startAt, Date endAt) {
        List<String> list = new ArrayList<>();
        if (startAt != null && endAt != null) {
            int month = (endAt.getYear() - startAt.getYear()) * 12 + (endAt.getMonth() - startAt.getMonth());
            for (int i = 0; i <= month; i++) {
                int m = (startAt.getMonth() + i) % 12;
                list.add((m + 1) + "月");
            }

    //        Calendar c = Calendar.getInstance();
    //        c.setTime(startAt);
    //        list.add((c.get(Calendar.MONTH) + 1) + "月");
    //        while (c.getTime().before(endAt)) {
    //            c.set(Calendar.MONTH, c.get(Calendar.MONTH) + 1);
    //            list.add(c.get(Calendar.MONTH ) + 1 + "月");
    //        }

            // 判断最后一条数据是否是月初1号的记录，若大于1号则添加下个月的分隔线
            // 如：最后一条是9月2日创建的，则要画出10月份的分割线
            Calendar c2 = Calendar.getInstance();
            c2.setTime(endAt);
            if (c2.get(Calendar.DAY_OF_MONTH) > 1) {
                c2.set(Calendar.MONTH, c2.get(Calendar.MONTH) + 1);
                list.add((c2.get(Calendar.MONTH) + 1) + "月");
            }

            // 若分割线小于最少条数，则要添满最少分割线
            if (list.size() < screenMonthLineNum) {
                int s = screenMonthLineNum - list.size();
                for (int i = 0; i < s; i++) {
                    c2.set(Calendar.MONTH, c2.get(Calendar.MONTH) + 1);
                    list.add((c2.get(Calendar.MONTH) + 1) + "月");
                }
            }
        }
        return list;
    }



    /**
     * 画坐标轴
     * @param canvas 画布
     */
    private void drawAxisLine(Canvas canvas) {
        if (labelList != null && labelList.size() > 0) {
            // 画X轴
            canvas.drawLine(xPoint, yWeightPoint, xPoint + xLength, yWeightPoint, getAxisPaint());
            // 画Y轴及月份分隔线
            for (int i = 0; i < labelList.size(); i++) {
                // x轴分割点
                float xPo = xPoint + i * xStep;
                // 画月份分隔线
                canvas.drawLine(xPo, yWeightPoint, xPo, yWeightPoint - yLength * 2, getAxisPaint());
                // 画月份标签
                canvas.drawText(labelList.get(i), xPo, yWeightPoint + 30, getLabelPaint());

                // 画月中分割线
                if (i != labelList.size() - 1) {
                    Path path = new Path();
                    path.moveTo(xPo + xStep / 2, yWeightPoint);
                    path.lineTo(xPo + xStep / 2, yWeightPoint - yLength * 2);
                    canvas.drawPath(path, getDashPaint());
                }
            }
        }
    }

    /**
     * 绘制体重折线
     */
    private void drawWeightLineRender(Canvas canvas, float xPoint, float yPoint, List<MyPoint> data) {
        if (!data.isEmpty()) {
            Paint linePaint = getLinePaint();
            linePaint.setColor(getResources().getColor(R.color.weight_line_color));
            Paint lineFillPaint = getLineInnerFillPaint();
            lineFillPaint.setColor(getResources().getColor(R.color.weight_line_fill_color));
            drawLineRender(canvas, linePaint, lineFillPaint, data, xPoint, yPoint);
        }
    }

    /**
     * 绘制身高折线
     */
    private void drawHeightLineRender(Canvas canvas, float xPoint, float yPoint, List<MyPoint> data) {
        if (!data.isEmpty()) {
            Paint linePaint = getLinePaint();
            linePaint.setColor(getResources().getColor(R.color.height_line_color));
            Paint lineFillPaint = getLineInnerFillPaint();
            lineFillPaint.setColor(getResources().getColor(R.color.height_line_fill_color));
            drawLineRender(canvas, linePaint, lineFillPaint, data, xPoint, yPoint);
        }
    }

    /**
     * 画折线
     * @param canvas
     */
    private void drawLineRender(Canvas canvas, Paint linePaint, Paint lineFillPaint, List<MyPoint> data, float xPoint, float yPoint){
        if (data.size() > 1) {
            Path path = new Path();
            Path path2 = new Path();
            path.moveTo(xPoint, data.get(0).getY());
            path2.moveTo(xPoint, yPoint);
            path2.lineTo(xPoint, data.get(0).getY());
            for (int i = 0; i < data.size(); i++) {
                path.lineTo(data.get(i).getX(), data.get(i).getY());
                path2.lineTo(data.get(i).getX(), data.get(i).getY());
            }
            path.lineTo(xPoint + xLength, data.get(data.size() - 1).getY());
            path2.lineTo(xPoint + xLength, data.get(data.size() - 1).getY());
            path2.lineTo(xPoint + xLength, yPoint);
            canvas.drawPath(path, linePaint);
            canvas.drawPath(path2, lineFillPaint);
            drawDot(canvas, linePaint.getColor(), data, xPoint, yPoint);
        }
    }

    /**
     * 绘制坐标点
     * @param canvas
     * @param color
     * @param data
     * @param xPoint
     * @param yPoint
     */
    private void drawDot(Canvas canvas, int color, List<MyPoint> data, float xPoint, float yPoint) {
        for (int i = 0; i < data.size(); i++) {
            renderRing(canvas, getDotLabelPaint(color), DOT_RADIUS, xPoint + data.get(i).getX(), yPoint - data.get(i).getY(), df.format(data.get(i).getValue()));
        }
    }

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
        float ringRadius = radius * 0.9f; // MathHelper.getInstance().mul(radius, 0.7f);
        canvas.drawCircle(cX, cy, radius, paint);

        canvas.drawCircle(cX, cy, ringRadius, getDotInnerFillPaint());

        canvas.drawText(value, cX, cy + 5, paint);
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
     * 标签画笔
     * @return  画笔
     */
    private Paint getLabelPaint() {
        if (labelPaint == null) {
            labelPaint = new Paint();
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
            linePaint.setColor(Color.BLUE);
            linePaint.setStyle(Paint.Style.STROKE);
            linePaint.setAntiAlias(true);
            linePaint.setStrokeWidth(3);
        }
        return linePaint;
    }

    /**
     * 填充内部环形的画笔
     */
    private Paint getDotInnerFillPaint() {
        if(mPaintDotFill == null) {
            mPaintDotFill = new Paint();
            mPaintDotFill.setColor(Color.WHITE);
            mPaintDotFill.setStyle(Paint.Style.FILL);
            mPaintDotFill.setAntiAlias(true);
        }
        return mPaintDotFill;
    }

    /**
     * 坐标点标签画笔
     * @return
     */
    private Paint getDotLabelPaint(int color) {
        if (dotLabelPaint == null) {
            dotLabelPaint = new Paint();
            dotLabelPaint.setColor(getResources().getColor(R.color.weight_line_color));
            dotLabelPaint.setAntiAlias(true);
            dotLabelPaint.setTextAlign(Paint.Align.CENTER);
            dotLabelPaint.setTextSize(20);
        }
        dotLabelPaint.setColor(color);
        return dotLabelPaint;
    }

    /**
     * 折线内部填充画笔
     * @return 画笔
     */
    private Paint getLineInnerFillPaint() {
        if (mPaintLineFill == null) {
            mPaintLineFill = new Paint();
            mPaintLineFill.setColor(Color.BLUE);
            mPaintLineFill.setStyle(Paint.Style.FILL);
            mPaintLineFill.setAntiAlias(true);
        }
        return mPaintLineFill;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                System.out.println("--------ACTION_DOWN:" + x + "------" + y);
                break;
            case MotionEvent.ACTION_UP:
                System.out.println("--------ACTION_UP:" + x + "--------" + y);
                break;
        }
        return true;
    }

    /**
     * 触摸屏幕时判断是否在可点击区域
     * @param x		触摸点X坐标
     * @param y		触摸点Y坐标
     * @return
     */
    private MyPoint checkClickable(float x, float y){
        if (!weightList.isEmpty()) {
            for (int i = weightList.size() - 1; i >= 0; i--) {
                MyPoint myPoint = weightList.get(i);
                RectF rectF = myPoint.getClickableArea();
                if (rectF != null && rectF.contains(x, y)) {
                    return myPoint;
                }
            }
        }

        if (!heigthList.isEmpty()) {
            for (int i = heigthList.size() - 1; i >= 0; i--) {
                MyPoint myPoint = heigthList.get(i);
                RectF rectF = myPoint.getClickableArea();
                if (rectF != null && rectF.contains(x, y)) {
                    return myPoint;
                }
            }
        }
        return null;
    }
}
