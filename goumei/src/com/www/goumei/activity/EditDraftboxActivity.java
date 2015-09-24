package com.www.goumei.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.www.goumei.R;
import com.www.goumei.bean.UpVideoDetailBean;
import com.www.goumei.bean.Videos;
import com.www.goumei.emoji.EmoteInputView;
import com.www.goumei.emoji.EmoticonsEditText;
import com.www.goumei.http.ApiClient;
import com.www.goumei.utils.ProcessUtil;
import com.www.goumei.utils.UIHelper;
import com.yixia.camera.demo.utils.ToastUtils;
import com.yixia.camera.util.StringUtils;

public class EditDraftboxActivity extends Activity implements OnClickListener{
	// 工具栏按钮
	private TextView textback, textfinish, texttheme, cover_text_ok;
	private TextView cover, theme, cla, rel;
	private EmoticonsEditText edittext;
	private TextView had_choose_str;
	private ImageView coverimg, cover_play;
	private VideoView video;
	private Videos videoMsg;
	/** 录制视频的本地地址*/
	private String videoPath;
	/** 录制视频的上传地址*/
	private String saveVideoPath;
	/** 用来下载图片的类*/
	public ImageLoader imageLoader;
	/** qq分享 */
	private LinearLayout qqShare;
	/** 微信分享 */
	private LinearLayout wechatShare;
	/** 新浪分享 */
	private LinearLayout sinaShare;
	private ImageView cover_img_smile;
	protected EmoteInputView mInputView;

	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_info_cover);
		initview();
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(this));
		Intent intent = getIntent();
		videoMsg = (Videos) intent.getSerializableExtra("video");
		
		if(videoMsg!=null){
			videoPath = saveVideoPath = videoMsg.getUrl();
			savePicPath = videoMsg.getCover();
			imageLoader.displayImage(savePicPath, coverimg);
			
			themeName = videoMsg.getThemeName();
			themeID = videoMsg.getThemeID();
			categoriesID = videoMsg.getCategoryID();
			categoriesName = videoMsg.getCategoryName();
			webSite = videoMsg.getLink();
			setText();
			edittext.setText(videoMsg.getTitle());
		}
	}

	private void initview() {
		// 自动生成的方法存根

		textback = (TextView) findViewById(R.id.tv_prior);
		texttheme = (TextView) findViewById(R.id.tv_title);
		texttheme.setText("视频信息");
		textfinish = (TextView) findViewById(R.id.tv_later);
		textfinish.setText("草稿箱");
		coverimg = (ImageView) findViewById(R.id.cover_img_img);
		cover_play = (ImageView) findViewById(R.id.cover_img_play);
		video = (VideoView) findViewById(R.id.cover_video);
		cover_img_smile = (ImageView) findViewById(R.id.cover_img_smile);
		
		qqShare = (LinearLayout) findViewById(R.id.qqShare);
		wechatShare = (LinearLayout) findViewById(R.id.wechatShare);
		sinaShare = (LinearLayout) findViewById(R.id.sinaShare);

		cover = (TextView) findViewById(R.id.cover_text_setcover);
		theme = (TextView) findViewById(R.id.cover_text_theme);
		rel = (TextView) findViewById(R.id.cover_text_rel);
		cla = (TextView) findViewById(R.id.cover_text_cla);
		had_choose_str = (TextView) findViewById(R.id.had_choose_str);
		cover_text_ok = (TextView) findViewById(R.id.cover_text_ok);
		edittext = (EmoticonsEditText) findViewById(R.id.cover_edit_des);
		mInputView = (EmoteInputView) findViewById(R.id.chat_eiv_inputview);
		mInputView.setEditText(edittext);
		
		cover_text_ok.setOnClickListener(this);
		textback.setOnClickListener(this);
		textfinish.setOnClickListener(this);
		cover.setOnClickListener(this);
		theme.setOnClickListener(this);
		rel.setOnClickListener(this);
		cla.setOnClickListener(this);
		cover_play.setOnClickListener(this);
		cover_img_smile.setOnClickListener(this);
		edittext.setOnClickListener(this);
		qqShare.setOnClickListener(this);
		wechatShare.setOnClickListener(this);
		sinaShare.setOnClickListener(this);

	}
	
	protected void showKeyBoard() {
		if (mInputView.isShown()) {
			mInputView.setVisibility(View.GONE);
			cover_text_ok.setVisibility(View.VISIBLE);
		}
		edittext.requestFocus();
		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
				.showSoftInput(edittext, 0);
	}
	protected void hideKeyBoard() {
		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(this.getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	public void onClick(View arg0) {
		// 自动生成的方法存根
		switch (arg0.getId()) {
		case R.id.cover_edit_des:
			showKeyBoard();
			
			break;
		case R.id.cover_img_smile:
			edittext.requestFocus();
			if (mInputView.isShown()) {
				// hideKeyBoard();
				showKeyBoard();
			} else {
				hideKeyBoard();
				mInputView.setVisibility(View.VISIBLE);
				cover_text_ok.setVisibility(View.GONE);
			}
			break;
		case R.id.tv_prior:
			finish();
			break;
		case R.id.tv_later:
			updateVideos(false);
			break;
		case R.id.cover_text_setcover:
			// 选择封面
			Intent intent4 = new Intent();
			intent4.setClass(EditDraftboxActivity.this, ThemeselectActivity.class);
			startActivityForResult(intent4, 004);
			break;
		case R.id.cover_text_theme:
			// 选择主题
			Intent intent1 = new Intent();
			intent1.setClass(EditDraftboxActivity.this, ThemeActivity.class);
			startActivityForResult(intent1, 001);
			break;
		case R.id.cover_text_cla:
			// 选择分类
			Intent intent2 = new Intent();
			intent2.setClass(EditDraftboxActivity.this, ThemeclaActivity.class);
			startActivityForResult(intent2, 002);
			break;
		case R.id.cover_text_rel:
			// 添加相关
			Intent intent3 = new Intent();
			intent3.setClass(EditDraftboxActivity.this, ThemerelActivity.class);
			startActivityForResult(intent3, 003);
			break;
		//上传视频、等数据到服务器端
		case R.id.cover_text_ok:
			updateVideos(true);
			break;
		case R.id.cover_img_play:
			// 先判断这个文件是否存在
			if (videoPath != null) {
				video.setVideoPath(videoPath);
				// 让VideoView获取焦点
				video.requestFocus();
				// 开始播放
				coverimg.setVisibility(8);
				cover_play.setVisibility(8);
				video.start();

			} else {
				Toast.makeText(EditDraftboxActivity.this, "文件不存在", Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.qqShare:
			showShare(true, "QZone", true);
			// showShare(true, null, true);
			break;
		case R.id.wechatShare:
			showShare(true, "Wechat", true);
			break;
		case R.id.sinaShare:
			showShare(false, "SinaWeibo", true);
			// showShare(true, null, true);
			break;

		}
	}
	/**
	 * 上传前添加参数
	 * @param isPublish true发布 false不发布-保存到草稿箱
	 */
	private void updateVideos(boolean isPublish){
		bean = new UpVideoDetailBean();
		SharedPreferences sharedPreferences = getSharedPreferences("wujay", Context.MODE_PRIVATE); //私有数据
		int local_user_id = sharedPreferences.getInt("local_user_id", 0);
		if(local_user_id!=0){
			bean.setUserID(local_user_id);
		}else{
			ToastUtils.showLongToast("请重新登录");
			return;
		}
		
		if(!StringUtils.isEmpty(edittext.getText().toString().trim())){
			bean.setTitle(edittext.getText().toString().trim());
		}else{
			ToastUtils.showLongToast("请输入描述");
			return;
		}
		if(!StringUtils.isEmpty(themeName)){
			bean.setThemeID(themeID);
		}else{
			ToastUtils.showLongToast("请选择主题");
			return;
		}
		if(!StringUtils.isEmpty(savePicPath)){
			bean.setCover(savePicPath);
		}else{
			ToastUtils.showLongToast("请选择一张封面图片");
			return;
		}
		if(!StringUtils.isEmpty(categoriesName)){
			bean.setCategoryID(categoriesID);
		}else{
			ToastUtils.showLongToast("请选择分类");
			return;
		}
		if(!StringUtils.isEmpty(webSite)){
			bean.setLink(webSite);
		}else{
			bean.setLink("");
		}
		if(!StringUtils.isEmpty(description)){
			bean.setLinkDescription(description);
		}else{
			bean.setLinkDescription("");
		}
		bean.setIsPublish(isPublish);
		bean.setUrl(saveVideoPath);
		
		DialogHelper dialogHelper = new DialogHelper();
		dialogHelper.execute();
	}
	
	
	
	//private static final int SAVE_VIDEO = 0;
	private int updateCode;
	private UpVideoDetailBean bean;
	
	public class DialogHelper extends AsyncTask<Integer, Void, Integer> {
		public DialogHelper() {
		}

		@Override
		protected void onPreExecute() {
			ProcessUtil.showProgressDialog(EditDraftboxActivity.this, "上传数据中...");
		}

		@Override
		protected Integer doInBackground(Integer... params) {
			
			updateCode = ApiClient.updateVideoDetailsMsg(bean);
			return updateCode;

		}

		@Override
		protected void onPostExecute(Integer result) {
			switch (result) {
			case 200:
				ProcessUtil.dismissProgressdialog();
				if(updateCode == 200){
					UIHelper.showMsg(EditDraftboxActivity.this, "数据上传成功");
					EditDraftboxActivity.this.finish();
				}else{
					UIHelper.showMsg(EditDraftboxActivity.this, "数据上传失败");
				}
				break;

			default:
				break;
			}
			super.onPostExecute(result);
		}
	}
	
	
	
	/** 主题ID */
	private int themeID=0;
	/** 主题名称*/
	private String themeName="";
	/** 分类ID*/
	private int categoriesID=0;
	/** 分类名称*/
	private String categoriesName="";
	/** 关联网址*/
	private String webSite;
	/** 网址描述*/
	private String description;
	/**封面服务器端的地址*/
	private String savePicPath;
	/**封面本地的地址*/
	private String localPicPath;
	
	/**
	 * 设置页面显示主题、分类内容
	 */
	private void setText(){
		StringBuffer buffer = new StringBuffer();
		if(!StringUtils.isEmpty(themeName)){
			buffer.append("#"+themeName);
		}
		if(!StringUtils.isEmpty(categoriesName)){
			buffer.append("#"+categoriesName);
		}
		if(!StringUtils.isEmpty(webSite)){
			buffer.append("#"+webSite);
		}
		had_choose_str.setText(buffer.toString());
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(null != data){
			switch (requestCode) {
			case 001:
				Bundle b1 = data.getExtras(); // data为B中回传的Intent
				if(b1 != null){
					themeID = b1.getInt("themeID");
					themeName = b1.getString("themeName");
				}
				setText();
				break;
			case 002:
				Bundle b2 = data.getExtras(); // data为B中回传的Intent
				if(b2 != null){
					categoriesName = b2.getString("categoriesName");
					categoriesID = b2.getInt("categoriesID");
				}
				setText();
				break;
			case 003:
				Bundle b3 = data.getExtras(); // data为B中回传的Intent
				if(b3!=null){
					webSite = b3.getString("webSite");
					description = b3.getString("description");
				}
				setText();
				break;
			case 004:
				Bundle b_cover = data.getExtras();
				if(b_cover!=null){
					savePicPath = b_cover.getString("savePicPath");
					localPicPath = b_cover.getString("localPicPath");
				}
			
				
				if (localPicPath.equals("") || localPicPath.length() == 0) {
					return;
				} else {
					Bitmap bitmap = BitmapFactory.decodeFile(localPicPath);
					coverimg.setImageBitmap(bitmap);
				}
				break;
			}
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
		if(saveVideoPath!=null&&savePicPath!=null){
			Context context = EditDraftboxActivity.this;
			final OnekeyShare oks = new OnekeyShare();
			oks.setTitle("我的分享");
			oks.setTitleUrl(saveVideoPath);
			oks.setText(saveVideoPath);
			oks.setImageUrl(savePicPath);
			// "http://img.kumi.cn/photo/b4/70/80/b47080e1c5ad4880.jpg"
			// 令编辑页面显示为Dialog模式
			oks.setDialogMode();
			oks.disableSSOWhenAuthorize();
			oks.setUrl("http://www.mob.com");
			oks.setSilent(silent);
			// oks.setTheme(OnekeyShareTheme.SKYBLUE);
			oks.setTheme(OnekeyShareTheme.CLASSIC);
			if (platform != null) {
				oks.setPlatform(platform);
			}
			// 令编辑页面显示为Dialog模式
			oks.setDialogMode();
			oks.show(context);
		}else{
			ToastUtils.showToast("请上传封面或视频后再来分享！");
		}
		
	}

}
