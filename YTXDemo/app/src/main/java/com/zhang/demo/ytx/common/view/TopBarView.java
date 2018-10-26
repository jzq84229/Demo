package com.zhang.demo.ytx.common.view;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhang.demo.ytx.R;
import com.zhang.demo.ytx.common.emoji.EmojiconTextView;
import com.zhang.demo.ytx.common.utils.DensityUtil;

/**
 * 顶部导航栏
 * Created by Administrator on 2016/7/8.
 */
public class TopBarView extends LinearLayout {
    public static final int SHOW_BUTITLE = 2;
    private Context mContext;
    private ImageView mLeftButton;
    private EmojiconTextView mMiddleButton;
    private TextView mMiddleSub;
    private ImageView mRightButton;
    private TextView mLeftText;
    private TextView mRightText;
    private View.OnClickListener mClickListener;

    private boolean mArrowUp = true;


    public TopBarView(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    public TopBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    private void initView() {
        setOrientation(LinearLayout.HORIZONTAL);
//        setBackgroundResource(R.color.red_btn_color_normal);
        setBackgroundDrawable(new ColorDrawable(mContext.getResources().getColor(R.color.red_btn_color_normal)));
        LayoutInflater.from(getContext()).inflate(R.layout.comm_view_top_bar, this, true);
        mLeftButton = (ImageView) findViewById(R.id.btn_left);
        mMiddleButton = (EmojiconTextView) findViewById(R.id.btn_middle);
        mMiddleSub = (TextView) findViewById(R.id.btn_middle_sub);
        mRightButton = (ImageView) findViewById(R.id.btn_right);
        mLeftText = (TextView) findViewById(R.id.text_left);
        mRightText = (TextView) findViewById(R.id.text_right);
    }

    /**
     * 设置子标题
     */
    private void setMiddleSubTitle(int type, String title, String subTitle, View.OnClickListener l) {
        if (type == 1) {
            setOnClickListener(l);
        }
        setTitle(title);
        if (TextUtils.isEmpty(subTitle) || type == 2) {
            mMiddleSub.setVisibility(View.GONE);
            return;
        }

        mMiddleSub.setText(subTitle);
        mMiddleSub.setVisibility(View.VISIBLE);
        mMiddleSub.setOnClickListener(l);
    }

    /**
     * 设置TopBarView标题
     * @param title     标题名称
     */
    public void setTitle(CharSequence title) {
        if (TextUtils.isEmpty(title)) {
            mMiddleButton.setVisibility(View.INVISIBLE);
            return;
        }
        mMiddleButton.setText(title);
        mMiddleButton.setVisibility(View.VISIBLE);
        mMiddleButton.setOnClickListener(mClickListener);

        doSetTouchDelegate();
    }

    /**
     * 设置TopBarView标题
     * @param title     标题名称
     */
    private void setTitle(String title) {
        if (TextUtils.isEmpty(title)) {
            mMiddleButton.setVisibility(View.INVISIBLE);
            return;
        }
        mMiddleButton.setText(title);
        mMiddleButton.setVisibility(View.VISIBLE);
        mMiddleButton.setOnClickListener(mClickListener);

        doSetTouchDelegate();
    }

    /**
     * 设置标题可点击范围
     */
    private void doSetTouchDelegate() {
        final TextView middleBtn = mMiddleButton;
        post(new Runnable() {
            @Override
            public void run() {
                Rect rect = new Rect();
                rect.left = (middleBtn.getWidth() / 4);
                rect.right = (3 * middleBtn.getWidth() / 4);
                rect.top = 0;
                rect.bottom = middleBtn.getHeight();
                middleBtn.setTouchDelegate(new TouchDelegate(rect, mMiddleSub));
            }
        });
    }

    /**
     * 设置标题的背景
     * @param resId     背景资源ID
     */
    public void setTitleDrawable(int resId) {
        if (resId != -1) {
            mMiddleButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(resId), null, null, null);
            return;
        }
        mMiddleButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
    }

    /**
     * 显示正在加载ProgressBar
     */
    public void showTopProgressbar() {
        mRightButton.setVisibility(View.GONE);
        mRightText.setVisibility(View.GONE);
        findViewById(R.id.top_progressbar).setVisibility(View.VISIBLE);
    }

    /**
     * 设置TopBarView右边按钮的背景
     * @param resId     背景资源Id
     */
    public void setRightButtonRes(int resId) {
        if (resId == -1) {
            mRightButton.setVisibility(View.GONE);
            return;
        }
        int padding = getContext().getResources().getDimensionPixelSize(R.dimen.btn_topbar_paddingHorizontal);
        mRightButton.setImageResource(resId);
        mRightButton.setPadding(padding, 0, padding, 0 );
    }

    /**
     * 设置右边按钮的显示文字
     * @param text      按钮文字
     */
    public void setRightButtonText(String text) {
        if (text == null) {
            mRightText.setVisibility(View.GONE);
            return;
        }
        mRightText.setText(text);
    }

    /**
     * 设置TopBarView顶部更新提示是否显示
     * @param show      是否显示
     */
    public void setTopbarupdatePoint(boolean show) {
        View mTopbarUpdatePoint = findViewById(R.id.topbar_update_point);
        if (show) {
            mTopbarUpdatePoint.setVisibility(View.VISIBLE);
            return;
        }
        mTopbarUpdatePoint.setVisibility(View.GONE);
    }

    /**
     * 设置TopBarView右侧按钮的显示
     */
    public void setRightVisible() {
        mRightButton.setVisibility(View.VISIBLE);
        mRightText.setVisibility(View.VISIBLE);
        findViewById(R.id.top_progressbar).setVisibility(View.GONE);
    }

    /**
     * 设置TopBarView RightPoint是否显示
     * @param show      是否显示
     */
    public void setTopBarRightPoint(boolean show) {
        View mTopBarRightPoint = findViewById(R.id.right_point);
        if (show) {
            mTopBarRightPoint.setVisibility(View.VISIBLE);
            return;
        }
        mTopBarRightPoint.setVisibility(View.GONE);
    }

    /**
     * get the mLeftButton
     * @return      mLeftButton
     */
    public ImageView getLeftButton() {
        return mLeftButton;
    }

    /**
     * get the mRightButton
     * @return      mRightButton
     */
    public ImageView getRightButton() {
        return mRightButton;
    }

    /**
     * get the mLeftText
     * @return      mLeftText
     */
    public TextView getLeftText() {
        return mLeftText;
    }

    /**
     * get the mRightText
     * @return      mRightText
     */
    public TextView getRightText() {
        return mRightText;
    }

    public void setFront() {
        bringToFront();
    }

    /**
     * 显示up 或Down 图标
     * @param up        是否为up箭头图标, true: up图标， false: down图标
     * @param arrow     是否显示箭头图标
     */
    public void setMiddleBtnArrowUp(boolean up, boolean arrow) {
        if (mArrowUp == up && !arrow) {
            return;
        }

        mArrowUp = up;
        int id = R.drawable.common_top_bar_arrow_down;
        if (mArrowUp) {
            id = R.drawable.common_top_bar_arrow_up;
        }
        Drawable upDownDrawable = mContext.getResources().getDrawable(id);
        upDownDrawable.setBounds(0, 0, upDownDrawable.getIntrinsicWidth(), upDownDrawable.getIntrinsicHeight());
        mMiddleButton.setCompoundDrawablePadding(DensityUtil.dip2px(5.0F));
        mMiddleButton.setCompoundDrawablesWithIntrinsicBounds(null, null, upDownDrawable, null);
    }

    /**
     * 设置MiddleButton 的padding
     */
    public void setMiddleBtnPadding(int padding) {
        if (mMiddleButton == null) {
            return;
        }
        mMiddleButton.setPadding(padding, 0, padding, 0);
    }

    /**
     * 设置右侧按钮是否可用
     */
    public void setRightBtnEnable(boolean enabled) {
        mRightButton.setEnabled(enabled);
        mRightText.setEnabled(enabled);
    }

    /**
     * 设置topBarView按钮和标题
     */
    public void setTopBarToStatus(int type, int leftResid, int rightResid, int titleRes, View.OnClickListener l) {
        String str = "";
        if (titleRes != -1) {
            str = getResources().getString(titleRes);
        }
        setTopBarToStatus(type, leftResid, rightResid, null, null, str, "", l);
    }

    /**
     * 设置纯图片的按钮TopBarView
     */
    public void setTopBarToStatus(int type, int leftResid, int rightResid, String title, View.OnClickListener l) {
        setTopBarToStatus(type, leftResid, rightResid, null, null, title, "", l);
    }

    /**
     * 重载方法，设置返回、标题、右侧Action按钮
     */
    public void setTopBarToStatus(int type, int leftResid, String rightText, String title, View.OnClickListener l) {
        setTopBarToStatus(type, leftResid, -1, null, rightText, title, "", l);
    }

    /**
     * 设置TopBarView属性
     * @param type              类型
     * @param leftResid         左边按钮背景
     * @param rightResid        右边按钮背景
     * @param leftText          左边按钮文字
     * @param rightText         右边按钮文字
     * @param title             标题文字
     * @param subTitle          子标题文字
     * @param l                 点击监听事件
     */
    public void setTopBarToStatus(int type, int leftResid, int rightResid,
                                  String leftText, String rightText, String title, String subTitle,
                                  View.OnClickListener l) {
        mClickListener = l;
        findViewById(R.id.common_top_wrapper).setOnClickListener(mClickListener);
        int padding = getContext().getResources().getDimensionPixelSize(R.dimen.btn_topbar_paddingHorizontal);
        if (leftResid <= 0 || leftText != null) {
            mLeftButton.setVisibility(View.GONE);
            if (leftText != null) {
                mLeftButton.setVisibility(View.GONE);
                mLeftText.setText(leftText);
                mLeftText.setVisibility(View.VISIBLE);
                mLeftText.setOnClickListener(l);
            } else {
                mLeftText.setVisibility(View.GONE);
            }

            if (leftResid > 0) {
                mLeftText.setBackgroundResource(leftResid);
                mLeftText.setPadding(padding, 0, padding, 0);
            }
        } else {
            mLeftButton.setImageResource(leftResid);
            mLeftButton.setPadding(padding, 0, padding, 0);
            mLeftButton.setVisibility(View.VISIBLE);
            mLeftButton.setOnClickListener(l);
        }

        if (rightResid <= 0 || rightText != null) {
            mRightButton.setVisibility(View.GONE);
            if (rightText != null) {
                mRightButton.setVisibility(View.GONE);
                mRightText.setText(rightText);
                mRightText.setVisibility(View.VISIBLE);
                mRightText.setOnClickListener(l);
            } else {
                mRightText.setVisibility(View.GONE);
            }

            if (rightResid > 0) {
                mRightText.setBackgroundResource(rightResid);
                mRightText.setPadding(padding, 0, padding, 0);
            }
        } else {
            mRightButton.setImageResource(rightResid);
            mRightButton.setPadding(padding, 0, padding, 0);
            mRightButton.setVisibility(View.VISIBLE);
            mRightButton.setOnClickListener(l);
        }

        setMiddleSubTitle(type, title, subTitle, l);
    }

    public EmojiconTextView getMiddleButton() {
        return mMiddleButton;
    }


}
