package com.yixia.camera.demo.ui.record;

import java.io.File;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.learnncode.mediachooser.MediaChooser;
import com.learnncode.mediachooser.activity.BucketHomeFragmentActivity;
import com.www.goumei.R;
import com.yixia.camera.FFMpegUtils;
import com.yixia.camera.MediaRecorder;
import com.yixia.camera.MediaRecorder.OnErrorListener;
import com.yixia.camera.MediaRecorder.OnPreparedListener;
import com.yixia.camera.MediaRecorderFilter;
import com.yixia.camera.VCamera;
import com.yixia.camera.demo.common.CommonIntentExtra;
import com.yixia.camera.demo.log.Logger;
import com.yixia.camera.demo.ui.BaseActivity;
import com.yixia.camera.demo.ui.record.views.ProgressView;
import com.yixia.camera.demo.utils.ConvertToUtils;
import com.yixia.camera.demo.utils.DeviceUtils;
import com.yixia.camera.demo.utils.NetworkUtils;
import com.yixia.camera.model.MediaObject;
import com.yixia.camera.model.MediaObject.MediaPart;
import com.yixia.camera.util.StringUtils;
import com.yixia.camera.view.CameraNdkView;

/**
 * 视频录制
 * 
 */
@SuppressLint({ "ClickableViewAccessibility", "HandlerLeak" })
public class MediaRecorderActivity extends BaseActivity implements OnErrorListener, OnClickListener, OnPreparedListener, MediaRecorderBase.OnEncodeListener {

	/** 录制最长时间 */
	public final static int RECORD_TIME_MAX = 10 * 1000;
	/** 录制最小时间 */
	public final static int RECORD_TIME_MIN = 3 * 1000;
	/** 刷新进度条 */
	private static final int HANDLE_INVALIDATE_PROGRESS = 0;
	/** 延迟拍摄停止 */
	private static final int HANDLE_STOP_RECORD = 1;
	/** 对焦 */
	private static final int HANDLE_HIDE_RECORD_FOCUS = 2;
	/** 导入图片 */
	public final static int REQUEST_CODE_IMPORT_IMAGE = 999;
	/** 导入视频 */
//	public final static int REQUEST_CODE_IMPORT_VIDEO = 998;
	/** 导入视频截取 */
	public final static int REQUEST_CODE_IMPORT_VIDEO_EDIT = 997;
	/** 下一步 */
	private LinearLayout mTitleNext;
	/** 对焦图标-带动画效果 */
	private ImageView mFocusImage;
	/** 前后摄像头切换 */
	private CheckBox mCameraSwitch;
	/** 回删按钮、延时按钮、滤镜按钮 */
	private LinearLayout mRecordDelete;
	/** 闪光灯 */
	private CheckBox mRecordLed;
	/** 拍摄按钮 */
	private ImageView mRecordController;
	/** 底部条 */
//	private LinearLayout mBottomLayout;
	/** 摄像头数据显示画布 */
	private CameraNdkView mSurfaceView;
	/** 录制进度 */
	private ProgressView mProgressView;
	/** 对焦动画 */
	private Animation mFocusAnimation;
	/** SDK视频录制对象 */
	private MediaRecorderFilter mMediaRecorder;
	/** 视频信息 */
	private MediaObject mMediaObject;

	/** 需要重新编译（拍摄新的或者回删） */
	//private boolean mRebuild;
	/** on */
	private boolean mCreated;
	/** 是否是点击状态 */
	private volatile boolean mPressedStatus;
	/** 是否已经释放 */
	//private volatile boolean mReleased;
	/** 对焦图片宽度 */
	private int mFocusWidth;
	/** 底部背景色 */
	//private int mBackgroundColorNormal, mBackgroundColorPress;
	/** 屏幕宽度 */
	private int mWindowWidth;
	/** 导入照片 */
	private LinearLayout record_import_img;
	/** 导入视频 */
	private LinearLayout record_import_video;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mCreated = false;
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 防止锁屏
		loadIntent();
		loadViews();
		mCreated = true;
	}

	/** 加载传入的参数 */
	private void loadIntent() {
		mWindowWidth = DeviceUtils.getScreenWidth(this);
		mFocusWidth = ConvertToUtils.dipToPX(this, 64);
	}

	/** 加载视图 */
	private void loadViews() {
		setContentView(R.layout.activity_media_recorder);
		// ~~~ 绑定控件
		mSurfaceView = (CameraNdkView) findViewById(R.id.record_preview);
		mCameraSwitch = (CheckBox) findViewById(R.id.record_camera_switcher);
		mTitleNext = (LinearLayout) findViewById(R.id.record_import_comp);
		mFocusImage = (ImageView) findViewById(R.id.record_focusing);
		mProgressView = (ProgressView) findViewById(R.id.record_progress);
		mRecordDelete = (LinearLayout) findViewById(R.id.record_import_del);
		mRecordController = (ImageView) findViewById(R.id.record_controller);
//		mBottomLayout = (LinearLayout) findViewById(R.id.bottom_layout);
		mRecordLed = (CheckBox) findViewById(R.id.record_camera_led);
		record_import_img = (LinearLayout) findViewById(R.id.record_import_img);
		record_import_video = (LinearLayout) findViewById(R.id.record_import_video);
		record_import_img.setOnClickListener(this);
		record_import_video.setOnClickListener(this);
		// ~~~ 绑定事件
		if (DeviceUtils.hasICS())
			mSurfaceView.setOnTouchListener(mOnSurfaveViewTouchListener);

		mTitleNext.setOnClickListener(this);
		findViewById(R.id.title_back).setOnClickListener(this);
		mRecordDelete.setOnClickListener(this);
		mRecordController.setOnTouchListener(mOnVideoControllerTouchListener);

		// ~~~ 设置数据

		//是否支持前置摄像头
		if (MediaRecorderBase.isSupportFrontCamera()) {
			mCameraSwitch.setOnClickListener(this);
		} else {
			mCameraSwitch.setVisibility(View.GONE);
		}
		//是否支持闪光灯
		if (DeviceUtils.isSupportCameraLedFlash(getPackageManager())) {
			mRecordLed.setOnClickListener(this);
		} else {
			mRecordLed.setVisibility(View.GONE);
		}

		try {
			mFocusImage.setImageResource(R.drawable.video_focus);
		} catch (OutOfMemoryError e) {
			Logger.e(e);
		}

		mProgressView.setMaxDuration(RECORD_TIME_MAX);
		initSurfaceView();
	}

	/** 初始化画布 */
	private void initSurfaceView() {
		final int w = DeviceUtils.getScreenWidth(this);
		final int h = DeviceUtils.getScreenHeight(this);
		//((RelativeLayout.LayoutParams) mBottomLayout.getLayoutParams()).topMargin = w;
		int width = w;
		int height = h/2 +100;
		//
		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mSurfaceView.getLayoutParams();
		lp.width = width;
		lp.height = height;
		mSurfaceView.setLayoutParams(lp);
	}

	/** 初始化拍摄SDK */
	private void initMediaRecorder() {
		mMediaRecorder = new MediaRecorderFilter();
		//mRebuild = true;
		mMediaRecorder.setOnErrorListener(this);
		mMediaRecorder.setOnPreparedListener(this);
		//WIFI下800k码率，其他情况（4G/3G/2G）600K码率
		mMediaRecorder.setVideoBitRate(NetworkUtils.isWifiAvailable(this) ? MediaRecorder.VIDEO_BITRATE_MEDIUM : MediaRecorder.VIDEO_BITRATE_NORMAL);
		//mMediaRecorder.setSurfaceHolder(mSurfaceView.getHolder());
		mMediaRecorder.setSurfaceView(mSurfaceView);

		String key = String.valueOf(System.currentTimeMillis());
		mMediaObject = mMediaRecorder.setOutputDirectory(key, VCamera.getVideoCachePath() + key);
		if (mMediaObject != null) {
			mMediaRecorder.prepare();
			mMediaRecorder.setCameraFilter(MediaRecorderFilter.CAMERA_FILTER_NO);
			mProgressView.setData(mMediaObject);
		} else {
			Toast.makeText(this, R.string.record_camera_init_faild, Toast.LENGTH_SHORT).show();
			finish();
		}
//		File f = new File(VCamera.getVideoCachePath());
//		if (!FileUtils.checkFile(f)) {
//			f.mkdirs();
//		}
//		mMediaRecorder.setSurfaceHolder(mSurfaceView.getHolder());
//		mMediaRecorder.prepare();
	}

	/** 点击屏幕对焦 */
	private View.OnTouchListener mOnSurfaveViewTouchListener = new View.OnTouchListener() {

		@SuppressLint("ClickableViewAccessibility")
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (mMediaRecorder == null || !mCreated) {
				return false;
			}

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				//检测是否手动对焦
				if (checkCameraFocus(event))
					return true;
				break;
			}
			return true;
		}

	};

	/** 按住录制 */
	private View.OnTouchListener mOnVideoControllerTouchListener = new View.OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (mMediaRecorder == null) {
				return false;
			}

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				//判断是否已经超时
				if (mMediaObject.getDuration() >= RECORD_TIME_MAX) {
					return true;
				}

				//取消回删
				if (cancelDelete())
					return true;

				startRecord();

				break;

			case MotionEvent.ACTION_UP:
				// 暂停
				if (mPressedStatus) {
					stopRecord();

					//检测是否已经完成
					if (mMediaObject.getDuration() >= RECORD_TIME_MAX) {
						mTitleNext.performClick();
					}
				}
				break;
			}
			return true;
		}

	};
	
	protected void onStart() {
		super.onStart();
//		UtilityAdapter.freeFilterParser();
//		UtilityAdapter.initFilterParser();

		if (mMediaRecorder == null) {
			initMediaRecorder();
		} else {
			mMediaRecorder.setSurfaceHolder(mSurfaceView.getHolder());
			mRecordLed.setChecked(false);
			mMediaRecorder.prepare();
			//mProgressView.setData(mMediaObject);
		}
		checkStatus();
		
	};
	
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
//		stopRecord();
//		UtilityAdapter.freeFilterParser();
//		if (!mReleased) {
//			if (mMediaRecorder != null)
//				mMediaRecorder.release();
//		}
//		mReleased = false;
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		
		if (mMediaRecorder != null)
			mMediaRecorder.release();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	

	/** 手动对焦 */
	@SuppressLint("NewApi")
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private boolean checkCameraFocus(MotionEvent event) {
		mFocusImage.setVisibility(View.GONE);
		float x = event.getX();
		float y = event.getY();
		float touchMajor = event.getTouchMajor();
		float touchMinor = event.getTouchMinor();

		Rect touchRect = new Rect((int) (x - touchMajor / 2), (int) (y - touchMinor / 2), (int) (x + touchMajor / 2), (int) (y + touchMinor / 2));
		if (touchRect.right > 1000)
			touchRect.right = 1000;
		if (touchRect.bottom > 1000)
			touchRect.bottom = 1000;
		if (touchRect.left < 0)
			touchRect.left = 0;
		if (touchRect.right < 0)
			touchRect.right = 0;

		if (touchRect.left >= touchRect.right || touchRect.top >= touchRect.bottom)
			return false;

		ArrayList<Camera.Area> focusAreas = new ArrayList<Camera.Area>();
		focusAreas.add(new Camera.Area(touchRect, 1000));
		if (!mMediaRecorder.manualFocus(new Camera.AutoFocusCallback() {

			@Override
			public void onAutoFocus(boolean success, Camera camera) {
				//				if (success) {
				mFocusImage.setVisibility(View.GONE);
				//				}
			}
		}, focusAreas)) {
			mFocusImage.setVisibility(View.GONE);
		}

		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mFocusImage.getLayoutParams();
		int left = touchRect.left - (mFocusWidth / 2);
		int top = touchRect.top - (mFocusWidth / 2);
		if (left < 0)
			left = 0;
		else if (left + mFocusWidth > mWindowWidth)
			left = mWindowWidth - mFocusWidth;
		if (top + mFocusWidth > mWindowWidth)
			top = mWindowWidth - mFocusWidth;

		lp.leftMargin = left;
		lp.topMargin = top;
		mFocusImage.setLayoutParams(lp);
		mFocusImage.setVisibility(View.VISIBLE);

		if (mFocusAnimation == null)
			mFocusAnimation = AnimationUtils.loadAnimation(this, R.anim.record_focus);

		mFocusImage.startAnimation(mFocusAnimation);

		mHandler.sendEmptyMessageDelayed(HANDLE_HIDE_RECORD_FOCUS, 3500);//最多3.5秒也要消失
		return true;
	}
	/** 显示下一步 */
	private static final int HANDLE_SHOW_TIPS = 2;

	/** 开始录制 */
	private void startRecord() {
		mPressedStatus = true;

		if (mMediaRecorder != null) {
			mMediaRecorder.startRecord();
		}

		if (mHandler != null) {
			mHandler.sendEmptyMessage(HANDLE_INVALIDATE_PROGRESS);
			mHandler.sendEmptyMessageDelayed(HANDLE_STOP_RECORD, RECORD_TIME_MAX - mMediaObject.getDuration());
		}

		mHandler.removeMessages(HANDLE_SHOW_TIPS);
		mHandler.sendEmptyMessage(HANDLE_SHOW_TIPS);
		
//		if (mMediaRecorder != null) {
//			MediaObject.MediaPart part = mMediaRecorder.startRecord();
//			if (part == null) {
//				return;
//			}
//
//			//如果使用MediaRecorderSystem，不能在中途切换前后摄像头，否则有问题
//			if (mMediaRecorder instanceof MediaRecorderSystem) {
//				mCameraSwitch.setVisibility(View.GONE);
//			}
//			mProgressView.setData(mMediaObject);
//		}
//		
//		record_import_img.setVisibility(View.GONE);
//		record_import_video.setVisibility(View.GONE);
//		mRecordDelete.setVisibility(View.VISIBLE);
//		mTitleNext.setVisibility(View.VISIBLE);
//		
//		mPressedStatus = true;
//
//		if (mHandler != null) {
//			mHandler.removeMessages(HANDLE_INVALIDATE_PROGRESS);
//			mHandler.sendEmptyMessage(HANDLE_INVALIDATE_PROGRESS);
//
//			mHandler.removeMessages(HANDLE_STOP_RECORD);
//			mHandler.sendEmptyMessageDelayed(HANDLE_STOP_RECORD, RECORD_TIME_MAX - mMediaObject.getDuration());
//		}
//		mCameraSwitch.setEnabled(false);
//		mRecordLed.setEnabled(false);
	}

	@Override
	public void onBackPressed() {
		if (mMediaObject != null && mMediaObject.getDuration() > 1) {
			//未转码
			new AlertDialog.Builder(this).setTitle(R.string.hint).setMessage(R.string.record_camera_exit_dialog_message).setNegativeButton(R.string.record_camera_cancel_dialog_yes, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					mMediaObject.delete();
					finish();
				}

			}).setPositiveButton(R.string.record_camera_cancel_dialog_no, null).setCancelable(false).show();
			return;
		}

		if (mMediaObject != null)
			mMediaObject.delete();
		finish();
	}

	/** 停止录制 */
	private void stopRecord() {
		mPressedStatus = false;

		if (mMediaRecorder != null) {
			mMediaRecorder.stopRecord();
		}

		mCameraSwitch.setEnabled(true);
		mRecordLed.setEnabled(true);

		mHandler.removeMessages(HANDLE_STOP_RECORD);
		checkStatus();
	}

	@Override
	public void onClick(View v) {
		final int id = v.getId();
		if (mHandler.hasMessages(HANDLE_STOP_RECORD)) {
			mHandler.removeMessages(HANDLE_STOP_RECORD);
		}

		//处理开启回删后其他点击操作
		if (id != R.id.record_import_del) {
			if (mMediaObject != null) {
				MediaObject.MediaPart part = mMediaObject.getCurrentPart();
				if (part != null) {
					if (part.remove) {
						part.remove = false;
						if (mProgressView != null)
							mProgressView.invalidate();
					}
				}
			}
		}

		switch (id) {
		case R.id.title_back:
			onBackPressed();
			break;
		case R.id.record_camera_switcher:// 前后摄像头切换
			if (mRecordLed.isChecked()) {
				if (mMediaRecorder != null) {
					mMediaRecorder.toggleFlashMode();
				}
				mRecordLed.setChecked(false);
			}

			if (mMediaRecorder != null) {
				mMediaRecorder.switchCamera();
			}

			if (mMediaRecorder.isFrontCamera()) {
				mRecordLed.setEnabled(false);
			} else {
				mRecordLed.setEnabled(true);
			}
			break;
		case R.id.record_camera_led://闪光灯
			//开启前置摄像头以后不支持开启闪光灯
			if (mMediaRecorder != null) {
				if (mMediaRecorder.isFrontCamera()) {
					return;
				}
			}

			if (mMediaRecorder != null) {
				mMediaRecorder.toggleFlashMode();
			}
			break;
		case R.id.record_import_comp:// 停止录制
			//stopRecord();
			startEncoding();
			break;
		case R.id.record_import_del:
			//取消回删
			if (mMediaObject != null) {
				MediaObject.MediaPart part = mMediaObject.getCurrentPart();
				if (part != null) {
					if (part.remove) {
						//mRebuild = true;
						part.remove = false;
						mMediaObject.removePart(part, true);
					} else {
						part.remove = true;
					}
				}
				if (mProgressView != null)
					mProgressView.invalidate();

				//检测按钮状态
				checkStatus();
			}
			break;
			
		case R.id.record_import_video:
			MediaChooser.setSelectionLimit(20);// 文件最大为20M
			Intent intent = new Intent();
			intent.setClass(MediaRecorderActivity.this, BucketHomeFragmentActivity.class);
			startActivityForResult(intent, 101);
			break;
		case R.id.record_import_img:
			Intent intent2 = new Intent();
			intent2.setAction(Intent.ACTION_GET_CONTENT);
			intent2.setType("image/*");
			startActivityForResult(Intent.createChooser(intent2,
					getString(R.string.record_camera_import_image_choose)),
					REQUEST_CODE_IMPORT_IMAGE);
		}
	}
	/** 是否是点击状态 */
	private volatile boolean mReleased, mStartEncoding;
	private void startEncoding() {
		//检测磁盘空间
		if (com.yixia.camera.util.FileUtils.showFileAvailable() < 200) {
			Toast.makeText(this, R.string.record_camera_check_available_faild, Toast.LENGTH_SHORT).show();
			return;
		}

		if (!isFinishing() && mMediaRecorder != null && mMediaObject != null && !mStartEncoding) {
			mStartEncoding = true;

			new AsyncTask<Void, Void, Boolean>() {

				@Override
				protected void onPreExecute() {
					super.onPreExecute();
					showProgress("", getString(R.string.record_camera_progress_message));
				}

				@Override
				protected Boolean doInBackground(Void... params) {
					boolean result = FFMpegUtils.videoTranscoding(mMediaObject, mMediaObject.getOutputTempVideoPath(), mWindowWidth, false);
					if (result && mMediaRecorder != null) {
						mMediaRecorder.release();
						mReleased = true;
					}
					return result;
				}

				@Override
				protected void onCancelled() {
					super.onCancelled();
					mStartEncoding = false;
				}

				@Override
				protected void onPostExecute(Boolean result) {
					super.onPostExecute(result);
					hideProgress();
					if (result) {
						/** 序列化保存数据 */
						if (saveMediaObject(mMediaObject)) {
							Intent intent = new Intent(MediaRecorderActivity.this, MediaPreviewActivity.class);
							intent.putExtra("obj", mMediaObject.getObjectFilePath());
							intent.putExtra("output", mMediaObject.getOutputTempVideoPath());
							startActivity(intent);
						} else {
							Toast.makeText(MediaRecorderActivity.this, R.string.record_camera_save_faild, Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(MediaRecorderActivity.this, R.string.record_video_transcoding_faild, Toast.LENGTH_SHORT).show();
					}
					mStartEncoding = false;
				}
			}.execute();
		}
	}

	/** 取消回删 */
	private boolean cancelDelete() {
		if (mMediaObject != null) {
			MediaObject.MediaPart part = mMediaObject.getCurrentPart();
			if (part != null && part.remove) {
				part.remove = false;

				if (mProgressView != null)
					mProgressView.invalidate();

				return true;
			}
		}
		return false;
	}

	/** 检查录制时间，显示/隐藏下一步按钮 */
	private int checkStatus() {
		int duration = 0;
//		if (!isFinishing() && mMediaObject != null) {
//			duration = mMediaObject.getDuration();
//			if (duration <= 0) {
//				mCameraSwitch.setVisibility(View.VISIBLE);
//				mRecordDelete.setVisibility(View.GONE);
//				mTitleNext.setVisibility(View.GONE);
//				record_import_img.setVisibility(View.VISIBLE);
//				record_import_video.setVisibility(View.VISIBLE);
//			}else if(duration < RECORD_TIME_MIN ){
//				mRecordDelete.setVisibility(View.VISIBLE);
//				mTitleNext.setVisibility(View.INVISIBLE);
//				record_import_img.setVisibility(View.GONE);
//				record_import_video.setVisibility(View.GONE);
//			}else if(duration >= RECORD_TIME_MIN){
//				mRecordDelete.setVisibility(View.VISIBLE);
//				mTitleNext.setVisibility(View.VISIBLE);
//				record_import_img.setVisibility(View.GONE);
//				record_import_video.setVisibility(View.GONE);
//			}
//		}
		return duration;
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HANDLE_INVALIDATE_PROGRESS:
				if (mMediaRecorder != null && !isFinishing()) {
					if (mProgressView != null)
						mProgressView.invalidate();
					if (mPressedStatus)
						sendEmptyMessageDelayed(0, 30);
				}
				break;
			case HANDLE_SHOW_TIPS:
				if (mMediaRecorder != null && !isFinishing()) {
					int duration = checkStatus();

					if (mPressedStatus) {
						if (duration < RECORD_TIME_MAX) {
							sendEmptyMessageDelayed(HANDLE_SHOW_TIPS, 200);
						} else {
							sendEmptyMessageDelayed(HANDLE_SHOW_TIPS, 500);
						}
					}
				}
				break;
			case HANDLE_STOP_RECORD:
				stopRecord();
				startEncoding();
				break;
			}
		}
	};

	@Override
	public void onEncodeStart() {
		showProgress("", getString(R.string.record_camera_progress_message));
	}

	@Override
	public void onEncodeProgress(int progress) {
		Logger.e("[MediaRecorderActivity]onEncodeProgress..." + progress);
	}

	/** 转码完成 */
	@Override
	public void onEncodeComplete() {
		hideProgress();
		
		Intent intent = new Intent(this, MediaPreviewActivity.class);
		Bundle bundle = getIntent().getExtras();
		if (bundle == null)
			bundle = new Bundle();
		bundle.putSerializable(CommonIntentExtra.EXTRA_MEDIA_OBJECT, mMediaObject);
		bundle.putString("output", mMediaObject.getOutputTempVideoPath());
		//bundle.putBoolean("Rebuild", mRebuild);
		intent.putExtras(bundle);
		startActivity(intent);
		//mRebuild = false;
		MediaRecorderActivity.this.finish();
	}

	/**
	 * 转码失败
	 * 检查sdcard是否可用，检查分块是否存在
	 */
	@Override
	public void onEncodeError() {
		hideProgress();
		Toast.makeText(this, R.string.record_video_transcoding_faild, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onVideoError(int what, int extra) {

	}

	@Override
	public void onAudioError(int what, String message) {

	}

	@Override
	public void onPrepared() {

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(requestCode==101 && resultCode==101){
			Bundle b=data.getExtras();  //data为B中回传的Intent
			String path = b.getString("back");
			if (saveMediaObject(mMediaObject)) {
				startActivityForResult(
						new Intent(
								this,
								ImportVideoActivity.class)
								.putExtra(
										"obj",
										mMediaObject
												.getObjectFilePath())
								.putExtra("path", path),
						REQUEST_CODE_IMPORT_VIDEO_EDIT);
			}
		}
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == REQUEST_CODE_IMPORT_VIDEO_EDIT) {
				mMediaObject = restoneMediaObject(mMediaObject.getObjectFilePath());
				checkStatus();
				mProgressView.setData(mMediaObject);
			} 
			// 处理导入图片和视频
			if (data != null && requestCode == REQUEST_CODE_IMPORT_IMAGE) {
				Uri uri = data.getData();
				if (uri != null) {
					String columnName = MediaStore.Images.Media.DATA;
					if (StringUtils.isNotEmpty(columnName)) {
						Cursor cursor = getContentResolver().query(uri, new String[] { columnName }, null, null, null);
						if (cursor != null) {
							String path = "";
							if (cursor.moveToNext()) {
								path = cursor.getString(0);
							}
							cursor.close();

							switch (requestCode) {
							case REQUEST_CODE_IMPORT_IMAGE:// 导入图片
								importImageOrVideo(path);
								break;
							}
						}
					}
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/** 导入图片或视频 */
	private void importImageOrVideo(final String path) {
		if (!isFinishing() && mMediaObject != null && StringUtils.isNotEmpty(path) && new File(path).exists()) {
			new AsyncTask<Void, Void, Boolean>() {

				@Override
				protected void onPreExecute() {
					super.onPreExecute();
					showProgress("", getString(R.string.record_camera_progress_message));
				}

				@Override
				protected Boolean doInBackground(Void... params) {
					MediaPart part = mMediaObject.buildMediaPart(path, 1000, MediaObject.MEDIA_PART_TYPE_IMPORT_IMAGE);
					if (part != null) {
						return FFMpegUtils.convertImage2Video(part);
					}
					return false;
				}

				@Override
				protected void onPostExecute(Boolean result) {
					super.onPostExecute(result);
					hideProgress();
					if (result) {
						mProgressView.setData(mMediaObject);
					} else {
						Toast.makeText(MediaRecorderActivity.this,
								R.string.record_video_transcoding_faild,
								Toast.LENGTH_SHORT).show();
					}
				}

			}.execute();
		}
	}
}
