package com.zhang.mydemo.framelayout;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.zhang.mydemo.BaseActivity;
import com.zhang.mydemo.R;

public class CoordinatorLayoutDemo extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void setContent() {
        setContentView(R.layout.activity_coordinator_layout_demo);
    }

    @Override
    public void findViews() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("cancel", new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                //这里的单击事件代表点击消除Action后的响应事件

                            }
                        }).show();
            }
        });
    }

    @Override
    public void setData() {

    }

    @Override
    public void showContent() {

    }

}
