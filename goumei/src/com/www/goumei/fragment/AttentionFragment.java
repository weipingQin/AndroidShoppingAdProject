package com.www.goumei.fragment;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.www.goumei.R;
import com.www.goumei.adapter.VideoAdapter;
import com.www.goumei.bean.ThemeDetails;
import com.www.goumei.bean.Videos;
import com.www.goumei.bean.VideosDataList;
import com.www.goumei.fragment.FanFragment.RefreshMainFragmentReceiver;
import com.www.goumei.http.ApiClient;
import com.www.goumei.utils.Constant;
import com.www.goumei.utils.ProcessUtil;
import com.www.goumei.utils.UIHelper;
import com.www.goumei.views.GridViewForScrollView;
//import com.android.volley.VolleyError;
//import com.sina.weibo.sdk.utils.LogUtil;
//import com.www.wsgou.R;
/*import com.www.wsgou.activity.MainActivity;
 import com.www.wsgou.adapter.DisplayAdapter;
 import com.www.wsgou.base.BaseActivity;

 import com.www.wsgou.bean.req.ReqVideoList;
 import com.www.wsgou.bean.res.ResVideoList;
 import com.www.wsgou.holder.DisplayHolder;
 import com.www.wsgou.http.engine.HttpEngine;
 import com.www.wsgou.http.engine.Urls;*/

/**
 * 
 * @author Eric.Chen
 * 
 */
public class AttentionFragment extends BaseFragment {
	private PullToRefreshGridView mGridView;
	// private AttentionAdapter adapter;
	// private List<Videos> list;
	// private HttpEngine client;
	private Activity pActivity;
	private int pageNo = 1;
	private static final int PAGESIZE = 6;
	private static final int GETMYATTENTION = 0;
	VideosDataList videosDataList;
	VideosDataList addMoreVideosDataList;
	String aString = "";
	VideoAdapter myAttentionAdapter;
	private boolean myAttentionAddMoredata = false;
	List<Videos> videos;
	PullToRefreshScrollView pullToRefreshScrollView;
	GridViewForScrollView gridViewForScrollView;
	ScrollView scrollView;
	View view2;
	Timer timer;
	MyHandler myHandler;
	List<ThemeDetails> videoDetails;
	private static final int getVideoDetail = 1;
	int VideosID = 0;
	private RefreshMainFragmentReceiver refreshMainFragmentReceiver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		refreshMainFragmentReceiver = new RefreshMainFragmentReceiver();
		IntentFilter filter0 = new IntentFilter();
		filter0.addAction(Constant.ACTION_PRAISE_STATUS);
		getActivity().registerReceiver(refreshMainFragmentReceiver, filter0);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_attention, container,
				false);
		pullToRefreshScrollView = (PullToRefreshScrollView) view
				.findViewById(R.id.pull_refresh_list);
		view2 = inflater.inflate(R.layout.find_layout_12, null);
		gridViewForScrollView = (GridViewForScrollView) view2
				.findViewById(R.id.grid);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		myHandler = new MyHandler();
		initScrollView();
		setPullToRefreshScrollView(pullToRefreshScrollView);
		setListeners();
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		getActivity().unregisterReceiver(refreshMainFragmentReceiver);
	}

	private void initScrollView() {
		// TODO Auto-generated method stub
		scrollView = pullToRefreshScrollView.getRefreshableView();
		scrollView.addView(view2);

	}

	private void setPullToRefreshScrollView(
			PullToRefreshScrollView pullToRefreshScrollView) {

		pullToRefreshScrollView.setMode(Mode.BOTH);
		// 下拉刷新时的提示文本设置
		pullToRefreshScrollView.getLoadingLayoutProxy(true, false)
				.setLastUpdatedLabel("下拉刷新");
		pullToRefreshScrollView.getLoadingLayoutProxy(true, false)
				.setPullLabel("");
		pullToRefreshScrollView.getLoadingLayoutProxy(true, false)
				.setRefreshingLabel("正在刷新");
		pullToRefreshScrollView.getLoadingLayoutProxy(true, false)
				.setReleaseLabel("放开以刷新");
		// 上拉加载更多时的提示文本设置
		pullToRefreshScrollView.getLoadingLayoutProxy(false, true)
				.setLastUpdatedLabel("上拉加载");
		pullToRefreshScrollView.getLoadingLayoutProxy(false, true)
				.setPullLabel("");
		pullToRefreshScrollView.getLoadingLayoutProxy(false, true)
				.setRefreshingLabel("正在加载...");
		pullToRefreshScrollView.getLoadingLayoutProxy(false, true)
				.setReleaseLabel("放开以加载");
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		this.pActivity = activity;
		super.onAttach(activity);
	}

	@Override
	public void onResume() {
		initData();
		super.onResume();
	}

	/*
	 * @Override protected void initView(LayoutInflater inflater, ViewGroup
	 * container) {
	 * 
	 * 
	 * 
	 * 
	 * list = new ArrayList<Videos>(); client = activity.client;
	 * mGridView.setOnRefreshListener(new OnRefreshListener2<GridView>() {
	 * //TODO 传用户id
	 * 
	 * @Override public void onPullDownToRefresh(PullToRefreshBase refreshView)
	 * { ReqVideoList reqHot = new ReqVideoList(); reqHot.PageNo = pageNo;
	 * reqHot.PageSize = PAGESIZE; client.requestPost(reqHot,
	 * AttentionFragment.this, Urls.URL_VIDEOLIST, ResVideoList.class); }
	 * 
	 * @Override public void onPullUpToRefresh(PullToRefreshBase refreshView) {
	 * ReqVideoList reqHot = new ReqVideoList(); reqHot.PageNo = pageNo;
	 * reqHot.PageSize = PAGESIZE; client.requestPost(reqHot,
	 * AttentionFragment.this, Urls.URL_VIDEOLIST, ResVideoList.class); } }); }
	 */

	/*
	 * private void initData() { ReqVideoList reqHot = new ReqVideoList();
	 * reqHot.PageNo = pageNo++; reqHot.PageSize = 6; client.requestPost(reqHot,
	 * this, Urls.URL_VIDEOLIST, ResVideoList.class); }
	 */

	private void initData() {
		// TODO Auto-generated method stub
		myAttentionAddMoredata = false;
		DialogHelper dialogHelper = new DialogHelper();
		dialogHelper.execute(GETMYATTENTION);
	}

	private void setListeners() {

		/*
		 * gridViewForScrollView.setOnItemClickListener(new
		 * OnItemClickListener() {
		 * 
		 * @Override public void onItemClick(AdapterView<?> arg0, View arg1, int
		 * arg2, long arg3) { // TODO Auto-generated method stub
		 * UIHelper.showMsg(pActivity, "kkkkkkkkkkkkkkkkk"); } });
		 */
		// 添加 一个下拉刷新事件
		pullToRefreshScrollView
				.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

					@Override
					public void onRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						if (refreshView.isShown()) {
							timer = new Timer();
							timer.schedule(new RemindTask(), 2000);
						} else {
							// 得到上一次滚动条的位置，让加载后的页面停在上一次的位置，便于用户操作
							// y = datalist.size();
							myAttentionAddMoredata = true;
							pageNo++;
							DialogHelper dialogHelper = new DialogHelper();
							dialogHelper.execute(GETMYATTENTION);

						}
					}
				});

	}

	/*
	 * @Override public void onResponse(String tag, Object obj) {
	 * if(TextUtils.equals(tag, Urls.URL_VIDEOLIST)){ ResVideoList resVideoList
	 * = (ResVideoList) obj; list.addAll(resVideoList.models); LogUtil.i("wsg",
	 * list.size() + ""); if(adapter == null){ adapter = new
	 * AttentionAdapter(list); mGridView.setAdapter(adapter); }else{
	 * adapter.notifyDataSetChanged(); mGridView.onRefreshComplete(); } } }
	 * 
	 * @Override public void onErrorResponse(String tag, VolleyError
	 * volleyerror) { }
	 * 
	 * private class AttentionAdapter extends DisplayAdapter{ private
	 * DisplayHolder holder; public AttentionAdapter(List<Videos> list) {
	 * super(list); }
	 * 
	 * @Override public View getView(int position, View convertView, ViewGroup
	 * parent) { final Videos item = getItem(position); if (convertView != null)
	 * { holder = (DisplayHolder) convertView.getTag(); } else { convertView =
	 * View.inflate(parent.getContext(), R.layout.item_gridview_home_hot, null);
	 * holder = new DisplayHolder(convertView); convertView.setTag(holder); }
	 * activity.loadUrlPicRound(holder.brandIv, item.userHeadsculpture);
	 * activity.loadUrlPic(holder.graphIv, item.cover); String
	 * descTv=item.title;
	 * 
	 * if(descTv.length()>8){
	 * 
	 * holder.descTv.setText(descTv.substring(0, 8)+"......"); }else{
	 * 
	 * holder.descTv.setText(descTv); }
	 * holder.favRb.setText(String.valueOf(item.hits));
	 * holder.favRb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	 * 
	 * @Override public void onCheckedChanged(CompoundButton buttonView, boolean
	 * isChecked) { if (isChecked) { // TODO 改变网络上数据
	 * holder.favRb.setText(String.valueOf(++item.hits));
	 * adapter.notifyDataSetChanged(); } else {
	 * holder.favRb.setText(String.valueOf(--item.hits));
	 * adapter.notifyDataSetChanged(); } } }); if (item.userCertificationState
	 * == 1) { //TODO 让认证标志 显示 /隐藏 } return convertView; } }
	 */

	@Override
	public void onLeave() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReLoad(Intent paramIntent) {
		// TODO Auto-generated method stub
		myAttentionAddMoredata = false;
		DialogHelper dialogHelper = new DialogHelper();
		dialogHelper.execute(GETMYATTENTION);
	}

	public class DialogHelper extends AsyncTask<Integer, Void, Integer> {
		public DialogHelper() {
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			ProcessUtil.showProgressDialog(pActivity, "获取数据中...");
		}

		@Override
		protected Integer doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			switch (params[0]) {
			case GETMYATTENTION:
				if (!myAttentionAddMoredata) {
					pageNo = 1;
					videosDataList = ApiClient.getMyAttention(0, pageNo,
							PAGESIZE);
				} else {
					addMoreVideosDataList = ApiClient.getMyAttention(0, pageNo,
							PAGESIZE);
				}

				// String aString=ApiClient.getGoodsList3();
				return GETMYATTENTION;
			case getVideoDetail:
				videoDetails = ApiClient.getVideoDetail(VideosID);
				return getVideoDetail;

			default:
				return -1;
			}

		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			switch (result) {
			case GETMYATTENTION:
				ProcessUtil.dismissProgressdialog();
				if (!myAttentionAddMoredata) {
					if (videosDataList != null) {
						videos = videosDataList.getModels();
						if (videos != null && videos.size() > 0) {
							UIHelper.showMsg(pActivity, "获取数据成功");

						} else {
							UIHelper.showMsg(pActivity, "获取数据失败");
						}
					} else {
						UIHelper.showMsg(pActivity, "获取数据失败");

					}
				} else {
					if (addMoreVideosDataList != null) {
						List<Videos> addMoreVideos = addMoreVideosDataList
								.getModels();
						if (addMoreVideos != null && addMoreVideos.size() > 0) {
							videos.addAll(addMoreVideos);
						}
					}
					pullToRefreshScrollView.onRefreshComplete();

				}
				if (videos != null && videos.size() > 0) {
					if (myAttentionAdapter == null) {
						myAttentionAdapter = new VideoAdapter(pActivity,
								videos, myHandler);
						gridViewForScrollView.setAdapter(myAttentionAdapter);
					} else {
						myAttentionAdapter.setData(videos);
						myAttentionAdapter.notifyDataSetChanged();

					}
				}

				break;
			case getVideoDetail:
				ProcessUtil.dismissProgressdialog();
				if (videoDetails != null && videoDetails.size() > 0) {
					ThemeDetails bean = videoDetails.get(0);
					if (bean != null) {
						// Intent intent=new
						// Intent(getActivity(),VideoDetailActivity.class);
						// intent.putExtra(Constant.VIDEO_ID, bean.getiD());
						// startActivity(intent);

						// DetailsCommentFragment detailsFragment=new
						// DetailsCommentFragment();
						// Bundle bundle=new Bundle();
						// //得到当前点击的条目的对象
						// //ThemeDetails themeDetail = list.get(position);
						// bundle.putSerializable("Bean", bean);
						// bundle.putString("form", "AttentionFragment");
						// detailsFragment.setArguments(bundle);
						// ((AttentionAct)pActivity).dumpToNext(detailsFragment,
						// "DetailsCommentFragment");
					}
				}
			default:
				break;
			}
			super.onPostExecute(result);
		}
	}

	class RemindTask extends TimerTask {
		public void run() {
			Message msg = myHandler.obtainMessage();
			msg.what = 0;
			myHandler.sendMessage(msg);
			timer.cancel(); // Terminate the timer thread
		}
	}

	class MyHandler extends Handler {
		// 子类必须重写此方法,接受数据
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				pullToRefreshScrollView.onRefreshComplete();

				break;
			case 5:
				VideosID = msg.arg1;
				DialogHelper dialogHelper = new DialogHelper();
				dialogHelper.execute(getVideoDetail);
			default:
				break;
			}

		}

	}

	public class RefreshMainFragmentReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, final Intent intent) {

			int videoID = intent.getIntExtra(Constant.INTENT_VIDEO_ID, -1);
			int isPraise = intent
					.getIntExtra(Constant.INTENT_PRAISE_STATUS, -1);
			if (videoID > 0 && isPraise > -1 && videos != null) {
				for (Videos v : videos) {
					if (videoID == v.getID()) {
						v.setIsPraise(isPraise);
						if (isPraise == Constant.ISPRAISE_YES) {
							int count = v.getPraiseCount();
							count++;
							v.setPraiseCount(count);
						} else if (isPraise == Constant.ISPRAISE_NO) {
							int count = v.getPraiseCount();
							count--;
							v.setPraiseCount(count);
						}
						if (myAttentionAdapter != null)
							myAttentionAdapter.notifyDataSetChanged();
						return;
					}
				}
			}

		}

	}
}
