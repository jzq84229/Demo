package com.zhang.demo.ytx.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zhang.demo.ytx.R;

/**
 * 网络提醒BannerView
 * Created by Administrator on 2016/7/12.
 */
public class NetWarnBannerView extends LinearLayout {
    private View mContentLayout;
    private ImageView mNetWarnIcon;
    private TextView mNetDetail;
    private TextView mNetDetailTips;
    private TextView mNetHintTips;
    private ProgressBar mProgressBar;

    public NetWarnBannerView(Context context) {
        this(context, null);
    }

    public NetWarnBannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NetWarnBannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View.inflate(getContext(), R.layout.net_warn_item, this);
        mContentLayout = findViewById(R.id.nwview);
        mNetWarnIcon = (ImageView) findViewById(R.id.nw_icon);
        mNetDetail = (TextView) findViewById(R.id.nw_detail);
        mNetDetailTips = (TextView) findViewById(R.id.nw_detail_tip);
        mNetHintTips = (TextView) findViewById(R.id.nw_hint_tip);
        mProgressBar = (ProgressBar) findViewById(R.id.nw_prog);
    }

    public final void setNetWarnText(CharSequence text) {
        mNetDetail.setText(text);
        mProgressBar.setVisibility(View.GONE);
        mContentLayout.setVisibility(View.VISIBLE);
    }

    public final void setNetWarnDetailTips(CharSequence text) {
        mNetDetailTips.setText(text);
        mProgressBar.setVisibility(View.GONE);
        mContentLayout.setVisibility(View.VISIBLE);
    }

    public final void setNetWarnHintText(CharSequence text) {
        mNetHintTips.setText(text);
        mProgressBar.setVisibility(View.GONE);
        mContentLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏网络提示栏
     */
    public void hideWarnBannerView() {
        if (mContentLayout == null) {
            return;
        }
        mContentLayout.setVisibility(View.GONE);
    }

    /**
     * 重新连接
     */
    public final void reconnect(boolean reconnect) {
        mContentLayout.setVisibility(View.VISIBLE);
        if (reconnect) {
            mProgressBar.setVisibility(View.VISIBLE);
            mNetWarnIcon.setVisibility(View.INVISIBLE);
            return;
        }
        mProgressBar.setVisibility(View.GONE);
        mNetWarnIcon.setVisibility(VISIBLE);
    }
}
