package com.zhang.mydemo;

import android.app.Application;
import android.graphics.Bitmap;
import android.os.StrictMode;

//import com.morgoo.droidplugin.PluginApplication;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.zhang.mydemo.util.CrashHandler;
import com.zhang.mydemo.util.ScreenUtil;

/**
 * Created by zjun on 2015/6/15.
 */
public class BaseApplication extends Application {
    private static BaseApplication instance;
    public int screenWidth;
    public int screenHeight;
    private DisplayImageOptions imageOptions;

    public static BaseApplication getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        initDebugSetting();

        instance = this;
        initImageLoaderConfig();
        initScreenData();
    }

    private void initDebugSetting(){
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()   // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                            //            .penaltyDeath()
                    .build());

            CrashHandler crashHandler = CrashHandler.getInstance();
            crashHandler.init(getApplicationContext());
        }
    }

    private void initScreenData(){
        screenWidth = ScreenUtil.screenWidthPixels(this);
        screenHeight = ScreenUtil.screenHeightPixels(this);
    }

    private void initImageLoaderConfig(){
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.color.default_image_bg) // resource or drawable
                .showImageForEmptyUri(R.color.default_image_bg) // resource or drawable
                .showImageOnFail(R.color.default_image_bg) // resource or drawable
                .cacheInMemory(true) 					// 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) 						// 设置下载的图片是否缓存在SD卡中
                .considerExifParams(true) 				// 是否考虑JPEG图像EXIF参数（旋转，翻转）
                .bitmapConfig(Bitmap.Config.RGB_565) 	// default
                .build();

//        File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), Constant.IMAGE_CACHE_PATH);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
//                .diskCache(new UnlimitedDiskCache(cacheDir)) // 你可以传入自己的磁盘缓存
//                .diskCacheExtraOptions(480, 320, null)
//			      .diskCacheExtraOptions(480, 800, null)
//			      .diskCacheSize(500 * 1024 * 1024)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .defaultDisplayImageOptions(options)
//                .memoryCache(new WeakMemoryCache())
//                .memoryCacheSize(2 * 1024 * 1024)
//			      .writeDebugLogs()
                .build();

        ImageLoader.getInstance().init(config);
    }

    //空间主页图片下载时的options
    public DisplayImageOptions getImageOptions() {
        if (imageOptions == null) {
            imageOptions = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.color.default_image_bg)
                    .showImageForEmptyUri(R.color.default_image_bg)
                    .showImageOnFail(R.color.default_image_bg)
                    .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                    .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .considerExifParams(true)
                    .build();
        }
        return imageOptions;
    }
}
