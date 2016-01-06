package com.zhang.mydemo.video;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.zhang.mydemo.BaseActivity;
import com.zhang.mydemo.R;

import java.io.File;

public class VideoRecordActivity extends BaseActivity implements View.OnClickListener{
    private Button btnRecord;
    private static final int VIDEO_RECORD = 0;  //视频录制

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContent() {
        setContentView(R.layout.activity_video_record);
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

        btnRecord = (Button) findViewById(R.id.btn_record);
        btnRecord.setOnClickListener(this);
    }

    @Override
    public void setData() {

    }

    @Override
    public void showContent() {

    }

    @Override
    public void onClick(View v) {
//        String FILE_PATH = Environment.getExternalStorageDirectory().getPath();
//        String fileName =
        switch (v.getId()) {
            case R.id.btn_record:
                Intent intent = new Intent();
                // 指定开启系统相机的Action
                intent.setAction(MediaStore.ACTION_VIDEO_CAPTURE);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
//                // 根据文件地址创建文件
//                File file = new File(FILE_PATH + fileName);
//                // 把文件地址转换成Uri格式
//                Uri uri = Uri.fromFile(file);
//                // 设置系统相机拍摄照片完成后图片文件的存放地址
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                //在这里的QUALITY参数，值为两个，一个是0，一个是1，代表录制视频的清晰程度，0最不清楚，1最清楚
                //没有0-1的中间值，另外，使用1也是比较占内存的，测试了一下，录制1分钟，大概内存是43M多

                //使用0，录制1分钟大概内存是几兆
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                // 限制时长 ，参数61代表61秒，可以根据需求自己调，最高应该是2个小时。
                //当在这里设置时长之后，录制到达时间，系统会自动保存视频，停止录制
//                intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);
                // 限制大小 限制视频的大小，这里是100兆。当大小到达的时候，系统会自动停止录制
//                intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 1024 * 1024 * 20L);
                intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 20971520L);

                //在这里有录制完成之后的操作，系统会默认把视频放到照片的文件夹中
                startActivityForResult(intent, VIDEO_RECORD);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case VIDEO_RECORD:
                if (data != null) {
                    Uri uri = data.getData();
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(uri, "video/mp4");
                    startActivity(intent);
//                    File file = new File(uri.getPath());
//                    if (file.exists()) {
//                        System.out.println(file.getAbsolutePath());
//                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
