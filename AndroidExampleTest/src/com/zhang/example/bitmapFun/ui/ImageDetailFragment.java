package com.zhang.example.bitmapFun.ui;


import com.zhang.example.R;
import com.zhang.example.bitmapFun.utils.ImageFetcher;
import com.zhang.example.bitmapFun.utils.ImageWorker;
import com.zhang.example.bitmapFun.utils.Utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

/** 
 * @Description: TODO(添加描述) 
 * @author sz.jun.zhang@gmail.com
 * @date 2013-6-5 下午5:26:47 
 * @version V1.0 
 */
public class ImageDetailFragment extends Fragment {
	private static final String IMAGE_DATA_EXTRA = "extra_image_data";
    private String mImageUrl;
    private ImageView mImageView;
    private ImageFetcher mImageFetcher;
    
    public static ImageDetailFragment newInstance(String imageUrl){
    	final ImageDetailFragment f = new ImageDetailFragment();
    	
    	final Bundle args = new Bundle();
    	args.putString(IMAGE_DATA_EXTRA, imageUrl);
    	f.setArguments(args);
    	return f;
    }
    
    public ImageDetailFragment(){}
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	mImageUrl = getArguments() != null ? getArguments().getString(IMAGE_DATA_EXTRA) : null;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	final View v = inflater.inflate(R.layout.image_detail_fragment, container, false);
    	mImageView = (ImageView) v.findViewById(R.id.imageView);
    	return v;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	
    	if (ImageDetailActivity.class.isInstance(getActivity())) {
			mImageFetcher = ((ImageDetailActivity) getActivity()).getImageFetcher();
			mImageFetcher.loadImage(mImageUrl, mImageView);
		}
    	
    	if (OnClickListener.class.isInstance(getActivity()) && Utils.hasHoneycomb()) {
			mImageView.setOnClickListener((OnClickListener) getActivity());
		}
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	if (mImageView != null) {
			ImageWorker.cancelWork(mImageView);
			mImageView.setImageDrawable(null);
		}
    }

}
