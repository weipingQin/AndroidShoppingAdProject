package com.www.goumei.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.www.goumei.R;
import com.www.goumei.bean.UserData;
import com.www.goumei.bean.UserDataB;
import com.www.goumei.http.req.ApiClientC;
import com.www.goumei.http.req.SP_ID;
import com.www.goumei.utils.Constant;
import com.www.goumei.utils.StringUtils;
import com.www.goumei.views.CircleImageView;
import com.yixia.camera.demo.utils.ToastUtils;

/**
 * 个人资料
 * 
 * @author Eric.Chen
 * 
 */
public class OtherPersonaldataActivity extends Activity implements
		OnClickListener {

	private CircleImageView icon;
	private TextView last;
	private TextView edit;
	private TextView username;
	private TextView userId;
	private TextView sex;
	private TextView address;
	private TextView birthday;
	private ImageView shareIV;
	private ImageView iv_verified;
	
	private UserData user;
	public ImageLoader imageLoader;
	private static DisplayImageOptions options;
	private View view;
	Activity pActivity;
	String id="";

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_personaldata);
		view = LayoutInflater.from(this).inflate(
				R.layout.activity_personaldata, null);
		id=getIntent().getStringExtra(Constant.OTHER_ID);
		this.pActivity = this;
		last = (TextView) findViewById(R.id.personaldata_last);
		edit = (TextView) findViewById(R.id.dataEdit);
		edit.setVisibility(View.INVISIBLE);
		icon = (CircleImageView) findViewById(R.id.CircleImageView);
		username = (TextView) findViewById(R.id.username);
		iv_verified=(ImageView)findViewById(R.id.iv_verified);
		userId = (TextView) findViewById(R.id.userID);
		sex = (TextView) findViewById(R.id.sex);
		address = (TextView) findViewById(R.id.address);
		birthday = (TextView) findViewById(R.id.birthday);
		shareIV = (ImageView) findViewById(R.id.ivShare);
		shareIV.setOnClickListener(this);
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(this));

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.test)
				.showImageForEmptyUri(R.drawable.test)
				.showImageOnFail(R.drawable.test).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		setListeners();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		new UserInfoTask().execute("");
	}

	protected void setListeners() {
		// TODO Auto-generated method stub
		last.setOnClickListener(this);
		edit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.personaldata_last:
			finish();
			break;
		// 编辑
		// case R.id.dataEdit:
		// if(user!=null){
		// UpdatePersonaldataFragment updateFragment=new
		// UpdatePersonaldataFragment();
		// Bundle bundle = new Bundle();
		// bundle.putSerializable(Constant.USERDATA, user);
		// updateFragment.setArguments(bundle);
		// ((MainMyAct)pActivity).dumpToNext(updateFragment,
		// "UpdatePersonaldataFragment");
		// }else{
		// ToastUtils.showLongToast(this, "未获取到用户数据");
		// }
		//
		// break;
		case R.id.ivShare:
			showShare(true, null, true);

			break;
		default:
			break;
		}
	}

	class UserInfoTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... arg0) {
			if(!StringUtils.isEmpty(id)){
				UserDataB udb = ApiClientC.getUserInfo(id);
				if (udb != null) {
					user = udb.getModel();
				}
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (user != null) {
				if (!StringUtils.isEmpty(user.getDisplayName()))
					username.setText(user.getDisplayName());
				String headurl = user.getHeadsculpture();

				if (!StringUtils.isEmpty(headurl)) {
					imageLoader.displayImage(headurl, icon, options);
				}
				if (user.getSex() == 1) {
					sex.setText("男");
				} else {
					sex.setText("女");
				}
				if (user.getAear() != null) {
					address.setText(user.getAear());
				}
				if (user.getBirthday() != null) {
					birthday.setText(user.getBirthday());
				}
				userId.setText("ID: " + SP_ID.id);
				if(user.getShopAuditStatus()==2){
					iv_verified.setVisibility(View.VISIBLE);
				}
			}
		}
	}

	private Uri takePhoto() {
		String user_head = "";
		String SDState = Environment.getExternalStorageState();
		if (SDState.equals(Environment.MEDIA_MOUNTED)) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			Date date = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd_HH-mm-ss");

			user_head = Environment.getExternalStorageDirectory() + "/image"
					+ "/" + "test.png";
			File file = new File(Environment.getExternalStorageDirectory()
					+ "/image");

			if (!file.exists()) {
				file.mkdir();
			}
			Bitmap bmp = BitmapFactory.decodeResource(getResources(),
					R.drawable.goumei);

			FileOutputStream out = null;
			File file2 = new File(user_head);
			if (!file2.exists()) {
				try {
					out = new FileOutputStream(user_head);
					bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return Uri.fromFile(file2);
		} else {
			ToastUtils.showLongToast(pActivity, "内存卡不存在");
			return null;
		}
	}

	/**
	 * 
	 * @param silent
	 *            是否直接分享
	 * @param platform
	 *            选择平台
	 * @param captureView
	 *            是否制定图片
	 */
	private void showShare(boolean silent, String platform, boolean captureView) {
		Context context = pActivity;
		final OnekeyShare oks = new OnekeyShare();

		// oks.setAddress("12345678901");
//		oks.setTitle("我的资料");
//		oks.setTitleUrl("http://baidu.com");
		oks.setText("内容 不多");
		if (captureView) {
//			oks.setViewToShare(view);
		} else {
//			 oks.setImagePath(CustomShareFieldsPage.getString("imagePath",
//			 MainActivity.TEST_IMAGE));
			// oks.setImageUrl(CustomShareFieldsPage.getString("imageUrl",
			// MainActivity.TEST_IMAGE_URL));
			// oks.setImageArray(new String[]{MainActivity.TEST_IMAGE,
			// MainActivity.TEST_IMAGE_URL});
		}

//		oks.setUrl("http://www.mob.com");
		oks.setSilent(silent);
		// oks.setTheme(OnekeyShareTheme.SKYBLUE);
		oks.setTheme(OnekeyShareTheme.CLASSIC);

		if (platform != null) {
			oks.setPlatform(platform);
		}

		// 令编辑页面显示为Dialog模式
		oks.setDialogMode();

		// 在自动授权时可以禁用SSO方式
		// if(!CustomShareFieldsPage.getBoolean("enableSSO", true))
		// oks.disableSSOWhenAuthorize();

		// 去除注释，则快捷分享的操作结果将通过OneKeyShareCallback回调
		// oks.setCallback(new OneKeyShareCallback());

		// 去自定义不同平台的字段内容
		// oks.setShareContentCustomizeCallback(new
		// ShareContentCustomizeDemo());

		// 去除注释，演示在九宫格设置自定义的图标

		// 去除注释，则快捷分享九宫格中将隐藏新浪微博和腾讯微博
		// oks.addHiddenPlatform(SinaWeibo.NAME);
		// oks.addHiddenPlatform(TencentWeibo.NAME);

		// 为EditPage设置一个背景的View
		// oks.setEditPageBackground(getPage());
		oks.show(context);
	}
}
