package com.zhang.example.viewpage;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zhang.example.R;
import com.zhang.example.imageViewLoader.ImageDownloader;
import com.zhang.example.utils.Images;
import com.zhang.example.viewpage.library.PhotoView;

/** 
 * @Description: TODO(添加描述) 
 * @author sz.jun.zhang@gmail.com
 * @date 2013-8-19 上午7:48:56 
 * @version V1.0 
 */
public class ViewPageActivity extends Activity {
	
	private ViewPager mViewPage;
	private LinearLayout linearLayout;
	private ImageView imageViewItem;
	private LayoutInflater inflate;
	private ImageDownloader mImageDownloader;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_viewpage);
		
		setUpViews();
//		setUpListeners();
	}
	
	private void setUpViews(){
		mViewPage = (HackyViewPager) findViewById(R.id.mViewPage);
		linearLayout = (LinearLayout) findViewById(R.id.llayout);
		MyPageAdapter adapter = new MyPageAdapter();
		mViewPage.setAdapter(adapter);
		mImageDownloader = new ImageDownloader(this);
		inflate = LayoutInflater.from(this);
		
//		for (int i = 0; i < Images.imageUrls.length; i++) {
//			imageViewItem = (ImageView) inflate.inflate(R.layout.imageview_item, null);
//			linearLayout.addView(imageViewItem);
//		}
	}
	
//	private void setUpListeners(){
//		mViewPage.setOnPageChangeListener(new OnPageChangeListener() {
//			
//			@Override
//			public void onPageSelected(int position) {
////				setCurrentPoint(position);
//			}
//			
//			@Override
//			public void onPageScrolled(int arg0, float arg1, int arg2) {
//				
//			}
//			
//			@Override
//			public void onPageScrollStateChanged(int arg0) {
//				
//			}
//		});
//	}
	
//	private void setCurrentPoint(int position){
//		if (position < 0 || position > imagesUrl.size() - 1 || currentItem == position) {
//            return;
//        }
//	}
	
	private class MyPageAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			return Images.imageUrls.length;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			PhotoView photoView = new PhotoView(container.getContext());
			mImageDownloader.download(Images.imageUrls[position], photoView);
			container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			return photoView;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View)object);
		}
		
		
	}

}
