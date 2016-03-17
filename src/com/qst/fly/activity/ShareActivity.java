/** 
 * @Title: ShareActivity.java 
 * @Package com.qst.fly.activity 
 * @Description: TODO 
 * @author yanfeizhao 417470640@qq.com 
 * @date 2016-3-14 下午4:29:05 
 * @version V1.0 
 */
package com.qst.fly.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.qst.fly.R;
import com.qst.fly.adapter.ImageShareListAdapter;
import com.qst.fly.entity.Picture;
import com.qst.fly.entity.SharePicture;
import com.qst.fly.utils.CheckAppExistUtils;
import com.qst.fly.utils.JsonUtils;
import com.qst.fly.utils.OkHttpUtils;
import com.qst.fly.utils.OkHttpUtils.ResultCallback;
import com.qst.fly.widget.WaterfallListView;
import com.qst.fly.widget.WaterfallListView.OnHeaderVisibilityChangeListener;
import com.qst.fly.widget.WaterfallListView.OnLoadDataListener;
import com.qst.fly.widget.view.PLA_AdapterView;
import com.qst.fly.widget.view.PLA_AdapterView.OnItemClickListener;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * @Title: ShareActivity.java
 * @Package com.qst.fly.activity
 * @Description: TODO
 * @author yanfeizhao 417470640@qq.com
 * @date 2016-3-14 下午4:29:05
 * @version V1.0
 */
public class ShareActivity extends BaseActivity
		implements OnClickListener, OnHeaderVisibilityChangeListener, OnLoadDataListener {

	private String mPackageQQ = "com.tencent.mobileqq";
	private String mPackageWeibo = "com.sina.weibo";
	private String mPackageWeixin = "com.tencent.mm";
	private String mPackageQQZone = "com.qzone";

	private ImageView mImgBack;
	private ImageView mImgTakePhoto;
	private ImageView mImgWeChat;
	private ImageView mImgFrinds;
	private ImageView mImgWeibo;
	private ImageView mImgQQ;
	private ImageView mImgQQZone;

	private ImageView mImgWeChatSmall;
	private ImageView mImgFrindsSmall;
	private ImageView mImgWeiboSmall;
	private ImageView mImgQQSmall;
	private ImageView mImgQQZoneSmall;

	private TextView mTextWeChatName;
	private TextView mTextFriendsName;
	private TextView mTextWeiboName;
	private TextView mTextQQName;
	private TextView mTextQQZoneName;

	private TextView mTextTitle;

	public static final String URL_EXAMPLE = "http://api.miaopai.com/m/maopaiImage.json";

	private List<Picture> mListPictures;
	private SharePicture mShareSimple = null;

	private WaterfallListView mMultiColumnListView;
	private ImageShareListAdapter mImageShareListAdapter;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			mImageShareListAdapter.notifyDataSetChanged();
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_share);

		ShareSDK.initSDK(this);

		initData();
		initView();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		mListPictures = new ArrayList<Picture>();
		mImageShareListAdapter = new ImageShareListAdapter(this, mListPictures);

		OkHttpUtils.get(URL_EXAMPLE, new ResultCallback<String>() {
			@Override
			public void onSuccess(String response) {
				String jsonString = (String) response;
				Log.e("jsonString", jsonString);
				mShareSimple = JsonUtils.deserialize(jsonString, SharePicture.class);
				List<String> picpath = mShareSimple.getResult();
				for (int i = 0; i < picpath.size(); i++) {
					mListPictures.add(new Picture(picpath.get(i)));
				}
				mImageShareListAdapter.notifyDataSetChanged();
			}

			@Override
			public void onFailure(Exception e) {
				showToast("数据加载失败！");
			}

		});

	}

	/**
	 * 显示全屏图片
	 */
	private void showFullScreenPicture(View view) {
		View root = getLayoutInflater().inflate(R.layout.activity_share, null);
		ImageView image = (ImageView) view.findViewById(R.id.img_sharelist);
		BitmapDrawable bd = (BitmapDrawable) image.getDrawable();
		Bitmap bitmap = bd.getBitmap();
		
		View contentView = LayoutInflater.from(ShareActivity.this).inflate(R.layout.layout_show_pic_full_screen,
				null);
		ImageView imageView = (ImageView) contentView.findViewById(R.id.img_dis_screen);
		TextView text = (TextView) contentView.findViewById(R.id.text_dis_close);
		imageView.setImageBitmap(bitmap);
		PhotoViewAttacher attacher = new PhotoViewAttacher(imageView);
		attacher.update();
		final PopupWindow pop = new PopupWindow(contentView, WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.MATCH_PARENT, true);
		text.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pop.dismiss();
			}
		});
		pop.setBackgroundDrawable(new ColorDrawable(0xffffff));// 支持点击Back虚拟键退出
		pop.showAtLocation(root, Gravity.NO_GRAVITY, 0, 0);
	}
	
	/**
	 * 初始化控件。
	 */
	private void initView() {
		// TODO Auto-generated method stub

		mMultiColumnListView = (WaterfallListView) findViewById(R.id.lv_act_share);
		View view = View.inflate(this, R.layout.sharelist_header, null);
		mMultiColumnListView.addHeaderView(view);
		mMultiColumnListView.setOnLoadDataListener(this);
		mMultiColumnListView.setOnHeaderVisibilityChangeListener(this);
		mMultiColumnListView.setAdapter(mImageShareListAdapter);
		mMultiColumnListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(PLA_AdapterView<?> parent, View view, int position, long id) {
				showFullScreenPicture(view);
			}

		});
		mImgBack = (ImageView) findViewById(R.id.img_back);
		mImgTakePhoto = (ImageView) findViewById(R.id.img_take_photo);
		mImgWeChat = (ImageView) view.findViewById(R.id.img_wechat);
		mImgFrinds = (ImageView) view.findViewById(R.id.img_friends);
		mImgWeibo = (ImageView) view.findViewById(R.id.img_weibo);
		mImgQQ = (ImageView) view.findViewById(R.id.img_qq);
		mImgQQZone = (ImageView) view.findViewById(R.id.img_qq_zone);
		mTextTitle = (TextView) findViewById(R.id.tv_toptitle);

		mImgWeChatSmall = (ImageView) findViewById(R.id.img_weichat_small);
		mImgFrindsSmall = (ImageView) findViewById(R.id.img_friends_small);
		mImgWeiboSmall = (ImageView) findViewById(R.id.img_weibo_small);
		mImgQQSmall = (ImageView) findViewById(R.id.img_qq_small);
		mImgQQZoneSmall = (ImageView) findViewById(R.id.img_qq_zone_small);

		mTextWeChatName = (TextView) view.findViewById(R.id.text_wechatname);
		mTextFriendsName = (TextView) view.findViewById(R.id.text_friendsname);
		mTextWeiboName = (TextView) view.findViewById(R.id.text_weiboname);
		mTextQQName = (TextView) view.findViewById(R.id.text_qqname);
		mTextQQZoneName = (TextView) view.findViewById(R.id.text_qzonename);

		mImgBack.setOnClickListener(this);
		mImgTakePhoto.setOnClickListener(this);
		mImgWeChat.setOnClickListener(this);
		mImgFrinds.setOnClickListener(this);
		mImgWeibo.setOnClickListener(this);
		mImgQQ.setOnClickListener(this);
		mImgQQZone.setOnClickListener(this);

		mImgWeChatSmall.setOnClickListener(this);
		mImgFrindsSmall.setOnClickListener(this);
		mImgWeiboSmall.setOnClickListener(this);
		mImgQQSmall.setOnClickListener(this);
		mImgQQZoneSmall.setOnClickListener(this);

		bigAppIconVisible();

	}

	private void bigAppIconVisible() {
		if (CheckAppExistUtils.checkAppExist(this, mPackageWeixin)) {
			mImgWeChat.setVisibility(View.VISIBLE);
			mImgFrinds.setVisibility(View.VISIBLE);
			mTextWeChatName.setVisibility(View.VISIBLE);
			mTextFriendsName.setVisibility(View.VISIBLE);

		}
		if (CheckAppExistUtils.checkAppExist(this, mPackageWeibo)) {
			mImgWeibo.setVisibility(View.VISIBLE);
			mTextWeiboName.setVisibility(View.VISIBLE);

		}

		if (CheckAppExistUtils.checkAppExist(this, mPackageQQ)) {
			mImgQQ.setVisibility(View.VISIBLE);
			mTextQQName.setVisibility(View.VISIBLE);

		}
		if (CheckAppExistUtils.checkAppExist(this, mPackageQQZone)) {
			mImgQQZone.setVisibility(View.VISIBLE);
			mTextQQZoneName.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 处理点击事件
	 */
	@Override
	public void onClick(View v) {

		// TODO 点击事件要一一实现
		switch (v.getId()) {
		case R.id.img_back:
			// showToast("返回预览界面");
			finish();
			break;
		case R.id.img_take_photo:
			showToast("回到拍摄界面02");
			// Intent intent=new Intent(this, cls)
			break;
		case R.id.img_wechat:
			showToast("调用第三方的微信");

			break;
		case R.id.img_friends:
			showToast("调用第三方的朋友圈");

			break;
		case R.id.img_weibo:

			showToast("调用第三方的微博");
			share(SinaWeibo.NAME, "分享来自微博", "http://sharesdk.cn");
			break;

		case R.id.img_qq:
			share(QQ.NAME, "QQ分享", "http://sharesdk.cn");
			break;

		case R.id.img_qq_zone:
			share(QZone.NAME, "QQ空间分享", "http://sharesdk.cn");
			break;

		case R.id.img_weichat_small:
		case R.id.img_friends_small:
		case R.id.img_weibo_small:
		case R.id.img_qq_small:
		case R.id.img_qq_zone_small:
			mMultiColumnListView.setSelectionFromTop(0, 0);// position y
			break;
		default:
			break;
		}
	}

	private void share(String platForm, String title, String url) {
		Platform plat = ShareSDK.getPlatform(platForm);
		ShareParams sp1 = new ShareParams();
		// 分享微博只能是text和image
		sp1.setTitleUrl(url);
		sp1.setText(title);
		// 从SD卡中得到图片分享，待分享的本地图片
		sp1.setImagePath("/storage/sdcard/测试分享的图片.jpg");
		plat.setPlatformActionListener(new PlatformActionListener() {
			// 设置分享事件回调
			@Override
			public void onError(Platform arg0, int arg1, Throwable arg2) {
				Toast.makeText(ShareActivity.this, "分享失败", 3000);

			}

			@Override
			public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
				Toast.makeText(ShareActivity.this, "分享成功", 3000);

			}

			@Override
			public void onCancel(Platform arg0, int arg1) {
				Toast.makeText(ShareActivity.this, "分享取消", 3000);

			}
		});
		// 执行图文分享
		plat.share(sp1);
	}

	/**
	 * 加载数据-先从接口中加载数据，然后再从本地加载10张图片。
	 */
	@Override
	public void onLoadDataListener() {
		// TODO 通过接口获取照片数据。

		showToast("加载数据");
		new Thread() {
			public void run() {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				// ImageInfo ii = null;
				// for (int i = 0; i < mImageId.length; i++) {
				// ii = new ImageInfo(mImageId[i]);
				// mListImageInfo.add(ii);
				// }
				// mHandler.sendEmptyMessage(0);
				// mMultiColumnListView.notifyFinishLoading(true);
			}
		}.start();

	}

	/**
	 * 监听瀑布流的头部状态，显示状态是大图标，隐藏状态下：更改标题，显示小图标
	 */
	@SuppressLint("NewApi")
	@Override
	public void OnHeaderVisibilityChange(int headerVisibility) {
		if (headerVisibility == OnHeaderVisibilityChangeListener.HEADER_VISIBLE) {
			// showToast("Header显示了");
			mTextTitle.setVisibility(View.VISIBLE);
			mTextTitle.setText("分享给朋友们");
			smallAppIconGone();
			bigAppIconVisible();
		}

		if (headerVisibility == OnHeaderVisibilityChangeListener.HEADER_INVISIBLE) {
			// showToast("Header隐藏了");
			mTextTitle.setVisibility(View.GONE);
			smallAppIconVisible();

		}

	}

	private void smallAppIconGone() {
		mImgWeChatSmall.setVisibility(View.GONE);
		mImgFrindsSmall.setVisibility(View.GONE);
		mImgWeiboSmall.setVisibility(View.GONE);
		mImgQQSmall.setVisibility(View.GONE);
		mImgQQZoneSmall.setVisibility(View.GONE);
	}

	private void smallAppIconVisible() {
		if (CheckAppExistUtils.checkAppExist(this, mPackageWeixin)) {
			mImgWeChatSmall.setVisibility(View.VISIBLE);
			mImgFrindsSmall.setVisibility(View.VISIBLE);
		}
		if (CheckAppExistUtils.checkAppExist(this, mPackageWeibo)) {
			mImgWeiboSmall.setVisibility(View.VISIBLE);
		}

		if (CheckAppExistUtils.checkAppExist(this, mPackageQQ)) {
			mImgQQSmall.setVisibility(View.VISIBLE);
		}
		if (CheckAppExistUtils.checkAppExist(this, mPackageQQZone)) {
			mImgQQZoneSmall.setVisibility(View.VISIBLE);
		}
	}

}
