package com.zhang.demo.ytx.ui.chatting.view;

import android.content.Context;
import android.content.res.Configuration;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.zhang.demo.ytx.R;
import com.zhang.demo.ytx.common.CCPAppManager;
import com.zhang.demo.ytx.common.base.CCPDotView;
import com.zhang.demo.ytx.common.base.CCPFlipper;
import com.zhang.demo.ytx.common.utils.DensityUtil;
import com.zhang.demo.ytx.common.utils.EmotionUtil;
import com.zhang.demo.ytx.common.utils.LogUtil;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * emoji表情框
 * Created by Administrator on 2016/7/28.
 */
public class SmileyPanel extends ChatFooterPanel implements
        CCPFlipper.OnFlipperPageListener,
        CCPFlipper.OnCCPFlipperMeasureListener{
    private static int APP_PANEL_HEIGHT_LANDSCAPE = 122;
    private static int APP_PANEL_HEIGHT_PORTRAIT = 179;

    private int mDefaultVerticalSpacing = DensityUtil.fromDPToPix(CCPAppManager.getContext(), 48);

    /** default App panel emoji */
    public static String APP_PANEL_NAME_DEFAULT = "emoji";
    private Context mContext;
    private int mEmojiPanelHeight = -1;
    private WindowManager mWindowManager;

    private CCPFlipper mFlipper;
    private CCPDotView mDotView;

    private ArrayList<EmojiGrid> mArrayList;
    private boolean mInitPanelHeight;



    public SmileyPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        inflate(context, R.layout.ccp_smile_panel, this);
        mFlipper = (CCPFlipper) findViewById(R.id.smiley_panel_flipper);
        mDotView = (CCPDotView) findViewById(R.id.smiley_panel_dot);

        initEmojiPanel();
    }

    /**
     * init {@link SmileyPanel} Child View
     * {@link EmojiGrid}
     */
    private void initEmojiPanel() {
        mFlipper.removeAllViews();
        mFlipper.setOnFlipperListener(this);
        mFlipper.setOnCCPFlipperMeasureListener(this);

        mInitPanelHeight = true;

        View smileyPanelDisplayView = findViewById(R.id.smiley_panel_display_view);
        LinearLayout.LayoutParams layoutParams = (LayoutParams) smileyPanelDisplayView.getLayoutParams();
        if (getWindowDisplayMode() == Configuration.ORIENTATION_LANDSCAPE) {
            layoutParams.height = DensityUtil.getMetricsDensity(getContext(), APP_PANEL_HEIGHT_LANDSCAPE);
        } else {
            int panelHeight = DensityUtil.getMetricsDensity(getContext(), APP_PANEL_HEIGHT_PORTRAIT);
            if (mEmojiPanelHeight > 0) {
                panelHeight = mEmojiPanelHeight;
            }
            layoutParams.height = panelHeight;
        }
        smileyPanelDisplayView.setLayoutParams(layoutParams);

        if (mArrayList != null && mArrayList.size() > 0) {
            Iterator<EmojiGrid> iterator = mArrayList.iterator();
            while (iterator.hasNext()) {
                EmojiGrid emojiGrid = iterator.next();
                if (emojiGrid != null) {
                    emojiGrid.releaseEmojiView();
                }
            }
            mArrayList.clear();
        }
    }

    /**
     * 获取屏幕状态，竖屏/横屏
     * @return
     */
    private int getWindowDisplayMode() {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        }
        Display localDisplay = mWindowManager.getDefaultDisplay();
        return localDisplay.getWidth() < localDisplay.getHeight() ? Configuration.ORIENTATION_PORTRAIT : Configuration.ORIENTATION_LANDSCAPE;
    }

    public void swicthToPanel(String panelName) {
        setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(panelName)) {
            return;
        }
        if (mArrayList == null) {
            mArrayList = new ArrayList<>();
        }
        if (APP_PANEL_NAME_DEFAULT.equals(panelName)) {
            doEmoji(mArrayList);
        }
    }

    /**
     * 获取emojiGrid纵向间隔高度
     * @return
     */
    private int getSmileyPanelVerticalSpacing() {
        if (getWindowDisplayMode() != Configuration.ORIENTATION_LANDSCAPE) {
            return (mEmojiPanelHeight - (3 * mDefaultVerticalSpacing)) / 4;
        }
        return (APP_PANEL_HEIGHT_LANDSCAPE - (2 * mDefaultVerticalSpacing)) / 3;
    }

    /**
     * 给emoji表情容器设置数据
     */
    private void doEmoji(ArrayList<EmojiGrid> mArrayList) {
        mFlipper.removeAllViews();
        if (mArrayList == null || mArrayList.size() <= 0) {
            doEmoji();
            return;
        }

        Iterator<EmojiGrid> iterator = mArrayList.iterator();
        while (iterator.hasNext()) {
            EmojiGrid emojiGrid = iterator.next();
            mFlipper.addView(emojiGrid, new LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT
            ));
            emojiGrid.setOnEmojiItemClickListener(mItemClickListener);
        }

        if (mArrayList.size() <= 1) {
            mDotView.setVisibility(View.VISIBLE);
            return;
        }

        mDotView.setVisibility(View.VISIBLE);
        mDotView.setDotCount(mArrayList.size());
        mDotView.setSelectedDot(mFlipper.getVisiableIndex());
    }


    /**
     * 给emoji表情容器设置数据
     */
    private void doEmoji() {
        mFlipper.removeAllViews();

        int pageCount = (int) Math.ceil(EmotionUtil.getInstatnce().getEmojiSize() / 20 + 0.1);
        LogUtil.d("AppPanel.doEmoji emoji total pageCount :" + pageCount);
        //循环将emoji表情分页生成EmojiGrid
        for (int i = 0; i < pageCount; i++) {
            EmojiGrid emojiGrid = new EmojiGrid(getContext());
            emojiGrid.initEmojiGrid(20, i, pageCount, i + 1, 7, getWidth());
            emojiGrid.setOnEmojiItemClickListener(mItemClickListener);
            mFlipper.addView(emojiGrid, new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT));

            mFlipper.setInterceptTouchEvent(true);

            mArrayList.add(emojiGrid);
        }

        if (mArrayList.size() <= 1) {
            mDotView.setVisibility(View.INVISIBLE);
            return;
        }

        mDotView.setVisibility(View.VISIBLE);
        mDotView.setDotCount(pageCount);
        mDotView.setSelectedDot(0);
    }

    /**
     * Change the height of the panel
     * @param height
     */
    public final void setPanelHeight(int height) {
        if (mEmojiPanelHeight == height) {
            return;
        }
        mEmojiPanelHeight = height;
        mInitPanelHeight = true;
    }

    public void setPanelGone() {
        mFlipper.removeAllViews();
        mDotView.removeAllViews();

        doChatTools();
    }

    /**
     * 隐藏控件视图
     */
    public void doChatTools() {
        setVisibility(View.GONE);
    }

    /**
     * 释放控件资源
     */
    public void releaseSmileyPanel() {
        setPanelGone();
        mFlipper.setOnFlipperListener(null);
        mFlipper.setOnCCPFlipperMeasureListener(null);

        if (mArrayList != null && mArrayList.size() > 0) {
            Iterator<EmojiGrid> iterator = mArrayList.iterator();
            while (iterator.hasNext()) {
                EmojiGrid emojiGrid = iterator.next();
                if (emojiGrid != null) {
                    emojiGrid.releaseEmojiView();
                }
            }
            mArrayList.clear();
        }

        mFlipper = null;
        mDotView = null;
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestory() {
        super.onDestory();
        releaseSmileyPanel();
    }

    @Override
    public void reset() {

    }

    @Override
    public void onCCPFlipperMeasure(int widthMeasureSpec, int heightMeasureSpec) {

    }

    @Override
    public void onFlipperPage(int startIndex, int finalIndex) {
        if (mDotView == null) {
            return;
        }
        if (finalIndex > mDotView.getDotCount()) {
            finalIndex = mDotView.getDotCount();
        }
        mDotView.setSelectedDot(finalIndex);
    }

    @Override
    public void setChatFooterPanelHeight(int height) {

    }
}
