package com.www.goumei.fragment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.www.goumei.R;
import com.www.goumei.activity.MainMyAct;
import com.www.goumei.bean.SaveVideoResult;
import com.www.goumei.bean.UserData;
import com.www.goumei.http.req.ApiClientC;
import com.www.goumei.utils.Constant;
import com.www.goumei.utils.ImageComPressUtil;
import com.www.goumei.utils.L;
import com.www.goumei.utils.PathComm;
import com.www.goumei.utils.ProcessUtil;
import com.www.goumei.utils.StringUtils;
import com.www.goumei.utils.UIHelper;
import com.www.goumei.utils.UIUtils;
import com.www.goumei.views.CircleImageView;
import com.yixia.camera.demo.utils.ToastUtils;

/**
 * 修改个人资料
 * 
 * @author Eric.Chen
 * 
 */
@SuppressLint("InflateParams")
public class UpdatePersonaldataFragment extends BaseFragment implements
		OnClickListener {
	final int RESULT_OK = -1;
	public static final int CROP = 109;
	private static String sys_img_name;
	private CircleImageView icon;
	private TextView last;
	private TextView edit;
	private TextView username;
	private TextView sex;
	private TextView address;
	private TextView birthday;
	private TextView sign;
	private UserData user;
	public ImageLoader imageLoader;

	private RelativeLayout headLL;
	private RelativeLayout usernameLL;
	private RelativeLayout sexLL;
	private RelativeLayout addressLL;
	private RelativeLayout birthdayLL;
	private RelativeLayout signLL;

	AlertDialog nickNameDialog;
	AlertDialog sexDialog;
	AlertDialog photoDialog;

	private String user_header_loacle_path;// 上传图片的本地路径
	private String user_header_network_path;// 已上传图片的網絡路径
	private String user_head = "";// 上传图片的路径
	private static DisplayImageOptions options;

	Activity pActivity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_update_personaldata,
				container, false);
		last = (TextView) view.findViewById(R.id.personaldata_last);
		edit = (TextView) view.findViewById(R.id.tv_save_data);
		icon = (CircleImageView) view.findViewById(R.id.head);
		username = (TextView) view.findViewById(R.id.tv_nickname);
		sex = (TextView) view.findViewById(R.id.sex);
		address = (TextView) view.findViewById(R.id.address);
		birthday = (TextView) view.findViewById(R.id.birthday);
		sign = (TextView) view.findViewById(R.id.sign);

		headLL = (RelativeLayout) view.findViewById(R.id.ll_head);
		usernameLL = (RelativeLayout) view.findViewById(R.id.ll_name);
		sexLL = (RelativeLayout) view.findViewById(R.id.ll_sex);
		addressLL = (RelativeLayout) view.findViewById(R.id.ll_address);
		birthdayLL = (RelativeLayout) view.findViewById(R.id.ll_birthday);
		signLL = (RelativeLayout) view.findViewById(R.id.ll_sign);
		
		headLL.setOnClickListener(this);
		usernameLL.setOnClickListener(this);
		sexLL.setOnClickListener(this);
		addressLL.setOnClickListener(this);
		birthdayLL.setOnClickListener(this);
		signLL.setOnClickListener(this);
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.test)
				.showImageForEmptyUri(R.drawable.test)
				.showImageOnFail(R.drawable.test).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		initData();

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		setListeners();
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.pActivity = activity;
		File f = new File(PathComm.picPath);
		if (!f.exists()) {
			f.mkdirs();
		}
	}

	protected void setListeners() {
		last.setOnClickListener(this);
		edit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Calendar calendar = Calendar.getInstance();
		switch (v.getId()) {
		case R.id.personaldata_last:
			((MainMyAct) pActivity).goBackAndShowPreView(null);
			break;
		// 保存
		case R.id.tv_save_data:

			new UpdateUserTask().execute("");
			break;
		// 地址
		case R.id.ll_address:
			AddressFragment addressFragment = new AddressFragment();
			((MainMyAct) pActivity).dumpToNext(addressFragment,
					"AddressFragment");
			break;
		// 生日
		case R.id.ll_birthday:
			new DatePickerDialog(pActivity,
					new DatePickerDialog.OnDateSetListener() {
						@Override
						public void onDateSet(DatePicker datePicker, int year,
								int month, int day) {
							birthday.setText(year + "-"
									+ StringUtils.addZerodata(month + 1) + "-"
									+ StringUtils.addZerodata(day));
							user.setBirthday(year + "-"
									+ StringUtils.addZerodata(month + 1) + "-"
									+ StringUtils.addZerodata(day));
						}
					}, calendar.get(Calendar.YEAR),
					calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH)).show();
			
			break;
		// 头像
		case R.id.ll_head:
			uptoclod();
			break;
		// 昵称
		case R.id.ll_name:
			editName();
			break;
		// 性别
		case R.id.ll_sex:
			selectSex();
			break;
		// 签名
		case R.id.ll_sign:
			if (user != null) {
				SignFragment updateFragment = new SignFragment();
				Bundle bundle = new Bundle();
				bundle.putSerializable(Constant.USERDATA, user);
				updateFragment.setArguments(bundle);
				((MainMyAct) pActivity).dumpToNext(updateFragment,
						"SignFragment");
			} else {
				ToastUtils.showLongToast(getActivity(), "未获取到用户数据");
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onLeave() {

	}

	@Override
	public void onReLoad(Intent paramIntent) {
		if (paramIntent != null) {
			UserData ud = (UserData) paramIntent
					.getSerializableExtra(Constant.USERDATA);
			if (ud != null) {
				user = ud;
				initData();
			}
			String address = paramIntent.getStringExtra(Constant.ADDRESS);
			if (!StringUtils.isEmpty(address)) {
				user.setAear(address);
				initData();
			}
		}

	}

	private void initData() {
		user = (UserData) getArguments().get(Constant.USERDATA);
		if (user != null) {
			imageLoader.displayImage(user.getHeadsculpture(), icon, options);
			username.setText(user.getDisplayName());
			if (user.getSex() == 1) {
				sex.setText("男");
			} else {
				sex.setText("女");
			}
			address.setText(user.getAear());
			birthday.setText(user.getBirthday());
			sign.setText(user.getIndividualitySignature());

		}
	}

	/**
	 * 姓名
	 */
	public void editName() {

		Builder dialog = new AlertDialog.Builder(pActivity);
		LayoutInflater inflater = (LayoutInflater) pActivity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.view_nickname, null);
		final EditText et_search = (EditText) layout.findViewById(R.id.edit);
		layout.findViewById(R.id.cancle).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						nickNameDialog.dismiss();
					}
				});
		layout.findViewById(R.id.yes).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				nickNameDialog.dismiss();
				String searchC = et_search.getText().toString();
				if (!StringUtils.isEmpty(searchC)) {
					user.setDisplayName(searchC);
					initData();
				} else {
					ToastUtils.showToast(pActivity, "昵称不能为空");
				}
			}
		});
		dialog.setView(layout);
		nickNameDialog = dialog.show();
		nickNameDialog.getWindow().setLayout(UIUtils.dip2px(pActivity, 300), UIUtils.dip2px(pActivity, 200));
	}

	/**
	 * 头像
	 */
	public void uptoclod() {
		LayoutInflater inflater = (LayoutInflater) pActivity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.view_photo, null);
		layout.findViewById(R.id.cancle).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						photoDialog.dismiss();
						
					}
				});
		layout.findViewById(R.id.nan).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				photoDialog.dismiss();
				pickPhoto();
			}
		});
		layout.findViewById(R.id.nv).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				photoDialog.dismiss();
				
				takePhoto();
			}
		});
		photoDialog = new AlertDialog.Builder(pActivity).setView(layout).create();
		if (!photoDialog.isShowing()) {
			photoDialog.show();
			photoDialog.getWindow().setLayout(UIUtils.dip2px(pActivity, 300), UIUtils.dip2px(pActivity, 200));
		}
	}

	/**
	 * 性别
	 */
	public void selectSex() {
		LayoutInflater inflater = (LayoutInflater) pActivity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.view_sex, null);
		layout.findViewById(R.id.cancle).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						sexDialog.dismiss();
					}
				});
		layout.findViewById(R.id.nan).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				sexDialog.dismiss();
				user.setSex(1);
				initData();
			}
		});
		layout.findViewById(R.id.nv).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				sexDialog.dismiss();
				user.setSex(0);
				initData();
			}
		});
		sexDialog = new AlertDialog.Builder(pActivity).setView(layout).create();
		if (!sexDialog.isShowing()) {
			sexDialog.show();
			sexDialog.getWindow().setLayout(UIUtils.dip2px(pActivity, 300), UIUtils.dip2px(pActivity, 200));
		}
	}

	/**
	 * 通过uri获取文件的绝对路径
	 * 
	 * @param uri
	 * @return
	 */
	public String getAbsoluteImagePath(Activity context, Uri uri) {
		String imagePath = "";
		String[] proj = { MediaStore.Images.Media.DATA };
		@SuppressWarnings("deprecation")
		Cursor cursor = context.managedQuery(uri, proj, null, null, null);
		if (cursor != null) {
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			if (cursor.getCount() > 0 && cursor.moveToFirst()) {
				imagePath = cursor.getString(column_index);
			}
		}
		return imagePath;
	}

	/***
	 * 从中取图片
	 */
	private void pickPhoto() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		Intent wrapperIntent = Intent.createChooser(intent, null);
		startActivityForResult(wrapperIntent, 150);
	}

	/**
	 * 接收其他页面返回的信息
	 */

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == 150) {
				Uri uri = data.getData();
				String[] pojo = { MediaStore.Images.Media.DATA };
				@SuppressWarnings("deprecation")
				Cursor cursor = pActivity.managedQuery(uri, pojo, null, null,
						null);
				if (cursor != null) {
					int colunm_index = cursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					cursor.moveToFirst();
					String path = cursor.getString(colunm_index);

					user_head = path;
					user_header_loacle_path = path;

					// getImage(user_header_loacle_path,
					// update_user_head);
					// 图片裁剪
					cropImg(getActivity(), user_header_loacle_path);
				}

			} else if (requestCode == 120) {
				if (user_head != null) {
					user_header_loacle_path = user_head;

					// getImage(user_header_loacle_path, update_user_head);
					// 图片裁剪
					cropImg(getActivity(), user_header_loacle_path);
				}
			} else if (requestCode == CropImageInterface.CROP) {

				File cropFile = new File(PathComm.picPath + sys_img_name);
				boolean exist = cropFile.exists();
				long length = cropFile.length();
				if (exist && length > 0) {
					getImage(cropFile.getAbsolutePath(), icon);
					new DialogHelper(cropFile.getAbsolutePath()).execute(1);
				}
			}

		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	public void cropImg(Activity activity, String imgFilePath) {
		sys_img_name = System.currentTimeMillis() + ".jpg";
		Intent intent = new Intent();
		intent.setAction("com.android.camera.action.CROP");
		intent.putExtra("crop", "true");

		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX,outputY 是剪裁图片的宽高
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 300);
		intent.putExtra("return-data", true);
		intent.setDataAndType(Uri.fromFile(new File(imgFilePath)), "image/*");
		intent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(new File(PathComm.picPath + sys_img_name)));
		startActivityForResult(intent, CROP);
	}

	@SuppressLint("SimpleDateFormat")
	private void takePhoto() {
		String SDState = Environment.getExternalStorageState();
		if (SDState.equals(Environment.MEDIA_MOUNTED)) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			Date date = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd_HH-mm-ss");

			user_head = Environment.getExternalStorageDirectory() + "/image"
					+ "/" + formatter.format(date) + ".jpg";
			File file = new File(Environment.getExternalStorageDirectory()
					+ "/image");

			if (!file.exists()) {
				file.mkdir();
			}

			intent.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(new File(user_head)));
			startActivityForResult(intent, 120);

		} else {
			ToastUtils.showLongToast(pActivity, "内存卡不存在");
		}
	}

	/**
	 * imageView中加载本地图片
	 * 
	 * @param img_url
	 *            本地图片的路径
	 * @param img
	 *            要显示图片的imageView
	 * @return
	 */
	public void getImage(String img_url, ImageView img) {
		// img.setScaleType(ScaleType.CENTER_INSIDE);
		BitmapDrawable bitmapDrawable = (BitmapDrawable) img.getDrawable();
		if (bitmapDrawable != null && !bitmapDrawable.getBitmap().isRecycled()) {
			bitmapDrawable.getBitmap().recycle();

		}
		img.setImageBitmap(ImageComPressUtil.getimage(img_url));
	}

	class UpdateUserTask extends AsyncTask<String, String, String> {
		String code;

		@Override
		protected String doInBackground(String... arg0) {
			code = ApiClientC.updateUser(user);

			return null;
		}

		@Override
		protected void onPreExecute() {

			ProcessUtil.showProgressDialog(pActivity, "修改中...");
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			ProcessUtil.dismissProgressdialog();
			L.e("code:::::::::::::::::::::::::" + code);
			if (code != null && code.equals("1")) {
				UIHelper.showMsg(pActivity, "修改成功");
			} else {
				UIHelper.showMsg(pActivity, "修改失败");
			}

		}
	}

	public class DialogHelper extends AsyncTask<Integer, Void, Integer> {
		SaveVideoResult saveVideoResult = null;
		String path;

		public DialogHelper(String path) {
			this.path = path;
		}

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected Integer doInBackground(Integer... params) {
			saveVideoResult = ApiClientC.SaveImage(path);
			return 1;

		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			if (saveVideoResult != null
					&& !StringUtils.isEmpty(saveVideoResult.getSavePath())) {
				user.setHeadsculpture(saveVideoResult.getSavePath());
			}
		}
	}

}
