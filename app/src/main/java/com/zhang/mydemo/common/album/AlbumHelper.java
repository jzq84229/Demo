package com.zhang.mydemo.common.album;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Thumbnails;
import android.provider.MediaStore.Video;

import com.zhang.mydemo.Constant;
import com.zhang.mydemo.util.DebugUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** 
 * @Description: 获取图片库所有图片
 * @author zhangjun
 * @date 2014年10月9日 上午2:54:09 
 * @version V1.0 
 */
public class AlbumHelper {
	private static final String TAG = AlbumHelper.class.getSimpleName();
	private static final String DEFAULT_BUCKET_NAME = "Camera";		//默认相册名称
//	public static final String DEFAULT_BUCKET_ID = "0";				//默认相册ID
	
	private static AlbumHelper instance;
	private Context context;
	private ContentResolver cr;
	private ImageItemComparator comparator = new ImageItemComparator();
	
	// 默认相册(所有图片)
//	private ImageBucket defaultBucket;
	// 缩略图列表
	private HashMap<String, String> thumbnailMap = new HashMap<String, String>();
	// 专辑列表
	private Map<String, ImageBucket> bucketMap = new LinkedHashMap<String, ImageBucket>();
	
	private AlbumHelper(){}
	
	public static AlbumHelper getHelper(){
		if (instance == null) {
			instance = new AlbumHelper();
		}
		return instance;
	}
	
	/**
	 * 初始化
	 * 
	 * @param context
	 */
	public void init(Context context) {
		if (this.context == null) {
			this.context = context;
			cr = context.getContentResolver();
		}
	}
	
	/**
	 * 是否创建了图片集
	 */
	private boolean hasBuildImagesBucketList = false;
	
	/**
	 * 获取图片文件夹集
	 * @param refresh	是否重新查询
	 * @param withVideo 是否显示视频
	 * @return
	 */
	public List<ImageBucket> getImagesBucketList(boolean refresh, boolean withVideo){
		if (refresh || (!refresh && !hasBuildImagesBucketList)) {
			buildImageBucketList(withVideo);
		}
		List<ImageBucket> tempList = new ArrayList<ImageBucket>();
		for (ImageBucket imageBucket : bucketMap.values()) {
			if (DEFAULT_BUCKET_NAME.equals(imageBucket.getBucketName())) {
				tempList.add(0, imageBucket);
			} else {
				tempList.add(imageBucket);
			}
		}
		return tempList;
	}
	
	/**
	 * 获取图片文件集
	 */
	private void buildImageBucketList(boolean withVideo){
		long startTime = System.currentTimeMillis();
		
		thumbnailMap.clear();
		bucketMap.clear();
		
//		addDefaultBucket();
//		 //构造缩略图索引
//		getThumbnail();
		if (withVideo) {
			getVideos();
		}
		getOriginal();
		sortImageList();
		hasBuildImagesBucketList = true;
		
		long endTime = System.currentTimeMillis();
		DebugUtils.d(TAG, "use time: " + (endTime - startTime) + " ms");
	}
	
	/**
	 * 将相册中的图片和视频按创建时间排序
	 */
	private void sortImageList(){
		for (ImageBucket imageBucket : bucketMap.values()) {
			if (!imageBucket.getImageList().isEmpty()) {
				Collections.sort(imageBucket.getImageList(), comparator);
			}
		}
	}
	
	/**
	 * 添加默认相册
	 */
//	private void addDefaultBucket(){
//		defaultBucket = new ImageBucket();
//		defaultBucket.setBucketName(DEFAULT_BUCKET_NAME);
//		defaultBucket.setBucketId(DEFAULT_BUCKET_ID);
//		bucketMap.put(DEFAULT_BUCKET_ID, defaultBucket);
//	}
	
//	/**
//	 * 添加默认图片
//	 * @param bucket
//	 */
//	private void addDefaultPhoto(ImageBucket bucket){
//		if (bucket != null) {
//			ImageItem imgItem = new ImageItem();
//			imgItem.setImageId("0");
//			bucket.getImageList().add(imgItem);
//		}
//	}
	
	/**
	 * 得到略缩图
	 */
	private void getThumbnail(){
		String[] projection = {Thumbnails._ID, Thumbnails.IMAGE_ID, Thumbnails.DATA};
		Cursor cursor = cr.query(Thumbnails.EXTERNAL_CONTENT_URI, projection, null, null, null);
		getThumbnailColumnData(cursor);
		if (cursor != null) {
			cursor.close();
		}
	}
	
	/**
	 * 从数据库中得到略缩图
	 * @param cur
	 */
	private void getThumbnailColumnData(Cursor cur){
		if (cur != null && cur.moveToFirst()) {
			String _id;
			String image_id;
			String image_path;
			int _idIndex = cur.getColumnIndex(Thumbnails._ID);
			int image_idIndex = cur.getColumnIndex(Thumbnails.IMAGE_ID);
			int dataIndex = cur.getColumnIndex(Thumbnails.DATA);
			do {
				_id = cur.getString(_idIndex);
				image_id = cur.getString(image_idIndex);
				image_path = cur.getString(dataIndex);
				
				thumbnailMap.put(image_id, image_path);
			} while (cur.moveToNext());
		}
	}
	
	// 构造相册索引
	private static final String[] IMAGE_PROJECTION = new String[] {
					Images.Media._ID, 
					Images.Media.DATA, 
					Images.Media.DATE_TAKEN,
					Images.Media.DISPLAY_NAME, 
					Images.Media.TITLE,
					Images.Media.SIZE, 
					Images.Media.BUCKET_ID,
					Images.Media.BUCKET_DISPLAY_NAME,
					Images.Media.DATE_MODIFIED};
	
	/**
	 * 得到原图
	 */
	private void getOriginal(){
		// 得到一个游标
		Cursor cur = cr.query(Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION, 
				Images.Media.MIME_TYPE + " in ('image/png', 'image/jpeg') and " + Images.Media.SIZE + " != ? ", 
				new String[]{"0"}, 
				Images.Media._ID + " desc");
		
		getOriginalColumnData(cur, Constant.DataType.TYPE_IMG);
		if (cur != null) {
			cur.close();
		}
	}
	
	/**
	 * 从数据库中得到原图
	 */
	private void getOriginalColumnData(Cursor cur, int dataType){
		if (cur != null && cur.moveToFirst()) {
			// 获取指定列的索引
			int _idIndex = cur.getColumnIndexOrThrow(Images.Media._ID);
			int dataIndex = cur.getColumnIndexOrThrow(Images.Media.DATA);
			int bucketIdIndex = cur.getColumnIndexOrThrow(Images.Media.BUCKET_ID);
			int bucketNameIndex = cur.getColumnIndexOrThrow(Images.Media.BUCKET_DISPLAY_NAME);
			int dateTakenIndex = cur.getColumnIndexOrThrow(Images.Media.DATE_TAKEN);
//			// 获取图片总数
//			int totalNum = cur.getCount();
			
			buildBucket(cur, dataType, _idIndex, dataIndex, bucketNameIndex, bucketIdIndex, dateTakenIndex, Constant.ERROR_INT);
		}
	}
	
	//构造视频索引
	private static final String[] VIDEO_PROJECTION = new String[] {
					Video.Media._ID,
					Video.Media.DATA,
					Video.Media.DATE_TAKEN,
					Video.Media.DISPLAY_NAME, 
					Video.Media.TITLE,
					Video.Media.SIZE, 
					Video.Media.BUCKET_ID,
					Video.Media.BUCKET_DISPLAY_NAME,
					Video.Media.DATE_MODIFIED,
					Video.Media.DURATION};
	
	/**
	 * 得到视频
	 */
	private void getVideos(){
		Cursor cur = cr.query(Video.Media.EXTERNAL_CONTENT_URI, 
				VIDEO_PROJECTION, 
				Video.Media.MIME_TYPE + " in ('video/mp4')", 
				null, 
				Video.Media._ID + " desc");
		getVideoColumnData(cur, Constant.DataType.TYPE_VIDEO);
		if (cur != null) {
			cur.close();
		}
	}
	
	private void getVideoColumnData(Cursor cur, int dataType){
		if (cur != null && cur.moveToFirst()) {
			int _idIndex = cur.getColumnIndexOrThrow(Video.Media._ID);
			int dataIndex = cur.getColumnIndexOrThrow(Video.Media.DATA);
			int bucketIdIndex = cur.getColumnIndexOrThrow(Video.Media.BUCKET_ID);
			int bucketNameIndex = cur.getColumnIndexOrThrow(Video.Media.BUCKET_DISPLAY_NAME);
			int dateTakenIndex = cur.getColumnIndexOrThrow(Video.Media.DATE_TAKEN);
			int durationIndex = cur.getColumnIndexOrThrow(Video.Media.DURATION);
			
			buildBucket(cur, dataType, _idIndex, dataIndex, bucketNameIndex, bucketIdIndex, dateTakenIndex, durationIndex);
		}
	}
	
	private void buildBucket(Cursor cur, int dataType, int _idIndex, int dataIndex, int bucketNameIndex, int bucketIdIndex, int dateTakenIndex, int durationIndex){
		do {
			String _id = cur.getString(_idIndex);
			String path = cur.getString(dataIndex);
			String bucketName = cur.getString(bucketNameIndex);
			String bucketId = cur.getString(bucketIdIndex);
			long dateTaken = cur.getLong(dateTakenIndex);
			long videoDuration = 0;
			if (durationIndex >= 0) {
				videoDuration = cur.getLong(durationIndex);
			}
			
			ImageBucket bucket = bucketMap.get(bucketId);
			if (bucket == null) {
				bucket = new ImageBucket();
				bucket.setBucketId(bucketId);
				bucket.setBucketName(bucketName);
				bucketMap.put(bucketId, bucket);
			}
			bucket.addCount();
			ImageItem imageItem = new ImageItem();
			imageItem.setImageId(_id);
			imageItem.setImagePath(path);
			imageItem.setThumbnailPath(thumbnailMap.get(_id));
			imageItem.setDateTaken(dateTaken);
			imageItem.setDataType(dataType);
			imageItem.setDataTime(videoDuration / 1000);
			imageItem.setDataUrl(path);
			bucket.getImageList().add(imageItem);
		} while (cur.moveToNext());
	}

}
