/*
 * Copyright (C) 2010 ZXing authors
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

package com.zhang.demo.zxing.qrcode.camera;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.hardware.Camera;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

import com.zhang.demo.zxing.qrcode.camera.open.CameraFacing;
import com.zhang.demo.zxing.qrcode.camera.open.OpenCamera;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * A class which deals with reading, parsing, and setting the camera parameters which are used to
 * configure the camera hardware.
 */
public final class CameraConfigurationManager {

  private static final String TAG = "CameraConfiguration";

  private static final int MIN_PREVIEW_PIXELS = 480 * 320; // normal screen
  private static final double MAX_ASPECT_DISTORTION = 0.15;

  private final Context context;
//  private int cwNeededRotation;
//  private int cwRotationFromDisplayToCamera;
  //屏幕分辨率
  private Point screenResolution;
  //相机分辨率
  private Point cameraResolution;
//  private Point bestPreviewSize;
//  private Point previewSizeOnScreen;

  public CameraConfigurationManager(Context context) {
    this.context = context;
  }

  /**
   * Reads, one time, values from the camera that are needed by the app.
   */
  public void initFromCameraParameters(OpenCamera camera) {
    Camera.Parameters parameters = camera.getCamera().getParameters();
    WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    Display display = manager.getDefaultDisplay();

    screenResolution = getDisplaySize(display);
    Log.i(TAG, "Scrren resolution:" + screenResolution);

    /** 因为换成了竖屏显示，所以不替换屏幕宽高得出的预览图是变形的 */
    Point screenResolutionForCamera = new Point();
    screenResolutionForCamera.x = screenResolution.x;
    screenResolutionForCamera.y = screenResolution.y;

    if (screenResolution.x < screenResolution.y) {
      screenResolutionForCamera.x = screenResolution.y;
      screenResolutionForCamera.y = screenResolution.x;
    }
    cameraResolution = findBestPreviewSizeValue(parameters, screenResolutionForCamera);
    Log.i(TAG, "Camera resolution x:" + cameraResolution.x);
    Log.i(TAG, "Camera resolution y:" + cameraResolution.y);

 /*   int displayRotation = display.getRotation();
    int cwRotationFromNaturalToDisplay;
    switch (displayRotation) {
      case Surface.ROTATION_0:
        cwRotationFromNaturalToDisplay = 0;
        break;
      case Surface.ROTATION_90:
        cwRotationFromNaturalToDisplay = 90;
        break;
      case Surface.ROTATION_180:
        cwRotationFromNaturalToDisplay = 180;
        break;
      case Surface.ROTATION_270:
        cwRotationFromNaturalToDisplay = 270;
        break;
      default:
        // Have seen this return incorrect values like -90
        if (displayRotation % 90 == 0) {
          cwRotationFromNaturalToDisplay = (360 + displayRotation) % 360;
        } else {
          throw new IllegalArgumentException("Bad rotation: " + displayRotation);
        }
    }
    Log.i(TAG, "Display at: " + cwRotationFromNaturalToDisplay);

    int cwRotationFromNaturalToCamera = camera.getOrientation();
    Log.i(TAG, "Camera at: " + cwRotationFromNaturalToCamera);

    // Still not 100% sure about this. But acts like we need to flip this:
    if (camera.getFacing() == CameraFacing.FRONT) {
      cwRotationFromNaturalToCamera = (360 - cwRotationFromNaturalToCamera) % 360;
      Log.i(TAG, "Front camera overriden to: " + cwRotationFromNaturalToCamera);
    }*/

    /*
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    String overrideRotationString;
    if (camera.getFacing() == CameraFacing.FRONT) {
      overrideRotationString = prefs.getString(PreferencesActivity.KEY_FORCE_CAMERA_ORIENTATION_FRONT, null);
    } else {
      overrideRotationString = prefs.getString(PreferencesActivity.KEY_FORCE_CAMERA_ORIENTATION, null);
    }
    if (overrideRotationString != null && !"-".equals(overrideRotationString)) {
      Log.i(TAG, "Overriding camera manually to " + overrideRotationString);
      cwRotationFromNaturalToCamera = Integer.parseInt(overrideRotationString);
    }
     */

  /*  cwRotationFromDisplayToCamera =
        (360 + cwRotationFromNaturalToCamera - cwRotationFromNaturalToDisplay) % 360;
    Log.i(TAG, "Final display orientation: " + cwRotationFromDisplayToCamera);
    if (camera.getFacing() == CameraFacing.FRONT) {
      Log.i(TAG, "Compensating rotation for front camera");
      cwNeededRotation = (360 - cwRotationFromDisplayToCamera) % 360;
    } else {
      cwNeededRotation = cwRotationFromDisplayToCamera;
    }
    Log.i(TAG, "Clockwise rotation from display to camera: " + cwNeededRotation);

    Point theScreenResolution = new Point();
    display.getSize(theScreenResolution);
    screenResolution = theScreenResolution;
    Log.i(TAG, "Screen resolution in current orientation: " + screenResolution);
    cameraResolution = CameraConfigurationUtils.findBestPreviewSizeValue(parameters, screenResolution);
    Log.i(TAG, "Camera resolution: " + cameraResolution);
    bestPreviewSize = CameraConfigurationUtils.findBestPreviewSizeValue(parameters, screenResolution);
    Log.i(TAG, "Best available preview size: " + bestPreviewSize);

    boolean isScreenPortrait = screenResolution.x < screenResolution.y;
    boolean isPreviewSizePortrait = bestPreviewSize.x < bestPreviewSize.y;

    if (isScreenPortrait == isPreviewSizePortrait) {
      previewSizeOnScreen = bestPreviewSize;
    } else {
      previewSizeOnScreen = new Point(bestPreviewSize.y, bestPreviewSize.x);
    }
    Log.i(TAG, "Preview size on screen: " + previewSizeOnScreen);*/
  }

  private Point getDisplaySize(final Display display) {
    final Point point = new Point();
    display.getSize(point);
    return point;
  }

  /**
   * 从相机支持的分辨率中计算出最适合的预览界面尺寸
   * @param parameters
   * @param screenResolution
   * @return
     */
  private Point findBestPreviewSizeValue(Camera.Parameters parameters, Point screenResolution) {
    List<Camera.Size> rawSupportedSizes = parameters.getSupportedPreviewSizes();
    if (rawSupportedSizes == null) {
      Log.w(TAG, "Device returned no supported preview sizes; using default");
      Camera.Size defaultSize = parameters.getPreviewSize();
      if (defaultSize == null) {
        throw new IllegalStateException("Parameters contained no preview size!");
      }
      return new Point(defaultSize.width, defaultSize.height);
    }

    // Sort by size, descending
    List<Camera.Size> supportedPreviewSizes = new ArrayList<>(rawSupportedSizes);
    Collections.sort(supportedPreviewSizes, new Comparator<Camera.Size>() {
      @Override
      public int compare(Camera.Size a, Camera.Size b) {
        int aPixels = a.height * a.width;
        int bPixels = b.height * b.width;
        if (bPixels < aPixels) {
          return -1;
        }
        if (bPixels > aPixels) {
          return 1;
        }
        return 0;
      }
    });

    if (Log.isLoggable(TAG, Log.INFO)) {
      StringBuilder previewSizesString = new StringBuilder();
      for (Camera.Size supportedPreviewSize : supportedPreviewSizes) {
        previewSizesString.append(supportedPreviewSize.width)
                .append('x')
                .append(supportedPreviewSize.height)
                .append(' ');
      }
      Log.i(TAG, "Supported preview sizes: " + previewSizesString);
    }

    double screenAspectRatio = (double) screenResolution.x / (double) screenResolution.y;

    // Remove sizes that are unsuitable
    Iterator<Camera.Size> it = supportedPreviewSizes.iterator();
    while (it.hasNext()) {
      Camera.Size supportedPreviewSize = it.next();
      int realWidth = supportedPreviewSize.width;
      int realHeight = supportedPreviewSize.height;
      if (realWidth * realHeight < MIN_PREVIEW_PIXELS) {
        it.remove();
        continue;
      }

      boolean isCandidatePortrait = realWidth < realHeight;
      int maybeFlippedWidth = isCandidatePortrait ? realHeight : realWidth;
      int maybeFlippedHeight = isCandidatePortrait ? realWidth : realHeight;
      double aspectRatio = (double) maybeFlippedWidth / (double) maybeFlippedHeight;
      double distortion = Math.abs(aspectRatio - screenAspectRatio);
      if (distortion > MAX_ASPECT_DISTORTION) {
        it.remove();
        continue;
      }

      if (maybeFlippedWidth == screenResolution.x && maybeFlippedHeight == screenResolution.y) {
        Point exactPoint = new Point(realWidth, realHeight);
        Log.i(TAG, "Found preview size exactly matching screen size: " + exactPoint);
        return exactPoint;
      }
    }

    // If no exact match, use largest preview size. This was not a great idea on older devices because
    // of the additional computation needed. We're likely to get here on newer Android 4+ devices, where
    // the CPU is much more powerful.
    if (!supportedPreviewSizes.isEmpty()) {
      Camera.Size largestPreview = supportedPreviewSizes.get(0);
      Point largestSize = new Point(largestPreview.width, largestPreview.height);
      Log.i(TAG, "Using largest suitable preview size: " + largestSize);
      return largestSize;
    }

    // If there is nothing at all suitable, return current preview size
    Camera.Size defaultPreview = parameters.getPreviewSize();
    if (defaultPreview == null) {
      throw new IllegalStateException("Parameters contained no preview size!");
    }
    Point defaultSize = new Point(defaultPreview.width, defaultPreview.height);
    Log.i(TAG, "No suitable preview sizes, using default: " + defaultSize);
    return defaultSize;
  }

  public void setDesiredCameraParameters(OpenCamera camera, boolean safeMode) {

    Camera theCamera = camera.getCamera();
    Camera.Parameters parameters = theCamera.getParameters();

    if (parameters == null) {
      Log.w(TAG, "Device error: no camera parameters are available. Proceeding without configuration.");
      return;
    }

    Log.i(TAG, "Initial camera parameters: " + parameters.flatten());

    if (safeMode) {
      Log.w(TAG, "In camera config safe mode -- most settings will not be honored");
    }

//    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//
//    initializeTorch(parameters, prefs, safeMode);
//
//    CameraConfigurationUtils.setFocus(
//        parameters,
//        prefs.getBoolean(PreferencesActivity.KEY_AUTO_FOCUS, true),
//        prefs.getBoolean(PreferencesActivity.KEY_DISABLE_CONTINUOUS_FOCUS, true),
//        safeMode);
//
//    if (!safeMode) {
//      if (prefs.getBoolean(PreferencesActivity.KEY_INVERT_SCAN, false)) {
//        CameraConfigurationUtils.setInvertColor(parameters);
//      }
//
//      if (!prefs.getBoolean(PreferencesActivity.KEY_DISABLE_BARCODE_SCENE_MODE, true)) {
//        CameraConfigurationUtils.setBarcodeSceneMode(parameters);
//      }
//
//      if (!prefs.getBoolean(PreferencesActivity.KEY_DISABLE_METERING, true)) {
//        CameraConfigurationUtils.setVideoStabilization(parameters);
//        CameraConfigurationUtils.setFocusArea(parameters);
//        CameraConfigurationUtils.setMetering(parameters);
//      }
//
//    }
//
//    parameters.setPreviewSize(bestPreviewSize.x, bestPreviewSize.y);
//
//    theCamera.setParameters(parameters);
//
//    theCamera.setDisplayOrientation(cwRotationFromDisplayToCamera);
//
//    Camera.Parameters afterParameters = theCamera.getParameters();
//    Camera.Size afterSize = afterParameters.getPreviewSize();
//    if (afterSize != null && (bestPreviewSize.x != afterSize.width || bestPreviewSize.y != afterSize.height)) {
//      Log.w(TAG, "Camera said it supported preview size " + bestPreviewSize.x + 'x' + bestPreviewSize.y +
//          ", but after setting it, preview size is " + afterSize.width + 'x' + afterSize.height);
//      bestPreviewSize.x = afterSize.width;
//      bestPreviewSize.y = afterSize.height;
//    }

    parameters.setPreviewSize(cameraResolution.x, cameraResolution.y);
    theCamera.setParameters(parameters);

    Camera.Parameters afterParameters = theCamera.getParameters();
    Camera.Size afterSize = afterParameters.getPreviewSize();
    if (afterSize != null && (cameraResolution.x != afterSize.width || cameraResolution.y != afterSize.height)) {
      Log.w(TAG, "Camera said it supported preview size " + cameraResolution.x + 'x' + cameraResolution.y +
              ", but after setting it, preview size is " + afterSize.width + 'x' + afterSize.height);
      cameraResolution.x = afterSize.width;
      cameraResolution.y = afterSize.height;
    }

    theCamera.setDisplayOrientation(90);
  }

/*  Point getBestPreviewSize() {
    return bestPreviewSize;
  }

  Point getPreviewSizeOnScreen() {
    return previewSizeOnScreen;
  }*/

  public Point getCameraResolution() {
    return cameraResolution;
  }

  public Point getScreenResolution() {
    return screenResolution;
  }


//  int getCWNeededRotation() {
//    return cwNeededRotation;
//  }
//
//  boolean getTorchState(Camera camera) {
//    if (camera != null) {
//      Camera.Parameters parameters = camera.getParameters();
//      if (parameters != null) {
//        String flashMode = camera.getParameters().getFlashMode();
//        return flashMode != null &&
//            (Camera.Parameters.FLASH_MODE_ON.equals(flashMode) ||
//             Camera.Parameters.FLASH_MODE_TORCH.equals(flashMode));
//      }
//    }
//    return false;
//  }

//  void setTorch(Camera camera, boolean newSetting) {
//    Camera.Parameters parameters = camera.getParameters();
//    doSetTorch(parameters, newSetting, false);
//    camera.setParameters(parameters);
//  }
//
//  private void initializeTorch(Camera.Parameters parameters, SharedPreferences prefs, boolean safeMode) {
//    boolean currentSetting = FrontLightMode.readPref(prefs) == FrontLightMode.ON;
//    doSetTorch(parameters, currentSetting, safeMode);
//  }
//
//  private void doSetTorch(Camera.Parameters parameters, boolean newSetting, boolean safeMode) {
//    CameraConfigurationUtils.setTorch(parameters, newSetting);
//    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//    if (!safeMode && !prefs.getBoolean(PreferencesActivity.KEY_DISABLE_EXPOSURE, true)) {
//      CameraConfigurationUtils.setBestExposure(parameters, newSetting);
//    }
//  }

}
