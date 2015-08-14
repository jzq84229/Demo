package com.zhang.mydemo.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhang.mydemo.R;


/** 
 * @Description: TODO(添加描述) 
 * @author zhangjun
 * @date 2014年11月18日 上午2:08:54 
 * @version V1.0 
 */
public class ToastUtil {
	
    /** 
     * 普通文本消息提示 
     * @param context 
     * @param text 
     * @param duration 
     */  
    public static void showToast(Context context, CharSequence text, int duration){  
        //创建一个Toast提示消息
//        //设置Toast提示消息在屏幕上的位置
//        toast.setGravity(Gravity.CENTER, 0, 0);
    	Toast.makeText(context, text, duration).show();
    }
    
    public static void showToast(Context context, int id, int duration){
    	Toast.makeText(context, id, duration).show();
    }
      
//    /**
//     * 带图片消息提示
//     * @param context
//     * @param ImageResourceId
//     * @param text
//     * @param duration
//     */
//    public static void ImageToast(Context context, int imageResourceId, CharSequence text, int duration){
////        //创建一个Toast提示消息
////        toast = Toast.makeText(context, text, duration);
////        //设置Toast提示消息在屏幕上的位置
////        toast.setGravity(Gravity.CENTER, 0, 0);
////        //获取Toast提示消息里原有的View
////        LinearLayout toastView = (LinearLayout) toast.getView();
////        toastView.setOrientation(LinearLayout.HORIZONTAL);
////        LinearLayout.LayoutParams layoutParams = new LayoutParams(ScreenUtil.dip2px(context, 22), ScreenUtil.dip2px(context, 22));
////        layoutParams.setMargins(0, 0, ScreenUtil.dip2px(context, 15), 0);
////        //创建一个ImageView
////        ImageView img = new ImageView(context);
////        img.setImageResource(ImageResourceId);
////        img.setLayoutParams(layoutParams);
////        //向LinearLayout中添加ImageView和Toast原有的View
////        toastView.addView(img, 0);
////        //显示消息
////        toast.show();
//
//		LayoutInflater inflater = LayoutInflater.from(context);
//		View view = inflater.inflate(R.layout.toast_layout, null);
//		ImageView imageView = (ImageView) view.findViewById(R.id.image_view);
//		TextView textView = (TextView) view.findViewById(R.id.text_view);
//		imageView.setImageResource(imageResourceId);
//		textView.setText(text);
//
//		Toast toast = new Toast(context);
//		//设置Toast提示消息在屏幕上的位置
//		toast.setGravity(Gravity.CENTER, 0, 0);
//		//自定义
//		toast.setDuration(duration);
//		toast.setView(view);
//		toast.show();
//    }
}  
