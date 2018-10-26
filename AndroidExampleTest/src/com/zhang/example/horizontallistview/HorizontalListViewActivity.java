package com.zhang.example.horizontallistview;

import java.util.ArrayList;
import java.util.List;

import com.zhang.example.R;
import com.zhang.example.R.id;
import com.zhang.example.R.layout;
import com.zhang.example.imageViewLoader.ImageDownloader;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/** 
 * @Description: TODO(添加描述) 
 * @author sz.jun.zhang@gmail.com
 * @date 2013-4-3 下午4:18:34 
 * @version V1.0 
 */
public class HorizontalListViewActivity extends Activity {
	private ImageDownloader imageDownloader;
	private List<String> strList = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_horizontallistview);
		
		imageDownloader = new ImageDownloader(this);
		for (int i = 0; i < 200; i++) {
			strList.add("Text " + i);
		}
		
		HorizontalListView listview = (HorizontalListView) findViewById(R.id.listview);
		listview.setAdapter(mAdapter);
		
	}
	
//	private static String[] dataObjects = new String[]{"Text #1",
//		"Text #2",
//		"Text #3" }; 
	
	private BaseAdapter mAdapter = new BaseAdapter() {

		private OnClickListener mOnButtonClicked = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(HorizontalListViewActivity.this);
				builder.setMessage("hello from " + v);
				builder.setPositiveButton("Cool", null);
				builder.show();
				
			}
		};

		@Override
		public int getCount() {
			return strList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View retval = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem, null);
			ImageView imageView = (ImageView) retval.findViewById(R.id.image);
			imageDownloader.download("http://a.hiphotos.baidu.com/album/w%3D2048/sign=2a89965ea9d3fd1f3609a53a0476251f/ac6eddc451da81cb91d986bd5366d01609243186.jpg", imageView);
			TextView title = (TextView) retval.findViewById(R.id.title);
			Button button = (Button) retval.findViewById(R.id.clickbutton);
			button.setOnClickListener(mOnButtonClicked);
			title.setText(strList.get(position));
			
			return retval;
		}
		
	};
}
