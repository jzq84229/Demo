package com.zhang.example;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TestActivity extends Activity {

	/** Called when the activity is first created. */
	private Button butStart;
	private Button butStop;
	TextView tv;
	int i = 0;
	Handler handler = new Handler();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		/**
		 * Handler在那个Activity中new的就且仅属于那个Activity的
		 * */
		// all by zcl qq158067568
		butStart = (Button) findViewById(R.id.button01);
		butStop = (Button) findViewById(R.id.button02);
		tv = (TextView) findViewById(R.id.textView01);
		butStart.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				handler.post(runnable);
			}
		});
		butStop.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				handler.removeCallbacks(runnable);
			}
		});
	}

	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			tv.setText(String.valueOf(i++));
			handler.postDelayed(runnable, 1000);
		}
	};

}
