package com.zhang.demo.mvp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements MainView {
    private TextView mShowText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mShowText = (TextView) findViewById(R.id.text);
        loadDatas();
    }

    public void loadDatas() {

    }

    @Override
    public void onShowString(String json) {

    }
}
