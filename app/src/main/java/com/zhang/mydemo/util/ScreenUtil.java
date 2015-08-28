package com.zhang.mydemo.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;

public class ScreenUtil {

	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	
	public static int screenWidthPixels(Context context){
		return context.getResources().getDisplayMetrics().widthPixels;
	}
	
	public static int screenHeightPixels(Context context){
		return context.getResources().getDisplayMetrics().heightPixels;
	}
	
    /** 
     * 获取状态栏高度
     * @param activity 
     * @return > 0 success; <= 0 fail 
     */  
    public static int getStatusHeight(Activity activity){  
        int statusHeight = 0;  
        Rect localRect = new Rect();  
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);  
        statusHeight = localRect.top;  
        if (0 == statusHeight){  
            Class<?> localClass;  
            try {  
                localClass = Class.forName("com.android.internal.R$dimen");  
                Object localObject = localClass.newInstance();  
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());  
                statusHeight = activity.getResources().getDimensionPixelSize(i5);  
            } catch (ClassNotFoundException e) {  
                e.printStackTrace();  
            } catch (IllegalAccessException e) {  
                e.printStackTrace();  
            } catch (InstantiationException e) {  
                e.printStackTrace();  
            } catch (NumberFormatException e) {  
                e.printStackTrace();  
            } catch (IllegalArgumentException e) {  
                e.printStackTrace();  
            } catch (SecurityException e) {  
                e.printStackTrace();  
            } catch (NoSuchFieldException e) {  
                e.printStackTrace();  
            }  
        }  
        return statusHeight;  
    }

    /**
     * 获取ActionBar高度
     * @param context
     * @return
     */
    public static int getActionBarHeight(Context context){
        int mActionBarSize = 0;
//        TypedValue tv = new TypedValue();
//        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
//            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
//        }
//        return actionBarHeight;

        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[] { android.R.attr.actionBarSize });
        mActionBarSize = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        return mActionBarSize;
    }

    /**
     * 获取actionbar的像素高度，默认使用android官方兼容包做actionbar兼容
     *
     * @return
     */
//    public static int getActionBarHeight(AppCompatActivity activity) {
//        int actionBarHeight = activity.getSupportActionBar().getHeight();
//        if (actionBarHeight != 0) {
//            return actionBarHeight;
//        }
//
//        final TypedValue tv = new TypedValue();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            if (activity.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
//                actionBarHeight = TypedValue.complexToDimensionPixelSize(
//                        tv.data, activity.getResources().getDisplayMetrics());
//            }
//        }
//        else {
//            // 使用android.support.v7.appcompat包做actionbar兼容的情况
//            if (activity.getTheme().resolveAttribute(
//                    android.support.v7.appcompat.R.attr.actionBarSize,
//                    tv, true)) {
//                actionBarHeight = TypedValue.complexToDimensionPixelSize(
//                        tv.data, activity.getResources().getDisplayMetrics());
//            }
//
//        }
//        // else if
//        // (getTheme().resolveAttribute(com.actionbarsherlock.R.attr.actionBarSize,
//        // tv, true))
//        // {
//        // //使用actionbarsherlock的actionbar做兼容的情况
//        // actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,
//        // getResources().getDisplayMetrics());
//        // }
//
//        return actionBarHeight;
//    }

}
