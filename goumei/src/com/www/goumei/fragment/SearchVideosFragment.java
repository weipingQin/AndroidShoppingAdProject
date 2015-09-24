package com.www.goumei.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.www.goumei.R;
import com.www.goumei.activity.MainHomeAct;
import com.www.goumei.adapter.WorksAdapter;
import com.www.goumei.bean.Videos;
import com.www.goumei.bean.VideosDataList;
import com.www.goumei.http.req.ApiClientC;
import com.www.goumei.utils.Constant;
import com.yixia.camera.demo.utils.ToastUtils;

/*import com.www.wsgou.R;
import com.www.wsgou.base.BaseActivity;
import com.www.wsgou.bean.BodyInfo;
import com.www.wsgou.bean.ModelsInfo;
import com.www.wsgou.bean.UserInfo;
import com.www.wsgou.http.FindUserTask;*/

/**
 * 
* @ClassName: FindfriendActivity 
* @Description: TODO
* @author sunyouyi
* @date 2015-6-5 下午10:36:46 
*
 */
public class SearchVideosFragment extends BaseFragment  implements OnClickListener{
	public static final int FIND_FRIEND = 1000;
	public static final int FIND_FRIEND_RESULT=1001;
	private TextView last;
	private EditText findfriend;
	private ImageView deleteIcon;
	private PullToRefreshGridView ptrGV;
	//private FindUserTask task;
	private   static   final   int  GetMYFriend=0;
	Activity  pActivity;
	private static List<Videos> mWorks = new ArrayList<Videos>();
	String value="";
	private int worksPageNo = 1;
	private boolean worksHasMore = true;
	String result="";
	private WorksAdapter worksAdapter;  
	String userId="1";
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	View   view=inflater.inflate(R.layout.activity_search_voideo, container,false);
    	last = (TextView) view.findViewById(R.id.findfriend_last);
		findfriend = (EditText) view.findViewById(R.id.findfriend);
		deleteIcon = (ImageView)view. findViewById(R.id.find_deleteIcon);
		ptrGV = (PullToRefreshGridView) view.findViewById(R.id.worksGV);
		ptrGV.setMode(Mode.BOTH);
		ptrGV.getRefreshableView().setNumColumns(2);
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
    	return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	setListeners();
    		
    	super.onActivityCreated(savedInstanceState);
    }
    @Override
    public void onStart() {
    	// TODO Auto-generated method stub
    	super.onStart();
    }
    @Override
    public void onDestroy() {
    	super.onDestroy();
    }
    @Override
    public void onAttach(Activity activity) {
    	// TODO Auto-generated method stub
    	pActivity=activity;
    	super.onAttach(activity);
    }
	protected void setListeners() {
		last.setOnClickListener(this);
		findfriend.setOnClickListener(this);
		deleteIcon.setOnClickListener(this);
		findfriend.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View arg0, int keyCode, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if(keyCode==KeyEvent.KEYCODE_ENTER){
					if(!TextUtils.isEmpty(findfriend.getText().toString())){
						((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
						.hideSoftInputFromWindow(
						getActivity()
						.getCurrentFocus()
						.getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
						new GetVideosTask().execute("");
						worksHasMore = true;
						ptrGV.setMode(Mode.BOTH);
						worksPageNo = 1;
						mWorks.clear();
						new GetVideosTask().execute("");
					}else{
						ToastUtils.showToast("请输入关键字");
					}
					
				}
				return false;
			}
		});
		findfriend.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (count > 0) {
					deleteIcon.setVisibility(View.VISIBLE);
					value = findfriend.getText().toString();
					/*task = new FindUserTask(value, handler);
					task.execute(FIND_FRIEND);*/
					
				} else {
					deleteIcon.setVisibility(View.INVISIBLE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.findfriend_last:
		   ((MainHomeAct)pActivity).goBackAndShowPreView(null);
			break;

		case R.id.find_deleteIcon:
			findfriend.setText("");
			worksHasMore = true;
			ptrGV.setMode(Mode.BOTH);
			worksPageNo = 1;
			mWorks.clear();
			break;

		default:
			break;
		}
	}

	/*Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case FIND_FRIEND_RESULT:
				BodyInfo info=(BodyInfo) msg.obj;
				Map<String, List<ModelsInfo>> body = info.getBody();
				
				
				break;

			default:
				break;
			}
		}
	};*/

	@Override
	public void onLeave() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReLoad(Intent paramIntent) {
		// TODO Auto-generated method stub
		
	}
	
	class GetVideosTask extends AsyncTask<String, String, String> {
		VideosDataList videoList;

		@Override
		protected String doInBackground(String... arg0) {
			videoList = ApiClientC.getVideosBySearchKeywords(findfriend.getText().toString(), worksPageNo);

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
				mWorks.addAll(videoList.getModels());
				worksAdapter = new WorksAdapter(SearchVideosFragment.this.getActivity(), mWorks);
				ptrGV.setAdapter(worksAdapter);
			}

		}
	}
}
