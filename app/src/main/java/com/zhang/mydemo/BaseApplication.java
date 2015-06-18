package com.zhang.mydemo;

import android.app.Application;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * Created by zjun on 2015/6/15.
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        initImageLoaderConfig();
    }

    private void initImageLoaderConfig(){
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_launcher) // resource or drawable
                .showImageForEmptyUri(R.drawable.ic_launcher) // resource or drawable
                .showImageOnFail(R.drawable.ic_launcher) // resource or drawable
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
}
