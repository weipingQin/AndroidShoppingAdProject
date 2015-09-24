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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.www.goumei.R;
import com.www.goumei.activity.MessageAct;
import com.www.goumei.adapter.PushAdapter;
import com.www.goumei.bean.PushBean;
import com.www.goumei.bean.PushModels;
import com.www.goumei.bean.UserData;
import com.www.goumei.bean.UserDataB;
import com.www.goumei.http.req.ApiClientC;
import com.www.goumei.http.req.SP_ID;
import com.www.goumei.utils.Constant;

/**
 * 信息
 * 
 * @author Eric.Chen
 *
 */
public class MessageFragment extends BaseFragment implements OnClickListener {
	private TextView titleTv;
	private PullToRefreshListView mListView;
	private List<PushBean> list;
	private UserData user;
	private int pageNo = 1;
	private PushAdapter adapter;
	Activity pActivity;
	RelativeLayout rl_fans, rl_comments, rl_favorite;
	TextView tv_fans, tv_comments, tv_favorite;
	boolean hasMore;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_message, container,
				false);
		titleTv = (TextView) view.findViewById(R.id.tv_title);
		mListView = (PullToRefreshListView) view.findViewById(R.id.lv_content);
		titleTv = (TextView) view.findViewById(R.id.tv_title);
		rl_fans = (RelativeLayout) view.findViewById(R.id.rl_fans);
		rl_comments = (RelativeLayout) view.findViewById(R.id.rl_comments);
		rl_favorite = (RelativeLayout) view.findViewById(R.id.rl_favorite);
		tv_fans = (TextView) view.findViewById(R.id.fansCount);
		tv_comments = (TextView) view.findViewById(R.id.commentsCount);
		tv_favorite = (TextView) view.findViewById(R.id.favoritesCount);
		list = new ArrayList<PushBean>();
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		titleTv.setText("我的信息");

		/*
		 * setFakeData(); adapter = new PushAdapter(list);
		 * mListView.setAdapter(adapter);
		 */
		setListeners();
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
		UserInfoTask task = new UserInfoTask();
		new GetPushTask().execute("");
		task.execute("");
	}

	@Override
	public void onAttach(Activity activity) {
		this.pActivity = activity;
		super.onAttach(activity);
	}

	/*
	 * private void setFakeData() { list = new ArrayList<PushBean>(); for(int i
	 * = 0;i<4;i++){ list.add(new PushBean()); } }
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void setListeners() {
		rl_fans.setOnClickListener(this);
		rl_comments.setOnClickListener(this);
		rl_favorite.setOnClickListener(this);
		mListView.setMode(Mode.BOTH);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				MessageDetailFragment messageDetailFragment = new MessageDetailFragment();
				((MessageAct) pActivity).dumpToNext(messageDetailFragment,
						"MessageDetailFragment");
			}
		});
		mListView.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				hasMore = true;
				mListView.setMode(Mode.BOTH);
				pageNo = 1;
				list.clear();
				new GetPushTask().execute("");
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				if (hasMore) {
					new GetPushTask().execute("");
				} else {
					mListView.onRefreshComplete();
				}

			}
		});
		mListView.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 粉丝
		case R.id.rl_fans:
			// startActivity(FansActivity.class);
			FanFragment fanFragment = new FanFragment();
			((MessageAct) pActivity).dumpToNext(fanFragment, "FanFragment");
			break;
		// 评论
		case R.id.rl_comments:
			CommenFragment commenFragment = new CommenFragment();
			((MessageAct) pActivity).dumpToNext(commenFragment,
					"CommenFragment");
			break;
		// 喜欢
		case R.id.rl_favorite:
			// startActivity(LoveActivity.class);
			LoveFragment loveFragment = new LoveFragment();
			((MessageAct) pActivity).dumpToNext(loveFragment, "LoveFragment");
			break;
		case R.id.lv_content:
			MessageDetailFragment messageDetailFragment = new MessageDetailFragment();
			((MessageAct) pActivity).dumpToNext(messageDetailFragment,
					"MessageDetailFragment");
			break;
		}
	}

	/*
	 * static class ViewHolder{
	 * 
	 * public ViewHolder(View v){
	 * 
	 * } }
	 */

	/*
	 * private class PushAdapter extends MyBaseAdapter<PushBean>{ private
	 * ViewHolder holder; public PushAdapter(List<PushBean> list) { super(list);
	 * }
	 * 
	 * @Override public View getView(int position, View convertView, ViewGroup
	 * parent) { if(convertView != null){ holder = (ViewHolder)
	 * convertView.getTag(); }else{ convertView =
	 * View.inflate(parent.getContext(), R.layout.item_listview_message_push,
	 * null); holder = new ViewHolder(convertView); convertView.setTag(holder);
	 * }
	 * 
	 * return convertView; }
	 * 
	 * }
	 */

	@Override
	public void onLeave() {

	}

	@Override
	public void onReLoad(Intent paramIntent) {

	}

	class UserInfoTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... arg0) {
			UserDataB udb = ApiClientC.getUserInfo(SP_ID.id);
			if (udb != null) {
				user = udb.getModel();
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (user != null) {

				int fansCount = user.getFansCount();
				int CommentsCount = user.getCommentsCount();
				int praiseCount = user.getVideoPraiseCount();
				tv_comments.setText("" + CommentsCount);
				tv_fans.setText(fansCount + "");
				tv_favorite.setText(praiseCount + "");

			}
		}
	}

	class GetPushTask extends AsyncTask<String, String, String> {
		PushModels videoList;

		@Override
		protected String doInBackground(String... arg0) {
			videoList = ApiClientC.getPushes(SP_ID.id, pageNo);

			return null;
		}

		@Override
		protected void onPreExecute() {
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
				pageNo++;
				list.addAll(videoList.getModels());
				adapter = new PushAdapter(pActivity, list);
				mListView.setAdapter(adapter);
			}

		}
	}
}
