package com.zhang.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zhang.mypatchlib.MyPatchActivity;
import com.zhang.sophixlib.SophixMainActivity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_image_picker:     //图片多选:

                break;
            case R.id.btn_sophix:           //阿里sophix修复
                startActivity(new Intent(this, SophixMainActivity.class));
                break;
            case R.id.btn_my_patch_fix:     //myPatchFix修复
                startActivity(new Intent(this, MyPatchActivity.class));
                break;
        }
    }
}
