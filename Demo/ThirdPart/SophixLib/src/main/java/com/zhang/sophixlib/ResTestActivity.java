package com.zhang.sophixlib;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 资源修复测试
 */
public class ResTestActivity extends AppCompatActivity {
    private TextView tv;
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res_test);

        initViews();
        initData();

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle("资源修复测试界面");
    }

    private void initViews() {
        tv = (TextView) findViewById(R.id.tv);
        iv = (ImageView) findViewById(R.id.iv);
    }

    private void initData() {
        tv.setText(R.string.tv_value);
        tv.setTextColor(getResources().getColor(R.color.colorBlack));

        iv.setImageResource(R.mipmap.old);
    }

    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.iv) {
        } else if (i == R.id.btn) {
        } else {
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
