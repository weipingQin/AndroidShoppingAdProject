package com.www.goumei.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.www.goumei.R;
import com.www.goumei.activity.MainMyAct;
import com.www.goumei.adapter.UnPublishViedeoAdapter;
import com.www.goumei.bean.Videos;
import com.www.goumei.bean.VideosDataList;
import com.www.goumei.http.req.ApiClientC;
import com.www.goumei.http.req.SP_ID;
import com.www.goumei.utils.Constant;
import com.www.goumei.utils.L;

/**
 * 
 * @ClassName: DraftboxFragment
 * @Description:
 * @author sunyouyi
 * @date 2015-6-7 下午2:50:22
 * 
 */
public class DraftboxFragment extends BaseFragment implements OnClickListener {

	private TextView last;
	/**下拉列表*/
	private PullToRefreshListView draftboxList;
	Activity pActivity;
	List<Videos> videos;
	int pageNo = 1;
	boolean hasMore = true;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_draftbox, container,
				false);
		last = (TextView) view.findViewById(R.id.draftbox_last);
		draftboxList = (PullToRefreshListView) view
				.findViewById(R.id.draftboxList);
		draftboxList.setMode(Mode.BOTH);
		videos=new ArrayList<Videos>();
		draftboxList.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				hasMore=true;
				draftboxList.setMode(Mode.BOTH);
				pageNo=1;
				videos.clear();
				new GetVideosTask().execute("");
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				if(hasMore){
					new GetVideosTask().execute("");
				}else{
					draftboxList.onRefreshComplete();
				}
				
			}
		});
		new GetVideosTask().execute("");
		return view;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		initView();
		setListeners();
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		this.pActivity = activity;
		super.onAttach(activity);
	}

	private void initView() {

	}

	protected void setListeners() {
		last.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.draftbox_last:
			((MainMyAct) pActivity).goBackAndShowPreView(null);
			break;

		default:
			break;
		}
	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		if(!hidden){
			hasMore=true;
			draftboxList.setMode(Mode.BOTH);
			pageNo=1;
			videos.clear();
			new GetVideosTask().execute("");
		}
	}
	/**
	 * 初始化数据
	 */
	public void initData() {

	}

	@Override
	public void onLeave() {

	}

	@Override
	public void onReLoad(Intent paramIntent) {

	}

	class GetVideosTask extends AsyncTask<String, String, String> {
		VideosDataList videoList;

		@Override
		protected String doInBackground(String... arg0) {
			videoList = ApiClientC.getUpPublishVideosById(SP_ID.id, pageNo);

			return null;
		}

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			draftboxList.onRefreshComplete();
			if (videoList != null) {
				if (videoList.getModels() != null
						&& videoList.getModels().size() < Constant.PAGE_SIZE) {
					hasMore = false;
					draftboxList.setMode(Mode.PULL_FROM_START);
				}
				pageNo++;
				videos.addAll(videoList.getModels());
				L.e("videos.size():::::::"+videos.size());
				draftboxList.setAdapter(new UnPublishViedeoAdapter(getActivity(), videos));
			}

		}
	}
}
