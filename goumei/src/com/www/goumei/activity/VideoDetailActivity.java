package com.www.goumei.activity;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.www.goumei.R;
import com.www.goumei.adapter.VideoCommentAdapter;
import com.www.goumei.bean.CommentData;
import com.www.goumei.bean.CommentDataList;
import com.www.goumei.bean.Videos;
import com.www.goumei.emoji.EmoteInputView;
import com.www.goumei.emoji.EmoticonsEditText;
import com.www.goumei.http.req.ApiClientC;
import com.www.goumei.http.req.SP_ID;
import com.www.goumei.utils.Constant;
import com.www.goumei.utils.L;
import com.www.goumei.utils.NetState;
import com.www.goumei.utils.ProcessUtil;
import com.www.goumei.utils.SPutil;
import com.www.goumei.utils.StringUtils;
import com.www.goumei.utils.Util;
import com.www.goumei.views.CircleImageView;
import com.www.goumei.views.RoundImageView;
import com.yixia.camera.demo.utils.ToastUtils;

public class VideoDetailActivity extends Activity implements OnClickListener,
		MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener,
		MediaPlayer.OnErrorListener, OnBufferingUpdateListener {
	private TextView last;
	private TextView share;
	private CircleImageView detailscommentIcon;
	private ImageView detailscommentTypr;
	private RelativeLayout rl_home_page;
	private TextView shopingName;
	private TextView commentTime;
	private ImageView ivIsFollow;
	// private VideoView video;
	private TextView saleTitle;
	private TextView tvComment;
	private TextView tvLove;
	private TextView tvLove_opt;

	private TextView tvShoping;
	private RoundImageView rivIcon;
	private TextView tv_title;
	private TextView tv_xh;
	private ImageView expression;
	private EmoticonsEditText inputContent;
	private TextView tvSendout;
	private Button start;
	private ImageView iv_cover;
	String form = "";
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private RelativeLayout commentRL, loveRL, shoppingRL;
	private RelativeLayout rl_comment, rl_love, rl_shopping;
	private LinearLayout mBGLL;
	private LinearLayout mBottomLL;
	private LinearLayout ll_opt;
	private PullToRefreshListView mListView;
	private VideoCommentAdapter adapter;
	private List<CommentData> list;
	boolean hasMore = true;
	private int pageNo = 1;
	private int index = 0;
	private Videos videoBean = null;
	private boolean isReply = false;
	private String replyUserID = "";
	private boolean isCreate = true;
	protected EmoteInputView mInputView;
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
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fragment_detailcomment);
		
		SharedPreferences sharedPreferences = this.getSharedPreferences("wujay", Context.MODE_PRIVATE); //私有数据
		Editor editor = sharedPreferences.edit();//获取编辑器
		editor.putBoolean("iflag", false);
		editor.commit();//提交修改
		
		ShareSDK.initSDK(this);
		ShareSDK.setConnTimeout(20000);
		ShareSDK.setReadTimeout(20000);
		initView();
		initData();
		addListener();
		boolean isAutoPay = (Boolean) SPutil.get(this, Constant.SP_WIFI_PLAY, false);
		if (isAutoPay && NetState.isConnected(this) && NetState.isWifi(this)) {
			start.performClick();
		}

	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		initData();
	}

	@SuppressWarnings({ "deprecation", "unchecked", "rawtypes" })
	private void initView() {
		View v = LayoutInflater.from(this).inflate(
				R.layout.view_video_detail_top, null);
		last = (TextView) findViewById(R.id.detailsLast);
		share = (TextView) findViewById(R.id.detailsShare);
		detailscommentIcon = (CircleImageView) v
				.findViewById(R.id.detailscommentIcon);
		rl_home_page = (RelativeLayout) v.findViewById(R.id.rl_home_page);
		shopingName = (TextView) v.findViewById(R.id.shopingName);
		commentTime = (TextView) v.findViewById(R.id.commentTime);
		ivIsFollow = (ImageView) v.findViewById(R.id.ivIsFollow);
		mBGLL = (LinearLayout) v.findViewById(R.id.ll_bg);
		detailscommentTypr = (ImageView) v
				.findViewById(R.id.detailscommentTypr);
		// video = (VideoView) view.findViewById(R.id.shopingVideo);
		saleTitle = (TextView) v.findViewById(R.id.saleTitle);
		tvComment = (TextView) v.findViewById(R.id.tvComment);
		tvLove = (TextView) v.findViewById(R.id.tvLove);
		tvLove_opt = (TextView) findViewById(R.id.tvLove);
		tvShoping = (TextView) v.findViewById(R.id.tvShoping);
		tv_title = (TextView) v.findViewById(R.id.tv_title);
		expression = (ImageView) findViewById(R.id.ivExpression);

		inputContent = (EmoticonsEditText) findViewById(R.id.InputContent);
		mInputView = (EmoteInputView) findViewById(R.id.chat_eiv_inputview);
		mInputView.setEditText(inputContent);
		tvSendout = (TextView) findViewById(R.id.tvSendout);
		surfaceView = (SurfaceView) v.findViewById(R.id.video);
		start = (Button) v.findViewById(R.id.btnStart);
		iv_cover = (ImageView) v.findViewById(R.id.iv_cover);
		progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
		commentRL = (RelativeLayout) v.findViewById(R.id.rl_comment);
		loveRL = (RelativeLayout) v.findViewById(R.id.rl_love);
		shoppingRL = (RelativeLayout) v.findViewById(R.id.rl_shopping);
		rl_comment = (RelativeLayout) findViewById(R.id.rl_comment_opt);
		rl_love = (RelativeLayout) findViewById(R.id.rl_love_opt);
		rl_shopping = (RelativeLayout) findViewById(R.id.rl_shopping_opt);
		mBottomLL = (LinearLayout) findViewById(R.id.bottomLL);
		ll_opt = (LinearLayout) findViewById(R.id.ll_opt);
		mListView = (PullToRefreshListView) findViewById(R.id.lv_content);
		mListView.setMode(Mode.BOTH);
		mListView.getRefreshableView().addHeaderView(v);
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(this));

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.test)
				.showImageForEmptyUri(R.drawable.test)
				.showImageOnFail(R.drawable.test).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		// 设置surfaceHolder
		surfaceHolder = surfaceView.getHolder();
		// 设置Holder类型,该类型表示surfaceView自己不管理缓存区,虽然提示过时，但最好还是要设置
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		// 设置surface回调
		surfaceHolder.addCallback(new SurfaceCallback());

		list = new ArrayList<CommentData>();
		mListView.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				// TODO Auto-generated method stub
				hasMore = true;
				mListView.setMode(Mode.BOTH);
				pageNo = 1;
				list.clear();
				start.setVisibility(View.VISIBLE);
				new GetComments().execute("");
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				// TODO Auto-generated method stub
				if (hasMore) {
					new GetComments().execute("");
				} else {
					mListView.onRefreshComplete();
				}

			}
		});
		adapter = new VideoCommentAdapter(this, list);
		mListView.setAdapter(adapter);

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

	/**
	 * 播放视频
	 */
	public void playVideo() {
		// 初始化MediaPlayer
		mediaPlayer = getMediaPlayer(VideoDetailActivity.this);
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
			if (!videoBean.getUrl().equals("")) {
				mediaPlayer.setDataSource(videoBean.getUrl());
				mediaPlayer.prepareAsync();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "加载视频错误！", Toast.LENGTH_LONG).show();
		}
		// boolean isAutoPay = (Boolean) SPutil.get(this, Constant.SP_WIFI_PLAY,
		// false);
		// if (isAutoPay && NetState.isConnected(this) && NetState.isWifi(this))
		// {
		// start.performClick();
		// }
		// isCreate = false;
	}

	private void addListener() {
		last.setOnClickListener(this);
		share.setOnClickListener(this);
		tvComment.setClickable(false);
		tvLove.setClickable(false);
		tvShoping.setClickable(false);
		start.setOnClickListener(this);
		commentRL.setOnClickListener(this);
		loveRL.setOnClickListener(this);
		shoppingRL.setOnClickListener(this);
		rl_comment.setOnClickListener(this);
		rl_love.setOnClickListener(this);
		rl_shopping.setOnClickListener(this);
		mBGLL.setOnClickListener(this);
		tvSendout.setOnClickListener(this);
		expression.setOnClickListener(this);
		detailscommentIcon.setOnClickListener(this);
		rl_home_page.setOnClickListener(this);
		mListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				if (arg1 <= 1) {
					mBottomLL.setVisibility(View.VISIBLE);
					ll_opt.setVisibility(View.GONE);
				} else {
					mBottomLL.setVisibility(View.GONE);
					ll_opt.setVisibility(View.VISIBLE);
				}
			}
		});

	}

	private void initData() {

		videoBean = (Videos) getIntent().getSerializableExtra(Constant.VIDEO);
		int reID = getIntent().getIntExtra(Constant.REPLYUSERID, 0);
		if (reID != 0) {
			replyUserID = reID + "";
			inputContent.setHint("@" + reID);
		}
		if (videoBean != null) {
			shopingName.setText(videoBean.getUserDisplayName());
			commentTime.setText(Util.getDateYYYYMMDDHHMM(videoBean.getPublishTime()));
			saleTitle.setText(videoBean.getTitle());
			tvLove.setText(videoBean.getPraiseCount() + "");
			imageLoader.displayImage(videoBean.getUserHeadsculpture(),
					detailscommentIcon, options);
			ivIsFollow.setImageResource(R.drawable.icon_right);
			// if(videoBean.getIsAttention()==1){
			// ivIsFollow.setImageResource(R.drawable.icon_right);
			// }else{
			// ivIsFollow.setImageResource(R.drawable.isfollow3);
			// }
			if (videoBean.getUserCertificationState() == Constant.UserCertificationState_V) {
				detailscommentTypr.setVisibility(View.VISIBLE);
			} else {
				detailscommentTypr.setVisibility(View.INVISIBLE);
			}
			if (videoBean.IsPraise == 1) {
				Drawable img_on, img_off;
				Resources res = getResources();
				img_off = res.getDrawable(R.drawable.icon_loves);
				// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
				img_off.setBounds(0, 0, img_off.getMinimumWidth(),
						img_off.getMinimumHeight());
				tvLove.setCompoundDrawables(img_off, null, null, null); // 设置左图标
				tvLove_opt.setCompoundDrawables(null, img_off, null, null);
			} else {
				Drawable img_on, img_off;
				Resources res = getResources();
				img_off = res.getDrawable(R.drawable.icon_loves_no);
				// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
				img_off.setBounds(0, 0, img_off.getMinimumWidth(),
						img_off.getMinimumHeight());
				tvLove.setCompoundDrawables(img_off, null, null, null); // 设置左图标
				tvLove_opt.setCompoundDrawables(null, img_off, null, null);
			}
			imageLoader.displayImage(videoBean.getCover(), iv_cover, options);
		}

		new GetComments().execute("");

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_comment_opt:
			mListView.getRefreshableView().setSelection(0);
			break;
		// 返回上一步
		case R.id.detailsLast:
			finish();
			break;
		// 分享
		case R.id.detailsShare:
			showShare(true, null, false);
			break;
		case R.id.rl_home_page:
			Intent homeIntent = new Intent(this, HomePageActivity.class);
			homeIntent.putExtra(Constant.OTHER_ID, videoBean.getUserID() + "");
			startActivity(homeIntent);
			break;
		// 评论
		case R.id.rl_comment:
			changeView(0);
			break;
		// 喜欢
		case R.id.rl_love:
		case R.id.rl_love_opt:
			changeView(1);
			if (videoBean != null) {
				if (videoBean.IsPraise == Constant.ISPRAISE_YES) {
					new AddPraiseTask().execute("");
				} else {
					new CanclePraiseTask().execute("");
				}
			}

			break;
		// 购物
		case R.id.rl_shopping:
		case R.id.rl_shopping_opt:
			// changeView(2);
			// 如果是有淘宝的连接，可以跳转到淘宝
			try {
				if (videoBean.Link != null
						&& !TextUtils.isEmpty(videoBean.Link)) {

					Intent intent = new Intent(Intent.ACTION_VIEW);
					if (videoBean.Link != null) {
						intent.setData(Uri.parse(videoBean.Link));
						startActivity(intent);
					}
				} else {
					ToastUtils.showToast(VideoDetailActivity.this, "店铺link为空");
				}
			} catch (Exception e) {

			}
			break;
		// 表情
		case R.id.ivExpression:
			inputContent.requestFocus();
			if (mInputView.isShown()) {
				// hideKeyBoard();
				showKeyBoard();
			} else {
				hideKeyBoard();
				mInputView.setVisibility(View.VISIBLE);
			}
			break;
		// 发送
		case R.id.tvSendout:
			if (checkComment()) {
				new AddCommentTask().execute("");
			}
			break;

		case R.id.btnStart:
			if (videoBean != null) {

				playVideo();
				start.setVisibility(View.GONE);
				iv_cover.setVisibility(View.GONE);
				progressBar.setVisibility(View.VISIBLE);
				// player = new MediaPlayer();
				// player.reset();
				// // 设置声音效果// 设置多媒体流类型
				// player.setAudioStreamType(AudioManager.STREAM_MUSIC);
				// player.start();
				// // 设置用于展示mediaPlayer的容器
				// player.setDisplay(surfaceHolder);
				// try {
				// player.setDataSource(videoBean.getUrl());
				// // player.prepare();
				// player.prepareAsync();
				// } catch (Exception e) {
				// e.printStackTrace();
				// }
				// start.setVisibility(View.GONE);
				// iv_cover.setVisibility(View.GONE);
				// player.setOnCompletionListener(new OnCompletionListener() {
				//
				// @Override
				// public void onCompletion(MediaPlayer arg0) {
				// start.setVisibility(View.VISIBLE);
				// }
				// });
			} else {

			}
			break;
		case R.id.detailscommentIcon:
			Intent homeIntenticon = new Intent(this, HomePageActivity.class);
			homeIntenticon.putExtra(Constant.OTHER_ID, videoBean.getUserID()
					+ "");
			startActivity(homeIntenticon);
			break;
		default:
			break;
		}
	}

	private void changeView(int i) {
		if (i != index) {
			if (i == 0) {
				tvComment.setTextColor(getResources().getColor(
						R.color.color_ff6600));
				tvShoping.setTextColor(getResources().getColor(
						R.color.color_black));
				mBGLL.setBackgroundResource(R.drawable.bg_video_detail_left);
			} else if (i == 1) {

			} else if (i == 2) {
				tvComment.setTextColor(getResources().getColor(
						R.color.color_black));
				tvShoping.setTextColor(getResources().getColor(
						R.color.color_ff6600));
				mBGLL.setBackgroundResource(R.drawable.bg_video_detial_right);
			}
			index = i;
		}
	}

	private boolean checkComment() {
		if (StringUtils.isEmpty(inputContent.getText().toString())) {
			ToastUtils.showToast(this, "请输入评论内容");
			return false;
		}
		return true;
	}

	class GetComments extends AsyncTask<String, String, String> {
		CommentDataList videoList;

		@Override
		protected String doInBackground(String... arg0) {
			if (videoBean != null) {
				videoList = ApiClientC.getCommentByVideoId(videoBean.getID()
						+ "", pageNo);
			}

			return null;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			mListView.onRefreshComplete();
			if (videoList != null) {
				if (videoList.getModels() != null
						&& videoList.getModels().size() < Constant.PAGE_SIZE) {
					hasMore = false;
					mListView.setMode(Mode.PULL_FROM_START);
				}
				if (pageNo == 1) {
					list.clear();
				}

				int index = list.size();
				list.addAll(videoList.getModels());
				if (adapter == null) {
					adapter = new VideoCommentAdapter(VideoDetailActivity.this,
							list);
					mListView.setAdapter(adapter);
				} else {
					adapter.notifyDataSetChanged();
				}

				if (index - 1 >= 0 && index < list.size() && pageNo != 1) {
					mListView.getRefreshableView().setSelection(index - 1);
				}
				pageNo++;
			}

		}
	}

	/**
	 * 添加评论
	 * 
	 * @author json
	 *
	 */
	class AddCommentTask extends AsyncTask<String, String, String> {
		String code;
		int index;

		public AddCommentTask() {
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			ProcessUtil
					.showProgressDialog(VideoDetailActivity.this, "添加评论中...");
		}

		@Override
		protected String doInBackground(String... arg0) {
			if (videoBean != null) {
				code = ApiClientC
						.addComment(videoBean.getID() + "", inputContent
								.getText().toString(), isReply, replyUserID);
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			ProcessUtil.dismissProgressdialog();
			L.e("code:::::::::::::::::::::::::" + code);
			if (code != null && code.equals("1")) {
				// if(list!=null){
				//
				// }else{
				// list=new ArrayList<CommentData>();
				// }
				// CommentData cd=new CommentData();
				// cd.setContent(inputContent.getText().toString());
				// cd.setCommonetTime("刚刚");
				//
				// list.add(0,cd);
				// inputContent.setText("");
				// if(adapter!=null){
				// adapter.notifyDataSetChanged();
				// }else{
				// adapter=new VideoCommentAdapter(VideoDetailActivity.this,
				// list);
				// adapter.notifyDataSetChanged();
				// }
				inputContent.setText("");
				pageNo = 1;
				new GetComments().execute("");
			} else {
				ToastUtils.showToast(VideoDetailActivity.this, "评论失败");
			}

		}
	}

	/**
	 * 加赞
	 * 
	 * @author json
	 *
	 */
	class AddPraiseTask extends AsyncTask<String, String, String> {
		String code;

		public AddPraiseTask() {
		}

		@Override
		protected String doInBackground(String... arg0) {
			if (videoBean != null)
				code = ApiClientC.addPraise(videoBean.getID() + "", SP_ID.id
						+ "");

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			L.e("code:::::::::::::::::::::::::" + code);
			if (code != null && code.equals("1")) {
				if (videoBean.getIsPraise() == 1) {
					videoBean.setIsPraise(0);
					videoBean.setPraiseCount(videoBean.getPraiseCount() - 1);
					tvLove.setText(videoBean.getPraiseCount() + "");
				} else {
					videoBean.setIsPraise(Constant.ISPRAISE_YES);
					videoBean.setPraiseCount(videoBean.getPraiseCount() + 1);
					tvLove.setText(videoBean.getPraiseCount() + "");
				}
				sendPraiseBroadcast();
				if (videoBean.IsPraise == 1) {
					Drawable img_on, img_off;
					Resources res = getResources();
					img_off = res.getDrawable(R.drawable.icon_loves);
					// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
					img_off.setBounds(0, 0, img_off.getMinimumWidth(),
							img_off.getMinimumHeight());
					tvLove.setCompoundDrawables(img_off, null, null, null); // 设置左图标
					tvLove_opt.setCompoundDrawables(null, img_off, null, null);

				} else {
					Drawable img_on, img_off;
					Resources res = getResources();
					img_off = res.getDrawable(R.drawable.icon_loves_no);
					// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
					img_off.setBounds(0, 0, img_off.getMinimumWidth(),
							img_off.getMinimumHeight());
					tvLove.setCompoundDrawables(img_off, null, null, null); // 设置左图标
					tvLove_opt.setCompoundDrawables(null, img_off, null, null);
				}
			}

		}
	}

	/**
	 * 取消赞
	 * 
	 * @author json
	 *
	 */
	class CanclePraiseTask extends AsyncTask<String, String, String> {
		String code;

		public CanclePraiseTask() {
		}

		@Override
		protected String doInBackground(String... arg0) {
			if (videoBean != null)
				code = ApiClientC.canclePraise(videoBean.getID() + "", SP_ID.id
						+ "");

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			L.e("code:::::::::::::::::::::::::" + code);
			if (code != null && code.equals("1")) {
				if (videoBean.getIsPraise() == 1) {
					videoBean.setIsPraise(0);
					videoBean.setPraiseCount(videoBean.getPraiseCount() - 1);
					tvLove.setText(videoBean.getPraiseCount() + "");
				} else {
					videoBean.setIsPraise(Constant.ISPRAISE_YES);
					videoBean.setPraiseCount(videoBean.getPraiseCount() + 1);
					tvLove.setText(videoBean.getPraiseCount() + "");
				}
				sendPraiseBroadcast();
				if (videoBean.IsPraise == 1) {
					Drawable img_on, img_off;
					Resources res = getResources();
					img_off = res.getDrawable(R.drawable.icon_loves);
					// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
					img_off.setBounds(0, 0, img_off.getMinimumWidth(),
							img_off.getMinimumHeight());
					tvLove.setCompoundDrawables(img_off, null, null, null); // 设置左图标
					tvLove_opt.setCompoundDrawables(null, img_off, null, null);

				} else {
					Drawable img_on, img_off;
					Resources res = getResources();
					img_off = res.getDrawable(R.drawable.icon_loves_no);
					// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
					img_off.setBounds(0, 0, img_off.getMinimumWidth(),
							img_off.getMinimumHeight());
					tvLove.setCompoundDrawables(img_off, null, null, null); // 设置左图标
					tvLove_opt.setCompoundDrawables(null, img_off, null, null);
				}
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
		Context context = this;
		final OnekeyShare oks = new OnekeyShare();

		// oks.setAddress("12345678901");
		oks.setTitle("美构视频");
		if (videoBean != null) {
			oks.setImageUrl(videoBean.getCover());
			oks.setTitleUrl(videoBean.getUrl());
			oks.setUrl(videoBean.getUrl());
		}

		oks.setText("美构视频。。。");
		if (captureView) {
			oks.setViewToShare(null);
		} else {
			// oks.setImagePath(CustomShareFieldsPage.getString("imagePath",
			// MainActivity.TEST_IMAGE));
			// oks.setImageUrl(CustomShareFieldsPage.getString("imageUrl",
			// MainActivity.TEST_IMAGE_URL));
			// oks.setImageArray(new String[]{MainActivity.TEST_IMAGE,
			// MainActivity.TEST_IMAGE_URL});
		}

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

	// emoji

	protected void showKeyBoard() {
		if (mInputView.isShown()) {
			mInputView.setVisibility(View.GONE);
		}
		inputContent.requestFocus();
		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
				.showSoftInput(inputContent, 0);
	}

	protected void hideKeyBoard() {
		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(this.getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
		if (mInputView.isShown()) {
			mInputView.setVisibility(View.GONE);
		} else {
			finish();
		}
	}

	private void sendPraiseBroadcast() {
		Intent intent = new Intent();
		intent.setAction(Constant.ACTION_PRAISE_STATUS);
		intent.putExtra(Constant.INTENT_VIDEO_ID, videoBean.getID());
		intent.putExtra(Constant.INTENT_PRAISE_STATUS, videoBean.getIsPraise());
		this.sendBroadcast(intent);
	}

	@Override
	public void onBufferingUpdate(MediaPlayer arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		switch (what) {
		case MediaPlayer.MEDIA_ERROR_UNKNOWN:
			Toast.makeText(this, "MEDIA_ERROR_UNKNOWN", Toast.LENGTH_SHORT)
					.show();
			break;
		case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
			Toast.makeText(this, "MEDIA_ERROR_SERVER_DIED", Toast.LENGTH_SHORT)
					.show();
			break;
		default:
			break;
		}

		switch (extra) {
		case MediaPlayer.MEDIA_ERROR_IO:
			Toast.makeText(this, "MEDIA_ERROR_IO", Toast.LENGTH_SHORT).show();
			break;
		case MediaPlayer.MEDIA_ERROR_MALFORMED:
			Toast.makeText(this, "MEDIA_ERROR_MALFORMED", Toast.LENGTH_SHORT)
					.show();
			break;
		case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
			Toast.makeText(this,
					"MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK",
					Toast.LENGTH_SHORT).show();
			break;
		case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
			Toast.makeText(this, "MEDIA_ERROR_TIMED_OUT", Toast.LENGTH_SHORT)
					.show();
			break;
		case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
			Toast.makeText(this, "MEDIA_ERROR_UNSUPPORTED", Toast.LENGTH_SHORT)
					.show();
			break;
		}
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
		
		SharedPreferences sharedPreferences = getSharedPreferences("wujay", Context.MODE_PRIVATE);
		boolean iflag = sharedPreferences.getBoolean("iflag", false);
		if(iflag){
			start.setVisibility(View.VISIBLE);
			//iv_cover.setVisibility(View.VISIBLE);
		}else{
			Editor editor = sharedPreferences.edit();//获取编辑器
			editor.putBoolean("iflag", true);
			editor.commit();//提交修改
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
}
