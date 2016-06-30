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

package com.zhang.demo.zxing.qrcode.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;


/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder rectangle and partial
 * transparency outside it, as well as the laser scanner animation and result points.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class ViewfinderView extends View {

//  private static final int[] SCANNER_ALPHA = {0, 64, 128, 192, 255, 192, 128, 64};
//  private static final long ANIMATION_DELAY = 30L;
//  private static final int CURRENT_POINT_OPACITY = 0xA0;
//  private static final int MAX_RESULT_POINTS = 20;
//  private static final int POINT_SIZE = 6;

  /** 非扫描区域的背景颜色 */
  private static final int SHADOW_COLOR = Color.parseColor("#7d000000");
  /** 刷新界面的时间 */
  private static final long ANIMATION_DELAY = 30L;
  /** 四个绿色边角对应的长度 */
  private static final int CORNER_LENGTH = 20;
  /** 四个绿色边角对应宽度 */
  private static final int CORNER_WIDTH = 5;
  /** 扫描框中间线的宽度 */
  private static final int MIDDLE_LINE_WIDTH = 6;
  /** 扫描框中间线与扫描框左右的间隙 */
  private static final int MIDDLE_LINE_PADDING = 5;
  /** 扫描框中间线每次刷新移动的距离 */
  private static final int SPEEN_DISTANCE = 5;
  /** 手机的屏幕密度 */
  private static float density;
  /** 画笔对象的引用 */
  private final Paint paint;
  /** 扫描框中间线滑动的最顶端位置 */
  private int mSlidePos;

  private boolean mIsFirst;

  private Rect mGuideRect;

//  private CameraManager cameraManager;
//  private Bitmap resultBitmap;
//  private final int maskColor;
//  private final int resultColor;
//  private final int laserColor;
//  private final int resultPointColor;
//  private int scannerAlpha;
//  private List<ResultPoint> possibleResultPoints;
//  private List<ResultPoint> lastPossibleResultPoints;

  // This constructor is used when the class is built from an XML resource.
  public ViewfinderView(Context context, AttributeSet attrs) {
    super(context, attrs);

    density = context.getResources().getDisplayMetrics().density;
    paint = new Paint();

    // Initialize these once for performance rather than calling them every time in onDraw().
//    paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//    Resources resources = getResources();
//    maskColor = resources.getColor(R.color.viewfinder_mask);
//    resultColor = resources.getColor(R.color.result_view);
//    laserColor = resources.getColor(R.color.viewfinder_laser);
//    resultPointColor = resources.getColor(R.color.possible_result_points);
//    scannerAlpha = 0;
//    possibleResultPoints = new ArrayList<>(5);
//    lastPossibleResultPoints = null;
  }

  @Override
  protected void onDraw(Canvas canvas) {
    Rect frame = mGuideRect;
    if (frame == null) {
      return;
    }

    //初始化中间线滑动的最上边和最下边
    if (!mIsFirst) {
      mIsFirst = true;
      mSlidePos = frame.top;
    }

    //获取屏幕的宽和高
    int width = canvas.getWidth();
    int height = canvas.getHeight();

    // 画出扫描框外面的阴影部分，共四个部分，扫描框的上面到屏幕上面，扫描框的下面到屏幕下面
    // 扫描框的左边面到屏幕左边，扫描框的右边到屏幕右边
    paint.setColor(SHADOW_COLOR);
    canvas.drawRect(0, 0, width, frame.top, paint);
    canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
    canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1, paint);
    canvas.drawRect(0, frame.bottom + 1, width, height, paint);

    // 画扫描框边上的角，总共8个部分
    int cornerWidth = (int) (density * CORNER_WIDTH);
    int cornerLength = (int) (density * CORNER_LENGTH);
    paint.setColor(Color.GREEN);
    canvas.drawRect(frame.left, frame.top, frame.left + cornerLength, frame.top + cornerWidth, paint);
    canvas.drawRect(frame.left, frame.top, frame.left + cornerWidth, frame.top + cornerLength, paint);
    canvas.drawRect(frame.right - cornerLength, frame.top, frame.right, frame.top + cornerWidth, paint);
    canvas.drawRect(frame.right - cornerWidth, frame.top, frame.right, frame.top + cornerLength, paint);
    canvas.drawRect(frame.left, frame.bottom - cornerWidth, frame.left + cornerLength, frame.bottom, paint);
    canvas.drawRect(frame.left, frame.bottom - cornerLength, frame.left + cornerWidth, frame.bottom, paint);
    canvas.drawRect(frame.right - cornerLength, frame.bottom - cornerWidth, frame.right, frame.bottom, paint);
    canvas.drawRect(frame.right - cornerWidth, frame.bottom - cornerLength, frame.right, frame.bottom, paint);

    // 绘制中间的线,每次刷新界面，中间的线往下移动SPEEN_DISTANCE
    mSlidePos += SPEEN_DISTANCE;
    if (mSlidePos >= frame.bottom) {
      mSlidePos = frame.top;
    }
    canvas.drawRect(frame.left + MIDDLE_LINE_PADDING, mSlidePos - MIDDLE_LINE_WIDTH / 2, frame.right
            - MIDDLE_LINE_PADDING, mSlidePos + MIDDLE_LINE_WIDTH / 2, paint);

    // 只刷新扫描框的内容，其他地方不刷新
    postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top, frame.right, frame.bottom);
  }

  public void setGuideFrame(Rect rect) {
    mGuideRect = rect;
  }

  //
//  public void setCameraManager(CameraManager cameraManager) {
//    this.cameraManager = cameraManager;
//  }
//
//  @SuppressLint("DrawAllocation")
//  @Override
//  public void onDraw(Canvas canvas) {
//    if (cameraManager == null) {
//      return; // not ready yet, early draw before done configuring
//    }
//    Rect frame = cameraManager.getFramingRect();
//    Rect previewFrame = cameraManager.getFramingRectInPreview();
//    if (frame == null || previewFrame == null) {
//      return;
//    }
//    int width = canvas.getWidth();
//    int height = canvas.getHeight();
//
//    // Draw the exterior (i.e. outside the framing rect) darkened
//    paint.setColor(resultBitmap != null ? resultColor : maskColor);
//    canvas.drawRect(0, 0, width, frame.top, paint);
//    canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
//    canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1, paint);
//    canvas.drawRect(0, frame.bottom + 1, width, height, paint);
//
//    if (resultBitmap != null) {
//      // Draw the opaque result bitmap over the scanning rectangle
//      paint.setAlpha(CURRENT_POINT_OPACITY);
//      canvas.drawBitmap(resultBitmap, null, frame, paint);
//    } else {
//
//      // Draw a red "laser scanner" line through the middle to show decoding is active
//      paint.setColor(laserColor);
//      paint.setAlpha(SCANNER_ALPHA[scannerAlpha]);
//      scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.length;
//      int middle = frame.height() / 2 + frame.top;
//      canvas.drawRect(frame.left + 2, middle - 1, frame.right - 1, middle + 2, paint);
//
//      float scaleX = frame.width() / (float) previewFrame.width();
//      float scaleY = frame.height() / (float) previewFrame.height();
//
//      List<ResultPoint> currentPossible = possibleResultPoints;
//      List<ResultPoint> currentLast = lastPossibleResultPoints;
//      int frameLeft = frame.left;
//      int frameTop = frame.top;
//      if (currentPossible.isEmpty()) {
//        lastPossibleResultPoints = null;
//      } else {
//        possibleResultPoints = new ArrayList<>(5);
//        lastPossibleResultPoints = currentPossible;
//        paint.setAlpha(CURRENT_POINT_OPACITY);
//        paint.setColor(resultPointColor);
//        synchronized (currentPossible) {
//          for (ResultPoint point : currentPossible) {
//            canvas.drawCircle(frameLeft + (int) (point.getX() * scaleX),
//                              frameTop + (int) (point.getY() * scaleY),
//                              POINT_SIZE, paint);
//          }
//        }
//      }
//      if (currentLast != null) {
//        paint.setAlpha(CURRENT_POINT_OPACITY / 2);
//        paint.setColor(resultPointColor);
//        synchronized (currentLast) {
//          float radius = POINT_SIZE / 2.0f;
//          for (ResultPoint point : currentLast) {
//            canvas.drawCircle(frameLeft + (int) (point.getX() * scaleX),
//                              frameTop + (int) (point.getY() * scaleY),
//                              radius, paint);
//          }
//        }
//      }
//
//      // Request another update at the animation interval, but only repaint the laser line,
//      // not the entire viewfinder mask.
//      postInvalidateDelayed(ANIMATION_DELAY,
//                            frame.left - POINT_SIZE,
//                            frame.top - POINT_SIZE,
//                            frame.right + POINT_SIZE,
//                            frame.bottom + POINT_SIZE);
//    }
//  }
//
//  public void drawViewfinder() {
//    Bitmap resultBitmap = this.resultBitmap;
//    this.resultBitmap = null;
//    if (resultBitmap != null) {
//      resultBitmap.recycle();
//    }
//    invalidate();
//  }
//
//  /**
//   * Draw a bitmap with the result points highlighted instead of the live scanning display.
//   *
//   * @param barcode An image of the decoded barcode.
//   */
//  public void drawResultBitmap(Bitmap barcode) {
//    resultBitmap = barcode;
//    invalidate();
//  }
//
//  public void addPossibleResultPoint(ResultPoint point) {
//    List<ResultPoint> points = possibleResultPoints;
//    synchronized (points) {
//      points.add(point);
//      int size = points.size();
//      if (size > MAX_RESULT_POINTS) {
//        // trim it
//        points.subList(0, size - MAX_RESULT_POINTS / 2).clear();
//      }
//    }
//  }

}
