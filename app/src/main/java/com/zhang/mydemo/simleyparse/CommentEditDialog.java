package com.zhang.mydemo.simleyparse;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.zhang.mydemo.R;

/**
 * Created by zjun on 2015/8/27 0027.
 */
public class CommentEditDialog extends Dialog implements View.OnClickListener {
    private EditText et_content;

    private EmotionLinearLayout.OnEmotionLayoutListener mEmotionLayoutListener;


    public CommentEditDialog(Context context) {
        super(context);
        init();
    }

    public CommentEditDialog(Context context, int theme) {
        super(context, theme);
        init();
    }

    protected CommentEditDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    @Override
    public void cancel() {
        clearContent();
        super.cancel();
    }

    @Override
    public void hide() {
        clearContent();
        super.hide();
    }

    @Override
    public void show() {
        et_content.requestFocus();
        super.show();
    }

    private void init(){
        setContentView(R.layout.comment_edit_layout);

        initViews();
        windowDeploy();
    }

    private void initViews(){
        et_content = (EditText) findViewById(R.id.et_content);
        Button btn_send = (Button) findViewById(R.id.btn_send);
        ImageButton btn_smiley = (ImageButton) findViewById(R.id.btn_smiley);

        btn_send.setOnClickListener(this);
        btn_smiley.setOnClickListener(this);
    }

    private void windowDeploy(){
        setCanceledOnTouchOutside(true);

        Window window = getWindow();
        window.setWindowAnimations(R.style.commentEditDialogAnim);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.gravity = Gravity.BOTTOM;
        wl.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wl);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                if (mEmotionLayoutListener != null) {
                    mEmotionLayoutListener.sendMessage(et_content.getText().toString());
                    clearContent();
                }
                break;
            case R.id.btn_smiley:
                if (mEmotionLayoutListener != null) {
                    mEmotionLayoutListener.smileyBtnClick(true);
                }
                break;
        }

    }

    public void setEmotionLayoutListener(EmotionLinearLayout.OnEmotionLayoutListener mEmotionLayoutListener){
        this.mEmotionLayoutListener = mEmotionLayoutListener;
    }


    /**
     * 设置内容框提示文字
     * @param hintStr
     */
    public void setContentHint(String hintStr){
        et_content.setHint(hintStr);
    }

    /**清空文本编辑框*/
    private void clearContent(){
        et_content.getText().clear();
    }

    public Editable getContent(){
        return et_content.getText();
    }

    public void setContent(Editable et){
        et_content.setText(et);
        et_content.setSelection(et.length());
    }
}
