package com.zhang.mydemo.qrcode;

import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.TextView;

import com.zhang.mydemo.R;

import java.io.IOException;

public class CameraSurfaceViewActivity extends AppCompatActivity {
    private static final String LOG_TAG = CameraSurfaceViewActivity.class.getSimpleName();
    private SurfaceView mSurFaceView;
    //    private ViewfinderView mViewfinderView;
    private TextView mTextView;

    private Camera camera;
    private boolean preview  = false ;
//    private SeekBar mSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        *设置窗口属性：一定要在 setContentView(R.layout.main) 之前
        */
        // 窗口标题,其实可以在manifes文件里面注册
        //     requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camera_surface_view);

        initViews();
    }

    private void initViews() {
        mSurFaceView = (SurfaceView) findViewById(R.id.preview_view);
//        mViewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        mTextView = (TextView) findViewById(R.id.txtResult);

        mSurFaceView.getHolder().addCallback(new SurfaceViewCallback());
    }

    private final class SurfaceViewCallback implements SurfaceHolder.Callback {

        /**
         * surfaceView 被创建成功后调用此方法
         */
        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            Log.d(LOG_TAG,"surfaceCreated");
            /*
             * 在SurfaceView创建好之后 打开摄像头
             * 注意是 android.hardware.Camera
             */
            camera = Camera.open();
            /*
             * This method must be called before startPreview(). otherwise surfaceview没有图像
             */
            try {
                camera.setPreviewDisplay(surfaceHolder);
            } catch (IOException e) {
                e.printStackTrace();
            }

            camera.setDisplayOrientation(90);
            Camera.Parameters parameters = camera.getParameters();
            /* 设置预览照片的大小，此处设置为全屏 */
            WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE); // 获取当前屏幕管理器对象
            Display display = wm.getDefaultDisplay();                        // 获取屏幕信息的描述类
            parameters.setPreviewSize(display.getWidth(), display.getHeight());          // 设置
//            parameters.setPreviewSize(200, 200);
            /* 每秒从摄像头捕获5帧画面， */
            parameters.setPreviewFrameRate(5);
            /* 设置照片的输出格式:jpg */
            parameters.setPictureFormat(PixelFormat.JPEG);
            /* 照片质量 */
            parameters.set("jpeg-quality", 85);
            /* 设置照片的大小：此处照片大小等于屏幕大小 */
            parameters.setPictureSize(display.getWidth(), display.getHeight());
//            parameters.setPictureSize(200, 200);
            /* 将参数对象赋予到 camera 对象上 */
//          camera.setParameters(parameters);
//            mSeekBar.setMax(100);
            camera.startPreview();
            /**
             * Installs a callback to be invoked for every preview frame in addition to displaying them on the screen.
             * The callback will be repeatedly called for as long as preview is active. This method can be called at
             * any time, even while preview is live. Any other preview callbacks are overridden.
             * a callback object that receives a copy of each preview frame, or null to stop receiving
             */
            camera.setPreviewCallback(new Camera.PreviewCallback(){

                @Override
                public void onPreviewFrame(byte[] data, Camera camera) {
                    // TODO Auto-generated method stub
                    //在视频聊天中，这里传送本地frame数据给remote端
                    Log.d(LOG_TAG, "camera:"+camera);
                    Log.d(LOG_TAG, "byte:"+data);
                }

            });
            preview = true;
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
            Log.d(LOG_TAG,"surfaceChanged");
        }

        /**
         * SurfaceView 被销毁时释放掉 摄像头
         */
        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            if(camera != null) {
                /* 若摄像头正在工作，先停止它 */
                if(preview) {
                    camera.stopPreview();
                    preview = false;
                }
                //如果注册了此回调，在release之前调用，否则release之后还回调，crash
                camera.setPreviewCallback(null);
                camera.release();
            }
        }
    }

    /**
     * 处理照片被拍摄之后的事件
     */
    private final class TakePictureCallback implements Camera.PictureCallback {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
        }
    }
}
