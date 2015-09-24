package com.www.goumei.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import android.annotation.SuppressLint;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.www.goumei.R;
import com.www.goumei.activity.MainHomeAct;
import com.www.goumei.adapter.VideoAdapter;
import com.www.goumei.bean.ThemeDetails;
import com.www.goumei.bean.Videos;
import com.www.goumei.bean.VideosDataList;
import com.www.goumei.http.ApiClient;
import com.www.goumei.utils.Constant;
import com.www.goumei.utils.ProcessUtil;
import com.www.goumei.utils.UIHelper;
import com.www.goumei.utils.UIUtils;
import com.www.goumei.views.GridViewForScrollView;

@SuppressLint({ "HandlerLeak", "InflateParams" })
public class CategoryDetailFragment extends BaseFragment {
	private TextView titleTv;
	private PullToRefreshGridView mGridView;
	private VideoAdapter categoryDetailVideoAdapter;
	private int PageNo = 1;
	private int PageSize = 10;

	List<Videos> videos;
	Activity pActivity;
	private static final int GETCATEGORYDETAIL = 0;
	private Boolean categoryDetailAddMoredata = false;
	VideosDataList videosDataList;
	VideosDataList addMoreVideosDataList;
	/**种类ID*/
	int categoryIDs;
	/**种类名称*/
	String categoryName;
	TextView tv_later, tv_prior;
	PullToRefreshScrollView pullToRefreshScrollView;
	GridViewForScrollView gridViewForScrollView;
	View view2;
	Timer timer;
	MyHandler myHandler;
	List<ThemeDetails> videoDetails;
	private static final int getVideoDetail = 1;
	ScrollView scrollView;
	int VideosID;

	private RefreshMainFragmentReceiver refreshMainFragmentReceiver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		refreshMainFragmentReceiver = new RefreshMainFragmentReceiver();
		IntentFilter filter0 = new IntentFilter();
		filter0.addAction(Constant.ACTION_PRAISE_STATUS);
		getActivity().registerReceiver(refreshMainFragmentReceiver, filter0);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_category_detail,
				container, false);
		titleTv = (TextView) view.findViewById(R.id.tv_title);
		pullToRefreshScrollView = (PullToRefreshScrollView) view
				.findViewById(R.id.pull_refresh_list);
		view2 = inflater.inflate(R.layout.find_layout_12, null);
		gridViewForScrollView = (GridViewForScrollView) view2
				.findViewById(R.id.grid);
		tv_later = (TextView) view.findViewById(R.id.tv_later);
		tv_prior = (TextView) view.findViewById(R.id.tv_prior);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		myHandler = new MyHandler();
		initScrollView();
		setPullToRefreshScrollView(pullToRefreshScrollView);
		initView();
		setData();
		setListeners();
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		getActivity().unregisterReceiver(refreshMainFragmentReceiver);
	}

	private void initScrollView() {
		ScrollView scrollView = pullToRefreshScrollView.getRefreshableView();
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
		this.pActivity = activity;
		super.onAttach(activity);
	}

	protected void initView() {

		videos = new ArrayList<Videos>();

	}

	private void setData() {
		Bundle data = getArguments();
		if (data != null) {
			categoryIDs = data.getInt("categoryID");
			categoryName = data.getString("categoryName");
			
			titleTv.setText(categoryName);
			PageNo = 1;
			categoryDetailAddMoredata = false;
			DialogHelper dialogHelper = new DialogHelper();
			dialogHelper.execute(GETCATEGORYDETAIL);
		}

	}

	protected void setListeners() {
		tv_prior.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				((MainHomeAct) pActivity).goBackAndShowPreView(null);
			}
		});
		tv_later.setVisibility(View.INVISIBLE);
		
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
							categoryDetailAddMoredata = true;
							PageNo++;
							DialogHelper dialogHelper = new DialogHelper();
							dialogHelper.execute(GETCATEGORYDETAIL);

						}
					}
				});
	}

	@Override
	public void onLeave() {

	}

	@Override
	public void onReLoad(Intent paramIntent) {
		setData();
	}

	public class DialogHelper extends AsyncTask<Integer, Void, Integer> {
		public DialogHelper() {
		}

		@Override
		protected void onPreExecute() {
			ProcessUtil.showProgressDialog(pActivity, "获取数据中...");
		}

		@Override
		protected Integer doInBackground(Integer... params) {
			switch (params[0]) {
			case GETCATEGORYDETAIL:
				if (!categoryDetailAddMoredata) {
					PageNo = 1;
					videosDataList = ApiClient.getCategoryDetail(categoryIDs,
							PageNo, PageSize);
				} else {
					addMoreVideosDataList = ApiClient.getCategoryDetail(categoryIDs, PageNo, PageSize);
				}
				// String aString=ApiClient.getGoodsList3();
				return GETCATEGORYDETAIL;
			case getVideoDetail:
				videoDetails = ApiClient.getVideoDetail(VideosID);
				return getVideoDetail;
			default:
				return -1;
			}
		}

		@Override
		protected void onPostExecute(Integer result) {
			switch (result) {
			case GETCATEGORYDETAIL:
				ProcessUtil.dismissProgressdialog();
				if (!categoryDetailAddMoredata) {
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
					mGridView.onRefreshComplete();

				}
				if (videos != null && videos.size() > 0) {
					if (categoryDetailVideoAdapter == null) {
						categoryDetailVideoAdapter = new VideoAdapter(
								pActivity, videos, myHandler);
						gridViewForScrollView
								.setAdapter(categoryDetailVideoAdapter);
					} else {
						categoryDetailVideoAdapter.setData(videos);
						categoryDetailVideoAdapter.notifyDataSetChanged();

					}
				}

				break;
			case getVideoDetail:
				ProcessUtil.dismissProgressdialog();
				if (videoDetails != null && videoDetails.size() > 0) {
					ThemeDetails bean = videoDetails.get(0);
					if (bean != null) {

						DetailsCommentFragment detailsFragment = new DetailsCommentFragment();
						Bundle bundle = new Bundle();
						// 得到当前点击的条目的对象
						// ThemeDetails themeDetail = list.get(position);
						bundle.putSerializable("Bean", bean);
						bundle.putString("form", "CategoryDetailFragment");
						detailsFragment.setArguments(bundle);
						((MainHomeAct) pActivity).dumpToNext(detailsFragment,
								"DetailsCommentFragment");
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
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				pullToRefreshScrollView.onRefreshComplete();

				break;
			case 5:
				// VideosID=msg.arg1;
				// DialogHelper dialogHelper=new DialogHelper();
				// dialogHelper.execute(getVideoDetail);
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
						if (categoryDetailVideoAdapter != null)
							categoryDetailVideoAdapter.notifyDataSetChanged();
						return;
					}
				}
			}

		}

	}
}
