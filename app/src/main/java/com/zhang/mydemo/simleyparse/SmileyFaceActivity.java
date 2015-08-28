package com.zhang.mydemo.simleyparse;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zhang.mydemo.BaseActivity;
import com.zhang.mydemo.Constant;
import com.zhang.mydemo.R;
import com.zhang.mydemo.util.ScreenUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class SmileyFaceActivity extends BaseActivity implements EmotionLinearLayout.OnEmotionLayoutListener {
    private static ListView mListView;
    private EmotionLinearLayout emotionLayout;

    private ActionBar mActionBar;
    private MyAdapter myAdapter;
    private List<String> list = new ArrayList<>();

    private View rootView;
    private ViewTreeObserver.OnGlobalLayoutListener mLayoutChangeListener;
    private int previousHeightDiffrence = 0;
    private boolean scrollableFlag = false;                     //是否滚动（仅点击评论、回复时滚动，其他弹出键盘列表不滚动）
    private static final int SCROLL_LIST_VIEW = 2;              //滚动listView;
    private static int screenHeight;
    private static int statusBarHeight;
    private static int actionBarHeight;
    private static int commentEditDialogHeight;
    private int scrollItemBottomPosition;
    private MyHandler mHandler;

    private static class MyHandler extends Handler{
        private final WeakReference<SmileyFaceActivity> mActivity;

        public MyHandler(SmileyFaceActivity activity){
            this.mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            SmileyFaceActivity activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case SCROLL_LIST_VIEW:
                        Bundle data = msg.getData();
                        if (data != null) {
                            listViewScrollHeight(data.getInt("scrollItemPosition"), data.getInt("softKeyboardHeight"));
                        }
                        break;
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContent() {
        setContentView(R.layout.activity_smiley_face);
    }

    @Override
    public void findViews() {
        mListView = (ListView) findViewById(R.id.lv_list_view);
        emotionLayout = (EmotionLinearLayout) findViewById(R.id.emotion_layout);

        rootView = getWindow().getDecorView();

        mActionBar = getSupportActionBar();
    }

    @Override
    public void setData() {
        statusBarHeight = ScreenUtil.getStatusHeight(this);
        actionBarHeight = ScreenUtil.getActionBarHeight(this);
        commentEditDialogHeight = getResources().getDimensionPixelOffset(R.dimen.comment_edit_layout_height);
        mHandler = new MyHandler(this);

        list = initStr();
        myAdapter = new MyAdapter(this);
        mListView.setAdapter(myAdapter);
    }

    @Override
    public void showContent() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                actionBarHeight = mActionBar.getHeight();
                showReplyDialog();
                getScrollItemBottomPosition(position);
            }
        });

        mLayoutChangeListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                rootView.getWindowVisibleDisplayFrame(rect);

                screenHeight = rootView.getRootView().getHeight();
                int keyboardHeight = screenHeight - rect.bottom;

                if (keyboardHeight - previousHeightDiffrence > 50 && scrollableFlag) {
                    scrollableFlag = false;
                    //当软键盘弹出时，listView计算滚动距离
//                	System.out.println("------ keyboard change:" + (previousHeightDiffrence - keyboardHeight));
//                	listViewScrollHeight(keyboardHeight);
                    Message msg = mHandler.obtainMessage();
                    msg.what = SCROLL_LIST_VIEW;
                    Bundle data = new Bundle();
                    data.putInt("scrollItemPosition", scrollItemBottomPosition);
                    data.putInt("softKeyboardHeight", keyboardHeight);
                    msg.setData(data);
                    mHandler.sendMessageDelayed(msg, 100);

                }
                previousHeightDiffrence = keyboardHeight;

                if (keyboardHeight > 100) {
                    emotionLayout.setEmotionLayoutHeight(keyboardHeight);
                }
            }
        };

        rootView.getViewTreeObserver().addOnGlobalLayoutListener(mLayoutChangeListener);
        emotionLayout.setOnEmotionLayoutListener(this);
    }

    @Override
    protected void onDestroy() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            rootView.getViewTreeObserver().removeOnGlobalLayoutListener(mLayoutChangeListener);
        } else {
            rootView.getViewTreeObserver().removeGlobalOnLayoutListener(mLayoutChangeListener);
        }
        super.onDestroy();
    }

    private void getScrollItemBottomPosition(int position){
        scrollItemBottomPosition = calcScrollItemBottomPosition(position);
    }

    /**
     * 获取控件底部Y轴位置
     * @param indexPosition			//点击indexRecord的位置
     */
    private int calcScrollItemBottomPosition(int indexPosition){
        int firstPo = mListView.getFirstVisiblePosition();
//		int lastPo = mListView.getLastVisiblePosition();
        int relativePo = indexPosition - firstPo;
        int yBottom = Constant.ERROR_INT;

        View view = mListView.getChildAt(relativePo);
        if (view != null) {
            yBottom = view.getBottom();
			System.out.println("indexItemView------left" + view.getLeft() + "-----top:" + view.getTop() + "----right:" + view.getRight() + "----bottom:" + view.getBottom());
        }
        return yBottom;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_default, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private List<String> initStr(){
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 31; i++) {
            list.add("高兴[疯了]" + "悲伤[祈祷]" + "眨眼[眨眼]" + "吐舌[吐舌]" + i);
        }
        return list;
    }

    private void sendComment(String str){
        list.add(str);
        myAdapter.notifyDataSetChanged();
    }

    /**
     * 软键盘弹出时计算listview滚动距离
     * 点击Item底部的Y轴位置 - 软件盘高度 = 滚动距离
     */
    private static void listViewScrollHeight(int scrollItemPosition, int softKeyboardHeight){
        if (scrollItemPosition >= 0) {
//            int scrollHeight = scrollItemPosition - (screenHeight - statusBarHeight - actionBarHeight - commentEditDialogHeight - softKeyboardHeight);
//            System.out.println("-----------actionbarHeight:" + actionBarHeight + "---statusBar:" + statusBarHeight + "---commentDialog:" + commentEditDialogHeight + "-- screen:" + screenHeight);
            int scrollHeight = (statusBarHeight + actionBarHeight + scrollItemPosition + commentEditDialogHeight + softKeyboardHeight) - screenHeight;
            if (Math.abs(scrollHeight) > 10) {
                mListView.smoothScrollBy(scrollHeight, 200);
            }
        }
    }



    private CommentEditDialog replyDialog;
    private void showReplyDialog(){
        scrollableFlag = true;
        if (replyDialog == null) {
            replyDialog = new CommentEditDialog(this, R.style.dialog);
            replyDialog.setEmotionLayoutListener(this);

            replyDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            replyDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    hideSoftKeyboard();
                }
            });

            replyDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                        hideReplyDialog();
                    }
                    return false;
                }
            });
        }

        String hintStr = "评论:";
        replyDialog.setContentHint(hintStr);
        emotionLayout.setContentHint(hintStr);
        showSoftKeyboard();
        if (emotionLayout.getVisibility() == View.VISIBLE) {
            emotionLayout.hideEmotionView();
        }

        replyDialog.show();
    }

    private void hideReplyDialog(){
        if (replyDialog != null) {
            hideSoftKeyboard();
            replyDialog.hide();
        }
    }

    @Override
    public void sendMessage(String str) {
        if (!TextUtils.isEmpty(str)) {
            hideReplyDialog();
            emotionLayout.hideEmotionView();

            sendComment(str);
        }
    }



    @Override
    public void smileyBtnClick(boolean showEmotion) {
        if (showEmotion) {  //显示表情栏
            emotionLayout.setContent(replyDialog.getContent());
            emotionLayout.showEmotionView();
            hideSoftKeyboard();
            replyDialog.hide();
        } else {    //隐藏表情栏
            showSoftKeyboard();
            replyDialog.setContent(emotionLayout.getContent());
            replyDialog.show();
            emotionLayout.setAnimation(AnimationUtils.loadAnimation(this, R.anim.comment_edit_dialog_out));
            emotionLayout.hideEmotionView();
        }
    }

    @Override
    public void emotionOutsideClick() {
        emotionLayout.hideEmotionView();
    }

    /**
     * 隐藏软件盘
     */
    private void hideSoftKeyboard() {
        if (replyDialog != null) {
            replyDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /**
     * 显示软键盘
     */
    private void showSoftKeyboard(){
        if (replyDialog != null) {
            replyDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }




































    private class MyAdapter extends BaseAdapter{
        private LayoutInflater inflater;

        SmileyParser parser = null;

        public MyAdapter(Context ctx) {
            inflater = getLayoutInflater();
            SmileyParser.init(ctx);
            parser = SmileyParser.getInstance();
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public String getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String str = getItem(position);
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
                holder.textView = (TextView) convertView.findViewById(android.R.id.text1);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.textView.setText(parser.addSmileySpans(str));
            return convertView;
        }
    }

    private static class ViewHolder{
        TextView textView;
    }
}
