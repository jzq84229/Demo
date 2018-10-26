package com.zhang.mydemo.chart.mychart;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.zhang.mydemo.BaseActivity;
import com.zhang.mydemo.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MyChartActivity extends BaseActivity {
    private MyChartView chartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContent() {
        setContentView(R.layout.activity_my_chart);
    }

    @Override
    public void findViews() {
        chartView = (MyChartView) findViewById(R.id.chart_view);
    }

    @Override
    public void setData() {
        chartView.setData(initData());
    }

    @Override
    public void showContent() {

    }

    /**
     * 数据初始化
     */
    private List<GrowData> initData() {
        List<GrowData> list = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        for (int i = 0; i < 11; i++) {
            c.set(2015, i, 20);
            GrowData gd = new GrowData();
            if (i % 2 == 0) {
                gd.setType(GrowData.TYPE_WEIGHT);
                gd.setValue(10.7d + i * 5);
                gd.setCreateAt(c.getTime());
            } else {
                gd.setType(GrowData.TYPE_HEIGHT);
                gd.setValue(20d + i * 10);
                gd.setCreateAt(c.getTime());
            }
            list.add(gd);
        }
        return list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_chart, menu);
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
