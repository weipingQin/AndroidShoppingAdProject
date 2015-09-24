package com.www.goumei.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.www.goumei.R;
import com.www.goumei.adapter.PersonalFriendAdapter;
import com.www.goumei.adapter.WorksAdapter;
import com.www.goumei.bean.FriendData;
import com.www.goumei.bean.FriendsDataList;
import com.www.goumei.bean.UserData;
import com.www.goumei.bean.UserDataB;
import com.www.goumei.bean.Videos;
import com.www.goumei.bean.VideosDataList;
import com.www.goumei.http.req.ApiClientC;
import com.www.goumei.http.req.SP_ID;
import com.www.goumei.utils.Constant;
import com.www.goumei.utils.L;
import com.www.goumei.utils.StringUtils;
import com.www.goumei.views.CircleImageView;

public class HomePageActivity extends Activity implements OnClickListener {
	/** 存放作品的集合 */
	private static List<Videos> mWorks = new ArrayList<Videos>();
	/** 存放关注的集合 */
	private static List<FriendData> mFollows = new ArrayList<FriendData>();
	/** 存放粉丝的集合 */
	private static List<FriendData> mFans = new ArrayList<FriendData>();
	private CircleImageView headportrait;
	private ImageView verifiedIcon;
	private TextView last;
	private TextView name;
	private TextView signature;
	private TextView count;
	private ImageView sex;
	private ImageView homeType;
	private ImageView userDetails;
	private ImageView isFollow;
	private LinearLayout topBG;
	private PullToRefreshGridView ptrGV;
	private RelativeLayout rl_person_detail;
	private TextView works;
	private TextView follow;
	private TextView fans;
	private TextView personalTitle;
	private PullToRefreshListView followLv;
	private PullToRefreshListView fansLv;
	private RelativeLayout rl1, rl2, rl3;
	Activity pActivity;
	private PersonalFriendAdapter fansadapter;
	private PersonalFriendAdapter followadapter;
	private WorksAdapter worksAdapter;
	private int fansPageNo = 1;
	private int followPageNo = 1;
	private int worksPageNo = 1;
	private boolean fansHasMore = true;
	private boolean followHasMore = true;
	private boolean worksHasMore = true;
	private String userId = "1";
	private UserData user;
	public ImageLoader imageLoader;
	private RefreshMainFragmentReceiver refreshMainFragmentReceiver;
	private int index = 1;
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			super.handleMessage(msg);
			if (msg.what == 1) {
				// 在这里刷新界面适配器
				fansadapter.notifyDataSetChanged();
			}
			if (msg.what == 0) {

			}
		}
	};

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_home_page);
		last = (TextView) findViewById(R.id.personalLast);
		headportrait = (CircleImageView) findViewById(R.id.homeIcon);
		verifiedIcon = (ImageView) findViewById(R.id.verifiedIcon);
		name = (TextView) findViewById(R.id.homeName);
		signature = (TextView) findViewById(R.id.homeSignature);
		personalTitle = (TextView) findViewById(R.id.personalTitle);
		rl_person_detail=(RelativeLayout)findViewById(R.id.rl_person_detail);
		count = (TextView) findViewById(R.id.tv_count);
		sex = (ImageView) findViewById(R.id.homeSex);
		homeType = (ImageView) findViewById(R.id.homeType);
		isFollow = (ImageView) findViewById(R.id.isFollow);
		userDetails = (ImageView) findViewById(R.id.userDetails);
		ptrGV = (PullToRefreshGridView) findViewById(R.id.worksGV);
		followLv = (PullToRefreshListView) findViewById(R.id.followLv);
		fansLv = (PullToRefreshListView) findViewById(R.id.fansLv);
		topBG = (LinearLayout) findViewById(R.id.top_bg);
		works = (TextView) findViewById(R.id.works);
		follow = (TextView) findViewById(R.id.follow);
		fans = (TextView) findViewById(R.id.fans);
		rl1 = (RelativeLayout) findViewById(R.id.rl1);
		rl2 = (RelativeLayout) findViewById(R.id.rl2);
		rl3 = (RelativeLayout) findViewById(R.id.rl3);
		fansLv.setMode(Mode.BOTH);
		ptrGV.setMode(Mode.BOTH);
		followLv.setMode(Mode.BOTH);
		mFans = new ArrayList<FriendData>();
		mFollows = new ArrayList<FriendData>();
		mWorks = new ArrayList<Videos>();
		fansLv.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				// TODO Auto-generated method stub
				fansHasMore = true;
				fansLv.setMode(Mode.BOTH);
				fansPageNo = 1;
				mFans.clear();
				new GetFansTask().execute("");
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				// TODO Auto-generated method stub
				if (fansHasMore) {
					new GetFansTask().execute("");
				} else {
					fansLv.onRefreshComplete();
				}

			}
		});
		followLv.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				// TODO Auto-generated method stub
				followHasMore = true;
				followLv.setMode(Mode.BOTH);
				followPageNo = 1;
				mFollows.clear();
				new GetFollowTask().execute("");
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				// TODO Auto-generated method stub
				if (followHasMore) {
					new GetFollowTask().execute("");
				} else {
					followLv.onRefreshComplete();
				}

			}
		});
		ptrGV.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				// TODO Auto-generated method stub
				worksHasMore = true;
				ptrGV.setMode(Mode.BOTH);
				worksPageNo = 1;
				mWorks.clear();
				new GetVideosTask().execute("");
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				// TODO Auto-generated method stub
				if (worksHasMore) {
					new GetVideosTask().execute("");
				} else {
					ptrGV.onRefreshComplete();
				}

			}
		});
		ptrGV.getRefreshableView().setNumColumns(2);
		userId = getIntent().getStringExtra(Constant.OTHER_ID);

		fansadapter = new PersonalFriendAdapter(this, mFans);
		followadapter = new PersonalFriendAdapter(this, mFollows);
		worksAdapter = new WorksAdapter(this, mWorks);
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(this));
		setListeners();
		refreshMainFragmentReceiver = new RefreshMainFragmentReceiver();
		IntentFilter filter0 = new IntentFilter();
		filter0.addAction(Constant.ACTION_ATTENTION_STATUS);
		filter0.addAction(Constant.ACTION_ADD_FANS);
		registerReceiver(refreshMainFragmentReceiver, filter0);

		index = getIntent().getIntExtra(Constant.HOME_PAGE_INDEX, 1);
		switch (index) {
		case 1:
			works.performClick();
			break;
		case 2:
			follow.performClick();
			break;
		case 3:
			fans.performClick();
			break;

		default:
			works.performClick();
			break;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		new UserInfoTask().execute("");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(refreshMainFragmentReceiver);
	}

	protected void setListeners() {
		last.setOnClickListener(this);
		userDetails.setOnClickListener(this);
		works.setOnClickListener(this);
		follow.setOnClickListener(this);
		fans.setOnClickListener(this);
		rl_person_detail.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.personalLast:
			finish();
			break;

		case R.id.userDetails:
		case R.id.rl_person_detail:
			Intent intent=new Intent(HomePageActivity.this,OtherPersonaldataActivity.class);
			intent.putExtra(Constant.OTHER_ID, userId);
			startActivity(intent);
			break;
		// 作品
		case R.id.works:
			// ptrGV.setVisibility(View.VISIBLE);
			// followLv.setVisibility(View.GONE);
			// fansLv.setVisibility(View.GONE);
			topBG.setBackgroundResource(R.drawable.backgourd_orange);
			rl1.setVisibility(View.VISIBLE);
			rl2.setVisibility(View.GONE);
			rl3.setVisibility(View.GONE);
			if (mWorks == null || mWorks.size() <= 0) {
				new GetVideosTask().execute("");
			}
			break;
		// 关注
		case R.id.follow:
			topBG.setBackgroundResource(R.drawable.backgourd_orange2);
			// ptrGV.setVisibility(View.GONE);
			// followLv.setVisibility(View.VISIBLE);
			// fansLv.setVisibility(View.GONE);
			if (mFollows == null || mFollows.size() <= 0) {
				new GetFollowTask().execute("");
			}
			rl1.setVisibility(View.GONE);
			rl2.setVisibility(View.VISIBLE);
			rl3.setVisibility(View.GONE);
			break;
		// 粉丝
		case R.id.fans:
			topBG.setBackgroundResource(R.drawable.backgourd_orange3);
			// ptrGV.setVisibility(View.GONE);
			// followLv.setVisibility(View.GONE);
			// fansLv.setVisibility(View.VISIBLE);
			if (mFans == null || mFans.size() <= 0) {
				new GetFansTask().execute("");
			}
			rl1.setVisibility(View.GONE);
			rl2.setVisibility(View.GONE);
			rl3.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}

	class GetFansTask extends AsyncTask<String, String, String> {
		FriendsDataList videoList;

		@Override
		protected String doInBackground(String... arg0) {
			videoList = ApiClientC.getFans(userId, fansPageNo);

			return null;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			fansLv.onRefreshComplete();
			if (videoList != null) {
				if (videoList.getModels() != null
						&& videoList.getModels().size() < Constant.PAGE_SIZE) {
					fansHasMore = false;
					fansLv.setMode(Mode.PULL_FROM_START);
				}
				fansPageNo++;
				mFans.addAll(videoList.getModels());
				fansadapter = new PersonalFriendAdapter(HomePageActivity.this,
						mFans);
				fansLv.setAdapter(fansadapter);
			}

		}
	}

	class GetFollowTask extends AsyncTask<String, String, String> {
		FriendsDataList videoList;

		@Override
		protected String doInBackground(String... arg0) {
			videoList = ApiClientC.getAttention(userId, followPageNo);

			return null;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			followLv.onRefreshComplete();
			if (videoList != null) {
				if (videoList.getModels() != null
						&& videoList.getModels().size() < Constant.PAGE_SIZE) {
					followHasMore = false;
					followLv.setMode(Mode.PULL_FROM_START);
				}
				if(followPageNo==1){
					mFollows.clear();
				}
				followPageNo++;
				mFollows.addAll(videoList.getModels());
				followadapter = new PersonalFriendAdapter(
						HomePageActivity.this, mFollows);
				followLv.setAdapter(followadapter);
			}

		}
	}

	class GetVideosTask extends AsyncTask<String, String, String> {
		VideosDataList videoList;

		@Override
		protected String doInBackground(String... arg0) {
			videoList = ApiClientC.getVideosById(userId, worksPageNo);

			return null;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			ptrGV.onRefreshComplete();
			if (videoList != null) {
				if (videoList.getModels() != null
						&& videoList.getModels().size() < Constant.PAGE_SIZE) {
					worksHasMore = false;
					ptrGV.setMode(Mode.PULL_FROM_START);
				}
				worksPageNo++;
				//由于服务器端对是否发布未过滤，现在在手机端对数据进行【是否已发布】的过滤处理
				for(int i=0;i<videoList.getModels().size();i++){
					if(videoList.getModels().get(i).isPublish == true){
						mWorks.add(videoList.getModels().get(i));
					}
				}
				//mWorks.addAll(videoList.getModels());
				worksAdapter = new WorksAdapter(HomePageActivity.this, mWorks);
				ptrGV.setAdapter(worksAdapter);
			}

		}
	}

	public class RefreshMainFragmentReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, final Intent intent) {

			new Thread(new Runnable() {
				@Override
				public void run() {

					int index = intent.getIntExtra(Constant.INTENT_INDEX, -1);
					String status = intent
							.getStringExtra(Constant.INTENT_STATUS);
					L.e("......." + status + index);
					if (index >= 0) {
						if (Constant.INTENT_STATUS_TRUE.equals(status)) {
							mFans.get(index).setIsAttention(1);
						} else {
							mFans.get(index).setIsAttention(0);
						}

						Message message = new Message();
						message.what = 1;
						mHandler.sendMessage(message);
					}

				}
			}).start();
		}

	}

	class UserInfoTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... arg0) {
			UserDataB udb = ApiClientC.getUserInfo(userId);
			if (udb != null) {
				user = udb.getModel();
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (user != null) {
				if (!StringUtils.isEmpty(user.getDisplayName())){
					name.setText(user.getDisplayName());
					personalTitle.setText(user.getDisplayName() +"的主页");
					//isFollow
					SharedPreferences sharedPreferences = getSharedPreferences("wujay", Context.MODE_PRIVATE); //私有数据
					String local_user_name = sharedPreferences.getString("local_user_name", null);
					if(null != local_user_name && local_user_name.equals(user.getDisplayName())){
						isFollow.setVisibility(View.INVISIBLE);
					}else{
						isFollow.setVisibility(View.VISIBLE);
					}
				}
				String headurl = user.getHeadsculpture();
				int videosCount = user.getVideosCount();
				int fansCount = user.getFansCount();
				int attentionsCount = user.getAttentionsCount();
				int certificationState = user.getCertificationState();
				int sexIndex = user.getSex();
				int pariseMeCount = user.getVideoPraiseCount();
				int isAttentionEachOther = user.getIsAttentionEachOther();
				int isAttention = user.getIsAttention();
				if (isAttentionEachOther == 1) {
					isFollow.setImageResource(R.drawable.isfollow1);
					isFollow.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							new CancelAttentionTask(SP_ID.id, userId)
									.execute("");

						}
					});
				} else {
					if (isAttention == 1) {
						isFollow.setImageResource(R.drawable.isfollow2);
						isFollow.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								new CancelAttentionTask(SP_ID.id, userId)
										.execute("");

							}
						});
					} else {
						isFollow.setImageResource(R.drawable.isfollow3);
						isFollow.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								new AddAttentionTask(SP_ID.id, userId)
										.execute("");

							}
						});
					}
				}
				count.setText(pariseMeCount + "");
				if (user.getIndividualitySignature() != null)
					signature.setText(user.getIndividualitySignature());
				if (sexIndex == 1) {
					sex.setImageResource(R.drawable.icon_man);
				} else {
					sex.setImageResource(R.drawable.icon_female);
				}
				if (!StringUtils.isEmpty(headurl)) {
					imageLoader.displayImage(headurl, headportrait);
				}

				works.setText(videosCount + "\n作品");
				fans.setText(fansCount + "\n粉丝");
				follow.setText(attentionsCount + "\n关注");
				switch (certificationState) {
				case 0:
					homeType.setImageResource(R.drawable.icon_me_not_verified);
					break;
				case 1:
					homeType.setImageResource(R.drawable.icon_renzhenged);
					verifiedIcon.setVisibility(View.VISIBLE);
					
					break;
				case 2:
					homeType.setImageResource(R.drawable.icon_renzhenging);
					break;
				default:
					break;
				}
			}
		}
	}

	class AddAttentionTask extends AsyncTask<String, String, String> {
		String code;
		String id;
		String fansId;
		int index;

		public AddAttentionTask(String id, String fansId) {
			this.id = id;
			this.fansId = fansId;
		}

		@Override
		protected String doInBackground(String... arg0) {
			code = ApiClientC.addAttention(id, fansId);

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			L.e("code:::::::::::::::::::::::::" + code);
			if (code != null && code.equals("1")) {

				new UserInfoTask().execute("");
			}

		}
	}
	class CancelAttentionTask extends AsyncTask<String, String, String> {
		String code;
		String id;
		String fansId;
		int index;
		
		public CancelAttentionTask(String id, String fansId) {
			this.id = id;
			this.fansId = fansId;
		}
		
		@Override
		protected String doInBackground(String... arg0) {
			code = ApiClientC.cancelAttention(id, fansId);
			
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			L.e("code:::::::::::::::::::::::::" + code);
			if (code != null && code.equals("1")) {
				
				new UserInfoTask().execute("");
			}
			
		}
	}
	
}
