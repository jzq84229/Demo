package com.zhang.mydemo.simleyparse;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.zhang.mydemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zjun on 2015/8/25 0025.
 */
public class EmotionAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<ChatEmoji> list = new ArrayList<>();

    public EmotionAdapter(Context context, List<ChatEmoji> list){
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ChatEmoji getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatEmoji emoji = getItem(position);
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.emotion_gridview_item, parent, false);
            holder.img_smiley = (ImageView) convertView.findViewById(R.id.img_smiley);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(emoji.getId() == R.drawable.smiley_del_ico) {
            convertView.setBackgroundDrawable(null);
            holder.img_smiley.setImageResource(emoji.getId());
        } else if(TextUtils.isEmpty(emoji.getCharacter())) {
            convertView.setBackgroundDrawable(null);
            holder.img_smiley.setImageDrawable(null);
        } else {
//            viewHolder.img_smiley.setTag(emoji);
            holder.img_smiley.setImageResource(emoji.getId());
        }
        holder.img_smiley.setImageResource(emoji.getId());
        return convertView;
    }

    private static class ViewHolder {
        public ImageView img_smiley;
    }
}
