package com.zhang.example.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.text.format.Time;
import android.util.Log;

/** 
 * @Description: TODO(添加描述) 
 * @author sz.jun.zhang@gmail.com
 * @date 2013-7-11 上午8:35:10 
 * @version V1.0 
 */
public class MyService extends Service {
	//定义一个Tag标签
	private static final String TAG = "MyService";
	//这里定义吧一个Binder类，用在onBind()有方法里，这样Activity那边可以获取到
	private MyBinder mBinder = new MyBinder();
	
	@Override
	public IBinder onBind(Intent intent) {
		Log.e(TAG, "start IBinder~~~");
		return mBinder;
	}
	
	@Override
	public void onCreate() {
		Log.e(TAG, "start onCreate~~~");
		super.onCreate();
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		Log.e(TAG, "start onStart~~~");
		super.onStart(intent, startId);
	}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "start onStartCommand~~~");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
	public void onDestroy() {
		Log.e(TAG, "start onDestroy~~~");
		super.onDestroy();
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		Log.e(TAG, "start onUnbind~~~");
		return super.onUnbind(intent);
	}
	
	public String getSystemTime(){
		Time t = new Time();
		t.setToNow();
		return t.toString();
	}
	
	//这里我写了一个获取当前时间的函数，不过没有格式化就先这么着吧
	public class MyBinder extends Binder{
		MyService getService(){
			return MyService.this;
		}
	}

}
