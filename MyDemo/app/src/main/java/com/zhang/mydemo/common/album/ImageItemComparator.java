package com.zhang.mydemo.common.album;

import java.util.Comparator;

/** 
 * @Description: TODO
 */
public class ImageItemComparator implements Comparator<ImageItem> {

	@Override
	public int compare(ImageItem lhs, ImageItem rhs) {
		return rhs.getDateTaken().compareTo(lhs.getDateTaken());
//		return rhs.getDateTaken() < lhs.getDateTaken() ? -1 : (rhs.getDateTaken() == lhs.getDateTaken() ? 0 : 1);
	}

}
