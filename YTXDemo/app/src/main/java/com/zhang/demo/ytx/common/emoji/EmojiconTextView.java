package com.zhang.demo.ytx.common.emoji;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.zhang.demo.ytx.R;

/**
 * 可现实Emoji的TextView控件
 * Created by Administrator on 2016/7/11.
 */
public class EmojiconTextView extends TextView {
    private int mEmotionSize;
    private int mEmotionTextSize;
    private int mTextStart = 0;
    private int mTextLength = -1;
    private boolean mUseSystemDefault = false;

    public EmojiconTextView(Context context) {
        super(context);
        init(null);
    }

    public EmojiconTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public EmojiconTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        mEmotionTextSize = (int) getTextSize();
        if (attrs == null) {
            mEmotionSize = (int) getTextSize();
        } else {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.Emotion);
            mEmotionSize = (int) a.getDimension(R.styleable.Emotion_emotionSize, getTextSize());
            mTextStart = a.getInteger(R.styleable.Emotion_emotionTextStart, 0);
            mTextLength = a.getInteger(R.styleable.Emotion_emotionTextLength, -1);
            mUseSystemDefault = a.getBoolean(R.styleable.Emotion_emotionUseSystemDefault, false);
            a.recycle();
        }
        setText(getText());
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (!TextUtils.isEmpty(text)) {
            SpannableStringBuilder builder = new SpannableStringBuilder(text);
            EmojiconHandler.addEmojis(getContext(), builder, mEmotionSize, mEmotionTextSize, mTextStart, mTextLength, mUseSystemDefault);
            text = builder;
        }
        super.setText(text, type);
    }

    /**
     * Set the size of emotion in pixels.
     * @param pixels        emoji图标尺寸
     */
    public void setEmotionSize(int pixels) {
        mEmotionSize = pixels;
        super.setText(getText());
    }

    /**
     * Set whether to use system default emotion.
     * @param useSystemDefault      是否使用系统图标
     */
    public void setUseSystemDefault(boolean useSystemDefault) {
        mUseSystemDefault = useSystemDefault;
    }
}
