package com.www.goumei.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.mob.tools.utils.UIHandler;
import com.www.goumei.R;
import com.www.goumei.activity.MainMyAct;
import com.www.goumei.adapter.PushAdapter;
import com.www.goumei.bean.AccountData;
import com.www.goumei.bean.AccountModels;
import com.www.goumei.bean.PushModels;
import com.www.goumei.fragment.MeFragment.LoginTask;
import com.www.goumei.http.req.ApiClientC;
import com.www.goumei.http.req.SP_ID;
import com.www.goumei.utils.Constant;
import com.www.goumei.utils.L;
import com.yixia.camera.demo.utils.ToastUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 我的帐号
 * 
 * @author Eric.Chen
 * 
 */
public class MyAccountFragment extends BaseFragment implements OnClickListener,
		PlatformActionListener, Callback {

	private TextView last;
	private TextView next;
	private TextView myPhone;
	private LinearLayout layout_xinlang;
	private LinearLayout layout_wechat;
	private LinearLayout layout_qq;
	private ImageView xinlangBind;
	private ImageView wechatBind;
	private ImageView qqBind;
	private TextView title;
	private List<AccountData> accounts;
	Activity pActivity;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.pActivity = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.activity_myaccountactivity,
				container, false);
		ShareSDK.initSDK(getActivity());
		last = (TextView) view.findViewById(R.id.tv_prior);
		next = (TextView) view.findViewById(R.id.tv_later);
		title = (TextView) view.findViewById(R.id.tv_title);
		myPhone = (TextView) view.findViewById(R.id.myPhone);
		layout_xinlang = (LinearLayout) view.findViewById(R.id.layout_xinlang);
		layout_wechat = (LinearLayout) view.findViewById(R.id.layout_wechat);
		layout_qq = (LinearLayout) view.findViewById(R.id.layout_qq);
		xinlangBind = (ImageView) view.findViewById(R.id.xinlangBind);
		wechatBind = (ImageView) view.findViewById(R.id.wechatBind);
		qqBind = (ImageView) view.findViewById(R.id.qqBind);
		accounts = new ArrayList<AccountData>();
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		initView();
		setListeners();
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		new GetPushTask().execute("");
	}

	public void initView() {
		// TODO Auto-generated method stub
		title.setText("我的账号");
		next.setVisibility(View.GONE);

	}

	public void setListeners() {
		last.setOnClickListener(this);
		layout_xinlang.setOnClickListener(this);
		layout_wechat.setOnClickListener(this);
		layout_qq.setOnClickListener(this);
		xinlangBind.setOnClickListener(this);
		wechatBind.setOnClickListener(this);
		qqBind.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Platform pf;
		switch (v.getId()) {
		
		case R.id.tv_prior:
			((MainMyAct) pActivity).goBackAndShowPreView(null);
			break;
		// 新浪绑定
		case R.id.layout_xinlang:
		case R.id.xinlangBind:
			 pf = ShareSDK.getPlatform(SinaWeibo.NAME);
			pf.setPlatformActionListener(MyAccountFragment.this);
			pf.authorize();
			break;
		// 微信号绑定
		case R.id.layout_wechat:
		case R.id.wechatBind:
			pf = ShareSDK.getPlatform(Wechat.NAME);
			pf.setPlatformActionListener(MyAccountFragment.this);
			pf.authorize();
			break;
		// QQ绑定
		case R.id.layout_qq:
		case R.id.qqBind:
			 pf = ShareSDK.getPlatform(QQ.NAME);
			pf.setPlatformActionListener(MyAccountFragment.this);
			pf.authorize();
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

	class GetPushTask extends AsyncTask<String, String, String> {
		AccountModels videoList;

		@Override
		protected String doInBackground(String... arg0) {
			videoList = ApiClientC.getAccounts(1);

			return null;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (videoList != null) {
				if (videoList.getModels() != null
						&& videoList.getModels().size() > 0) {
					accounts = videoList.getModels();
					handleAccounts();
				}

			} else {
				ToastUtils.showToast(getActivity(), "获取数据失败，请返回重试");
			}

		}
	}

	private void handleAccounts() {
		for (AccountData ad : accounts) {
			if (ad.getAccountType() == 1 && ad.isIsBind()) {// qq
				qqBind.setVisibility(View.GONE);

			}
			if (ad.getAccountType() == 2 && ad.isIsBind()) {// 微信
				wechatBind.setVisibility(View.GONE);
			}
			if (ad.getAccountType() == 3 && ad.isIsBind()) {// 新浪
				xinlangBind.setVisibility(View.GONE);
			}
			myPhone.setText(ad.getPhone());
		}
	}

	/**
	 * 绑定账号
	 * 
	 * @author json
	 * 
	 */
	class BindAccountTask extends AsyncTask<String, String, String> {
		String code;
		String openID;
		Platform platform;
		int type;

		public BindAccountTask(String openID, Platform platform) {
			this.openID = openID;
			this.platform = platform;
			if (QQ.NAME.equals(platform.getName())) {// qq
				this.type = MeFragment.TYPE_QQ;
			} else if (Wechat.NAME.equals(platform.getName())) {// 微信
				this.type = MeFragment.TYPE_WECHAT;
			} else if (SinaWeibo.NAME.equals(platform.getName())) {// 新浪
				this.type = MeFragment.TYPE_SINA;
			}
		}

		@Override
		protected String doInBackground(String... arg0) {
			code = ApiClientC.bindClient(Integer.parseInt(SP_ID.id), openID,
					type);

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			L.e("code:::::::::::::::::::::::::" + code);
			if (code != null && code.equals("1")) {
				ToastUtils.showLongToast("绑定成功");
				new GetPushTask().execute("");
			}

		}
	}

	@Override
	public void onCancel(Platform arg0, int arg1) {
		// TODO Auto-generated method stub
		Message msg = new Message();
		msg.arg1 = 3;
		msg.arg2 = arg1;
		msg.obj = arg0;
		UIHandler.sendMessage(msg, MyAccountFragment.this);
	}

	@Override
	public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
		// TODO Auto-generated method stub
		if (arg1 == Platform.ACTION_AUTHORIZING) {
			Message msg = new Message();
			msg.arg1 = 1;
			msg.arg2 = arg1;
			msg.obj = arg0;
			UIHandler.sendMessage(msg, MyAccountFragment.this);
		}
	}

	@Override
	public void onError(Platform arg0, int arg1, Throwable arg2) {
		// TODO Auto-generated method stub
		Message msg = new Message();
		msg.arg1 = 2;
		msg.arg2 = arg1;
		msg.obj = arg0;
		UIHandler.sendMessage(msg, MyAccountFragment.this);
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		Platform plat = (Platform) msg.obj;
		String text = MeFragment.actionToString(msg.arg2);
		switch (msg.arg1) {
		case 1: {
			// 成功
			// text = plat.getName() + " completed at " +
			// text+plat.getDb().getUserId();
			// ToastUtils.showToast(pActivity, text);
			String openID = plat.getDb().getUserId();
			L.e("openID::::::::::" + openID);

			if (openID != null)
			new BindAccountTask(openID, plat).execute("");
		}
			break;
		case 2: {
			// 失败
			text = plat.getName() + " caught error at " + text;
			ToastUtils.showToast(pActivity, text);
			return false;
		}
		case 3: {
			// 取消
			text = plat.getName() + " canceled at " + text;
			ToastUtils.showToast(pActivity, text);
			return false;
		}
		}
		return false;
	}

}
