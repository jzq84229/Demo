package com.zhang.mydemo.wheel;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.zhang.mydemo.BaseActivity;
import com.zhang.mydemo.R;
import com.zhang.mydemo.wheel.widget.WheelView;
import com.zhang.mydemo.wheel.widget.adapter.NumericWheelAdapter;

public class WheelViewActivity extends BaseActivity {
    private WheelView wheelView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContent() {
        setContentView(R.layout.activity_wheel_view);
    }

    @Override
    public void findViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        wheelView = (WheelView) findViewById(R.id.wheelview);
        wheelView.setViewAdapter(new NumericWheelAdapter(getApplicationContext(), 1, 2000));
    }

    @Override
    public void setData() {

    }

    @Override
    public void showContent() {

    }

}
