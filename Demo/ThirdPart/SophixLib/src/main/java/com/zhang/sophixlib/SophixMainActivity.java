package com.zhang.sophixlib;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.taobao.sophix.SophixManager;

/**
 * 阿里Sophix补丁修复测试
 */
public class SophixMainActivity extends AppCompatActivity {
    private static final int REQUEST_EXTERNAL_STORAGE_PERMISSION = 0;
    private TextView mStatusTv;
    private String mStatusStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sophix_main);

        initViews();
        updateConsole(SophixHelper.cacheMsg.toString());

        if (Build.VERSION.SDK_INT >= 23) {
            requestExternalStoragePermission();
        }
    }

    private void initViews() {
        mStatusTv = (TextView) findViewById(R.id.tv_status);
    }

    private void requestExternalStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_EXTERNAL_STORAGE_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE_PERMISSION:
                if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    updateConsole("local external storage patch is invalid as not read external storage permission");
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 更新监控台的输出信息
     *
     * @param content 更新内容
     */
    private void updateConsole(String content) {
        mStatusStr += content + "\n";
        if (mStatusTv != null) {
            mStatusTv.setText(mStatusStr);
        }
    }

    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_restest) {
            startActivity(new Intent(this, ResTestActivity.class));

        } else if (i == R.id.btn_download) {
            SophixManager.getInstance().queryAndLoadNewPatch();

        } else if (i == R.id.btn_test) {
            DexFixDemo.test_normal(this);
            DexFixDemo.test_annotation();
            DexFixDemo.test_addField();
            DexFixDemo.test_addMethod();

        } else if (i == R.id.btn_sotest) {
            SOFixDemo.test(this);

        } else if (i == R.id.btn_kill) {
            SophixManager.getInstance().killProcessSafely();

        } else if (i == R.id.btn_clean_patch) {
            SophixManager.getInstance().cleanPatches();

        } else if (i == R.id.btn_clean_console) {
            mStatusStr = "";
            updateConsole("");

        } else {
        }
    }
}
