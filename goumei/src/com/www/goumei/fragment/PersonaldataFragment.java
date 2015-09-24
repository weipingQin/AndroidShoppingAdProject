package com.www.goumei.fragment;

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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.www.goumei.R;
import com.www.goumei.activity.MainActivity;
import com.www.goumei.activity.MainMyAct;
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
 * @author Eric.Chen
 *
 */
public class PersonaldataFragment extends BaseFragment  implements   OnClickListener {

	private CircleImageView icon;
	private TextView last;
	private TextView edit;
	private TextView username;
	private TextView userId;
	private TextView sex;
	private TextView address;
	private TextView birthday;
	private ImageView shareIV;
	private ImageView iv_v;

	private UserData user;
	public ImageLoader imageLoader; 
	private static DisplayImageOptions options;
	
	Activity  pActivity;
	View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view=inflater.inflate(R.layout.activity_personaldata, container, false);
		last = (TextView) view.findViewById(R.id.personaldata_last);
		edit = (TextView) view.findViewById(R.id.dataEdit);
		icon = (CircleImageView) view.findViewById(R.id.CircleImageView);
		username = (TextView)view. findViewById(R.id.username);
		userId=(TextView)view.findViewById(R.id.userID);
		sex = (TextView) view.findViewById(R.id.sex);
		address = (TextView)view.findViewById(R.id.address);
		birthday = (TextView) view.findViewById(R.id.birthday);
		shareIV=(ImageView)view.findViewById(R.id.ivShare);
		iv_v=(ImageView)view.findViewById(R.id.iv_verified);
		shareIV.setOnClickListener(this);
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
		
		 options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.test)
		.showImageForEmptyUri(R.drawable.test)
		.showImageOnFail(R.drawable.test)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
		
		return view;
	}
   @Override
public void onActivityCreated(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	   setListeners();
	super.onActivityCreated(savedInstanceState);
}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.pActivity=activity;
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
		   ((MainMyAct)pActivity).goBackAndShowPreView(null);
			break;
		//编辑
		case R.id.dataEdit:
			if(user!=null){
				UpdatePersonaldataFragment   updateFragment=new UpdatePersonaldataFragment();
				Bundle bundle = new Bundle();  
				bundle.putSerializable(Constant.USERDATA, user);
				updateFragment.setArguments(bundle);  
				((MainMyAct)pActivity).dumpToNext(updateFragment, "UpdatePersonaldataFragment");
			}else{
				ToastUtils.showLongToast(getActivity(), "未获取到用户数据");
			}
			
			break;
		case R.id.ivShare:
			showShare(true,null,true);
            
			break;
		default:
			break;
		}
	}

	@Override
	public void onLeave() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReLoad(Intent paramIntent) {
		// TODO Auto-generated method stub
		new UserInfoTask().execute("");
	}

	class UserInfoTask extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... arg0) {
			UserDataB udb=ApiClientC.getUserInfo(SP_ID.id);
			if(udb!=null){
				user=udb.getModel();
			}
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if(user!=null){
				if(!StringUtils.isEmpty(user.getDisplayName()))
				username.setText(user.getDisplayName());
				String headurl=user.getHeadsculpture();
				int certificationState =user.getCertificationState();
				if(certificationState==Constant.UserCertificationState_V){
					iv_v.setVisibility(View.VISIBLE);
				}else{
					iv_v.setVisibility(View.INVISIBLE);
				}
//				if(!StringUtils.isEmpty(headurl)){
					imageLoader.displayImage(headurl, icon, options);
//				}
				if(user.getSex()==1){
					sex.setText("男");
				}else{
					sex.setText("女");
				}
				if(user.getAear()!=null){
					address.setText(user.getAear());
				}
				if(user.getBirthday()!=null){
					birthday.setText(user.getBirthday());
				}
				userId.setText("ID: "+SP_ID.id);
				
			}
		}
	}
	private Uri takePhoto() {
		String user_head="";
		String SDState = Environment.getExternalStorageState();
		if (SDState.equals(Environment.MEDIA_MOUNTED)) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			Date date = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd_HH-mm-ss");

			user_head = Environment.getExternalStorageDirectory() + "/image"
					+ "/"  + "test.png";
			File file = new File(Environment.getExternalStorageDirectory()
					+ "/image");

			if (!file.exists()) {
				file.mkdir();
			}
			Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.goumei);
			 
			 
			FileOutputStream out = null;
			File file2=new File(user_head);
			if(!file2.exists()){
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
	 * @param silent 是否直接分享
	 * @param platform 选择平台
	 * @param captureView 是否制定图片
	 */
	private void showShare(boolean silent, String platform, boolean captureView) {
		Context context = pActivity;
		final OnekeyShare oks = new OnekeyShare();

		//oks.setAddress("12345678901");
		oks.setTitle("我的资料");
		oks.setTitleUrl("http://baidu.com");
		oks.setText("内容 不多");
		if (captureView) {
			oks.setViewToShare(view);
		} else {
//			oks.setImagePath(CustomShareFieldsPage.getString("imagePath", MainActivity.TEST_IMAGE));
//			oks.setImageUrl(CustomShareFieldsPage.getString("imageUrl", MainActivity.TEST_IMAGE_URL));
		//	oks.setImageArray(new String[]{MainActivity.TEST_IMAGE, MainActivity.TEST_IMAGE_URL});
		}

		oks.setUrl( "http://www.mob.com");
		oks.setSilent(silent);
//			oks.setTheme(OnekeyShareTheme.SKYBLUE);
			oks.setTheme(OnekeyShareTheme.CLASSIC);

		if (platform != null) {
			oks.setPlatform(platform);
		}


		// 令编辑页面显示为Dialog模式
		oks.setDialogMode();

		// 在自动授权时可以禁用SSO方式
		//if(!CustomShareFieldsPage.getBoolean("enableSSO", true))
//			oks.disableSSOWhenAuthorize();

		// 去除注释，则快捷分享的操作结果将通过OneKeyShareCallback回调
//		oks.setCallback(new OneKeyShareCallback());

		// 去自定义不同平台的字段内容
		//oks.setShareContentCustomizeCallback(new ShareContentCustomizeDemo());

		// 去除注释，演示在九宫格设置自定义的图标

		// 去除注释，则快捷分享九宫格中将隐藏新浪微博和腾讯微博
//		oks.addHiddenPlatform(SinaWeibo.NAME);
//		oks.addHiddenPlatform(TencentWeibo.NAME);

		// 为EditPage设置一个背景的View
//		oks.setEditPageBackground(getPage());
		oks.show(context);
	}
}
