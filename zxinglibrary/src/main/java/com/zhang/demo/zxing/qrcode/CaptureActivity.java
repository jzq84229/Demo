/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zhang.demo.zxing.qrcode;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.google.zxing.Result;


import com.zhang.demo.zxing.R;
import com.zhang.demo.zxing.qrcode.camera.CameraManager;

import java.io.IOException;

/**
 * This activity opens the camera and does the actual scanning on a background thread. It draws a
 * viewfinder to help the user place the barcode correctly, shows feedback as the image processing
 * is happening, and then overlays the results when a scan is successful.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */
public final class CaptureActivity extends Activity implements SurfaceHolder.Callback {

  private static final String TAG = CaptureActivity.class.getSimpleName();

  //摄像头管理类
  private CameraManager cameraManager;
  //handler
  private com.zhang.demo.zxing.qrcode.utils.CaptureActivityHandler handler;
  //activity计时类
  private com.zhang.demo.zxing.qrcode.utils.InactivityTimer inactivityTimer;
  //震动类
  private com.zhang.demo.zxing.qrcode.utils.BeepManager beepManager;
  private SurfaceView surfaceView = null;
  private com.zhang.demo.zxing.qrcode.view.ViewfinderView viewfinderView;

  //截取正方形
  private Rect mCropRect = null;
  private Rect mScanRect = null;
  //是否有surface
  private boolean hasSurface = false;
  //view延迟加载
  private ViewStub mViewStub;
  //是否被inflate
  private boolean isInflate = false;

  com.zhang.demo.zxing.qrcode.view.ViewfinderView getViewfinderView() {
    return viewfinderView;
  }

  public Handler getHandler() {
    return handler;
  }

  public CameraManager getCameraManager() {
    return cameraManager;
  }

  @Override
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);

    //设置屏幕常亮
    Window window = getWindow();
    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    setContentView(R.layout.library_activity_capture);

    surfaceView = (SurfaceView) findViewById(R.id.preview_view);
    //在初始化相机的时候截取正方形
    mViewStub = (ViewStub) findViewById(R.id.viewStub);
    //给activity生命周期计时，当手机不处于充电状态，5分钟关闭该activity
    inactivityTimer = new com.zhang.demo.zxing.qrcode.utils.InactivityTimer(this);
    // 扫码成功后震动
    beepManager = new com.zhang.demo.zxing.qrcode.utils.BeepManager(this);

  }

  @Override
  protected void onResume() {
    super.onResume();
    // CameraManager must be initialized here, not in onCreate(). This is necessary because we don't
    // want to open the camera driver and measure the screen size if we're going to show the help on
    // first launch. That led to bugs where the scanning rectangle was the wrong size and partially
    // off screen.
    //处理相机事物
    cameraManager = new CameraManager(getApplication());
    //将hanlder设置为空
    handler = null;

    surfaceView = (SurfaceView) findViewById(R.id.preview_view);
    //判断是否添加了callback
    if (hasSurface) {
      // The activity was paused but not stopped, so the surface still exists. Therefore
      // surfaceCreated() won't be called, so init the camera here.
      initCamera(surfaceView.getHolder());
    } else {
      // Install the callback and wait for surfaceCreated() to init the camera.
      surfaceView.getHolder().addCallback(this);
    }
    //注册是否充电监听
    inactivityTimer.onResume();

  }

  @Override
  protected void onPause() {
    if (handler != null) {
      handler.quitSynchronously();
      handler = null;
    }
    inactivityTimer.onPause();
    beepManager.close();
    cameraManager.closeDriver();
    if (!hasSurface) {
//      SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
      SurfaceHolder surfaceHolder = surfaceView.getHolder();
      surfaceHolder.removeCallback(this);
    }
    super.onPause();
  }

  @Override
  protected void onDestroy() {
    inactivityTimer.shutdown();
    super.onDestroy();
  }

  /**
   * 初始化相机
   * @param surfaceHolder surfaceHolder
     */
  private void initCamera(SurfaceHolder surfaceHolder) {
    if (surfaceHolder == null) {
      throw new IllegalStateException("No SurfaceHolder provided");
    }
    if (cameraManager.isOpen()) {
      Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
      return;
    }
    try {
      cameraManager.openDriver(surfaceHolder);
      // Creating the handler starts the preview, which can also throw a RuntimeException.
      if (handler == null) {
        handler = new com.zhang.demo.zxing.qrcode.utils.CaptureActivityHandler(this, cameraManager, com.zhang.demo.zxing.qrcode.decode.DecodeThread.ALL_MODE);
      }

      if (!isInflate) {
        FrameLayout layout = (FrameLayout) mViewStub.inflate();
        viewfinderView = (com.zhang.demo.zxing.qrcode.view.ViewfinderView) layout.findViewById(R.id.viewfinder_view);
        initCrop();
        viewfinderView.setGuideFrame(getmScanRect());
        isInflate = true;
      }
//      decodeOrStoreSavedBitmap(null, null);
    } catch (IOException ioe) {
      Log.w(TAG, ioe);
      displayFrameworkBugMessageAndExit();
    } catch (RuntimeException e) {
      // Barcode Scanner has seen crashes in the wild of this variety:
      // java.?lang.?RuntimeException: Fail to connect to camera service
      Log.w(TAG, "Unexpected error initializing camera", e);
      displayFrameworkBugMessageAndExit();
    }
  }

  /**
   * 初始化截取矩形区域
   */
  public void initCrop() {
    int cameraWidth = cameraManager.getCameraResolution().y;
    int cameraHeight = cameraManager.getCameraResolution().x;

    //扫描框的为正方形，边长为相机最短边长的1/2
    int cropLength = Math.min(cameraWidth, cameraHeight) / 2;

    /** 获取布局中扫描框的位置信息 */
    int[] location = new int[2];
    viewfinderView.getLocationInWindow(location);

    int cropLeft = (cameraWidth - cropLength) / 2;
    int cropTop = (cameraHeight - cropLength) / 3;

    /** 生成最终的截取的矩形 */
    mCropRect = new Rect(cropLeft, cropTop, cropLeft + cropLength, cropTop + cropLength);

    /** 获取布局容器的宽高 */
    int containerWidth = surfaceView.getWidth();
    int containerHeight = surfaceView.getHeight();

    /** 计算屏幕显示最终截取的矩形的左上角顶点x坐标 */
    int x = cropLeft * containerWidth / cameraWidth;
    /** 计算屏幕显示最终截取的矩形的左上角顶点y坐标 */
    int y = cropTop * containerHeight / cameraHeight;

    /** 计算屏幕显示最终截取的矩形的宽度 */
    int width = cropLength * containerWidth / cameraWidth;
    /** 计算屏幕显示最终截取的矩形的高度 */
    int height = cropLength * containerHeight / cameraHeight;

    mScanRect = new Rect(x, y, width + x, height + y);
  }

  public Rect getmScanRect() {
    return mScanRect;
  }

  public Rect getmCropRect() {
    return mCropRect;
  }

  private void displayFrameworkBugMessageAndExit() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle(getString(R.string.app_name));
    builder.setMessage(getString(R.string.msg_camera_framework_bug));
    builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {
        setResult(com.zhang.demo.zxing.qrcode.Constants.PERMISSION_DENY);
        finish();
      }
    });
//    builder.setOnCancelListener(new FinishListener(this));
    builder.show();
  }

  @Override
  public void surfaceCreated(SurfaceHolder holder) {
    if (holder == null) {
      Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
    }
    //初始化相机
    if (!hasSurface) {
      hasSurface = true;
      initCamera(holder);
    }
  }

  @Override
  public void surfaceDestroyed(SurfaceHolder holder) {
    hasSurface = false;
  }

  @Override
  public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

  }

  public void handleDecode(Result rawResult, Bundle bundle) {
    inactivityTimer.onActivity();
    beepManager.playBeepSoundAndVibrate();
    bundle.putInt("width", mCropRect.width());
    bundle.putInt("height", mCropRect.height());
    bundle.putString("result", rawResult.getText());

    Intent intent = new Intent();
    intent.putExtras(bundle);
    setResult(RESULT_OK, intent);
    finish();
  }

}
