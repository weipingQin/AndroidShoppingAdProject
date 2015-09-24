package com.www.goumei.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;

import com.www.goumei.R;
import com.www.goumei.activity.MainMyAct;
import com.www.goumei.http.req.SP_ID;
import com.www.goumei.utils.DataCleanManager;
import com.www.goumei.utils.SPutil;
import com.www.goumei.utils.UIHelper;


/**
 * 设置
 * @author Eric.Chen
 *
 */
public class AppSetFragment extends BaseFragment  implements  OnClickListener{

	private LinearLayout layout_myuser;
	private LinearLayout layout_general;
	private LinearLayout layout_feedback;
	private LinearLayout layout_about;
	private LinearLayout layout_cache;
	private Button appExit;
	private TextView last;
	/**缓存大小*/
	private TextView cacheCount;
    Activity  pActivity;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View    view=inflater.inflate(R.layout.activity_appset, container, false);
		last = (TextView) view.findViewById(R.id.appset_last);
		layout_myuser = (LinearLayout)view. findViewById(R.id.layout_myuser);
		layout_general = (LinearLayout) view.findViewById(R.id.layout_general);
		layout_feedback = (LinearLayout) view.findViewById(R.id.layout_feedback);
		layout_about = (LinearLayout) view.findViewById(R.id.layout_about);
		layout_cache = (LinearLayout) view.findViewById(R.id.layout_cache);
		appExit = (Button)view. findViewById(R.id.appExit);
		cacheCount = (TextView) view.findViewById(R.id.cacheCount);
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		initData();
		setListeners();
		super.onActivityCreated(savedInstanceState);
	}

  @Override
public void onAttach(Activity activity) {
	// TODO Auto-generated method stub
	super.onAttach(activity);
	this.pActivity=activity;
}
	
	protected void setListeners() {
		layout_myuser.setOnClickListener(this);
		layout_general.setOnClickListener(this);
		layout_feedback.setOnClickListener(this);
		layout_about.setOnClickListener(this);
		layout_cache.setOnClickListener(this);
		appExit.setOnClickListener(this);
		last.setOnClickListener(this);
	}
	public void initData() {
		// TODO Auto-generated method stub
		//拿到缓存大小设置上去
		cacheCount.setText(DataCleanManager.getTotalCacheSize(pActivity));
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.appset_last:
		     ((MainMyAct)pActivity).goBackAndShowPreView(null);
			break;
		//我的账号
		case R.id.layout_myuser:
			MyAccountFragment    myAccountFragment=new   MyAccountFragment();
			((MainMyAct)pActivity).dumpToNext(myAccountFragment, "MyAccountFragment");
			break;
		//通用
		case R.id.layout_general:
			GeneralFragment    generalFragment=new   GeneralFragment();
			((MainMyAct)pActivity).dumpToNext(generalFragment, "GeneralFragment");
			break;
		//意见反馈
		case R.id.layout_feedback:
			FeedbackFragment    feedbackFragment=new   FeedbackFragment();
			((MainMyAct)pActivity).dumpToNext(feedbackFragment, "FeedbackFragment");
			break;
		//关于微视购
		case R.id.layout_about:
			
			break;
		//清除缓存
		case R.id.layout_cache:
			DataCleanManager.cleanInternalCache(pActivity);
			cacheCount.setText(DataCleanManager.getTotalCacheSize(pActivity));
			UIHelper.showClearMsg(getActivity());
			break;
		//退出程序
		case R.id.appExit:
			 UIHelper.showChose(getActivity(), "确认退出？", "确认", new DialogInterface.OnClickListener() {
	                @Override
	                public void onClick(DialogInterface dialog, int which) {
	                	/*SharedPreferences sharedPreferences=getSharedPreferences(LianKaLoginFragment.SETLIANKALOGIN_STAUS, MODE_PRIVATE);
						sharedPreferences.edit().putInt("login_state", 0).commit();*/
	                	SPutil.clear(pActivity);
	                	SP_ID.id="0";
	                	for(Platform p:ShareSDK.getPlatformList()){
	                		p.removeAccount(true);
	                	}
	                	System.exit(0);
	                }
	            }
	                    , "取消", new DialogInterface.OnClickListener() {
	                @Override
	                public void onClick(DialogInterface dialog, int which) {

	                }
	            });
			break;
		default:
			break;
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

}
