package com.www.goumei.fragment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.www.goumei.R;
import com.www.goumei.activity.MainMyAct;
import com.www.goumei.bean.UserInfo;
import com.www.goumei.http.ApiClient;
import com.www.goumei.http.req.SP_ID;
import com.www.goumei.utils.Constant;
import com.www.goumei.utils.ProcessUtil;
import com.www.goumei.utils.SPutil;
import com.www.goumei.utils.UIHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginFragment extends BaseFragment implements OnClickListener {
	private final static int LOGIN_TAG = 1301;
	/** 登录失败，输入错误 */
	private final static int LOGIN_ERROR = 1302;
	/** 登录成功，输入正确 */
	private final static int LOGIN_CORRECT = 1303;
	private TextView tv_prior;
	private TextView tv_title;
	private TextView tv_later;
	private Intent intent;
	private EditText tel_num;
	private EditText pwd;
	private TextView forget_pwd;
	private Button btn_submit;
	private String phoneNum;
	private String passWord;
	/** 验证登录返回的用户信息 */
	private static UserInfo info;
	String phoneNumberStr = "";
	Activity pActivity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_login, container, false);
		tv_prior = (TextView) view.findViewById(R.id.tv_prior);
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		tv_later = (TextView) view.findViewById(R.id.tv_later);
		tel_num = (EditText) view.findViewById(R.id.tel_num);
		forget_pwd = (TextView) view.findViewById(R.id.forget_pwd);
		btn_submit = (Button) view.findViewById(R.id.btn_submit);
		pwd = (EditText) view.findViewById(R.id.pwd);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		addListener();
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.pActivity = activity;
	}

	private void initView() {

	}

	private void addListener() {
		tv_prior.setOnClickListener(this);
		tv_later.setOnClickListener(this);
		btn_submit.setOnClickListener(this);
		forget_pwd.setOnClickListener(this);
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			// 登录失败
			case LOGIN_ERROR:
				ProcessUtil.dismissProgressdialog();
				UIHelper.showMsg(pActivity, "您的帐号或密码有误，请重新输入");

				break;
			// 登录成功
			case LOGIN_CORRECT:
				ProcessUtil.dismissProgressdialog();
				if (info != null) {
					MeFragment meFragment = new MeFragment();
					Bundle bundle = new Bundle();
					bundle.putBoolean("is_login", true);
					bundle.putInt("iD", info.getID());
					String result = info.getID()+"";
					SP_ID.id = result;
					SPutil.put(pActivity, Constant.ONLINE_ID, result);
					((MainMyAct) pActivity).goHomeView();
					((MainMyAct) pActivity)
							.dumpToNext(meFragment, "MeFragment");
					
					SharedPreferences sharedPreferences = getActivity().getSharedPreferences("wujay", Context.MODE_PRIVATE); //私有数据
					Editor editor = sharedPreferences.edit();//获取编辑器
					editor.putInt("local_user_id", info.getID());
					editor.commit();//提交修改
				}
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_prior:
			((MainMyAct) pActivity).goBackAndShowPreView(null);
			break;

		case R.id.tv_later:
			RegisterFragment registerFragment = new RegisterFragment();

			((MainMyAct) pActivity).dumpToNext(registerFragment,
					"RegisterFragment");

			break;

		case R.id.forget_pwd:

			break;

		case R.id.btn_submit:
			String numString = tel_num.getText().toString().trim();
			String pass = pwd.getText().toString().trim();
			if (TextUtils.isEmpty(numString) || TextUtils.isEmpty(pass)) {
				UIHelper.showMsg(pActivity, "请输入正确规格的手机号或者密码");

				return;
			} else {
				ProcessUtil.showProgressDialog(pActivity, "");
				loginTask task = new loginTask(numString, pass);
				task.execute(LOGIN_TAG);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 检查手机号码格式是否正确
	 * 
	 * @return
	 */
	private boolean checkPhoneNum() {
		phoneNum = tel_num.getText().toString().trim();
		if (!TextUtils.isEmpty(phoneNum)) {
			Pattern p = Pattern.compile("[1][358]\\d{9}");
			Matcher m = p.matcher(phoneNum);
			return m.matches();
		}
		return false;
	}

	/**
	 * 判断是否填写了东西
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		passWord = pwd.getText().toString().trim();
		return (!TextUtils.isEmpty(passWord)) && checkPhoneNum();

	}

	class loginTask extends AsyncTask<Integer, Void, Integer> {

		private String number;
		private String pass;

		public loginTask(String number, String pass) {
			this.number = number;
			this.pass = pass;
		}

		@Override
		protected Integer doInBackground(Integer... params) {
			switch (params[0]) {
			case LOGIN_TAG:
				info = ApiClient.loginClient(number, pass);
				if (info == null) {
					handler.sendEmptyMessage(LOGIN_ERROR);
				} else {
					handler.sendEmptyMessage(LOGIN_CORRECT);
				}
				break;

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
}
