package com.zhang.common.lib.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by John on 2017/6/12.
 */

public abstract class BaseRecycleAdapter<T> extends RecyclerView.Adapter<BaseRecycleItemHolder> {
    public Context mContext;
    public List<T> mData;
    protected View mView;

    public BaseRecycleAdapter(Context context) {
        mContext = context;
    }

    public BaseRecycleAdapter(Context context, List<T> list) {
        mContext = context;
        mData = list;
    }

    public T getItem(int position) {
        return mData.get(position);
    }

    public void setData(List<T> data) {
        mData = data;
        onSetData(mData);
    }
    public void onSetData(List<T> data){

    }
    public void appendData(List<T> data){
        mData.addAll(data);
    }
    public List<T> getData() {
        return mData;
    }

    @Override
    public BaseRecycleItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mView = LayoutInflater.from(mContext).inflate(getItemLayoutId(), parent, false);
        BaseRecycleItemHolder holder = new BaseRecycleItemHolder(mView);
        return holder;
    }

    @Override
    public int getItemCount() {
        if (mData == null) {
            return 0;
        }
        return mData.size();
    }

    abstract public int getItemLayoutId();
}
