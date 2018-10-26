package com.zhang.demo.ytx.ui.chatting.holder;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zhang.demo.ytx.R;

/**
 * Created by Administrator on 2016/8/1.
 */
public class ImageRowViewHolder extends BaseHolder {
    public ImageView chattingContentIv;
    public View uploadingView;
    public TextView uploadingText;
    public ImageView maskView;
    public ImageView mGifIcon;


    public ImageRowViewHolder(int type) {
        super(type);
    }

//    public BaseHolder iniBaseHolder(View baseView, boolean receive) {
//        super
//    }

    /* (non-Javadoc)
	 * @see com.hisun.cas.model.BaseHolder#initBaseHolder(android.view.View)
	 */
    public BaseHolder initBaseHolder(View baseView, boolean receive) {
        super.initBaseHolder(baseView);

        chattingTime = (TextView) baseView.findViewById(R.id.chatting_time_tv);
        chattingContentIv = (ImageView) baseView.findViewById(R.id.chatting_content_iv);
        checkBox = (CheckBox) baseView.findViewById(R.id.chatting_checkbox);
        chattingMaskView = baseView.findViewById(R.id.chatting_maskview);
        uploadingView = baseView.findViewById(R.id.uploading_view);
        mGifIcon = (ImageView) baseView.findViewById(R.id.img_gif);

        if(receive) {
            chattingUser = (TextView) baseView.findViewById(R.id.chatting_user_tv);
            progressBar = (ProgressBar) baseView.findViewById(R.id.downloading_pb);
            type = 1;
        } else {
            progressBar = (ProgressBar) baseView.findViewById(R.id.uploading_pb);
            uploadingText = (TextView) baseView.findViewById(R.id.uploading_tv);
            chattingUser = (TextView) baseView.findViewById(R.id.chatting_user_tv);
            type = 2;
        }
        maskView = (ImageView) baseView.findViewById(R.id.chatting_content_mask_iv);

        return this;
    }
}
