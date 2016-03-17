package com.qst.fly.fragment;

import java.util.ArrayList;
import java.util.List;

import com.qst.fly.R;
import com.qst.fly.activity.AlbumActivity;
import com.qst.fly.adapter.CameraAlbumAdapter;
import com.qst.fly.entity.PhotoUpImageBucket;
import com.qst.fly.utils.PhotoUpAlbumHelper;
import com.qst.fly.utils.PhotoUpAlbumHelper.GetAlbumList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

/**
* @author smallzoo
* @version
* @date 2016年3月16日 上午10:43:34
* 类说明
*/
public class AlbumFragment extends Fragment {
	public static final String IMAGE_LIST = "imagelist";
	protected static final int REQUEST_PHOTO = 6;

	private View mConentView;
	private ListView mListView;
	private CameraAlbumAdapter adapter;
	private PhotoUpAlbumHelper photoUpAlbumHelper;/** 加载相册和图片的异步线程类 */
	private List<PhotoUpImageBucket> mList ;/** 存放相册列表数据 */
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mConentView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_album, null);
		
		init();
		loadData();
		onItemClick();
		return mConentView;
	}
	
	

	private void init() {
		mListView = (ListView) mConentView.findViewById(R.id.listview_album);
		mList = new ArrayList<PhotoUpImageBucket>();
		adapter = new CameraAlbumAdapter(getActivity(), mList,
				R.layout.album_item);
		mListView.setAdapter(adapter);
	}
	
	/**
	 * 获得数据
	 */
	private void loadData() {
		photoUpAlbumHelper = PhotoUpAlbumHelper.getHelper();
		photoUpAlbumHelper.init(getActivity());
		photoUpAlbumHelper.setGetAlbumList(new GetAlbumList() {
			@Override
			public void getAlbumList(List<PhotoUpImageBucket> addList) {
				mList.addAll(addList);
				adapter.notifyDataSetChanged();
			}
		});
		photoUpAlbumHelper.execute(false);
	}

	private void onItemClick() {
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				((AlbumActivity)getActivity()).setPhotoUpImageBucket(mList.get(position));
				((AlbumActivity)getActivity()).jump2Fragment(AlbumActivity.ALBUM_DETAIL_FRAGMENT_POSITION);
			}
		});
	}
	

}
