package com.zhang.mydemo.chart;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.zhang.mydemo.BaseActivity;
import com.zhang.mydemo.R;
import com.zhang.mydemo.chart.mychart.GrowData;
import com.zhang.mydemo.chart.mychart.MyChartView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChartActivity extends BaseActivity {
    private LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContent() {
        setContentView(R.layout.activity_chart);
    }

    @Override
    public void findViews() {
        lineChart = (LineChart) findViewById(R.id.lineChart);
    }

    @Override
    public void setData() {
        initData();
    }

    @Override
    public void showContent() {

    }

    private void initData() {
        ChartInfo charInfo = new ChartInfo();
        charInfo.setTitle("最近24小时空气质量指数");
        charInfo.setxScaleNum(6);
        charInfo.setyScaleNum(6);
        String[] yScaleLeftLable = new String[6];
        yScaleLeftLable[0] = "50";
        yScaleLeftLable[1] = "100";
        yScaleLeftLable[2] = "150";
        yScaleLeftLable[3] = "200";
        yScaleLeftLable[4] = "300";
        yScaleLeftLable[5] = "500";
        charInfo.setyScaleLeftLable(yScaleLeftLable);
        String[] yScaleRightLable = new String[6];
        yScaleRightLable[0] = "优";
        yScaleRightLable[1] = "良";
        yScaleRightLable[2] = "轻度污染";
        yScaleRightLable[3] = "中度污染";
        yScaleRightLable[4] = "重度污染";
        yScaleRightLable[5] = "严重污染";
        charInfo.setyScaleRightLable(yScaleRightLable);
        String[] xScaleDownLable = new String[7];
        xScaleDownLable[0] = "21:00";
        xScaleDownLable[1] = "01:00";
        xScaleDownLable[2] = "05:00";
        xScaleDownLable[3] = "09:00";
        xScaleDownLable[4] = "13:00";
        xScaleDownLable[5] = "17:00";
        xScaleDownLable[6] = "21:00";
        charInfo.setxScaleDownLable(xScaleDownLable);

        lineChart.setChartInfo(charInfo);

        List<LineInfo> lineInfos = new ArrayList<LineInfo>();

        LineInfo lineInfo1 = new LineInfo();
        lineInfo1.setPointColor(0xFFE5B814);
        lineInfo1.setLineColor(0xFFC8A724);
        lineInfo1.setName("美国领事馆");
        lineInfo1.setPoints(new int[]{570,450,350,250,130,
                170,190,185,177,165,155,150,168,170,190,
                200,210,205,230,220,210,190,180,190,170,
                180});
        lineInfos.add(lineInfo1);

        LineInfo lineInfo2 = new LineInfo();
        lineInfo2.setPointColor(0xFF06D606);
        lineInfo2.setLineColor(0xFF99CC00);
        lineInfo2.setName("静安监测站");
        lineInfo2.setPoints(new int[]{170,150,250,300,170,
                120,130,165,147,265,255,170,268,180,160,
                240,220,215,200,210,240,180,190,200,270,
                190});
        lineInfos.add(lineInfo2);

        lineChart.setLineInfos(lineInfos);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
