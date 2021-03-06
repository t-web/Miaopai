package com.qst.fly.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap.Config;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.qst.fly.R;
import com.qst.fly.entity.PhotoUpImageItem;

public class CameraAlbumDetailAdapter extends CommonAdapter<PhotoUpImageItem> {

	private List<PhotoUpImageItem> list;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;

	
	public CameraAlbumDetailAdapter(Context context,
			List<PhotoUpImageItem> mDatas, int itemLayoutId) {
		super(context, mDatas, itemLayoutId);

		// 实例化图片加载器
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.default_picture)
				.showImageForEmptyUri(R.drawable.default_picture)
				.showImageOnFail(R.drawable.default_picture)
				.cacheInMemory(true)
				.cacheOnDisc(true)
				.bitmapConfig(Config.ARGB_8888)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build(); 
	}

	@Override
	public void convert(ViewHolder helper, PhotoUpImageItem item, int position) {
		ImageView ImageView = helper.getView(R.id.gridview_item);
        imageLoader.displayImage("file://"+item.getImagePath(), ImageView, options);
        
	}

}
