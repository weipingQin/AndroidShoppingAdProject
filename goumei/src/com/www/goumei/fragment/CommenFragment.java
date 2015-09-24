package com.www.goumei.fragment;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.www.goumei.R;
import com.www.goumei.activity.MessageAct;
import com.www.goumei.adapter.CommentAdapter;
import com.www.goumei.bean.CommentData;
import com.www.goumei.bean.CommentDataList;
import com.www.goumei.http.req.ApiClientC;
import com.www.goumei.http.req.SP_ID;
import com.www.goumei.utils.Constant;
import com.www.goumei.utils.L;

public class CommenFragment extends BaseFragment implements OnClickListener {
	private PullToRefreshListView mListView;
	private CommentAdapter adapter;
	private List<CommentData> list;
	private TextView titleTv;
	TextView tv_later, tv_prior;
	Activity pActivity;
	boolean hasMore;
	private int pageNo = 1;
	private RefreshMainFragmentReceiver refreshMainFragmentReceiver;
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			super.handleMessage(msg);
			if (msg.what == 1) {
				// 在这里刷新界面适配器
				adapter.notifyDataSetChanged();
			}
			if (msg.what == 0) {

			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.activity_fans, container, false);
		mListView = (PullToRefreshListView) view.findViewById(R.id.lv_content);
		titleTv = (TextView) view.findViewById(R.id.tv_title);
		mListView.setMode(Mode.BOTH);
		tv_later = (TextView) view.findViewById(R.id.tv_later);
		tv_later.setVisibility(View.INVISIBLE);
		tv_prior = (TextView) view.findViewById(R.id.tv_prior);
		tv_prior.setOnClickListener(this);
		list = new ArrayList<CommentData>();
		mListView.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				// TODO Auto-generated method stub
				hasMore = true;
				mListView.setMode(Mode.BOTH);
				pageNo = 1;
				list.clear();
				new GetVideosTask().execute("");
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				// TODO Auto-generated method stub
				if (hasMore) {
					new GetVideosTask().execute("");
				} else {
					mListView.onRefreshComplete();
				}

			}
		});
		new GetVideosTask().execute("");
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		titleTv.setText("评论");
		adapter = new CommentAdapter(pActivity, list);
		mListView.setAdapter(adapter);
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.pActivity = activity;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		refreshMainFragmentReceiver = new RefreshMainFragmentReceiver();
		IntentFilter filter0 = new IntentFilter();
		filter0.addAction(Constant.ACTION_COMMENT_DELETE);
		pActivity.registerReceiver(refreshMainFragmentReceiver, filter0);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		pActivity.unregisterReceiver(refreshMainFragmentReceiver);
	}

	protected void setListeners() {
		tv_prior.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_prior:
			((MessageAct) pActivity).goBackAndShowPreView(null);
			break;

		default:
			break;
		}
	}

	static class ViewHolder {

		public ViewHolder(View v) {

		}
	}

	@Override
	public void onLeave() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReLoad(Intent paramIntent) {
		// TODO Auto-generated method stub

	}

	class GetVideosTask extends AsyncTask<String, String, String> {
		CommentDataList videoList;

		@Override
		protected String doInBackground(String... arg0) {
			videoList = ApiClientC.getCommentById(SP_ID.id, pageNo);

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
				pageNo++;
				list.addAll(videoList.getModels());
				adapter = new CommentAdapter(pActivity, list);
				mListView.setAdapter(adapter);
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
						if (Constant.INTENT_STATUS_FALSE.equals(status)) {
							if(index<list.size())
							list.remove(index);
						} else {
						}

						Message message = new Message();
						message.what = 1;
						mHandler.sendMessage(message);
					}

				}
			}).start();
		}

	}

}
