package com.zhang.demo.ytx.ui.chatting.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhang.demo.ytx.R;

/**
 * 聊天内容项容器
 * Created by Administrator on 2016/8/1.
 */
public class ChattingItemContainer extends RelativeLayout {
    private static final String LOG_TAG = ChattingItemContainer.class.getSimpleName();

    private int mResource;
    private LayoutInflater mInflater;


    public ChattingItemContainer(LayoutInflater inflater, int resource) {
        super(inflater.getContext());
        mInflater = inflater;
        mResource = resource;

        //add timeView for chatting item.
        TextView textView = new TextView(getContext(), null, R.style.ChattingUISplit);
        textView.setId(R.id.chatting_time_tv);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12.0F);
        LayoutParams textViewLayoutParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        textViewLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        textViewLayoutParams.setMargins(0, getResources().getDimensionPixelSize(R.dimen.NormalPadding), 0, getResources().getDimensionPixelSize(R.dimen.NormalPadding));

        addView(textView, textViewLayoutParams);

        //add message content view
        View chattingView = mInflater.inflate(mResource, null);
        int id = chattingView.getId();
        if (id == -1) {
            id = R.id.chatting_content_area;
            chattingView.setId(id);
        }

        LayoutParams chattingViewLayoutParams = new  LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        chattingViewLayoutParams.addRule(RelativeLayout.BELOW, R.id.chatting_time_tv);
        chattingViewLayoutParams.addRule(RelativeLayout.LEFT_OF, R.id.chatting_checkbox);
        addView(chattingView, chattingViewLayoutParams);

        View maskView = new View(getContext());
        maskView.setId(R.id.chatting_maskview);
        maskView.setVisibility(View.GONE);
        LayoutParams maskViewLayoutParams = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);

        maskViewLayoutParams.addRule(RelativeLayout.ALIGN_TOP , id);
        maskViewLayoutParams.addRule(RelativeLayout.ALIGN_BOTTOM , id);
        addView(maskView, maskViewLayoutParams);
    }


}
