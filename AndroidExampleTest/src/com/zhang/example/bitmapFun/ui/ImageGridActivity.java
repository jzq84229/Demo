package com.zhang.example.bitmapFun.ui;

import com.zhang.example.BuildConfig;
import com.zhang.example.bitmapFun.utils.Utils;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

/** 
 * @Description: TODO(添加描述) 
 * @author sz.jun.zhang@gmail.com
 * @date 2013-6-4 下午1:46:27 
 * @version V1.0 
 */
public class ImageGridActivity extends FragmentActivity {
	private static final String TAG = "ImageGridActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (BuildConfig.DEBUG) {
			Utils.enableStrictMode();
		}
		super.onCreate(savedInstanceState);
		
		if (getSupportFragmentManager().findFragmentByTag(TAG) == null) {
			final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.add(android.R.id.content, new ImageGridFragment(), TAG);
			ft.commit();
		}
	}

}
