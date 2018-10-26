package com.zhang.demo.ytx;

import android.app.Application;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.zhang.demo.ytx.common.CCPAppManager;
import com.zhang.demo.ytx.common.utils.ECPreferenceSettings;
import com.zhang.demo.ytx.common.utils.ECPreferences;
import com.zhang.demo.ytx.common.utils.FileAccessor;
import com.zhang.demo.ytx.common.utils.LogUtil;

import java.io.File;
import java.io.InvalidClassException;

/**
 * Created by Administrator on 2016/7/5.
 */
public class ECApplication extends Application {

    private static ECApplication instance;

    public static ECApplication getInstance() {
        if (instance == null) {
            LogUtil.w("[ECApplication] instance is null.");
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        CCPAppManager.setContext(instance);
        FileAccessor.initFileAccess();
        setChattingContactId();
        initImageLoader();

    }

    /**
     * 保存当前的聊天界面所对应的联系人、方便来消息屏蔽通知
     */
    private void setChattingContactId() {
        try {
            ECPreferences.savePreference(ECPreferenceSettings.SETTING_CHATTING_CONTACTID, "", true);
        } catch (InvalidClassException e) {
            e.printStackTrace();
        }
    }

    private void initImageLoader() {
        File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "ECSDK_Demo/image");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(this)
                .threadPoolSize(1)//线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .memoryCache(new WeakMemoryCache())
                // .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(CCPAppManager.md5FileNameGenerator)
                // 将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .diskCache(new UnlimitedDiscCache(cacheDir ,null , CCPAppManager.md5FileNameGenerator))//自定义缓存路径
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                // .writeDebugLogs() // Remove for release app
                .build();//开始构建
        ImageLoader.getInstance().init(config);

    }
}
