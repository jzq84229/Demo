package com.zhang.demo.ytx.ui.chatting.view;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.zhang.demo.ytx.common.emoji.Emojicon;
import com.zhang.demo.ytx.common.utils.DensityUtil;
import com.zhang.demo.ytx.common.utils.EmotionUtil;
import com.zhang.demo.ytx.common.utils.LogUtil;
import com.zhang.demo.ytx.ui.chatting.model.CCPEmoji;

import java.util.ArrayList;

/**
 * 自定义GridView
 * emoji表情GridView控件
 * Created by Administrator on 2016/7/25.
 */
public class EmojiGrid extends GridView implements AdapterView.OnItemClickListener {
    private Context mContext;
    private EmojiAdapter mEmojiAdapter;
    private int emojiMode = 20;
    private int perPage;
    private int totalPage;          //总页数
    private int curPage;            //当前页数
    private int numColumns;         //GridView列数
    private int gridViewWidth;      //视图宽度
    private int mItemWidthInPix;
    private OnEmojiItemClickListener mItemClickListener;


    public EmojiGrid(Context context) {
        super(context);
        mContext = context;
        initEmojiLayout(context);
    }

    public EmojiGrid(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initEmojiLayout(context);
    }

    /**
     * 初始化EmojiGrid视图
     * @param context
     */
    private void initEmojiLayout(Context context) {
        mEmojiAdapter = new EmojiAdapter(context);
        setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        setBackgroundResource(0);
        setSelector(new ColorDrawable(Color.TRANSPARENT));
        setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        setGravity(Gravity.CENTER);

        switch (emojiMode) {
            case 21:
                mItemWidthInPix = DensityUtil.fromDPToPix(getContext(), 43);
                break;
            case 20:
                mItemWidthInPix = DensityUtil.fromDPToPix(getContext(), 80);
                break;
            default:
                break;
        }
        LogUtil.d("EmojiGrid.initEmojiLayout mItemWidthInPix:" + mItemWidthInPix);
        setColumnWidth(mItemWidthInPix);
        setAdapter(mEmojiAdapter);
        setOnItemClickListener(this);

        int left = DensityUtil.fromDPToPix(getContext(), 6);
        int top = DensityUtil.fromDPToPix(getContext(), 6);
        int right = DensityUtil.fromDPToPix(getContext(), 6);
        LogUtil.d("EmojiGrid.initEmojiLayout paddingLeft:"
                + left + " ,paddingRight:" + right + ",paddingTop:" + top);
        setPadding(left, top, right, 0);
    }

    public final void setPanelVerticalSpacing(int verticalSpacing) {
        if (verticalSpacing <= 0) {
            return;
        }
        int padding = DensityUtil.getMetricsDensity(mContext, 10.0F);
        setPadding(padding, verticalSpacing, padding, 0);
        setVerticalSpacing(verticalSpacing / 2);
    }

    /**
     * 设置EmojiGrid的参数
     * @param mode              类型
     * @param perPage
     * @param totalPage         总页数
     * @param curPage           当前页数
     * @param numColumns        gridView列数
     * @param gridViewWidth     视图总宽度
     */
    public void initEmojiGrid(int mode, int perPage, int totalPage, int curPage, int numColumns, int gridViewWidth) {
        LogUtil.d("EmojiGrid.initEmojiGrid mode:" + mode
                + " , perPage:" + perPage + " , totalPage:" + totalPage
                + "  ,curPage:" + curPage);
        this.emojiMode = mode;
        this.perPage = perPage;
        this.totalPage = totalPage;
        this.curPage = curPage;
        this.numColumns = numColumns;
        this.gridViewWidth = gridViewWidth;
        if (numColumns == 7) {
            setColumnWidth(gridViewWidth / 7);
        } else {
            setColumnWidth(gridViewWidth / 14);
        }

        setNumColumns(numColumns);
        mEmojiAdapter.updateEmoji(getPageEmoji(curPage));
    }

    /**
     * 获取第page页的emoji列表
     * @param page      第几页
     * @return          ArrayList<CCPEmoji>
     */
    private ArrayList<CCPEmoji> getPageEmoji(int page) {
        page--;
        int startIndex = page * emojiMode;
        int endIndex = startIndex + emojiMode;

        ArrayList<CCPEmoji> emojiCache = EmotionUtil.getInstatnce().getEmojiCache();
        if (emojiCache != null) {
            if (endIndex > emojiCache.size()) {
                endIndex = emojiCache.size();
            }
            ArrayList<CCPEmoji> list = new ArrayList<>();
            list.addAll(emojiCache.subList(startIndex, endIndex));
            return list;
        }
        return null;
    }


    @Override

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mEmojiAdapter != null) {
            if (mItemClickListener != null) {
                if (position == mEmojiAdapter.getCount() - 1) {
                    mItemClickListener.onEmojiDelClick();
                    return;
                }

                CCPEmoji item = (CCPEmoji) mEmojiAdapter.getItem(position);
                mItemClickListener.onEmojiItemClick(item.getId(), item.getEmojiName());
            }
        }
    }

    /**
     * 释放emojiView的资源
     */
    public void releaseEmojiView() {
        mItemClickListener = null;
        if (mEmojiAdapter != null) {
            mEmojiAdapter.release();
        }
        mEmojiAdapter = null;
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    /**
     * Interface definition for a callback to be invoked when an item in this
     * AdapterView has been clicked.
     */
    public interface OnEmojiItemClickListener {

        /**
         * Callback method to be invoked when an item in this EmojiGird View has
         * been clicked.
         *
         * @param emojiid
         * @param emojiName
         */
        void onEmojiItemClick(int emojiid, String emojiName);

        /**
         * Callback method to be invoked when an item  of del in this EmojiGird View has
         * been clicked.
         */
        void onEmojiDelClick();
    }

    /**
     * Register a callback to be invoked when an item in this EmojiGird View has
     * been clicked.
     *
     * @param listener The callback that will be invoked.
     */
    public void setOnEmojiItemClickListener(OnEmojiItemClickListener listener) {
        mItemClickListener = listener;
    }

    /**
     * @return The callback to be invoked with an item in this EmojiGird View has
     * been clicked, or null id no callback has been set.
     */
    public final OnEmojiItemClickListener getOnEmojiItemClickListener() {
        return mItemClickListener;
    }
}
