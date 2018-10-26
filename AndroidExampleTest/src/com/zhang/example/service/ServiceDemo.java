package com.zhang.example.service;

import com.zhang.example.R;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/** 
 * @Description: TODO(添加描述) 
 * @author sz.jun.zhang@gmail.com
 * @date 2013-7-11 上午8:34:22 
 * @version V1.0 
 */
public class ServiceDemo extends Activity implements OnClickListener {

	private MyService mMyService;
	private TextView mTextView;
	private Button startServiceButton;
	private Button stopServiceButton;
	private Button bindServiceButton;
	private Button unbindServiceButton;
	private Context mContext;
	
	//这里需要用到ServiceConnection在Context.bindService和context.unBindService()里用到
	private ServiceConnection mServiceConnection = new ServiceConnection() {
		//当我bindService时，让TextView显示MyService里getSystemTime()方法的返回值 
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mMyService = ((MyService.MyBinder)service).getService();
			mTextView.setText("I am from Service:" + mMyService.getSystemTime());
		}
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_service_demo);
		setupViews();
	}
	
	private void setupViews(){
		mContext = ServiceDemo.this;
		mTextView = (TextView) findViewById(R.id.text);
		startServiceButton = (Button) findViewById(R.id.startservice);
		stopServiceButton = (Button) findViewById(R.id.stopservice);
		bindServiceButton = (Button) findViewById(R.id.bindservice);
		unbindServiceButton = (Button) findViewById(R.id.unbindservice);
		
		startServiceButton.setOnClickListener(this);
		stopServiceButton.setOnClickListener(this);
		bindServiceButton.setOnClickListener(this);
		unbindServiceButton.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		Intent i = new Intent();
		i.setClass(ServiceDemo.this, MyService.class);
		switch (v.getId()) {
		case R.id.startservice:
			mContext.startService(i);
			break;
		case R.id.stopservice:
			mContext.stopService(i);
			break;
		case R.id.bindservice:
			mContext.bindService(i, mServiceConnection, BIND_AUTO_CREATE);
			break;
		case R.id.unbindservice:
			mContext.unbindService(mServiceConnection);
			break;
		}

	}

}
