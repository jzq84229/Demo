package com.zhang.example;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

/** 
 * 视屏录制 
 * @author Administrator 
 * 
 */  
public class VedioRecorderActivity extends Activity {
	private File myRecAudioFile;
	private SurfaceView mSurfaceView;
	private SurfaceHolder mSurfaceHolder;
	private Button buttonStart;
	private Button buttonStop;
	private File dir;
	private MediaRecorder recorder;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏  
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏  
        // 设置横屏显示  
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);  
        // 选择支持半透明模式,在有surfaceview的activity中使用。  
        getWindow().setFormat(PixelFormat.TRANSLUCENT);  
        
		setContentView(R.layout.activity_vedio);
		
		mSurfaceView = (SurfaceView) findViewById(R.id.videoView);
		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		buttonStart = (Button) findViewById(R.id.start);
		buttonStop = (Button) findViewById(R.id.stop);
		File defaultDir = Environment.getExternalStorageDirectory();
		String path = defaultDir.getAbsolutePath() + File.separator + "V" + File.separator; //创建文件夹存放视屏
		dir = new File(path);
		
		if (!dir.exists()) {
			dir.mkdir();
		}
		recorder = new MediaRecorder();
		buttonStart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				recorder();
			}
		});
		
		buttonStop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				recorder.stop();
				recorder.reset();
				recorder.release();
				recorder = null;
				Toast.makeText(VedioRecorderActivity.this, "停止录制", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	public void recorder(){
		try {
			myRecAudioFile = File.createTempFile("video", ".3gp", dir); //创建临时文件
			recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);// 设置录制视频源为Camera(相机) 
			recorder.setAudioSource(MediaRecorder.AudioSource.MIC);// 录音源为麦克风
			recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);// 设置录制完成后视频的封装格式THREE_GPP为3gp.MPEG_4为mp4
			recorder.setVideoEncoder(MediaRecorder.VideoEncoder.H263);// 设置录制的视频编码h263 h264
			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);// 音频编码
			recorder.setVideoSize(800, 480);// 设置视频尺寸。必须放在设置编码和格式的后面，否则报错
			recorder.setVideoFrameRate(15);// 设置录制的视频帧率。必须放在设置编码和格式的后面，否则报错
			recorder.setMaxDuration(10000);// 最大期限
			recorder.setPreviewDisplay(mSurfaceHolder.getSurface());// 预览
			recorder.setOutputFile(myRecAudioFile.getAbsolutePath());// 保存路径
			recorder.prepare(); // 准备录制
			recorder.start(); // 开始录制  
			Toast.makeText(this, "开始录制视频", Toast.LENGTH_SHORT).show();
			
//			recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);// 视频源
//		    recorder.setAudioSource(MediaRecorder.AudioSource.MIC); // 录音源为麦克风
//		    recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);// 输出格式为mp4
//		    recorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);// 视频编码
//		    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);// 音频编码
//		    recorder.setVideoFrameRate(15);// 视频帧频率
//		    recorder.setVideoSize(800, 480);// 视频尺寸
//		    recorder.setPreviewDisplay(mSurfaceHolder.getSurface());// 预览
//		    // recorder.setMaxDuration(10000);// 最大期限
//		    recorder.setOutputFile(myRecAudioFile.getAbsolutePath());// 保存路径
//		    recorder.prepare();
//		    recorder.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
