package com.zhang.example.bitmapFun.ui;

import com.zhang.example.BuildConfig;
import com.zhang.example.R;
import com.zhang.example.R.id;
import com.zhang.example.bitmapFun.utils.ImageCache.ImageCacheParams;
import com.zhang.example.bitmapFun.utils.ImageFetcher;
import com.zhang.example.bitmapFun.utils.Utils;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

/** 
 * @Description: TODO(添加描述) 
 * @author sz.jun.zhang@gmail.com
 * @date 2013-6-4 下午1:47:41 
 * @version V1.0 
 */
public class ImageGridFragment extends Fragment implements AdapterView.OnItemClickListener {
	private static final String TAG = "ImageGridFragment";
    private static final String IMAGE_CACHE_DIR = "thumbs";
    
    private int mImageThumbSize;
    private int mImageThumbSpacing;
    private ImageAdapter mAdapter;
    private ImageFetcher mImageFetcher;
    
    public ImageGridFragment(){}
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		
		mImageThumbSize = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_size);
		mImageThumbSpacing = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_spacing);
		
		mAdapter = new ImageAdapter(getActivity());
		
		ImageCacheParams cacheParams = new ImageCacheParams(getActivity(), IMAGE_CACHE_DIR);
		cacheParams.setMemCacheSizePercent(0.25f);
		
		mImageFetcher = new ImageFetcher(getActivity(), mImageThumbSize);
		mImageFetcher.setLoadingImage(R.drawable.empty_photo);
		mImageFetcher.addImageCache(getActivity().getSupportFragmentManager(), cacheParams);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.image_grid_fragment, container, false);
		final GridView mGridView = (GridView) v.findViewById(R.id.gridView);
		mGridView.setAdapter(mAdapter);
		mGridView.setOnItemClickListener(this);
		mGridView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
					mImageFetcher.setPauseWork(true);
				} else {
					mImageFetcher.setPauseWork(false);
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}
		});
		
		mGridView.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					
					@Override
					public void onGlobalLayout() {
						if (mAdapter.getmNumColumns() == 0) {
							final int numColumns = (int) Math.floor(mGridView.getWidth() / (mImageThumbSize + mImageThumbSpacing));
							if (numColumns > 0) {
								final int columnWidth = (mGridView.getWidth() / numColumns) - mImageThumbSpacing;
								mAdapter.setmNumColumns(numColumns);
								mAdapter.setmItemHeight(columnWidth);
								if (BuildConfig.DEBUG) {
									Log.d(TAG, "onCreateView - numColumns set to " + numColumns);
								}
							}
						}
					}
				});
		
		return v;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		mImageFetcher.setExitTasksEarly(false);
		mAdapter.notifyDataSetChanged();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		mImageFetcher.setPauseWork(false);
		mImageFetcher.setExitTasksEarly(true);
		mImageFetcher.flushCache();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mImageFetcher.closeCache();
	}
	
	@TargetApi(16)
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		final Intent i = new Intent(getActivity(), ImageDetailActivity.class);
		i.putExtra(ImageDetailActivity.EXTRA_IMAGE, (int) id);
		if (Utils.hasJellyBean()) {
			ActivityOptions options = ActivityOptions.makeScaleUpAnimation(view, 0, 0, view.getWidth(), view.getHeight());
			getActivity().startActivity(i, options.toBundle());
		} else {
			startActivity(i);
		}
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.main_menu, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.clear_cache:
			mImageFetcher.clearCache();
			Toast.makeText(getActivity(), R.string.clear_cache_complete_toast, Toast.LENGTH_SHORT).show();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	private class ImageAdapter extends BaseAdapter{
		private final Context mContext;
		private int mItemHeight = 0;
		private int mNumColumns = 0;
		private int mActionBarHeight = 0;
		private GridView.LayoutParams mImageViewLayoutParams;
		
		public ImageAdapter(Context context){
			super();
			mContext = context;
			mImageViewLayoutParams = new GridView.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			
			TypedValue tv = new TypedValue();
			if (context.getTheme().resolveAttribute(
					android.R.attr.actionBarSize, tv, true)) {
				mActionBarHeight = TypedValue.complexToDimensionPixelSize(
						tv.data, context.getResources().getDisplayMetrics());
			}
		}

		@Override
		public int getCount() {
			return Images.imageThumbUrls.length + mNumColumns;
		}

		@Override
		public Object getItem(int position) {
			return position < mNumColumns ? null : Images.imageThumbUrls[position - mNumColumns];
		}

		@Override
		public long getItemId(int position) {
			return position < mNumColumns ? 0 : position - mNumColumns;
		}
		
		@Override
		public int getViewTypeCount() {
			return 2;
		}
		
		@Override
		public int getItemViewType(int position) {
			return (position < mNumColumns) ? 1 : 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (position < mNumColumns) {
				if (convertView == null) {
					convertView = new View(mContext);
				}
				convertView.setLayoutParams(new AbsListView.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT, mActionBarHeight));
				return convertView;
			}
			
			ImageView imageView;
			if (convertView == null) {
				imageView = new RecyclingImageView(mContext);
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageView.setLayoutParams(mImageViewLayoutParams);
			} else {
				imageView = (ImageView) convertView;
			}
			if (imageView.getLayoutParams().height != mItemHeight) {
				imageView.setLayoutParams(mImageViewLayoutParams);
			}
			mImageFetcher.loadImage(Images.imageThumbUrls[position - mNumColumns], imageView);
			return imageView;
		}

		public void setmItemHeight(int height) {
			if (height == mItemHeight) {
				return;
			}
			mItemHeight = height;
			mImageViewLayoutParams = new GridView.LayoutParams(LayoutParams.MATCH_PARENT, mItemHeight);
			mImageFetcher.setImageSize(height);
			notifyDataSetChanged();
		}

		public void setmNumColumns(int numColumns) {
			mNumColumns = numColumns;
		}
		
		public int getmNumColumns() {
			return mNumColumns;
		}
	}

}
