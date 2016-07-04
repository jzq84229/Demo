package com.zhang.mydemo.qrcode;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zhang.demo.zxing.qrcode.CaptureActivity;
import com.zhang.demo.zxing.qrcode.decode.DecodeThread;
import com.zhang.demo.zxing.qrcode.utils.Util;
import com.zhang.mydemo.BaseActivity;
import com.zhang.mydemo.R;

public class QrcodeMainActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTextView;
    private static final int SCAN_QR = 1;           //扫描二维码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContent() {
        //TODO 二维码扫描框样式修改，修改扫描框大小，修改扫描框上下滚动条样式
        setContentView(R.layout.activity_qrcode_main);
    }

    @Override
    public void findViews() {
        mTextView = (TextView) findViewById(R.id.text_view);
        Button btnGenerate = (Button) findViewById(R.id.btn_create);
        Button btnScaner = (Button) findViewById(R.id.btn_scaner);
        btnGenerate.setOnClickListener(this);
        btnScaner.setOnClickListener(this);
    }

    @Override
    public void setData() {

    }

    @Override
    public void showContent() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_create:
                startActivity(new Intent(this, GenrateQRActivity.class));
                break;
            case R.id.btn_scaner:
                if (Util.checkCameraHardware(getApplicationContext())) {
                    startActivityForResult(new Intent(this, CaptureActivity.class), SCAN_QR);
                } else {
                    Toast.makeText(QrcodeMainActivity.this, "没有检测的摄像头", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case SCAN_QR:
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    String result = bundle.getString("result");
                    byte[] compressedBitmap = bundle.getByteArray(DecodeThread.BARCODE_BITMAP);
                    int width = bundle.getInt("width");
                    int height = bundle.getInt("height");
                    Bitmap bitmap = getBitmap(compressedBitmap);
                    mTextView.setText(result + "图片宽：" + width + "图片高:" + height);
                }
                break;
        }
    }

    /**
     * 将byte数组decode为bitmap
     *
     * @param compressedBitmap byte数组
     * @return bitmap对象
     */
    public static Bitmap getBitmap(byte[] compressedBitmap) {
        Bitmap barcode = null;
        if (compressedBitmap != null) {
            barcode = BitmapFactory.decodeByteArray(compressedBitmap, 0, compressedBitmap.length, null);
            // Mutable copy:
            barcode = barcode.copy(Bitmap.Config.RGB_565, true);
        }
        return barcode;
    }
}
