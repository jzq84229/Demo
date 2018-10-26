package com.zhang.demo.ytx.ui.chatting.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * emoji表情框抽象类
 * Created by Administrator on 2016/7/25.
 */
public abstract class ChatFooterPanel extends LinearLayout {
    protected EmojiGrid.OnEmojiItemClickListener mItemClickListener;

    public ChatFooterPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Register a callback to be invoked when an item in this EmojiGird View has
     * been clicked.
     *
     * @param listener The callback that will be invoked.
     */
    protected void setOnEmojiItemClickListener(EmojiGrid.OnEmojiItemClickListener listener) {
        mItemClickListener = listener;
    }

    /**
     * @return The callback to be invoked with an item in this EmojiGird View has
     *         been clicked, or null id no callback has been set.
     */
    public final EmojiGrid.OnEmojiItemClickListener getOnEmojiItemClickListener() {
        return mItemClickListener;
    }

    public void onDestory() {}

    public abstract void setChatFooterPanelHeight(int height);

    /** {@link ChatFooterPanel} onPause */
    public abstract void onPause();
    /**{@link ChatFooterPanel} onResume */
    public abstract void onResume();
    /** {@link ChatFooterPanel} reset */
    public abstract void reset();

}
