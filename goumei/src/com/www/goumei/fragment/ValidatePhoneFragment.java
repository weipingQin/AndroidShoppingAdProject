package com.www.goumei.fragment;

import com.www.goumei.R;
import com.www.goumei.activity.MainMyAct;
import com.www.goumei.http.ApiClient;
import com.www.goumei.http.req.ApiClientC;
import com.www.goumei.http.req.SP_ID;
import com.www.goumei.utils.Constant;
import com.www.goumei.utils.L;
import com.www.goumei.utils.UIHelper;
import com.yixia.camera.demo.utils.ToastUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 验证手机号
 * 
 * @author Eric.Chen
 * 
 */
public class ValidatePhoneFragment extends BaseFragment implements
		OnClickListener {
	private final static int USER_REGISTER = 1601;
	private final static int USER_REGISTER_OK = 1602;
	private final static int USER_REGISTER_REPEAT=1603;
	private TextView tv_prior;
	private TextView tv_title;
	private EditText verificationCode;
	private Button btnRepeat;
	private TextView tv_later;
	private TextView myNumber;
	Activity pActivity;
	TimeCount time;
	// 用户注册的验证码、用户名、密码
	private static String code;
	private static String username;
	private static String password;
	/**是否重发*/
	private static boolean isRepeat=true;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View view = inflater.inflate(R.layout.activity_validatephone,
				container, false);
		tv_prior = (TextView) view.findViewById(R.id.tv_prior);
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		tv_later = (TextView) view.findViewById(R.id.tv_later);
		myNumber = (TextView) view.findViewById(R.id.myNumber);
		verificationCode = (EditText) view.findViewById(R.id.VerificationCode);
		btnRepeat = (Button) view.findViewById(R.id.btnRepeat);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		initView();
		addListener();
		Bundle bundle = getArguments();
		if (bundle != null) {
			username = bundle.getString("number");
			password = bundle.getString("pass");
			code = bundle.getString("code");
			Log.e("TAG", "验证码：" + code);
			myNumber.setText("+86  " + username);

		}

		time = new TimeCount(59000, 1000);// 构造CountDownTimer对象
		time.start();// 开始到时
		btnRepeat.setBackgroundResource(R.drawable.bt_setting_huisebg);
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

	}

	private void initView() {

		tv_title.setText("验证手机号");
		tv_later.setText("下一步");
	}

	private void addListener() {
		tv_prior.setOnClickListener(this);
		tv_later.setOnClickListener(this);
		btnRepeat.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_prior:
			((MainMyAct) pActivity).goBackAndShowPreView(null);
			break;

		case R.id.tv_later:
			String myCode = verificationCode.getText().toString();
			if (myCode.length() < 3) {
				UIHelper.showMsg(pActivity, "请输入完整的验证码");
			} else {
				Log.e("TAG", myCode + "--" + code);
				//验证短信码
				new ValidateMessageTask(username,myCode).execute("");
					
			}
			break;

		case R.id.btnRepeat:
			if (isRepeat) {
				task=new userTask();
				task.execute(USER_REGISTER_REPEAT);
				btnRepeat.setBackgroundColor(Color.parseColor("#ada2a1"));
				time = new TimeCount(59000, 1000);// 构造CountDownTimer对象
				time.start();// 开始到时
			}
			break;
		default:
			break;
		}
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case USER_REGISTER_OK:
				int result = msg.arg1;
				if (result > 0) {
					UIHelper.showMsg(pActivity, "注册成功！");
					LoginFragment loginFragment = new LoginFragment();
					((MainMyAct) pActivity).dumpToNext(loginFragment,
							"LoginFragment");
				} else {
					UIHelper.showMsg(pActivity, "注册失败！");
				}
				break;

			default:
				break;
			}
		}
	};
	private userTask task;

	class userTask extends AsyncTask<Integer, Void, Integer> {

		@Override
		protected Integer doInBackground(Integer... params) {
			switch (params[0]) {
			case USER_REGISTER:
				int result = ApiClient.RegisterUser(username, password);
				Message msg = new Message();
				msg.what = USER_REGISTER_OK;
				msg.arg1 = result;
				handler.sendMessage(msg);
				return USER_REGISTER;

			case USER_REGISTER_REPEAT:
				code = ApiClient.GetShortMessageCode(username);
				return USER_REGISTER_REPEAT;
				
			default:
				break;
			}
			return null;
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

	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			btnRepeat.setText("重新获取验证码");
			btnRepeat.setClickable(true);
			btnRepeat.setBackgroundResource(R.drawable.bt_setting_bg);
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
			btnRepeat.setClickable(false);
			btnRepeat
					.setText("重新发送"
							+ Html.fromHtml("<font color='red'>"
									+ millisUntilFinished / 1000 + "</font>"
									+ "<font color='black'>s</font>"));
		}
	}
	
	class ValidateMessageTask extends AsyncTask<String, String, String>{
		String coderesult;
		String telphone;
		String code;
		public ValidateMessageTask(String telphone,String code){
			this.telphone=telphone;
			this.code=code;
		}
		@Override
		protected String doInBackground(String... arg0) {
			coderesult=ApiClientC.validateMessage(telphone, code);
			
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			L.e("code:::::::::::::::::::::::::"+coderesult);
			if(coderesult!=null &&coderesult.equals("1")){
				// 注册用户
				task = new userTask();
				task.execute(USER_REGISTER);
			}else{
				ToastUtils.showToast(pActivity, "验证码过期或不匹配");
			}
			
		}
	}

}
