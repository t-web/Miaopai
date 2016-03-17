package com.qst.fly.fragment;

import com.qst.fly.R;
import com.qst.fly.activity.AlbumActivity;
import com.qst.fly.entity.PhotoUpImageItem;
import com.qst.fly.widget.CropImageView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

/**
* @author smallzoo
* @version
* @date 2016年3月16日 上午10:44:14
* 类说明
*/
public class CropFragment extends Fragment implements OnClickListener{

	private View contentView;
	private static final String TAG = "CropPhotoActivity";
	public static final String CROPED_PHOTO = "CROPED_PHOTO";
	private CropImageView mCropImage;
	private Button mBtnConfirm;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		contentView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_crop_photo, null);
		
		initView();
		getBitmap();
		return contentView;
	}
	
	public void initView() {
		mCropImage = (CropImageView)contentView.findViewById(R.id.img_tobe_crop);
		mBtnConfirm = (Button)contentView.findViewById(R.id.btn_confirm);
		mBtnConfirm.setOnClickListener(this);
	}

	public void getBitmap() {
		PhotoUpImageItem mSelectPhoto = ((AlbumActivity)getActivity()).getPhotoUpImageItem();
		BitmapDrawable drawable = new BitmapDrawable(BitmapFactory.decodeFile(mSelectPhoto.getImagePath()));
		mCropImage.setDrawable(drawable, 300, 300);
	}
	
	@Override
	public void onClick(View v) {
		//获取到裁剪后的图片
		Bitmap bitmap = mCropImage.getCropImage();
		((AlbumActivity)getActivity()).finishSelect(bitmap);
	}
}
