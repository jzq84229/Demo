package com.zhang.imagepicker.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SoundEffectConstants;
import android.widget.CheckBox;

/**
 * 带声音的CheckBox
 * Created by admin on 2017/7/28.
 */

public class SuperCheckBox extends CheckBox {
    public SuperCheckBox(Context context) {
        super(context);
    }

    public SuperCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SuperCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean performClick() {
        final boolean handled = super.performClick();
        if (!handled) {
            // View only makes a sound effect if the onClickListener was
            // called, so we'll need to make one here instead.
            playSoundEffect(SoundEffectConstants.CLICK);
        }
        return handled;
    }
}
