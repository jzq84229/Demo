package com.zhang.common.lib.adapter;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhang.common.lib.imageLoader.ImageLoader;


/**
 * Created by John on 2017/6/12.
 */

public class BaseRecycleItemHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews;
    private View mConvertView;

    public BaseRecycleItemHolder(View itemView) {
        super(itemView);
        mViews = new SparseArray<>();
        mConvertView = itemView;
    }
    /**
     * get view
     */
    public <T extends View> T getView(int viewId){
        View view = mViews.get(viewId);
        if(view == null){
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId,view);
        }
        return (T)view;
    }

    /**
     * set text
     */
    public BaseRecycleItemHolder setText(int viewId, String text){
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    public BaseRecycleItemHolder addTextChangedListener(int viewId,TextWatcher textWatcher){
        EditText tv = getView(viewId);
        tv.addTextChangedListener(textWatcher);
        return this;
    }

    /**
     *
     */
    public BaseRecycleItemHolder setText(int viewId, @StringRes int stringRid){
        TextView tv = getView(viewId);
        tv.setText(stringRid);
        return this;
    }
    public BaseRecycleItemHolder setTextColor(int viewId, @ColorInt int colorRid){
        TextView tv = getView(viewId);
        tv.setTextColor(colorRid);
        return this;
    }
    /**
     *  set image res
     */
    public BaseRecycleItemHolder setImageResource(int viewId, int resId){
        ImageView iv = getView(viewId);
        iv.setImageResource(resId);
        return this;
    }

    public BaseRecycleItemHolder loadImage(int viewId, String imageUrl){
        ImageView iv = getView(viewId);
        ImageLoader.loadImage(imageUrl, iv);
        return this;
    }
    public BaseRecycleItemHolder loadcircleAvatar(int viewId, String imageUrl){
        ImageView iv = getView(viewId);
        ImageLoader.loadImage(imageUrl, iv);
        return this;
    }
    /**
     *  set image bitmap
     */
    public BaseRecycleItemHolder setImageBitmap(int viewId, Bitmap bitmap){
        ImageView iv = getView(viewId);
        iv.setImageBitmap(bitmap);
        return this;
    }
    /**
     * 设置背景
     */
    public BaseRecycleItemHolder setBackgroundColor(int viewId, @ColorInt int color){
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }
    public BaseRecycleItemHolder setBackground(int viewId, Drawable drawable){
        View view = getView(viewId);
        view.setBackground(drawable);
        return this;
    }
    public BaseRecycleItemHolder setTag(int viewId, Object tag){
        View view = getView(viewId);
        view.setTag(tag);
        return this;
    }
    public BaseRecycleItemHolder setTag(int viewId, int key,Object tag){
        View view = getView(viewId);
        view.setTag(key,tag);
        return this;
    }
    public BaseRecycleItemHolder setOnClickListener(int viewId, View.OnClickListener onClickListener){
        View view = getView(viewId);
        view.setOnClickListener(onClickListener);
        return this;
    }
    public BaseRecycleItemHolder setVisibility(int viewId, int visible){
        View view = getView(viewId);
        view.setVisibility(visible);
        return this;
    }
    public BaseRecycleItemHolder setCheck(int viewId,boolean isCheck){
        View view = getView(viewId);
        if(view instanceof CheckBox){
            ((CheckBox)view).setChecked(isCheck);
        }
        return this;
    }
    public BaseRecycleItemHolder setClickable(int viewId, boolean click){
        View view = getView(viewId);
        view.setClickable(click);
        return this;
    }
}
