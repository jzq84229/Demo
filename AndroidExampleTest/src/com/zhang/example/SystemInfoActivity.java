package com.zhang.example;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

public class SystemInfoActivity extends Activity {
	private TextView mTextView;  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_system_info);  
        init();  
    }  
   private void init(){  
       mTextView=(TextView) findViewById(R.id.textView);  
       mTextView.setText(getHandSetInfo());  
   }  
  private String getHandSetInfo(){  
	  String phoneInfo = "BOARD: " + android.os.Build.BOARD;
	  phoneInfo += ", BOOTLOADER: " + android.os.Build.BOOTLOADER;
	  //BRAND 运营商
	  phoneInfo += ", BRAND: " + android.os.Build.BRAND;
	  phoneInfo += ", CPU_ABI: " + android.os.Build.CPU_ABI;
	  phoneInfo += ", CPU_ABI2: " + android.os.Build.CPU_ABI2;
	  //DEVICE 驱动
	  phoneInfo += ", DEVICE: " + android.os.Build.DEVICE;
	  //DISPLAY Rom的名字 例如 Flyme 1.1.2（魅族rom）  JWR66V（Android nexus系列原生4.3rom）
	  phoneInfo += ", DISPLAY: " + android.os.Build.DISPLAY;
	  //指纹
	  phoneInfo += ", FINGERPRINT: " + android.os.Build.FINGERPRINT;
	  //HARDWARE 硬件
	  phoneInfo += ", HARDWARE: " + android.os.Build.HARDWARE;
	  phoneInfo += ", HOST: " + android.os.Build.HOST;
	  phoneInfo += ", ID: " + android.os.Build.ID;
	  //MANUFACTURER 生产厂家
	  phoneInfo += ", MANUFACTURER: " + android.os.Build.MANUFACTURER;
	  //MODEL 机型
	  phoneInfo += ", MODEL: " + android.os.Build.MODEL;
	  phoneInfo += ", PRODUCT: " + android.os.Build.PRODUCT;
	  phoneInfo += ", RADIO: " + android.os.Build.RADIO;
	  phoneInfo += ", RADITAGSO: " + android.os.Build.TAGS;
	  phoneInfo += ", TIME: " + android.os.Build.TIME;
	  phoneInfo += ", TYPE: " + android.os.Build.TYPE;
	  phoneInfo += ", USER: " + android.os.Build.USER;
	  //VERSION.RELEASE 固件版本
	  phoneInfo += ", VERSION.RELEASE: " + android.os.Build.VERSION.RELEASE;
	  phoneInfo += ", VERSION.CODENAME: " + android.os.Build.VERSION.CODENAME;
	  //VERSION.INCREMENTAL 基带版本
	  phoneInfo += ", VERSION.INCREMENTAL: " + android.os.Build.VERSION.INCREMENTAL;
	  //VERSION.SDK SDK版本
	  phoneInfo += ", VERSION.SDK: " + android.os.Build.VERSION.SDK;
	  phoneInfo += ", VERSION.SDK_INT: " + android.os.Build.VERSION.SDK_INT;
      return phoneInfo; 
  }  
  
  //获取当前版本号  
  private  String getAppVersionName(Context context) {  
        String versionName = "";  
        try {  
            PackageManager packageManager = context.getPackageManager();  
            PackageInfo packageInfo = packageManager.getPackageInfo("cn.testgethandsetinfo", 0);  
            versionName = packageInfo.versionName;  
            if (TextUtils.isEmpty(versionName)) {  
                return "";  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return versionName;  
    }  
  

}
