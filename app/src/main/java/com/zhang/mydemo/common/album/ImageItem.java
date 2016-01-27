package com.zhang.mydemo.common.album;

import android.R.integer;
import android.os.Parcel;
import android.os.Parcelable;

import com.zhang.mydemo.Constant;
import com.zhang.mydemo.util.Utils;

import java.util.Observable;
import java.util.Observer;

/**
 * 一个图片对象
 * 
 * @author Administrator
 * 
 */
public class ImageItem extends Observable implements Observer, Parcelable {
	public static final int SELECT_TYPE_UNSELECT = 0;		//未选中
	public static final int SELECT_TYPE_SELECT = 1;			//选中
	public static final int SELECT_TYPE_SELECT_GRAY = 2;	//已被选中，不可点击取消

	
	private String imageId;				//本地图片Id
	private String displayName;			//图片文件名
	private String imagePath;			//图片路径
	private String thumbnailPath;		//图片小图路径
	private Integer rotate = 0;			//旋转角度
    private Integer width = 0;			//宽度
    private Integer height = 0;			//高度
    /** 资源类型{@link Constant.DataType}*/
    private Integer dataType = Constant.DataType.TYPE_IMG;
    private Long dateTaken = 0L;		//音视频文件创建日期，列表排序使用
    private Long dataTime = 0L;			//音视频文件时长
    private String dataUrl;				//音视频路径
	private Integer selectType = SELECT_TYPE_UNSELECT;


	public String getImageId() {
		return imageId;
	}
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public String getThumbnailPath() {
		return thumbnailPath;
	}
	public void setThumbnailPath(String thumbnailPath) {
		this.thumbnailPath = thumbnailPath;
	}
	public Integer getRotate() {
		return rotate;
	}
	public void setRotate(Integer rotate) {
		this.rotate = rotate;
	}
	public Integer getWidth() {
		return width;
	}
	public void setWidth(Integer width) {
		this.width = width;
	}
	public Integer getHeight() {
		return height;
	}
	public void setHeight(Integer height) {
		this.height = height;
	}
	public Integer getDataType() {
		return dataType;
	}
	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}
	public Long getDateTaken() {
		return dateTaken;
	}
	public void setDateTaken(Long dateTaken) {
		this.dateTaken = dateTaken;
	}
	public Long getDataTime() {
		return dataTime;
	}
	public void setDataTime(Long dataTime) {
		this.dataTime = dataTime;
	}
	public String getDataUrl() {
		return dataUrl;
	}
	public void setDataUrl(String dataUrl) {
		this.dataUrl = dataUrl;
	}
	public Integer getSelectType() {
		return selectType;
	}
	public void setSelectType(Integer selectType) {
		this.selectType = selectType;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(imageId);
		dest.writeString(displayName);
		dest.writeString(imagePath);
		dest.writeString(thumbnailPath);
		dest.writeInt(Utils.getIntValue(rotate));
		dest.writeInt(Utils.getIntValue(width));
		dest.writeInt(Utils.getIntValue(height));
		dest.writeLong(Utils.getLongValue(dateTaken));
		dest.writeInt(Utils.getIntValue(dataType));
		dest.writeLong(Utils.getLongValue(dataTime));
		dest.writeString(dataUrl);
	}
	
	public static final Creator<ImageItem> CREATOR = new Creator<ImageItem>() {
		@Override
		public ImageItem createFromParcel(Parcel source) {
			ImageItem item = new ImageItem();
			item.setImageId(source.readString());
			item.setDisplayName(source.readString());
			item.setImagePath(source.readString());
			item.setThumbnailPath(source.readString());
			item.setRotate(source.readInt());
			item.setWidth(source.readInt());
			item.setHeight(source.readInt());
			item.setDateTaken(source.readLong());
			item.setDataType(source.readInt());
			item.setDataTime(source.readLong());
			item.setDataUrl(source.readString());
			return item;
		}

		@Override
		public ImageItem[] newArray(int size) {
			return new ImageItem[size];
		}
	};
	
	public void changeChecked(){
		if(selectType==SELECT_TYPE_UNSELECT)
				selectType = SELECT_TYPE_SELECT;
		else selectType=SELECT_TYPE_UNSELECT;
		setChanged();
		notifyObservers(selectType);
	}

	@Override
	public void update(Observable observable, Object data) {
		if (data instanceof integer) {
			this.selectType = (int) data;
		}
	}


}
