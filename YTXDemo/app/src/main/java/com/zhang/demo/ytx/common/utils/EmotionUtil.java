package com.zhang.demo.ytx.common.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;

import com.zhang.demo.ytx.ECApplication;
import com.zhang.demo.ytx.R;
import com.zhang.demo.ytx.common.emoji.Emojicon;
import com.zhang.demo.ytx.common.emoji.EmojiconHandler;
import com.zhang.demo.ytx.common.emoji.People;
import com.zhang.demo.ytx.ui.chatting.model.CCPEmoji;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 表情处理工具类, 单例
 * Created by Administrator on 2016/7/8.
 */
public class EmotionUtil {

    private static HashMap<String, SpannableString> hashMap = new HashMap<>();
    private final HashMap<String, SoftReference<Bitmap>> emojiCache = new HashMap<>();
    private final HashMap<String, Integer> emojiKeyValue = new HashMap<>();
    private static ArrayList<CCPEmoji> emojis = new ArrayList<>();
    private static HashMap<String, String> emojiMap = new HashMap<>();
    private static EmotionUtil mEmotionUtils;

    private EmotionUtil() {}

    public static EmotionUtil getInstatnce() {
        if (mEmotionUtils == null) {
            mEmotionUtils = new EmotionUtil();
        }
        return mEmotionUtils;
    }

    /**
     * 获取emoji图片资源ID
     * @param emojiUnicode
     * @param c
     * @return
     */
    public static Integer getEmotionResId(String emojiUnicode, Context c) {
        //根据文件名获取资源的ID
        return c.getResources().getIdentifier("emoji_" + emojiUnicode, "drawable", c.getPackageName());
    }

    /**
     *
     * @param emojiName
     * @return
     */
    public static String formatFaces(String emojiName) {
        StringBuffer sb = new StringBuffer();
        sb.append("<img src=\"emoji_");
        sb.append(emojiName);
        sb.append("\">");
        return sb.toString();
    }

    /**
     *
     * @param c
     * @return
     */
    public static Html.ImageGetter getImageGetter(final Context c) {
        return new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                System.out.println("source =  " + source);
                Integer resID = c.getResources().getIdentifier(source, "drawable", c.getPackageName());
                Drawable d = c.getResources().getDrawable(resID);
                d.setBounds(0, 0, 24, 24);
                return d;
            }
        };
    }

    public static void initEmoji() {
        Emojicon[] data = People.DATA;
        if (data == null) {
            return;
        }
        for (Emojicon emojicon : data) {
            if (emojicon.getEmoji() != null) {
                CCPEmoji emoji = new CCPEmoji();
                emoji.setEmojiName(emojicon.getEmoji());
                emoji.setEmojiDesc(emojicon.getEmoji());
                int unicode = Character.codePointAt(emojicon.getEmoji(), 0);
                if (unicode > 0xff) {
                    emoji.setId(EmojiconHandler.getEmojiResource(ECApplication.getInstance().getApplicationContext(), unicode));
                    emojis.add(emoji);
                }
            }
        }
    }



    public boolean emojiFilter(String filter) {
        if (TextUtils.isEmpty(filter)) {
            return false;
        }
        if ("emoji_custom_bg".equals(filter)
                || "emoji_del_selector".equals(filter)
                || "emoji_icon_selector".equals(filter)
                || "emoji_item_selector".equals(filter)
                || "emoji_press".equals(filter)) {
            return false;
        }
        return true;
    }


    public ArrayList<CCPEmoji> getEmojiCache() {
        return emojis;
    }

    public static int getEmojiSize() {
        return emojis.size();
    }

    public void release() {
        if (emojiCache != null) {
            Iterator<SoftReference<Bitmap>> iter = emojiCache.values().iterator();
            while (iter.hasNext()) {
                SoftReference<Bitmap> sr = iter.next();
                if (sr != null && sr.get() != null) {
                    sr.get().recycle();
                }
            }
            emojiCache.clear();
        }
        emojis.clear();
    }

    /**
     * 将字符串转成Unicode
     * The emo (local expression file name) into the corresponding Emoji
     * expression Unicode coding, each UTF-16 code takes two bytes
     *
     * @param emo The local expression of file name for example: emoji_e415.png
     * @return
     */
    public static String convertUnicode(String emo) {// e403
                                                     // 1f615
        emo = emo.substring(emo.indexOf("_") + 1);
        if (emo.length() < 6) {
            String d = new String(Character.toChars(Integer.parseInt(emo, 16)));
            return d;
        }
        String[] emos = emo.split("_");

        char[] char0 = Character.toChars(Integer.parseInt(emos[0], 16));
        char[] char1 = Character.toChars(Integer.parseInt(emos[1], 16));
        char[] emoji = new char[char0.length + char1.length];
        for (int i = 0; i < char0.length; i++) {
            emoji[i] = char0[i];
        }
        for (int i = char0.length; i < emoji.length; i++) {
            emoji[i] = char1[i - char0.length];
        }
        String s = new String(emoji);
        LogUtil.d(emo + "1: " + s);
        return s;
    }

    /**
     * 将换行符替换为空格
     * @param str       替换前字符串
     * @return          str
     */
    private static CharSequence replaceLinebreak(CharSequence str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }

        if (str.toString().contains("\n")) {
            return str.toString().replace("\n", " ");
        }
        return str;
    }

    public static boolean containsKeyEmoji(Context context, SpannableString spannableString, int textSize) {
        if (TextUtils.isEmpty(spannableString)) {
            return false;
        }

        boolean isEmoji = false;
        char[] charArray = spannableString.toString().toCharArray();
        int i = 0;
        while (i < charArray.length) {
            int emojiId = getEmojiId(charArray[i]);
            if (emojiId != -1) {
                Drawable drawable = EmotionUtil.getEmotionDrawable(context, emojiId);
                if (drawable != null) {
                    drawable.setBounds(0, 0, (int) (1.3D * textSize), (int) (1.3D * textSize));
                    spannableString.setSpan(new ImageSpan(drawable, 0), i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    isEmoji = true;
                }
            }
        }
        i++;

        return isEmoji;
    }

    /**
     * 根据文件名获，取资源图片
     * @param context
     * @param emojiId
     * @return
     */
    private static Drawable getEmotionDrawable(Context context, int emojiId) {
        Drawable drawable = null;
        if (context == null || emojiId == -1) {
            return drawable;
        }

        int identifier = context.getResources().getIdentifier("emoji_" + emojiId, "drawable", context.getPackageName());
        if (identifier != 0) {
            drawable = context.getResources().getDrawable(identifier);
        }
        return drawable;
    }

    /**
     * Replace the not support emoji.
     * @param str
     * @return
     */
    public static String matchEmojiUnicode(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] charArray = str.toCharArray();
        try {
            for (int i = 0; i < charArray.length - 1; i++) {
                int _index = charArray[i];
                int _index_inc = charArray[i + 1];
                if (_index == 55356) {
                    if (_index_inc < 56324 || _index_inc > 57320) {
                        continue;
                    }
                    charArray[i] = '.';
                    charArray[(i + 1)] = '.';
                    continue;
                }

                if (_index != 55357 || _index_inc < 56343 || _index_inc > 57024) {
                    continue;
                }

                charArray[i] = '.';
                charArray[(i + 1)] = '.';
            }
        } catch (Exception e) {
        }

        return new String(charArray);
    }

    private static int getEmojiId(char charStr) {
        int i = -1;
        if (charStr < 57345 || charStr > 58679) {
            return i;
        }
        if ((charStr >= 57345) && (charStr <= 57434)) {
            i = charStr - 57345;
        } else if ((charStr >= 57601) && (charStr <= 57690)) {
            i = charStr + 'Z' - 57601;
        } else if ((charStr >= 57857) && (charStr <= 57939)) {
            i = charStr + '´' - 57857;
        } else if ((charStr >= 58113) && (charStr <= 58189)) {
            i = charStr + 'ć' - 58113;
        } else if ((charStr >= 58369) && (charStr <= 58444)) {
            i = charStr + 'Ŕ' - 58369;
        } else if ((charStr >= 58625) && (charStr <= 58679)) {
            i = charStr + 'Ơ' - 58625;
        }
        return i;
    }

    public static SpannableString getTextFormat(Context context, String formate, int paddingSize) {
        if (TextUtils.isEmpty(formate)) {
            return new SpannableString("");
        }
        String strValue = replaceLinebreak(formate).toString();
        switch (paddingSize) {
            case -1:
                paddingSize = context.getResources().getDimensionPixelSize(R.dimen.ccp_button_text_size);
                break;
            case -2:
                paddingSize = context.getResources().getDimensionPixelSize(R.dimen.primary_text_size);
                break;
            default:
                break;
        }
        String key = strValue + "@" + paddingSize;
        String source = matchEmojiUnicode(strValue);
        SpannableString object = hashMap.get(key);
        if (object == null) {
            object = new SpannableString(source);
        }
        boolean containkeyEmoji = containsKeyEmoji(context, object, paddingSize);
        if (containkeyEmoji) {
            hashMap.put(key, object);
        }
        return object;
    }

    public static void main(String[] args) {
        String convertUnicode = convertUnicode("e415");
        char[] charArray = convertUnicode.toCharArray();
        int emojiId = getEmojiId(charArray[0]);
        System.err.println(emojiId);
    }

}
