package com.www.goumei.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

import com.mob.tools.utils.UIHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.www.goumei.R;
import com.www.goumei.activity.HomePageActivity;
import com.www.goumei.activity.MainMyAct;
import com.www.goumei.bean.UserData;
import com.www.goumei.bean.UserDataB;
import com.www.goumei.bean.UserInfo;
import com.www.goumei.bean.VideosDataList;
import com.www.goumei.http.req.ApiClientC;
import com.www.goumei.http.req.SP_ID;
import com.www.goumei.utils.Constant;
import com.www.goumei.utils.L;
import com.www.goumei.utils.SPutil;
import com.www.goumei.utils.StringUtils;
import com.www.goumei.utils.UIHelper;
import com.www.goumei.views.CircleImageView;
import com.yixia.camera.demo.utils.ToastUtils;

/**
 * 我的
 */
public class MeFragment extends BaseFragment  implements OnClickListener ,PlatformActionListener,Callback{
	private TextView titleTv;
	private RelativeLayout rl_draft;
	private RelativeLayout rl_config;
	private RelativeLayout rl_personaldata;
	private RelativeLayout rl_shop;
	private RelativeLayout rl_findfriend;
	private TextView count;
	private ImageView authentication;
	private TextView works;
	private TextView attention;
	private TextView fans;
	private TextView usernameTV;
	private TextView userID;
	private ImageView verifiedTV;
	private CircleImageView avarIV;
	private ImageView iv_v;
	private Intent intent;
    Activity  pActivity;
    RelativeLayout layout_Pdata;
	private UserData user;
	public ImageLoader imageLoader; 
	private static DisplayImageOptions options;
	private static boolean isLogin=false;
	private AlertDialog builder;
	
	public static final int TYPE_QQ=1;
	public static final int TYPE_WECHAT=2;
	public static final int TYPE_SINA=3;
	private boolean isReg=false;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ShareSDK.initSDK(getActivity());
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View rootView = inflater.inflate(R.layout.fragment_me, container, false);
		initView(rootView);
		return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setListeners();
		super.onActivityCreated(savedInstanceState);
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
		
		 options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.test)
		.showImageForEmptyUri(R.drawable.test)
		.showImageOnFail(R.drawable.test)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
	}
    @Override
    public void onAttach(Activity activity) {
    	// TODO Auto-generated method stub
    	this.pActivity=activity;
    	super.onAttach(activity);
    }
    @Override
    public void onResume() {
    	super.onResume();
    	Bundle  bundle =getArguments();
    	if(bundle!=null){
    	isLogin=bundle.getBoolean("is_login");
    	isLogin=true;
	    
		
    	}
    	 UserInfoTask task=new UserInfoTask();
	     task.execute("");
	     new GetUnPublishVideosTask().execute("");
    	
    }
	/**
	 * 初始化控件
	 * @param v
	 */
	private void initView(View v) {
		titleTv = (TextView) v.findViewById(R.id.tv_title);
		titleTv.setText("我的主页");
		rl_findfriend = (RelativeLayout) v.findViewById(R.id.rl_findfriend);
		rl_draft = (RelativeLayout) v.findViewById(R.id.rl_draft);
		rl_config = (RelativeLayout) v.findViewById(R.id.rl_config);
		rl_personaldata = (RelativeLayout) v.findViewById(R.id.rl_personaldata);
		rl_shop = (RelativeLayout) v.findViewById(R.id.rl_shop);
		
		usernameTV=(TextView)v.findViewById(R.id.tv_name);
		userID=(TextView)v.findViewById(R.id.userID);
		verifiedTV=(ImageView)v.findViewById(R.id.iv_verified);
		avarIV=(CircleImageView)v.findViewById(R.id.iv_portrait);
		iv_v=(ImageView)v.findViewById(R.id.iv_v);
		works = (TextView) v.findViewById(R.id.tv_works);
		attention = (TextView) v.findViewById(R.id.tv_attention);
		fans = (TextView) v.findViewById(R.id.tv_fans);
		count = (TextView) v.findViewById(R.id.tv_count);
		authentication = (ImageView) v.findViewById(R.id.iv_authentication);
		layout_Pdata = (RelativeLayout) v.findViewById(R.id.layout_Pdata);
	}


	
	protected void setListeners() {
		rl_findfriend.setOnClickListener(this);
		rl_draft.setOnClickListener(this);
		rl_config.setOnClickListener(this);
		rl_personaldata.setOnClickListener(this);
		rl_shop.setOnClickListener(this);
		layout_Pdata.setOnClickListener(this);
		works.setOnClickListener(this);
		fans.setOnClickListener(this);
		attention.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_works://作品
			Intent homeIntent1=new Intent(pActivity,HomePageActivity.class);
			homeIntent1.putExtra(Constant.OTHER_ID, SP_ID.id);
			homeIntent1.putExtra(Constant.HOME_PAGE_INDEX, 1);
			pActivity.startActivity(homeIntent1);
			break;
		case R.id.tv_attention://关注
			Intent homeIntent2=new Intent(pActivity,HomePageActivity.class);
			homeIntent2.putExtra(Constant.OTHER_ID, SP_ID.id);
			homeIntent2.putExtra(Constant.HOME_PAGE_INDEX, 2);
			pActivity.startActivity(homeIntent2);
			break;
		case R.id.tv_fans://粉丝
			Intent homeIntent3=new Intent(pActivity,HomePageActivity.class);
			homeIntent3.putExtra(Constant.OTHER_ID, SP_ID.id);
			homeIntent3.putExtra(Constant.HOME_PAGE_INDEX, 3);
			pActivity.startActivity(homeIntent3);
			break;
		//找好友
		case R.id.rl_findfriend:
		    FindfriendFragment   findfriendFragment=new FindfriendFragment();
		    ((MainMyAct)pActivity).dumpToNext(findfriendFragment, "FindfriendFragment");
			break;
		//草稿箱
		case R.id.rl_draft:
		/*	intent = new Intent(getActivity(), DraftboxActivity.class);
			startActivity(intent);*/
			DraftboxFragment   draftboxFragment=new DraftboxFragment();
		    ((MainMyAct)pActivity).dumpToNext(draftboxFragment, "DraftboxFragment");
			break;
		//设置
		case R.id.rl_config:
		/*	intent=new Intent(getActivity(), AppSetActivity.class);
			startActivity(intent);*/
			AppSetFragment   appSetFragment=new AppSetFragment();
			((MainMyAct)pActivity).dumpToNext(appSetFragment, "AppSetFragment");
			break;
		//个人资料
		case R.id.rl_personaldata:
			/*intent=new Intent(getActivity(), PersonaldataActivity.class);
			startActivity(intent);*/
			
			PersonaldataFragment   personaldataFragment=new PersonaldataFragment();
			((MainMyAct)pActivity).dumpToNext(personaldataFragment, "PersonaldataFragment");
			
			break;
		//店铺认证
		case R.id.rl_shop:
//			intent = new Intent(getActivity(), ShopCertificationActivity.class);	
//			//认证情况--1.未认证;2.认证中;3.已认证
//			intent.putExtra("type", 1);
//			intent.putExtra("shopName", "");
//			intent.putExtra("contact", "");
//			intent.putExtra("phone", "");
//			intent.putExtra("qq", "");
//			intent.putExtra("email", "");
//			startActivity(intent);
			
			ShopCertificationFragment   shopCertificationFragment=new ShopCertificationFragment();
			Bundle  bundle=new Bundle();
			if(user!=null){
				bundle.putInt(Constant.SHOPID, user.getShopID());
				bundle.putSerializable(Constant.USERDATA, user);
				
			}
			
			shopCertificationFragment.setArguments(bundle);
			((MainMyAct)pActivity).dumpToNext(shopCertificationFragment, "ShopCertificationFragment");
			break;
		case  R.id.layout_Pdata:
			
//			if (isLogin) {
				if (false) {
					Intent homeIntent=new Intent(pActivity,HomePageActivity.class);
					homeIntent.putExtra(Constant.OTHER_ID, SP_ID.id);
					pActivity.startActivity(homeIntent);
//				PersonalHomeFragment home=new PersonalHomeFragment();
//				Bundle bundleHome=new Bundle();
//				bundleHome.putString(Constant.OTHER_ID, SP_ID.id+"");
//				home.setArguments(bundleHome);
//				((MainMyAct)pActivity).dumpToNext(home, "PersonalHomeFragment");
			}else {
				//未登录，打开选择登录的对话框
				View view = LayoutInflater.from(pActivity).inflate(R.layout.activity_selectloging, null);
				builder = new AlertDialog.Builder(pActivity).create();
				TextView telelogin=(TextView) view.findViewById(R.id.tv_telelogin);
				TextView weixin=(TextView) view.findViewById(R.id.tv_weixin);
				TextView qq=(TextView) view.findViewById(R.id.tv_qq);
				TextView xinlang=(TextView) view.findViewById(R.id.tv_weibo);
				TextView close=(TextView) view.findViewById(R.id.tv_close);
				telelogin.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						builder.dismiss();
					LoginFragment   loginFragment=new LoginFragment();
					((MainMyAct)pActivity).dumpToNext(loginFragment, "LoginFragment");
					
					}
				});
				weixin.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Platform pf=ShareSDK.getPlatform(Wechat.NAME);
						pf.setPlatformActionListener(MeFragment.this);
						pf.authorize();
						builder.dismiss();
					}
				});
				qq.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Platform pf=ShareSDK.getPlatform(QQ.NAME);
						pf.setPlatformActionListener(MeFragment.this);
						pf.authorize();
						builder.dismiss();
					}
				});
				xinlang.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Platform pf=ShareSDK.getPlatform(SinaWeibo.NAME);
						pf.setPlatformActionListener(MeFragment.this);
						pf.authorize();
						builder.dismiss();
					}
				});
				close.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						builder.dismiss();
					}
				});				
				builder.show();
				// 得到属性
				WindowManager.LayoutParams params = builder.getWindow()
						.getAttributes();
				params.gravity = Gravity.CENTER;// 显示在中间
				// 设置对话框的宽度为手机屏幕的0.8
				params.width = (int) (pActivity.getWindowManager()
						.getDefaultDisplay().getWidth() * 0.8);
				// 设置对话框的高度为手机屏幕的0.6
				params.height = (int) (pActivity.getWindowManager()
						.getDefaultDisplay().getHeight() * 0.6);
				builder.getWindow().setAttributes(params);// 設置屬性
				builder.getWindow().setContentView(view);// 把自定義view加上去
			}
		
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
				new UserInfoTask().execute("");
	}
	class UserInfoTask extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... arg0) {
				UserDataB udb=ApiClientC.getUserInfo(String.valueOf(SP_ID.id));
				if(udb!=null){
					user=udb.getModel();
				}
			
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if(user!=null){
				if(!StringUtils.isEmpty(user.getDisplayName()))
				usernameTV.setText(user.getDisplayName());
				SharedPreferences sharedPreferences = getActivity().getSharedPreferences("wujay", Context.MODE_PRIVATE); //私有数据
				Editor editor = sharedPreferences.edit();//获取编辑器
				editor.putString("local_user_name", user.getDisplayName());
				editor.commit();//提交修改

				userID.setText("ID: "+SP_ID.id);
				String headurl=user.getHeadsculpture();
				int videosCount=user.getVideosCount();
				int fansCount=user.getFansCount();
				int attentionsCount=user.getAttentionsCount();
				int certificationState =user.getCertificationState();
				int auth=user.getShopAuditStatus();
				
//				switch (auth) {
//				case 0:
//					authentication.setImageResource(R.drawable.icon_me_not_verified);
//					verifiedTV.setImageResource(R.drawable.icon_me_not_verified);
//					break;
//				case 1:
//					authentication.setImageResource(R.drawable.icon_renzhenging);
//					verifiedTV.setImageResource(R.drawable.icon_renzhenging);
//					break;
//				case 2:
//					authentication.setImageResource(R.drawable.icon_renzhenged);
//					verifiedTV.setImageResource(R.drawable.icon_renzhenged);
//					iv_v.setVisibility(View.VISIBLE);
//					break;
//				default:
//					break;
//				}
				
				
//				if(!StringUtils.isEmpty(headurl)){
					imageLoader.displayImage(headurl, avarIV, options);
//				}
				works.setText(videosCount+"\n作品");
				fans.setText(fansCount+"\n粉丝");
				attention.setText(attentionsCount+"\n关注");
				switch (certificationState) {
				case 0:
					authentication.setImageResource(R.drawable.icon_me_not_verified);
					verifiedTV.setImageResource(R.drawable.icon_me_not_verified);
					break;
				case 1:
					authentication.setImageResource(R.drawable.icon_renzhenging);
					verifiedTV.setImageResource(R.drawable.icon_renzhenging);
					break;
				case 2:
					authentication.setImageResource(R.drawable.icon_renzhenged);
					verifiedTV.setImageResource(R.drawable.icon_renzhenged);
					iv_v.setVisibility(View.VISIBLE);
					break;
				default:
					break;
				}
				
			}
		}
	}
	class LoginTask extends AsyncTask<String, String, String>{
		String openID;
		Platform platform;
		int type;
		
		public LoginTask(Platform platform,String openID){
			this.openID=openID;
			this.platform=platform;
			if(QQ.NAME.equals(platform.getName())){//qq
				this.type=TYPE_QQ;
			}else if(Wechat.NAME.equals(platform.getName())){//微信
				this.type=TYPE_WECHAT;
			}else if(SinaWeibo.NAME.equals(platform.getName())){//新浪
				this.type=TYPE_SINA;
			}
		}
		@Override
		protected String doInBackground(String... arg0) {
			UserInfo udb=ApiClientC.loginClient(openID, type);
			if(udb!=null){
				if(StringUtils.isEmpty(udb.getDisplayName())){
					isReg=true;
				}else{
					isReg=false;
				}
				
				return udb.getID()+"";
			}
			
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if(result!=null){
				SP_ID.id=result;
				SPutil.put(pActivity, Constant.ONLINE_ID, result);
				platform.showUser(null);
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
		UIHandler.sendMessage(msg, MeFragment.this);
	}
	@Override
	public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
		// TODO Auto-generated method stub
		
		if(arg1==Platform.ACTION_USER_INFOR){
			Message msg2 = new Message();
			msg2.what = 1;
			int type=0;
			if(QQ.NAME.equals(arg0.getName())){//qq
				type=TYPE_QQ;
			}else if(Wechat.NAME.equals(arg0.getName())){//微信
				type=TYPE_WECHAT;
			}else if(SinaWeibo.NAME.equals(arg0.getName())){//新浪
				type=TYPE_SINA;
			}
			msg2.arg1=type;
			msg2.obj = arg2;
			UIHandler.sendMessage(msg2, this);
		}else if(arg1==Platform.ACTION_AUTHORIZING){
			Message msg = new Message();
			msg.arg1 = 1;
			msg.arg2 = arg1;
			msg.obj = arg0;
			UIHandler.sendMessage(msg, MeFragment.this);
		}
	}
	@Override
	public void onError(Platform arg0, int arg1, Throwable arg2) {
		// TODO Auto-generated method stub
		Message msg = new Message();
		msg.arg1 = 2;
		msg.arg2 = arg1;
		msg.obj = arg0;
		UIHandler.sendMessage(msg, MeFragment.this);
	}
	@Override
	public boolean handleMessage(Message msg) {
		
		if(msg.what==1){//更新用户资料
			String ss=format("",(HashMap<String, Object>)(msg.obj));
			UserData myUser=new UserData();
			myUser.setId(Integer.parseInt(SP_ID.id));
			if(msg.arg1==TYPE_QQ){
				try {
					JSONObject obj=new JSONObject(ss);
					if(obj.has("nickname")){//名字
						myUser.setDisplayName(obj.getString("nickname"));
					}
					if(obj.has("figureurl_qq_1")){//头像
						myUser.setHeadsculpture(obj.getString("figureurl_qq_1"));
					}
					String province="";
					String city="";
					if(obj.has("province")){//省
						province=obj.getString("province");
					}
					if(obj.has("city")){//市
						city=(obj.getString("city"));
					}
					myUser.setAear(province+" "+city);
					if(obj.has("gender")){//性别
						if("男".equals(obj.getString("gender"))){
							myUser.setSex(Constant.SEX_M);
						}else{
							myUser.setSex(Constant.SEX_F);
						}
						
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}else if(msg.arg1==TYPE_WECHAT){
				
			}else if(msg.arg1==TYPE_SINA){
				try {
					JSONObject obj=new JSONObject(ss);
					if(obj.has("name")){//名字
						myUser.setDisplayName(obj.getString("name"));
					}
					if(obj.has("avatar_hd")){//头像
						myUser.setHeadsculpture(obj.getString("avatar_hd"));
					}
					if(obj.has("location")){
						
						myUser.setAear(obj.getString("location"));
					}
					if(obj.has("gender")){//性别
						if("m".equals(obj.getString("gender"))){
							myUser.setSex(Constant.SEX_M);
						}else{
							myUser.setSex(Constant.SEX_F);
						}
						
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(isReg){
				new UpdateUserTask(myUser).execute("");
			}else{
				new UserInfoTask().execute("");
			}
			
			L.e("............."+ss);
			return false;
		}else{
			Platform plat = (Platform) msg.obj;
			String text = actionToString(msg.arg2);
			switch (msg.arg1) {
			case 1: {
				// 成功
//				text = plat.getName() + " completed at " + text+plat.getDb().getUserId();
//				ToastUtils.showToast(pActivity, text);
				String openID=plat.getDb().getUserId();
				L.e("openID::::::::::"+openID);
				
				if(openID!=null)
				new LoginTask(plat,openID).execute("");
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
		}
		
		return false;
	}
	
	
	public static String actionToString(int action) {
		switch (action) {
			case Platform.ACTION_AUTHORIZING: return "ACTION_AUTHORIZING";
			case Platform.ACTION_GETTING_FRIEND_LIST: return "ACTION_GETTING_FRIEND_LIST";
			case Platform.ACTION_FOLLOWING_USER: return "ACTION_FOLLOWING_USER";
			case Platform.ACTION_SENDING_DIRECT_MESSAGE: return "ACTION_SENDING_DIRECT_MESSAGE";
			case Platform.ACTION_TIMELINE: return "ACTION_TIMELINE";
			case Platform.ACTION_USER_INFOR: return "ACTION_USER_INFOR";
			case Platform.ACTION_SHARE: return "ACTION_SHARE";
			default: {
				return "UNKNOWN";
			}
		}
	}
	
	private String format(String sepStr, HashMap<String, Object> map) {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		String mySepStr = sepStr + "";
		int i = 0;
		for (Entry<String, Object> entry : map.entrySet()) {
			if("source".equals(entry.getKey())){
				continue;
			}
			if (i > 0) {
				sb.append(",");
			}
			sb.append(mySepStr).append('\"').append(entry.getKey()).append("\":");
			Object value = entry.getValue();
			if (value instanceof HashMap<?, ?>) {
				sb.append(format(mySepStr, (HashMap<String, Object>)value));
			}
			else if (value instanceof ArrayList<?>) {
				sb.append(format(mySepStr, (ArrayList<Object>)value));
			}
			else if (value instanceof String) {
				sb.append('\"').append(value).append('\"');
			}
			else {
				sb.append(value);
			}
			i++;
		}
		sb.append(sepStr).append('}');
		return sb.toString();
	}
	private String format(String sepStr, ArrayList<Object> list) {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		String mySepStr = sepStr + "";
		int i = 0;
		for (Object value : list) {
			if (i > 0) {
				sb.append(",");
			}
			sb.append(mySepStr);
			if (value instanceof HashMap<?, ?>) {
				sb.append(format(mySepStr, (HashMap<String, Object>)value));
			}
			else if (value instanceof ArrayList<?>) {
				sb.append(format(mySepStr, (ArrayList<Object>)value));
			}
			else if (value instanceof String) {
				sb.append('\"').append(value).append('\"');
			}
			else {
				sb.append(value);
			}
			i++;
		}
		sb.append(sepStr).append(']');
		return sb.toString();
	}
	
	class UpdateUserTask extends AsyncTask<String, String, String> {
		String code;
		UserData user;
		public UpdateUserTask(UserData user) {
			this.user=user;
		}
		@Override
		protected String doInBackground(String... arg0) {
			code = ApiClientC.updateUser(this.user);
			
			return null;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub

			
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			L.e("code:::::::::::::::::::::::::" + code);
			if (code != null && code.equals("1")) {
				UIHelper.showMsg(pActivity, "修改成功");
				new UserInfoTask().execute("");
			} else {
				UIHelper.showMsg(pActivity, "修改失败");
			}

		}
	}
	
	class GetUnPublishVideosTask extends AsyncTask<String, String, String> {
		VideosDataList videoList;

		@Override
		protected String doInBackground(String... arg0) {
			videoList = ApiClientC.getUpPublishVideosById(SP_ID.id, 1);

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
				count.setText(""+videoList.getTotalCounts());
			}

		}
	}
		
}
