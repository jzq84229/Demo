package com.zhang.example.bitmapFun.ui;

import com.zhang.example.BuildConfig;
import com.zhang.example.R;
import com.zhang.example.bitmapFun.utils.ImageCache;
import com.zhang.example.bitmapFun.utils.ImageCache.ImageCacheParams;
import com.zhang.example.bitmapFun.utils.ImageFetcher;
import com.zhang.example.bitmapFun.utils.Utils;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Toast;

/** 
 * @Description: TODO(添加描述) 
 * @author sz.jun.zhang@gmail.com
 * @date 2013-6-4 下午1:52:51 
 * @version V1.0 
 */
public class ImageDetailActivity extends FragmentActivity implements OnClickListener {
	private static final String IMAGE_CACHE_DIR = "images";
    public static final String EXTRA_IMAGE = "extra_image";

    private ImagePagerAdapter mAdapter;
    private ImageFetcher mImageFetcher;
    private ViewPager mPager;
	
	@TargetApi(11)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (BuildConfig.DEBUG) {
			Utils.enableStrictMode();
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_detail_pager);
		
		final DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		final int height = displayMetrics.heightPixels;
		final int width = displayMetrics.widthPixels;
		
		final int longest = (height > width ? height : width) / 2;
		
		ImageCache.ImageCacheParams cacheParams = 
				new ImageCache.ImageCacheParams(this, IMAGE_CACHE_DIR);
		cacheParams.setMemCacheSizePercent(0.25f);
		
		mImageFetcher = new ImageFetcher(this, longest);
		mImageFetcher.addImageCache(getSupportFragmentManager(), cacheParams);
		mImageFetcher.setImageFadeIn(false);
		
		mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), Images.imageUrls.length);
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);
		mPager.setPageMargin((int) getResources().getDimension(R.dimen.image_detail_pager_margin));
		mPager.setOffscreenPageLimit(2);
		
		getWindow().addFlags(LayoutParams.FLAG_FULLSCREEN);
		
		if (Utils.hasHoneycomb()) {
			final ActionBar actionBar = getActionBar();
			
			actionBar.setDisplayShowTitleEnabled(false);
			actionBar.setDisplayHomeAsUpEnabled(true);
			
			mPager.setOnSystemUiVisibilityChangeListener(
					new View.OnSystemUiVisibilityChangeListener() {
						@Override
						public void onSystemUiVisibilityChange(int vis) {
							if ((vis & View.SYSTEM_UI_FLAG_LOW_PROFILE) != 0) {
								actionBar.hide();
							} else {
								actionBar.show();
							}
						}
					});
			mPager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
			actionBar.hide();
		}
		
		final int extraCurrentItem = getIntent().getIntExtra(EXTRA_IMAGE, -1);
		if (extraCurrentItem != -1) {
			mPager.setCurrentItem(extraCurrentItem);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		mImageFetcher.setExitTasksEarly(false);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		mImageFetcher.setExitTasksEarly(true);
		mImageFetcher.flushCache();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mImageFetcher.clearCache();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;

		case R.id.clear_cache:
			mImageFetcher.clearCache();
			Toast.makeText(this, R.string.clear_cache_complete_toast, Toast.LENGTH_SHORT).show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}
	
	public ImageFetcher getImageFetcher(){
		return mImageFetcher;
	}
	
	private class ImagePagerAdapter extends FragmentStatePagerAdapter{
		private final int mSize;

		public ImagePagerAdapter(FragmentManager fm, int size) {
			super(fm);
			mSize = size;
		}

		@Override
		public Fragment getItem(int position) {
			return ImageDetailFragment.newInstance(Images.imageUrls[position]);
		}

		@Override
		public int getCount() {
			return mSize;
		}
		
	}
	
	@TargetApi(11)
	@Override
	public void onClick(View v) {
		final int vis = mPager.getSystemUiVisibility();
		if ((vis & View.SYSTEM_UI_FLAG_LOW_PROFILE) != 0) {
			mPager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
		} else {
			mPager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
		}
	}


}
