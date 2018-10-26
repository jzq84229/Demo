package com.zhang.example;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/** 
 * @Description: 文本款伸缩 ，当文本内容超出最大行数（2行），显示...，
 * 				   点击文本展开全部内容，再次点击文本收缩内容
 * @author sz.jun.zhang@gmail.com
 * @date 2013-8-1 上午2:03:46 
 * @version V1.0 
 */
public class ExpandTextViewActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_expand_textview);
		
		final TextView textView = (TextView) findViewById(R.id.textView);
		textView.setText("sadas阿上的发生大幅爱上 上的发生大幅阿斯顿发生的撒地方撒地方阿斯顿方法" +
				"阿斯顿发生的发生的事发生的发生阿斯顿发生的发生的事发生的发生阿斯顿发生的发生的事发生的发生" +
				"阿斯顿发生的发生的事发生的发生阿斯顿发生的发生的事发生的发生阿斯顿发生的发生的事发生的发生" +
				"阿斯顿发生的发生的事发生的发生阿斯顿发生的发生的事发生的发生阿斯顿发生的发生的事发生的发生" +
				"阿斯顿发生的发生的事发生的发生阿斯顿发生的发生的事发生的发生阿斯顿发生的发生的事发生的发生");
//		textView.setText("s说的方法对方");
		textView.setOnClickListener(new OnClickListener() {
			boolean flag = false;
			@Override
			public void onClick(View v) {
				if (flag) {
					flag = false;
					textView.setEllipsize(null); // 展开
					textView.setSingleLine(flag);
				} else {
					 flag = true;
					 textView.setMaxLines(2);
					 textView.setEllipsize(TextUtils.TruncateAt.END); // 收缩
				}
			}
		});
	}

}
