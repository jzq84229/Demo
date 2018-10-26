package com.zhang.demo.ytx.ui.chatting.model;

/**
 * emoji对象Entity
 * Created by Administrator on 2016/7/11.
 */
public class CCPEmoji {
    /** Expression corresponding resource picture ID */
    private int id;
    /** Expression resources corresponding text description */
    private String EmojiDesc;
    /** File name expression resources */
    private String EmojiName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmojiDesc() {
        return EmojiDesc;
    }

    public void setEmojiDesc(String emojiDesc) {
        EmojiDesc = emojiDesc;
    }

    public String getEmojiName() {
        return EmojiName;
    }

    public void setEmojiName(String emojiName) {
        EmojiName = emojiName;
    }
}
