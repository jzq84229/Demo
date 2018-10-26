package com.zhang.mypatchlib;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MyPatchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_patch);
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_test) {       //bug测试
            TestClass testClass = new TestClass();
            testClass.testFix(this);
        } else if (i == R.id.btn_fix) { //bug修复
            fixBug();
            Process.killProcess(Process.myPid());
            Toast.makeText(this, "修复成功", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 修复bug
     */
    private void fixBug() {
        //目录:   /data/data/packageName/odex
        File fileDir = getDir("odex", Context.MODE_PRIVATE);
        //往该目录下面放置我们修复好的dex文件
        String name = "patch2.dex";
        String filePath = fileDir.getAbsolutePath() + File.separator + name;
        File file = new File(filePath);
        //若补丁文件存在则删除
        if (file.exists()) {
            file.delete();
        }
        String patchPath = Environment.getExternalStorageDirectory() + File.separator + name;
        File patchFile = new File(patchPath);
        if (patchFile.exists()) {
            //搬家：把下载好的SD卡里面修复的patch2.dex搬到应用目录 filePath
            //实际使用时可以直接下载到fileDir目录下
            InputStream is = null;
            OutputStream os = null;
            try {
                is = new FileInputStream(patchFile);
                os = new FileOutputStream(file);
                int len = 0;
                byte[] buffer = new byte[1024];
                while ((len = is.read(buffer)) != -1) {
                    os.write(buffer, 0, len);
                }
            } catch (FileNotFoundException e) {
                Toast.makeText(this, "补丁文件不存在", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (IOException e) {
                Toast.makeText(this, "修复出错", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                    if (os != null) {
                        os.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Toast.makeText(this, "补丁文件不存在", Toast.LENGTH_SHORT).show();
        }
    }
}
