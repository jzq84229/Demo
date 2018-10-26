package com.zhang.imagepicker;

import com.zhang.imagepicker.bean.ImageItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/7/25.
 */

public class DataHolder {
    public static final String DH_CURRENT_IMAGE_FOLDER_ITEMS = "dh_current_image_folder_items";

    private static DataHolder mInstance;
    private Map<String, List<ImageItem>> data;

    public static DataHolder getInstance() {
        if (mInstance == null) {
            synchronized (ImagePickerProvider.class) {
                if (mInstance == null) {
                    mInstance = new DataHolder();
                }
            }
        }
        return mInstance;
    }

    private DataHolder() {
        data = new HashMap<>();
    }

    public void save(String id, List<ImageItem> object) {
        if (data != null) {
            data.put(id, object);
        }
    }

    public Object retrieve(String id) {
        if (data == null || mInstance == null) {
            throw new RuntimeException("你必须先初始化");
        }
        return data.get(id);
    }
}
