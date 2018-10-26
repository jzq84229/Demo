package com.zhang.demo.ytx.ui.chatting.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhang.demo.ytx.R;
import com.zhang.demo.ytx.common.CCPAppManager;
import com.zhang.demo.ytx.common.utils.DensityUtil;
import com.zhang.demo.ytx.common.utils.ECPreferenceSettings;
import com.zhang.demo.ytx.common.utils.ECPreferences;
import com.zhang.demo.ytx.common.utils.FileAccessor;
import com.zhang.demo.ytx.common.utils.LogUtil;
import com.zhang.demo.ytx.common.utils.ResourceHelper;
import com.zhang.demo.ytx.common.utils.ToastUtil;
import com.zhang.demo.ytx.ui.chatting.IMChattingHelper;
import com.zhang.demo.ytx.ui.chatting.base.EmojiconEditText;
import com.zhang.demo.ytx.ui.contact.ECContacts;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 聊天输入框容器
 * Created by Administrator on 2016/7/22.
 */
public class CCPChattingFooter2 extends LinearLayout {
    private static final String LOG_TAG = CCPChattingFooter2.class.getSimpleName();
    private static final int WHAT_ON_DIMISS_DIALOG = 0x1;
    // cancel recording sliding distance field.
    private static final int CANCLE_DANSTANCE = 60;
    /** Chatting mode that input mode */
    public static final int CHATTING_MODE_KEYBORD = 1;      //输入模式键盘输入

    /** Chatting mode that Speech mode, voice record. */
    public static final int CHATTING_MODE_VOICE = 2;        //输入模式语音

    private InputMethodManager mInputMethodManager;

    private View mChattingFooterView;

    private View mVoiceHintRcding;
    private View mVoiceHintTooshort;
    private View mVoiceHintAnimArea;
    private View mVoiceHintLoading;

    /** voice_rcd_hint_cancel_area */
    private View mVoiceRcdHitCancelView;
    private LinearLayout mTextPanel;            //文本输入框容器
    private FrameLayout mChattingBottomPanel;   //输入框插件容器
    private ImageButton mChattingAttach;        //更多按钮
    private ImageButton mChattingModeButton;    //音频输入按钮

    /** The emoji send button in panel. */
    private ImageButton mBiaoqing;              //表情按钮
    private ImageView mVoiceHintAnim;           //录音时音量动画视图
    private ImageView mVoiceHintCancelIcon;     //录音取消的图片
    private TextView mVoiceHintCancelText;      //录音取消文本
    private TextView mVoiceNormalWording;       //录音取消文本
    private ImageButton mVoiceRecord;           //语音录制按钮
    private Button mChattingSend;               //发送按钮
    public EmojiconEditText mEditText;          //文本输入框
    /** Cloud communication panel, display all support ability */
    private AppPanel mAppPanel;                 //插件容器
    /** Panel tha display emoji. */
    private ChatFooterPanel mChatFooterPanel;   //表情框对象
    /** 录制音频时的动画弹出框 */
    private RecordPopupWindow popupWindow;      //音频录制动画弹出框

    /**
     * Interface definition for a callback to be invoked
     * when the {@link ChatFooterPanel} has been click
     */
    private OnChattingFooterLinstener mChattingFooterLinstener;

    private OnChattingPanelClickListener mChattingPanelClickListener;

    /** Interface definition for a callback to be invoked when Input Text */
    private ChattingInputTextWatcher mChatingInputTextWatcher;

    /** Do not enable the enter button to send the message */
    private boolean mDonotEnableEnterkey;

    /** Whether to display the keyboard */
    private boolean mShowKeyBord;

    /** Whether to display emoji panel. */
    private boolean mBiaoqingEnabled;

    /** Whether the voice recording button is touched. */
    private boolean mVoiceButtonTouched;

    /**
     * @see #CHATTING_MODE_KEYBORD
     * @see #CHATTING_MODE_VOICE
     */
    private int mChattingMode;
    private int mChattingFooterTopHeight;
    private int mAppPanleHeight = -1;
    private boolean mHasKeybordHeight;
    private boolean mSetAtSomeone = false;
    private InsertSomeone mInsertSomeone;
    private List<ECContacts> mAtSomeone;

    private static final int ampValue[] = {
            0, 15, 30, 45, 60, 75, 90, 100
    };
    private static final int ampIcon[] = {
            R.drawable.amp1,
            R.drawable.amp2,
            R.drawable.amp3,
            R.drawable.amp4,
            R.drawable.amp5,
            R.drawable.amp6,
            R.drawable.amp7
    };

    private int mTop;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            popupWindow.dismiss();
            mVoiceRecord.setEnabled(true);
            mVoiceRecordBianSheng.setEnabled(true);
        }
    };

    /**
     * 设置输入框回车(Enter)点击的监听事件
     */
    private final TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if ((actionId == EditorInfo.IME_NULL && mDonotEnableEnterkey)
                    || actionId == EditorInfo.IME_ACTION_SEND) {
                mChattingSend.performClick();
                return true;
            }
            return false;
        }
    };


    /**
     * 文本输入框touch监听事件
     */
    private final OnTouchListener mOnTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            hideBottomPanel();
            if (mChattingFooterLinstener != null) {
                mChattingFooterLinstener.OnInEditMode();
            }
            return false;
        }
    };

    public boolean isChangeVoice = false;           //是否变声
    private long currentTimeMillis = 0;             //用于防止重复点击
    public static boolean isRecodering = false;     //是否正在录音
    /** 录音、变声点击监听事件 */
    private final OnTouchListener mOnVoiceRecTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int buId = v.getId();
            if (buId == R.id.voice_record_imgbtn) {
                isChangeVoice = false;
            } else if (buId == R.id.voice_record_imgbtn_biansheng) {
                isChangeVoice = true;
            }
            if (getAvailaleSize() < 10) {
                LogUtil.d(LogUtil.getLogUtilsTag(CCPChattingFooter2.class), "sdcard no memory ");
                ToastUtil.showMessage(R.string.media_no_memory);
                return false;
            }
            long time = System.currentTimeMillis() - currentTimeMillis;
            if (time <= 300) {  //防止重复点击
                LogUtil.d(LogUtil.getLogUtilsTag(CCPChattingFooter2.class), "Invalid click ");
                currentTimeMillis = System.currentTimeMillis();
                return false;
            }
            if (!FileAccessor.isExistExternalStore()) {     //判断存储卡是否可用
                ToastUtil.showMessage(R.string.media_ejected);
                return false;
            }

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isRecodering = true;
                    mVoiceButtonTouched = true;
                    animate(mVoiceRecord, true);
                    onPause();

                    if (mChattingFooterLinstener != null) {
                        mChattingFooterLinstener.OnVoiceRcdInitReuqest();
                    }

                    if (isRecodering) {
                        pageView.setEnabled(false);
                        pageView.setOnTouchListener(null);
                        if (isChangeVoice) {
                            mVoiceRecordBianSheng.getParent().requestDisallowInterceptTouchEvent(true);
                        } else {
                            mVoiceRecord.getParent().requestDisallowInterceptTouchEvent(true);
                        }
                        return true;
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (popupWindow == null) {
                        return false;
                    }
                    if (event.getX() <= 0F || event.getY() <= -CANCLE_DANSTANCE || event.getX() >= mVoiceRecord.getWidth()) {
                        LogUtil.d(LogUtil.getLogUtilsTag(CCPChattingFooter2.class), "show cancel Tips");
                        mVoiceHintCancelText.setText(R.string.chatfooter_cancel_rcd_release);
                        mVoiceRcdHitCancelView.setVisibility(View.VISIBLE);
                        mVoiceHintAnimArea.setVisibility(View.GONE);
                    } else {
                        LogUtil.d(LogUtil.getLogUtilsTag(CCPChattingFooter2.class), "show rcd animation Tips");
                        mVoiceHintCancelText.setText(R.string.chatfooter_cancel_rcd);
                        mVoiceRcdHitCancelView.setVisibility(View.GONE);
                        mVoiceHintAnimArea.setVisibility(View.VISIBLE);
                    }
                    if (isRecodering) {
                        pageView.setEnabled(false);
                        pageView.setOnTouchListener(null);
                        if (isChangeVoice) {
                            mVoiceRecordBianSheng.getParent().requestDisallowInterceptTouchEvent(true);
                        } else {
                            mVoiceRecord.getParent().requestDisallowInterceptTouchEvent(true);
                        }
                        return true;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    animate(mVoiceRecord, false);
                    isRecodering = false;
                    resetVoiceRecordingButton();
                    break;
            }
            return false;
        }
    };

    /**
     * 播放、停止动画
     * @param imgView
     * @param start
     */
    private void animate(ImageView imgView, boolean start) {
        if (start) {
            imgView.setImageResource(R.drawable.ytx_voicebtn_animation_list);
        }
        Drawable drawable = imgView.getDrawable();
        if (!(drawable instanceof AnimationDrawable)) {
            LogUtil.e(LOG_TAG, "animate() !(drawable instanceof AnimationDrawable");
            return;
        }
        AnimationDrawable frameAnimation = (AnimationDrawable) drawable;
        if (frameAnimation.isRunning()) {
            if (!start) {   //停止播放动画
                frameAnimation.stop();
            }
        } else {
            if (start) {    //开始播放动画
                frameAnimation.stop();
                frameAnimation.start();
            }
        }
        if (!start) {
            imgView.setImageResource(R.drawable.voice_push_button);
        }
    }

    /**
     * 变声类型列表取消、发送按钮点击监听事件
     */
    private final OnClickListener mChangeVoiceClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.layout_cancel_changevoice:
                    llBianSheng.setVisibility(View.GONE);
                    pageView.setVisibility(View.VISIBLE);
                    break;
                case R.id.layout_send_changevoice:
                    mTextPanel.setVisibility(View.VISIBLE);
                    setChattingModeImageResource(R.drawable.chatting_setmode_keyboard_btn);
                    llBianSheng.setVisibility(View.GONE);
                    pageView.setVisibility(View.VISIBLE);
                    ll_voice_area.setVisibility(View.GONE);

                    if (bianShengPosition == 0) {
                        mChattingFooterLinstener.sendChangeVoiceMsg(true);
                    } else {
                        mChattingFooterLinstener.sendChangeVoiceMsg(false);
                    }
                    break;
            }
            if (mChattingFooterLinstener != null) {
                mChattingFooterLinstener.stopVoicePlay();
            }
        }
    };

    /**
     * 消息发送监听事件
     */
    private final OnClickListener mChattingSendClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            LogUtil.d(LOG_TAG, "send msg onClick");
            String message = mEditText.getText().toString();
            if (message.trim().length() == 0 && message.length() != 0) {
                LogUtil.d(LOG_TAG, "empty message cant be sent");
                return;
            }
            if (mChattingFooterLinstener != null) {
                mChattingFooterLinstener.OnSendTextMessageRequest(message);
            }

            mEditText.clearComposingText();
            mEditText.setText("");
        }
    };

    /**
     * 键盘监听事件
     */
    private final OnKeyListener mVoiceButtonKeyListener = new OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            switch (event.getAction()) {
                case KeyEvent.ACTION_DOWN:
                    if (keyCode != KeyEvent.KEYCODE_DPAD_CENTER && keyCode != KeyEvent.KEYCODE_ENTER) {
                    }
                    break;
                case KeyEvent.ACTION_UP:
                    if (keyCode != KeyEvent.KEYCODE_DPAD_CENTER && keyCode != KeyEvent.KEYCODE_ENTER) {
                    }
                    break;
            }
            return false;
        }
    };

    /**
     * 聊天输入模式按钮点击监听事件
     */
    private final OnClickListener mChattingModeClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            hideInputMethod();
            hideChatFooterPanel();
            if (mChattingMode != CHATTING_MODE_VOICE) {
                switchChattingMode(CHATTING_MODE_VOICE);
            } else {
                switchChattingMode(CHATTING_MODE_KEYBORD);
            }
        }
    };

    /**
     * 表情显示按钮监听事件
     */
    private final OnClickListener mChattingSmileyClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            displaySmileyPanel();
        }
    };


    /**
     * 更多按钮点击监听事件
     */
    private final OnClickListener mChattingAttachClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isButtomPanelNotVisibility()) {
                hideInputMethod();
            }

        }
    };

    /**
     * 聊天输入框插件点击监听事件
     */
    private final AppPanel.OnAppPanelItemClickListener mAppPanelItemClickListener = new AppPanel.OnAppPanelItemClickListener() {
        @Override
        public void OnTakingPictureClick() {
            if (mChattingPanelClickListener != null) {
                mChattingPanelClickListener.OnTakingPictureRequest();
            }
        }

        @Override
        public void OnSelectImageClick() {
            if (mChattingPanelClickListener != null) {
                mChattingPanelClickListener.OnSelectImageReuqest();
            }
        }

        @Override
        public void OnSelectFileClick() {
            if (mChattingPanelClickListener != null) {
                mChattingPanelClickListener.OnSelectFileRequest();
            }
        }

        @Override
        public void OnSelectVoiceClick() {
            if (mChattingPanelClickListener != null) {
                mChattingPanelClickListener.OnSelectVoiceRequest();
            }
        }

        @Override
        public void OnSelectVideoClick() {
            if (mChattingPanelClickListener != null) {
                mChattingPanelClickListener.OnSelectVideoRequest();
            }
        }

        @Override
        public void OnSelectFireMsgClick() {
            if (mChattingPanelClickListener != null) {
                mChattingPanelClickListener.OnSelectFireMsg();
            }
        }

        @Override
        public void OnSelectFireLocationClick() {
            if (mChattingPanelClickListener != null) {
                mChattingPanelClickListener.OnSelectLocationRequest();
            }
        }
    };

    /**
     * emoji列表点击监听事件
     */
    private final EmojiGrid.OnEmojiItemClickListener mEmojiItemClickListener = new EmojiGrid.OnEmojiItemClickListener() {
        @Override
        public void onEmojiItemClick(int emojiid, String emojiName) {
            //输入emoji表情
            input(mEditText, emojiName);
        }

        @Override
        public void onEmojiDelClick() {
            //发送删除表情事件
            mEditText.getInputConnection().sendKeyEvent(new KeyEvent(MotionEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
            mEditText.getInputConnection().sendKeyEvent(new KeyEvent(MotionEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
        }
    };



    private View ll_voice_area;
    private Chronometer mChronometer;           //录制语音计时器

    private PageView pageView;                  //语音、变声按钮的自定义PageView容器
    private ImageButton mVoiceRecordBianSheng;  //变声录制按钮
    private Chronometer mChronometerBianSheng;  //录制变声计时器
    private LinearLayout llBianSheng;           //变声类型选择列表的容器
    private GridView gvChangeVoice;             //变声类型GridView选择列表

    private int bianShengPosition = 0;
    private int[] voiceArr=new int[]{R.drawable.voicechange_normal,R.drawable.voicechange_luodi,R.drawable.voicechange_dashu,R.drawable.voicechange_jingsong,R.drawable.voicechange_gaoguai,R.drawable.voicechange_kongling};

    private TextView tvCancel;      //变声适配器取消按钮
    private TextView tvSend;        //变声适配器发送按钮

    public CCPChattingFooter2(Context context) {
        this(context, null);
    }

    public CCPChattingFooter2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CCPChattingFooter2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs);

        mInputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        initChatFooter(context);
    }

    /**
     * 变声类型选择监听器
     */
    private final AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (mChattingFooterLinstener != null) {
                bianShengPosition = position;
                mChattingFooterLinstener.onVoiceChangeRequest(position);
            }
        }
    };

    private OnFocusChangeListener onFocusChangeListener = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            Intent intent = new Intent();
            intent.putExtra("hasFoucs", hasFocus);
            intent.setAction(IMChattingHelper.INTENT_ACTION_CHAT_EDITTEXT_FOUCU);
            CCPAppManager.getContext().sendBroadcast(intent);
        }
    };


    private void initChatFooter(Context context) {
        long currentTimeMillis = System.currentTimeMillis();
        mChattingFooterView = inflate(context, R.layout.ccp_chatting_footer2, this);
        mEditText = (EmojiconEditText) findViewById(R.id.chatting_content_et);
        mEditText.setOnFocusChangeListener(onFocusChangeListener);
        mTextPanel = (LinearLayout) findViewById(R.id.text_panel_ll);
        mChattingBottomPanel = (FrameLayout) findViewById(R.id.chatting_bottom_panel);
        mChattingAttach = (ImageButton) findViewById(R.id.chatting_attach_btn);
        mChattingSend = (Button) findViewById(R.id.chatting_send_btn);
        mChattingModeButton = (ImageButton) findViewById(R.id.chatting_mode_btn);

        mInsertSomeone = new InsertSomeone();
        mAtSomeone = new ArrayList<>();

        enableChattingSend(false);      //初始化发送按钮不可点击
        resetEnableEnterKey();          //重设mDonotEnableEnterkey的值

        ll_voice_area = findViewById(R.id.ll_voice_area);
        gvChangeVoice = (GridView) findViewById(R.id.gv_change_voice);
        gvChangeVoice.setAdapter(changeVoiceAdapter);
        gvChangeVoice.setOnItemClickListener(onItemClickListener);
        llBianSheng = (LinearLayout) findViewById(R.id.ll_biansheng_contain);

        pageView = (PageView) findViewById(R.id.chat_voice_panle);
        //语音输入界面
        View normalLaout = View.inflate(getContext(), R.layout.chat_voice_normal, null);
        //变声输入界面
        View bianshengLayout = View.inflate(getContext(), R.layout.chat_voice_biansheng, null);

        pageView.addPage(normalLaout, 0);
        pageView.addPage(bianshengLayout, 1);

        tvCancel = (TextView) findViewById(R.id.layout_cancel_changevoice);
        tvSend = (TextView) findViewById(R.id.layout_send_changevoice);

        tvCancel.setOnClickListener(mChangeVoiceClickListener);
        tvSend.setOnClickListener(mChangeVoiceClickListener);

        mChronometer = ((Chronometer) findViewById(R.id.chronometer));
        mChronometerBianSheng = ((Chronometer) findViewById(R.id.chronometer_biansheng));

        mVoiceRecord = ((ImageButton) findViewById(R.id.voice_record_imgbtn));
        mVoiceRecordBianSheng = ((ImageButton) findViewById(R.id.voice_record_imgbtn_biansheng));
        mChattingModeButton = ((ImageButton) findViewById(R.id.chatting_mode_btn));

        LogUtil.e(LOG_TAG, "send edittext ime option " + mEditText.getImeOptions());
        mEditText.setOnEditorActionListener(mOnEditorActionListener);
        mEditText.setOnTouchListener(mOnTouchListener);
        mChattingSend.setOnClickListener(mChattingSendClickListener);
        mVoiceRecord.setOnTouchListener(mOnVoiceRecTouchListener);
        mVoiceRecord.setOnKeyListener(mVoiceButtonKeyListener);

        mVoiceRecordBianSheng.setOnTouchListener(mOnVoiceRecTouchListener);
        mVoiceRecordBianSheng.setOnKeyListener(mVoiceButtonKeyListener);
        mChattingModeButton.setOnClickListener(mChattingModeClickListener);

        initAppPanel();

        mChattingAttach.setVisibility(View.VISIBLE);
        mChattingAttach.setOnClickListener(mChattingAttachClickListener);

        setBottomPanelHeight(LayoutParams.MATCH_PARENT);

        LogUtil.i(LOG_TAG, "init time:" + (System.currentTimeMillis() - currentTimeMillis));

    }

    /**
     * 变声类型GridView适配器
     */
    private final BaseAdapter changeVoiceAdapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return 6;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = View.inflate(getContext(), R.layout.ec_gridview_changevoice_item, null);
            ImageView iv = (ImageView) v.findViewById(R.id.changevoice_item_iv);
            TextView tv = (TextView) v.findViewById(R.id.changevoice_item_tv);

            iv.setImageResource(voiceArr[position]);
            tv.setText(getContext().getResources().getStringArray(R.array.change_voice_arr)[position]);
            return null;
        }
    };

    /**
     * 初始化聊天输入框插件列表
     */
    private void initAppPanel() {
        mAppPanel = (AppPanel) findViewById(R.id.chatting_app_panel);

        int height = ECPreferences.getSharedPreferences()
                .getInt(ECPreferenceSettings.SETTINGS_KEYBORD_HEIGHT.getId(),
                        ResourceHelper.fromDPToPix(getContext(), 3320));
        mAppPanel.setOnAppPanelItemClickListener(mAppPanelItemClickListener);
        mAppPanel.setPanelHeight(height);
    }

    public final void initSmileyPanel() {
        mBiaoqing = (ImageButton) findViewById(R.id.chatting_smiley_btn);
        mBiaoqing.setVisibility(View.VISIBLE);
        mBiaoqing.setOnClickListener(mChattingSmileyClickListener);
    }

    /**
     * 显示emoji表情列表栏
     */
    public final void displaySmileyPanel() {
        mChattingMode = CHATTING_MODE_KEYBORD;
        mTextPanel.setVisibility(View.VISIBLE);
        if (mChatFooterPanel != null) {
            mChatFooterPanel.reset();
        }

        setMode(CHATTING_MODE_VOICE, 21, true);
    }

    /**
     * 隐藏软键盘
     * Hide keyboard, keyboard does not show
     */
    private void hideInputMethod() {
        hideSoftInputFromWindow(this);
        setKeyBordShow(false);
    }

    public final void switchChattingPanel(String tab) {
        if (TextUtils.isEmpty(tab)) {
            return;
        }
        if (mChatFooterPanel == null) {
            initChattingFooterPanel();
        }
    }

    /**
     * 隐藏软键盘
     * @param view
     */
    public void hideSoftInputFromWindow(View view) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager == null) {
                return;
            }
            inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * init {@link ChatFooterPanel} if not null.
     */
    private void initChattingFooterPanel() {
        if (mChatFooterPanel == null) {
            if (CCPAppManager.getChatFooterPanel(getContext()) == null) {
                mChatFooterPanel = new SmileyPanel(getContext(), null);
            } else {
                mChatFooterPanel = CCPAppManager.getChatFooterPanel(getContext());
            }
        }
        mChatFooterPanel.setOnEmojiItemClickListener(mEmojiItemClickListener);
        if (mChatFooterPanel != null) {
            mChatFooterPanel.setVisibility(View.GONE);
        }

        if (mChattingBottomPanel != null) {
            mChattingBottomPanel.addView(mChatFooterPanel,
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT);
        }
        if (mEditText.getText().length() <= 0) {
            return;
        }
    }

    /**
     * Hide {@link ChatFooterPanel} if not null.
     */
    private void hideChatFooterPanel() {
        mChattingBottomPanel.setVisibility(View.GONE);
        mAppPanel.setVisibility(View.GONE);
        if (mChatFooterPanel != null) {
            mChatFooterPanel.setVisibility(View.GONE);
        }
        setBiaoqingEnabled(false);
    }

    /**
     * 是否显示表情栏
     * Whether display emoji panel
     */
    private void setBiaoqingEnabled(boolean enabled) {
        if (mBiaoqing == null) {
            return;
        }
        if ((mBiaoqingEnabled && enabled) || (!mBiaoqingEnabled && !enabled)) {
            return;
        }
        mBiaoqingEnabled = enabled;
        if (enabled) {
            mBiaoqing.setImageDrawable(getContext().getResources().getDrawable(R.drawable.chatting_biaoqing_operation_enabled));
            return;
        }
        mBiaoqing.setImageDrawable(getContext().getResources().getDrawable(R.drawable.chatting_setmode_biaoqing_btn));
    }

    /**
     * 重新设置录音按钮状态
     */
    public void resetVoiceRecordingButton() {
        mVoiceButtonTouched = false;
        if (mVoiceRcdHitCancelView != null && mVoiceRcdHitCancelView.getVisibility() == View.VISIBLE) {
            //Start to cancel sending events when recording over
            if (mChattingFooterLinstener != null) {
                mChattingFooterLinstener.OnVoiceRcdCancelRequest();
            }
            return;
        }

        if (mChattingFooterLinstener != null && isChangeVoice) {
            mChattingFooterLinstener.OnVoiceRcdStopRequest(true);
        } else {
            mChattingFooterLinstener.OnVoiceRcdStopRequest(false);
        }
    }

    /**
     * 显示变声类型选择栏
     */
    public void showBianShengView() {
        llBianSheng.setVisibility(View.VISIBLE);
        pageView.setVisibility(View.GONE);
    }

    /**
     * 切换聊天输入模式
     * switch chatting mode for Speech mode, input mode
     * @param mode
     */
    private void switchChattingMode(int mode) {
        mChattingMode = mode;
        switch (mode) {
            case CHATTING_MODE_KEYBORD:     //键盘输入
                ll_voice_area.setVisibility(View.GONE);
                mTextPanel.setVisibility(View.VISIBLE);
                setChattingModeImageResource(R.drawable.chatting_setmode_voice_btn);
                break;
            case CHATTING_MODE_VOICE:       //语音输入
                isRecodering = false;
                ll_voice_area.setVisibility(View.VISIBLE);
                ll_voice_area.invalidate();
                mTextPanel.setVisibility(View.INVISIBLE);
                setChattingModeImageResource(R.drawable.chatting_setmode_keyboard_btn);
                bianShengPosition = 0;
                break;
        }
    }

    /**
     * 判断发送按钮是否可点击
     * If it is possible to enable the send button
     * @param canSend
     */
    private void enableChattingSend(boolean canSend) {
        if (mChattingAttach == null || mChattingSend == null) {
            return;
        }
        // If the current attachment button visible, and the Enter key to send the message model
        if (mDonotEnableEnterkey && mChattingAttach.getVisibility() == View.VISIBLE
                || mDonotEnableEnterkey && mChattingSend.getVisibility() == View.VISIBLE) {
            return;
        }

        if (mDonotEnableEnterkey) {
            mChattingSend.setVisibility(View.VISIBLE);
            mChattingAttach.setVisibility(View.GONE);
        } else {
            mChattingSend.setVisibility(View.GONE);
            mChattingAttach.setVisibility(View.VISIBLE);
        }
        LogUtil.d(LogUtil.getLogUtilsTag(getClass()), "mDonotEnableEnterkey " + mDonotEnableEnterkey);
        mChattingSend.getParent().requestLayout();
    }

    /**
     * 设置文本输入框是否获取焦点
     * @param focus     是否获取焦点
     */
    private void requestFocusEditText(boolean focus) {
        if (focus) {
            mEditText.requestFocus();
            mTextPanel.setEnabled(true);
            return;
        }
        mEditText.clearFocus();
        mTextPanel.setEnabled(false);
    }

    /**
     * 重设mDonotEnableEnterkey
     */
    public final void resetEnableEnterKey() {
        mDonotEnableEnterkey = ECPreferences.getSharedPreferences()
                .getBoolean(ECPreferenceSettings.SETTINGS_ENABLE_ENTER_KEY.getId(),
                        (Boolean) ECPreferenceSettings.SETTINGS_ENABLE_ENTER_KEY.getDefaultValue());
    }

    /**
     * Clear input box
     */
    public final void clearEditText() {
        if (mEditText == null) {
            return;
        }
        mEditText.setText("");
    }

    /**
     * 获得最后的文本
     * @return
     */
    public final String getLastText() {
        if (mEditText == null) {
            return "";
        }
        return mEditText.getText().toString();
    }

    private void setKeyBordShow(boolean showKeybord) {
        if (mShowKeyBord = showKeybord) {
            return;
        }
        mShowKeyBord = showKeybord;
    }

    /**
     * 改变聊天输入模式
     * change chatting mode for Speech mode, input mode
     * @param resId
     */
    private void setChattingModeImageResource(int resId) {
        if (mChattingModeButton == null) {
            return;
        }
        if (resId == R.drawable.chatting_setmode_voice_btn) {
            mChattingModeButton.setContentDescription(getContext().getString(R.string.chat_footer_switch_mode_btn));
        } else {
            mChattingModeButton.setContentDescription(getContext().getString(R.string.chat_footer_switch_mode_keybord_btn));
        }
        mChattingModeButton.setImageResource(resId);
        mChattingModeButton.setPadding(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.ChattingFootPaddingBottom));
    }





    public boolean isButtomPanelNotVisibility() {
        return mChattingBottomPanel.getVisibility() != View.VISIBLE;
    }

    public boolean isSetAtSomeoneing() {
        return mSetAtSomeone;
    }

    /**
     * 设置AppPanel默认高度
     * set the {@link AppPanel} default height
     * @param height
     */
    private void setBottomPanelHeight(int height) {
        int widthPixels = 0;
        if (height <= 0) {
            int[] displayScreenMetrics = getDisplayScreenMetrics();
            if (displayScreenMetrics[0] >= displayScreenMetrics[1]) {
                height = ResourceHelper.fromDPToPix(getContext(), 230);
            } else {
                height = ECPreferences.getSharedPreferences()
                        .getInt(ECPreferenceSettings.SETTINGS_KEYBORD_HEIGHT.getId(),
                                ResourceHelper.fromDPToPix(getContext(), 230));
            }
            widthPixels = displayScreenMetrics[0];
        }

        if (height > 0 && mChattingBottomPanel != null) {
            ViewGroup.LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, height);
            if (mChattingBottomPanel.getLayoutParams() != null) {
                layoutParams = mChattingBottomPanel.getLayoutParams();
            }
            layoutParams.height = height;
        }
        mAppPanel.setPanelHeight(height);
    }

    /**
     * Access to mobile phone screen resolution and the width and height
     * @return
     */
    private int[] getDisplayScreenMetrics() {
        int[] metrics = new int[2];
        if (getContext() instanceof Activity) {
            DisplayMetrics displayMetrices = new DisplayMetrics();
            ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrices);
            metrics[0] = displayMetrices.widthPixels;
            metrics[1] = displayMetrices.heightPixels;
            return metrics;
        }
        Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        metrics[0] = display.getWidth();
        metrics[1] = display.getHeight();

        return metrics;
    }

    /**
     * 显示录音、变声录制动画框
     * @param offset
     */
    public final void showVoiceRecordWindow(int offset) {
        int yLocation = 0;
        int maxHeightDensity = ResourceHelper.fromDPToPix(getContext(), 180);
        int density = DensityUtil.getMetricsDensity(getContext(), 50F);
        if (offset + density < maxHeightDensity) {
            yLocation = -1;
        } else {
            yLocation = density + (offset - maxHeightDensity) / 2;
        }

        if (popupWindow == null) {
            popupWindow = new RecordPopupWindow(
                    View.inflate(getContext(), R.layout.voice_rcd_hint_window2, null),
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            mVoiceHintAnim = (ImageView) popupWindow.getContentView().findViewById(R.id.voice_rcd_hint_anim);
            mVoiceHintAnimArea = popupWindow.getContentView().findViewById(R.id.voice_rcd_hint_anim_area);
            mVoiceRcdHitCancelView = popupWindow.getContentView().findViewById(R.id.voice_rcd_hint_cancel_area);
            mVoiceHintCancelText = (TextView) popupWindow.getContentView().findViewById(R.id.voice_rcd_hint_cancel_text);
            mVoiceHintCancelIcon = (ImageView) popupWindow.getContentView().findViewById(R.id.voice_rcd_hint_cancel_icon);
            mVoiceHintLoading = popupWindow.getContentView().findViewById(R.id.voice_rcd_hint_loading);
            mVoiceHintRcding = popupWindow.getContentView().findViewById(R.id.voice_rcd_hint_rcding);
            mVoiceHintTooshort = popupWindow.getContentView().findViewById(R.id.voice_rcd_hint_tooshort);
            mVoiceNormalWording = (TextView) popupWindow.getContentView().findViewById(R.id.voice_rcd_normal_wording);
        }

        if (yLocation != -1) {
            mVoiceHintTooshort.setVisibility(View.GONE);
            mVoiceHintRcding.setVisibility(View.GONE);
            mVoiceHintLoading.setVisibility(View.VISIBLE);
            popupWindow.showAtLocation(this, Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, yLocation);
        }
    }

    public void displayAmplitude(double amplitude) {
        for (int i = 0; i < ampIcon.length; i++) {
            if (amplitude < ampValue[i] || amplitude >= ampValue[i + 1]) {
                continue;
            }
            mVoiceHintAnim.setBackgroundDrawable(ResourceHelper.getDrawableById(getContext(), ampIcon[i]));
            if (amplitude == -1 && popupWindow != null) {
                popupWindow.dismiss();
                mVoiceHintLoading.setVisibility(View.VISIBLE);
                mVoiceHintRcding.setVisibility(View.GONE);
                mVoiceHintTooshort.setVisibility(View.GONE);
            }
            return;
        }
    }

    /**
     * 显示音频录制动画框
     */
    public void showVoiceRecording() {
        if (mChattingFooterLinstener != null) {
            mChattingFooterLinstener.OnVoiceRcdStartRequest();
        }
        mVoiceHintLoading.setVisibility(View.GONE);
        mVoiceHintRcding.setVisibility(View.VISIBLE);
    }

    /**
     * 影藏音频录制动画弹出框
     */
    public final void dismissPopupWindow() {
        if (popupWindow != null) {
            popupWindow.dismiss();
            mVoiceHintRcding.setVisibility(View.VISIBLE);
            mVoiceHintLoading.setVisibility(View.GONE);
            mVoiceHintTooshort.setVisibility(View.GONE);
            mVoiceRcdHitCancelView.setVisibility(View.GONE);
            mVoiceHintAnimArea.setVisibility(View.VISIBLE);
        }
        mVoiceButtonTouched = false;
    }

    public synchronized void tooShortPopupWindow() {
        mVoiceRecord.setEnabled(false);
        mVoiceRecordBianSheng.setEnabled(false);
        if (popupWindow != null) {
            mVoiceHintTooshort.setVisibility(View.VISIBLE);
            if (mChronometer != null) {
//              mChronometer.stop();
//              mChronometerBianSheng.stop();
            }
            mVoiceHintRcding.setVisibility(View.GONE);
            mVoiceHintLoading.setVisibility(View.GONE);
        }

        if (mHandler != null) {
            mHandler.sendEmptyMessageDelayed(WHAT_ON_DIMISS_DIALOG, 500L);
        }

        if (llBianSheng.VISIBLE == View.VISIBLE) {
            llBianSheng.setVisibility(View.GONE);
            pageView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Register a drag event listener callback object for {@link CCPEditText}. The parameter is
     * an implementation of {@link android.view.View.OnDragListener}. To send a drag event to a
     * View, the system calls the
     * {@link android.view.View.OnDragListener#onDrag(android.view.View, android.view.DragEvent)} method.
     * @param l An implementation of {@link android.view.View.OnDragListener}.
     */
    public final void setOnEditTextDragListener(OnDragListener l) {
        mEditText.setOnDragListener(l);
    }

    public final void setOnChattingFooterLinstener(OnChattingFooterLinstener l) {
        mChattingFooterLinstener = l;
    }

    public final void setOnChattingPanelClickListener(OnChattingPanelClickListener l) {
        mChattingPanelClickListener = l;
    }

    public final void addTextChangedListener(TextWatcher textWatcher) {
        mChatingInputTextWatcher = new ChattingInputTextWatcher(textWatcher);
        mEditText.addTextChangedListener(mChatingInputTextWatcher);
    }

    public final void setEditTextNull() {
        mEditText.setText(null);
    }

    public final void setEditText(CharSequence text) {
        mEditText.setText(text);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    /**
     * 获取手机SDCard可用空间大小
     * @return
     */
    public long getAvailaleSize() {
        File path = Environment.getExternalStorageDirectory();  //取得sdcard文件路径
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return (availableBlocks * blockSize) / 1024 / 1024;     //MIB单位
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public final void hideBottomPanel() {
        setMode(CHATTING_MODE_VOICE, 20, false);
    }

    /** 设置输入模式 */
    public void setMode(int mode) {
        setMode(mode, true);
    }

    /**
     * 设置输入模式
     * @param mode      输入模式
     * @param focus     是否获得焦点
     */
    public final void setMode(int mode, boolean focus) {
        switchChattingMode(mode);
        switch (mode) {
            case CHATTING_MODE_KEYBORD:
                requestFocusEditText(true);
                hideChatFooterPanel();
                if (focus) {
                    if (mEditText.length() > 0) {
                        enableChattingSend(true);
                        return;
                    }
                }
                enableChattingSend(false);
                break;
            case CHATTING_MODE_VOICE:
                enableChattingSend(false);
                setMode(0, -1, false);
                break;
            default:
                setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * 设置输入模式
     * @param mode          输入模式
     * @param messageMode
     * @param focus         是否获得焦点
     */
    private void setMode(int mode, int messageMode, boolean focus) {
        if (focus) {
            switch (mode) {
                case CHATTING_MODE_KEYBORD:
                    requestFocusEditText(true);
                    mInputMethodManager.showSoftInput(this.mEditText, 0);
                    break;
                case CHATTING_MODE_VOICE:
                    if (messageMode == 22) {
                        if (mAppPanel == null) {
                            initAppPanel();
                        }
                        mAppPanel.initFlipperRotateMe();

                        if (mChatFooterPanel != null) {
                            mChatFooterPanel.setVisibility(View.GONE);
                        }
                        mAppPanel.setVisibility(View.VISIBLE);
                        requestFocusEditText(false);
                        if (mChattingMode == CHATTING_MODE_VOICE) {
                            switchChattingMode(CHATTING_MODE_KEYBORD);
                        }
                    } else if (messageMode == 21) {
                        if (mAppPanel != null) {
                            mAppPanel.setVisibility(View.GONE);
                        }
                        if (mChatFooterPanel == null) {
                            initChattingFooterPanel();
                        }
                        mChatFooterPanel.onResume();
                        if (mChatFooterPanel != null) {
                            mChatFooterPanel.setVisibility(View.VISIBLE);
                        }
                        setBiaoqingEnabled(true);
                        requestFocusEditText(true);

                        hideInputMethod();
                        this.mChattingBottomPanel.setVisibility(View.VISIBLE);
                    }
                    break;
                default:
                    if (focus && messageMode != 21 && mBiaoqing != null) {
                        setBiaoqingEnabled(false);
                    }
                    if (!focus && mode == 0) {
                        setBiaoqingEnabled(false);
                    }
                    mChattingBottomPanel.setVisibility(View.VISIBLE);
                    mAppPanel.setVisibility(View.VISIBLE);
                    break;
            }
        } else {
            if (messageMode == 20) {
                hideChatFooterPanel();
            }
            if (messageMode != 21 && mBiaoqing != null) {
                setBiaoqingEnabled(false);
            }
        }
    }

    public final void refreshAppPanel() {
        mAppPanel.refreshAppPanel();
    }

    public final void onPause() {
        if (mChatFooterPanel != null) {
            mChatFooterPanel.onPause();
        }
        mChattingFooterLinstener.onPause();
    }

    public void onDestory() {
        mAppPanel = null;
        if (mChatFooterPanel != null) { //释放mChatFooterPanel资源
            mChatFooterPanel.onDestory();
            mChatFooterPanel = null;
        }
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        if (mEditText != null) {    //释放mEditText资源
            mEditText.miInputConnection = null;
            mEditText.setOnEditorActionListener(null);
            mEditText.setOnTouchListener(null);
            mEditText.removeTextChangedListener(null);
            mEditText.clearComposingText();
            mEditText = null;
        }
        mChattingSend.setOnClickListener(null);
        mVoiceRecord.setOnTouchListener(null);
        mVoiceRecord.setOnKeyListener(null);
        mVoiceRecordBianSheng.setOnTouchListener(null);
        mVoiceRecordBianSheng.setOnKeyListener(null);
//        mVoiceRecord.removeTextChangedListener(null);
        mChattingModeButton.setOnClickListener(null);
        //initAppPanel();
        mChattingAttach.setOnClickListener(null);
        mVoiceRecord = null;
        mVoiceRecordBianSheng = null;
        mChattingModeButton = null;
        mChattingAttach = null;
        popupWindow = null;
        mChattingFooterLinstener = null;
        mChattingPanelClickListener = null;
        mChatingInputTextWatcher = null;
    }

    public void setLastText(String text) {
        setLastText(text, -1, true);
    }

    public void setLastText(String text, int selection, boolean clear) {
        if (clear && (text == null || text.length() == 0 || mEditText == null)) {
            mEditText.setText("");
            return;
        }
        mSetAtSomeone = true;
        EmojiconEditText editText = mEditText;
        editText.setText(text);
        mSetAtSomeone = false;
        if (selection < 0 || selection > mEditText.getText().length()) {
            this.mEditText.setSelection(this.mEditText.getText().length());
            return;
        }
        this.mEditText.setSelection(selection);
    }

    public int getSelectionStart() {
        return this.mEditText.getSelectionStart();
    }

    public char getCharAtCursor() {
        int i = getSelectionStart();
        if (i <= 0) {
            return 'x';
        }
        return getLastText().charAt(i - 1);
    }

    public String getAtSomebody(String somebody) {
        return mInsertSomeone.someBody;
    }

    public void setAtSomebody(String somebody) {
        mInsertSomeone.someBody = somebody;
    }

    public void putSomebody(ECContacts contacts) {
        mAtSomeone.add(contacts);
    }

    public void delSomeBody(String text) {
        ECContacts remove = null;
        for (ECContacts c : mAtSomeone) {
            if (c != null && text.equals(c.getNickname())) {
                remove = c;
                break;
            }
        }
        if (remove != null) {
            mAtSomeone.remove(remove);
        }
    }

    public String[] getAtSomeBody() {
        if (mAtSomeone == null || mAtSomeone.isEmpty()) {
            return null;
        }
        String[] at = new String[mAtSomeone.size()];
        for (int i = 0; i < mAtSomeone.size(); i++) {
            at[i] = mAtSomeone.get(i).getContactid();
        }
        return at;
    }

    public void clearSomeBody() {
        if (mAtSomeone == null || mAtSomeone.isEmpty()) {
            return;
        }
        mAtSomeone.clear();
    }

    public void setLastContent(String lastContent) {
        mInsertSomeone.lastContent = lastContent;
    }

    public String getLastContent() {
        return mInsertSomeone.lastContent;
    }

    public int getInsertPos() {
        return mInsertSomeone.insetPosition;
    }

    public void setInsertPos(int position) {
        mInsertSomeone.insetPosition = position;
    }

    public int getMode() {
        return 0;
    }

    public boolean isVoiceRecordCancel() {
        return false;
    }


    /**
     * 输入emoji表情
     * @param editText      输入框
     * @param emojiName     emoji名称
     */
    public static void input(EditText editText, String emojiName) {
        if (editText == null || emojiName == null) {
            return;
        }

        int start = editText.getSelectionStart();
        int end = editText.getSelectionEnd();
        if (start < 0) {
            editText.append(emojiName);
        } else {
            editText.getText().replace(Math.min(start, end), Math.max(start, end), emojiName, 0, emojiName.length());
        }
    }

    public class InsertSomeone {
        String someBody;
        String lastContent;
        int insetPosition = 0;
    }

    /**
     * Interface definition for a callback to be invoked when the {@link ChatFooterPanel} has been click
     * such as .emoji click , voice rcd onTouch
     */
    public interface OnChattingFooterLinstener {
        void OnVoiceRcdInitReuqest();
        void sendChangeVoiceMsg(boolean isSendYuanSheng);
        void OnVoiceRcdStartRequest();
        void stopVoicePlay();
        /**
         * Called when the voce record button nomal and cancel send voice.
         */
        void OnVoiceRcdCancelRequest();
        /**
         * Called when the voce record button nomal and send voice.
         */
        void OnVoiceRcdStopRequest(boolean isSend);
        void onVoiceChangeRequest(int position);
        void OnSendTextMessageRequest(CharSequence text);
        void OnUpdateTextOutBoxRequest(CharSequence text);
        void OnSendCustomEmojiRequest(int emojiid, String emojiName);
        void OnEmojiDelRequest();
        void OnInEditMode();
        void onPause();
        void onResume();
        void release();
    }

    /**
     * Interface definition for a callback to be invoked when the {@link ChatFooterPanel} has been click
     * such as .emoji click , voice rcd onTouch
     */
    public interface OnChattingPanelClickListener {
        void OnTakingPictureRequest();
        void OnSelectImageReuqest();
        void OnSelectFileRequest();
        void OnSelectVoiceRequest();
        void OnSelectVideoRequest();
        void OnSelectFireMsg();
        void OnSelectLocationRequest();
    }

    private class ChattingInputTextWatcher implements TextWatcher {
        private TextWatcher mTextWatcher;

        public ChattingInputTextWatcher(TextWatcher mTextWatcher) {
            this.mTextWatcher = mTextWatcher;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            mTextWatcher.beforeTextChanged(s, start, count, after);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mTextWatcher.onTextChanged(s, start, before, count);
        }

        @Override
        public void afterTextChanged(Editable s) {
            mTextWatcher.afterTextChanged(s);
            if (s.length() > 0 && s.toString().trim().length() > 0) {
                mDonotEnableEnterkey = true;
                enableChattingSend(true);
            } else {
                mDonotEnableEnterkey = false;
                enableChattingSend(false);
            }
        }
    }


}
