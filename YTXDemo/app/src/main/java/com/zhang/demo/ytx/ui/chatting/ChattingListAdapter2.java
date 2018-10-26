package com.zhang.demo.ytx.ui.chatting;

import android.content.Context;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuntongxun.ecsdk.ECMessage;
import com.zhang.demo.ytx.R;
import com.zhang.demo.ytx.common.utils.LogUtil;
import com.zhang.demo.ytx.storage.ConversationSqlManager;
import com.zhang.demo.ytx.storage.IMessageSqlManager;
import com.zhang.demo.ytx.ui.CCPListAdapter;
import com.zhang.demo.ytx.ui.chatting.holder.BaseHolder;
import com.zhang.demo.ytx.ui.chatting.model.BaseChattingRow;
import com.zhang.demo.ytx.ui.chatting.model.ChattingRowType;
import com.zhang.demo.ytx.ui.chatting.model.IChattingRow;
import com.zhang.demo.ytx.ui.chatting.model.ImageRxRow;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/8/1.
 */
public class ChattingListAdapter2 extends CCPListAdapter<ECMessage> {

    protected View.OnClickListener mOnClickListener;
    /**当前语音播放的Item*/
    public int mVoicePosition = -1;
    /**需要显示时间的Item position*/
    private ArrayList<String> mShowTimePosition;
    /**初始化所有类型的聊天Item 集合*/
    private HashMap<Integer, IChattingRow> mRowItems;
    /**时间显示控件的垂直Padding*/
    private int mVerticalPadding;
    /**时间显示控件的横向Padding*/
    private int mHorizontalPadding;
    /**消息联系人名称显示颜色*/
    private ColorStateList[] mChatNameColor;
    private String mUsername;
    private long mThread = -1;
    private int mMsgCount = 18;
    private int mTotalCount = mMsgCount;


    /**
     * 构造方法
     *
     * @param ctx
     * @param message
     */
    public ChattingListAdapter2(Context ctx, ECMessage message, String user) {
        this(ctx, message, user, -1);
    }

    public ChattingListAdapter2(Context ctx, ECMessage ecMessage, String user, long thread) {
        super(ctx, ecMessage);
        mUsername = user;
        mThread = thread;
        mRowItems = new HashMap<Integer, IChattingRow>();
        mShowTimePosition = new ArrayList<>();
        initRowItems();

        //初始化聊天消息点击事件回调
        mOnClickListener = new ChattingListClickListener((ChattingActivity) mContext, null);
        mVerticalPadding = mContext.getResources().getDimensionPixelSize(R.dimen.SmallestPadding);
        mHorizontalPadding = mContext.getResources().getDimensionPixelSize(R.dimen.LittlePadding);
        mChatNameColor = new ColorStateList[]{
                mContext.getResources().getColorStateList(R.color.white),
                mContext.getResources().getColorStateList(R.color.chatroom_user_displayname_color)};
    }

    public long setUsername(String username) {
        this.mUsername = username;
        mThread = ConversationSqlManager.querySessionIdForBySessionId(mUsername); //通过sessionId找会话Id
        return mThread;
    }

    public long getThread() {
        return mThread;
    }

    public int increaseCount() {
        if (isLimitCount()) {
            return 0;
        }
        mMsgCount += 18;
        if (mMsgCount <= mTotalCount) {
            return 18;
        }
        return mTotalCount % 18;
    }

    public boolean isLimitCount() {
        return mTotalCount < mMsgCount;
    }

    @Override
    protected void notifyChange() {
        this.mTotalCount = IMessageSqlManager.queryIMCountForSession(mThread);
        //初始化一个空的数据列表
        setCursor(IMessageSqlManager.queryIMessageCursor(mThread, mMsgCount));
        super.notifyDataSetChanged();
    }

    @Override
    protected void initCursor() {
        //初始化一个空的数据列表
        if (mThread > 0) {
            notifyChange();
            return;
        }
        setCursor(IMessageSqlManager.getNullCursor());
    }

    @Override
    protected ECMessage getItem(ECMessage message, Cursor cursor) {
        return IMessageSqlManager.packageMessage(cursor);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ECMessage item = getItem(position);
        if (item == null) {
            return null;
        }
        boolean showTime = false;
        if (position == 0) {
            showTime = true;
        }
        if (position != 0) {    //判断是否显示日期
            ECMessage previousItem = getItem(position - 1);
            if (mShowTimePosition.contains(item.getMsgId())
                    || (item.getMsgTime() - previousItem.getMsgTime() >= 180000L)) {
                showTime = true;
            }
        }

        int messageType = ChattingsRowUtils.getChattingMessageType(item.getType());
        BaseChattingRow chattingRow = getBaseChattingRow(messageType, item.getDirection() == ECMessage.Direction.SEND);
        View chatView = chattingRow.buildChatView(LayoutInflater.from(mContext), convertView);
        BaseHolder baseHolder = (BaseHolder) chatView.getTag();

        if (showTime) {
            baseHolder.getChattingTime().setVisibility(View.VISIBLE);
        }
        return null;
    }

    /**
     * 初始化不同的聊天Item View
     */
    private void initRowItems() {
        mRowItems.put(Integer.valueOf(1), new ImageRxRow(1));
        mRowItems.put(Integer.valueOf(2), new ImageTxRow(2));
        mRowItems.put(Integer.valueOf(3), new FileRxRow(3));
        mRowItems.put(Integer.valueOf(4), new FileTxRow(4));
        mRowItems.put(Integer.valueOf(5), new VoiceRxRow(5));
        mRowItems.put(Integer.valueOf(6), new VoiceTxRow(6));
        mRowItems.put(Integer.valueOf(7), new DescriptionRxRow(7));
        mRowItems.put(Integer.valueOf(8), new DescriptionTxRow(8));
        mRowItems.put(Integer.valueOf(10), new LocationRxRow(10));
        mRowItems.put(Integer.valueOf(11), new LocationTxRow(11));
        mRowItems.put(Integer.valueOf(12), new CallRxRow(12));
        mRowItems.put(Integer.valueOf(13), new CallTxRow(13));
        mRowItems.put(Integer.valueOf(14), new RichTextTxRow(14));
        mRowItems.put(Integer.valueOf(15), new RichTextRxRow(15));
    }

    /**
     * 根据消息类型返回相对应的消息Item
     * @param rowType
     * @param isSend
     * @return
     */
    public BaseChattingRow getBaseChattingRow(int rowType, boolean isSend) {
        StringBuilder builder = new StringBuilder("C").append(rowType);
        if (isSend) {
            builder.append("T");
        } else {
            builder.append("R");
        }

        LogUtil.d("ChattingListAdapter", "builder.toString() = " + builder.toString());
        ChattingRowType fromValue = ChattingRowType.fromValue(builder.toString());
        LogUtil.d("ChattingListAdapter", "fromValue = " + fromValue);
        IChattingRow iChattingRow = mRowItems.get(fromValue.getId().intValue());
        return (BaseChattingRow) iChattingRow;
    }
}
