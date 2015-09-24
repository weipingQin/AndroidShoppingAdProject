package com.www.goumei.fragment;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.www.goumei.R;
import com.www.goumei.activity.AttentionAct;
import com.www.goumei.activity.MainHomeAct;
import com.www.goumei.bean.ThemeDetails;
import com.www.goumei.utils.Constant;
import com.www.goumei.utils.NetState;
import com.www.goumei.utils.SPutil;
import com.www.goumei.utils.StringUtils;
import com.www.goumei.views.CircleImageView;
import com.www.goumei.views.RoundImageView;

/**
 * 详情-评论页
 * 
 * @author Eric.Chen
 * 
 */
public class DetailsCommentFragment extends BaseFragment implements
		OnClickListener, MediaPlayer.OnCompletionListener,
		MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
		OnBufferingUpdateListener {
	Activity pActivity;
	private TextView last;
	private TextView share;
	private CircleImageView detailscommentIcon;
	private TextView shopingName;
	private TextView commentTime;
	private ImageView ivIsFollow;
	// private VideoView video;
	private com.www.goumei.emoji.EmoticonsTextView saleTitle;
	private TextView tvComment;
	private TextView tvLove;
	private TextView tvShoping;
	private RoundImageView rivIcon;
	private TextView tv_title;
	private TextView tv_xh;
	private ImageView expression;
	private EditText inputContent;
	private TextView tvSendout;
	private ThemeDetails bean;
	private Button start;
	String form = "";
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private ProgressBar progressBar;
	/**
    *
    */
	private SurfaceView surfaceView;

	/**
	 * surfaceView播放控制
	 */
	private SurfaceHolder surfaceHolder;
	/**
	 * 播放视频
	 */
	private MediaPlayer mediaPlayer;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_detailcomment,
				container, false);

		SharedPreferences sharedPreferences = getActivity()
				.getSharedPreferences("wujay", Context.MODE_PRIVATE); // 私有数据
		Editor editor = sharedPreferences.edit();// 获取编辑器
		editor.putBoolean("iflag", false);
		editor.commit();// 提交修改

		initView(view);
		initData();
		addListener();
		boolean isAutoPay = (Boolean) SPutil.get(getActivity(), Constant.SP_WIFI_PLAY, false);
		if (isAutoPay && NetState.isConnected((getActivity())) && NetState.isWifi(getActivity())) {
			start.performClick();
		}
		return view;
	}

	private void initView(View view) {
		last = (TextView) view.findViewById(R.id.detailsLast);
		share = (TextView) view.findViewById(R.id.detailsShare);
		detailscommentIcon = (CircleImageView) view
				.findViewById(R.id.detailscommentIcon);
		shopingName = (TextView) view.findViewById(R.id.shopingName);
		commentTime = (TextView) view.findViewById(R.id.commentTime);
		ivIsFollow = (ImageView) view.findViewById(R.id.ivIsFollow);
		// video = (VideoView) view.findViewById(R.id.shopingVideo);
		saleTitle = (com.www.goumei.emoji.EmoticonsTextView) view
				.findViewById(R.id.saleTitle);
		tvComment = (TextView) view.findViewById(R.id.tvComment);
		tvLove = (TextView) view.findViewById(R.id.tvLove);
		tvShoping = (TextView) view.findViewById(R.id.tvShoping);
		// rivIcon = (RoundImageView) view.findViewById(R.id.rivIcon);
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		// tv_xh = (TextView) view.findViewById(R.id.tv_xh);
		expression = (ImageView) view.findViewById(R.id.ivExpression);
		inputContent = (EditText) view.findViewById(R.id.InputContent);
		tvSendout = (TextView) view.findViewById(R.id.tvSendout);
		surfaceView = (SurfaceView) view.findViewById(R.id.video);
		start = (Button) view.findViewById(R.id.btnStart);
		progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

		// 设置surfaceHolder
		surfaceHolder = surfaceView.getHolder();
		// 设置Holder类型,该类型表示surfaceView自己不管理缓存区,虽然提示过时，但最好还是要设置
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		// 设置surface回调
		surfaceHolder.addCallback(new SurfaceCallback());
	}

	private void addListener() {
		last.setOnClickListener(this);
		share.setOnClickListener(this);
		tvComment.setOnClickListener(this);
		tvLove.setOnClickListener(this);
		tvShoping.setOnClickListener(this);
		start.setOnClickListener(this);
	}

	// SurfaceView的callBack
	private class SurfaceCallback implements SurfaceHolder.Callback {
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {

		}

		public void surfaceCreated(SurfaceHolder holder) {
			// surfaceView被创建
			// 设置播放资源
			// playVideo();
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
			// surfaceView销毁,同时销毁mediaPlayer
			if (null != mediaPlayer) {
				mediaPlayer.release();
				mediaPlayer = null;
			}

		}

	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.pActivity = activity;
	}

	private void initData() {
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.test)
				.showImageForEmptyUri(R.drawable.test)
				.showImageOnFail(R.drawable.test).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		Bundle bundle = getArguments();
		if (bundle != null) {
			bean = (ThemeDetails) bundle.getSerializable("Bean");
			form = bundle.getString("form");
			shopingName.setText(bean.getUserDisplayName());
			commentTime.setText(bean.getPublishTime());
			saleTitle.setText(bean.getTitle());
			tvLove.setText(bean.getHits() + "");
			imageLoader.displayImage(bean.getCover(), detailscommentIcon,
					options);
		}
	}

	/**
	 * 播放视频
	 */
	public void playVideo() {
		// 初始化MediaPlayer
		mediaPlayer = getMediaPlayer(getActivity());
		// 重置mediaPaly,建议在初始滑mediaplay立即调用。
		mediaPlayer.reset();
		// player.release();
		// 设置声音效果
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		// 设置播放完成监听
		mediaPlayer.setOnCompletionListener(this);
		// 设置媒体加载完成以后回调函数。
		mediaPlayer.setOnPreparedListener(this);
		// 错误监听回调函数
		mediaPlayer.setOnErrorListener(this);
		// 设置缓存变化监听
		mediaPlayer.setOnBufferingUpdateListener(this);

		// 设置显示视频显示在SurfaceView上
		try {
			if (!bean.getUrl().equals("")) {
				mediaPlayer.setDataSource(bean.getUrl());
				mediaPlayer.prepareAsync();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("rawtypes")
	MediaPlayer getMediaPlayer(Context context) {

		MediaPlayer mediaplayer = new MediaPlayer();

		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.KITKAT) {
			return mediaplayer;
		}

		try {
			Class<?> cMediaTimeProvider = Class
					.forName("android.media.MediaTimeProvider");
			Class<?> cSubtitleController = Class
					.forName("android.media.SubtitleController");
			Class<?> iSubtitleControllerAnchor = Class
					.forName("android.media.SubtitleController$Anchor");
			Class<?> iSubtitleControllerListener = Class
					.forName("android.media.SubtitleController$Listener");

			Constructor constructor = cSubtitleController
					.getConstructor(new Class[] { Context.class,
							cMediaTimeProvider, iSubtitleControllerListener });

			Object subtitleInstance = constructor.newInstance(context, null,
					null);

			Field f = cSubtitleController.getDeclaredField("mHandler");

			f.setAccessible(true);
			try {
				f.set(subtitleInstance, new Handler());
			} catch (IllegalAccessException e) {
				return mediaplayer;
			} finally {
				f.setAccessible(false);
			}

			Method setsubtitleanchor = mediaplayer.getClass().getMethod(
					"setSubtitleAnchor", cSubtitleController,
					iSubtitleControllerAnchor);

			setsubtitleanchor.invoke(mediaplayer, subtitleInstance, null);
			// Log.e("", "subtitle is setted :p");
		} catch (Exception e) {
		}

		return mediaplayer;
	}

	@Override
	public void onLeave() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReLoad(Intent paramIntent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 返回上一步
		case R.id.detailsLast:
			if (!StringUtils.isEmpty(form)) {
				if (form.equals("HomeFragment")) {
					((MainHomeAct) pActivity).goBackAndShowPreView(null);

				} else if (form.equals("AttentionFragment")) {
					((AttentionAct) pActivity).goBackAndShowPreView(null);

				} else if (form.equals("AttentionFragment")) {
					((AttentionAct) pActivity).goBackAndShowPreView(null);
				}
			}
			break;
		// 分享
		case R.id.detailsShare:

			break;
		// 评论
		case R.id.tvComment:

			break;
		// 喜欢
		case R.id.tvLove:

			break;
		// 购物
		case R.id.tvShoping:

			break;
		// 表情
		case R.id.ivExpression:

			break;
		// 发送
		case R.id.tvSendout:

			break;

		case R.id.btnStart:
			playVideo();
			start.setVisibility(View.GONE);
			// iv_cover.setVisibility(View.GONE);
			progressBar.setVisibility(View.VISIBLE);
		default:
			break;
		}
	}

	@Override
	public void onBufferingUpdate(MediaPlayer arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onPrepared(MediaPlayer arg0) {
		// 当视频加载完毕以后，隐藏加载进度条
		progressBar.setVisibility(View.GONE);
		// 播放视频
		mediaPlayer.start();
		// // 设置显示到屏幕
		mediaPlayer.setDisplay(surfaceHolder);
		// // 设置surfaceView保持在屏幕上
		mediaPlayer.setScreenOnWhilePlaying(true);
		surfaceHolder.setKeepScreenOn(true);
	}

	@Override
	public void onCompletion(MediaPlayer arg0) {

		SharedPreferences sharedPreferences = getActivity()
				.getSharedPreferences("wujay", Context.MODE_PRIVATE);
		boolean iflag = sharedPreferences.getBoolean("iflag", false);
		if (iflag) {
			start.setVisibility(View.VISIBLE);
			// iv_cover.setVisibility(View.VISIBLE);
		} else {
			Editor editor = sharedPreferences.edit();// 获取编辑器
			editor.putBoolean("iflag", true);
			editor.commit();// 提交修改
		}

	}

}
