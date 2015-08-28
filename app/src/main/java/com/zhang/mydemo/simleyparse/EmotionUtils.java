package com.zhang.mydemo.simleyparse;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;

import com.zhang.mydemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zjun on 2015/8/26 0026.
 */
public class EmotionUtils {
    private Context mContext;

    /** 每一页表情的个数 */
    private int pageSize = 20;

//    public static final int DEL_ICO_ID = -9;

    private final String[] mSmileyTexts;
    private final int[] mSileyResIds;
    private List<List<ChatEmoji>> emojiPageList = new ArrayList<>();
    private static EmotionUtils instance;

    private EmotionUtils(Context context){
        this.mContext = context;
        SmileyParser.init(context);
        mSmileyTexts = SmileyParser.getInstance().getSmileyTexts();
        mSileyResIds = SmileyParser.getInstance().getSmileyResIds();
    }

    public static EmotionUtils getInstance(Context context){
        if (instance == null){
            instance = new EmotionUtils(context);
        }
        return instance;
    }

    private List<List<ChatEmoji>> initSmileyPageList(){
        List<List<ChatEmoji>> list = new ArrayList<>();
        List<ChatEmoji> emojiList = new ArrayList<>();
        for (int i = 0; i < mSmileyTexts.length; i++) {
            int resId = mSileyResIds[i];
            if (resId != 0) {
                ChatEmoji chatEmoji = new ChatEmoji();
                chatEmoji.setId(resId);
                chatEmoji.setCharacter(mSmileyTexts[i]);
                emojiList.add(chatEmoji);
            }
        }
        int pageCount = (int) Math.ceil((double)emojiList.size() / pageSize);
        for (int i = 0; i < pageCount; i++) {
            list.add(getEmojiPageList(emojiList, i));
        }
        emojiPageList = list;
        return emojiPageList;
    }

    private List<ChatEmoji> getEmojiPageList(List<ChatEmoji> emojis, int page){
        int startIndex = page * pageSize;
        int endIndex = startIndex + pageSize;

        if (endIndex > emojis.size()) {
            endIndex = emojis.size();
        }
        // 不这么写，会在viewpager加载中报集合操作异常，我也不知道为什么
        List<ChatEmoji> list = new ArrayList<ChatEmoji>();
        list.addAll(emojis.subList(startIndex, endIndex));
        if (list.size() < pageSize) {
            for (int i = list.size(); i < pageSize; i++) {
                ChatEmoji object = new ChatEmoji();
                list.add(object);
            }
        }
        if (list.size() == pageSize) {
            ChatEmoji object = new ChatEmoji();
            object.setId(R.drawable.smiley_del_ico);
            list.add(object);
        }
        return list;
    }

    public List<List<ChatEmoji>> getSmileyPageList(){
        if (emojiPageList.isEmpty()) {
            return initSmileyPageList();
        } else {
            return emojiPageList;
        }
    }

    /**
     * 添加表情
     */
    public SpannableString addFace(int resId, String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }

        SpannableString spannable = new SpannableString(str);
        spannable.setSpan(new ImageSpan(mContext, resId),
                0, str.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

}
