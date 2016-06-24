package com.zhang.demo.zxing;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.qrcode.encoder.QRCode;

import java.io.File;

public class GenrateQRActivity extends AppCompatActivity {
    private EditText etContent;
    private Button btnGenerate;
    private ImageView ivQR;
    private static final int WIDTH = 300;
    private static final int HEIGHT = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genrate_qr);

        initViews();
    }

    private void initViews() {
        etContent = (EditText) findViewById(R.id.et_content);
        btnGenerate = (Button) findViewById(R.id.btn_generate);
        ivQR = (ImageView) findViewById(R.id.img_qr);

        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateQRImage();
            }
        });
    }

    private void generateQRImage() {
        String content = etContent.getText().toString().trim();
        if (!TextUtils.isEmpty(content)) {
            String filePath = getFileRoot(this) + File.separator + "qr_" + System.currentTimeMillis() + ".jpg";
            generateImage(content, filePath);
        }
    }

    private void generateImage(final String content, final String filePath) {
        //二维码图片较大时，生成图片、保存文件的时间可能较长，因此放在新线程中
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean success = QRCodeUtil.createQRImage(content, WIDTH, HEIGHT, null, filePath);

                if (success) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ivQR.setImageBitmap(BitmapFactory.decodeFile(filePath));
                        }
                    });
                }
            }
        }).start();
    }


    //文件存储根目录
    private String getFileRoot(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File external = context.getExternalFilesDir(null);
            if (external != null) {
                return external.getAbsolutePath();
            }
        }

        return context.getFilesDir().getAbsolutePath();
    }
}
