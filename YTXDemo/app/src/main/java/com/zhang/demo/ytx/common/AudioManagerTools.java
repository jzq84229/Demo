package com.zhang.demo.ytx.common;

import android.content.Context;
import android.media.AudioManager;

/**
 * AudioManager管理工具类，单例
 * Created by Administrator on 2016/7/8.
 */
public class AudioManagerTools {

    /** AudioManager */
    private AudioManager mAudioManager = null;
    private static AudioManagerTools mInstance;

    private AudioManagerTools() {
    }

    /** 单例方法 */
    public static AudioManagerTools getInstance() {
        if (mInstance == null) {
            mInstance = new AudioManagerTools();
        }
        return mInstance;
    }

    /**
     * 返回当前所持有的AudioManager访问实例
     * @return      mAudioManager
     */
    public final AudioManager getAudioManager() {
        if (mAudioManager == null) {
            mAudioManager = (AudioManager) CCPAppManager.getContext().getSystemService(Context.AUDIO_SERVICE);
        }
        return mAudioManager;
    }
}
