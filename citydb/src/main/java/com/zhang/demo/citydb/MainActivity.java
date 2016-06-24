package com.zhang.demo.citydb;

import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.zhang.demo.citydb.db.DB;
import com.zhang.demo.citydb.db.DatabaseHelper;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private SQLiteDatabase db;
    private MyHanlder myHanlder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myHanlder = new MyHanlder(this);
        findViewById(R.id.btn_insert).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_insert:
                insertData();
                break;
        }
    }

    private void insertData() {
        DatabaseHelper mDatabaseHelper = new DatabaseHelper(this, DB.DATABASE_NAME, null, DB.VERSION);
        db = mDatabaseHelper.getWritableDatabase();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream inputStream = getAssets().open("city.sql");
                    BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
//                    StringBuilder total = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        System.out.println("=====" + line);
                        db.execSQL(line);
//                        total.append(line).append('\n');
                    }
                    inputStream.close();
                    r.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (db != null) {
                        db.close();
                    }
                    System.out.println("=====数据库操作完成！！！！！！！！");
                    myHanlder.sendEmptyMessage(INSERT_DATA_FINISH);
                }
            }
        }).start();
    }

    private void insertDataFinish() {
        Toast.makeText(MainActivity.this, "数据库操作完成", Toast.LENGTH_SHORT).show();
    }
    
    private static final int INSERT_DATA_FINISH = 1;
    private class MyHanlder extends Handler{
        WeakReference<MainActivity> reference;
        public MyHanlder(MainActivity activity) {
            reference = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = reference.get();
            if (activity != null) {
                switch (msg.what) {
                    case INSERT_DATA_FINISH:
                        activity.insertDataFinish();
                        break;
                }
            }
        }
    }
}
