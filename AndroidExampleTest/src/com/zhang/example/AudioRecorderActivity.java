package com.zhang.example;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

/*
 * The application needs to have the permission to write to external storage
 * if the output file is written to the external storage, and also the
 * permission to record audio. These permissions must be set in the
 * application's AndroidManifest.xml file, with something like:
 *
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
 * <uses-permission android:name="android.permission.RECORD_AUDIO" />
 *
 */
public class AudioRecorderActivity extends Activity {
	private static final String LOG_TAG = "AudioRecordTest";
	private static String mFileName = null;
	//录音按钮
	private RecordButton mRecordButton = null;
	private MediaRecorder mRecorder = null;
	//回放按钮
	private PlayButton mPlayButton = null;
	private MediaPlayer mPlayer = null;
	
	//当录音按钮被click时调用此方法，开始或停止录音  
	private void onRecord(boolean start){
		if (start) {
			startRecording();
		} else {
			stopRecording();
		}
	}
	
	//当播放按钮被click时调用此方法，开始或停止播放  
	private void onPlay(boolean start){
		if (start) {
			startPlaying();
		} else {
			stopPlaying();
		}
	}
	
	private void startPlaying(){
		mPlayer = new MediaPlayer();
		try {
			//设置要播放的文件  
			mPlayer.setDataSource(mFileName);
			mPlayer.prepare();
			//播放
			mPlayer.start();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			Log.e(LOG_TAG, "prepare() failed");
		}
	}
	
	//停止播放
	private void stopPlaying(){
		mPlayer.release();
		mPlayer = null;
	}
	
	private void startRecording(){
		mRecorder = new MediaRecorder();
		//设置音源为Micphone
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		//设置封装格式
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mRecorder.setOutputFile(mFileName);
		//设置编码格式
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		
		try {
			mRecorder.prepare();
		} catch (Exception e) {
			Log.e(LOG_TAG, "prepare() failed");
		}
		mRecorder.start();
	}
	
	private void stopRecording(){
		mRecorder.stop();
		mRecorder.release();
		mRecorder = null;
	}
	
	
	//定义录音按钮  
	class RecordButton extends Button
	{
		boolean mStartRecording = true;
		
		OnClickListener clicker = new OnClickListener() {  
            public void onClick(View v) {  
                onRecord(mStartRecording);  
                if (mStartRecording) {  
                    setText("Stop recording");  
                } else {  
                    setText("Start recording");  
                }  
                mStartRecording = !mStartRecording;  
            }  
        };

		public RecordButton(Context ctx) {
			super(ctx);
			setText("Start recording");
			setOnClickListener(clicker);
		}
	}
	
    //定义播放按钮  
    class PlayButton extends Button {  
        boolean mStartPlaying = true;  
  
        OnClickListener clicker = new OnClickListener() {  
            public void onClick(View v) {  
                onPlay(mStartPlaying);  
                if (mStartPlaying) {  
                    setText("Stop playing");  
                } else {  
                    setText("Start playing");  
                }  
                mStartPlaying = !mStartPlaying;  
            }  
        };  
  
        public PlayButton(Context ctx) {  
            super(ctx);  
            setText("Start playing");  
            setOnClickListener(clicker);  
        }  
    }
    
    //构造方法
    public AudioRecorderActivity(){
    	mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
    	mFileName += "/audiorecordtest.3gp";
    }
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//构造界面
		LinearLayout ll = new LinearLayout(this);
		mRecordButton = new RecordButton(this);
		ll.addView(mRecordButton, 
				new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.WRAP_CONTENT, 
						ViewGroup.LayoutParams.WRAP_CONTENT,
						0));
		mPlayButton = new PlayButton(this);
		ll.addView(mPlayButton, 
				new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.WRAP_CONTENT, 
						ViewGroup.LayoutParams.WRAP_CONTENT, 
						0));
		setContentView(ll);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		//Activity暂停时释放录音盒播放对象
		if (mRecorder != null) {
			mRecorder.release();
			mRecorder = null;
		}
		
		if (mPlayer != null) {
			mPlayer.release();
			mPlayer = null;
		}
	}
	
}
