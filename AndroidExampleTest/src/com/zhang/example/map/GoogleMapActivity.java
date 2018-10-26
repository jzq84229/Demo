package com.zhang.example.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.zhang.example.R;

import java.io.IOException;
import java.util.List;

/*
 * 权限
 * <uses-permission android:name="android.permission.INTERNET"/>
 * <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
 * <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
 */
public class GoogleMapActivity extends MapActivity {
	private MapController mapController;
	private GeoPoint geoPoint;
	private TextView textView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //全屏    
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,    
                      WindowManager.LayoutParams.FLAG_FULLSCREEN);  
        setContentView(R.layout.activity_map);
        
        textView = (TextView) findViewById(R.id.textView);
        MapView mapView = (MapView) findViewById(R.id.mapView); //从xml布局文件获得MapView对象
        mapView.setClickable(true); //允许通过触摸拖动地图
        mapView.setBuiltInZoomControls(true); //当触摸地图时在地图下方会出现缩放按钮，几秒后就会消失
        mapController = mapView.getController(); //获得MapController对象，mapController是一个在类中定义的MapController类型变量
        Geocoder gc = new Geocoder(this); //创建Geocoder对象，用于获得指定地点的地址
        mapView.setTraffic(true); //将地图设为Traffic模式
        try {
			//查询指定地点的地址
//        	List<Address> addresses = gc.getFromLocationName("苏州", 5);
//        	System.out.println("address list = " + addresses.size());
//        	System.out.println("address list:" + addresses);
        	//根据经纬度创建GeoPoint对象
        	geoPoint = new GeoPoint(
        			(int)(31.2988860 * 1E6), 
        			(int)(120.5853160 * 1E6));
        	setTitle("苏州");
//        	setTitle(addresses.get(0).getFeatureName());
		} catch (Exception e) {
			e.printStackTrace();
		}
        MyOverlay myOverlay = new MyOverlay(); //创建MyOverlay对象，用于在地图上绘制图形
        mapView.getOverlays().add(myOverlay); //将MyOverlay对象添加到MapView控件中
        mapController.setZoom(17); //设置地图的初始大小，范围在1和21之间。1：最小尺寸，21：最大尺寸
        mapController.animateTo(geoPoint); //以动画方式进行定位
        
//        getLocalPosition();
    }
    
    private void getLocalPosition(){
    	try {
	    	LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    	
	    	Criteria criteria = new Criteria();
	    	
	    	criteria.setAccuracy(Criteria.ACCURACY_FINE); //获得最好的定位效果(设置查询精度)
	    	criteria.setSpeedRequired(false); //设置是否要求速度 
	    	criteria.setCostAllowed(false); //设置是否允许产生费用
	    	criteria.setBearingRequired(false); //设置是否需要得到方向
	    	criteria.setAltitudeRequired(false); //设置是否需要得到海拔高度
	    	criteria.setPowerRequirement(Criteria.POWER_LOW); //设置允许的电池消耗级别
	    	
	    	String provider = locationManager.getBestProvider(criteria, true); //获得当前的位置提供者
	    	Location location = locationManager.getLastKnownLocation(provider); //获得当前位置
	    	Double latitude = location.getLatitude() * 1E6; // 获得当前位置的纬度 
	    	Double longitude = location.getLongitude() * 1E6; // 获得当前位置的经度
	    	
	    	String msg = "";
	    	Geocoder gc = new Geocoder(this);
	    	List<Address> addresses;
				addresses = gc.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
	    	if (addresses.size() > 0) {
				msg += "AddressLine: " + addresses.get(0).getAddressLine(0) + "\n";
				msg += "CountryName: " + addresses.get(0).getCountryName() + "\n";
				msg += "Locality: " + addresses.get(0).getLocality() + "\n";
				msg += "FeatureName: " + addresses.get(0).getFeatureName();
			}
	    	textView.setText(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	return super.onCreateOptionsMenu(menu);
    }

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	class MyOverlay extends Overlay
	{
		@Override
		public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
				long when) {
			Paint paint = new Paint();
			paint.setColor(Color.RED);
			Point screenPoint = new Point();
			//将"沈阳三好街"在地图上的位置转换成屏幕的实际坐标
			mapView.getProjection().toPixels(geoPoint, screenPoint);
			Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.iconmarka);
			//在地图上绘制图像
			canvas.drawBitmap(bmp, screenPoint.x, screenPoint.y, paint);
			//在地图上绘制文字
			canvas.drawText("苏州", screenPoint.x, screenPoint.y, paint);
			return super.draw(canvas, mapView, shadow, when);
		}
	}

}
