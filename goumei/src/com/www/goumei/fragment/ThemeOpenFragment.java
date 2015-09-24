package com.www.goumei.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.www.goumei.R;
import com.www.goumei.activity.MainHomeAct;
import com.www.goumei.adapter.ThemeOpenNewAdapter;
import com.www.goumei.adapter.ThemeOpenVgAdapter;
import com.www.goumei.bean.ThemeBean;
import com.www.goumei.bean.Videos;
import com.www.goumei.http.ApiClient;

public class ThemeOpenFragment extends BaseFragment implements
		OnCheckedChangeListener, OnPageChangeListener {
	/** 请求主题的详细资讯 */
	private final static int THEMEOPEN_REQUEST = 1101;
	/** 加载数据完成 */
	private final static int GETDATA_COMPLETE = 1102;
	private ThemeOpemTask task;
	/** 存放所有主题详情的集合 */
	private static List<Videos> details = new ArrayList<Videos>();
	private static List<View> views = new ArrayList<View>();
	private RadioButton btn[] = new RadioButton[2];
	private static int ids[] = { R.id.themeopenNew, R.id.themeopenHot };
	private TextView title;
	private TextView last;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private ImageView cover;
	private RadioButton news;
	private RadioButton hot;
	private ViewPager vg;
	private View view1;
	private View view2;
	private RadioGroup themeopenVg;
	private PullToRefreshGridView newGv;
	private PullToRefreshGridView hotGv;
	private ThemeOpenNewAdapter newAdapter;
	private ThemeBean bean;
	private Activity pActivity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_themeopen, null);
		initView(view, inflater);
		initListener();
		initData();
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		this.pActivity = activity;
		super.onAttach(activity);
	}

	/**
	 * 初始化控件
	 * 
	 * @param inflater
	 * 
	 * @param view
	 */
	private void initView(View v, LayoutInflater inflater) {
		title = (TextView) v.findViewById(R.id.themeopenTitle);
		last = (TextView) v.findViewById(R.id.themeopenLast);
		cover = (ImageView) v.findViewById(R.id.ThemeCover);
		news = (RadioButton) v.findViewById(R.id.themeopenNew);
		hot = (RadioButton) v.findViewById(R.id.themeopenHot);
		vg = (ViewPager) v.findViewById(R.id.themeopemVg);
		themeopenVg = (RadioGroup) v.findViewById(R.id.themeopenVg);

		view1 = inflater.inflate(R.layout.layout_themeopen_new, null);
		view2 = inflater.inflate(R.layout.layout_themeopen_hot, null);
		views.add(view1);
		views.add(view2);
		for (int i = 0; i < btn.length; i++) {
			btn[i] = (RadioButton) v.findViewById(ids[i]);
		}
		newGv = (PullToRefreshGridView) view1.findViewById(R.id.themeopenGvNew);
		hotGv = (PullToRefreshGridView) view2.findViewById(R.id.themeopenGvHot);

	}

	/**
	 * 获取数据
	 */
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
			DisplayMetrics metric = new DisplayMetrics();
			pActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
			int screenWidth = metric.widthPixels;
			ViewGroup.LayoutParams lp = cover.getLayoutParams();
			lp.width = screenWidth;
			lp.height = LayoutParams.WRAP_CONTENT;
			cover.setLayoutParams(lp);

			cover.setMaxWidth(screenWidth);
			cover.setMaxHeight(screenWidth / 5);
			bean = (ThemeBean) bundle.getSerializable("ThemeBean");
			title.setText(bean.getName());
			imageLoader.displayImage(bean.getCover(), cover, options);
			task = new ThemeOpemTask(bean);
			task.execute(THEMEOPEN_REQUEST);
		}
	}

	private void initListener() {
		vg.setAdapter(new ThemeOpenVgAdapter(views));
		vg.setCurrentItem(0);
		themeopenVg.setOnCheckedChangeListener(this);
		vg.setOnPageChangeListener(this);
		last.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((MainHomeAct) getActivity()).goBackAndShowPreView(null);
			}
		});

		newGv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Log.e("TAG", "点击了" + position);
			}
		});
		hotGv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Log.e("TAG", "点击了" + position);
			}
		});
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			// 数据加载完成
			case GETDATA_COMPLETE:
				// 将数据适配进去
				newAdapter = new ThemeOpenNewAdapter(pActivity, details);
				newGv.setAdapter(newAdapter);
				hotGv.setAdapter(newAdapter);
				newAdapter.notifyDataSetChanged();
				break;

			default:
				break;
			}
		}
	};

	@Override
	public void onLeave() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReLoad(Intent paramIntent) {
		// TODO Auto-generated method stub

	}

	private class ThemeOpemTask extends AsyncTask<Integer, Void, Integer> {
		private ThemeBean ThemeBean;

		public ThemeOpemTask(ThemeBean bean) {
			this.ThemeBean = bean;
		}

		@Override
		protected Integer doInBackground(Integer... params) {
			switch (params[0]) {
			case THEMEOPEN_REQUEST:
				details = ApiClient.getThemeMore(ThemeBean);
				if (details.size() > 0 && details != null) {
					// 加载数据完成，handler通知更新界面
					handler.sendEmptyMessage(GETDATA_COMPLETE);
				}
				return THEMEOPEN_REQUEST;

			default:
				return -1;
			}
		}

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.themeopenNew:
			vg.setCurrentItem(0);
			break;

		case R.id.themeopenHot:
			task = new ThemeOpemTask(bean);
			task.execute(THEMEOPEN_REQUEST);
			vg.setCurrentItem(1);
			break;
		default:
			break;
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int position) {
		// TODO Auto-generated method stub
		btn[position].performClick();
	}

	// /**最新的条目点击监听*/
	// class NewItemClickListener implements OnItemClickListener{
	//
	// @Override
	// public void onItemClick(AdapterView<?> parent, View view, int position,
	// long id) {
	// Toast.makeText(getActivity(),"tttttttttt", 100).show();
	// Log.e("TAG", "点击了"+position);
	// DetailsFragment detailsFragment=new DetailsFragment();
	// Bundle bundle=new Bundle();
	// //得到当前点击的条目的对象
	// ThemeDetails themeDetail = details.get(position);
	// bundle.putSerializable("Bean", themeDetail);
	// detailsFragment.setArguments(bundle);
	// ((MainHomeAct)getActivity()).dumpToNext(detailsFragment,
	// "DetailsFragment");
	// }
	//
	// }

	/** 最热的条目点击监听 */
	// class HotItemClickListener implements OnItemClickListener{
	//
	// @Override
	// public void onItemClick(AdapterView<?> parent, View view, int position,
	// long id) {
	// Toast.makeText(getActivity(),"ffffffffffff", 100).show();
	// Log.e("TAG", "点击了--"+position);
	// }
	//
	// }

}
