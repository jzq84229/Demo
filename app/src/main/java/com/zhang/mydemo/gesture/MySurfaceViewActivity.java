package com.zhang.mydemo.gesture;

import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zhang.mydemo.R;

public class MySurfaceViewActivity extends AppCompatActivity {
    private MySurfaceView mySurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_surface_view);
    }

    private void init() {
        mySurfaceView = (MySurfaceView) findViewById(R.id.my_surface_view);
        mySurfaceView.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));

    }
}
