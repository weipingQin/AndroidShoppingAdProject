package com.www.goumei.fragment;

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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.www.goumei.R;
import com.www.goumei.activity.MainMyAct;
import com.www.goumei.adapter.FriendAdapter;
import com.www.goumei.bean.FriendData;
import com.www.goumei.bean.FriendsDataList;
import com.www.goumei.http.ApiClient;
import com.www.goumei.utils.Constant;
import com.www.goumei.utils.L;
import com.www.goumei.utils.ProcessUtil;
import com.www.goumei.utils.UIHelper;

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
public class FindfriendFragment extends BaseFragment  implements OnClickListener{
	public static final int FIND_FRIEND = 1000;
	public static final int FIND_FRIEND_RESULT=1001;
	private TextView last;
	private EditText findfriend;
	private ImageView deleteIcon;
	private ListView friendList;
	//private FindUserTask task;
	private   static   final   int  GetMYFriend=0;
	Activity  pActivity;
	String value="";
	String result="";
	FriendsDataList  friendsDataList;
	FriendAdapter friendAdapter;
	List<FriendData>  models;
   
	private RefreshMainFragmentReceiver refreshMainFragmentReceiver;
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			super.handleMessage(msg);
			if (msg.what == 1) {
				// 在这里刷新界面适配器
				friendAdapter.notifyDataSetChanged();
			}
			if (msg.what == 0) {

			}
		}
	};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	View   view=inflater.inflate(R.layout.activity_findfriend, container,false);
    	last = (TextView) view.findViewById(R.id.findfriend_last);
		findfriend = (EditText) view.findViewById(R.id.findfriend);
		deleteIcon = (ImageView)view. findViewById(R.id.find_deleteIcon);
		friendList = (ListView) view.findViewById(R.id.friendList);
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
    	refreshMainFragmentReceiver = new RefreshMainFragmentReceiver();
		IntentFilter filter0 = new IntentFilter();
		filter0.addAction(Constant.ACTION_ATTENTION_STATUS);
		filter0.addAction(Constant.ACTION_FIND_FRIEND);
		getActivity().registerReceiver(refreshMainFragmentReceiver, filter0);
    }
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	getActivity().unregisterReceiver(refreshMainFragmentReceiver);
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
		findfriend.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (count > 0) {
					deleteIcon.setVisibility(View.VISIBLE);
					value = findfriend.getText().toString();
					/*task = new FindUserTask(value, handler);
					task.execute(FIND_FRIEND);*/
					DialogHelper   dialogHelper=new DialogHelper();
					dialogHelper.execute(GetMYFriend);
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
		   ((MainMyAct)pActivity).goBackAndShowPreView(null);
			break;

		case R.id.find_deleteIcon:
			findfriend.setText("");
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
	public class DialogHelper extends AsyncTask<Integer, Void, Integer> {
        public DialogHelper(){
        }
       

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
        	
			
				ProcessUtil.showProgressDialog(pActivity, "获取好友中..."); 
			}
      
		@Override
		protected Integer doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			 switch (params[0]) {
				case GetMYFriend :
					/*if(!myAttentionAddMoredata){
						pageNo=1;
						videosDataList=ApiClient.getMyAttention(0,pageNo,PAGESIZE);
					}else{
						addMoreVideosDataList=ApiClient.getMyAttention(0,pageNo,PAGESIZE);
					}*/
						friendsDataList=ApiClient.getSearchFriend(value, 1, 50);
					
				   // String aString=ApiClient.getGoodsList3();
					return GetMYFriend;
				
			
					
				
				default:
					return -1;
			 }
			
			 
		}


		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			switch (result) {
			case GetMYFriend:
				ProcessUtil.dismissProgressdialog();
				if(friendsDataList!=null){
					 models=friendsDataList.getModels();
					 if(models!=null && models.size()>0){
						 UIHelper.showMsg(pActivity, "成功搜索到好友");
						 if(friendAdapter==null){
							friendAdapter=new FriendAdapter(pActivity, models); 
							friendList.setAdapter(friendAdapter);
						 }else{
							 friendAdapter.setData(models);
							 friendAdapter.notifyDataSetChanged();
						 }
					 }else{
						 UIHelper.showMsg(pActivity, "没有搜索到好友"); 
					 }
				}else{
					 UIHelper.showMsg(pActivity, "搜索好友失败"); 
				}
			
				break;
			
			default:
				break;
			}
			super.onPostExecute(result);
		}
	}
	
	public class RefreshMainFragmentReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, final Intent intent) {
			
			new Thread(new Runnable() {
				@Override
				public void run() {
					
					int index=intent.getIntExtra(Constant.INTENT_INDEX, -1);
					String status=intent.getStringExtra(Constant.INTENT_STATUS);
					L.e("......."+status+index);
					if(index>=0){
						if(Constant.INTENT_STATUS_TRUE.equals(status)){
							models.get(index).setIsAttention(1);
						}else{
							models.get(index).setIsAttention(0);
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
