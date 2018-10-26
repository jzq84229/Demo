package com.zhang.mydemo.common.album;


import com.zhang.mydemo.util.StringUtil;

import java.util.ArrayList;

/**
 * 一个目录的相册对象
 * 
 * @author Administrator
 * 
 */
public class ImageBucket implements Comparable<ImageBucket>{
	private int count = 0;
	private String bucketId;
	private String bucketName = "";
	private ArrayList<ImageItem> imageList = new ArrayList<ImageItem>();
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getBucketId() {
		return bucketId;
	}
	public void setBucketId(String bucketId) {
		this.bucketId = bucketId;
	}
	public String getBucketName() {
		return bucketName;
	}
	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}
	public ArrayList<ImageItem> getImageList() {
		return imageList;
	}
	
	public void addCount(){
		count++;
	}
	
	@Override
	public String toString() {
		return bucketName;
	}

	@Override
	public int compareTo(ImageBucket another) {
		return StringUtil.compare(this.getBucketName(), another.getBucketName());
	}
}
