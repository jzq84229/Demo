package com.zhang.example.guideview;

import com.zhang.example.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class GuideActivity extends Activity implements OnViewChangeListener{
	private MyScrollLayout mScrollLayout;
	private ImageView[] imgs;
	private int count;
	private int currentItem;
	private RelativeLayout mainRLayout;
	
	private int[] images = new int[]{
			R.drawable.w01,
			R.drawable.w02,
			R.drawable.w03,
			R.drawable.w04,
			R.drawable.w05,
			R.drawable.w06,
			R.drawable.w07,
			R.drawable.w08
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_guide);
		initView();
	}
	private int imageId = 0;
	
	private void initView() {
		mScrollLayout  = (MyScrollLayout) findViewById(R.id.ScrollLayout);
		mainRLayout = (RelativeLayout) findViewById(R.id.mainRLayout);
//		count = mScrollLayout.getChildCount();
		count = images.length;
		imgs = new ImageView[count];
		for(int i = 0; i< count;i++) {
			RelativeLayout relativeLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.guide_image_view, null);
			mScrollLayout.addView(relativeLayout);
			imageId = images[i];
			imgs[i] = (ImageView) relativeLayout.findViewById(R.id.imageView);
			imgs[i].setImageResource(images[i]);
//			imgs[i].setOnLongClickListener(new OnLongClickListener() {
//				
//				@Override
//				public boolean onLongClick(View v) {
//					Toast.makeText(GuideActivity.this, "图片ID:" + imageId, Toast.LENGTH_SHORT).show();
//					return false;
//				}
//			});
//			imgs[i].setEnabled(true);
			imgs[i].setTag(i);
		}
		currentItem = 0;
		imgs[currentItem].setEnabled(false);
		mScrollLayout.SetOnViewChangeListener(this);
	}
	
//	private View.OnClickListener onClick = new View.OnClickListener() {
//		@Override
//		public void onClick(View v) {
//			switch (v.getId()) {
//			case R.id.startBtn:
//				mScrollLayout.setVisibility(View.GONE);
//				pointLLayout.setVisibility(View.GONE);
//				animLayout.setVisibility(View.VISIBLE);
//				mainRLayout.setBackgroundResource(R.drawable.whatsnew_bg);
//				Animation leftOutAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_left);
//				Animation rightOutAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_right);
////				Animation leftOutAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadedout_to_left_down);
////				Animation rightOutAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadedout_to_right_down);
//				leftLayout.setAnimation(leftOutAnimation);
//				rightLayout.setAnimation(rightOutAnimation);
//				leftOutAnimation.setAnimationListener(new AnimationListener() {
//					@Override
//					public void onAnimationStart(Animation animation) {
//						mainRLayout.setBackgroundColor(R.color.bgColor);
//					}
//					@Override
//					public void onAnimationRepeat(Animation animation) {
//					}
//					@Override
//					public void onAnimationEnd(Animation animation) {
//						leftLayout.setVisibility(View.GONE);
//						rightLayout.setVisibility(View.GONE);
//						Intent intent = new Intent(GuideActivity.this,OtherActivity.class);
//						GuideActivity.this.startActivity(intent);
//						GuideActivity.this.finish();
//						overridePendingTransition(R.anim.zoom_out_enter, R.anim.zoom_out_exit);
//					}
//				});
//				break;
//			}
//		}
//	};

	@Override
	public void OnViewChange(int position) {
		setcurrentPoint(position);
	}

	private void setcurrentPoint(int position) {
		if(position < 0 || position > count -1 || currentItem == position) {
			return;
		}
		imgs[currentItem].setEnabled(true);
		imgs[position].setEnabled(false);
		currentItem = position;
	}

}
