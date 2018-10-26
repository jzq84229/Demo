package com.zhang.demo.imageloader;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by admin on 2017/7/25.
 */

public class GlideImageLoaderStrategy implements BaseImageLoaderStrategy {

    @Override
    public void loadImage(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url).into(imageView);
    }
}
