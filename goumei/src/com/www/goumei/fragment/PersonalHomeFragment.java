//package com.www.goumei.fragment;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.app.Activity;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.graphics.Bitmap;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.View.OnClickListener;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.handmark.pulltorefresh.library.PullToRefreshBase;
//import com.handmark.pulltorefresh.library.PullToRefreshGridView;
//import com.handmark.pulltorefresh.library.PullToRefreshListView;
//import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
//import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
//import com.www.goumei.R;
//import com.www.goumei.activity.MainMyAct;
//import com.www.goumei.adapter.FansAdapter;
//import com.www.goumei.adapter.PersonalFriendAdapter;
//import com.www.goumei.adapter.WorksAdapter;
//import com.www.goumei.bean.FansBean;
//import com.www.goumei.bean.FriendData;
//import com.www.goumei.bean.FriendsDataList;
//import com.www.goumei.bean.UserData;
//import com.www.goumei.bean.UserDataB;
//import com.www.goumei.bean.Videos;
//import com.www.goumei.bean.VideosDataList;
//import com.www.goumei.fragment.FanFragment.GetVideosTask;
//import com.www.goumei.fragment.FanFragment.RefreshMainFragmentReceiver;
//import com.www.goumei.http.req.ApiClientC;
//import com.www.goumei.http.req.SP_ID;
//import com.www.goumei.utils.Constant;
//import com.www.goumei.utils.L;
//import com.www.goumei.utils.StringUtils;
//import com.www.goumei.views.CircleImageView;
//
//
//public class PersonalHomeFragment extends BaseFragment implements   OnClickListener{
//	/**存放作品的集合*/
//	private static List<Videos> mWorks=new ArrayList<Videos>();
//	/**存放关注的集合*/
//	private static List<FriendData> mFollows=new ArrayList<FriendData>();
//	/**存放粉丝的集合*/
//	private static List<FriendData> mFans=new ArrayList<FriendData>();
//	private CircleImageView headportrait;
//	private TextView last;
//	private TextView name;
//	private TextView signature;
//	private TextView count;
//	private ImageView sex;
//	private ImageView homeType;
//	private ImageView userDetails;
//	private ImageView isFollow;
//	private LinearLayout topBG;
//	private PullToRefreshGridView ptrGV;
//	private TextView works;
//	private TextView follow;
//	private TextView fans;
//	private PullToRefreshListView followLv;
//	private PullToRefreshListView fansLv;
//	private RelativeLayout rl1,rl2,rl3;
//	Activity  pActivity;
//	private PersonalFriendAdapter fansadapter;
//	private PersonalFriendAdapter followadapter;
//	private WorksAdapter worksAdapter;
//	private int fansPageNo=1;
//	private int followPageNo=1;
//	private int worksPageNo=1;
//	private boolean fansHasMore=true;
//	private boolean followHasMore=true;
//	private boolean worksHasMore=true;
//	private String userId="1";
//	private UserData user;
//	public ImageLoader imageLoader; 
//	private RefreshMainFragmentReceiver refreshMainFragmentReceiver;
//	Handler mHandler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//
//			super.handleMessage(msg);
//			if (msg.what == 1) {
//				// 在这里刷新界面适配器
//				fansadapter.notifyDataSetChanged();
//			}
//			if (msg.what == 0) {
//
//			}
//		}
//	};
//  @Override
//public View onCreateView(LayoutInflater inflater, ViewGroup container,
//		Bundle savedInstanceState) {
//	// TODO Auto-generated method stub
//	  View view=inflater.inflate(R.layout.activity_personalhome, container,false);
//	  last = (TextView)view. findViewById(R.id.personalLast);
//		headportrait = (CircleImageView) view.findViewById(R.id.homeIcon);
//		name = (TextView) view. findViewById(R.id.homeName);
//		signature = (TextView) view.findViewById(R.id.homeSignature);
//		count = (TextView) view. findViewById(R.id.tv_count);
//		sex = (ImageView)view.findViewById(R.id.homeSex);
//		homeType = (ImageView) view.findViewById(R.id.homeType);
//		isFollow = (ImageView) view.findViewById(R.id.isFollow);
//		userDetails = (ImageView)view. findViewById(R.id.userDetails);
//		ptrGV = (PullToRefreshGridView)view. findViewById(R.id.worksGV);
//		followLv = (PullToRefreshListView) view.findViewById(R.id.followLv);
//		fansLv = (PullToRefreshListView)view.findViewById(R.id.fansLv);
//		topBG=(LinearLayout)view.findViewById(R.id.top_bg);
//		works = (TextView)view.findViewById(R.id.works);
//		follow = (TextView) view.findViewById(R.id.follow);
//		fans = (TextView) view. findViewById(R.id.fans);
//		rl1=(RelativeLayout)view.findViewById(R.id.rl1);
//		rl2=(RelativeLayout)view.findViewById(R.id.rl2);
//		rl3=(RelativeLayout)view.findViewById(R.id.rl3);
//		fansLv.setMode(Mode.BOTH);
//		ptrGV.setMode(Mode.BOTH);
//		followLv.setMode(Mode.BOTH);
//		mFans=new ArrayList<FriendData>();
//		mFollows=new ArrayList<FriendData>();
//		mWorks=new ArrayList<Videos>();
//		fansLv.setOnRefreshListener(new OnRefreshListener2() {
//
//			@Override
//			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
//				// TODO Auto-generated method stub
//				fansHasMore = true;
//				fansLv.setMode(Mode.BOTH);
//				fansPageNo = 1;
//				mFans.clear();
//				new GetFansTask().execute("");
//			}
//
//			@Override
//			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
//				// TODO Auto-generated method stub
//				if (fansHasMore) {
//					new GetFansTask().execute("");
//				} else {
//					fansLv.onRefreshComplete();
//				}
//
//			}
//		});
//		followLv.setOnRefreshListener(new OnRefreshListener2() {
//			
//			@Override
//			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
//				// TODO Auto-generated method stub
//				followHasMore = true;
//				followLv.setMode(Mode.BOTH);
//				followPageNo = 1;
//				mFollows.clear();
//				new GetFollowTask().execute("");
//			}
//			
//			@Override
//			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
//				// TODO Auto-generated method stub
//				if (followHasMore) {
//					new GetFollowTask().execute("");
//				} else {
//					followLv.onRefreshComplete();
//				}
//				
//			}
//		});
//		ptrGV.setOnRefreshListener(new OnRefreshListener2() {
//			
//			@Override
//			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
//				// TODO Auto-generated method stub
//				worksHasMore = true;
//				ptrGV.setMode(Mode.BOTH);
//				worksPageNo = 1;
//				mWorks.clear();
//				new GetVideosTask().execute("");
//			}
//			
//			@Override
//			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
//				// TODO Auto-generated method stub
//				if (worksHasMore) {
//					new GetVideosTask().execute("");
//				} else {
//					ptrGV.onRefreshComplete();
//				}
//				
//			}
//		});
//		ptrGV.getRefreshableView().setNumColumns(2);
//		userId=getArguments().getString(Constant.OTHER_ID);
//		new GetVideosTask().execute("");
//	  return view;
//}
//  @Override
//public void onActivityCreated(Bundle savedInstanceState) {
//	// TODO Auto-generated method stub
//	  fansadapter=new PersonalFriendAdapter(getActivity(), mFans);
//	  followadapter=new PersonalFriendAdapter(getActivity(), mFollows);
//	  worksAdapter=new WorksAdapter(getActivity(), mWorks);
//	  imageLoader = ImageLoader.getInstance();
//		imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
//	  setListeners();
//	super.onActivityCreated(savedInstanceState);
//	
//}
//  @Override
//public void onAttach(Activity activity) {
//	// TODO Auto-generated method stub
//	super.onAttach(activity);
//	this.pActivity=activity;
//}
//	
//
//  	@Override
//  	public void onStart() {
//  		super.onStart();
//  		refreshMainFragmentReceiver = new RefreshMainFragmentReceiver();
//		IntentFilter filter0 = new IntentFilter();
//		filter0.addAction(Constant.ACTION_ATTENTION_STATUS);
//		filter0.addAction(Constant.ACTION_ADD_FANS);
//		getActivity().registerReceiver(refreshMainFragmentReceiver, filter0);
//
//  	}
//	@Override
//	public void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//		setData();
//		new UserInfoTask().execute("");
//	}
//	@Override
//	public void onDestroy() {
//		super.onDestroy();
//		getActivity().unregisterReceiver(refreshMainFragmentReceiver);
//	}
//	/**
//	 * 设置数据
//	 */
//	private void setData() {
//		//分别请求
//	}
//
//
//	protected void setListeners() {
//		last.setOnClickListener(this);
//		userDetails.setOnClickListener(this);
//		works.setOnClickListener(this);
//		follow.setOnClickListener(this);
//		fans.setOnClickListener(this);
//	}
//
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.personalLast:
//			((MainMyAct)pActivity).goBackAndShowPreView(null);
//			break;
//
//		case R.id.userDetails:
//			
//			break;
//		//作品
//		case R.id.works:
////			ptrGV.setVisibility(View.VISIBLE);
////			followLv.setVisibility(View.GONE);
////			fansLv.setVisibility(View.GONE);
//			topBG.setBackgroundResource(R.drawable.backgourd_orange);
//			rl1.setVisibility(View.VISIBLE);
//			rl2.setVisibility(View.GONE);
//			rl3.setVisibility(View.GONE);
//			if(mWorks==null||mWorks.size()<=0){
//				new GetVideosTask().execute("");
//			}
//			break;
//		//关注
//		case R.id.follow:
//			topBG.setBackgroundResource(R.drawable.backgourd_orange2);
////			ptrGV.setVisibility(View.GONE);
////			followLv.setVisibility(View.VISIBLE);
////			fansLv.setVisibility(View.GONE);
//			if(mFollows==null||mFollows.size()<=0){
//				new GetFollowTask().execute("");
//			}
//			rl1.setVisibility(View.GONE);
//			rl2.setVisibility(View.VISIBLE);
//			rl3.setVisibility(View.GONE);
//			break;
//		//粉丝
//		case R.id.fans:
//			topBG.setBackgroundResource(R.drawable.backgourd_orange3);
////			ptrGV.setVisibility(View.GONE);
////			followLv.setVisibility(View.GONE);
////			fansLv.setVisibility(View.VISIBLE);
//			if(mFans==null||mFans.size()<=0){
//				new GetFansTask().execute("");
//			}
//			rl1.setVisibility(View.GONE);
//			rl2.setVisibility(View.GONE);
//			rl3.setVisibility(View.VISIBLE);
//			break;
//		default:
//			break;
//		}
//	}
//
//	@Override
//	public void onLeave() {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void onReLoad(Intent paramIntent) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	class GetFansTask extends AsyncTask<String, String, String> {
//		FriendsDataList videoList;
//
//		@Override
//		protected String doInBackground(String... arg0) {
//			videoList = ApiClientC.getFans(userId, fansPageNo);
//
//			return null;
//		}
//
//		@Override
//		protected void onPreExecute() {
//			// TODO Auto-generated method stub
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//			super.onPostExecute(result);
//			fansLv.onRefreshComplete();
//			if (videoList != null) {
//				if (videoList.getModels() != null
//						&& videoList.getModels().size() < Constant.PAGE_SIZE) {
//					fansHasMore = false;
//					fansLv.setMode(Mode.PULL_FROM_START);
//				}
//				fansPageNo++;
//				mFans.addAll(videoList.getModels());
//				fansadapter = new PersonalFriendAdapter(getActivity(), mFans);
//				fansLv.setAdapter(fansadapter);
//			}
//
//		}
//	}
//	class GetFollowTask extends AsyncTask<String, String, String> {
//		FriendsDataList videoList;
//		
//		@Override
//		protected String doInBackground(String... arg0) {
//			videoList = ApiClientC.getAttention(userId, followPageNo);
//			
//			return null;
//		}
//		
//		@Override
//		protected void onPreExecute() {
//			// TODO Auto-generated method stub
//		}
//		
//		@Override
//		protected void onPostExecute(String result) {
//			super.onPostExecute(result);
//			followLv.onRefreshComplete();
//			if (videoList != null) {
//				if (videoList.getModels() != null
//						&& videoList.getModels().size() < Constant.PAGE_SIZE) {
//					followHasMore = false;
//					followLv.setMode(Mode.PULL_FROM_START);
//				}
//				followPageNo++;
//				mFollows.addAll(videoList.getModels());
//				followadapter = new PersonalFriendAdapter(getActivity(), mFollows);
//				followLv.setAdapter(followadapter);
//			}
//			
//		}
//	}
//	class GetVideosTask extends AsyncTask<String, String, String> {
//		VideosDataList videoList;
//		
//		@Override
//		protected String doInBackground(String... arg0) {
//			videoList = ApiClientC.getVideosById(userId, worksPageNo);
//			
//			return null;
//		}
//		
//		@Override
//		protected void onPreExecute() {
//			// TODO Auto-generated method stub
//		}
//		
//		@Override
//		protected void onPostExecute(String result) {
//			super.onPostExecute(result);
//			ptrGV.onRefreshComplete();
//			if (videoList != null) {
//				if (videoList.getModels() != null
//						&& videoList.getModels().size() < Constant.PAGE_SIZE) {
//					worksHasMore = false;
//					ptrGV.setMode(Mode.PULL_FROM_START);
//				}
//				worksPageNo++;
//				mWorks.addAll(videoList.getModels());
//				worksAdapter= new WorksAdapter(getActivity(), mWorks);
//				ptrGV.setAdapter(worksAdapter);
//			}
//			
//		}
//	}
//
//	public class RefreshMainFragmentReceiver extends BroadcastReceiver {
//		@Override
//		public void onReceive(Context context, final Intent intent) {
//
//			new Thread(new Runnable() {
//				@Override
//				public void run() {
//
//					int index = intent.getIntExtra(Constant.INTENT_INDEX, -1);
//					String status = intent
//							.getStringExtra(Constant.INTENT_STATUS);
//					L.e("......." + status + index);
//					if (index >= 0) {
//						if (Constant.INTENT_STATUS_TRUE.equals(status)) {
//							mFans.get(index).setIsAttention(1);
//						} else {
//							mFans.get(index).setIsAttention(0);
//						}
//
//						Message message = new Message();
//						message.what = 1;
//						mHandler.sendMessage(message);
//					}
//
//				}
//			}).start();
//		}
//
//	}
//	class UserInfoTask extends AsyncTask<String, String, String>{
//
//		@Override
//		protected String doInBackground(String... arg0) {
//				UserDataB udb=ApiClientC.getUserInfo(userId);
//				if(udb!=null){
//					user=udb.getModel();
//				}
//			
//			return null;
//		}
//		@Override
//		protected void onPostExecute(String result) {
//			super.onPostExecute(result);
//			if(user!=null){
//				if(!StringUtils.isEmpty(user.getDisplayName()))
//				name.setText(user.getDisplayName());
//				String headurl=user.getHeadsculpture();
//				int videosCount=user.getVideosCount();
//				int fansCount=user.getFansCount();
//				int attentionsCount=user.getAttentionsCount();
//				int certificationState =user.getCertificationState();
//				int sexIndex=user.getSex();
//				int pariseMeCount=user.getVideoPraiseCount();
//				int isAttentionEachOther=user.getIsAttentionEachOther();
//				int isAttention=user.getIsAttention();
//				if(isAttentionEachOther==1){
//					isFollow.setImageResource(R.drawable.isfollow1);
//				}else{
//					if(isAttention==1){
//						isFollow.setImageResource(R.drawable.isfollow2);
//					}else{
//						isFollow.setImageResource(R.drawable.isfollow3);
//						isFollow.setOnClickListener(new OnClickListener() {
//							
//							@Override
//							public void onClick(View arg0) {
//								new AddAttentionTask(SP_ID.id, userId).execute("");
//								
//							}
//						});
//					}
//				}
//				count.setText(pariseMeCount+"");
//				if(user.getIndividualitySignature()!=null)
//				signature.setText(user.getIndividualitySignature());
//				if(sexIndex==1){
//					sex.setImageResource(R.drawable.icon_man);
//				}else{
//					sex.setImageResource(R.drawable.icon_female);
//				}
//				if(!StringUtils.isEmpty(headurl)){
//					imageLoader.displayImage(headurl, headportrait);
//				}
//				
//				works.setText(videosCount+"\n作品");
//				fans.setText(fansCount+"\n粉丝");
//				follow.setText(attentionsCount+"\n关注");
//				switch (certificationState) {
//				case 0:
//					homeType.setImageResource(R.drawable.icon_me_not_verified);
//					break;
//				case 1:
//					homeType.setImageResource(R.drawable.icon_renzhenging);
//					break;
//				case 2:
//					homeType.setImageResource(R.drawable.icon_renzhenged);
//					break;
//				default:
//					break;
//				}
//			}
//		}
//	}
//	
//	class AddAttentionTask extends AsyncTask<String, String, String>{
//		String code;
//		String id;
//		String fansId;
//		int index;
//		public AddAttentionTask(String id,String fansId){
//			this.id=id;
//			this.fansId=fansId;
//		}
//		@Override
//		protected String doInBackground(String... arg0) {
//			code=ApiClientC.addAttention(id, fansId);
//			
//			return null;
//		}
//		@Override
//		protected void onPostExecute(String result) {
//			super.onPostExecute(result);
//			L.e("code:::::::::::::::::::::::::"+code);
//			if(code!=null &&code.equals("1")){
//				
//				new UserInfoTask().execute("");
//			}
//			
//		}
//	}
//}
