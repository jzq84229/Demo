package com.zhang.mydemo.animation;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.zhang.mydemo.BaseActivity;
import com.zhang.mydemo.R;

public class WaveActivity extends BaseActivity {
    private WaveView waveView;
    private WaveHelper mWaveHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContent() {
        setContentView(R.layout.activity_wave);
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

        waveView = (WaveView) findViewById(R.id.waveview);
    }

//    setWaveShiftRatio - Shift the wave horizontally.
//    setWaterLevelRatio - Set water level.
//    setAmplitudeRatio - Set vertical size of wave.
//    setWaveLengthRatio - Set horizontal size of wave.
    @Override
    public void setData() {
        waveView.setShapeType(WaveView.ShapeType.SQUARE);
        waveView.setAmplitudeRatio(0.4f);
        waveView.setWaterLevelRatio(0.5f);
//        waveView.setWaveColor(Color.parseColor("#88b8f1ed"), Color.parseColor("#b8f1ed"));
        mWaveHelper = new WaveHelper(waveView);
    }

    @Override
    public void showContent() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mWaveHelper.start();
    }

}
