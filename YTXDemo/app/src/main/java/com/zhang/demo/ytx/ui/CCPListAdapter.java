package com.zhang.demo.ytx.ui;

import android.content.Context;
import android.database.Cursor;
import android.widget.BaseAdapter;

import com.zhang.demo.ytx.storage.OnMessageChange;

import junit.framework.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * 定义抽象适配器， 继承扩展不同的消息
 * Created by Administrator on 2016/7/12.
 */
public abstract class CCPListAdapter<T> extends BaseAdapter implements OnMessageChange {
    /**数据Cursor*/
    private Cursor mCursor;
    /**数据缓存*/
    private Map<Integer, T> mData ;
    /**适配器使用数据类型*/
    protected T t;
    /**上下文对象*/
    protected Context mContext;
    /**数据总数*/
    protected int mCount;
    /**数据改变回调接口*/
    protected OnCursorChangeListener mOnCursorChangeListener;

    protected abstract void notifyChange();
    protected abstract void initCursor();
    protected abstract T getItem(T t, Cursor cursor);

    /** 数据变化监听接口 */
    public interface OnCursorChangeListener {
        void onCursorChangeBefore();
        void onCursorChangeAfter();
    }

    public interface OnListAdapterCallBackListener{
        void OnListAdapterCallBack();
    }

    /** 构造方法 */
    public CCPListAdapter(Context mContext, T t) {
        this.mContext = mContext;
        this.t = t;
        this.mCount = -1;
    }

    protected void setCursor(Cursor cursor) {
        mCursor = cursor;
        this.mCount = -1;
    }

    public void initCache() {
        if (mData != null) {
            return;
        }
        mData = new HashMap<>();
    }

    /** 返回一个数据类型Cursor */
    protected Cursor getCursor() {
        if (mCursor == null) {
            initCursor();
            Assert.assertNotNull(mCursor);
        }
        return mCursor;
    }

    /**
     * 设置数据库变化监听
     */
    public void setOnCursorChangeListener(OnCursorChangeListener listener) {
        this.mOnCursorChangeListener = listener;
    }

    /**
     * 清空数据库变化监听
     */
    public void resetListener() {
        this.mOnCursorChangeListener = null;
    }

    /**
     * 关闭数据库
     */
    public void closeCursor() {
        if (mData != null) {
            mData.clear();
        }
        if (mCursor != null) {
            mCursor.close();
        }
        mCount = -1;
    }



    @Override
    public int getCount() {
        if (mCount < 0) {
            mCount = getCursor().getCount();
        }
        return mCount;
    }

    @Override
    public T getItem(int position) {
        if (position < 0 || !getCursor().moveToPosition(position)) {
            return null;
        }
        if (mData == null) {
            return getItem(this.t, getCursor());
        }

        T _t = mData.get(Integer.valueOf(position));
        if (_t == null) {
            _t = getItem(null, getCursor());
        }
        mData.put(Integer.valueOf(position), _t);
        return _t;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public void onChanged(String sessionId) {
        if (mOnCursorChangeListener != null) {
            mOnCursorChangeListener.onCursorChangeBefore();
        }
        closeCursor();
        notifyChange();
        if (mOnCursorChangeListener == null) {
            return;
        }
        mOnCursorChangeListener.onCursorChangeAfter();
    }

}

