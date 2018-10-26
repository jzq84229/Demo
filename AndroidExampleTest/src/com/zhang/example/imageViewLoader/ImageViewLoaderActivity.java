package com.zhang.example.imageViewLoader;

import com.zhang.example.R;
import com.zhang.example.R.layout;

import android.app.ListActivity;
import android.os.Bundle;

public class ImageViewLoaderActivity extends ListActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_image_view_loader);
		setListAdapter(new DemoImageAdapter(ImageViewLoaderActivity.this));
	}

}
