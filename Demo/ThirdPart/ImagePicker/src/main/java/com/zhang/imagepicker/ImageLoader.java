package com.zhang.imagepicker;

import android.app.Activity;
import android.widget.ImageView;

/**
 * ImageLoader抽象类，外部需要实现这个类去加载图片， 尽力减少对第三方库的依赖，所以这么干了
 * Created by admin on 2017/7/25.
 */

public interface ImageLoader {

    void displayImage(Activity activity, String path, ImageView imageView, int width, int height);

    void clearMemoryCache();
}
