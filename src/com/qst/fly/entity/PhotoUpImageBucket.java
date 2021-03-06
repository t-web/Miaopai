package com.qst.fly.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: PhotoUpImageBucket
 * @Description: 相册的实体类
 * @author wzg0816 466959515@qq.com
 * @date 2016-3-15 上午12:05:49
 */

public class PhotoUpImageBucket implements Serializable {

	/** 相册照片的数量 */
	public int count = 0;
	/** 相册名字 */
	public String bucketName;
	/** 每个相册里面相片集合 */
	public List<PhotoUpImageItem> imageList;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public List<PhotoUpImageItem> getImageList() {
		return imageList;
	}

	public void setImageList(List<PhotoUpImageItem> imageList) {
		this.imageList = imageList;
	}

}
