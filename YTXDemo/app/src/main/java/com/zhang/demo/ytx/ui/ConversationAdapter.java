package com.zhang.demo.ytx.ui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuntongxun.ecsdk.ECMessage;
import com.zhang.demo.ytx.R;
import com.zhang.demo.ytx.common.CCPAppManager;
import com.zhang.demo.ytx.common.emoji.EmojiconTextView;
import com.zhang.demo.ytx.common.utils.DateUtil;
import com.zhang.demo.ytx.common.utils.DemoUtils;
import com.zhang.demo.ytx.common.utils.ECPreferenceSettings;
import com.zhang.demo.ytx.common.utils.ECPreferences;
import com.zhang.demo.ytx.common.utils.ResourceHelper;
import com.zhang.demo.ytx.storage.ContactSqlManager;
import com.zhang.demo.ytx.storage.ConversationSqlManager;
import com.zhang.demo.ytx.storage.GroupMemberSqlManager;
import com.zhang.demo.ytx.storage.GroupNoticeSqlManager;
import com.zhang.demo.ytx.ui.chatting.model.Conversation;
import com.zhang.demo.ytx.ui.contact.ContactLogic;
import com.zhang.demo.ytx.ui.contact.ECContacts;
import com.zhang.demo.ytx.ui.group.GroupNoticeHelper;

import java.util.ArrayList;

/**
 * 会话ListView适配器
 * Created by Administrator on 2016/7/12.
 */
public class ConversationAdapter extends CCPListAdapter<Conversation> {

    private OnListAdapterCallBackListener mCallBackListener;
    int padding;
    private ColorStateList[] colorStateLists;
    private String isAtSession;

    public ConversationAdapter(Context mContext, OnListAdapterCallBackListener listener) {
        super(mContext, new Conversation());
        mCallBackListener = listener;
        padding = mContext.getResources().getDimensionPixelSize(R.dimen.OneDPPadding);
        colorStateLists = new ColorStateList[]{
                ResourceHelper.getColorStateList(mContext, R.color.normal_text_color),
                ResourceHelper.getColorStateList(mContext, R.color.ccp_list_textcolor_three)
        };
    }

    @Override
    protected Conversation getItem(Conversation t, Cursor cursor) {
        Conversation conversation = new Conversation();
        conversation.setCursor(cursor);
        if (conversation.getUsername() != null && conversation.getUsername().endsWith("@priategroup.com")) {
            //根据会话ID查询会话成员的ID集合
            ArrayList<String> member = GroupMemberSqlManager.getGroupMemberID(conversation.getSessionId());
            if (member != null) {
                //根据成员ID集合查询成员名称集合
                ArrayList<String> contactName = ContactSqlManager.getContactname(member.toArray(new String[]{}));
                if (contactName != null && !contactName.isEmpty()) {
                    //将成员名称集合转换成"，"分隔的字符串
                    String chatroomName = DemoUtils.listToString(contactName, ",");
                    conversation.setUsername(chatroomName);
                }
            }
        }
        return conversation;
    }

    /**
     * 会话时间
     */
    protected final CharSequence getConversationTime(Conversation conversation) {
        if (conversation.getSendStatus() == ECMessage.MessageStatus.SENDING.ordinal()) {
            return mContext.getString(R.string.conv_msg_sending);
        }
        if (conversation.getDateTime() <= 0) {
            return "";
        }
        return DateUtil.getDateString(conversation.getDateTime(), DateUtil.SHOW_TYPE_CALL_LOG).trim();
    }

    /**
     * 根据消息类型返回相应的主题描述
     * @param conversation      会话
     * @return
     */
    protected final CharSequence getConversationSnippet(Conversation conversation) {
        if (conversation == null) {
            return "";
        }
        //服务号, 获取会话内容
        if (GroupNoticeSqlManager.CONTACT_ID.equals(conversation.getSessionId())) {
            return GroupNoticeHelper.getNoticeContent(conversation.getContent());
        }

        String fromNickName = "";
        if (conversation.getSessionId().toUpperCase().startsWith("G")) {
            if (conversation.getContactId() != null && CCPAppManager.getClientUser() != null
                    && !conversation.getContactId().equals(CCPAppManager.getClientUser().getUserId())) {
                ECContacts contact = ContactSqlManager.getContact(conversation.getContactId());
                if (contact != null && contact.getNickname() != null) {
                    fromNickName = contact.getNickname() + ": ";
                } else {
                    fromNickName = conversation.getContactId() + ": ";
                }
            }
        }

        //Android Demo 免打扰后需要显示未读数
        if (!conversation.isNotice() && conversation.getUnreadCount() > 1) {
            fromNickName = "[" + conversation.getUnreadCount() + "条]" + fromNickName;
        }

        if (conversation.getMsgType() == ECMessage.Type.VOICE.ordinal()) {
            return fromNickName + mContext.getString(R.string.app_voice);
        } else if(conversation.getMsgType() == ECMessage.Type.FILE.ordinal()) {
            return fromNickName + mContext.getString(R.string.app_file);
        } else if(conversation.getMsgType() == ECMessage.Type.IMAGE.ordinal()) {
            return fromNickName + mContext.getString(R.string.app_pic);
        } else if(conversation.getMsgType() == ECMessage.Type.VIDEO.ordinal()) {
            return fromNickName + mContext.getString(R.string.app_video);
        }else if(conversation.getMsgType()==ECMessage.Type.LOCATION.ordinal()){
            return fromNickName + mContext.getString(R.string.app_location);
        }
        String snippet = fromNickName + conversation.getContent();
        if(conversation.getSessionId().equals(isAtSession)) {
            return Html.fromHtml(mContext.getString(R.string.conversation_at, snippet));
        }
        return snippet;
    }

    /**
     * 根据消息发送状态，返回对应图标
     * @param context            上下文
     * @param conversation       会话
     * @return                   Drawable
     */
    public static Drawable getChattingSnipentCompoundDrawables(Context context, Conversation conversation) {
        if (conversation.getSendStatus() == ECMessage.MessageStatus.FAILED.ordinal()) {
            return DemoUtils.getDrawables(context, R.drawable.msg_state_failed);
        } else if (conversation.getSendStatus() == ECMessage.MessageStatus.SENDING.ordinal()) {
            return DemoUtils.getDrawables(context, R.drawable.msg_state_sending);
        } else {
            return null;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder mViewHolder;
        if (convertView == null || convertView.getTag() == null) {
            view = View.inflate(mContext, R.layout.conversation_item, null);

            mViewHolder = new ViewHolder();
            mViewHolder.user_avatar = (ImageView) view.findViewById(R.id.avatar_iv);
            mViewHolder.prospect_iv = (ImageView) view.findViewById(R.id.avatar_prospect_iv);
            mViewHolder.nickname_tv = (EmojiconTextView) view.findViewById(R.id.nickname_tv);
            mViewHolder.tipcnt_tv = (TextView) view.findViewById(R.id.tipcnt_tv);
            mViewHolder.update_time_tv = (TextView) view.findViewById(R.id.update_time_tv);
            mViewHolder.last_msg_tv = (EmojiconTextView) view.findViewById(R.id.last_msg_tv);
            mViewHolder.image_input_text = (ImageView) view.findViewById(R.id.image_input_text);
            mViewHolder.image_mute = (ImageView) view.findViewById(R.id.image_mute);
            view.setTag(mViewHolder);
        } else {
            view = convertView;
            mViewHolder = (ViewHolder) view.getTag();
        }

        Conversation conversation = getItem(position);
        if (conversation != null) {
            if (TextUtils.isEmpty(conversation.getUsername())) {
                mViewHolder.nickname_tv.setText(conversation.getSessionId());
            } else {
                mViewHolder.nickname_tv.setText(conversation.getUsername());
            }
            handleDisplayNameTextColor(mViewHolder.nickname_tv, conversation.getSessionId());
            mViewHolder.last_msg_tv.setText(getConversationSnippet(conversation));
            mViewHolder.last_msg_tv.setCompoundDrawables(getChattingSnipentCompoundDrawables(mContext, conversation), null, null, null);
            //未读提醒设置
            setConversationUnread(mViewHolder, conversation);
            mViewHolder.image_input_text.setVisibility(View.GONE);
            mViewHolder.update_time_tv.setText(getConversationTime(conversation));
            if (conversation.getSessionId().toUpperCase().startsWith("G")) {
                Bitmap bitmap = ContactLogic.getChatroomPhoto(conversation.getSessionId());
                if(bitmap != null) {
                    mViewHolder.user_avatar.setImageBitmap(bitmap);
                    mViewHolder.user_avatar.setPadding(padding, padding, padding, padding);
                    mViewHolder.user_avatar.setBackgroundColor(Color.parseColor("#d5d5d5"));
                } else {
                    mViewHolder.user_avatar.setImageResource(R.drawable.group_head);
                    mViewHolder.user_avatar.setPadding(0, 0, 0, 0);
                    mViewHolder.user_avatar.setBackgroundDrawable(null);
                }
            } else {
                mViewHolder.user_avatar.setBackgroundDrawable(null);
                if(conversation.getMsgType() == 1000) {
                    mViewHolder.user_avatar.setImageResource(R.drawable.ic_launcher);
                } else {
                    ECContacts contact = ContactSqlManager.getContact(conversation.getSessionId());
                    mViewHolder.user_avatar.setImageBitmap(ContactLogic.getPhoto(contact.getRemark()));
                }
            }
            mViewHolder.image_mute.setVisibility(isNotice(conversation)? View.GONE :View.VISIBLE);
        }
        return view;
    }

    /**
     * 设置会话联系人名称文字颜色
     * @param textView
     * @param contactId
     */
    private void handleDisplayNameTextColor(EmojiconTextView textView, String contactId) {
        if (ContactLogic.isCustomService(contactId)) {  //客服号
            textView.setTextColor(colorStateLists[1]);
        } else {    //其他
            textView.setTextColor(colorStateLists[0]);
        }
    }

    /**
     * 设置未读图片显示规则
     * @param mViewHolder
     * @param conversation
     */
    private void setConversationUnread(ViewHolder mViewHolder, Conversation conversation) {
        String msgCount = conversation.getUnreadCount() > 100 ? "..." : String.valueOf(conversation.getUnreadCount());
        mViewHolder.tipcnt_tv.setText(msgCount);
        if (conversation.getUnreadCount() == 0) {   //无未读
            mViewHolder.tipcnt_tv.setVisibility(View.GONE);     //未读数文本
            mViewHolder.prospect_iv.setVisibility(View.GONE);   //新消息小红点
        } else if (conversation.isNotice()) {
            mViewHolder.tipcnt_tv.setVisibility(View.VISIBLE);
            mViewHolder.prospect_iv.setVisibility(View.GONE);
        } else {
            mViewHolder.prospect_iv.setVisibility(View.VISIBLE);
            mViewHolder.tipcnt_tv.setVisibility(View.GONE);
        }
    }

    @Override
    protected void notifyChange() {
        if (mCallBackListener != null) {
            mCallBackListener.OnListAdapterCallBack();
        }
        Cursor cursor = ConversationSqlManager.getConversationCursor();
        setCursor(cursor);
        isAtSession = ECPreferences.getSharedPreferences().getString(ECPreferenceSettings.SETTINGS_AT.getId(), "");
        super.notifyDataSetChanged();
    }

    @Override
    protected void initCursor() {
        notifyChange();
    }

    /**
     * 判断是否是通知
     */
    private boolean isNotice(Conversation conversation) {
        if (conversation.getSessionId().toLowerCase().startsWith("g")) {
            return conversation.isNotice();
        }
        return true;
    }



    private static class ViewHolder{
        ImageView user_avatar;
        TextView tipcnt_tv;
        ImageView prospect_iv;
        EmojiconTextView nickname_tv;
        TextView update_time_tv;
        EmojiconTextView last_msg_tv;
        ImageView image_input_text;
        ImageView image_mute;
    }

}
