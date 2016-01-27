package com.zhang.mydemo.stickyGridHeaders;

import android.content.Context;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleAdapter;
import com.zhang.mydemo.R;
import com.zhang.mydemo.util.Utils;

import java.util.List;

/**
 * Created by zjun on 2015/6/18.
 */
public class StickyGridAdatper extends BaseAdapter implements StickyGridHeadersSimpleAdapter {
    private List<GridItem> list;
    private LayoutInflater mInflater;
    private GridView mGridView;
    private Point mPoint = new Point(0, 0); //用来封装ImageView的宽和高的对象

    public StickyGridAdatper(Context context, List<GridItem> list, GridView mGridView) {
        mInflater = LayoutInflater.from(context);
        this.mGridView = mGridView;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public GridItem getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        GridItem item = getItem(position);
        if (convertView == null) {
            mViewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.sticky_grid_item, parent, false);
            mViewHolder.mImageView = (MyImageView) convertView.findViewById(R.id.grid_item);
            convertView.setTag(mViewHolder);

            //用来监听ImageView的宽和高
            mViewHolder.mImageView.setOnMeasureListener(new MyImageView.OnMeasureListener() {
                @Override
                public void onMeasureSize(int width, int height) {
                    mPoint.set(width, height);
                }
            });
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        ImageLoader.getInstance().displayImage(Utils.getImageFileUrl(item.getPath()), mViewHolder.mImageView);

        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return list.get(position).getSection();
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder mHeaderHolder;
        if (convertView == null) {
            mHeaderHolder = new HeaderViewHolder();
            convertView = mInflater.inflate(R.layout.sticky_grid_item_header, parent, false);
            mHeaderHolder.mTextView = (TextView) convertView.findViewById(R.id.header);
            convertView.setTag(mHeaderHolder);
        } else {
            mHeaderHolder = (HeaderViewHolder) convertView.getTag();
        }
        mHeaderHolder.mTextView.setText(list.get(position).getTime());
        return convertView;
    }


    private static class ViewHolder {
        public MyImageView mImageView;
    }

    private static class HeaderViewHolder {
        public TextView mTextView;
    }
}
