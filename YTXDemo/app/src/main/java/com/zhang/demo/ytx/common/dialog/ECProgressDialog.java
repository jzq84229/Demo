package com.zhang.demo.ytx.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.zhang.demo.ytx.R;

/**
 * Created by Administrator on 2016/7/13.
 */
public class ECProgressDialog extends Dialog {
    private TextView mTextView;
    private View mImageView;
    AsyncTask mAsyncTask;

    private final OnCancelListener mCancelListener = new OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            if (mAsyncTask != null) {
                mAsyncTask.cancel(true);
            }
        }
    };

    public ECProgressDialog(Context context) {
        super(context, R.style.Theme_Light_CustomDialog_Blue);
        mAsyncTask = null;
        setCancelable(true);
        setContentView(R.layout.common_loading_dialog);
        mTextView = (TextView) findViewById(R.id.textview);
        mTextView.setText(getContext().getString(R.string.loading_press));
        mImageView = findViewById(R.id.imageview);
        setOnCancelListener(mCancelListener);
    }

    /**
     * 构造器
     * @param resid     文本资源Id
     */
    public ECProgressDialog(Context context, int resid) {
        this(context);
        mTextView.setText(resid);
    }

    public ECProgressDialog(Context context, CharSequence text) {
        this(context);
        mTextView.setText(text);
    }

    public ECProgressDialog(Context context, AsyncTask asyncTask) {
        this(context);
        asyncTask = asyncTask;
    }

    public ECProgressDialog(Context context , CharSequence text , AsyncTask asyncTask) {
        this(context , text);
        mAsyncTask = asyncTask;
    }

    /**
     * 设置对话框显示文本
     * @param text
     */
    public final void setPressText(CharSequence text) {
        mTextView.setText(text);
    }

    public final void dismiss() {
        super.dismiss();
        mImageView.clearAnimation();
    }

    public final void show() {
        super.show();
        Animation loadAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.loading);
        mImageView.startAnimation(loadAnimation);
    }
}
