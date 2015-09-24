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
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.www.goumei.R;
import com.www.goumei.activity.MainHomeAct;
import com.www.goumei.adapter.CateoryAdapter;
import com.www.goumei.adapter.MyPagerAdapter;
import com.www.goumei.adapter.VideoAdapter;
import com.www.goumei.bean.Categories;
import com.www.goumei.bean.ThemeBean;
import com.www.goumei.bean.ThemeDetails;
import com.www.goumei.bean.Videos;
import com.www.goumei.bean.VideosDataList;
import com.www.goumei.http.ApiClient;
import com.www.goumei.utils.Constant;
import com.www.goumei.utils.L;
import com.www.goumei.utils.ProcessUtil;
import com.www.goumei.utils.UIHelper;
import com.www.goumei.views.GridViewForScrollView;
import com.www.goumei.views.LazyViewPager;
import com.www.goumei.views.RoundImageView;
/**
 * 首页
 */
@SuppressLint({ "HandlerLeak", "InflateParams" })
public class HomeFragment extends BaseFragment implements
		OnCheckedChangeListener,OnClickListener{
	/** 发现--请求数据 */
	private static final int DISCOVER_REQUEST = 1001;
	/** 发现-数据加载完成 */
	private static final int LOAD_COMPLETE = 1002;
	/** 存放所有主题的集合 */
	private static List<ThemeBean> MyTheme;
	/** 所以主题图片控件的数组 */
	//private static RoundImageView[] theres;
	List<RoundImageView> roundImageViews;
	private RadioGroup mTopRadioGroup;
	private RadioButton hotRb;
	private RadioButton discoverRb;
	private RadioButton buycirlceRb;
	private RadioButton categoryRb;
	private LazyViewPager mViewPager;
	MyPagerAdapter myPagerAdapter;
	Activity pActivity;
	private List<View> listViews = new ArrayList<View>();
	View view1, view2, view3, view4;
	int page;
	private List<Videos> hotVideosList;
	private List<Videos> zuixinVideosList;
	private List<Videos> zuireiVideosList;
	private List<PullToRefreshScrollView> pullToRefreshScrollViews=new ArrayList<PullToRefreshScrollView>();
	private GridViewForScrollView  mHotGridViewForScrollView,mZuiXinGridViewForScrollView,mZuiReiGridViewForScrollView;
	private PullToRefreshScrollView mHotVideosPullToRefreshScrollView, zuixin_PullToRefreshScrollView, zuirei_PullToRefreshScrollView;
	private int hotVideosPageNo = 1;
	private int zuixinVideosPageNo = 1;
	private int zuireiVideosPageNo = 1;
	private static final int PAGESIZE = 6;
	/** 获取热门视频*/
	private static final int GETHOTVIDEO = 0;
	/** 获取买手圈*/
	private static final int GETBUYCIRLCEVIDEO = 1;
	/** 获取视频详细*/
	private static final int getVideoDetail = 2;
	/** 获取分类*/
	private static final int getCategories = 3;
	private boolean hotVideoAddMoredata = false;
	private boolean zuixinVideoAddMoredata = false;
	private boolean zuireiVideoAddMoredata = false;
	VideosDataList hotVideosDataList;
	VideosDataList zuixinVideosDataList;
	VideosDataList zuireiVideosDataList;
	VideosDataList addMoreHotVideosDataList;
	VideosDataList addMoreZuiReiVideosDataList;
	VideosDataList addMoreZuiXinVideosDataList;
	VideoAdapter hotVideoAdapter, zuixinVideoAdapter, zuireiVideoAdapter;
	private TextView tv_new;
	private TextView tv_hot;
	// 0 最新 1 最热
	int buycirlcType = 0;
	View  view1_1,view4_1,view3_1;
	GridView category_grid;
	CateoryAdapter cateoryAdapter;
	ScrollView scrollView,scrollView2,scrollView3;
	Timer  timer;
	MyHandler myHandler;
	int   req_type=0;
	int VideosID=0;
	List<ThemeDetails>   videoDetails;
	/** 分类 */
	List<Categories> myCategories;
	
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
		myHandler = new MyHandler();  
		roundImageViews=new ArrayList<RoundImageView>();
		View rootView = inflater.inflate(R.layout.fragment_home, container,
				false);
		mTopRadioGroup = (RadioGroup) rootView.findViewById(R.id.rb_group_top);
		hotRb = (RadioButton) rootView.findViewById(R.id.rb_hot);
		discoverRb = (RadioButton) rootView.findViewById(R.id.rb_discover);
		buycirlceRb = (RadioButton) rootView.findViewById(R.id.rb_buycirlce);
		categoryRb = (RadioButton) rootView.findViewById(R.id.rb_category);
		mViewPager = (LazyViewPager) rootView.findViewById(R.id.viewpager);
		rootView.findViewById(R.id.ib_search).setOnClickListener(this);
		view1 = inflater.inflate(R.layout.fragment_hot, null);
		view1_1=inflater.inflate(R.layout.find_layout_12, null);
		mHotGridViewForScrollView=(GridViewForScrollView) view1_1.findViewById(R.id.grid);
		mHotVideosPullToRefreshScrollView= (PullToRefreshScrollView) view1
				.findViewById(R.id.pull_refresh_list);

		// 发现界面的布局及控件
		view2 = inflater.inflate(R.layout.fragment_discover, null);
		
		goddessSeason = (RoundImageView) view2.findViewById(R.id.goddessSeason);
		parisFashionWeek = (RoundImageView) view2
				.findViewById(R.id.ParisFashionWeek);
		foodMuseum = (RoundImageView) view2.findViewById(R.id.FoodMuseum);
		makeup = (RoundImageView) view2.findViewById(R.id.Makeup);
		happyPregnantMother = (RoundImageView) view2
				.findViewById(R.id.HappyPregnantMother);
		warmManBaymax = (RoundImageView) view2.findViewById(R.id.WarmManBaymax);
		childrensClothing = (RoundImageView) view2
				.findViewById(R.id.ChildrensClothing);
		mengMengDa = (RoundImageView) view2.findViewById(R.id.MengMengDa);

		view3 = inflater.inflate(R.layout.fragment_buycircle, null);
		view3_1=inflater.inflate(R.layout.find_layout_12, null);
		mZuiXinGridViewForScrollView=(GridViewForScrollView) view3_1.findViewById(R.id.grid);
		zuixin_PullToRefreshScrollView = (PullToRefreshScrollView) view3
				.findViewById(R.id.zuixin_pull_refresh_list);
		zuirei_PullToRefreshScrollView= (PullToRefreshScrollView) view3
				.findViewById(R.id.zuirei_pull_refresh_list);
		tv_new = (TextView) view3.findViewById(R.id.tv_new);
		// 初始化最新的字体为黄色
		tv_new.setTextColor(getResources().getColor(R.color.orange));
		tv_hot = (TextView) view3.findViewById(R.id.tv_hot);
		view4 = inflater.inflate(R.layout.fragment_category, null);
		view4_1=inflater.inflate(R.layout.find_layout_12, null);
		mZuiReiGridViewForScrollView=(GridViewForScrollView) view4_1.findViewById(R.id.grid);
		category_grid = (GridView) view4.findViewById(R.id.category_grid);
		
		roundImageViews.add(goddessSeason);
		roundImageViews.add(parisFashionWeek);
		roundImageViews.add(foodMuseum);
		roundImageViews.add(makeup);
		roundImageViews.add(happyPregnantMother);
		roundImageViews.add(warmManBaymax);
		roundImageViews.add(childrensClothing);
		roundImageViews.add(mengMengDa);
		pullToRefreshScrollViews.add(mHotVideosPullToRefreshScrollView);
		pullToRefreshScrollViews.add(zuirei_PullToRefreshScrollView);
		pullToRefreshScrollViews.add(zuixin_PullToRefreshScrollView);
		 for (int i = 0; i < pullToRefreshScrollViews.size(); i++) {
				setPullToRefreshScrollView(pullToRefreshScrollViews.get(i));
			}
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		initView();
		initScrollView();
		initData();
		setListeners();

		super.onActivityCreated(savedInstanceState);
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		getActivity().unregisterReceiver(refreshMainFragmentReceiver);
	}
	private void initData() {
		hotVideosPageNo = 1;

		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.test)
				.showImageForEmptyUri(R.drawable.test)
				.showImageOnFail(R.drawable.test).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		DialogHelper dialogHelper = new DialogHelper();
		dialogHelper.execute(GETHOTVIDEO);

	}

	private void setPullToRefreshScrollView(PullToRefreshScrollView pullToRefreshScrollView){
		
		pullToRefreshScrollView.setMode(Mode.BOTH);
		// 下拉刷新时的提示文本设置
		pullToRefreshScrollView.getLoadingLayoutProxy(true, false).setLastUpdatedLabel("下拉刷新");
		pullToRefreshScrollView.getLoadingLayoutProxy(true, false).setPullLabel("");
		pullToRefreshScrollView.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新");
		pullToRefreshScrollView.getLoadingLayoutProxy(true, false).setReleaseLabel("放开以刷新");
		// 上拉加载更多时的提示文本设置
		pullToRefreshScrollView.getLoadingLayoutProxy(false, true).setLastUpdatedLabel("上拉加载");
		pullToRefreshScrollView.getLoadingLayoutProxy(false, true).setPullLabel("");
		pullToRefreshScrollView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...");
		pullToRefreshScrollView.getLoadingLayoutProxy(false, true).setReleaseLabel("放开以加载");
	}
	private void initView() {
		listViews.add(view1);
		listViews.add(view2);
		listViews.add(view3);
		listViews.add(view4);
		hotRb.setChecked(true);
		myPagerAdapter = new MyPagerAdapter(listViews);
		mViewPager.setAdapter(myPagerAdapter);
		mViewPager.setCurrentItem(0);
	}

	@Override
	public void onAttach(Activity activity) {
		this.pActivity = activity;
		super.onAttach(activity);
	}

	private void initScrollView() {
			 scrollView = mHotVideosPullToRefreshScrollView.getRefreshableView();
			 scrollView .addView(view1_1);
			 scrollView2 = zuixin_PullToRefreshScrollView.getRefreshableView();
			 scrollView2 .addView(view3_1);
			 scrollView3 = zuirei_PullToRefreshScrollView.getRefreshableView();
			 scrollView3 .addView(view4_1);
			
	    }
	protected void setListeners() {
		
		// 添加 一个下拉刷新事件
		mHotVideosPullToRefreshScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

					@Override
					public void onRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						if (refreshView.isShown()) {
							req_type=0;
							timer = new Timer();  
							timer.schedule(new RemindTask(), 2000);  
						} else {
							//得到上一次滚动条的位置，让加载后的页面停在上一次的位置，便于用户操作
							//y = datalist.size();
							hotVideoAddMoredata=true;
							hotVideosPageNo ++;
							DialogHelper dialogHelper=new DialogHelper();
							dialogHelper.execute(GETHOTVIDEO);	
							
							
						}
					}
				});
		zuixin_PullToRefreshScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {
			
			@Override
			public void onRefresh(
					PullToRefreshBase<ScrollView> refreshView) {
				if (refreshView.isShown()) {
					req_type=1;
					timer = new Timer();  
					timer.schedule(new RemindTask(), 2000);  
				} else {
					//得到上一次滚动条的位置，让加载后的页面停在上一次的位置，便于用户操作
					//y = datalist.size();
					buycirlcType=0;
					zuixinVideoAddMoredata=true;
					zuixinVideosPageNo ++;
					DialogHelper dialogHelper=new DialogHelper();
					dialogHelper.execute(GETBUYCIRLCEVIDEO);	
					
					
				}
			}
		});
		zuirei_PullToRefreshScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {
			
			@Override
			public void onRefresh(
					PullToRefreshBase<ScrollView> refreshView) {
				if (refreshView.isShown()) {
					req_type=2;
					timer = new Timer();  
					timer.schedule(new RemindTask(), 2000);  
				} else {
					//得到上一次滚动条的位置，让加载后的页面停在上一次的位置，便于用户操作
					//y = datalist.size();
					buycirlcType=1;
					zuireiVideoAddMoredata=true;
					zuireiVideosPageNo ++;
					DialogHelper dialogHelper=new DialogHelper();
					dialogHelper.execute(GETBUYCIRLCEVIDEO);	
					
					
				}
			}
		});
		mTopRadioGroup.setOnCheckedChangeListener(this);
		tv_hot.setOnClickListener(onTextViewClickListener);
		tv_new.setOnClickListener(onTextViewClickListener);
		// 页面滑动
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				if (mTopRadioGroup != null
						&& mTopRadioGroup.getChildCount() > position) {
					((RadioButton) mTopRadioGroup.getChildAt(position))
							.performClick();
					page = position;
					switch (position) {
					case 0:
						hotVideosPageNo = 1;
						hotVideoAddMoredata = false;
						DialogHelper dialogHelper = new DialogHelper();
						dialogHelper.execute(GETHOTVIDEO);
						break;
					case 1:
						// 请求数据
						dialogHelper = new DialogHelper();
						dialogHelper.execute(DISCOVER_REQUEST);
						break;
					case 2:
						if (buycirlcType == 0) {
							zuixinVideosPageNo = 1;
							zuireiVideoAddMoredata = false;
						} else {
							zuireiVideosPageNo = 1;
							zuireiVideoAddMoredata = false;
						}
						DialogHelper dialogHelper2 = new DialogHelper();
						dialogHelper2.execute(GETBUYCIRLCEVIDEO);
						break;
					case 3:
						DialogHelper dialogHelper3 = new DialogHelper();
						dialogHelper3.execute(getCategories);
						
						break;

					default:
						break;
					}

				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		
		category_grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				CategoryDetailFragment categoryDetailFragment = new CategoryDetailFragment();
				Bundle bundle = new Bundle();
				bundle.putInt("categoryID", myCategories.get(position).getID());
				bundle.putString("categoryName", myCategories.get(position).getName());
				categoryDetailFragment.setArguments(bundle);
				((MainHomeAct) pActivity).dumpToNext(categoryDetailFragment, "CategoryDetailFragment");
			}
		});
		
		goddessSeason.setOnClickListener(this);
		parisFashionWeek.setOnClickListener(this);
		foodMuseum.setOnClickListener(this); 
		makeup.setOnClickListener(this);
		happyPregnantMother.setOnClickListener(this);
		warmManBaymax.setOnClickListener(this);
		childrensClothing.setOnClickListener(this);
		mengMengDa.setOnClickListener(this);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {

		int position = 0;
		switch (checkedId) {

		case R.id.rb_hot:
			position = 0;
			break;
		case R.id.rb_discover:
			position = 1;
			break;
		case R.id.rb_buycirlce:
			position = 2;
			break;
		case R.id.rb_category:
			position = 3;
			break;
		default:
			break;
		}
		mViewPager.setCurrentItem(position);
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case LOAD_COMPLETE:
				ProcessUtil.dismissProgressdialog();
				if(null != MyTheme){
					for (int i = 0; i < MyTheme.size(); i++) {
						imageLoader.displayImage(MyTheme.get(i).getCover(),
								roundImageViews.get(i), options);
					}
				}
				break;

			default:
				break;
			}
		}
	};

	OnClickListener onTextViewClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			tv_hot.setTextColor(Color.BLACK);
			tv_new.setTextColor(Color.BLACK);

			switch (arg0.getId()) {
			case R.id.tv_new:
				tv_new.setTextColor(getResources().getColor(R.color.orange));

				buycirlcType = 0;
				zuixinVideosPageNo = 1;
				break;
			case R.id.tv_hot:
				tv_hot.setTextColor(getResources().getColor(R.color.orange));
				/*
				 * zuirei_grid.setVisibility(View.VISIBLE);
				 * zuixin_grid.setVisibility(View.GONE);
				 */
				buycirlcType = 1;
				zuireiVideosPageNo = 1;
				break;

			default:
				break;
			}

			DialogHelper dialogHelper = new DialogHelper();
			dialogHelper.execute(GETBUYCIRLCEVIDEO);
		}
	};
	private RoundImageView goddessSeason;
	private RoundImageView parisFashionWeek;
	private RoundImageView foodMuseum;
	private RoundImageView makeup;
	private RoundImageView happyPregnantMother;
	private RoundImageView warmManBaymax;
	private RoundImageView childrensClothing;
	private RoundImageView mengMengDa;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private ThemeOpenFragment themeOpenFragment;

	@Override
	public void onLeave() {

	}

	@Override
	public void onReLoad(Intent paramIntent) {

	}

	public class DialogHelper extends AsyncTask<Integer, Void, Integer> {
		String resultStr = "";

		public DialogHelper() {
		}

		@Override
		protected void onPreExecute() {
			ProcessUtil.showProgressDialog(pActivity, "获取数据中...");
		}

		@Override
		protected Integer doInBackground(Integer... params) {
			switch (params[0]) {
			case GETHOTVIDEO:
				if (!hotVideoAddMoredata) {
					hotVideosPageNo = 1;
					hotVideosDataList = ApiClient.getMyAttention(0,
							hotVideosPageNo, PAGESIZE);
				} else {
					addMoreHotVideosDataList = ApiClient.getMyAttention(0,
							hotVideosPageNo, PAGESIZE);
				}
				// String aString=ApiClient.getGoodsList3();
				return GETHOTVIDEO;
				// 发现页-请求数据
			case DISCOVER_REQUEST:
				MyTheme = ApiClient.getTheme();
				// 数据加载完成，更新界面
				handler.sendEmptyMessage(LOAD_COMPLETE);
				return DISCOVER_REQUEST;

			case GETBUYCIRLCEVIDEO:
				if (buycirlcType == 0) {
					if (!zuixinVideoAddMoredata) {
						zuixinVideosPageNo = 1;
						zuixinVideosDataList = ApiClient.getMyAttention(1,
								zuixinVideosPageNo, PAGESIZE);
					} else {
						zuixinVideosPageNo = 1;
						addMoreZuiXinVideosDataList = ApiClient.getMyAttention(
								1, zuixinVideosPageNo, PAGESIZE);
					}
				} else if (buycirlcType == 1) {
					if (!zuireiVideoAddMoredata) {
						zuireiVideosPageNo = 1;
						zuireiVideosDataList = ApiClient.getMyAttention(2,
								zuireiVideosPageNo, PAGESIZE);
					} else {
						zuireiVideosPageNo = 1;
						addMoreZuiReiVideosDataList = ApiClient.getMyAttention(
								2, zuireiVideosPageNo, PAGESIZE);
					}
				}
				return GETBUYCIRLCEVIDEO;
			case   getVideoDetail:
				videoDetails=ApiClient.getVideoDetail(VideosID);
				return getVideoDetail;
			case getCategories:
				myCategories = ApiClient.getCategories(1,100);
				return getCategories;
			default:
				return -1;
			}

		}

		@Override
		protected void onPostExecute(Integer result) {
			switch (result) {
			case GETHOTVIDEO:
				ProcessUtil.dismissProgressdialog();
				if (!hotVideoAddMoredata) {
					if (hotVideosDataList != null) {
						hotVideosList = hotVideosDataList.getModels();
						if (hotVideosList != null && hotVideosList.size() > 0) {
							UIHelper.showMsg(pActivity, "获取数据成功");

						} else {
							UIHelper.showMsg(pActivity, "获取数据失败");
						}
					} else {
						UIHelper.showMsg(pActivity, "获取数据失败");

					}
				} else {
					if (addMoreHotVideosDataList != null) {
						List<Videos> addMoreVideos = addMoreHotVideosDataList
								.getModels();
						if (addMoreVideos != null && addMoreVideos.size() > 0) {
							hotVideosList.addAll(addMoreVideos);
						}
					}
					mHotVideosPullToRefreshScrollView.onRefreshComplete();

				}
				if (hotVideosList != null && hotVideosList.size() > 0) {
					if (hotVideoAdapter == null) {
						hotVideoAdapter = new VideoAdapter(pActivity,
								hotVideosList,myHandler);

						mHotGridViewForScrollView.setAdapter(hotVideoAdapter);
					} else {
						hotVideoAdapter.setData(hotVideosList);
						hotVideoAdapter.notifyDataSetChanged();

					}
				}

				break;
			case GETBUYCIRLCEVIDEO:

				ProcessUtil.dismissProgressdialog();
				if (buycirlcType == 0) {

					if (!zuixinVideoAddMoredata) {
						if (zuixinVideosDataList != null) {
							zuixinVideosList = zuixinVideosDataList.getModels();
							if (zuixinVideosList != null
									&& zuixinVideosList.size() > 0) {
								UIHelper.showMsg(pActivity, "获取数据成功");

							} else {
								UIHelper.showMsg(pActivity, "获取数据失败");
							}
						} else {
							UIHelper.showMsg(pActivity, "获取数据失败");

						}
					} else {
						if (addMoreZuiXinVideosDataList != null) {
							List<Videos> addMoreVideos = addMoreZuiXinVideosDataList
									.getModels();
							if (addMoreVideos != null
									&& addMoreVideos.size() > 0) {
								zuixinVideosList.addAll(addMoreVideos);
							}
						}
						zuixin_PullToRefreshScrollView.onRefreshComplete();

					}
					zuixin_PullToRefreshScrollView.setVisibility(View.VISIBLE);
					zuirei_PullToRefreshScrollView.setVisibility(View.GONE);
					if (zuixinVideosList != null && zuixinVideosList.size() > 0) {
						if (zuixinVideoAdapter == null) {
							zuixinVideoAdapter = new VideoAdapter(pActivity,
									zuixinVideosList,myHandler);

							mZuiXinGridViewForScrollView.setAdapter(zuixinVideoAdapter);
						} else {
							zuixinVideoAdapter.setData(zuixinVideosList);
							zuixinVideoAdapter.notifyDataSetChanged();

						}
					}
				} else if (buycirlcType == 1) {
					if (!zuireiVideoAddMoredata) {
						if (zuireiVideosDataList != null) {
							zuireiVideosList = zuireiVideosDataList.getModels();
							if (zuireiVideosList != null
									&& zuireiVideosList.size() > 0) {
								UIHelper.showMsg(pActivity, "获取数据成功");

							} else {
								UIHelper.showMsg(pActivity, "获取数据失败");
							}
						} else {
							UIHelper.showMsg(pActivity, "获取数据失败");

						}
					} else {
						if (addMoreZuiReiVideosDataList != null) {
							List<Videos> addMoreVideos = addMoreZuiReiVideosDataList
									.getModels();
							if (addMoreVideos != null
									&& addMoreVideos.size() > 0) {
								zuireiVideosList.addAll(addMoreVideos);
							}
						}
						zuirei_PullToRefreshScrollView.onRefreshComplete();

					}
					zuirei_PullToRefreshScrollView.setVisibility(View.VISIBLE);
					zuixin_PullToRefreshScrollView.setVisibility(View.GONE);
					if (zuireiVideosList != null && zuireiVideosList.size() > 0) {
						if (zuireiVideoAdapter == null) {
							zuireiVideoAdapter = new VideoAdapter(pActivity,
									zuireiVideosList,myHandler);

							mZuiReiGridViewForScrollView.setAdapter(zuireiVideoAdapter);
						} else {
							zuireiVideoAdapter.setData(zuireiVideosList);
							zuireiVideoAdapter.notifyDataSetChanged();

						}
					}
				}
				break;

				
			case getVideoDetail:
				ProcessUtil.dismissProgressdialog();
				if(videoDetails!=null && videoDetails.size()>0){
					ThemeDetails	bean=videoDetails.get(0);
					if(bean!=null){
						DetailsCommentFragment detailsFragment=new DetailsCommentFragment();
						Bundle bundle=new Bundle();
						//得到当前点击的条目的对象
						//ThemeDetails themeDetail = list.get(position);
						bundle.putSerializable("Bean", bean);
						bundle.putString("form", "HomeFragment");
						detailsFragment.setArguments(bundle);
						((MainHomeAct)pActivity).dumpToNext(detailsFragment, "DetailsCommentFragment");
					}
				}
				break;
			case getCategories:
				ProcessUtil.dismissProgressdialog();
				if(myCategories!=null && myCategories.size()>0){
					
					cateoryAdapter = new CateoryAdapter(pActivity, myCategories);
					category_grid.setAdapter(cateoryAdapter);
				}
				break;
			default:
				break;
			}
			super.onPostExecute(result);
		}
	}

	@Override
	public void onClick(View v) {
		themeOpenFragment = new ThemeOpenFragment();
		Bundle bundle=new Bundle();
		switch (v.getId()) {
		case R.id.ib_search:
			SearchVideosFragment searchViedeo=new SearchVideosFragment();
			((MainHomeAct)pActivity).dumpToNext(searchViedeo,"SearchVideosFragment");
			
			break;
		case R.id.goddessSeason:
			bundle.putSerializable("ThemeBean", MyTheme.get(0));
			themeOpenFragment.setArguments(bundle);
			((MainHomeAct)pActivity).dumpToNext(themeOpenFragment, "ThemeOpenFragment");
			break;
			
		case R.id.ParisFashionWeek:
			bundle.putSerializable("ThemeBean", MyTheme.get(1));
			themeOpenFragment.setArguments(bundle);
			((MainHomeAct)pActivity).dumpToNext(themeOpenFragment, "ThemeOpenFragment");
			break;
			
		case R.id.FoodMuseum:
			bundle.putSerializable("ThemeBean", MyTheme.get(2));
			themeOpenFragment.setArguments(bundle);
			((MainHomeAct)pActivity).dumpToNext(themeOpenFragment, "ThemeOpenFragment");
			break;
			
		case R.id.Makeup:
			bundle.putSerializable("ThemeBean", MyTheme.get(3));
			themeOpenFragment.setArguments(bundle);
			((MainHomeAct)pActivity).dumpToNext(themeOpenFragment, "ThemeOpenFragment");
			break;
			
		case R.id.HappyPregnantMother:
			bundle.putSerializable("ThemeBean", MyTheme.get(4));
			themeOpenFragment.setArguments(bundle);
			((MainHomeAct)pActivity).dumpToNext(themeOpenFragment, "ThemeOpenFragment");
			break;
			
		case R.id.WarmManBaymax:
			bundle.putSerializable("ThemeBean", MyTheme.get(5));
			themeOpenFragment.setArguments(bundle);
			((MainHomeAct)pActivity).dumpToNext(themeOpenFragment, "ThemeOpenFragment");
			break;
			
		case R.id.ChildrensClothing:
			bundle.putSerializable("ThemeBean", MyTheme.get(6));
			themeOpenFragment.setArguments(bundle);
			((MainHomeAct)pActivity).dumpToNext(themeOpenFragment, "ThemeOpenFragment");
			break;
			
		case R.id.MengMengDa:
			bundle.putSerializable("ThemeBean", MyTheme.get(7));
			themeOpenFragment.setArguments(bundle);
			((MainHomeAct)pActivity).dumpToNext(themeOpenFragment, "ThemeOpenFragment");
			break;
		default:
			break;
		}
		
	}
	class RemindTask extends TimerTask {  
        public void run(){  
        	 Message msg = myHandler.obtainMessage();  
	         msg.what=req_type;
	         myHandler.sendMessage(msg);
            timer.cancel(); //Terminate the timer thread  
        }  
    }  
	class MyHandler extends Handler {  
        // 子类必须重写此方法,接受数据  
        @Override  
        public void handleMessage(Message msg) {  
            super.handleMessage(msg);  
             switch (msg.what) {
			case 0:
				 mHotVideosPullToRefreshScrollView.onRefreshComplete();
				
				break;
			case 1:
				zuixin_PullToRefreshScrollView.onRefreshComplete();
				
				break;
			case 2:
				zuirei_PullToRefreshScrollView.onRefreshComplete();
				
				break;
			case 5:
				  VideosID=msg.arg1;
				  DialogHelper   dialogHelper=new DialogHelper();
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
			L.e("viedowId..................."+videoID);
			L.e("isPraise..................."+isPraise);
			if (videoID > 0 && isPraise > -1 ) {
				//更新hot
				if(hotVideosList!=null&&hotVideosList.size()>0){
					for (Videos v : hotVideosList) {
						if (videoID == v.getID()) {
							L.e(".........."+hotVideosList.size());
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
							if (hotVideoAdapter != null)
								hotVideoAdapter.notifyDataSetChanged();
							return;
						}
					}
				}
				//更新最新
				if(zuixinVideosList!=null&&zuixinVideosList.size()>0){
					for (Videos v : zuixinVideosList) {
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
							if (zuixinVideoAdapter != null)
								zuixinVideoAdapter.notifyDataSetChanged();
							return;
						}
					}
				}
				//更新最热
				if(zuireiVideosList!=null&&zuireiVideosList.size()>0){
					for (Videos v : zuireiVideosList) {
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
							if (zuireiVideoAdapter != null)
								zuireiVideoAdapter.notifyDataSetChanged();
							return;
						}
					}
				}
				
			}

		}

	}
}
