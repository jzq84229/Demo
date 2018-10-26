package com.zhang.demo.ytx.ui.chatting.base;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;

import com.zhang.demo.ytx.R;
import com.zhang.demo.ytx.common.emoji.EmojiconHandler;

/**
 * 自定义EditText，可显示emoji表情
 * Created by Administrator on 2016/7/22.
 */
public class EmojiconEditText extends EditText {
    private int mEmojiconSize;
    private int mEmojiconTextSize;
    private boolean mUseSystemDefault = false;
    public InputConnection miInputConnection;

    public EmojiconEditText(Context context) {
        super(context);
        mEmojiconSize = (int) getTextSize();
        mEmojiconTextSize = (int) getTextSize();
    }

    public EmojiconEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public EmojiconEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.Emotion);
        mEmojiconSize = (int) a.getDimension(R.styleable.Emotion_emotionSize, getTextSize());
        mUseSystemDefault = a.getBoolean(R.styleable.Emotion_emotionUseSystemDefault, false);
        a.recycle();
        mEmojiconTextSize = (int) getTextSize();
        setText(getText());
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        updateText();
    }

    /**
     * Set the size of emojicon in pixels.
     */
    public void setEmojiconSize(int pixels) {
        mEmojiconSize = pixels;
        updateText();
    }

    /**
     * 更新编辑框内容
     */
    private void updateText() {
        EmojiconHandler.addEmojis(getContext(), getText(), mEmojiconSize, mEmojiconTextSize, mUseSystemDefault);
    }

    /** Set whether to use system default emojicon */
    public void setUseSystemDefault(boolean useSystemDefault) {
        mUseSystemDefault = useSystemDefault;
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        miInputConnection = super.onCreateInputConnection(outAttrs);
        return miInputConnection;
    }

    public InputConnection getInputConnection() {
        return miInputConnection;
    }
}
