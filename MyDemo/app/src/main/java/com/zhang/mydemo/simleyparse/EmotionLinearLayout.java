package com.zhang.mydemo.simleyparse;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zhang.mydemo.R;
import com.zhang.mydemo.util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

/** 
 * @Description: 表情框
 */
public class EmotionLinearLayout extends LinearLayout implements OnItemClickListener, View.OnClickListener {
	private LayoutInflater inflater;
	
	/** 表情页的监听事件 */
	private OnCorpusSelectedListener mListener;

	/** 显示表情页的viewpager */
	private ViewPager vp_emotion;

	/** 表情页界面集合 */
	private ArrayList<View> emotionViewList;

	/** 游标显示布局 */
	private LinearLayout point_layout;

	/** 游标点集合 */
	private ArrayList<ImageView> pointViews;

	/** 表情集合 */
	private List<List<ChatEmoji>> emojiPageList;

	/** 表情区域 */
	private View emotion_layout;

	/** 输入框 */
	private EditText et_content;

	/** 表情数据填充器 */
	private List<EmotionAdapter> emotionAdapters;

	/** 当前表情页 */
	private int current = 0;

	//表情栏高度，默认值自适应
	private int emotionLayoutHeight = ScreenUtil.screenHeightPixels(getContext()) / 2;

	public EmotionLinearLayout(Context context) {
		super(context);
		init();
	}

	public EmotionLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public EmotionLinearLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	public void setOnCorpusSelectedListener(OnCorpusSelectedListener listener) {
		mListener = listener;
	}
	
	/**
	 * 表情选择监听
	 */
	public interface OnCorpusSelectedListener {

		void onCorpusSelected(ChatEmoji emoji);

		void onCorpusDeleted();
	}
	
	/**
	 * 表情框按钮监听事件
	 */
	public interface OnEmotionLayoutListener{
		void sendMessage(String str);
		
		void smileyBtnClick(boolean showEmotion);
		
		void emotionOutsideClick();
	}
	
	private OnEmotionLayoutListener emotionListener;
	
	public void setOnEmotionLayoutListener(OnEmotionLayoutListener emotionListener){
		this.emotionListener = emotionListener;
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
	}
	
	private void init(){
		inflater = LayoutInflater.from(getContext());
		inflater.inflate(R.layout.comment_emotion_layout, this);
		
		emojiPageList = EmotionUtils.getInstance(getContext().getApplicationContext()).getSmileyPageList();
		initView();
		initViewPager();
		initPoint();
		initData();
	}
	
	/**
	 * 初始化控件
	 */
	private void initView() {
		/* 表情按钮 */
		ImageButton btn_smiley = (ImageButton) findViewById(R.id.btn_smiley);
		/* 发送按钮 */
		Button btn_send = (Button) findViewById(R.id.btn_send);
		et_content = (EditText) findViewById(R.id.et_content);
		btn_smiley.setImageResource(R.drawable.emo_1);
		btn_smiley.setOnClickListener(this);
		btn_send.setOnClickListener(this);
		et_content.setOnClickListener(this);

		emotion_layout = findViewById(R.id.ll_emotion_layout);
		vp_emotion = (ViewPager) findViewById(R.id.vp_emotion);
		point_layout = (LinearLayout) findViewById(R.id.ll_point_layout);

		View emotionLayout = findViewById(R.id.emotion_layout);
		View outsideView = findViewById(R.id.v_outside);
		outsideView.setOnClickListener(this);
		emotionLayout.setOnClickListener(this);
	}

	/**
	 * 初始化显示表情的viewpager
	 */
	private void initViewPager() {
		emotionViewList = new ArrayList<>();
		// 中间添加表情页
		emotionAdapters = new ArrayList<>();
		for (int i = 0; i < emojiPageList.size(); i++) {
			View view = inflater.inflate(R.layout.comment_edit_emotion_gridview, this, false);
			GridView gv_emotion = (GridView) view.findViewById(R.id.gv_emotion);
			EmotionAdapter adapter = new EmotionAdapter(getContext(), emojiPageList.get(i));
			gv_emotion.setAdapter(adapter);
			emotionAdapters.add(adapter);
			gv_emotion.setOnItemClickListener(this);
			
			emotionViewList.add(view);
		}
	}

	/**
	 * 初始化游标
	 */
	private void initPoint() {
		pointViews = new ArrayList<ImageView>();
		ImageView imageView;
		for (int i = 0; i < emotionViewList.size(); i++) {
			imageView = new ImageView(getContext());
			imageView.setBackgroundResource(R.mipmap.emotion_page_point_0);
			LayoutParams layoutParams = new LayoutParams(
					new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT));
			layoutParams.leftMargin = 10;
			layoutParams.rightMargin = 10;
			layoutParams.width = 16;
			layoutParams.height = 16;
			point_layout.addView(imageView, layoutParams);

			if (i == 0) {
				imageView.setBackgroundResource(R.mipmap.emotion_page_point_1);
			}
			pointViews.add(imageView);
		}
	}

	/**
	 * 填充数据
	 */
	private void initData(){
		vp_emotion.setAdapter(new EmotionViewPageAdapter(emotionViewList));
		vp_emotion.setCurrentItem(0);
		current = 0;
        vp_emotion.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                current = position;
                // 描绘分页点
                draw_Point(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
	}

	/**
	 * 绘制游标背景
	 */
	public void draw_Point(int index) {
		for (int i = 0; i < pointViews.size(); i++) {
			if (index == i) {
				pointViews.get(i).setBackgroundResource(R.mipmap.emotion_page_point_1);
			} else {
				pointViews.get(i).setBackgroundResource(R.mipmap.emotion_page_point_0);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.emotion_layout:
			//表情框点击事件，消耗点击事件，防止表情框底部控件获取点击事件
			break;
		case R.id.btn_smiley:
		case R.id.et_content:
			// 隐藏表情选择框
			if (emotionListener != null) {
				emotionListener.smileyBtnClick(false);
			}
			break;
		case R.id.btn_send:
			if (emotionListener != null) {
				emotionListener.sendMessage(et_content.getText().toString());
				clearContent();
			}
			break;
		case R.id.v_outside:
			if (emotionListener != null) {
				emotionListener.emotionOutsideClick();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		ChatEmoji emoji = emotionAdapters.get(current).getItem(position);
		if (emoji.getId() == R.drawable.smiley_del_ico) {
			int selection = et_content.getSelectionStart();
			String text = et_content.getText().toString();
			if (selection > 0) {
				String text2 = text.substring(selection - 1);
				if ("]".equals(text2)) {
					int start = text.lastIndexOf("[");
					int end = selection;
					et_content.getText().delete(start, end);
					return;
				}
				et_content.getText().delete(selection - 1, selection);
			}
		}
		if (!TextUtils.isEmpty(emoji.getCharacter())) {
			if (mListener != null){
				mListener.onCorpusSelected(emoji);
			}
            SpannableString spannableString = EmotionUtils.getInstance(getContext().getApplicationContext()).addFace(emoji.getId(), emoji.getCharacter());
			et_content.append(spannableString);
		}
	}
	
	/**
	 * 隐藏表情选择框
	 */
	public void hideEmotionView() {
		this.setVisibility(View.GONE);
		clearContent();
	}
	
	/**
	 * 显示表情框
	 */
	public void showEmotionView(){
		emotion_layout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, emotionLayoutHeight));
		emotion_layout.setVisibility(View.VISIBLE);
		this.setVisibility(View.VISIBLE);
		et_content.requestFocus();
	}
	
	/**清空文本编辑框*/
	private void clearContent(){
		et_content.getText().clear();
	}
	
	/**
	 * 设置内容框提示文字
	 * @param hintStr
	 */
	public void setContentHint(String hintStr){
		et_content.setHint(hintStr);
	}
	
	/**
	 * 设置表情框高度
	 * @param height
	 */
	public void setEmotionLayoutHeight(int height){
		if (height > 100) {
			this.emotionLayoutHeight = height;
		}
	}
	
	public Editable getContent(){
		return et_content.getText();
	}
	
	public void setContent(Editable et){
		et_content.setText(et);
		et_content.setSelection(et.length());
	}

}
