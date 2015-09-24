package com.www.goumei.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.www.goumei.R;
import com.www.goumei.activity.MessageAct;
import com.www.goumei.adapter.PushDetailAdapter;
import com.www.goumei.bean.PushBean;
import com.www.goumei.bean.PushModels;
import com.www.goumei.bean.UserData;
import com.www.goumei.http.req.ApiClientC;
import com.www.goumei.http.req.SP_ID;
import com.www.goumei.utils.Constant;
import com.yixia.camera.demo.utils.ToastUtils;

/**
 * 信息
 * @author Eric.Chen
 *
 */
public class MessageDetailFragment extends BaseFragment  implements OnClickListener {
	private TextView titleTv,backTV,tv_later;
	private PullToRefreshListView mListView;
	private List<PushBean> list;
	private UserData user;
	private int pageNo=1;
	private PushDetailAdapter adapter;
	Activity  pActivity;
	boolean hasMore;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	View   view=inflater.inflate(R.layout.fragment_message_detail, container,false);
    			titleTv = (TextView) view.findViewById(R.id.tv_title);
		mListView = (PullToRefreshListView) view.findViewById(R.id.lv_content);
		titleTv = (TextView) view.findViewById(R.id.tv_title);
		backTV=(TextView)view.findViewById(R.id.tv_prior);
		tv_later=(TextView)view.findViewById(R.id.tv_later);
		list=new ArrayList<PushBean>();
		return  view ;
    }	
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	titleTv.setText("推送信息");
		tv_later.setVisibility(View.GONE);
		/*setFakeData();
		adapter = new PushAdapter(list);
		mListView.setAdapter(adapter);*/
		setListeners();
    	super.onActivityCreated(savedInstanceState);
    }
    @Override
    public void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	new GetPushTask().execute("");
    }
    @Override
    public void onAttach(Activity activity) {
    	// TODO Auto-generated method stub
    	this.pActivity=activity;
    	super.onAttach(activity);
    }

	
	/*private void setFakeData() {
		list = new ArrayList<PushBean>();
		for(int i = 0;i<4;i++){
			list.add(new PushBean());
		}
	}*/
	protected void setListeners() {
		backTV.setOnClickListener(this);
		mListView.setMode(Mode.BOTH);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(list!=null&&arg2<list.size()){
					
				}
				
			}
		});
		mListView.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				// TODO Auto-generated method stub
				hasMore = true;
				mListView.setMode(Mode.BOTH);
				pageNo = 1;
				list.clear();
				new GetPushTask().execute("");
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				// TODO Auto-generated method stub
				if (hasMore) {
					new GetPushTask().execute("");
				} else {
					mListView.onRefreshComplete();
				}

			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//粉丝
		case R.id.rl_fans:
			//startActivity(FansActivity.class);
			FanFragment  fanFragment=new FanFragment();
			((MessageAct)pActivity).dumpToNext(fanFragment, "FanFragment");
			break;
		//评论
		case R.id.rl_comments:
			CommenFragment  commenFragment=new CommenFragment();
			((MessageAct)pActivity).dumpToNext(commenFragment, "CommenFragment");
			break;
		//喜欢
		case R.id.rl_favorite:
			//startActivity(LoveActivity.class);
			LoveFragment  loveFragment=new LoveFragment();
			((MessageAct)pActivity).dumpToNext(loveFragment, "LoveFragment");
			break;
		case R.id.tv_prior:
			((MessageAct)pActivity).goBackAndShowPreView(null);

			
			break;

		}
	}
	

	
	/*static class ViewHolder{
		
		public ViewHolder(View v){
			
		}
	}*/
	
	/*private class PushAdapter extends MyBaseAdapter<PushBean>{
		private ViewHolder holder;
		public PushAdapter(List<PushBean> list) {
			super(list);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView != null){
				holder = (ViewHolder) convertView.getTag();
			}else{
				convertView = View.inflate(parent.getContext(), R.layout.item_listview_message_push, null);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			}
			
			return convertView;
		}
		
	}*/

	
	
	@Override
	public void onLeave() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReLoad(Intent paramIntent) {
		// TODO Auto-generated method stub
		
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
				adapter = new PushDetailAdapter(getActivity(), list);
				mListView.setAdapter(adapter);
			}

		}
	}
}
