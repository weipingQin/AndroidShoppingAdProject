package com.yixia.camera.demo.ui.record;

import android.annotation.TargetApi;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.TextView;

import com.www.goumei.R;
import com.www.goumei.activity.EditActivity;
import com.www.goumei.bean.SaveVideoResult;
import com.www.goumei.http.ApiClient;
import com.www.goumei.utils.ProcessUtil;
import com.www.goumei.utils.UIHelper;
import com.yixia.camera.demo.ui.BaseActivity;
import com.yixia.camera.demo.ui.widget.SurfaceVideoView;
import com.yixia.camera.util.DeviceUtils;
import com.yixia.camera.util.StringUtils;

/**
 * 通用单独播放界面
 * 
 *
 */
public class VideoPlayerActivity extends BaseActivity implements SurfaceVideoView.OnPlayStateListener, OnErrorListener, OnPreparedListener, OnClickListener, OnCompletionListener, OnInfoListener {

	/** 播放控件 */
	private SurfaceVideoView mVideoView;
	/** 暂停按钮 */
	private View mPlayerStatus;
	private View mLoading;
	/** 播放路径 */
	private String mPath;
	/** 是否需要回复播放 */
	private boolean mNeedResume;
	private TextView titleLeft;
	private TextView titleRight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 防止锁屏
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		mPath = getIntent().getStringExtra("path");
		if (StringUtils.isEmpty(mPath)) {
			finish();
			return;
		}

		setContentView(R.layout.activity_video_player);
		mVideoView = (SurfaceVideoView) findViewById(R.id.videoview);
		mPlayerStatus = findViewById(R.id.play_status);
		mLoading = findViewById(R.id.loading);
		titleLeft = (TextView) findViewById(R.id.titleLeft);
		titleRight = (TextView) findViewById(R.id.titleRight);
		
		titleLeft.setOnClickListener(this);
		titleRight.setOnClickListener(this);

		mVideoView.setOnPreparedListener(this);
		mVideoView.setOnPlayStateListener(this);
		mVideoView.setOnErrorListener(this);
		mVideoView.setOnClickListener(this);
		mVideoView.setOnInfoListener(this);
		mVideoView.setOnCompletionListener(this);

		mVideoView.getLayoutParams().height = DeviceUtils.getScreenWidth(this);
		mVideoView.setVideoPath(mPath);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mVideoView != null && mNeedResume) {
			mNeedResume = false;
			if (mVideoView.isRelease())
				mVideoView.reOpen();
			else
				mVideoView.start();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mVideoView != null) {
			if (mVideoView.isPlaying()) {
				mNeedResume = true;
				mVideoView.pause();
			}
		}
	}

	@Override
	protected void onDestroy() {
		if (mVideoView != null) {
			mVideoView.release();
			mVideoView = null;
		}
		super.onDestroy();
	}
	
	
	private static final int SAVE_VIDEO = 0;
	private SaveVideoResult saveVideoResult;
	
	public class DialogHelper extends AsyncTask<Integer, Void, Integer> {
		public DialogHelper() {
		}

		@Override
		protected void onPreExecute() {
			ProcessUtil.showProgressDialog(VideoPlayerActivity.this, "上传视频中...");
		}

		@Override
		protected Integer doInBackground(Integer... params) {
			switch (params[0]) {
			case SAVE_VIDEO:
				saveVideoResult = ApiClient.SaveVideo(mPath);
				return SAVE_VIDEO;
			default:
				return -1;
			}

		}

		@Override
		protected void onPostExecute(Integer result) {
			switch (result) {
			case SAVE_VIDEO:
				ProcessUtil.dismissProgressdialog();
				if (saveVideoResult != null) {
					String savePath = saveVideoResult.getSavePath();
					if (!StringUtils.isEmpty(savePath)) {
						UIHelper.showMsg(VideoPlayerActivity.this, "上传视频成功");
						Intent intent = new Intent();
						intent.setClass(VideoPlayerActivity.this, EditActivity.class);
						intent.putExtra("saveVideoPath", savePath);
						intent.putExtra("localVideoPath", mPath);
						startActivity(intent);
						VideoPlayerActivity.this.finish();
						
					} else {
						UIHelper.showMsg(VideoPlayerActivity.this, "上传视频失败");
					}
				} else {
					UIHelper.showMsg(VideoPlayerActivity.this, "上传视频失败");
				}
				break;

			default:
				break;
			}
			super.onPostExecute(result);
		}
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		mVideoView.setVolume(SurfaceVideoView.getSystemVolumn(this));
		mVideoView.start();
//		new Handler().postDelayed(new Runnable() {
//
//			@SuppressWarnings("deprecation")
//			@Override
//			public void run() {
//				if (DeviceUtils.hasJellyBean()) {
//					mVideoView.setBackground(null);
//				} else {
//					mVideoView.setBackgroundDrawable(null);
//				}
//			}
//		}, 300);
		mLoading.setVisibility(View.GONE);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		switch (event.getKeyCode()) {//跟随系统音量走
		case KeyEvent.KEYCODE_VOLUME_DOWN:
		case KeyEvent.KEYCODE_VOLUME_UP:
			mVideoView.dispatchKeyEvent(this, event);
			break;
		}
		return super.dispatchKeyEvent(event);
	}

	@Override
	public void onStateChanged(boolean isPlaying) {
		mPlayerStatus.setVisibility(isPlaying ? View.GONE : View.VISIBLE);
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		if (!isFinishing()) {
			//播放失败
		}
		finish();
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.videoview:
			if (mVideoView.isPlaying())
				mVideoView.pause();
			else
				mVideoView.start();
			break;
		case R.id.titleLeft:
			VideoPlayerActivity.this.finish();
			break;
		case R.id.titleRight:
			
			DialogHelper dialogHelper = new DialogHelper();
			dialogHelper.execute(SAVE_VIDEO);
			
			break;
		}
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		if (!isFinishing())
			mVideoView.reOpen();
	}

  @SuppressWarnings("deprecation")
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
  @Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		switch (what) {
		case MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
			//音频和视频数据不正确
			break;
		case MediaPlayer.MEDIA_INFO_BUFFERING_START:
			if (!isFinishing())
				mVideoView.pause();
			break;
		case MediaPlayer.MEDIA_INFO_BUFFERING_END:
			if (!isFinishing())
				mVideoView.start();
			break;
		case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
			if (DeviceUtils.hasJellyBean()) {
				mVideoView.setBackground(null);
			} else {
				mVideoView.setBackgroundDrawable(null);
			}
			break;
		}
		return false;
	}

}
