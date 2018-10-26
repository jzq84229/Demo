package com.zhang.demo.ytx.ui.chatting.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.zhang.demo.ytx.R;
import com.zhang.demo.ytx.ui.chatting.model.CCPEmoji;

import java.util.ArrayList;

/**
 * emoji表情适配器
 * Created by Administrator on 2016/7/25.
 */
public class EmojiAdapter extends BaseAdapter {

    private ArrayList<CCPEmoji> emojis;
    private LayoutInflater mInflater;

    public EmojiAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (emojis != null && emojis.size() > 0) {
            return emojis.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (emojis != null && position <= emojis.size() - 1) {
            return emojis.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null || convertView.getTag() == null) {
            convertView = mInflater.inflate(R.layout.emoji_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.emoji_icon = (ImageView) convertView.findViewById(R.id.emoji_id);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (getCount() - 1 == position) {
            viewHolder.emoji_icon.setImageResource(R.drawable.emoji_del_selector);
        } else {
            CCPEmoji emoji = (CCPEmoji) getItem(position);
            if (emoji != null) {
                if (emoji.getId() == R.drawable.emoji_del_selector) {
                    convertView.setBackgroundDrawable(null);
                    viewHolder.emoji_icon.setImageResource(emoji.getId());
                } else if (TextUtils.isEmpty(emoji.getEmojiDesc())) {
                    convertView.setBackgroundDrawable(null);
                    viewHolder.emoji_icon.setImageDrawable(null);
                } else {
                    viewHolder.emoji_icon.setTag(emoji);
                    viewHolder.emoji_icon.setImageResource(emoji.getId());
                }
            }
        }
        return convertView;
    }

    public void release(){
        if (emojis != null) {
            emojis.clear();
            emojis = null;
        }
        mInflater = null;
    }

    /**
     * 更新emoji表情数据
     * @param emojis
     */
    public void updateEmoji(ArrayList<CCPEmoji> emojis) {
        this.emojis = emojis;
        if (this.emojis == null) {
            this.emojis = new ArrayList<>();
        }
        notifyDataSetChanged();
    }

    private class ViewHolder {
        public ImageView emoji_icon;
    }

}
